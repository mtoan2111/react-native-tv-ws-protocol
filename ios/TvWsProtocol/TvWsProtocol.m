//
//  TvWsProtocol.m
//  TvWsProtocol
//
//  Created by Nguyễn Mạnh Toàn on 10/27/20.
//
#import <React/RCTBridgeModule.h>
#import <React//RCTEventEmitter.h>

@interface RCT_EXTERN_MODULE(TvWsProtocol, RCTEventEmitter)

RCT_EXTERN_METHOD(create:(NSString *)uri options:(NSDictionary *)uri )

RCT_EXTERN_METHOD(send:(NSString *)message)

RCT_EXTERN_METHOD(close)

@end
