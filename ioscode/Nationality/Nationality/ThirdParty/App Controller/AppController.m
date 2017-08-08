
#import "AppController.h"



@implementation AppController


static AppController *sharedappController = nil;

+(AppController *)sharedappController{
    @synchronized(self) {
        if (sharedappController == nil)
            sharedappController = [[self alloc] init];
    }
    return sharedappController;
}

- (id)init {
    if (self = [super init]) {
        
        self.strDeliveryLoc = @"";
        self.strCustomerId = @"";
        self.strDateOfDelivery = @"";
        self.arrAupairList = [[NSMutableArray alloc] init];
        self.dictPaymentDetails = [[NSMutableDictionary alloc] init];
        self.arrProvinceList=[[NSArray alloc]init];
        self.arrCartCheckoutItems = [[NSMutableArray alloc] init];
        self.arrProvinceIdList= [[NSMutableArray alloc] init];
        self.arrBookingList=[[NSMutableArray alloc]init];
        self.arrSelectedIndexesForMeetup = [[NSMutableArray alloc]init];
        
        self.strSignInSectionName = @"";
        
        self.formatDateSendServer = [[NSDateFormatter alloc] init];
        [self.formatDateSendServer setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
        
        self.formatDateGetServer = [[NSDateFormatter alloc] init];
        [self.formatDateGetServer setDateFormat:@"yyyy-MM-dd"];
        
        self.addMeetupShowDate = [[NSDateFormatter alloc] init];
        [self.addMeetupShowDate setDateFormat:@"MM/dd/yy HH:mm"];
        
        self.formatTimeSet = [[NSDateFormatter alloc] init];
        [self.formatTimeSet setDateFormat:@"HH:mm"];
        
        self.formatDateForCheckAvailability = [[NSDateFormatter alloc] init];
        [self.formatDateForCheckAvailability setDateFormat:@"MMMM dd,yyyy"];
        
        self.formatDayForCheckAvailability = [[NSDateFormatter alloc] init];
        [self.formatDayForCheckAvailability setDateFormat:@"eeee"];
        
        
        

       
    }
    return self;
}


#pragma -mark showAlert
-(void)showAlert : (NSString*)strMsg viewController : (UIViewController*)viewController
{
    UIAlertController * alert=   [UIAlertController
                                  alertControllerWithTitle:@""
                                  message:strMsg
                                  preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction
                         actionWithTitle:@"OK"
                         style:UIAlertActionStyleDefault
                         handler:^(UIAlertAction * action)
                         {
                             [alert dismissViewControllerAnimated:YES completion:nil];
                             
                         }];
    
    [alert addAction:ok];
    
    [viewController presentViewController:alert animated:YES completion:nil];
}

-(void)customViewShowAlert:(NSString *)msg{
    UIAlertController * alert=   [UIAlertController
                                  alertControllerWithTitle:@""
                                  message:msg
                                  preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction
                         actionWithTitle:@"OK"
                         style:UIAlertActionStyleDefault
                         handler:^(UIAlertAction * action)
                         {
                             
                             [alert dismissViewControllerAnimated:YES completion:nil];
                             
                         }];
    
    [alert addAction:ok];
    UIWindow *keyWindow = [[UIApplication sharedApplication]keyWindow];
    UIViewController *mainController = [keyWindow rootViewController];
    [mainController presentViewController:alert animated:YES completion:nil];
}

-(void)showAlerttoNavigateSignin : (NSString*)strMsg viewController : (UIViewController*)viewController
{
    UIAlertController * alert=   [UIAlertController
                                  alertControllerWithTitle:@"Au pair"
                                  message:strMsg
                                  preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction
                         actionWithTitle:@"OK"
                         style:UIAlertActionStyleDefault
                         handler:^(UIAlertAction * action)
                         {
                             [alert dismissViewControllerAnimated:YES completion:nil];
                             
                            
                             
                         }];
    
    [alert addAction:ok];
    
    [viewController presentViewController:alert animated:YES completion:nil];
}



-(NSString*)getCutomerId
{
    return self.strCustomerId;
}


-(BOOL)isSignIn
{
    if(self.strCustomerId.length > 0)
        return YES;
    else
        return NO;
}




#pragma mark - Common WEbservice Call




@end
