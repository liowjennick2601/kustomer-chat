"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.KusChatMessageDirection = void 0;
var KusWeekday = /*#__PURE__*/function (KusWeekday) {
  KusWeekday["sunday"] = "0";
  KusWeekday["monday"] = "1";
  KusWeekday["tuesday"] = "2";
  KusWeekday["wednesday"] = "3";
  KusWeekday["thursday"] = "4";
  KusWeekday["friday"] = "5";
  KusWeekday["saturday"] = "6";
  KusWeekday["none"] = "none";
  return KusWeekday;
}(KusWeekday || {});
/* kotlin spec for KustomerChatProvider:


*/
let KusChatMessageDirection = /*#__PURE__*/function (KusChatMessageDirection) {
  KusChatMessageDirection["agent"] = "out";
  KusChatMessageDirection["user"] = "in";
  return KusChatMessageDirection;
}({});
exports.KusChatMessageDirection = KusChatMessageDirection;
var KusActionValueType = /*#__PURE__*/function (KusActionValueType) {
  KusActionValueType["text"] = "text";
  KusActionValueType["emoji"] = "emoji";
  return KusActionValueType;
}(KusActionValueType || {});
var KusPresenceType = /*#__PURE__*/function (KusPresenceType) {
  KusPresenceType["online"] = "online";
  KusPresenceType["offline"] = "offline";
  return KusPresenceType;
}(KusPresenceType || {});
var KusCsatStatus = /*#__PURE__*/function (KusCsatStatus) {
  KusCsatStatus["offered"] = "offered";
  KusCsatStatus["rated"] = "rated";
  KusCsatStatus["commented"] = "commented";
  KusCsatStatus["scheduled"] = "scheduled";
  KusCsatStatus["unresponded"] = "unresponded";
  KusCsatStatus["canceled"] = "canceled";
  return KusCsatStatus;
}(KusCsatStatus || {});
var KusCsatQuestionType = /*#__PURE__*/function (KusCsatQuestionType) {
  KusCsatQuestionType["text"] = "text";
  KusCsatQuestionType["radio"] = "radio";
  KusCsatQuestionType["checkbox"] = "checkbox";
  return KusCsatQuestionType;
}(KusCsatQuestionType || {});
var KusSatisfactionScaleType = /*#__PURE__*/function (KusSatisfactionScaleType) {
  KusSatisfactionScaleType["number"] = "number";
  KusSatisfactionScaleType["emoji"] = "emoji";
  KusSatisfactionScaleType["thumb"] = "thumb";
  KusSatisfactionScaleType["unknown"] = "unknown";
  return KusSatisfactionScaleType;
}(KusSatisfactionScaleType || {});
var KusChatAvailability = /*#__PURE__*/function (KusChatAvailability) {
  KusChatAvailability["online"] = "online";
  KusChatAvailability["offline"] = "offline";
  KusChatAvailability["none"] = "none";
  KusChatAvailability["disabled"] = "disabled";
  return KusChatAvailability;
}(KusChatAvailability || {});
var KusPreferredView = /*#__PURE__*/function (KusPreferredView) {
  KusPreferredView["default"] = "default";
  KusPreferredView["chatHistory"] = "chatHistory";
  KusPreferredView["chat"] = "chat";
  KusPreferredView["kb"] = "kb";
  KusPreferredView["chatKb"] = "chatKb";
  return KusPreferredView;
}(KusPreferredView || {});
var KusSessionProperty = /*#__PURE__*/function (KusSessionProperty) {
  KusSessionProperty["language"] = "language";
  KusSessionProperty["userAgent"] = "userAgent";
  KusSessionProperty["currentPage"] = "currentPage";
  KusSessionProperty["previousPage"] = "previousPage";
  KusSessionProperty["timeOnPage"] = "timeOnPage";
  return KusSessionProperty;
}(KusSessionProperty || {});
var KusOperator = /*#__PURE__*/function (KusOperator) {
  KusOperator["equals"] = "equals";
  KusOperator["notEquals"] = "notEquals";
  KusOperator["greaterThan"] = "greaterThan";
  KusOperator["greaterThanOrEqual"] = "greaterThanOrEqual";
  KusOperator["lessThan"] = "lessThan";
  KusOperator["lessThanOrEqual"] = "lessThanOrEqual";
  KusOperator["isIn"] = "isIn";
  KusOperator["isNotIn"] = "isNotIn";
  KusOperator["contains"] = "contains";
  KusOperator["doesNotContain"] = "doesNotContain";
  KusOperator["containsAnyOf"] = "containsAnyOf";
  KusOperator["doesNotContainAnyOf"] = "doesNotContainAnyOf";
  KusOperator["isSet"] = "isSet";
  KusOperator["isNotSet"] = "isNotSet";
  return KusOperator;
}(KusOperator || {});
var KusConversationCount = /*#__PURE__*/function (KusConversationCount) {
  KusConversationCount["all"] = "all";
  KusConversationCount["done"] = "done";
  KusConversationCount["open"] = "open";
  KusConversationCount["snoozed"] = "snoozed";
  return KusConversationCount;
}(KusConversationCount || {});
//# sourceMappingURL=KustomerChatProvider.js.map