//
//AppController

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface AppController : NSObject{
   
}

+(AppController *)sharedappController;

@property (assign) NSInteger categoryTag;
@property (strong ,nonatomic) NSString *strDeliveryLoc;
@property (strong ,nonatomic) NSMutableArray *arrAupairList;
@property (strong ,nonatomic) NSMutableArray *arrBookingList;
@property (strong ,nonatomic) NSMutableDictionary *dictPaymentDetails;
@property (strong ,nonatomic) NSArray *arrProvinceList;
@property (strong ,nonatomic) NSMutableArray *arrProvinceIdList;

@property (strong, nonatomic) NSString *strDeliveryAddress;



@property (strong, nonatomic) NSString *strCardHolderName;
@property (strong, nonatomic) NSString *strCardNumber;
@property (strong, nonatomic) NSString *strCardExpiryDate;
@property (strong, nonatomic) NSString *strCardCVV;
@property (strong, nonatomic) NSString *strCardType;
@property (strong, nonatomic) NSString *strCardNickname;



@property (strong, nonatomic) NSString *strCustomerId;
@property (strong, nonatomic) NSString *strDateOfDelivery;
@property (strong, nonatomic) NSString *strPaymentMode;


@property (strong, nonatomic) NSString *strCatID;



@property (strong,nonatomic) NSString *strSignInSectionName;
@property (nonatomic,strong) NSDictionary *dictCustomerDetails;

@property (strong, nonatomic) NSMutableArray *arrCartCheckoutItems;
@property (assign, nonatomic) CGFloat TotalCartPrice;
@property (assign, nonatomic) CGFloat totalVAT;
@property (assign, nonatomic) CGFloat totalTips;
@property (assign, nonatomic) CGFloat deliveryFees;
@property (assign, nonatomic) CGFloat serviceFees;

@property (nonatomic, strong) NSDateFormatter *formatDateSendServer;
@property (nonatomic, strong) NSDateFormatter *formatDateGetServer;
@property (nonatomic, strong) NSDateFormatter *formatTimeSet;
@property (nonatomic, strong) NSDateFormatter *addMeetupShowDate;
@property (nonatomic, strong) NSDateFormatter *formatDateForCheckAvailability;
@property (nonatomic, strong) NSDateFormatter *formatDayForCheckAvailability;

@property (nonatomic, strong) NSMutableDictionary *dictBookAnAupair;

@property (strong, nonatomic) NSMutableArray *arrSelectedIndexesForMeetup;


@property (assign) BOOL isPincodeDone;
-(void)customViewShowAlert:(NSString *)msg;
-(void)showAlert : (NSString*)strMsg viewController : (UIViewController*)viewController;
-(void)showAlerttoNavigateSignin : (NSString*)strMsg viewController : (UIViewController*)viewController;
-(NSInteger)getStoreId;
-(NSString*)getCutomerId;


-(void)addToCartserviceWithItewmDeatils:(NSDictionary *)dictItemDeatils andQuantityInfo:(NSDictionary *)dictQuantityDeatils inViewcontroller:(UIViewController *)controller;

-(BOOL)isSignIn;
@end
