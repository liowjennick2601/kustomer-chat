package com.kustomer.chatreactnative.providers

import com.facebook.react.bridge.*
import com.kustomer.chatreactnative.*
import com.kustomer.core.KustomerCore
import com.kustomer.core.exception.KusNotInitializedException
import com.kustomer.core.models.KusResult
import com.kustomer.core.models.KusChatSetting
import com.kustomer.core.models.chat.*
import com.kustomer.core.models.pubnub.KusPresenceEvent
import com.kustomer.ui.Kustomer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class RNKustomerChatProvider(reactContext: ReactApplicationContext) :
  RNKustomerChatProviderSpec(reactContext) {
  companion object {
    const val NAME = "RNKustomerChatProvider"
  }
  override fun getName() = NAME

  @ReactMethod
  override fun createConversation(
    newConversationOptions: ReadableMap,
    promise: Promise
  ) {
    CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch {
      val title: String? = newConversationOptions.getString("title")
      val messages: ReadableArray? = newConversationOptions.getArray("messages")
      val messageList: List<String> = ArrayUtil.toArray(messages).asList().map { it as String }

      when (val it: KusResult<Pair<KusConversation, List<KusChatMessage>>> = KustomerCore.getInstance().kusChatProvider().createConversation(
        title, messageList, listOf()
      )) {
        is KusResult.Success -> {
          val (conversation, ) = it.data
          val payload: WritableMap = toWritableKustomerConversation(conversation)
          promise.resolve(payload)
        }
        is KusResult.Error -> promise.reject(
          "$NAME::CREATE_CONVERSATION_ERROR",
          it.exception.localizedMessage, it.exception
        )
        else -> promise.reject(
          "$NAME::CREATE_CONVERSATION_ERROR",
          "Unknown error"
        )
      }
    }
  }

  @ReactMethod
  fun fetchConversations(page: Double, pageSize: Double, promise: Promise) {
    CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch {
      when (val it: KusResult<List<KusConversation>> = KustomerCore.getInstance().kusChatProvider().fetchConversations(page.toInt(), pageSize.toInt())) {
        is KusResult.Success -> {
          val arr: Array<WritableMap> = it.data.map { conversation -> toWritableKustomerConversation(conversation) }.toTypedArray()
          val payload: WritableArray = ArrayUtil.toWritableArray(arr)
          promise.resolve(payload)
        }
        is KusResult.Error -> promise.reject(
          "$NAME::FETCH_CONVERSATIONS_ERROR",
          it.exception.localizedMessage, it.exception
        )
        else -> promise.reject(
          "$NAME::FETCH_CONVERSATIONS_ERROR",
          "Unknown error"
        )
      }
    }
  }

  @ReactMethod
  override fun endConversation(conversationId: String, promise: Promise) {
    CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch {
      when (val it: KusResult<KusConversation> = KustomerCore.getInstance().kusChatProvider().endConversation(conversationId)) {
        is KusResult.Success -> {
          val payload: WritableMap = toWritableKustomerConversation(it.data)
          promise.resolve(payload)
        }
        is KusResult.Error -> promise.reject(
          "$NAME::END_CONVERSATION_ERROR",
          it.exception.localizedMessage, it.exception
        )
        else -> promise.reject(
          "$NAME::END_CONVERSATION_ERROR",
          "Unknown error"
        )
      }
    }
  }

  @ReactMethod
  fun fetchConversation(conversationId: String, promise: Promise) {
    CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch {
      when (val it: KusResult<KusConversation> = KustomerCore.getInstance().kusChatProvider().fetchConversation(conversationId)) {
        is KusResult.Success -> {
          val payload: WritableMap = toWritableKustomerConversation(it.data)
          promise.resolve(payload)
        }
        is KusResult.Error -> promise.reject(
          "$NAME::FETCH_CONVERSATION_ERROR",
          it.exception.localizedMessage, it.exception
        )
        else -> promise.reject(
          "$NAME::FETCH_CONVERSATION_ERROR",
          "Unknown error"
        )
      }
    }
  }

  @ReactMethod
  fun markRead(conversationId: String, messageIds: ReadableArray, promise: Promise) {
    val messageIdsList: List<String> = ArrayUtil.toArray(messageIds).asList().map { it as String }

    CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch {
      KustomerCore.getInstance().kusChatProvider().markRead(conversationId, messageIdsList)
      promise.resolve(null)
    }
  }

  @ReactMethod
  fun fetchChatMessages(conversationId: String, pageSize: Double, startTimeToken: Double, promise: Promise) {
    CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch {
      when (val it: KusResult<List<Any>> = KustomerCore.getInstance().kusChatProvider().fetchChatMessages(conversationId, pageSize.toInt(), startTimeToken.toInt())) {
        is KusResult.Success -> promise.resolve(it.data) // todo
        is KusResult.Error -> promise.reject(
          "$NAME::FETCH_CHAT_MESSAGES_ERROR",
          it.exception.localizedMessage, it.exception
        )
        else -> promise.reject(
          "$NAME::FETCH_CHAT_MESSAGES_ERROR",
          "Unknown error"
        )
      }
    }
  }

  @ReactMethod
  fun sendMessageWithAttachments(
    body: String?,
    messageActionMap: ReadableMap,
//    attachments: List<KusUploadAttachment>?, // TODO: add support for attachments
    conversationId: String,
    isFirstMessage: Boolean,
    promise: Promise
  ) {

    val messageAction: KusMessageAction = toKusMessageAction(messageActionMap)

    CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch {
      when (val it: KusResult<KusChatMessage> = KustomerCore.getInstance().kusChatProvider().sendMessageWithAttachments(
        body, messageAction, listOf(), conversationId, isFirstMessage
      )) {
        is KusResult.Success -> {
          val payload: WritableMap = toWritableKustomerChatMessage(it.data)
          promise.resolve(payload)
        }
        is KusResult.Error -> promise.reject(
          "$NAME::SEND_MESSAGE_WITH_ATTACHMENTS_ERROR",
          it.exception.localizedMessage, it.exception
        )
        else -> promise.reject(
          "$NAME::SEND_MESSAGE_WITH_ATTACHMENTS_ERROR",
          "Unknown error"
        )
      }
    }
  }


  @ReactMethod
  override fun sendTypingStatus(conversationId: String, typingStatus: String, promise: Promise) {
    val stauts = toKusTypingStatus(typingStatus)

    CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch {
      when (val it: KusResult<Boolean> = KustomerCore.getInstance().kusChatProvider().sendTypingStatus(conversationId, stauts)) {
        is KusResult.Success -> promise.resolve(it.data)
        is KusResult.Error -> promise.reject(
          "$NAME::SEND_TYPING_STATUS_ERROR",
          it.exception.localizedMessage, it.exception
        )
        else -> promise.reject(
          "$NAME::SEND_TYPING_STATUS_ERROR",
          "Unknown error"
        )
      }
    }
  }

  @ReactMethod
  override fun isChatAvailable(promise: Promise) = try {
    Kustomer.getInstance().isChatAvailable {
      when (it) {
        is KusResult.Success -> promise.resolve(it.data)
        is KusResult.Error -> promise.reject(
          "$NAME::IS_CHAT_AVAILABLE_ERROR", it.exception.localizedMessage
        )
        else -> promise.reject("$NAME::IS_CHAT_AVAILABLE_ERROR", it.toString())
      }
    }
  } catch (e: KusNotInitializedException) {
    promise.reject("$NAME::IS_CHAT_AVAILABLE_ERROR", e)
  }

  @ReactMethod
  override fun getChatSettings(promise: Promise) {
    CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch {
      when (val it: KusResult<KusChatSetting> = KustomerCore.getInstance().kusChatProvider().getChatSettings()) {
        is KusResult.Success -> {
          val payload: WritableMap = toWritableKustomerChatSetting(it.data)
          promise.resolve(payload)
        }
        is KusResult.Error -> promise.reject(
          "$NAME::GET_CHAT_SETTINGS_ERRROR",
          it.exception.localizedMessage, it.exception
        )
        else -> promise.reject(
          "$NAME::GET_CHAT_SETTINGS_ERRROR",
          "Unknown error"
        )
      }
    }
  }

  @ReactMethod
  override fun overrideBusinessSchedule(scheduleId: String, promise: Promise) = try {
    Kustomer.getInstance().overrideBusinessSchedule(scheduleId) {
      when (it) {
        is KusResult.Success -> promise.resolve(it.data)
        is KusResult.Error -> promise.reject(
          "$NAME::OVERRIDE_BUSINESS_SCHEDULE_ERROR", it.exception.localizedMessage
        )
        else -> promise.reject("$NAME::OVERRIDE_BUSINESS_SCHEDULE_ERROR", it.toString())
      }
    }
  } catch (e: KusNotInitializedException) {
    promise.reject("$NAME::OVERRIDE_BUSINESS_SCHEDULE_ERROR", e)
  }

  @ReactMethod
  override fun changeActiveAssistant(assistantId: String, promise: Promise) {
    val activeAssistant = KusActiveAssistant.WithId(assistantId)

    CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch {
      when (val it: KusResult<KusAssistant>? = KustomerCore.getInstance().kusChatProvider().changeActiveAssistant(activeAssistant)) {
        is KusResult.Success -> {
          val payload: WritableMap = toWritableKustomerAssistant(it.data)
          promise.resolve(payload)
        }
        is KusResult.Error -> promise.reject(
          "$NAME::CHANGE_ACTIVE_ASSISTANT_ERROR",
          it.exception.localizedMessage, it.exception
        )
        else -> promise.reject(
          "$NAME::CHANGE_ACTIVE_ASSISTANT_ERROR",
          "Unknown error"
        )
      }
    }
  }

  @ReactMethod
  override fun init(promise: Promise) {
    // todo
  }

  @ReactMethod
  override fun setPresenceActivity(activity: String)  {
    val presence = toKusPresenceEvent(activity)
    KustomerCore.getInstance().kusChatProvider().sendPresenceActivity(presence)
  }

  @ReactMethod
  override fun describeCustomer(attributes: ReadableMap, promise: Promise) {
    val customer = toKusCustomerDescribeAttributes(attributes)

    CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch {
      when (val it: KusResult<Boolean> = KustomerCore.getInstance().kusChatProvider().describeCustomer(customer)) {
        is KusResult.Success -> {
          promise.resolve(it.data)
        }
        is KusResult.Error -> promise.reject(
          "$NAME::DESCRIBE_CUSTOMER_ERROR",
          it.exception.localizedMessage, it.exception
        )
        else -> promise.reject(
          "$NAME::DESCRIBE_CUSTOMER_ERROR",
          "Unknown error"
        )
      }
    }
  }

  @ReactMethod
  override fun getInitialMessages(promise: Promise) {
    CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch {
      when (val it: KusResult<List<KusChatMessage>> = KustomerCore.getInstance().kusChatProvider().getInitialMessages()) {
        is KusResult.Success -> {
          val arr: Array<WritableMap> = it.data.map { message -> toWritableKustomerChatMessage(message) }.toTypedArray()
          val payload: WritableArray = ArrayUtil.toWritableArray(arr)
          promise.resolve(payload)
        }
        is KusResult.Error -> promise.reject(
          "$NAME::GET_INITIAL_MESSAGES_ERROR",
          it.exception.localizedMessage, it.exception
        )
        else -> promise.reject(
          "$NAME::GET_INITIAL_MESSAGES_ERROR",
          "Unknown error"
        )
      }
    }
  }

  @ReactMethod
  override fun describeConversation(conversationId: String, attributes: ReadableMap, promise: Promise) {
    val attrMap: Map<String, Any> = MapUtil.toMap(attributes)

    CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch {
      when (val it: KusResult<Boolean> = KustomerCore.getInstance().kusChatProvider().describeConversation(conversationId, attrMap)) {
        is KusResult.Success -> {
          promise.resolve(it.data)
        }
        is KusResult.Error -> promise.reject(
          "$NAME::DESCRIBE_CONVERSATION_ERROR",
          it.exception.localizedMessage, it.exception
        )
        else -> promise.reject(
          "$NAME::DESCRIBE_CONVERSATION_ERROR",
          "Unknown error"
        )
      }
    }
  }

  @ReactMethod
  override fun getLastReadTimeToken(conversationId: String, promise: Promise) {
    // todo
  }

  @ReactMethod
  override fun sendDeflection(conversationId: String, deflection: ReadableMap, promise: Promise) {
    // todo
  }

  @ReactMethod
  override fun sendChatMessage(conversationId: String, message:String, promise: Promise) {
    val messageAction = null
    val attachments = null

    CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch {
      when (val it: KusResult<KusChatMessage> = KustomerCore.getInstance().kusChatProvider().sendMessageWithAttachments(
        message,
        messageAction,
        attachments,
        conversationId
      )) {
        is KusResult.Success -> {
          val payload = toWritableKustomerChatMessage(it.data)
          promise.resolve(payload)
        }
        is KusResult.Error -> promise.reject(
          "$NAME::SEND_CHAT_MESSAGE_ERROR",
          it.exception.localizedMessage, it.exception
        )
        else -> promise.reject(
          "$NAME::SEND_CHAT_MESSAGE_ERROR",
          "Unknown error"
        )
      }
    }
  }

  @ReactMethod
  override fun sendChatImage(conversationId: String, imageUri: String, promise: Promise) {
    // todo
  }

  @ReactMethod
  override fun sendChatFile(conversationId: String, fileUri: String, promise: Promise) {
    // todo
  }

  @ReactMethod
  override fun sendChatAssistantMessage(conversationId: String, assistantId: String, message:String, promise: Promise) {
    // todo
  }

  @ReactMethod
  override fun sendChatAssistantAction(conversationId: String, action: ReadableMap, additionalParams: ReadableMap, promise: Promise) {
    // todo
  }

  @ReactMethod
  override fun sendChatAssistantMllAction(conversationId: String, mllNode: ReadableMap, additionalParams: ReadableMap, promise: Promise) {
    // todo
  }

  @ReactMethod
  override fun submitSatisfactionForm(conversationId: String, conversationSatisfactionId: String, response: ReadableMap, promise: Promise) {
    val satisfactionPostBody: KusSatisfactionNetworkPostBody = toKusSatisfactionNetworkPostBody(response)

    CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch {
      when (val it: KusResult<KusSatisfaction> = KustomerCore.getInstance().kusChatProvider().submitSatisfactionForm(
        conversationSatisfactionId,
        satisfactionPostBody,
        conversationId
      )) {
        is KusResult.Success -> {
          val satisfaction = toWritableKustomerSatisfaction(it.data)
          promise.resolve(satisfaction)
        }
        is KusResult.Error -> promise.reject(
          "$NAME::SUBMIT_SATISFACTION_FORM_ERROR",
          it.exception.localizedMessage, it.exception
        )
        else -> promise.reject(
          "$NAME::SUBMIT_SATISFACTION_FORM_ERROR",
          "Unknown error"
        )
      }
    }
  }

  @ReactMethod
  override fun getChatMessages(conversationId: String, promise: Promise) {
    // todo
  }

  @ReactMethod
  override fun getHistoricChatMessages(conversationId: String, page: Double, pageSize: Double, promise: Promise) {
    // todo
  }

  @ReactMethod
  override fun getChatMessagesBefore(conversationId: String, timestamp: Double, count: Double, promise: Promise) {
    CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch {
      when (val it: KusResult<List<Any>> = KustomerCore.getInstance().kusChatProvider().getChatMessagesBeforeTimestamp(
        conversationId,
        timestamp.toLong(),
        count.toInt()
      )) {
        is KusResult.Success -> {
          promise.resolve(it.data)
        }
        is KusResult.Error -> promise.reject(
          "$NAME::GET_CHAT_MESSAGES_BEFORE_ERROR",
          it.exception.localizedMessage, it.exception
        )
        else -> promise.reject(
          "$NAME::GET_CHAT_MESSAGES_BEFORE_ERROR",
          "Unknown error"
        )
      }
    }
  }

  @ReactMethod
  override fun getConversation(conversationId: String, promise: Promise) {
    CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch {
      when (val it: KusResult<KusConversation> = KustomerCore.getInstance().kusChatProvider().getConversation(
        conversationId,
      )) {
        is KusResult.Success -> {
          val conversation = toWritableKustomerConversation(it.data)
          promise.resolve(conversation)
        }
        is KusResult.Error -> promise.reject(
          "$NAME::GET_CONVERSATION_ERROR",
          it.exception.localizedMessage, it.exception
        )
        else -> promise.reject(
          "$NAME::GET_CONVERSATION_ERROR",
          "Unknown error"
        )
      }
    }
  }

  @ReactMethod
  override fun getConversations(page: Double, pageSize: Double, promise: Promise) {
    // todo
  }

  @ReactMethod
  override fun getAllConversations(promise: Promise) {
    // todo
  }

  @ReactMethod
  override fun getActiveConversations(promise: Promise) {
    // todo
  }

  @ReactMethod
  override fun getOpenConversationCount(promise: Promise) {
    // todo
  }

  @ReactMethod
  override fun getUnreadCount(promise: Promise) {
    // todo
  }

  @ReactMethod
  override fun fetchSatisfactionFormWithResponse(conversationId: String, promise: Promise) {
    // todo
  }
}
