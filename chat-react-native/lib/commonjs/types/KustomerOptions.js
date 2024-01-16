"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.PushEnvironment = exports.PresenceType = exports.KustomerLogType = exports.KustomerConfigurationError = void 0;
let KustomerConfigurationError = /*#__PURE__*/function (KustomerConfigurationError) {
  KustomerConfigurationError["notEnoughDiskSpace"] = "notEnoughDiskSpace";
  return KustomerConfigurationError;
}({});
exports.KustomerConfigurationError = KustomerConfigurationError;
let PresenceType = /*#__PURE__*/function (PresenceType) {
  PresenceType["online"] = "online";
  PresenceType["offline"] = "offline";
  return PresenceType;
}({});
exports.PresenceType = PresenceType;
let PushEnvironment = /*#__PURE__*/function (PushEnvironment) {
  PushEnvironment["development"] = "development";
  PushEnvironment["production"] = "production";
  return PushEnvironment;
}({});
exports.PushEnvironment = PushEnvironment;
let KustomerLogType = /*#__PURE__*/function (KustomerLogType) {
  KustomerLogType["info"] = "info";
  KustomerLogType["error"] = "error";
  KustomerLogType["debug"] = "debug";
  KustomerLogType["warning"] = "warning";
  KustomerLogType["fatal"] = "fatal";
  return KustomerLogType;
}({});
exports.KustomerLogType = KustomerLogType;
//# sourceMappingURL=KustomerOptions.js.map