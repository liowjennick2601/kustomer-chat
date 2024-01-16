//
//  RNKustomerCodables.swift
//  RNKustomerChat
//
//  Created by Krisztián on 2023. 02. 16..
//  Copyright © 2023. Facebook. All rights reserved.
//

import Foundation
import KustomerChat

extension KustomerOptions: DictionaryEncodable {
  convenience init?(dict: [String: Any?]) {
    self.init()

    (dict["enableUI"] as? Bool).map { self.enableUI = $0 }
    (dict["showInAppNotifications"] as? Bool).map { self.showInAppNotifications = $0 }
    (dict["language"] as? String).map { self.language = KustomerLanguage(rawValue: $0) }
    (dict["activeAssistant"] as? String).map {
      self.activeAssistant = ActiveAssistantOptions(rawString: $0)
    }
    (dict["disablePush"] as? Bool).map { self.disablePush = $0 }
    (dict["pushBundleId"] as? String).map { self.pushBundleId = $0 }
    (dict["pushEnvironment"] as? String).map {
      self.pushEnvironment = PushEnvironment(rawValue: $0) ?? self.pushEnvironment
    }
    (dict["iosAnimate5Star"] as? Bool).map { self.showAnimationFor5StarCSATRatings = $0 }
    (dict["hostDomain"] as? String).map { self.hostDomain = $0 }
    (dict["businessScheduleId"] as? String).map { self.businessScheduleId = $0 }
    (dict["brandId"] as? String).map { self.brandId = $0 }
    (dict["logLevel"] as? String).map { self.logLevels = KustomerLogType.parse($0) }
    (dict["hideNewConversationButton"] as? Bool).map { self.hideNewConversationButton = $0 }
    (dict["hideKbArticleShare"] as? Bool).map { self.hideKbArticleShare = $0 }
    (dict["hideHistoryNavigation"] as? Bool).map { self.hideHistoryNavigation = $0 }
  }
}

extension KustomerDisplayMode {
  init(from: String) {
    switch from {
    case "chatHistory": self = .chatHistory
    case "newChat": self = .newChat
    case "activeChat": self = .activeChat
    case "knowledgeBase": self = .knowledgeBase
    case "knowledgeBaseHome": self = .knowledgeBaseHome
    case "onlyChat": self = .onlyChat
    case "onlyKnowledgeBase": self = .onlyKnowledgeBase
    default: self = .default
    }
  }
}

extension KustomerLogType {
  static func parse(_ from: String) -> [KustomerLogType] {
    switch from {
    case "info": return [.info]
    case "error": return [.info, .error]
    case "debug": return [.info, .error, .debug]
    case "all": return [.info, .error, .debug, .warning, .fatal]
    default:
      print("Invalid log type: '\(from)'. Defaulting to 'info'")
      return [.info]
    }
  }
}

//extension PushEnvironment {
//    init?(rawValue: String) {
//
//    }
//}

extension ActiveAssistantOptions {
  init(rawString: String) {
    switch rawString {
    case "none": self = .none
    case "orgDefault": self = .orgDefault
    default: self = .withId(rawString)
    }
  }
}

extension KustomerClientStatus: DictionaryDecodable {
  func toDictionary() -> [String: Any?] {
    switch self {
    case .configurationError(let error):
      return ["type": "configurationError", "payload": error.debugDescription]
    default: return ["type": self]
    }
  }
}

extension KustomerLogType {
  init(type: String) {
    switch type {
    case "info": self = .info
    case "error": self = .error
    case "debug": self = .debug
    case "warning": self = .warning
    case "fatal": self = .fatal
    default:
      print("Invalid log type: '\(type)'. Defaulting to 'info'")
      self = .info
    }
  }
}

extension KustomerChat.PresenceType {
    init(type: String) {
        switch type {
        case "online": self = .online
        case "offline": self = .offline
        default:
          print("Invalid presence type: '\(type)'. Defaulting to 'online'")
          self = .online
        }
        }
}

extension KustomerChat.KUSConversation: DictionaryEncodable {
  func toDictionary() -> [String: Any?] {
    return [
      "id": id,
      "createdAt": createdAt,
      "lastMessageAt": lastMessageAt,
      //      "status": status,
      "lockedAt": lockedAt,
      "lockedByCustomer": lockedByCustomer,
      "lockReason": lockReason,
      //      "preview": preview,
      "respondingUserIds": respondingUserIds,
      "unreadMessageCount": unreadMessageCount,
      //      "customers": customers,
      "users": [
        "id": nil, "displayName": previewDetails?.lastResponder?.displayName,
        "avatarUrl": previewDetails?.lastResponder?.avatarUrl,
      ],
    ]
  }
}

