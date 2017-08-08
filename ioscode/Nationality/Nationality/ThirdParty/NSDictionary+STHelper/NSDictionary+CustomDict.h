//
//  NSDictionary+CustomDict.h
//  HooruuApp
//
//  Created by Sarfaraj biswas on 27/11/13.
//  Copyright (c) 2013 Sarfaraj biswas. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSDictionary (CustomDict)

- (id)objectForKeyCheckNull:(id)aKey;
- (id)objectForKeyOrNil:(id)akey;


- (BOOL)isObjectForKeyNil:(id)akey;
- (BOOL)isObjectForKeyArray:(id)akey;
- (BOOL)isObjectForKeyString:(id)akey;
- (BOOL)isObjectForKeyDict:(id)akey;

//
- (NSString *)objectForKeyString:(id)akey;
- (NSArray *)objectForKeyArray:(id)akey;
- (NSDictionary *)objectForKeyDict:(id)akey;
- (BOOL)objectForKeyBool:(id)akey;
- (NSInteger)objectForKeyInt:(id)akey;
- (id)objectOrNilForKey:(id)akey;

@end
