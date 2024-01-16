import KustomerChat
import UIKit

@objcMembers
@objc(RNKustomerAuthProvider)
class RNKustomerAuthProvider: NSObject {
  static func requiresMainQueueSetup() -> Bool {
    return false
  }

  func constantsToExport() -> [AnyHashable: Any]! {
    return [:]
  }

  func customerExists(
    _ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock
  ) {
    resolve(KustomerClient.shared.chatProvider.customerExists())
  }

  func isLoggedIn(
    _ idType: String,
    id: String,
    resolve: @escaping RCTPromiseResolveBlock,
    rejecter reject: @escaping RCTPromiseReject
  ) {
    switch idType {
    case "email":
      resolve(KustomerClient.shared.isLoggedIn(userEmail: id, userId: nil))
      break
    case "id":
      resolve(KustomerClient.shared.isLoggedIn(userEmail: nil, userId: id))
      break
    default:
      reject("\("RNKusomterChat")::IS_LOGGED_IN_ERROR", "Wrong idType passed: \(idType)", nil)
    }
  }

  func logIn(
    _ jwtToken: String,
    resolve: @escaping RCTPromiseResolveBlock,
    reject: @escaping RCTPromiseRejectBlock
  ) {
    KustomerClient.shared.logIn(
      jwt: jwtToken,
      { (error) in
        switch error {
        case .failure(let kError):
          reject("\("RNKusomterChat")::LOGIN_ERROR", "Unable to log in: \(kError)", kError)
          break
        case .success(let identity):
          resolve(identity.toDictionary())
        }
      })
  }

  func logOut() {
    KustomerClient.shared.logOut { _ in }
  }
}
