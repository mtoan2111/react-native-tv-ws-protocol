//
//  TvWsProtocol.m
//  TvWsProtocol
//
//  Created by Nguyễn Mạnh Toàn on 10/24/20.
//  Copyright © 2020 Facebook. All rights reserved.
//

#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(TvWsProtocol, NSObject)

RCT_EXTERN_METHOD(create:(NSString *)uri options:(NSDictionary *)options)

@end

