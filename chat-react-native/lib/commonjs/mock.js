"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.Mock = void 0;
// const Noop: () => any = () => {};
class Stub {
  constructor() {
    return new Proxy(() => {}, {
      get: () => new Stub(),
      apply: () => new Stub(),
      set: () => true
    });
  }
}

// very basic, not too useful starter mock, will expand with more useful ones
// todo
const Mock = new Stub();
exports.Mock = Mock;
//# sourceMappingURL=mock.js.map