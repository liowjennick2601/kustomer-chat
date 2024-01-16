"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.useIsKustomerConfigured = useIsKustomerConfigured;
var _react = require("react");
var _NativeModules = require("../NativeModules");
function useIsKustomerConfigured() {
  let checkInterval = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : 1000;
  const [isConfigured, setIsConfigured] = (0, _react.useState)(null);
  (0, _react.useEffect)(() => {
    if (isConfigured === true) return;
    const updateTimer = setInterval(() => _NativeModules.KustomerChat.isConfigured().then(setIsConfigured), checkInterval);
    return () => clearTimeout(updateTimer);
  }, [isConfigured, checkInterval]);
  return isConfigured;
}
//# sourceMappingURL=isKustomerConfigured.js.map