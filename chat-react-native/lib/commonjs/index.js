"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
var _exportNames = {};
Object.defineProperty(exports, "default", {
  enumerable: true,
  get: function () {
    return _NativeModules.KustomerChat;
  }
});
var _KustomerEventEmitter = require("./KustomerEventEmitter");
Object.keys(_KustomerEventEmitter).forEach(function (key) {
  if (key === "default" || key === "__esModule") return;
  if (Object.prototype.hasOwnProperty.call(_exportNames, key)) return;
  if (key in exports && exports[key] === _KustomerEventEmitter[key]) return;
  Object.defineProperty(exports, key, {
    enumerable: true,
    get: function () {
      return _KustomerEventEmitter[key];
    }
  });
});
var _types = require("./types");
Object.keys(_types).forEach(function (key) {
  if (key === "default" || key === "__esModule") return;
  if (Object.prototype.hasOwnProperty.call(_exportNames, key)) return;
  if (key in exports && exports[key] === _types[key]) return;
  Object.defineProperty(exports, key, {
    enumerable: true,
    get: function () {
      return _types[key];
    }
  });
});
var _isKustomerConfigured = require("./hooks/isKustomerConfigured");
Object.keys(_isKustomerConfigured).forEach(function (key) {
  if (key === "default" || key === "__esModule") return;
  if (Object.prototype.hasOwnProperty.call(_exportNames, key)) return;
  if (key in exports && exports[key] === _isKustomerConfigured[key]) return;
  Object.defineProperty(exports, key, {
    enumerable: true,
    get: function () {
      return _isKustomerConfigured[key];
    }
  });
});
var _NativeModules = require("./NativeModules");
Object.keys(_NativeModules).forEach(function (key) {
  if (key === "default" || key === "__esModule") return;
  if (Object.prototype.hasOwnProperty.call(_exportNames, key)) return;
  if (key in exports && exports[key] === _NativeModules[key]) return;
  Object.defineProperty(exports, key, {
    enumerable: true,
    get: function () {
      return _NativeModules[key];
    }
  });
});
//# sourceMappingURL=index.js.map