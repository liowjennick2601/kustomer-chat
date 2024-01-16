package com.kustomer.chatreactnative.providers

import com.facebook.react.bridge.*

class RNKustomerPushProvider(reactContext: ReactApplicationContext) :
  RNKustomerPushProviderSpec(reactContext) {
  companion object {
    const val NAME = "RNKustomerPushProvider"
  }
  override fun getName() = NAME

  @ReactMethod
  override fun setToken(token: String) {
    // todo
  }

  @ReactMethod
  override fun clearToken() {
    // todo
  }
}
