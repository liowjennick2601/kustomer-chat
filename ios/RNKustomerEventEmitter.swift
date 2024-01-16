import KustomerChat
import UIKit

enum KustomerConfigError: Error {
  case missingRequiredValue(valueKey: String)
  case invalidValue(valueKey: String, value: String)
  case unknownConfigureError
}

enum KusEvent: String, CaseIterable {
  case onKustomerInitialized
  case onChatMessageReceived
  case onSatisfactionEventReceived
  case onAgentIsTyping
  case onUnreadCountChange
  case onUnreadTotalChange
  case onAgentJoined
  case onConversationCreated
  case onConversationEnded
  case onConversationDeleted
  case onConversationResumed
  case onCustomerMerged
  case onCustomerDeleted
  case onAssistantEnded
  case onConversationMerged
  case onOpenConversationCountChanged
}

@objcMembers
@objc(RNKustomerEventEmitter)
class RNKustomerEventEmitter: RCTEventEmitter, KUSChatListener {
  override func supportedEvents() -> [String]! {
    return KusEvent.allCases.map { $0.rawValue }
  }

  override class func requiresMainQueueSetup() -> Bool {
    return true
  }

  var listenerUuid: String? = nil

  override func startObserving() {
    listenerUuid.map { Kustomer.chatProvider.removeChatListener($0) }
    listenerUuid = Kustomer.chatProvider.addChatListener(self)
  }

  override func stopObserving() {
    listenerUuid.map { Kustomer.chatProvider.removeChatListener($0) }
    listenerUuid = nil
  }

  func onKustomerInitialized() {
    self.sendEvent(withName: "onKustomerInitialized", body: nil)
  }

  func onChatMessageReceived(conversationId: String, chatMessage: KustomerChat.KUSChatMessage) {
    switch chatMessage.messageType {
    case .chatMessage:
      self.sendEvent(
        withName: "onChatMessageReceived",
        body: chatMessage.toDictionary()
      )
      break
    case .satisfactionResponse:
      self.sendEvent(
        withName: "onSatisfactionEventReceived",
        body: chatMessage.toDictionary()
      )
      break
    default:
      break
    }
  }

  /**
     When a conversation ends
     */
  func onConversationEnded(conversationId: String, conversation: KustomerChat.KUSConversation) {
    self.sendEvent(
      withName: "onConversationEnded",
      body: conversation.toDictionary()
    )
  }

  /**
     When a conversation is re-opened
     */
  func onConversationReOpened(conversationId: String, conversation: KustomerChat.KUSConversation) {
    self.sendEvent(
      withName: "onConversationResumed",
      body: conversation.toDictionary()
    )
  }

  /**
     When the open conversation count changes
     */
  func onOpenConversationCountChange(count: Int) {
    self.sendEvent(withName: "onOpenConversationCountChanged", body: ["count": count])
  }

  /**
     When a conversation is deleted
     */
  func onConversationDeleted(conversationId: String, conversation: KustomerChat.KUSConversation) {
    self.sendEvent(
      withName: "onConversationDeleted",
      body: conversation.toDictionary()
    )
  }
  /**
     When a new conversation is created and successfully sent to the Kustomer servers
     */
  func onConversationCreated(conversationId: String, conversation: KustomerChat.KUSConversation) {
    self.sendEvent(
      withName: "onConversationCreated",
      body: conversation.toDictionary()
    )
  }

  /**
     When the total unread count changes
     */
  func onUnreadCountChange(count: Int) {
    self.sendEvent(
      withName: "onUnreadTotalChange",
      body: ["count": count])
  }

  /**
     When the unread count changes for a particular conversation
     */
  func onUnreadCountChange(conversationId: String, count: Int) {
    self.sendEvent(
      withName: "onUnreadCountChange",
      body: [
        "conversationId": conversationId,
        "count": count,
      ])
  }

  /**
     When a new `KUSUser` (customer support agent) joins a conversation.
     */
  func onUserJoined(conversationId: String, user: KustomerChat.KUSUser) {
    self.sendEvent(withName: "onAgentJoined", body: user.toDictionary())
  }

  func onUserIsTyping(
    conversationId: String, user: KustomerChat.KUSUser, timetoken: KustomerChat.KUSPNTtoken?
  ) {
    // todo check if this is user too, or just agent
    self.sendEvent(
      withName: "onAgentIsTyping",
      body: [
        "conversationId": conversationId,
        "typingIndicator": true,
        "userId": user.id ?? "",
      ])
  }

  /**
     When a `KUSUser` stops typing.
     */
  func onUserStopsTyping(
    conversationId: String, user: KustomerChat.KUSUser, timetoken: KustomerChat.KUSPNTtoken?
  ) {
    // todo check if this is user too, or just agent
    self.sendEvent(
      withName: "onAgentIsTyping",
      body: [
        "conversationId": conversationId,
        "typingIndicator": false,
        "userId": user.id ?? "",
      ])
  }

  /**
     Called when there is new preview text for a conversation. Note: it's not guaranteed that
     initial messages arrive in order. We recommend using PreviewDetails.messageCreatedAt to ensure that
     the preview displayed is the most recent message.
     */
  func onPreviewChanged(
    conversationId: String, preview: String, previewDetails: KustomerChat.PreviewDetails?
  ) {
    // todo
  }

  func onAssistantEnded(conversation: KustomerChat.KUSConversation) {
    self.sendEvent(
      withName: "onAssistantEnded",
      body: [
        "conversationId": conversation.id
      ])
  }

  func onConversationMerged(
    sourceConversation: KustomerChat.KUSConversation,
    targetConversation: KustomerChat.KUSConversation
  ) {
    self.sendEvent(
      withName: "onConversationMerged",
      body: [
        "source": sourceConversation.toDictionary(),
        "target": targetConversation.toDictionary(),
      ])
  }

  func onSatisfactionEventReceived(
    conversationId: String, satisfaction: KustomerChat.KUSSatisfaction
  ) {
    self.sendEvent(withName: "onSatisfactionEventReceived", body: satisfaction.toDictionary())
  }

  func onCustomerDeleted() {
    self.sendEvent(withName: "onCustomeDeleted", body: [:])
  }
}
