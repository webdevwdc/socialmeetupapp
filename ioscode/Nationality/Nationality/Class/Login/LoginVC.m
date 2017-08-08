//
//  LoginVC.m
//  Nationality
//
//  Created by webskitters on 04/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "LoginVC.h"
#import "RegistrationVC.h"
#import "DashboardVC.h"
#import "OtherUserProfileVC.h"
@interface LoginVC (){
    AppDelegate * appdel;
}

@end

@implementation LoginVC
@synthesize latitude,longitude;
- (void)viewDidLoad {
    [super viewDidLoad];
    //_txt_email.text=@"arpita.dutta@webskitters.com";
  //_txt_password.text=@"123456";
    appdel=(AppDelegate *)[[UIApplication sharedApplication] delegate];
    [Utility saveObjectInUserDefaults:@"login" forKey:@"Login"];
    [self currentLocation];
    [self loadVw];
      // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)loadVw{
    _VwTextField.layer.borderWidth=1.0;
    _VwTextField.layer.borderColor=[UIColor whiteColor].CGColor;
     [Utility changeUItextFieldColor:_txt_password];
     [Utility changeUItextFieldColor:_txt_email];
     [Utility addTextFieldPadding:_txt_password];
     [Utility addTextFieldPadding:_txt_email];
     appdel.navi = self.navigationController;
    _btnRegister.layer.borderWidth=1.0;
    _btnRegister.layer.borderColor=[UIColor whiteColor].CGColor;
    _btnForgot.layer.borderWidth=1.0;
    _btnForgot.layer.borderColor=[UIColor whiteColor].CGColor;
}
#pragma mark - Login
-(IBAction)go:(id)sender{
    if (![Utility istextEmpty:_txt_email withError:@" INVALID EMAIL"] && ![Utility istextEmpty:_txt_password withError:@" Please give your password"]) {
        if ([Utility isValidEmail:_txt_email.text]) {
            
            [self loginWebservice];
        }
        else{
            _txt_email.layer.borderWidth=1.0;
            _txt_email.layer.borderColor =[UIColor redColor].CGColor ;
            [_txt_email becomeFirstResponder];
            _txt_email.clipsToBounds=YES;
            _txt_email.text=@"";
            _txt_email.placeholder=@" INVALID EMAIL";
            [_txt_email setValue:[UIColor redColor]
                     forKeyPath:@"_placeholderLabel.textColor"];
            

        }
          
     }

}
//-(IBAction)go:(id)sender{
//   [self goToDashboard];
//}
-(void)loginWebservice{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:_txt_email.text forKey:EMAIL];
    [httpParams setValue:_txt_password.text forKey:PASSWORD];
    [httpParams setValue:[Utility getObjectForKey:DEVICE_TOKEN] forKey:DEVICE_TOKEN];
    
    if(_address)
    {
         [httpParams setValue:_address forKey:@"address"];
    }
    else
    {
        [httpParams setValue:@"" forKey:@"address"];
    }
    
    [httpParams setValue:[NSString stringWithFormat:@"%f",longitude] forKey:@"longitude"];
    [httpParams setValue:[NSString stringWithFormat:@"%f",latitude] forKey:@"latitude"];
    [httpParams setValue:@"ios" forKey:DEVICE_TYPE];
    
    [[ServiceRequestHandler sharedRequestHandler] getLoginServiceData:httpParams posturl:[NSString stringWithFormat:@"%@",HTTPS_USER_LOGIN] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         //NSDictionary *dict = (NSDictionary*)data;
         if(status==0)
         {
             NSDictionary *dict = [[(NSDictionary*)data st_dictionaryForKey:@"result"] st_dictionaryForKey:@"data"];            
             
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:USER_ID] forKey:USER_ID];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:NATIONALITY_ID] forKey:NATIONALITY_ID];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:LONGITUDE] forKey:LONGITUDE];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:HOME_CITY] forKey:HOME_CITY];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:ADDRESS] forKey:ADDRESS];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:LATITUDE] forKey:LATITUDE];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:TAG] forKey:TAG];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:SHORT_BIO] forKey:SHORT_BIO];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:LANGUAGE_ID] forKey:LANGUAGE_ID];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:INTEREST] forKey:INTEREST];
              [Utility saveObjectInUserDefaults:[dict st_stringForKey:FIRST_NAME] forKey:FIRST_NAME];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:LAST_NAME] forKey:LAST_NAME];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:FACEBOOKID] forKey:FACEBOOKID];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:REGISTRATION_TYPE] forKey:REGISTRATION_TYPE];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:@"profile_pic"] forKey:@"profile_pic"];
              [Utility saveObjectInUserDefaults:@"no" forKey:@"registration"];
             [self getFirebaseAuthentications];
             
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
             
         }
     }];
}
-(void)getFirebaseAuthentications{
    
    [SVProgressHUD showWithStatus:@"Loading...."];
    
    [[FIRAuth auth]signInWithEmail:_txt_email.text password:_txt_password.text completion:^(FIRUser * _Nullable user, NSError * _Nullable error) {
        [SVProgressHUD dismiss];
        [self goDashboard];
    }];
    
}

