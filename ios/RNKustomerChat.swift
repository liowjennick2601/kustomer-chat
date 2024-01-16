import KustomerChat
import UIKit

@objcMembers
@objc(RNKustomerChat)
class RNKustomerChat: NSObject {
  static func requiresMainQueueSetup() -> Bool {
    return true
  }

  static var usedApiKey: String?
  static var usedConfig: KustomerOptions?

  @objc var bridge: RCTBridge!

  func constantsToExport() -> [AnyHashable: Any]! {
    return [
      "sdkVersion": KustomerClient.shared.sdkVersion,
      "sdkBuild": KustomerClient.shared.sdkVersionBuild,
    ]
  }

  func isConfigured(
    _ resolve: @escaping RCTPromiseResolveBlock,
    reject _: @escaping RCTPromiseRejectBlock
  ) {
    resolve(RNKustomerChat.usedApiKey != nil)
  }

  func configure(
    _ apiKey: String,
    options: NSDictionary,
    resolve: @escaping RCTPromiseResolveBlock,
    reject: @escaping RCTPromiseRejectBlock
  ) {
    if apiKey.isEmpty {
      reject("\("RNKusomterChat")::CONFIG_ERROR", "Empty apiKey was passed: '\(apiKey)'", nil)
    }
    let kus = KustomerOptions(dict: options as! [String: Any?])

    if RNKustomerChat.usedApiKey == apiKey {
      // compare configs by values
      if String(describing: RNKustomerChat.usedConfig!.toDictionary()) == String(describing: kus!.toDictionary()) {
        resolve(nil)
        return
      } else {
        reject(
          "\("RNKusomterChat")::CONFIG_ERROR",
          "Kustomer already configured with different options",
          nil)
      }
      // tried to init with different api key
      reject(
        "\("RNKusomterChat")::CONFIG_ERROR",
        "Kustomer already configured with different API key",
        nil)
    }

    let _ = KustomerClient.shared.configure(
      apiKey: apiKey, options: kus,
      launchOptions: bridge.launchOptions as! [UIApplication.LaunchOptionsKey: Any]?,
      file: "application(_:didFinishLaunchingWithOptions:)")

    if case let .configurationError(error) = KustomerClient.shared.status {
      reject(
        "\("RNKusomterChat")::CONFIG_ERROR", "Kustomer configure error: \(error.debugDescription)",
        error)
    } else {
      resolve(nil)
      RNKustomerChat.usedApiKey = apiKey
      RNKustomerChat.usedConfig = kus
    }
  }

  func start(
    _ resolve: @escaping RCTPromiseResolveBlock,
    reject: @escaping RCTPromiseRejectBlock
  ) {
    KustomerClient.shared.start(
      completion: { resolve(nil) },
      failure: { err in
        reject("\("RNKusomterChat")::START_ERROR", err.localizedDescription, err)
      })
  }

  func show(
    _ displayMode: String
  ) {
    KustomerClient.shared.show(preferredView: KustomerDisplayMode(from: displayMode))
  }

  func close(
    _ animated: NSNumber,
    resolve: @escaping RCTPromiseResolveBlock,
    reject: @escaping RCTPromiseRejectBlock
  ) {
    KustomerClient.shared.close(
      animated: animated != 0,
      completion: { resolve(nil) })
  }

  func getStatus(
    _ resolve: @escaping RCTPromiseResolveBlock,
    reject _: @escaping RCTPromiseRejectBlock
  ) {
    resolve(Kustomer.status.toDictionary())
  }

  func getStatusSync() -> String {
    return Kustomer.status.rnValue
  }

  func isVisible(
    _ resolve: @escaping RCTPromiseResolveBlock,
    reject _: @escaping RCTPromiseRejectBlock
  ) {
    resolve(Kustomer.isVisible)
  }

  func isVisibleSync() -> Bool {
    return Kustomer.isVisible
  }

  // We map the email/uid to a single identifier to conform to Android, and make this api a bit simpler
  func isLoggedIn(
    _ idType: String,
    idValue: String,
    resolve: @escaping RCTPromiseResolveBlock,
    reject _: @escaping RCTPromiseRejectBlock
  ) {
    // if userIdentifier contains an @, we assume it's an email
    if idType == "email" {
      resolve(KustomerClient.shared.isLoggedIn(userEmail: idValue, userId: nil))
    } else {
      resolve(KustomerClient.shared.isLoggedIn(userEmail: nil, userId: idValue))
    }
  }

