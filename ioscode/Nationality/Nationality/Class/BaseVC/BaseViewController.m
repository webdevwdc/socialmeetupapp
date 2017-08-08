//
//  BaseViewController.m
//  Nationality
//
//  Created by webskitters on 04/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "BaseViewController.h"
#import "MeetupsVC.h"
#import "MyBulletinVC.h"
#import "MessagesVC.h"
#import "ConnectionVC.h"
#import "MessagesListVC.h"
@interface BaseViewController ()

@end

@implementation BaseViewController
@synthesize appdel;
- (void)viewDidLoad {
    [super viewDidLoad];

     appdel=(AppDelegate *)[[UIApplication sharedApplication] delegate];
    // Do any additional setup after loading the view.
  
        
   
}
-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:YES];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(goToConnectionPage:) name:@"goToConnectionPage" object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(goToMessageListPage:) name:@"goToMessageListPage" object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showMeetup:) name:@"meetupInvite" object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(showBulletin:) name:@"bulletinCommentPost" object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(refreshPushCount:) name:@"RefreshPushCount" object:nil];
    [self request_count];
}
-(void)viewWillDisappear:(BOOL)animated
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:@"meetupInvite" object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:@"bulletinCommentPost" object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:@"goToConnectionPage" object:nil];
    
    [[NSNotificationCenter defaultCenter] removeObserver:self name:@"goToMessageListPage" object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:@"RefreshPushCount" object:nil];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)refreshPushCount:(NSNotification *)notification{
    NSDictionary *pushInfo =notification.object;
    
    if ([[pushInfo objectForKey:@"type"] isEqualToString:@"messaging"]) {
        if ([self isKindOfClass:[MessagesVC class]] || [self isKindOfClass:[MessagesListVC class]]) {
            [[NSNotificationCenter defaultCenter] postNotificationName:@"MessagePageRefresh" object:pushInfo];
            //if ([self isKindOfClass:[MessagesListVC class]])
                [self request_count];
        }
        
    }
    else
        [self request_count];
    
    

}
#pragma mark - UITextFieldDelegate
-(void)showMeetup:(NSNotification *)notification{

    self.tabBarController.selectedIndex=1;

}
-(void)showBulletin:(NSNotification *)notification{

    self.tabBarController.selectedIndex=3;
    
}

-(void)goToConnectionPage:(NSNotification *)notification {
    

    self.tabBarController.selectedIndex=0;
    
}

