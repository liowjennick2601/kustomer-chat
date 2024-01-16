export class KustomerError extends Error {
  constructor(type, message, reason) {
    super(message);
    this.type = type;
    this.reason = reason;
  }
}
//# sourceMappingURL=KustomerError.js.map