package com.kustomer.chatreactnative

import com.facebook.react.bridge.*

abstract class RNKustomerChatSpec internal constructor(context: ReactApplicationContext) :
  ReactContextBaseJavaModule(context) {
  abstract fun getUnreadCount(promise: Promise)
  abstract fun getOpenConversationCount(promise: Promise)
  abstract fun startNewConversation(
    initialMessage: ReadableMap?,
    customDescribe: ReadableMap?,
    afterCreateConversation: Callback?,
    animated: Boolean?
  )
  abstract fun openChatAssistant(
    chatAssistantId: String,
    startDialog: String?,
    initialMessage: String?,
    customDescribe: ReadableMap?,
    afterFirstMessage: Callback?,
    animated: Boolean?,
    promise: Promise
  )
  abstract fun setPushToken(token: String, promise: Promise)
  abstract fun unsetPushToken(promise: Promise?)
  abstract fun onRemoteMessage(remoteMessage: ReadableMap, promise: Promise)
  abstract fun start(promise: Promise)
  abstract fun isLoggedIn(idType: String, id: String, promise: Promise)
  abstract fun logIn(jwt: String, promise: Promise)
  abstract fun logOut(promise: Promise)
  abstract fun logOutThenLogIn(jwt: String, promise: Promise)
}
