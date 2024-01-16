#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE (RNKustomerPushProvider, NSObject)

RCT_EXTERN_METHOD(setToken : (NSString *)token)
RCT_EXTERN_METHOD(clearToken
                  : (RCTPromiseResolveBlock)resolve rejecter
                  : (RCTPromiseRejectBlock)reject)
@end
