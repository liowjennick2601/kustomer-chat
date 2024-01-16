import { NativeEventEmitter, NativeModules } from 'react-native';
import { useEffect } from 'react';
export const KustomerEventEmitter = new NativeEventEmitter(NativeModules.RNKustomerEventEmitter);
export const useKustomerEvent = (event, callback) => {
  useEffect(() => {
    const listener = KustomerEventEmitter.addListener(event, callback);
    return () => listener.remove();
  }, [event, callback]);
};
//# sourceMappingURL=KustomerEventEmitter.js.map