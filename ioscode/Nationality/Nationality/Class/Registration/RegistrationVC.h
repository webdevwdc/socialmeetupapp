//
//  RegistrationVC.h
//  Nationality
//
//  Created by webskitters on 04/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import <CoreLocation/CoreLocation.h>
#import <GoogleMaps/GoogleMaps.h>
@import GooglePlaces;
@import GooglePlacePicker;
@interface RegistrationVC : UIViewController<UITabBarControllerDelegate,GMSAutocompleteViewControllerDelegate>{
    NSMutableArray *arr_bottom;
    GMSPlacesClient *_placesClient;
    CGRect kb;
    CGRect af;
    CGSize svos;
    CGPoint point;
     GMSPlacePicker *placePicker;
    NSString *nationalityId;
    NSString *cityId;
    NSString * registration;
    BOOL registration_done;
    }
@property (retain, nonatomic) FBSDKLoginButton *loginfb;
@property(nonatomic,weak)IBOutlet UITextField *txt_first_name;
@property(nonatomic,weak)IBOutlet UITextField *txt_last_name;
@property(nonatomic,weak)IBOutlet UITextField *txt_email;
@property(nonatomic,weak)IBOutlet UITextField *txt_password;
@property(nonatomic,weak)IBOutlet UITextField *txt_confirm_password;
@property(nonatomic,weak)IBOutlet UITextField *txt_homecity;
@property(nonatomic,weak)IBOutlet UITextField *txt_zipcode;
@property(nonatomic,weak)IBOutlet UITextField *txt_language;
@property(nonatomic,weak)IBOutlet UIButton *btnFinish;
@property(nonatomic,weak)IBOutlet UIButton *btnFacebook;
@property(nonatomic,weak)IBOutlet UILabel *lblOr;
@property (strong, nonatomic)NSString *firstName;
@property (strong, nonatomic)NSString *lastName;
@property (strong, nonatomic)NSString *email;
@property (strong, nonatomic)NSDictionary *fbresponse;

@property (strong, nonatomic) UIWindow *window;
@property(nonatomic,readonly) UITabBar *tabBar;
@property(nonatomic,strong) UITabBarController *tabBarController;
@property(nonatomic,weak)IBOutlet UIScrollView *scrollReg;
@property (strong, nonatomic) CLLocationManager *locationManager;
@property(nonatomic,strong)DropDownListView * Dropobj;
@property(nonatomic,assign)NSInteger index_tag;
- (IBAction)btnTermsOfUseAction:(id)sender;
- (IBAction)btnPrivacyPolicyAction:(id)sender;
@end
