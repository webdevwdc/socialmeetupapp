//
//  LoginVC.h
//  Nationality
//
//  Created by webskitters on 04/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <GoogleMaps/GoogleMaps.h>
#import <FBSDKLoginKit/FBSDKLoginKit.h>
@import GooglePlaces;
@import GooglePlacePicker;
@interface LoginVC : UIViewController<UITabBarControllerDelegate,GMSMapViewDelegate>
{
   
  BOOL registration_done;  
  GMSPlacePicker *placePicker;
  GMSPlacesClient *placesClient;
    
}
@property(nonatomic,weak)IBOutlet UITextField *txt_email;
@property(nonatomic,weak)IBOutlet UITextField *txt_password;
@property(nonatomic,weak)IBOutlet UIButton *btnGo;
@property(nonatomic,weak)IBOutlet UIView *VwTextField;
@property(nonatomic,weak)IBOutlet UIButton *btnRegister;
@property(nonatomic,weak)IBOutlet UIButton *btnForgot;
@property(nonatomic,strong) UITabBarController *tabBarController;
@property (weak, nonatomic) FBSDKLoginButton *loginButton;
@property(nonatomic,strong)NSDictionary *fbResponse;
@property(nonatomic,weak)IBOutlet UIScrollView *scrollReg;
@property (strong, nonatomic) CLLocationManager *locationManager;
@property (assign, nonatomic)  double latitude;
@property (assign, nonatomic) double longitude;
@property(nonatomic,strong)NSString * address;
@end
