package com.kustomer.chatreactnative.providers

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactMethod

class RNKustomerKbProvider(reactContext: ReactApplicationContext) :
  RNKustomerKbProviderSpec(reactContext) {
  companion object {
    const val NAME = "RNKustomerKbProvider"
  }
  override fun getName() = NAME

  @ReactMethod
  override fun getRootCategory(locale: String?, promise: Promise) {
    promise.resolve(null)
  }
}
