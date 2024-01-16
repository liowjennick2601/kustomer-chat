<img align="left" width="80" height="80" src="https://files.readme.io/7859feb-small-Group_4.png" title="Kustomer logo" float="left" xstyle="width:356px;height:61px"/>

# Kustomer Chat React Native SDK

Embed [Kustomer](https://www.kustomer.com/) in your own products with our chat SDK for React Native.

---

## Installation

```sh
yarn add @kustomer/chat-react-native
npx pod-install
```

## Known Issues

### iOS

In order to fix a dependency build issue, a `postinstall` script will be ran, that will edit your `Podfile` to add a `post_install` hook that will override the `build_type` of `PubNubSwift`.

## Usage

### Setup

You can configure the SDK either in your `AppDelegate.m` or in your JS/TS sources.
To configure the SDK in your `AppDelegate.m`:

First, `import KustomerChat`
Then add a call to Kustomer via your `didFinishLaunchingWithOptions`:

```objc
import KustomerChat

// if you use the following API key, you will be able to chat with one of the RN SDK developers when they're online
NSString * const apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYzNTY1MjI1ODdiOTU1YmYzNDFlZDU3ZCIsInVzZXIiOiI2MzU2NTIyNTMyZDlkODYzOWNkMTMwMmMiLCJvcmciOiI2MzUxN2E2YzVmMjdiY2MzNDFjMjQzZDEiLCJvcmdOYW1lIjoienp6LWtmLXN0YWdpbmciLCJ1c2VyVHlwZSI6Im1hY2hpbmUiLCJwb2QiOiJzdGFnaW5nIiwicm9sZXMiOlsib3JnLnRyYWNraW5nIl0sImF1ZCI6InVybjpjb25zdW1lciIsImlzcyI6InVybjphcGkiLCJzdWIiOiI2MzU2NTIyNTMyZDlkODYzOWNkMTMwMmMifQ.RcCT4lIYUXd5pxVE0Srp3jawlbJXoj7O0qW2WV-NC1g";

func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
  Kustomer.configure(apiKey: process.env.KUSTOMER_API_KEY, options: nil, launchOptions: launchOptions)
}
```

You can configure the SDK this way too, follow the remainder of the `iOS` section [here](https://developer.kustomer.com/chat-sdk/v2-iOS/docs/installation#initialize-the-chat-ios-sdk-with-options)

Due to a late init issue, for Android, you want to configure the SDK in your `MainApplication.java/kt` for now:

```java
public void onCreate() {
  // ...
  KustomerChat.configure(apiKey, options);
}
```

### Currently not the best way to configure the SDK

Another way to configure the SDK is to do it in your JS/TS sources:

```ts
import KustomerChat from '@kustomer/chat-react-native';

KustomerChat.configure(apiKey, options);
```

### Permissions iOS

Add the following to your `Info.plist` for file upload support:

```txt
Privacy - Camera Usage Description
Privacy - Media Library Usage Description
Privacy - Microphone Usage Description
Privacy - Photo Library Usage Description
```

### Authentication

For auth setup, see the [iOS docs](https://developer.kustomer.com/chat-sdk/v2.7.3-iOS/docs/authentication)

### Open a conversation dialog

```ts
import KustomerChat from '@kustomer/chat-react-native';

KustomerChat.show(KustomerDisplayMode); // KustomerDisplayMode is an optional enum param
```

### Custom colors

For Android, see the [Android docs](https://developer.kustomer.com/chat-sdk/v2-Android/docs/customize-colors-updated)
For iOS, see the [iOS docs](https://developer.kustomer.com/chat-sdk/v2.7.4-iOS/docs/customize-colors)

We are working on a way to customize colors in JS/TS sources, once, but it is currently not possible with the native SDKs.

## License

You can view the license [here](https://www.kustomer.com/legal/supplemental-terms/)

## Notes

> WIP: Most of the APIs are stable, and no major changes are expected in the way the SDK works.

### Push notifications

#### iOS

- should request permission using another lib, like `react-native-permissions` or `react-native-push-notification` or `react-native-firebase`
- check the iOS docs for [Push Keys and Certificates](https://developer.kustomer.com/chat-sdk/v2.7.6-iOS/docs/push-certificates) and [Push Notifications](https://developer.kustomer.com/chat-sdk/v2.7.6-iOS/docs/push-notifications) and modify your `AppDelegate.m` accordingly.

> ℹ️ **Note:** this will likely match android's behavior in the future

#### Android

- use a PN library (like `react-native-firebase`), and call `setPushToken` once a token is received.
- call `KustomerChat.onRemoteMessage` in your `onRemoteMessage` callback. You can use `isKustomerNotification` to check if the message is from Kustomer, and should be forwarded.
