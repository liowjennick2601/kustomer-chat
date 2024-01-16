import KustomerChat
import UIKit

@objcMembers
@objc(RNKustomerKbProvider)
class RNKustomerKbProvider: NSObject {
  static func requiresMainQueueSetup() -> Bool {
    return false
  }

  func constantsToExport() -> [AnyHashable: Any]! {
    return [:]
  }

  func getRootCategory(
    _ locale: String?, resolve: @escaping RCTPromiseResolve<[String: Any?]>,
    rejecter reject: @escaping RCTPromiseReject
  ) {
      reject("\("RNKusomterChat")::NO_IMPL", "Not implemented", nil)
      
  }

  func getFeaturedArticles(
    _ locale: String?, resolve: @escaping RCTPromiseResolve<[[String: Any?]]>,
    rejecter reject: @escaping RCTPromiseReject
  ) {
      reject("\("RNKusomterChat")::NO_IMPL", "Not implemented", nil)
      
  }

  func getCategory(
    _ categoryId: String, locale: String?, resolve: @escaping RCTPromiseResolve<[String: Any?]>,
    rejecter reject: @escaping RCTPromiseReject
  ) {
      reject("\("RNKusomterChat")::NO_IMPL", "Not implemented", nil)
  }
    
      func searchArticles(
    _ query: String, page: NSNumber?, pageSize: NSNumber?, locale: String?,
    resolve: @escaping RCTPromiseResolve<[[String: Any?]]>,
    rejecter reject: @escaping RCTPromiseReject
  ) {
      reject("\("RNKusomterChat")::NO_IMPL", "Not implemented", nil)

  }

  func getArticle(
    _ articleId: String, knowledgeBaseId: String?, locale: String?,
    resolve: @escaping RCTPromiseResolve<[String: Any?]>,
    rejecter reject: @escaping RCTPromiseReject
  ) {
      reject("\("RNKusomterChat")::NO_IMPL", "Not implemented", nil)
    }
}
