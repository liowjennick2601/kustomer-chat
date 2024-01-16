package com.kustomer.chatreactnative.providers

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule

abstract class RNKustomerPushProviderSpec internal constructor(context: ReactApplicationContext) :
  ReactContextBaseJavaModule(context) {
    abstract fun setToken(token: String)
    abstract fun clearToken()
}
