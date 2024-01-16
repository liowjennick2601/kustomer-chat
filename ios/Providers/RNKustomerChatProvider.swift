import KustomerChat
import UIKit

@objcMembers
@objc(RNKustomerChatProvider)
class RNKustomerChatProvider: NSObject {
  static func requiresMainQueueSetup() -> Bool {
    return false
  }

  func constantsToExport() -> [AnyHashable: Any]! {
    return [:]
  }

  // #region Settings
  func isChatAvailable(_ resolve: RCTPromiseResolve<Bool>, rejecter reject: RCTPromiseReject) {
    resolve(Kustomer.chatProvider.isChatAvailable() ?? false)
  }

  func getChatSettings(
    _ resolve: RCTPromiseResolve<[String: Any?]>, rejecter reject: RCTPromiseReject
  ) {
    resolve(Kustomer.chatProvider.getChatSettings().toDictionary())
  }

  func overrideBusinessSchedule(
    _ resolve: @escaping RCTPromiseResolve<[String: Any?]>, rejecter reject: @escaping RCTPromiseReject,
    scheduleId: String
  ) {
    Kustomer.changeBusinessSchedule(scheduleId: scheduleId) { r in
      switch r {
      case .success():
        resolve([:])
        break
      case .failure(let err):
        reject(
          "\("RNKusomterChat")::OVERRIDE_BUSINESS_SCHEDULE_ERROR",
          "Unable to override business schedule: \(err)", err)
        break
      }
    }
  }

  func changeActiveAssistant(
    _ resolve: RCTPromiseResolve<[String: Any?]>, rejecter reject: RCTPromiseReject,
    assistant: NSDictionary
  ) {
      let type = assistant["type"] as! String
      var activeAssistant: KustomerChat.ActiveAssistantOptions? = nil
      switch type {
      case "withId":
          activeAssistant = .withId(assistant["payload"] as! String)
          break;
      default:
          activeAssistant = KustomerChat.ActiveAssistantOptions(rawString: type)
          break;
      }
      KustomerClient.shared.changeActiveAssistant(activeAssistant!)
      resolve([:])
  }
  // #endregion

  // #region meta
  func initinalise(_ resolve: RCTPromiseResolve<Bool>, rejecter reject: RCTPromiseReject) {
      resolve(true) // todo: is this correct?
  }

  func setPresenceActivity(presence: String) {
      Kustomer.chatProvider.sendPresenceActivity(KustomerChat.PresenceType(type: presence))
  }
  func describeCustomer(  // describeCurrentCustomer
    _ resolve: @escaping RCTPromiseResolve<Bool>, rejecter reject: @escaping RCTPromiseReject,
    describeAttributes: NSDictionary
  ) {
      Kustomer.chatProvider.describeCurrentCustomer(phone: describeAttributes["phone"] as? String,
                                             email: describeAttributes["email"] as? String,
                                             phones: describeAttributes["phones"] as? [String],
                                             emails: describeAttributes["emails"] as? [String],
                                             facebook: describeAttributes["facebook"] as? String,
                                             instagram: describeAttributes["instagram"] as? String,
                                             twitter: describeAttributes["twitter"] as? String,
                                             linkedIn: describeAttributes["linkedIn"] as? String,
                                             custom: describeAttributes["custom"] as? [String : Any],
                                               { r in
          switch r {
          case .success():
              resolve(true)
          case .failure(let err):
              reject(
                "\("RNKusomterChat")::DESCRIBE_CUSTOMER_ERROR",
                "Unable to describe customer: \(err)", err)
          }
      })
  }
  // #endregion

