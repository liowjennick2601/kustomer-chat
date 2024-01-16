import { useEffect, useState } from 'react';
import { KustomerChat } from '../NativeModules';
export function useIsKustomerConfigured() {
  let checkInterval = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : 1000;
  const [isConfigured, setIsConfigured] = useState(null);
  useEffect(() => {
    if (isConfigured === true) return;
    const updateTimer = setInterval(() => KustomerChat.isConfigured().then(setIsConfigured), checkInterval);
    return () => clearTimeout(updateTimer);
  }, [isConfigured, checkInterval]);
  return isConfigured;
}
//# sourceMappingURL=isKustomerConfigured.js.map