  func logOut(
    _ resolve: @escaping RCTPromiseResolveBlock,
    reject: @escaping RCTPromiseRejectBlock
  ) {
    KustomerClient.shared.logOut { result in
      switch result {
      case .none:
        resolve(nil)
      case .some:
        reject("\("RNKusomterChat")::LOGOUT_ERROR", result?.localizedDescription, result)
      }
    }
  }

  func logIn(
    _ jwt: String,
    resolve: @escaping RCTPromiseResolveBlock,
    reject: @escaping RCTPromiseRejectBlock
  ) {
    KustomerClient.shared.logIn(jwt: jwt) { result in
      switch result {
      case let .success(trackingIdentity):
        resolve(trackingIdentity.toDictionary())
      case let .failure(error):
        reject("\("RNKusomterChat")::LOGIN_ERROR", error.localizedDescription, error)
      }
    }
  }

  func logOutThenLogIn(
    _ jwt: String,
    resolve: @escaping RCTPromiseResolveBlock,
    reject: @escaping RCTPromiseRejectBlock
  ) {
    KustomerClient.shared.logOutThenLogIn(jwt: jwt) { result in
      switch result {
      case let .success(user):
        resolve(user.toDictionary())
      case let .failure(error):
        reject("\("RNKusomterChat")::LOGOUT_LOGIN_ERROR", error.localizedDescription, error)
      }
    }
  }

  func startNewConversation(
    _ initialMessage: NSDictionary,
    customDescribe: NSDictionary,
    afterCreateConversation: RCTResponseSenderBlock?,
    animated: NSNumber?
  ) {
    let initialMessage = KustomerChat.KUSInitialMessage(
      body: initialMessage["body"] as! String,
      direction: initialMessage["direction"] as! String == "agent" ? .agent : .user)
    KustomerClient.shared.startNewConversation(
      initialMessage: initialMessage, customDescribe: customDescribe as? [String: String],
      afterCreateConversation: { result in
        afterCreateConversation?([result])
      }, animated: animated != 0)
  }

  func openChatAssistant(
    _ chatAssistantId: String,
    startDialog: String?,
    initialMessages: [String]? = nil,
    customDescribe: [String: Any]? = nil,
    afterFirstMessage: RCTResponseSenderBlock? = nil,  // gets `KUSConversation`
    animated: NSNumber?,
    resolve: @escaping RCTPromiseResolveBlock,
    reject: @escaping RCTPromiseRejectBlock
  ) {
    KustomerClient.shared.openChatAssistant(
      id: chatAssistantId,
      startDialog: startDialog,
      presentingVC: nil,
      completion: { result in
        switch result {
        case let .success(conversation):
          resolve(conversation)
        case let .failure(err):
          reject("\("RNKusomterChat")::OPEN_CHAT_ASSISTANT_ERROR", err.localizedDescription, err)
        }
      },
      afterFirstMessage: { _ in afterFirstMessage?(nil) },
      initialMessages: initialMessages,
      customDescribe: customDescribe,
      animated: animated != 0
    )
  }

  func changeActiveAssistant(_ activeAssistant: String) {
    KustomerClient.shared.changeActiveAssistant(ActiveAssistantOptions(activeAssistant))
  }

  func changeBusinessSchedule(
    _ scheduleId: String,
    resolve: @escaping RCTPromiseResolveBlock,
    reject: @escaping RCTPromiseRejectBlock
  ) {
    KustomerClient.shared.changeBusinessSchedule(
      scheduleId: scheduleId,
      completion: { result in
        switch result {
        case .success():
          resolve(nil)
        case let .failure(err):
          reject(
            "\("RNKusomterChat")::CH_BUSINESS_SCH", err.localizedDescription, err)
        }
      }
    )
  }

  func isChatAvailable(
    _ resolve: @escaping RCTPromiseResolveBlock,
    reject _: @escaping RCTPromiseRejectBlock
  ) { resolve(isChatAvailableSync()) }

  func isChatAvailableSync() -> Bool {
    return Kustomer.isChatAvailable() ?? false
  }

  func openConversation(
    _ id: String, animated: NSNumber?,
    resolve: @escaping RCTPromiseResolveBlock,
    reject: @escaping RCTPromiseRejectBlock
  ) {
    KustomerClient.shared.openConversation(
      id: id, animated: animated != 0,
      completion: { result in
        switch result {
        case let .success(conversation):
          resolve(conversation.toDictionary())
        case let .failure(err):
          reject("\("RNKusomterChat")::OPEN_CONVERSATION_ERROR", err.localizedDescription, err)
        }
      })
  }