  // #region Start/end messaging
  func getInitialMessages(
    _ resolve: RCTPromiseResolve<[String: Any?]>, rejecter reject: RCTPromiseReject
  ) {

  }
  func getInitialMessages(
    _ resolve: RCTPromiseResolve<[String: Any?]>, rejecter reject: RCTPromiseReject,
    assistantId: String?,
    startDialog: String?
  ) {
      reject("\("RNKustomerChatProvider")::NO_IMPL", "Feature not implemented", nil)
  }
  func createConversation(
    _ resolve: RCTPromiseResolve<[String: Any?]>, rejecter reject: RCTPromiseReject,
    newConversationOptions: NSDictionary
  ) {
      reject("\("RNKustomerChatProvider")::NO_IMPL", "Feature not implemented", nil)
  }
  func endConversation(
    _ resolve: RCTPromiseResolve<[String: Any?]>, rejecter reject: RCTPromiseReject,
    conversationId: String
  ) {
      reject("\("RNKustomerChatProvider")::NO_IMPL", "Feature not implemented", nil)
  }
  // #endregion

  // #region Messaging
  func describeConversation(
    _ resolve: RCTPromiseResolve<Void?>, rejecter reject: RCTPromiseReject,
    conversationId: String,
    attributes: NSDictionary
  ) {
      reject("\("RNKustomerChatProvider")::NO_IMPL", "Feature not implemented", nil)
  }
  func getLastReadTimeToken(
    _ conversationId: String, resolve: RCTPromiseResolve<Void?>, rejecter reject: RCTPromiseReject
  ) {
//      resolve(Kustomer.chatProvider.getLastReadTimeToken(for: conversationId))
      reject("\("RNKustomerChatProvider")::NO_IMPL", "Feature not implemented", nil)
  }  // todo: can be done on android through getLastReadTimestamp | getTimeTokenMap
  func sendDeflection(  // sendArticlesClicked on Android
    _ conversationId: String,
    deflection: Any?,
    resolve: RCTPromiseResolve<[String: Any?]>, rejecter reject: RCTPromiseReject
  ) {
      reject(
        "\("RNKusomterChat")::NO_IMPL",
        "Feature not implemented", nil)
//      Kustomer.chatProvider.sendDeflectionTracking(.init(articles: []), conversationId) // todo impl
  }  // todo: iOS doesn't report issues, but android does
  func markRead(
    conversationId: String,
    messageIds: [String]?
  ) {
  }
  func sendTypingStatus(
    conversationId: String,
    typing: Bool = false
  ) {
    Kustomer.chatProvider.sendTypingStatus(for: conversationId, isTyping: typing)
  }
  func sendChatMessage(  // sendMessageWithAttachments on Android
    _ conversationId: String,
    message: String,
    resolve: RCTPromiseResolve<[String: Any?]>, rejecter reject: RCTPromiseReject
  ) {
      reject("\("RNKustomerChatProvider")::NO_IMPL", "Feature not implemented", nil)
  }
  /** Max size for a file: 5MB */
  func sendChatImage(
    _ conversationId: String,
    /* uri of image from camera tmp save, or elsewhere, use Image.resolveAssetSource(foo).uri if needed */
    imageUri: String,
    resolve: RCTPromiseResolve<[String: Any?]>, rejecter reject: RCTPromiseReject
  ) {
      reject("\("RNKustomerChatProvider")::NO_IMPL", "Feature not implemented", nil)
  }
  /** Max size for a file: 5MB */
  func sendChatFile(  // todo: android can send multiple
    _ conversationId: String,
    /* uri of file to send */
    fileUri: String,
    resolve: RCTPromiseResolve<[String: Any?]>, rejecter reject: RCTPromiseReject
  ) {
      reject("\("RNKustomerChatProvider")::NO_IMPL", "Feature not implemented", nil)
  }
  func sendChatAssistantMessage(  // todo: android doesn't distinguish
    _ conversationId: String,
    assistantId: String,
    message: String,
    resolve: RCTPromiseResolve<[String: Any?]>, rejecter reject: RCTPromiseReject
  ) {
      reject("\("RNKustomerChatProvider")::NO_IMPL", "Feature not implemented", nil)
  }
  func sendChatAssistantAction(
    _ conversationId: String,
    action: KUSMessageAction,
    additionalParams: NSDictionary,
    resolve: RCTPromiseResolve<[String: Any?]>, rejecter reject: RCTPromiseReject
  ) {
      reject("\("RNKustomerChatProvider")::NO_IMPL", "Feature not implemented", nil)
  }
  func sendChatAssistantMllAction(
    _ conversationId: String,
    mllNode: KUSMLLNode,
    additionalParams: NSDictionary,
    resolve: RCTPromiseResolve<[String: Any?]>, rejecter reject: RCTPromiseReject
  ) {
      reject("\("RNKustomerChatProvider")::NO_IMPL", "Feature not implemented", nil)
  }
  /**
    * Submits a satisfaction form response for a conversation.
    * @param conversationSatisfactionId Get from `onSatisfactionEventReceived` event
    */
  func submitSatisfactionForm(
    _ conversationId: String,
    conversationSatisfactionId: String,
    response: NSDictionary,
    resolve: RCTPromiseResolve<[String: Any?]>, rejecter reject: RCTPromiseReject
  ) {
      reject("\("RNKustomerChatProvider")::NO_IMPL", "Feature not implemented", nil)
  }
  // #endregion