-(void)goToMessageListPage:(NSNotification *)notification {

    self.tabBarController.selectedIndex=2;

    
}
-(void)request_count{
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    NSLog(@"user id %@",[Utility getObjectForKey:USER_ID]);
    
    
    [[ServiceRequestHandler sharedRequestHandler] getRequestCountServiceData:httpParams geturl:[NSString stringWithFormat:@"%@%@",HTTP_USER_REQUEST_COUNT,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             NSLog(@"Badges....%@",dict);
             if ([[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"bulletin_request"] intValue]>0) {
                 [appdel.tabBarController.tabBar.items objectAtIndex:3].badgeValue = [[dict st_dictionaryForKey:@"data"] st_stringForKey:@"bulletin_request"];
                 [self badgesView];
             }
             else{
                   [appdel.tabBarController.tabBar.items objectAtIndex:3].badgeValue =nil;
               
             }
             if ([[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"connection_request"]intValue]>0) {
                 [appdel.tabBarController.tabBar.items objectAtIndex:0].badgeValue = [[dict st_dictionaryForKey:@"data"] st_stringForKey:@"connection_request"];
                 [self badgesView];
             }
             else{
                  [appdel.tabBarController.tabBar.items objectAtIndex:0].badgeValue = nil;
             }
             if ([[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"meetup_request"]intValue]>0) {
               
                 [appdel.tabBarController.tabBar.items objectAtIndex:1].badgeValue = [[dict st_dictionaryForKey:@"data"] st_stringForKey:@"meetup_request"];
                 [self badgesView];
             }
             else{
                  [appdel.tabBarController.tabBar.items objectAtIndex:1].badgeValue = nil;
             }
             if ([[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"message_request"]intValue]>0) {
                 [appdel.tabBarController.tabBar.items objectAtIndex:2].badgeValue = [[dict st_dictionaryForKey:@"data"] st_stringForKey:@"message_request"];
                 [self badgesView];
             }
             else{
                  [appdel.tabBarController.tabBar.items objectAtIndex:2].badgeValue =nil;
             }
             
             
             
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
    
    
}
-(void)badgesView{
    for (UIView *tabBarButton in appdel.tabBarController.tabBar.subviews)
    {
        for (UIView *badgeView in tabBarButton.subviews)
        {
            NSString *className = NSStringFromClass([badgeView class]);
            
            // Looking for _UIBadgeView
            if ([className rangeOfString:@"BadgeView"].location != NSNotFound)
            {
                badgeView.layer.transform = CATransform3DIdentity;
                badgeView.layer.transform = CATransform3DMakeTranslation(-25.0, 0.5, 1.0);
            }
        }
    }
    
}
- (BOOL)textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    return YES;
}
#pragma mark - AlertView
-(void)showAlert:(NSString *)message{
    UIAlertController * alert=[UIAlertController
                               
                               alertControllerWithTitle:@"" message:message preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* yesButton = [UIAlertAction
                                actionWithTitle:@"Ok"
                                style:UIAlertActionStyleDefault
                                handler:^(UIAlertAction * action)
                                {
                                    
                                    
                                }];
    UIAlertAction* noButton = [UIAlertAction
                               actionWithTitle:@"Cancel"
                               style:UIAlertActionStyleDefault
                               handler:^(UIAlertAction * action)
                               {
                                  
                                   
                               }];
    
    [alert addAction:yesButton];
    [alert addAction:noButton];
    
    [self presentViewController:alert animated:YES completion:nil];

}

-(IBAction)back:(id)sender{
   
    
    [appdel.navi popViewControllerAnimated:YES];
}
#pragma mark - Facebook Integration

-(void)facebookProfileImage{
    FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc]
                                  initWithGraphPath:[NSString stringWithFormat:@"me/picture?type=large&redirect=false"]
                                  parameters:nil
                                  HTTPMethod:@"GET"];
    [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection,
                                          id result,
                                          NSError *error) {
        if (!error){
            NSLog(@"result: %@",result);
            NSString *pictureURL = [NSString stringWithFormat:@"%@",[[result objectForKey:@"data"] valueForKey:@"url"]];
            NSURL *url = [NSURL URLWithString:pictureURL];
            // imageData = [NSData dataWithContentsOfURL:url];
            // UIImage *img = [[UIImage alloc] initWithData:data];
            // imageData = UIImageJPEGRepresentation(img,0.5);
            // NSString*  b64str =[data base64EncodedStringWithOptions:NSUTF8StringEncoding];
            
            
            // imageData = [b64str dataUsingEncoding:NSUTF8StringEncoding];
            
            //[self facebookloginService];
        }
        else {
            NSLog(@"result: %@",[error description]);
            
            
            
        }}];
    
    
    
}

#pragma mark - ----
- (void)tabBarController:(UITabBarController *)tabBarController
 didSelectViewController:(UIViewController *)viewController {
    if(viewController == [MeetupsVC class]) {
        // if you want to remove the badge from the current tab
        viewController.tabBarItem.badgeValue = nil;
        
        // or from the new tab
        viewController.tabBarItem.badgeValue = nil;
        
        // update our tab-tracking
    }
   else if(viewController == [ConnectionVC class]) {
        // if you want to remove the badge from the current tab
        viewController.tabBarItem.badgeValue = nil;
        
        // or from the new tab
        viewController.tabBarItem.badgeValue = nil;
        
        // update our tab-tracking
    }
   else if(viewController == [MessagesVC class]) {
       // if you want to remove the badge from the current tab
       viewController.tabBarItem.badgeValue = nil;
       
       // or from the new tab
       viewController.tabBarItem.badgeValue = nil;
       
       // update our tab-tracking
   }
   else if(viewController == [MyBulletinVC class]) {
       // if you want to remove the badge from the current tab
       viewController.tabBarItem.badgeValue = nil;
       
       // or from the new tab
       viewController.tabBarItem.badgeValue = nil;
       
       // update our tab-tracking
   }

}
-(void)currentLocation{
    
    
    self.locationManager = [[CLLocationManager alloc] init];
    [self.locationManager requestWhenInUseAuthorization];
    
    
    self.locationManager.delegate = self;
    // self.locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    
    [self.locationManager startUpdatingLocation];
}
- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error
{
//    NSLog(@"didFailWithError: %@", error);
//    UIAlertView *errorAlert = [[UIAlertView alloc]
//                               initWithTitle:@"Error" message:@"Failed to Get Your Location" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
//    [errorAlert show];
}

- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation
{
    NSLog(@"didUpdateToLocation: %@", newLocation);
    CLLocation *currentLocation = newLocation;
    
    if (currentLocation != nil) {
        _latitude =  currentLocation.coordinate.latitude;
        _longitude = currentLocation.coordinate.longitude;
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
