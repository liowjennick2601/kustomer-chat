package com.kustomer.chatreactnative

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import com.kustomer.chatreactnative.providers.RNKustomerChatProvider
import com.kustomer.chatreactnative.providers.RNKustomerKbProvider
import com.kustomer.chatreactnative.providers.RNKustomerPushProvider
import com.kustomer.chatreactnative.providers.RNKustomerAuthProvider


class RNKustomerChatPackage : ReactPackage {
  override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
    return listOf(
      RNKustomerChat(reactContext),
      RNKustomerChatProvider(reactContext),
      RNKustomerKbProvider(reactContext),
      RNKustomerPushProvider(reactContext),
      RNKustomerAuthProvider(reactContext)
    )
  }

  override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
    return emptyList()
  }
}
