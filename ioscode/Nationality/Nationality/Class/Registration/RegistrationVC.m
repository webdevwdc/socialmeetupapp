//
//  RegistrationVC.m
//  Nationality
//
//  Created by webskitters on 04/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "RegistrationVC.h"
#import "MeetupsVC.h"
#import "DashboardVC.h"
#import "DashboardVC.h"
#import "LocationVC.h"
#import "CMSVC.h"
@interface RegistrationVC ()<GMSAutocompleteViewControllerDelegate,GMSMapViewDelegate>{
   
    CGFloat lati;
    CGFloat longi;
    NSArray *arr_nationality;
     NSArray *arr_cities;
    AppDelegate *appdel;
}

@end

@implementation RegistrationVC

- (void)viewDidLoad {
    [super viewDidLoad];
    nationalityId=@"";
    registration_done=NO;
      appdel.navi = self.navigationController;
    arr_cities=[NSArray new];
        appdel=(AppDelegate *)[[UIApplication sharedApplication] delegate];
    _txt_email.text=_email;
    _txt_first_name.text=_firstName;
    _txt_last_name.text=_lastName;
    if ([_firstName length]>0) {
        registration_done=YES;
        _btnFacebook.hidden=YES;
        _lblOr.hidden=YES;
    }
    else{
        _btnFacebook.hidden=NO;
        _lblOr.hidden=NO;
    }
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:YES];
    [self loadLocation];
    [self loadRegVW];
    [self currentLocation];
    
}
-(void)loadLocation{
    _placesClient = [GMSPlacesClient sharedClient];
     [self.locationManager requestAlwaysAuthorization];
    
}
-(void)loadRegVW{
    arr_nationality=[NSArray new];
}
#pragma mark - Submit Regstration
-(IBAction)submit:(id)sender{
    
     if (![Utility istextEmpty:_txt_first_name withError:@" ENTER FIRST NAME"] && ![Utility istextEmpty:_txt_last_name withError:@" ENTER LAST NAME"] && ![Utility istextEmpty:_txt_email withError:@" ENTER EMAIL"]&& ![Utility istextEmpty:_txt_password withError:@" ENTER PASSWORD"]&& ![Utility istextEmpty:_txt_homecity withError:@" SELECT CURRENT CITY"] && ![Utility istextEmpty:_txt_language withError:@" SELECT NATIONALITY"]) {
         if(_txt_password.text.length < 5)
         {
             [[AppController sharedappController] showAlert:@"Please provide atleast five characters password." viewController:self];
             return;
         }
         if ([Utility isValidEmail:_txt_email.text]) {
             [_txt_first_name resignFirstResponder];
              [_txt_last_name resignFirstResponder];
              [_txt_password resignFirstResponder];
              [_txt_email resignFirstResponder];
              [_txt_zipcode resignFirstResponder];
              [_txt_homecity resignFirstResponder];
             [_txt_language resignFirstResponder];
             [self callRegistration];
             
        }
         else{
             
              [self textField:_txt_email withError:@" INVALID EMAIL"];
            }
     }
}
#pragma mark - Web Service
-(void)callRegistration{
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:_txt_first_name.text forKey:FIRST_NAME];
    [httpParams setValue:_txt_last_name.text forKey:LAST_NAME];
    [httpParams setValue:_txt_email.text forKey:EMAIL];
    [httpParams setValue:_txt_homecity.text forKey:HOME_CITY];
    //[httpParams setValue:_txt_zipcode.text forKey:ADDRESS];
    [httpParams setValue:_txt_password.text forKey:PASSWORD];
    [httpParams setValue:[Utility getObjectForKey:DEVICE_TOKEN] forKey:DEVICE_TOKEN];
    [httpParams setValue:@"Normal" forKey:REGISTRATION_TYPE];
    [httpParams setValue:@"ios" forKey:DEVICE_TYPE];
    [httpParams setValue:nationalityId forKey:NATIONALITY_ID];
    //[httpParams setValue:[NSString stringWithFormat:@"%f", longi] forKey:LONGITUDE];
   // [httpParams setValue:[NSString stringWithFormat:@"%f", lati] forKey:LATITUDE];
    if (registration_done) {
        [httpParams setValue:@"1" forKey:@"register_done"];
        [httpParams setValue:[_fbresponse st_stringForKey:@"id"] forKey:@"facebookId"];
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
             
             
             NSDictionary *dict = [[(NSDictionary*)data st_dictionaryForKey:@"result"] st_dictionaryForKey:@"data"];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:@"id"] forKey:USER_ID];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:FACEBOOKID] forKey:FACEBOOKID];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:REGISTRATION_TYPE] forKey:REGISTRATION_TYPE];
             [self getFirBaseAuthentication];
             
             
         }
         else
         {
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
}
-(void)getNationality:(NSString *)keyword{
    NSMutableDictionary *httpParams=[NSMutableDictionary new];

    [httpParams setValue:keyword forKey:@"keyword"];
  
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@",HTTPS_NATIONALITY] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         NSDictionary *dict = (NSDictionary*)data;
         if(status==0)
         {
             
             NSDictionary *dict = (NSDictionary*)data;
             arr_nationality=[[dict st_dictionaryForKey:@"result"] st_arrayForKey:@"data"];
             [self showDropDown];
             //[Utility saveObjectInUserDefaults:[dict st_stringForKey:@"id"] forKey:USER_ID];
             
         }
         
         else
         {
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];

}
-(void)getHomecity:(NSString *)keyword{
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    
    [httpParams setValue:keyword forKey:@"keyword"];
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@",HTTPS_HOMECITIES] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         NSDictionary *dict = (NSDictionary*)data;
         if(status==0)
         {
             
             NSDictionary *dict = (NSDictionary*)data;
             arr_cities=[[dict st_dictionaryForKey:@"result"] st_arrayForKey:@"data"];
             [self showDropDowncity];
             //[Utility saveObjectInUserDefaults:[dict st_stringForKey:@"id"] forKey:USER_ID];
             
         }
         
         else
         {
             NSString *str = (NSString*)data;
            // [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
    
}

#pragma mark - LOCATION TRACE
- (IBAction)getCurrentPlace:(id)sender{
    CLLocationCoordinate2D center = CLLocationCoordinate2DMake(lati, longi);
    CLLocationCoordinate2D northEast = CLLocationCoordinate2DMake(center.latitude + 0.001,
                                                                  center.longitude + 0.001);
    CLLocationCoordinate2D southWest = CLLocationCoordinate2DMake(center.latitude - 0.001,
                                                                  center.longitude - 0.001);
    GMSCoordinateBounds *viewport = [[GMSCoordinateBounds alloc] initWithCoordinate:northEast
                                                                         coordinate:southWest];
    GMSPlacePickerConfig *config = [[GMSPlacePickerConfig alloc] initWithViewport:viewport];
    placePicker = [[GMSPlacePicker alloc] initWithConfig:config];
    
    [placePicker pickPlaceWithCallback:^(GMSPlace *place, NSError *error) {
        if (error != nil) {
            NSLog(@"Pick Place error %@", [error localizedDescription]);
            return;
        }
        
        if (place != nil) {
//            NSLog(@"current...........lat%f",place.coordinate.latitude);
            lati=place.coordinate.latitude;
            longi=place.coordinate.longitude;
            self.txt_zipcode.text = [[place.formattedAddress componentsSeparatedByString:@", "]
                                     componentsJoinedByString:@"\n"];
            NSArray*arr=place.addressComponents;
            // GMSAddressComponent *gsm=[GMSAddressComponent new];
            NSDictionary *dic=[NSDictionary new];
            
            for (int i=0;i<arr.count;i++) {
                GMSAddressComponent* myObject = (GMSAddressComponent*)arr[i];
                if ([myObject.type isEqualToString:@"locality"]) {
                    self.txt_homecity.text =myObject.name;
                    break;
                    
                }
                
            }
            
        }
        else {
            self.txt_homecity.text = @"";
            self.txt_zipcode.text = @"";
        }
    }];
    

   
    
}
-(void)currentLocation{
    [_placesClient currentPlaceWithCallback:^(GMSPlaceLikelihoodList *likelihoodList, NSError *error) {
        if (error != nil) {
            NSLog(@"Current Place error %@", [error localizedDescription]);
            return;
        }
        
        for (GMSPlaceLikelihood *likelihood in likelihoodList.likelihoods) {
            GMSPlace* place = likelihood.place;
            //[self showLocation:place.coordinate.latitude withLongitude:place.coordinate.longitude];
            lati=place.coordinate.latitude;
            longi=place.coordinate.longitude;
        }
        
    }];

   }
#pragma mark - FBIntegration
-(void)facebookLogin{
    //FBSDKLoginManager *login = [[FBSDKLoginManager alloc] init];
    _loginfb.readPermissions =
    @[@"public_profile", @"email", @"user_friends"];
    
    FBSDKLoginManager *login = [[FBSDKLoginManager alloc] init];
    
    [login logOut];
    [SVProgressHUD dismiss];
    [login logInWithReadPermissions:@[@"public_profile", @"email", @"user_friends"] fromViewController:nil handler:^(FBSDKLoginManagerLoginResult *result, NSError *error) {
        
        if (error) {
            NSLog(@"Process error%@",error.description);
            ;
        } else if (result.isCancelled) {
            
            NSLog(@"Cancelled");
        }
        else {
            NSLog(@"Logged in");
            // fbAccessToken = [FBSDKAccessToken currentAccessToken].tokenString;
            
            
            // [self facebookProfileImage];
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
        _fbresponse=[NSDictionary new];
         _fbresponse =(NSDictionary *) result;
         //[self facebookProfileImage];
         [self facebookRegistration:_fbresponse];
         
     }];
}
-(void)facebookRegistration:(NSDictionary*)response{
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:[response st_stringForKey:@"first_name"] forKey:FIRST_NAME];
    [httpParams setValue:[response st_stringForKey:@"last_name"] forKey:LAST_NAME];
    [httpParams setValue:[response st_stringForKey:@"email"]?[response st_stringForKey:@"email"]:@"" forKey:EMAIL];
 [httpParams setValue:[response st_stringForKey:@"id"] forKey:@"facebookId"];
    [httpParams setValue:_txt_homecity.text forKey:HOME_CITY];
    //[httpParams setValue:_txt_zipcode.text forKey:ADDRESS];
    [httpParams setValue:_txt_password.text forKey:PASSWORD];
    [httpParams setValue:[Utility getObjectForKey:DEVICE_TOKEN] forKey:DEVICE_TOKEN];
    [httpParams setValue:@"Fb" forKey:REGISTRATION_TYPE];
    [httpParams setValue:@"ios" forKey:DEVICE_TYPE];
    [httpParams setValue:@"" forKey:NATIONALITY_ID];
    //[httpParams setValue:[NSString stringWithFormat:@"%f", longi] forKey:LONGITUDE];
   // [httpParams setValue:[NSString stringWithFormat:@"%f", lati] forKey:LATITUDE];
    [httpParams setValue:@"0" forKey:@"register_done"];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@",HTTPS_USER_SIGNUP] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         NSDictionary *dict = (NSDictionary*)data;
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             if ([[[dict valueForKey:@"result"] valueForKey:@"data"] count ]>0) {
             [Utility saveObjectInUserDefaults:[[[dict valueForKey:@"result"] valueForKey:@"data"]st_stringForKey:@"id"] forKey:USER_ID];
             [Utility saveObjectInUserDefaults:[[[dict valueForKey:@"result"] valueForKey:@"data"] st_stringForKey:FACEBOOKID] forKey:FACEBOOKID];
             [self goToDashboard];
                 
             }
             else
             {
                 _btnFacebook.hidden=YES;
                 _lblOr.hidden=YES;
                  registration_done=YES;
                 _txt_first_name.text=[response st_stringForKey:@"first_name"];
                 _txt_last_name.text=[response st_stringForKey:@"last_name"];
                 _txt_email.text=[response st_stringForKey:@"email"];
                 
             }
         }
         else
         {
             
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
             
         }
     }];

}
#pragma mark - Go To Dashboard
-(void)getFirBaseAuthentication{
    [[FIRAuth auth]
     createUserWithEmail:_txt_email.text
     password:_txt_password.text
     completion:^(FIRUser *_Nullable user,
                  NSError *_Nullable error)
    {
         NSString *userid = user.uid;
         [self goToDashboard];
     }];
}

