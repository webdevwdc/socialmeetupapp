
#import "ServiceRequestHandler.h"


@implementation ServiceRequestHandler

static ServiceRequestHandler *sharedRequestHandler = nil;

+(ServiceRequestHandler *)sharedRequestHandler {
    @synchronized (self) {
        if (sharedRequestHandler == nil) {
            sharedRequestHandler = [[self alloc] init];
        }
    }
    
    return sharedRequestHandler;
}


-(id)init {
    if (self = [super init]) {
        
    }
    return self;
}


-(void)getServiceData:(NSMutableDictionary*)param geturl:(NSString*)url getServiceDataCallBack:(void (^)(NSInteger status, NSObject *data))callBack {
    
    [SVProgressHUD showWithStatus:@"loading" maskType:SVProgressHUDMaskTypeClear];
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
   
    [manager GET:url parameters:param success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         NSLog(@"%@",responseObject);
          [SVProgressHUD dismiss];
         NSDictionary *response = (NSDictionary *)responseObject;
         int statusCode = [[[response valueForKey:@"result"]valueForKey:@"error"] intValue];
         statusCode=0;
         if (statusCode==0)
         {
             
             callBack (statusCode,[response valueForKey:@"result"]);
             
         }
         else
         {
             
             callBack (statusCode,[[response valueForKey:@"result"] st_stringForKey:@"message"]);
             
         }
         
     }
          failure:^(AFHTTPRequestOperation *operation, NSError *error) {
              [SVProgressHUD dismiss];
              NSString *strFailure = [error localizedDescription];
              
              if([strFailure isEqualToString:@"Could not connect to the server."])
              {
             
                  callBack(2, @"Could not connect to the server.");
                  
              }
              else{
                  
                  callBack(2, strFailure);
              }
          }];

}

-(void)getServiceData:(NSMutableDictionary*)param puturl:(NSString*)url getServiceDataCallBack:(void (^)(NSInteger status, NSObject *data))callBack {
    
    
    [SVProgressHUD showWithStatus:@"loading" maskType:SVProgressHUDMaskTypeClear];
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    
    [manager PUT:url parameters:param success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         
         NSLog(@"%@",responseObject);
         [SVProgressHUD dismiss];
         NSDictionary *response = (NSDictionary *)responseObject;
         int statusCode = [[[response valueForKey:@"result"]valueForKey:@"error"] intValue];
         statusCode=0;
         if (statusCode==1)
         {
             callBack (statusCode,[[response valueForKey:@"result"] st_stringForKey:@"message"]);
             
         }
         else
         {
             callBack (statusCode,[response valueForKey:@"result"]);
             
         }
         
     }
         failure:^(AFHTTPRequestOperation *operation, NSError *error) {
             [SVProgressHUD dismiss];
             NSString *strFailure = (NSString *)[error localizedDescription];
             
             if([strFailure isEqualToString:@"Could not connect to the server."])
             {
                 callBack(2, @"Could not connect to the server.");
             }
             else{
                 callBack(2, strFailure);
             }
         }];
    
}

-(void)getServiceData:(NSMutableDictionary*)param posturl:(NSString*)url getServiceDataCallBack:(void (^)(NSInteger status, NSObject *data))callBack
{
    
     [SVProgressHUD showWithStatus:@"loading" maskType:SVProgressHUDMaskTypeClear];
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    [manager POST:url parameters:param success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         [SVProgressHUD dismiss];
         NSLog(@"%@",responseObject);
         NSDictionary *response = (NSDictionary *)responseObject;
         int statusCode = [[[response valueForKey:@"result"]valueForKey:@"error"] intValue];
         statusCode=0;
         if (statusCode==0)
         {
             callBack (statusCode,response);
             
         }
         else
         {
             callBack (statusCode,[[response valueForKey:@"result"] st_stringForKey:@"message"]);
         }
         
     }
          failure:^(AFHTTPRequestOperation *operation, NSError *error) {
              [SVProgressHUD dismiss];
              NSString *strFailure = [NSString stringWithFormat:@"%@",[error localizedDescription]];
              
              if([strFailure isEqualToString:@"Could not connect to the server."])
              {
                 callBack(2, @"Could not connect to the server.");
              }
              else{
                 callBack(2, strFailure);
              }
          }];
    
}

-(void)getLoginServiceData:(NSMutableDictionary*)param posturl:(NSString*)url getServiceDataCallBack:(void (^)(NSInteger status, NSObject *data))callBack
{
    
    [SVProgressHUD showWithStatus:@"loading" maskType:SVProgressHUDMaskTypeClear];
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    [manager POST:url parameters:param success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         [SVProgressHUD dismiss];
         NSLog(@"%@",responseObject);
         NSDictionary *response = (NSDictionary *)responseObject;
         int statusCode = [[[response valueForKey:@"result"]valueForKey:@"error"] intValue];
         if (statusCode==0)
         {
             callBack (statusCode,response);
             
         }
         else
         {
             callBack (statusCode,[[response valueForKey:@"result"] st_stringForKey:@"message"]);
         }
         
     }
          failure:^(AFHTTPRequestOperation *operation, NSError *error) {
              [SVProgressHUD dismiss];
              NSString *strFailure = [NSString stringWithFormat:@"%@",[error localizedDescription]];
              
              if([strFailure isEqualToString:@"Could not connect to the server."])
              {
                  callBack(2, @"Could not connect to the server.");
              }
              else{
                  callBack(2, strFailure);
              }
          }];
    
}

