"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.useKustomerEvent = exports.KustomerEventEmitter = void 0;
var _reactNative = require("react-native");
var _react = require("react");
const KustomerEventEmitter = new _reactNative.NativeEventEmitter(_reactNative.NativeModules.RNKustomerEventEmitter);
exports.KustomerEventEmitter = KustomerEventEmitter;
const useKustomerEvent = (event, callback) => {
  (0, _react.useEffect)(() => {
    const listener = KustomerEventEmitter.addListener(event, callback);
    return () => listener.remove();
  }, [event, callback]);
};
exports.useKustomerEvent = useKustomerEvent;
//# sourceMappingURL=KustomerEventEmitter.js.map