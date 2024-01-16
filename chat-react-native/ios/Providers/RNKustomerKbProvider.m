#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE (RNKustomerKbProvider, NSObject)

RCT_EXTERN_METHOD(getRootCategory
                  : (NSString *)locale resolver
                  : (RCTPromiseResolveBlock)resolve rejecter
                  : (RCTPromiseRejectBlock)reject)

@end