-(void)getService:(NSMutableDictionary*)param posturl:(NSString*)url getServiceDataCallBack:(void (^)(NSInteger status, NSObject *data))callBack
{
    //[SVProgressHUD showWithStatus:@"loading" maskType:SVProgressHUDMaskTypeClear];
    
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    
    [manager POST:url parameters:param success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         NSLog(@"%@",responseObject);
         NSDictionary *response = (NSDictionary *)responseObject;
         int statusCode = [[[response valueForKey:@"result"]valueForKey:@"error"] intValue];
         statusCode=0;
         if (statusCode==0)
         {
             callBack (statusCode,response);
             
         }
         else
         {
             callBack (statusCode,[[response valueForKey:@"result"] st_stringForKey:@"message"]);
         }
         
     }
          failure:^(AFHTTPRequestOperation *operation, NSError *error) {
              NSString *strFailure = [NSString stringWithFormat:@"%@",[error localizedDescription]];
              
              if([strFailure isEqualToString:@"Could not connect to the server."])
              {
                  callBack(2, @"Could not connect to the server.");
              }
              else{
                  callBack(2, strFailure);
              }
          }];
    
}
-(void)getServiceData:(NSMutableDictionary*)param deleteurl:(NSString*)url getServiceDataCallBack:(void (^)(NSInteger status, NSObject *data))callBack {
    
    [SVProgressHUD showWithStatus:@"loading" maskType:SVProgressHUDMaskTypeClear];
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    
    [manager DELETE:url parameters:param success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         
         NSLog(@"%@",responseObject);
         [SVProgressHUD dismiss];
         NSDictionary *response = (NSDictionary *)responseObject;
         int statusCode = [[[response valueForKey:@"result"]valueForKey:@"error"] intValue];
         statusCode=0;
         if (statusCode==0)
         {
             callBack (statusCode,response);
             
         }
         else
         {
             callBack (statusCode,[[response valueForKey:@"result"] st_stringForKey:@"message"]);
         }
         
     }
            failure:^(AFHTTPRequestOperation* operation, NSError* error) {
                [SVProgressHUD dismiss];
                NSString *strFailure = [error localizedDescription];
                
                if([strFailure isEqualToString:@"Could not connect to the server."])
                {
                    callBack(2, @"Could not connect to the server.");
                }
                else{
                    callBack(2, strFailure);
                }
            }];
    
}
//-(void)getDemoList:(void(^)(NSInteger status,NSObject *date)) handler
//{
//    
//}

-(void)getConnectionListData:(NSMutableDictionary*)param geturl:(NSString*)url getServiceDataCallBack:(void (^)(NSInteger status, NSObject *data))callBack {
    
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    
    [manager GET:url parameters:param success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         NSLog(@"%@",responseObject);
         [SVProgressHUD dismiss];
         NSDictionary *response = (NSDictionary *)responseObject;
         int statusCode = [[[response valueForKey:@"result"]valueForKey:@"error"] intValue];
         statusCode=0;
         if (statusCode==0)
         {
             
             callBack (statusCode,[response valueForKey:@"result"]);
             
         }
         else
         {
             
             callBack (statusCode,[[response valueForKey:@"result"] st_stringForKey:@"message"]);
             
         }
         
     }
         failure:^(AFHTTPRequestOperation *operation, NSError *error) {
             [SVProgressHUD dismiss];
             NSString *strFailure = [error localizedDescription];
             
             if([strFailure isEqualToString:@"Could not connect to the server."])
             {
                 
                 callBack(2, @"Could not connect to the server.");
                 
             }
             else{
                 
                 callBack(2, strFailure);
             }
         }];
    
}

-(void)getRequestCountServiceData:(NSMutableDictionary*)param geturl:(NSString*)url getServiceDataCallBack:(void (^)(NSInteger status, NSObject *data))callBack {
    
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    
    [manager GET:url parameters:param success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         NSLog(@"%@",responseObject);
         NSDictionary *response = (NSDictionary *)responseObject;
         int statusCode = [[[response valueForKey:@"result"]valueForKey:@"error"] intValue];
         statusCode=0;
         if (statusCode==0)
         {
             
             callBack (statusCode,[response valueForKey:@"result"]);
             
         }
         else
         {
             
             callBack (statusCode,[[response valueForKey:@"result"] st_stringForKey:@"message"]);
             
         }
         
     }
         failure:^(AFHTTPRequestOperation* operation, NSError *error) {
             NSString *strFailure = [error localizedDescription];
             
             if([strFailure isEqualToString:@"Could not connect to the server."])
             {
                 
                 callBack(2, @"Could not connect to the server.");
                 
             }
             else{
                 
                 callBack(2, strFailure);
             }
         }];
    
}
@end