-(void)goToDashboard{
    self.tabBarController = [self.storyboard instantiateViewControllerWithIdentifier:@"DashboardVC"];
    DashboardVC * Connection = [self.storyboard instantiateViewControllerWithIdentifier:@"DashVC"];
    //Connection.isRegistration=YES;
    [Utility saveObjectInUserDefaults:@"yes" forKey:@"registration"];
    Connection = (DashboardVC *)[self.tabBarController.viewControllers objectAtIndex:4];
    self.tabBarController.selectedIndex=4;   
    [self tabBarDesign];
    [appdel.navi pushViewController:self.tabBarController animated:YES];
    
}
#pragma mark - UITextField Alert

-(void)textField:(UITextField*)textField withError:(NSString *)msg{
    textField.layer.borderWidth=1.0;
    textField.layer.borderColor = [UIColor redColor].CGColor;
    
    
    textField.placeholder=msg;
    [textField setValue:[UIColor redColor]
              forKeyPath:@"_placeholderLabel.textColor"];
    [textField becomeFirstResponder];

}
#pragma mark - UITextField Delegate
- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField{
    point=CGPointMake(0,0);
    af=[[UIScreen mainScreen] bounds] ;
    [self moveScrollView:textField];
    return YES;
}
-(void)moveScrollView:(UIView *)theView
{
    CGFloat vcy=theView.center.y;
    CGFloat fsh=af.size.height;
    CGFloat sa=0.0;
    sa=vcy-fsh/4.0;
    if(sa<0)
        sa=0;
    self.scrollReg.contentSize=CGSizeMake(af.size.width,af.size.height+sa);
    NSLog(@"%f-%f-%f,%f",self.scrollReg.contentSize.height,af.size.height,kb.size.height,sa);
    [ self.scrollReg setContentOffset:CGPointMake(0,sa) animated:YES];
}
- (BOOL)textFieldShouldReturn:(UITextField *)textField{
   self.scrollReg.contentSize = CGSizeMake(0,self.view.frame.size.height);
    [textField resignFirstResponder];
    return YES;
}
- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string{
    NSString *str=@"";
    if (textField==_txt_language) {
        if (range.location>=2) {
            str= [string length]>0?textField.text.length>0?[textField.text stringByAppendingString:string]:string:[textField.text substringToIndex:[textField.text length]-1];
            [self getNationality:str];
        }
        else
        {
            [_Dropobj removeFromSuperview];
        }
//        else if (range.location>=1) {
//            [self getHomecity:@""];
//        }
    }
   else if (textField==_txt_homecity) {
        if (range.location>=2) {
            str= [string length]>0?textField.text.length>0?[textField.text stringByAppendingString:string]:string:[textField.text substringToIndex:[textField.text length]-1];
            
            [self getHomecity:str];
        }
       
       else
       {
           [_Dropobj removeFromSuperview];
       }
//       else if (range.location>=1) {
//           [self getHomecity:@""];
//       }
    }
    return YES;
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
#pragma mark - UITabbar
-(void)tabBarDesign{
    self.tabBarController.tabBar.barTintColor=[Utility getColorFromHexString:@"#00A0B6"];
    [[UITabBarItem appearance] setTitleTextAttributes:@{NSFontAttributeName : [UIFont fontWithName:@"OPENSANS-BOLD" size:10.0f],
                                                        NSForegroundColorAttributeName : [UIColor whiteColor]
                                                        }
                                             forState:UIControlStateNormal];
    
    UITabBar *tabBar = self.tabBarController.tabBar;
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
        item2.image = [[UIImage imageNamed:@"Message@2x.png"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    }
    else{
    item0.selectedImage = [[UIImage imageNamed:@"Connections"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    item0.image = [[UIImage imageNamed:@"Connections"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    item1.selectedImage = [[UIImage imageNamed:@"Meetups"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    item1.image = [[UIImage imageNamed:@"Meetups"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    item2.selectedImage = [[UIImage imageNamed:@"Messages"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    item2.image = [[UIImage imageNamed:@"Messages"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    item3.selectedImage = [[UIImage imageNamed:@"Bulletins"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    item3.image = [[UIImage imageNamed:@"Bulletins"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    item4.selectedImage = [[UIImage imageNamed:@"Me"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    item4.image = [[UIImage imageNamed:@"Me"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    }
}
-(IBAction)facelogin:(id)sender{
    [self facebookLogin];
   
//    if (self.fbresponse) {
//        
//    }
    
}
#pragma mark-
#pragma mark-Drop Down

-(void)showPopUpWithTitle:(NSString*)popupTitle withOption:(NSArray*)arrOptions xy:(CGPoint)point size:(CGSize)size isMultiple:(BOOL)isMultiple{
    _Dropobj = [[DropDownListView alloc] initWithTitle:popupTitle options:arrOptions xy:point size:size isMultiple:isMultiple];
    _Dropobj.delegate = self;
    [_Dropobj showInView:self.scrollReg animated:YES];
    
    /*----------------Set DropDown backGroundColor-----------------*/
    [_Dropobj SetBackGroundDropDown_R:255.0 G:255.0 B:255.0 alpha:1.0];
    
}
- (void)DropDownListView:(DropDownListView *)dropdownListView didSelectedIndex:(NSInteger)anIndex{

    if(_index_tag==0)
    {
        _txt_language.text=[[arr_nationality objectAtIndex:anIndex] st_stringForKey:@"name"];
        nationalityId=[[arr_nationality objectAtIndex:anIndex] st_stringForKey:@"id"];
        //        _btn_ethnic.selected=NO;
    }
    if(_index_tag==1)
    {
        _txt_homecity.text=[[arr_cities objectAtIndex:anIndex] st_stringForKey:@"name"];
        cityId=[[arr_cities objectAtIndex:anIndex] st_stringForKey:@"id"];
        //        _btn_ethnic.selected=NO;
    }
    
}
-(void)showDropDowncity{
    [_Dropobj fadeOut];
    [self showPopUpWithTitle:@"" withOption:arr_cities xy:CGPointMake(CGRectGetMinX(self.txt_homecity.frame), CGRectGetMaxY(self.txt_homecity.frame)) size:CGSizeMake(self.txt_homecity.frame.size.width, 280) isMultiple:NO];
    _index_tag=1;
}
-(void)showDropDown{
    
    [_Dropobj fadeOut];
    [self showPopUpWithTitle:@"" withOption:arr_nationality xy:CGPointMake(CGRectGetMinX(self.txt_language.frame), CGRectGetMaxY(self.txt_language.frame)) size:CGSizeMake(self.txt_language.frame.size.width, 280) isMultiple:NO];
    _index_tag=0;
}
#pragma -mark back
-(IBAction)back:(id)sender{
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma -mark btnTermsOfUseAction
- (IBAction)btnTermsOfUseAction:(id)sender
{
    CMSVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"CMSVC"];
    vc.strType = @"terms-of-services";
    [self.navigationController pushViewController:vc animated:YES];
}

#pragma -mark btnPrivacyPolicyAction
- (IBAction)btnPrivacyPolicyAction:(id)sender
{
    CMSVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"CMSVC"];
    vc.strType = @"privacy-policy";
    [self.navigationController pushViewController:vc animated:YES];
}
@end
