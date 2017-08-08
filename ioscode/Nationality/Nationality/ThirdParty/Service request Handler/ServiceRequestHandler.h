
#import <Foundation/Foundation.h>

@interface ServiceRequestHandler : NSObject

+(ServiceRequestHandler *)sharedRequestHandler;
-(void)getDemoList:(void(^)(BOOL status,NSObject *date)) handler;
-(void)getServiceData:(NSMutableDictionary*)param geturl:(NSString*)url getServiceDataCallBack:(void (^)(NSInteger status, NSObject *data))callBack;
-(void)getServiceData:(NSMutableDictionary*)param posturl:(NSString*)url getServiceDataCallBack:(void (^)(NSInteger status, NSObject *data))callBack;
-(void)getLoginServiceData:(NSMutableDictionary*)param posturl:(NSString*)url getServiceDataCallBack:(void (^)(NSInteger status, NSObject *data))callBack;
-(void)getServiceData:(NSMutableDictionary*)param puturl:(NSString*)url getServiceDataCallBack:(void (^)(NSInteger status, NSObject *data))callBack;
-(void)getService:(NSMutableDictionary*)param posturl:(NSString*)url getServiceDataCallBack:(void (^)(NSInteger status, NSObject *data))callBack;
-(void)getServiceData:(NSMutableDictionary*)param deleteurl:(NSString*)url getServiceDataCallBack:(void (^)(NSInteger status, NSObject *data))callBack;

-(void)getRequestCountServiceData:(NSMutableDictionary*)param geturl:(NSString*)url getServiceDataCallBack:(void (^)(NSInteger status, NSObject *data))callBack;
-(void)getConnectionListData:(NSMutableDictionary*)param geturl:(NSString*)url getServiceDataCallBack:(void (^)(NSInteger status, NSObject *data))callBack;
@end
