import KustomerChat
import UIKit

@objcMembers
@objc(RNKustomerPushProvider)
class RNKustomerPushProvider: NSObject {
    static func requiresMainQueueSetup() -> Bool {
      return true
    }
    
    func setToken(token: String) {
        let data = token.data(using: .utf8)!
        KustomerClient.shared.pushProvider.didRegisterForRemoteNotifications(deviceToken: data)
    }

    func clearToken(_ resolve: @escaping RCTPromiseResolve<Void?>, rejecter reject: @escaping RCTPromiseRejectBlock) {
        KustomerClient.shared.pushProvider.deregisterCurrentDeviceForPushNotifications { result in
            switch result {
            case .success:
                resolve(nil)
            case .failure(let error):
                reject(error.localizedDescription, nil, error)
            }
        }
    }
}
