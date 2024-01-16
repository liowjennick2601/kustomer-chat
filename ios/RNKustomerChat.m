#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE (RNKustomerChat, NSObject)

RCT_EXTERN_METHOD(getProperties
                  : (RCTPromiseResolveBlock)resolve rejecter
                  : (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(isConfigured
                  : (RCTPromiseResolveBlock)resolve reject
                  : (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(configure
                  : (NSString *)apiKey options
                  : (NSDictionary *)options resolve
                  : (RCTPromiseResolveBlock)resolve reject
                  : (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(start
                  : (RCTPromiseResolveBlock)resolve reject
                  : (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(show : (NSString *)displayMode)
RCT_EXTERN_METHOD(close
                  : (NSNumber *)animated resolve
                  : (RCTPromiseResolveBlock)resolve reject
                  : (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(getStatus
                  : (RCTPromiseResolveBlock)resolve reject
                  : (RCTPromiseRejectBlock)reject)
RCT_EXTERN__BLOCKING_SYNCHRONOUS_METHOD(getStatusSync)
RCT_EXTERN_METHOD(isVisible
                  : (RCTPromiseResolveBlock)resolve reject
                  : (RCTPromiseRejectBlock)reject)
RCT_EXTERN__BLOCKING_SYNCHRONOUS_METHOD(isVisibleSync)
RCT_EXTERN_METHOD(isLoggedIn
                  : (NSString *)userEmailOrId resolve
                  : (RCTPromiseResolveBlock)resolve reject
                  : (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(logOut
                  : (RCTPromiseResolveBlock)resolve reject
                  : (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(logIn
                  : (NSString *)jwt resolve
                  : (RCTPromiseResolveBlock)resolve reject
                  : (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(logOutThenLogIn
                  : (NSString *)jwt resolve
                  : (RCTPromiseResolveBlock)resolve reject
                  : (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(startNewConversation
                  : (NSDictionary *)initialMessage customDescribe
                  : (NSDictionary *)customDescribe afterCreateConversation
                  : (RCTResponseSenderBlock)afterCreateConversation animated
                  : (NSNumber *)animated resolve
                  : (RCTPromiseResolveBlock)resolve reject
                  : (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(openChatAssistant
                  : (NSString *)id startDialog
                  : (NSString *)startDialog initialMessages
                  : (NSString[] *)initialMessages customDescribe
                  : (NSDictionary *)customDescribe animated
                  : (NSNumber *)animated afterFirstMessage
                  : (RCTResponseSenderBlock)afterFirstMessage resolve
                  : (RCTPromiseResolveBlock)resolve reject
                  : (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(changeActiveAssistant
                  : (NSString *)activeAssistant resolve
                  : (RCTPromiseResolveBlock)resolve reject
                  : (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(changeBusinessSchedule
                  : (NSString *)scheduleId resolve
                  : (RCTPromiseResolveBlock)resolve reject
                  : (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(getActiveAssistantId
                  : (RCTPromiseResolveBlock)resolve reject
                  : (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(getActiveAssistantStartDialog
                  : (RCTPromiseResolveBlock)resolve reject
                  : (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(isChatAvailable
                  : (RCTPromiseResolveBlock)resolve reject
                  : (RCTPromiseRejectBlock)reject)
RCT_EXTERN__BLOCKING_SYNCHRONOUS_METHOD(isChatAvailableSync)

RCT_EXTERN_METHOD(openConversation
                  : (NSString *)id animated
                  : (NSNumber *)animated resolve
                  : (RCTPromiseResolveBlock)resolve reject
                  : (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(showKbArticle : (NSString *)id)
RCT_EXTERN_METHOD(showKbCategory : (NSString *)id)
RCT_EXTERN_METHOD(getUnreadCount
                  : (RCTPromiseResolveBlock)resolve reject
                  : (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(getOpenConversationCount resolve
                  : (RCTPromiseResolveBlock)resolve reject
                  : (RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(setPushToken
                  : (NSString *)token resolve
                  : (RCTPromiseResolveBlock)resolve reject
                  : (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(onRemoteMessage
                  : (NSDictionary *)remoteMessage resolve
                  : (RCTPromiseResolveBlock)resolve reject
                  : (RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(unsetPushToken
                  : (RCTPromiseResolveBlock)resolve reject
                  : (RCTPromiseRejectBlock)reject)
@end
