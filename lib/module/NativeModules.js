import { NativeModules, Platform } from 'react-native';
import { KustomerDisplayMode } from './types';
function getLinkingErrorText(moduleName) {
  return `The package '@kustomer/chat-react-native' doesn't seem to be linked.\n` + `Looking for module '${moduleName}'. Make sure: \n\n` + Platform.select({
    ios: "- You have run 'pod install'\n",
    default: ''
  }) + '- You rebuilt the app after installing the package\n' + '- You are not using Expo Go\n';
}
function getLinkingErrorProxy(moduleName) {
  return new Proxy({}, {
    get() {
      // throw new Error(getLinkingErrorText(moduleName));
      console.error(new Error(getLinkingErrorText(moduleName)));
      return null;
    }
  });
}
const RNKustomerChatProvider = NativeModules.RNKustomerChatProvider ?? getLinkingErrorProxy('RNKustomerChatProvider');
export const KustomerChatProvider = RNKustomerChatProvider;
const RNKustomerAuthProvider = NativeModules.RNKustomerAuthProvider ?? getLinkingErrorProxy('RNKustomerAuthProvider');
export const KustomerAuthProvider = RNKustomerAuthProvider;
const RNKustomerPushProvider = NativeModules.RNKustomerPushProvider ?? getLinkingErrorProxy('RNKustomerPushProvider');
export const KustomerPushProvider = RNKustomerPushProvider;
const RNKustomerKbProvider = NativeModules.RNKustomerKbProvider ?? getLinkingErrorProxy('RNKustomerKbProvider');
export const KustomerKbProvider = RNKustomerKbProvider;
const RNKustomerChat = NativeModules.RNKustomerChat ?? getLinkingErrorProxy('RNKustomerChat');
export const KustomerChat = {
  ...RNKustomerChat,
  configure(apiKey, options) {
    return RNKustomerChat.configure(apiKey, options).then(() => {
      if (options !== null && options !== void 0 && options.disablePush || options !== null && options !== void 0 && options.skipFCMPushRegistration) return;
      try {
        const firebaseMessaging = require('@react-native-firebase/messaging').default;
        async function setToken(token) {
          const v = await Platform.select({
            ios: firebaseMessaging().getAPNSToken(),
            android: Promise.resolve(token)
          });
          RNKustomerChat.setPushToken(v);
        }
        Platform.select({
          ios: firebaseMessaging().getAPNSToken(),
          android: firebaseMessaging().getToken()
        }).then(RNKustomerChat.setPushToken);
        firebaseMessaging().onTokenRefresh(setToken);
        firebaseMessaging().onMessage(message => {
          KustomerChat.onRemoteMessage(message);
        });
      } catch (e) {
        if ((options === null || options === void 0 ? void 0 : options.disablePush) !== true) console.warn('Kustomer: @react-native-firebase/messaging is not installed. Push notifications will not work.');
        return;
      }
    });
  },
  show() {
    let displayMode = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : KustomerDisplayMode.default;
    RNKustomerChat.show(displayMode);
  },
  isKustomerNotification(message) {
    var _message$data;
    return ((_message$data = message.data) === null || _message$data === void 0 || (_message$data = _message$data.name) === null || _message$data === void 0 ? void 0 : _message$data.includes('kustomer')) ?? false;
  }
};
//# sourceMappingURL=NativeModules.js.map