package com.kustomer.chatreactnative.providers

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.*

class RNKustomerAuthProvider(reactContext: ReactApplicationContext) :
  RNKustomerAuthProviderSpec(reactContext) {
  companion object {
    const val NAME = "RNKustomerAuthProvider"
  }
  override fun getName() = NAME

  @ReactMethod
  override fun isLoggedIn(idType: String, id:String, promise: Promise) {
    // todo
  }

  @ReactMethod
  override fun logIn(jwtToken: String, promise: Promise) {
    // todo
  }

  @ReactMethod
  override fun logOut(promise: Promise) {
    // todo
  }
}
