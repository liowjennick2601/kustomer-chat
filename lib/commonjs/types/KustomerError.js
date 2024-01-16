"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.KustomerError = void 0;
class KustomerError extends Error {
  constructor(type, message, reason) {
    super(message);
    this.type = type;
    this.reason = reason;
  }
}
exports.KustomerError = KustomerError;
//# sourceMappingURL=KustomerError.js.map