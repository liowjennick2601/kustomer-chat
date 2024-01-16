package com.kustomer.chatreactnative.providers

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReadableMap

abstract class RNKustomerChatProviderSpec internal constructor(context: ReactApplicationContext) :
  ReactContextBaseJavaModule(context) {
    // Settings
    abstract fun isChatAvailable(promise: Promise); // boolean
    abstract fun getChatSettings(promise: Promise); // KusChatSettings
    abstract fun overrideBusinessSchedule(scheduleId: String, promise: Promise); // KusSchedule
    abstract fun changeActiveAssistant(assistantId: String, promise: Promise); // void

    // Meta
    abstract fun init(promise: Promise); // boolean
    abstract fun setPresenceActivity(activity: String);
    abstract fun describeCustomer(attributes: ReadableMap, promise: Promise); // boolean

    // Start/end messaging
    // ignore assistantId and startDialog, they're iOS only
    abstract fun getInitialMessages(promise: Promise); // KusChatMessage[]
    abstract fun createConversation(newConversationOptions: ReadableMap, promise: Promise); // KusCreateConversationResult
    abstract fun endConversation(conversationId: String, promise: Promise); // KusConversation

    // Messaging
    abstract fun describeConversation(conversationId: String, attributes: ReadableMap, promise: Promise); // void
    // use getLastReadTimestamp | getTimeTokenMap to get this
    abstract fun getLastReadTimeToken(conversationId: String, promise: Promise); // String
    abstract fun sendDeflection(conversationId: String, deflection: ReadableMap, promise: Promise); // void
    abstract fun sendTypingStatus(conversationId: String, typingStatus: String, promise: Promise); // void
    abstract fun sendChatMessage(conversationId: String, message: String, promise: Promise); // KusSendChatMessageResult
    abstract fun sendChatImage(conversationId: String, imageUri: String, promise: Promise); // KusSendChatMessageResult
    abstract fun sendChatFile(conversationId: String, fileUri: String, promise: Promise); // KusSendChatMessageResult
    // can call sendChatMessage(conversationId, message)
    abstract fun sendChatAssistantMessage(conversationId: String, assistantId: String, message: String, promise: Promise); // KusSendChatMessageResult
    abstract fun sendChatAssistantAction(conversationId: String, action: ReadableMap, additionalParams: ReadableMap, promise: Promise); // KusSendChatMessageResult
    abstract fun sendChatAssistantMllAction(conversationId: String, mllNode: ReadableMap, additionalParams: ReadableMap, promise: Promise); // KusSendChatMessageResult
    abstract fun submitSatisfactionForm(conversationId: String, conversationSatisfactionId: String, response: ReadableMap, promise: Promise); // KusSatisfaction

    // Messaging history
    // paging is noop, just use fetchChatMessages
    abstract fun getChatMessages(conversationId: String, promise: Promise); // KusChatMessage[]
    // abstract fun getRecentChatMessages(conversationId: String, promise: Promise); // KusChatMessage[]
    abstract fun getHistoricChatMessages(conversationId: String, page: Double, pageSize: Double, promise: Promise); // KusChatMessage[]
    abstract fun getChatMessagesBefore(conversationId: String, timestamp: Double, count: Double, promise: Promise); // KusChatMessage[]

    // Conversations
    // use fetchX versions
    abstract fun getConversation(conversationId: String, promise: Promise); // KusConversation
    abstract fun getConversations(page: Double, pageSize: Double, promise: Promise); // KusConversation[]
    abstract fun getAllConversations(promise: Promise); // KusConversation[]
    abstract fun getActiveConversations(promise: Promise); // KusConversation[]
    abstract fun getOpenConversationCount(promise: Promise); // Double
    abstract fun getUnreadCount(promise: Promise); // Double

    // Misc
    abstract fun fetchSatisfactionFormWithResponse(conversationId: String, promise: Promise); // KusSatisfaction
}