extension KustomerChat.KUSChatMessage: DictionaryEncodable {
  func toDictionary() -> [String: Any?] {
    return [
      "id": id,
      "conversationId": conversationId,
      "createdAt": createdAt,
      "messageType": messageType,
      "direction": direction,
      "directionType": directionType,
      "body": body,
      "attachments": attachments.map { $0.toDictionary() },
      "templateType": templateType,
      "sentByUser": sentByUser?.toDictionary(),
      "sentById": sentById,
    ]
  }
}

extension KustomerChat.KUSSatisfaction: DictionaryEncodable {
  func toDictionary() -> [String: Any?] {
    return [:]
  }
}

extension KustomerChat.KUSChatAttachment: DictionaryEncodable {
  func toDictionary() -> [String: Any?] {
    return [
      "id": id,
      "conversationId": conversationId,
      "contentType": contentType,
      "name": name,
      "url": url,
    ]
  }
}

extension KustomerChat.KUSUser: DictionaryEncodable {
  func toDictionary() -> [String: Any?] {
    return [
      "id": id,
      "displayName": displayName,
      "avatarUrl": avatarUrl,
    ]
  }
}

extension KustomerChat.KUSTrackingIdentity: DictionaryEncodable {
  func toDictionary() -> [String: Any?] {
    return [
      "id": id,
      "customerId": customerId,
      "trackingId": trackingId,
      "trackingToken": trackingToken,
      "email": email,
      "externalId": externalId,
      "verified": verified,
      "verifiedAt": verifiedAt,
      "createdAt": createdAt,
      "updatedAt": updatedAt,
    ]
  }
}

extension KustomerClientStatus {
  public var rnValue: String {
    switch self {
    case let .configurationError(e):
      return "ERROR :: \(e?.localizedDescription ?? "Unknown") :: \(e.debugDescription)"
    case .configured: return "configured"
    case .uninitialized: return "uninitialized"
    case .starting: return "starting"
    case .started: return "started"
    case .stopped: return "stopped"
    case .connected: return "connected"
    case .disconnected: return "disconnected"
    @unknown default: return "unknown"
    }
  }
}

extension ActiveAssistantOptions {
  init(_ from: String) {
    switch from {
    case "none": self = .none
    case "orgDefault": self = .orgDefault
    default: self = .withId(from)
    }
  }

  public var rnValue: String {
    switch self {
    case .none:
      return "none"
    case .orgDefault:
      return "orgDefault"
    case .withId(let id):
      return id
    default:
      return "unknown"
    }
  }
}

extension KustomerChat.KUSChatSettings: DictionaryEncodable {
  func toDictionary() -> [String: Any?] {
      let dict = dictionaryRepresentation
      return dict
//    return [
//        "id": dict["id"] as? ,
//        "widgetType": dict["widgetType"] as? ?.rawValue,
//        "teamName": dict["teamName"] as? ,
//        "teamIconUrl": dict["teamIconUrl"] as? ,
//        "activeFormId": dict["activeFormId"] as? ,
//        "enabled": dict["enabled"] as? ,
//        "greeting": dict["greeting"] as? ,
//        "activeAssistant": dict["activeAssistant"] as? ,
//        "brandId": dict["brandId"] as? ,
//        "knowledgeBaseId": dict["knowledgeBaseId"] as? ,
//        "availability": dict["availability"] as? ?.rawValue,
//        "offHoursImageUrl": dict["offHoursImageUrl"] as? ,
//        "offHoursMessage": dict["offHoursMessage"] as? ,
//        "volumeControl": dict["volumeControl"] as? ?.toDictionary(),
//        "defaultWaitMessage": dict["defaultWaitMessage"] as? ,
//        "closableChat": dict["closableChat"] as? ,
//        "noHistory": dict["noHistory"] as? ,
//        "autoreply": dict["autoreply"] as? ,
//        "showEmailInputBanner": dict["showEmailInputBanner"] as? ,
//        "embedIconUrl": dict["embedIconUrl"] as? ,
//        "pusherAccessKey": dict["pusherAccessKey"] as? ,
//        "campaignsEnabled": dict["campaignsEnabled"] as? ,
//        "showBrandingIdentifier": dict["showBrandingIdentifier"] as? ,
//        "embedIconColor": dict["embedIconColor"] as? ,
//        "assistantRules": dict["assistantRules"] as? .map { $0.toDictionary() },
//        "disableAttachments": dict["disableAttachments"] as? ,
//        "outboundChatEnabled": dict["outboundChatEnabled"] as? ,
//        "showTypingIndicatorWeb": dict["showTypingIndicatorWeb"] as? ,
//        "showTypingIndicatorCustomerWeb": dict["showTypingIndicatorCustomerWeb"] as? ,
//        "singleSessionChat": dict["singleSessionChat"] as? ,
//        "sharedPubNubKeySet": dict["sharedPubNubKeySet"] as? ?.toDictionary(),
//        "orgPubNubKeySets": dict["orgPubNubKeySets"] as? .map { $0.toDictionary() },
//        "sentry": dict["sentry"] as? ?.toDictionary(),
//    ]
  }
}
