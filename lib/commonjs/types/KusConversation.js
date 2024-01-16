"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.KusChannelType = exports.ConversationStatus = exports.ConversationLockReason = void 0;
let KusChannelType = /*#__PURE__*/function (KusChannelType) {
  KusChannelType["conversation"] = "conversation";
  KusChannelType["sharedConversation"] = "shared-conversation";
  KusChannelType["sharedCustomer"] = "shared-customer";
  KusChannelType["sharedCustomerPresence"] = "shared-customer-presence";
  KusChannelType["metaConversation"] = "meta-conversation";
  KusChannelType["metaCustomer"] = "meta-customer";
  KusChannelType["pushNotification"] = "push-notification";
  return KusChannelType;
}({});
exports.KusChannelType = KusChannelType;
let ConversationStatus = /*#__PURE__*/function (ConversationStatus) {
  ConversationStatus["open"] = "open";
  ConversationStatus["ended"] = "ended";
  ConversationStatus["new"] = "new";
  return ConversationStatus;
}({});
exports.ConversationStatus = ConversationStatus;
let ConversationLockReason = /*#__PURE__*/function (ConversationLockReason) {
  ConversationLockReason["customerEnded"] = "customerEnded";
  ConversationLockReason["userEnded"] = "userEnded";
  return ConversationLockReason;
}({});
exports.ConversationLockReason = ConversationLockReason;
//# sourceMappingURL=KusConversation.js.map