-(void)goDashboard{
   appdel.tabBarController = [self.storyboard instantiateViewControllerWithIdentifier:@"DashboardVC"];
    DashboardVC * Connection = [self.storyboard instantiateViewControllerWithIdentifier:@"DashVC"];
   /// [Utility saveObjectInUserDefaults:@"no" forKey:@"registration"];
    Connection = (DashboardVC *)[appdel.tabBarController.viewControllers objectAtIndex:4];
    appdel.tabBarController.selectedIndex=4;
//    [[[[[self tabBarController] tabBar] items] objectAtIndex:4] setBadgeValue:@"10"];
    [self tabBarDesign];
    [appdel.navi pushViewController:appdel.tabBarController animated:YES];
}
- (BOOL)textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    return YES;
}
#pragma mark - UITabBar

-(void)tabBarDesign{
    //self.tabBarController.tabBar.barTintColor=[Utility getColorFromHexString:@"#00A0B6"];
    appdel.tabBarController.tabBar.barTintColor=[Utility getColorFromHexString:@"#00A0B6"];
    [[UITabBarItem appearance] setTitleTextAttributes:@{NSFontAttributeName : [UIFont fontWithName:@"OPENSANS-BOLD" size:10.0f],
                                                        NSForegroundColorAttributeName : [UIColor whiteColor]
                                                        }
                                             forState:UIControlStateNormal];
    
    
    UITabBar *tabBar = appdel.tabBarController.tabBar;
    
    UITabBarItem *item0 = [tabBar.items objectAtIndex:0];
    UITabBarItem *item1 = [tabBar.items objectAtIndex:1];
    UITabBarItem *item2 = [tabBar.items objectAtIndex:2];
    UITabBarItem *item3 = [tabBar.items objectAtIndex:3];
    UITabBarItem *item4 = [tabBar.items objectAtIndex:4];
    
    if (IS_IPHONE_5) {
        item0.selectedImage = [[UIImage imageNamed:@"connection-select"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
        item0.image = [[UIImage imageNamed:@"connection"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
        item1.selectedImage = [[UIImage imageNamed:@"BTbar-ico2act@2x.png"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
        item1.image = [[UIImage imageNamed:@"BTbar-ico2dis@2x.png"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
        item4.selectedImage = [[UIImage imageNamed:@"Me@2x.png"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
        item4.image = [[UIImage imageNamed:@"Me-select@2x.png"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
        item3.selectedImage = [[UIImage imageNamed:@"Bulletin-select@2x.png"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
        item3.image = [[UIImage imageNamed:@"Bulletin@2x.png"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
        item2.selectedImage = [[UIImage imageNamed:@"Message-select@2x.png"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
        item2.image = [[UIImage imageNamed:@"Message@2x.png"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    }
    else{
    item0.selectedImage = [[UIImage imageNamed:@"Connections-selected"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    item0.image = [[UIImage imageNamed:@"Connections"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
   
    
    item1.selectedImage = [[UIImage imageNamed:@"Meetups-selected"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    item1.image = [[UIImage imageNamed:@"Meetups"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
   
    item2.selectedImage = [[UIImage imageNamed:@"Messages-selected"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    item2.image = [[UIImage imageNamed:@"Messages"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    item3.selectedImage = [[UIImage imageNamed:@"Bulletins-selected"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    item3.image = [[UIImage imageNamed:@"Bulletins"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    
    item4.selectedImage = [[UIImage imageNamed:@"Me-selected"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    item4.image = [[UIImage imageNamed:@"Me"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    }

}
#pragma mark - Secure Password

-(IBAction)passwordSecure:(UIButton*)sender{
    
    if (sender.selected) {
        sender.selected=NO;
         [_txt_password setSecureTextEntry:YES];
    }
    else{
        sender.selected=YES;
        [_txt_password setSecureTextEntry:NO];
       
    }
   
}
#pragma mark - Go To Registration

-(IBAction)registration:(id)sender{
        RegistrationVC  *registervc = [self.storyboard instantiateViewControllerWithIdentifier:@"RegistrationVC"];
    [appdel.navi pushViewController:registervc animated:YES];
}
#pragma mark - Forgot Password

-(IBAction)forgotPassword:(id)sender{
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"" message:@"Enter your email address" preferredStyle:UIAlertControllerStyleAlert];
    [alert addTextFieldWithConfigurationHandler:nil];
    
    [alert addAction: [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
        UITextField *textField = alert.textFields[0];
        [self callForgotPasswordService:textField.text];
        
        NSLog(@"text was %@", textField.text);
    }]];
    
    [alert addAction:[UIAlertAction actionWithTitle:@"Cancel" style:UIAlertActionStyleCancel handler:^(UIAlertAction *action) {
        NSLog(@"Cancel pressed");
    }]];
    
    [self presentViewController:alert animated:YES completion:nil];
}
-(void)callForgotPasswordService : (NSString*)str
{
    NSMutableDictionary *httpParams = [NSMutableDictionary dictionary];
    
    [httpParams setValue:str forKey:@"email"];
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:HTTPS_FORGOT_PASSWORD getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             [[AppController sharedappController] showAlert:[[dict st_dictionaryForKey:@"result"] st_stringForKey:@"message"] viewController:self];
             
         }
         
         else
         {
             NSString* str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
}
#pragma mark - Facebook Integration

-(IBAction)facebookLogin:(id)sender{
   [self facebookIntegration];
}


-(void)facebookIntegration{
 
    _loginButton.readPermissions =
    @[@"public_profile", @"email", @"user_friends"];
    FBSDKLoginManager *login = [[FBSDKLoginManager alloc] init];
    [login logOut];
    [SVProgressHUD dismiss];
    [login logInWithReadPermissions:@[@"public_profile", @"email", @"user_friends"] fromViewController:nil handler:^(FBSDKLoginManagerLoginResult *result, NSError *error) {
        
        if (error) {
            
            NSLog(@"Process error%@",error.description);
            
            
        } else if (result.isCancelled) {
            
            NSLog(@"Cancelled");
        }
        else {
            NSLog(@"Logged in");
            [self session];
        }
        
        
    }];
}

-(void)session{
    NSMutableDictionary* parameters = [NSMutableDictionary dictionary];
    [parameters setValue:@"id,first_name,last_name,email,name" forKey:@"fields"];
    [[[FBSDKGraphRequest alloc] initWithGraphPath:@"me" parameters:parameters]
     startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection,
                                  id result, NSError *error) {
         NSLog(@"%@",result);
         [SVProgressHUD dismiss];
         _fbResponse=[NSDictionary new];
         _fbResponse =(NSDictionary *) result;
         //[self facebookProfileImage];
         [self facebookRegistration:_fbResponse];
         
     }];
}

-(void)facebookRegistration:(NSDictionary*)response{
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:[response st_stringForKey:@"first_name"] forKey:FIRST_NAME];
    [httpParams setValue:[response st_stringForKey:@"last_name"] forKey:LAST_NAME];
    
    [httpParams setValue:[response st_stringForKey:@"email"]?[response st_stringForKey:@"email"]:@"" forKey:EMAIL];
    [httpParams setValue:[response st_stringForKey:@"id"] forKey:@"facebookId"];
    [httpParams setValue:@"" forKey:HOME_CITY];
    //[httpParams setValue:_address forKey:ADDRESS];
    [httpParams setValue:@"" forKey:PASSWORD];
    [httpParams setValue:[Utility getObjectForKey:DEVICE_TOKEN] forKey:DEVICE_TOKEN];
    [httpParams setValue:@"Fb" forKey:REGISTRATION_TYPE];
    [httpParams setValue:@"ios" forKey:DEVICE_TYPE];
    [httpParams setValue:@"" forKey:NATIONALITY_ID];
   // [httpParams setValue:[NSString stringWithFormat:@"%f",longitude] forKey:LONGITUDE];
  //  [httpParams setValue:[NSString stringWithFormat:@"%f",latitude] forKey:LATITUDE];
    [httpParams setValue:@"0" forKey:@"register_done"];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@",HTTPS_USER_SIGNUP] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         NSDictionary *dict = (NSDictionary*)data;
         if(status==0)
         {
             
             NSDictionary *dict = [[(NSDictionary*)data st_dictionaryForKey:@"result"] st_dictionaryForKey:@"data"];
             if ([dict count ]>0) {
                 
                 
                 [Utility saveObjectInUserDefaults:[dict st_stringForKey:USER_ID] forKey:USER_ID];
                 [Utility saveObjectInUserDefaults:[dict st_stringForKey:NATIONALITY_ID] forKey:NATIONALITY_ID];
                 [Utility saveObjectInUserDefaults:[dict st_stringForKey:LONGITUDE] forKey:LONGITUDE];
                 [Utility saveObjectInUserDefaults:[dict st_stringForKey:HOME_CITY] forKey:HOME_CITY];
                 [Utility saveObjectInUserDefaults:[dict st_stringForKey:ADDRESS] forKey:ADDRESS];
                 [Utility saveObjectInUserDefaults:[dict st_stringForKey:LATITUDE] forKey:LATITUDE];
                 [Utility saveObjectInUserDefaults:[dict st_stringForKey:TAG] forKey:TAG];
                 [Utility saveObjectInUserDefaults:[dict st_stringForKey:SHORT_BIO] forKey:SHORT_BIO];
                 [Utility saveObjectInUserDefaults:[dict st_stringForKey:LANGUAGE_ID] forKey:LANGUAGE_ID];
                 [Utility saveObjectInUserDefaults:[dict st_stringForKey:INTEREST] forKey:INTEREST];
                 [Utility saveObjectInUserDefaults:[dict st_stringForKey:FIRST_NAME] forKey:FIRST_NAME];
                 [Utility saveObjectInUserDefaults:[dict st_stringForKey:LAST_NAME] forKey:LAST_NAME];
                 [Utility saveObjectInUserDefaults:[dict st_stringForKey:FACEBOOKID] forKey:FACEBOOKID];
                 [Utility saveObjectInUserDefaults:[dict st_stringForKey:REGISTRATION_TYPE] forKey:REGISTRATION_TYPE];
                  [Utility saveObjectInUserDefaults:@"no" forKey:@"registration"];
                 [self goDashboard];
             }
             else{
                 RegistrationVC  *registervc = [self.storyboard instantiateViewControllerWithIdentifier:@"RegistrationVC"];
                 registervc.firstName=[_fbResponse st_stringForKey:@"first_name"];
                 registervc.lastName=[_fbResponse st_stringForKey:@"last_name"];
                 registervc.email=[_fbResponse st_stringForKey:@"email"];
                 registervc.fbresponse=_fbResponse;
                 [appdel.navi pushViewController:registervc animated:YES];

//                 registration_done=YES;
//                 [self callRegistration];
             }
             
             
             
         }
         else
         {
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
    
}
//

-(void)callRegistration{
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:[_fbResponse st_stringForKey:@"first_name"] forKey:FIRST_NAME];
    [httpParams setValue:[_fbResponse st_stringForKey:@"last_name"] forKey:LAST_NAME];
    [httpParams setValue:_txt_email.text forKey:EMAIL];
    [httpParams setValue:@"" forKey:HOME_CITY];
    //[httpParams setValue:_address forKey:ADDRESS];
    [httpParams setValue:_txt_password.text forKey:PASSWORD];
    [httpParams setValue:[Utility getObjectForKey:DEVICE_TOKEN] forKey:DEVICE_TOKEN];
    [httpParams setValue:@"Normal" forKey:REGISTRATION_TYPE];
    [httpParams setValue:@"ios" forKey:DEVICE_TYPE];
    [httpParams setValue:@"" forKey:NATIONALITY_ID];
    //[httpParams setValue:[NSString stringWithFormat:@"%f",longitude] forKey:LONGITUDE];
   // [httpParams setValue:[NSString stringWithFormat:@"%f",latitude] forKey:LATITUDE];
    if (registration_done) {
        [httpParams setValue:@"1" forKey:@"register_done"];
        [httpParams setValue:[_fbResponse st_stringForKey:@"id"] forKey:@"facebookId"];
        [httpParams setValue:@"Fb" forKey:REGISTRATION_TYPE];
    }
    //    else{
    //
    //    }
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@",HTTPS_USER_SIGNUP] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         NSDictionary *dict = (NSDictionary*)data;
         if(status==0)
         {
             
             
             NSDictionary *dict = [(NSDictionary*)data st_dictionaryForKey:@"result"];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:@"id"] forKey:USER_ID];
              [Utility saveObjectInUserDefaults:@"yes" forKey:@"registration"];
             [self getFirebaseAuthentications];
             
             
         }
         else
         {
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
}

#pragma mark - Current Location
-(void)currentLocation{
    
    
    self.locationManager = [[CLLocationManager alloc] init];
    [self.locationManager requestWhenInUseAuthorization];
    
    
    self.locationManager.delegate = self;
    // self.locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    
    [self.locationManager startUpdatingLocation];
}
- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error
{
    NSLog(@"didFailWithError: %@", error);
    UIAlertView *errorAlert = [[UIAlertView alloc]
                               initWithTitle:@"Error" message:@"Failed to Get Your Location" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
    [errorAlert show];
}

- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation
{
    NSLog(@"didUpdateToLocation: %@", newLocation);
    CLLocation *currentLocation = newLocation;
    
    if (currentLocation != nil) {
        latitude =  currentLocation.coordinate.latitude;
        longitude = currentLocation.coordinate.longitude;
       // [self fetchAddress:latitude :longitude];
        
    }
    CLGeocoder *geocoder = [[CLGeocoder alloc] init];
    [geocoder reverseGeocodeLocation:self.locationManager.location
                   completionHandler:^(NSArray *placemarks, NSError *error) {
                       NSLog(@"reverseGeocodeLocation:completionHandler: Completion Handler called!");
                       
                       if (error){
                           NSLog(@"Geocode failed with error: %@", error);
                           return;
                           
                       }
                       
                       NSLog(@"placemarks=%@",[placemarks objectAtIndex:0]);
                       CLPlacemark *placemark = [placemarks objectAtIndex:0];
                       
                       NSLog(@"placemark.ISOcountryCode =%@",placemark.ISOcountryCode);
                       NSLog(@"placemark.country =%@",placemark.country);
                       NSLog(@"placemark.postalCode =%@",placemark.postalCode);
                       NSLog(@"placemark.administrativeArea =%@",placemark.administrativeArea);
                       NSLog(@"placemark.locality =%@",placemark.locality);
                       NSLog(@"placemark.subLocality =%@",placemark.subLocality);
                       NSLog(@"placemark.subThoroughfare =%@",placemark.subThoroughfare);
                       
                       NSString *strAddr= @"";
                      
                       if(placemark.subLocality) {
                           
                           if ([strAddr isEqualToString:@""]) {
                               strAddr = [NSString stringWithFormat:@"%@%@",strAddr,placemark.subLocality];
                           }
                           else {
                               strAddr = [NSString stringWithFormat:@"%@, %@",strAddr,placemark.subLocality];
                           }
                           
                       }
                       
                       
                       if(placemark.locality) {
                           
                           if ([strAddr isEqualToString:@""]) {
                               strAddr = [NSString stringWithFormat:@"%@%@",strAddr,placemark.locality];
                           }
                           else {
                               strAddr = [NSString stringWithFormat:@"%@, %@",strAddr,placemark.locality];
                           }
                       }
                       
                       
                       
                       
                       if(placemark.administrativeArea) {
                           
                           if ([strAddr isEqualToString:@""]) {
                               strAddr = [NSString stringWithFormat:@"%@%@",strAddr,placemark.administrativeArea];
                           }
                           else {
                               strAddr = [NSString stringWithFormat:@"%@, %@",strAddr,placemark.administrativeArea];
                           }
                           
                           
                       }
                       
                       
                       if(placemark.postalCode) {
                           
                           if ([strAddr isEqualToString:@""]) {
                               strAddr = [NSString stringWithFormat:@"%@%@",strAddr,placemark.postalCode];
                           }
                           else {
                               strAddr = [NSString stringWithFormat:@"%@, %@",strAddr,placemark.postalCode];
                           }
                           
                       }
                       if(placemark.country) {
                           
                           if ([strAddr isEqualToString:@""]) {
                               strAddr = [NSString stringWithFormat:@"%@%@",strAddr,placemark.country];
                           }
                           else {
                               strAddr = [NSString stringWithFormat:@"%@, %@",strAddr,placemark.country];
                           }
                           
                       }
                       
                       
                       _address = [NSString stringWithFormat:@"%@",strAddr];
                       if ([_address length]>2) {
                           [self.locationManager stopUpdatingLocation];
                           self.locationManager = nil;
                       }
                       
                   }];
    
}

@end