  // #region Messaging history
  /** Gets the messages for a conversation from PubNub history (up to 30 days). */
  func getChatMessages(
    _ conversationId: String,
    resolve: RCTPromiseResolve<[String: Any?]>, rejecter reject: RCTPromiseReject
  ) {
      reject("\("RNKustomerChatProvider")::NO_IMPL", "Feature not implemented", nil)
  }  // android has paging but noop
  /** Eagerly fetches recent (30 days) chat messages for the conversation */
  // getRecentChatMessages(
  //   conversationId: String
  // ): Promise<KusChatMessage[]>; // todo: android `fetchChatMessages`, page and pageSize are noop
  /** A less optimal way to fetch messages, primarily to be used to go beyond the last 30 days */
  func getHistoricChatMessages(  // todo: iOS is hardcoded page1@100 on historic, can use `getMessages` with `before` and `count` derived from page and pageSize
    _ conversationId: String,
    page: NSNumber,
    pageSize: NSNumber,
    resolve: RCTPromiseResolve<[String: Any?]>, rejecter reject: RCTPromiseReject
  ) {
      reject("\("RNKustomerChatProvider")::NO_IMPL", "Feature not implemented", nil)
  }
  func getChatMessagesBefore(  // todo: android getHistoricChatMessages
    _ conversationId: String,
    timestamp: String,
    count: NSNumber,
    resolve: RCTPromiseResolve<[String: Any?]>, rejecter reject: RCTPromiseReject
  ) {
      reject("\("RNKustomerChatProvider")::NO_IMPL", "Feature not implemented", nil)
  }
  // #endregion

  // #region Conversations
  //? android has fetchX versions, should that be on iOS too?
  func getConversation(
    _ conversationId: String,
   resolve: RCTPromiseResolve<[String: Any?]>, rejecter reject: RCTPromiseReject
  ) {
      reject("\("RNKustomerChatProvider")::NO_IMPL", "Feature not implemented", nil)
  }
  func getConversations(
    _ pageSize: NSNumber,
    page: NSNumber,
    resolve: RCTPromiseResolve<[String: Any?]>, rejecter reject: RCTPromiseReject
  ) {

  }  // iOS: getConversationPage
  func getAllConversations(
    _ resolve: RCTPromiseResolve<[String: Any?]>, rejecter reject: RCTPromiseReject
  ) {

  }  // todo: android use v. large page?
  func getActiveConversations(
    _ resolve: RCTPromiseResolve<[String: Any?]>, rejecter reject: RCTPromiseReject
  ) {

  }  // todo: android can use observed active conv ids, and filter all convs
  func getOpenConversationCount(
    _ resolve: RCTPromiseResolve<Int>, rejecter reject: RCTPromiseReject
  ) {
      reject("\("RNKustomerChatProvider")::NO_IMPL", "Feature not implemented", nil)
  }  // todo: android can use observed active ids count
  func getUnreadCount(_ resolve: RCTPromiseResolve<Int>, rejecter reject: RCTPromiseReject) {
          reject("\("RNKustomerChatProvider")::NO_IMPL", "Feature not implemented", nil)
  }
  // #endregion
}
