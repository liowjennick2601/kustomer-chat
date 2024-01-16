package com.kustomer.chatreactnative.providers

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule

abstract class RNKustomerAuthProviderSpec internal constructor(context: ReactApplicationContext) :
  ReactContextBaseJavaModule(context) {
    fun customerExists(promise: Promise) {
        promise.reject("${this.javaClass.name}.customerExists", "Not implemented")
    }
    abstract fun isLoggedIn(idType: String, id: String, promise: Promise) // KusTrackingIdentity
    abstract fun logIn(jwtToken: String, promise: Promise) // KusTrackingIdentity
    abstract fun logOut(promise: Promise) // void
}
