import Foundation

public typealias RCTPromiseResolve<TResult> = (TResult) -> Void
public typealias RCTPromiseReject = (String?, String?, Error?) -> Void

struct RNPackage {
  var type: String
  var payload: [String: Any]?

  func asDict() -> [String: Any?] {
    return ["type": type, "payload": payload]
  }
}

// MARK: - Protocols

protocol DictionaryDecodable {
    init?(dict: [String: Any?])
}

protocol DictionaryEncodable {
    func toDictionary() -> [String: Any?]
}

// MARK: - Extensions

extension DictionaryDecodable {
    init?(dict: [String: Any?]) {
        guard let dict = dict as? [String: Any?] else { return nil }
        self.init(dict: dict)
    }
}

extension DictionaryEncodable {
    func toDictionary() -> [String: Any?] {
        return [:]
    }
}
