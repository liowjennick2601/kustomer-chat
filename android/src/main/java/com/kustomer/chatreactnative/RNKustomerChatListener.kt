package com.kustomer.chatreactnative

import com.facebook.react.bridge.*
import com.facebook.react.util.RNLog
import com.kustomer.core.listeners.KusChatListener
import com.kustomer.core.models.chat.*

class RNKustomerChatListener(
  private val sendEvent: (String, WritableMap) -> Unit,
  private val reactContext: ReactContext
) : KusChatListener {
  override fun onAgentIsTyping(conversationId: String, typingIndicator: KusTypingIndicator) = try {
    sendEvent(
      "onAgentIsTyping", Arguments.makeNativeMap(
        mapOf(
          "conversationId" to conversationId,
          "typingIndicator" to typingIndicator.status.name.lowercase().endsWith("started")
        )
      )
    )
  } catch (e: Exception) {
    RNLog.e(reactContext, "onAgentIsTyping error" + e.message)
  }

  override fun onAgentJoined(conversationId: String, agent: KusUser) = try {
    sendEvent(
      "onAgentJoined", Arguments.makeNativeMap(
        mapOf(
          "conversationId" to conversationId, "agent" to toWritableKustomerUser(agent)
        )
      )
    )
  } catch (e: Exception) {
    RNLog.e(reactContext, "onAgentJoined error" + e.message)
  }

  override fun onAssistantEnded(conversation: KusConversation) = try {
    sendEvent("onAssistantEnded", toWritableKustomerConversation(conversation))
  } catch (e: Exception) {
    RNLog.e(reactContext, "onAssistantEnded error" + e.message)
  }


  override fun onChatMessageReceived(conversationId: String, chatMessage: KusChatMessage) = try {
    sendEvent("onChatMessageReceived", toWritableKustomerChatMessage(chatMessage))
  } catch (e: Exception) {
    RNLog.e(reactContext, "onChatMessageReceived error" + e.message)
  }

  override fun onConversationCreated(conversation: KusConversation) = try {
    sendEvent("onConversationCreated", toWritableKustomerConversation(conversation))
  } catch (e: Exception) {
    RNLog.e(reactContext, "onConversationCreated error" + e.message)
  }

  override fun onConversationDeleted(conversation: KusConversation) = try {
    sendEvent("onConversationDeleted", toWritableKustomerConversation(conversation))
  } catch (e: Exception) {
    RNLog.e(reactContext, "onConversationDeleted error" + e.message)
  }

  override fun onConversationEnded(conversation: KusConversation) = try {
    sendEvent("onConversationEnded", toWritableKustomerConversation(conversation))
  } catch (e: Exception) {
    RNLog.e(reactContext, "onConversationEnded error" + e.message)
  }

  override fun onConversationLastMessageAtChanged(conversationId: String, lastMessageAt: Long) =
    try {
      sendEvent(
        "onConversationLastMessageAtChanged", Arguments.makeNativeMap(
          mapOf(
            "conversationId" to conversationId, "lastMessageAt" to lastMessageAt
            // todo not on ios
          )
        )
      )
    } catch (e: Exception) {
      RNLog.e(reactContext, "onConversationLastMessageAtChanged error" + e.message)
    }

  override fun onConversationMerged(source: KusConversation, target: KusConversation) = try {
    sendEvent(
      "onConversationMerged", Arguments.makeNativeMap(
        mapOf(
          "source" to toWritableKustomerConversation(source),
          "target" to toWritableKustomerConversation(target)
        )
      )
    )
  } catch (e: Exception) {
    RNLog.e(reactContext, "onConversationMerged error" + e.message)
  }

  override fun onConversationUnended(conversation: KusConversation) = try {
    sendEvent(
      "onConversationResumed", toWritableKustomerConversation(conversation)
    )
  } catch (e: Exception) {
    RNLog.e(reactContext, "onConversationResumed error" + e.message)
  }

  override fun onCustomerDeleted() = sendEvent("onCustomerDeleted", Arguments.createMap())

  override fun onCustomerMerged(customerId: String) = try {
    sendEvent(
      "onCustomerMerged", Arguments.makeNativeMap(
        mapOf(
          "conversationId" to customerId
        )
      )
    )
  } catch (e: Exception) {
    RNLog.e(reactContext, "onCustomerMerged error" + e.message)
  }

  override fun onPreviewChanged(conversationId: String, preview: KusConversationPreview) = try {
    sendEvent(
      "onPreviewChanged", Arguments.makeNativeMap(
        mapOf(
          "conversationId" to conversationId,
//            "preview" to toWritableKustomerPreview(preview) // todo
        )
      )
    )
  } catch (e: Exception) {
    RNLog.e(reactContext, "onPreviewChanged error" + e.message)
  }

  override fun onSatisfactionEventReceived(
    conversationId: String, satisfaction: KusSatisfaction
  ) = try {
    sendEvent(
      "onSatisfactionEventReceived", Arguments.makeNativeMap(
        mapOf(
          "conversationId" to conversationId, "satisfaction" to satisfaction // todo serialize
        )
      )
    )
  } catch (e: Exception) {
    RNLog.e(reactContext, "onSatisfactionEventReceived error" + e.message)
  }

  override fun onUnreadCountChange(conversationId: String, unreadCount: Int) = try {
    sendEvent(
      "onUnreadCountChange", Arguments.makeNativeMap(
        mapOf(
          "conversationId" to conversationId, "count" to unreadCount
        )
      )
    )
  } catch (e: Exception) {
    RNLog.e(reactContext, "onUnreadCountChange error" + e.message)
  }
}
