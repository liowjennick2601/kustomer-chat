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
export const Mock = new Stub();
//# sourceMappingURL=mock.js.map