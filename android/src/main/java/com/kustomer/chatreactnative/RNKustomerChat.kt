package com.kustomer.chatreactnative

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.facebook.react.util.RNLog
import com.kustomer.core.BuildConfig
import com.kustomer.core.KustomerCore
import com.kustomer.core.exception.KusNotInitializedException
import com.kustomer.core.listeners.KusChatListener
import com.kustomer.core.models.KusInitialMessage
import com.kustomer.core.models.KusPreferredView
import com.kustomer.core.models.KusResult
import com.kustomer.core.models.chat.*
import com.kustomer.core.models.kb.KusKbArticle
import com.kustomer.core.utils.log.KusLogOptions
import com.kustomer.ui.Kustomer
import com.kustomer.ui.KustomerOptions
import com.kustomer.ui.model.KusDescribeAttributes
import com.kustomer.ui.utils.helpers.KusNotificationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.*

@SuppressWarnings("unused")
class RNKustomerChat(reactContext: ReactApplicationContext) :
  RNKustomerChatSpec(reactContext) {

  override fun getName(): String = NAME

  companion object {
    const val NAME = "RNKustomerChat"
    const val TAG = "RNKus"
    var isInitialized = false
  }

  override fun getConstants() = mapOf(
    "sdkVersion" to BuildConfig.VERSION_NAME,
    "sdkBuild" to BuildConfig.VERSION_CODE,
  )

  lateinit var liveUnreadCount: LiveData<Int>
  lateinit var liveActiveConversationIds: LiveData<Set<String>>
  var clientStatus = KustomerClientStatus.UNINITIALIZED
  var chatListener: KusChatListener? = null

  @Deprecated("Deprecated in Java")
  override fun onCatalystInstanceDestroy() { try {
    chatListener?.let{Kustomer.getInstance().removeChatListener(it)}
  } catch (_: KusNotInitializedException) {} }

  @ReactMethod
  fun isConfigured(promise: Promise) {
    promise.resolve(isInitialized)
  }

  @ReactMethod
  fun configure(apiKey: String, options: ReadableMap, promise: Promise) {
    if (isInitialized) {
      initRn()
      RNLog.w(reactApplicationContext, "Kustomer is already initialized. We're not checking if different configuration is supplied.")
      if (clientStatus == KustomerClientStatus.UNINITIALIZED) {
        clientStatus = KustomerClientStatus.STARTED
      }
      promise.resolve(null)
      return
    }

    val builder = KustomerOptions.Builder()

    if (options.hasKey("enableUI"))
      Log.w("$TAG::_CONFIGURE", "enableUI can not be turned off just yet.")
    if (options.hasKey("showInAppNotifications"))
      options.getBoolean("showInAppNotifications").let(builder::showInAppNotifications)
    options.getString("language")?.let { builder.setUserLocale(Locale.forLanguageTag(it)) }
    options.getString("activeAssistant")?.let {
      builder.setChatAssistant(
        when (it) {
          "none" -> KusActiveAssistant.None
          "orgDefault" -> {
            Log.w(
              "$TAG::CONFIGURE", "Unable to set `activeAssistant` to `orgDefault` on Android. Fallback: `none`"
            )
            KusActiveAssistant.None
          }
          else -> KusActiveAssistant.WithId(it)
        }
      )
    }
    options.getString("hostDomain")?.let(builder::setHostDomain)
    options.getString("businessScheduleId")?.let(builder::setBusinessScheduleId)
    options.getString("brandId")?.let(builder::setBrandId)
    builder.setLogLevel(
      when (options.getString("logLevel")) {
        "info" -> KusLogOptions.KusLogOptionInfo
        "error" -> KusLogOptions.KusLogOptionErrors
        "debug" -> KusLogOptions.KusLogOptionDebug
        "warning" -> KusLogOptions.KusLogOptionAll
        else -> KusLogOptions.KusLogOptionAll
      }
    )
    builder.hideNewConversationButton(
      options.hasKey("hideNewConversationButton") && options.getBoolean(
        "hideNewConversationButton"
      )
    )
    builder.hideKbArticleShare(options.hasKey("hideKbArticleShare") && options.getBoolean("hideKbArticleShare"))
    builder.hideHistoryNavigation(options.hasKey("hideHistoryNavigation") && !options.getBoolean("hideHistoryNavigation"))

    Kustomer.init(
      reactApplicationContext.applicationContext as Application,
      apiKey,
      builder.build()
    ) {
      if (it.dataOrNull == true) {
        initRn()
        promise.resolve(null)
      } else {
        clientStatus = KustomerClientStatus.CONFIGURATION_ERROR
        promise.reject("$TAG::CONFIG", "Kustomer configure error: unknown")
      }
    }
  }

  fun initRn() {
    isInitialized = true
    KustomerCore.getInstance().kusChatProvider()
    clientStatus = KustomerClientStatus.STARTED
    val k = Kustomer.getInstance()
    liveUnreadCount = k.observeUnreadCount()
    liveActiveConversationIds = k.observeActiveConversationIds()
    Handler(Looper.getMainLooper()).post {
      liveActiveConversationIds.observeForever { v ->
        sendEvent(
          "onOpenConversationCountChanged", Arguments.makeNativeMap(
            mapOf("count" to v.size)
          )
        )
      }
    }
    Handler(Looper.getMainLooper()).post {
      liveUnreadCount.observeForever { v ->
        sendEvent(
          "onUnreadTotalChange", Arguments.makeNativeMap(mapOf("count" to v))
        )
      }

    }
    chatListener = RNKustomerChatListener(this::sendEvent, this.reactApplicationContext)
    Kustomer.getInstance().addChatListener(chatListener as RNKustomerChatListener)
//    if (options.hasKey("disablePush")) options.getBoolean("disablePush").let { disable ->
//      if (disable) unsetPushToken(null) // todo
//    }
  }

  @ReactMethod
  fun show(displayMode: String, promise: Promise) = try {
    if (displayMode === "newChat") {
      Kustomer.getInstance().startNewConversation()
    } else {
      val kusPreferredView: KusPreferredView = when (displayMode) {
        "chatHistory" -> KusPreferredView.CHAT_HISTORY
        "default" -> KusPreferredView.DEFAULT
        "knowledgeBaseHome" -> KusPreferredView.KNOWLEDGE_BASE_HOME
        "onlyChat" -> KusPreferredView.CHAT_ONLY
        "onlyKnowledgeBase" -> KusPreferredView.KB_ONLY
        else -> {
          Log.w(
            "$TAG::SHOW",
            "Unsupported `displayMode` supplied to `.show`: $displayMode - `default` is used instead."
          )
          KusPreferredView.DEFAULT
        }
      }
      Kustomer.getInstance().open(kusPreferredView)
    }
  } catch (e: RuntimeException) {
    promise.reject("$TAG::SHOW", e)
  }

  @ReactMethod(isBlockingSynchronousMethod = true)
  fun isVisibleSync(): Boolean = try {
    !Kustomer.isBackground()
  } catch (_: KusNotInitializedException) {
    false
  }

  @ReactMethod
  fun isVisible(promise: Promise) = try {
    promise.resolve(!Kustomer.isBackground())
  } catch (e: KusNotInitializedException) {
    promise.reject("$TAG::IS_VISIBLE", e)
  }

  @ReactMethod(isBlockingSynchronousMethod = true)
  fun getStatusSync(): String = clientStatus.jsName

  @ReactMethod
  fun getStatus(promise: Promise) = promise.resolve(clientStatus.jsName)

  @ReactMethod
  fun changeActiveAssistant(assistant: String, promise: Promise) = try {
    CoroutineScope(Dispatchers.Default).launch {
      when (val result = Kustomer.getInstance().overrideAssistant(assistant)) {
          is KusResult.Success -> promise.resolve(toWritableKustomerAssistant(result.data))
          is KusResult.Error -> promise.reject(
            "$TAG::CHANGE_ACTIVE_ASSISTANT", result.exception.localizedMessage
          )

          else -> promise.reject(
            "$TAG::CHANGE_ACTIVE_ASSISTANT", result.toString()
          )
      }
    }
  } catch (e: KusNotInitializedException) {
    promise.reject("$TAG::CHANGE_ACTIVE_ASSISTANT", e)
  }

  @ReactMethod
  fun openConversation(conversationId: String, promise: Promise) = try {
    Kustomer.getInstance()
      .openConversationWithId(conversationId) { result: KusResult<KusConversation> ->
        when (result) {
          is KusResult.Success -> promise.resolve(toWritableKustomerConversation(result.data))
          is KusResult.Error -> promise.reject(
            "$TAG::OPEN_CONVERSATION", result.exception.localizedMessage
          )
          else -> promise.reject("$TAG::OPEN_CONVERSATION", result.toString())
        }
      }
  } catch (e: KusNotInitializedException) {
    promise.reject("$TAG::OPEN_CONVERSATION", e)
  }

  @ReactMethod
  fun showKbArticle(articleId: String, promise: Promise) = try {
    Kustomer.getInstance().openKbArticle(articleId) { result: KusResult<KusKbArticle> ->
      when (result) {
        is KusResult.Success -> promise.resolve(toWritableKustomerKbArticle(result.data))
        is KusResult.Error -> promise.reject(
          "$TAG::SHOW_KB_ARTICLE",
          result.exception.localizedMessage
        )
        else -> promise.reject("$TAG::SHOW_KB_ARTICLE", result.toString())
      }
    }
  } catch (e: KusNotInitializedException) {
    promise.reject("$TAG::SHOW_KB_ARTICLE", e)
  }

  @ReactMethod
  fun showKbCategory(categoryId: String, promise: Promise) = try {
    Kustomer.getInstance().openKbCategory(categoryId) { result: KusResult<KusKbArticle> ->
      when (result) {
        is KusResult.Success -> promise.resolve(toWritableKustomerKbArticle(result.data))
        is KusResult.Error -> promise.reject(
          "$TAG::SHOW_KB_CATEGORY", result.exception.localizedMessage
        )
        else -> promise.reject(
          "$TAG::SHOW_KB_CATEGORY", result.toString()
        )
      }
    }
  } catch (e: KusNotInitializedException) {
    promise.reject("$TAG::SHOW_KB_CATEGORY", e)
  }

  @ReactMethod
  fun close(
    animated: Boolean?, promise: Promise
  ) {
    if (animated == true) Log.w(
      "$TAG::NO_ANIM", "The `animated` parameter is noop on android."
    )
    Log.w(
      "$TAG::NO_CLOSE",
      "There is no way to close the Kustomer Activity on Android for now."
    )
  }

  @ReactMethod
  override fun isLoggedIn(idType: String, id: String, promise: Promise) = try {
    val userEmail = if (idType == "email") id else null
    val userId = if (idType == "id") id else null
    when (val it = Kustomer.getInstance().isLoggedIn(userEmail, userId)) {
      is KusResult.Success -> promise.resolve(it.data)
      is KusResult.Error -> promise.reject(
        "$TAG::IS_LOGGED_IN", it.exception.localizedMessage
      )
      else -> promise.reject("$TAG::IS_LOGGED_IN", "Unknown error")
    }
  } catch (e: KusNotInitializedException) {
    promise.reject("$TAG::IS_LOGGED_IN", e)
  }

  @ReactMethod
  override fun logOut(promise: Promise) = try {
    Kustomer.getInstance().logOut()
    promise.resolve(null)
  } catch (e: KusNotInitializedException) {
    promise.reject("$TAG::LOG_OUT", e)
  }

  @ReactMethod
  override fun logIn(jwt: String, promise: Promise) = try {
    Kustomer.getInstance().logIn(jwt) {
      when (it) {
        is KusResult.Success -> promise.resolve(toWritableKustomerTrackingIdentity(it.data))
        is KusResult.Error -> promise.reject("$TAG::LOG_IN", it.exception.localizedMessage)
        else -> promise.reject("$TAG::LOG_IN", "Unknown error")
      }
    }
  } catch (e: KusNotInitializedException) {
    promise.reject("$TAG::LOG_IN", e)
  }

  @ReactMethod
  override fun logOutThenLogIn(jwt: String, promise: Promise) = try {
    Kustomer.getInstance().logOut()
    logIn(jwt, promise)
  } catch (e: KusNotInitializedException) {
    promise.reject("$TAG::LOG_OUT_THEN_LOG_IN", e)
  }

  @ReactMethod
  override fun startNewConversation(
    initialMessage: ReadableMap?,
    customDescribe: ReadableMap?,
    afterCreateConversation: Callback?,
    animated: Boolean?
  ): Unit = try {
    if (animated == true) Log.w(
      "$TAG::NO_ANIMATED", "The `animated` parameter is noop on android."
    )
    val body = initialMessage?.getString("body")
    val direction: KusChatMessageDirection = when (initialMessage?.getString("direction")) {
      "user" -> KusChatMessageDirection.CUSTOMER
      else -> KusChatMessageDirection.AGENT
    }
    Kustomer.getInstance().startNewConversation(
      initialMessage = body?.let { KusInitialMessage(body, direction) },
      describeAttributes = customDescribe?.toHashMap()?.let(::KusDescribeAttributes),
      onResponse = { result: KusResult<KusConversation> ->
        when (result) {
          is KusResult.Success -> afterCreateConversation?.invoke(
            toWritableKustomerConversation(
              result.data
            )
          )
          is KusResult.Error -> Log.e(
            "$TAG::START_NEW_CNV",
            result.exception.localizedMessage ?: "Unknown error"
          )
          else -> Log.e(
            "$TAG::START_NEW_CNV",
            "Unknown error"
          )
        }
      }
    )
  } catch (e: KusNotInitializedException) {
    Log.e("$TAG::START_NEW_CNV", e.localizedMessage ?: "Unknown error")
    RNLog.e(this.reactApplicationContext, "$TAG::START_NEW_CNV " + (e.localizedMessage ?: "Unknown error"))
  }

  @ReactMethod
  override fun start(promise: Promise) = promise.resolve(null) // iOS stub

  @ReactMethod
  override fun openChatAssistant(
    chatAssistantId: String,
    startDialog: String?,
    initialMessage: String?,
    customDescribe: ReadableMap?,
    afterFirstMessage: Callback?,
    animated: Boolean?,
    promise: Promise
  ) = try {
    RNLog.e(this.reactApplicationContext, "$TAG::OPEN_CHAT_ASSISTANT Not implemented")
    promise.reject("$TAG::OPEN_CHAT_ASSISTANT", "Not implemented")
//    if (animated == true) Log.w(
//      "$TAG::NO_ANIMATED", "The `animated` parameter is noop on android."
//    )
//    Kustomer.getInstance().overrideAssistant(chatAssistantId) {
//      when (it) {
//        is KusResult.Success -> {
//          startNewConversation(initialMessage, customDescribe, afterFirstMessage, animated)
//          promise.resolve(null)
//        }
//        is KusResult.Error -> promise.reject(
//          "$TAG::OPEN_CHAT_ASSISTANT", it.exception.localizedMessage
//        )
//        else -> promise.reject(
//          "$TAG::OPEN_CHAT_ASSISTANT", "Unknown error"
//        )
//      }
//    }
  } catch (e: KusNotInitializedException) {
    promise.reject("$TAG::OPEN_CHAT_ASSISTANT", e)
  }

  @ReactMethod
  override fun getUnreadCount(promise: Promise) =
    promise.resolve(liveUnreadCount.value)

  @ReactMethod
  override fun getOpenConversationCount(promise: Promise) =
    promise.resolve(liveActiveConversationIds.value?.size ?: 0)

  @ReactMethod
  override fun setPushToken(token: String, promise: Promise) {
    CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch {
      when (val it: KusResult<Boolean> = Kustomer.getInstance().registerDeviceToken(token)) {
        is KusResult.Success -> promise.resolve(it.data)
        is KusResult.Error -> promise.reject(
          "$TAG::REGISTER_DEVICE_TOKEN", it.exception.localizedMessage, it.exception
        )
        else -> promise.reject(
          "$TAG::REGISTER_DEVICE_TOKEN", "Unknown error"
        )
      }
    }
  }

  @ReactMethod
  override fun unsetPushToken(promise: Promise?) {
    CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch {
      when (val it = Kustomer.getInstance().deregisterDeviceForPushNotifications()) {
        is KusResult.Success -> promise?.resolve(it.data)
        is KusResult.Error -> promise?.reject(
          "$TAG::DEREGISTER_DEVICE_TOKEN", it.exception.localizedMessage, it.exception
        )
        else -> promise?.reject(
          "$TAG::DEREGISTER_DEVICE_TOKEN", "Unknown error"
        )
      }
    }
  }

  @ReactMethod
  override fun onRemoteMessage(remoteMessage: ReadableMap, promise: Promise) = try {
    promise.resolve(
      KusNotificationService.onMessageReceived(
        remoteMessageFromReadableMap(remoteMessage), reactApplicationContext, null
      )
    )
  } catch (e: KusNotInitializedException) {
    promise.reject("$TAG::ON_REMOTE_MESSAGE", e)
  }

  @ReactMethod
  fun addListener(eventName: String) = Unit // iOS stub

  @ReactMethod
  fun removeListeners(count: Int) = Unit // iOS stub

  private fun sendEvent(eventName: String, params: WritableMap) {
    Log.d("$TAG::SEND_EVENT", "$eventName: $params")
    reactApplicationContext?.getJSModule(
      DeviceEventManagerModule.RCTDeviceEventEmitter::class.java
    )?.emit(eventName, params)
  }

  enum class KustomerClientStatus(val jsName: String) {
    UNINITIALIZED("uninitialized"), STARTED("started"), CONFIGURATION_ERROR("configurationError"),
  }
}