  func showKbArticle(_ id: String) {
    KustomerClient.shared.showKbArticle(id: id)
  }

  func showKbCategory(_ id: String) {
    KustomerClient.shared.showKbCategory(id: id)
  }

  func getUnreadCount(
    _ resolve: @escaping RCTPromiseResolveBlock,
    reject: @escaping RCTPromiseRejectBlock
  ) {
    KustomerClient.shared.getUnreadCount(completion: { result in
      switch result {
      case let .success(count):
        resolve(count)
      case let .failure(err):
        reject("\("RNKusomterChat")::GET_UNREAD_COUNT_ERROR", err.localizedDescription, err)
      }
    })
  }

  func getOpenConversationCount(
    _ resolve: @escaping RCTPromiseResolveBlock,
    reject: @escaping RCTPromiseRejectBlock
  ) {
    KustomerClient.shared.getOpenConversationCount(completion: { result in
      switch result {
      case let .success(count):
        resolve(count)
      case let .failure(err):
        reject(
          "\("RNKusomterChat")::GET_OPEN_CONVERSATION_COUNT_ERROR", err.localizedDescription, err)
      }
    })
  }

  /*
   + (NSData *)APNSTokenDataFromNSString:(NSString *)token {
   NSString *string = [token lowercaseString];
   NSMutableData *data = [NSMutableData new];
   unsigned char whole_byte;
   char byte_chars[3] = {'\0', '\0', '\0'};
   NSUInteger i = 0;
   NSUInteger length = string.length;
   while (i < length - 1) {
   char c = [string characterAtIndex:i++];
   if (c < '0' || (c > '9' && c < 'a') || c > 'f') continue;
   byte_chars[0] = c;
   byte_chars[1] = [string characterAtIndex:i++];
   whole_byte = strtol(byte_chars, NULL, 16);
   [data appendBytes:&whole_byte length:1];
   }
   return data;
   }
   */
  // func APNSTokenDataFromNSString(token: String) -> Data {
  //   let string = token.lowercased()
  //   let data = NSMutableData()
  //   var whole_byte: UInt8 = 0
  //   var byte_chars: [CChar] = [0, 0, 0]
  //   var i = 0
  //   let length = string.count
  //   while i < length - 1 {
  //     let c = string[i]
  //     i += 1
  //     if c < "0" || (c > "9" && c < "a") || c > "f" {
  //       continue
  //     }
  //     byte_chars[0] = c
  //     byte_chars[1] = string[i]
  //     i += 1
  //     whole_byte = UInt8(strtol(byte_chars, nil, 16))
  //     data.append(&whole_byte, length: 1)
  //   }
  //   return data as Data
  // }

  //  func setPushToken(
  //    _ token: String, resolve: @escaping RCTPromiseResolveBlock,
  //    reject: @escaping RCTPromiseRejectBlock
  //  ) {
  //    KustomerClient.shared.pushProvider.didRegisterForRemoteNotifications(
  //      deviceToken: token.data(using: .utf16))
  //  }

  func onRemoteMessage(
    message: NSDictionary, resolve: @escaping RCTPromiseResolveBlock,
    reject: @escaping RCTPromiseRejectBlock
  ) {
    // todo
  }

  func unsetPushToken(
    _ resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock
  ) {
    KustomerClient.shared.deregisterCurrentDeviceForPushNotifications { result in
      switch result {
      case .success():
        resolve(nil)
      case let .failure(err):
        reject("\("RNKusomterChat")::DEREGISTER_PUSH_ERROR", err.localizedDescription, err)
      }
    }
  }
}

// extension AppDelegate: UNUserNotificationCenterDelegate {
//   func userNotificationCenter(
//     _ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse,
//     withCompletionHandler completionHandler: @escaping () -> Void
//   ) {
//     let threadId = response.notification.request.content.threadIdentifier
//     if threadId == "kustomer.app.chat" {
//       Kustomer.pushProvider.userNotificationCenter(
//         center, didReceive: response, withCompletionHandler: completionHandler)
//     }
//   }

//   func userNotificationCenter(
//     _ center: UNUserNotificationCenter, willPresent notification: UNNotification,
//     withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void
//   ) {
//     let threadId = notification.request.content.threadIdentifier
//     if threadId == "kustomer.app.chat" {
//       Kustomer.pushProvider.userNotificationCenter(
//         center, willPresent: notification, withCompletionHandler: completionHandler)
//     }
//   }
// }
