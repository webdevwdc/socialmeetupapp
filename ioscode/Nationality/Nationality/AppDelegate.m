//
//  AppDelegate.m
//  Nationality
//
//  Created by webskitters on 03/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "AppDelegate.h"
#import "LoginVC.h"
#import "RegistrationVC.h"
#import "ConnectionVC.h"
#import "MeetupsVC.h"
#import "DashboardVC.h"
#import "MyBulletinVC.h"
//AIzaSyDX4y2Gx0Qn2xYKgAQTMwHzC7dstZDhLRk
@import GooglePlaces;
@import GoogleMaps;

@interface AppDelegate (){
    NSMutableArray *arr_bottom;
}

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    _navi=[[UINavigationController alloc]init];
    [FIRApp configure];
    [[FBSDKApplicationDelegate sharedInstance] application:application
                             didFinishLaunchingWithOptions:launchOptions];
    
    [GMSPlacesClient provideAPIKey:@"AIzaSyDUnKQjLLRiakf6rHGRT8_QXsYIhFjYERM"];
    [GMSServices provideAPIKey:@"AIzaSyDUnKQjLLRiakf6rHGRT8_QXsYIhFjYERM"];
   
    
    if ([[UIApplication sharedApplication] respondsToSelector:@selector(registerForRemoteNotifications)])
    {
        UIUserNotificationType types = UIUserNotificationTypeBadge | UIUserNotificationTypeSound | UIUserNotificationTypeAlert;
        UIUserNotificationSettings *mySettings = [UIUserNotificationSettings settingsForTypes:types categories:nil];
        
        [[UIApplication sharedApplication] registerUserNotificationSettings:mySettings];
        [[UIApplication sharedApplication] registerForRemoteNotifications];
    } else {
        UIRemoteNotificationType types = UIUserNotificationTypeBadge | UIUserNotificationTypeSound | UIUserNotificationTypeAlert;
        [[UIApplication sharedApplication] registerForRemoteNotificationTypes:types];
    }
    
    [Utility saveObjectInUserDefaults:@"134345345455677" forKey:DEVICE_TOKEN];
    
    [UIApplication sharedApplication].applicationIconBadgeNumber = 0;
    
    if ([Utility getObjectForKey:USER_ID]) {
       [self goToDashboard];
    }
    else{
        
        [self goToLogin];
        
    }
    
   
    return YES;
}


-(void)goToLogin{
    UIStoryboard*storyboard=[UIStoryboard storyboardWithName:@"Main" bundle:nil];
    [Utility saveObjectInUserDefaults:@"login" forKey:@"Login"];
    LoginVC * Connection = [storyboard instantiateViewControllerWithIdentifier:@"LoginVC"];
       [(UINavigationController *)self.window.rootViewController pushViewController:Connection animated:YES];
   [self.window makeKeyAndVisible];
}
-(void)goToDashboard{
    UIStoryboard*storyboard=[UIStoryboard storyboardWithName:@"Main" bundle:nil];
    [Utility saveObjectInUserDefaults:@"dashboard" forKey:@"Login"];
    self.tabBarController = [storyboard instantiateViewControllerWithIdentifier:@"DashboardVC"];
    DashboardVC * Connection = [storyboard instantiateViewControllerWithIdentifier:@"DashVC"];
     [Utility saveObjectInUserDefaults:@"no" forKey:@"registration"];
    Connection = (DashboardVC *)[self.tabBarController.viewControllers objectAtIndex:4];
    self.tabBarController.selectedIndex=4;
    [self tabBarDesign];
    [(UINavigationController *)self.window.rootViewController pushViewController:self.tabBarController animated:YES];
  
}
#pragma mark - UITabBar
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
    
    [self viewForTabBarItemAtIndex:0];
    [self viewForTabBarItemAtIndex:1];
    [self viewForTabBarItemAtIndex:2];
    [self viewForTabBarItemAtIndex:3];
    [self viewForTabBarItemAtIndex:4];
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


-(UIView*)viewForTabBarItemAtIndex:(NSInteger)index {
    
    CGRect tabBarRect = self.tabBarController.tabBar.frame;
    NSInteger buttonCount = self.tabBarController.tabBar.items.count;
    CGFloat containingWidth = tabBarRect.size.width/buttonCount;
    CGFloat originX = containingWidth * index ;
    CGRect containingRect = CGRectMake( originX, 0, containingWidth, self.tabBarController.tabBar.frame.size.height );
    CGPoint center = CGPointMake( CGRectGetMidX(containingRect), CGRectGetMidY(containingRect));
    return [ self.tabBarController.tabBar hitTest:center withEvent:nil ];
    
}


- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url
  sourceApplication:(NSString *)sourceApplication annotation:(id)annotation {
    
    BOOL handled = [[FBSDKApplicationDelegate sharedInstance] application:application
                                                                  openURL:url
                                                        sourceApplication:sourceApplication
                                                               annotation:annotation
                    ];
    // Add any custom logic here.
    return handled;
} 
#pragma mrk - Push Notification Methods

-(void)handleRemoteNotification:(UIApplication *)application userInfo:(NSDictionary *)dict {
    
    NSLog(@"%@",dict);
}

- (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error {
    NSLog(@"Error: %@", error.description);
}


- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    NSLog(@"didRegisterForRemoteNotificationsWithDeviceToken: %@", deviceToken);
    
    NSString *deviceTokenString = [[[[[deviceToken description]
                                      stringByReplacingOccurrencesOfString:@"<"withString:@""]
                                     stringByReplacingOccurrencesOfString:@">" withString:@""]
                                    stringByReplacingOccurrencesOfString: @" " withString: @""]
                                   stringByReplacingOccurrencesOfString:@"-" withString:@""];
     [Utility saveObjectInUserDefaults:deviceTokenString forKey:DEVICE_TOKEN];
    
    NSLog(@"didRegisterForRemoteNotificationsWithDeviceToken: %@", deviceTokenString);
    
}

- (void)application:(UIApplication *)application didRegisterUserNotificationSettings:(UIUserNotificationSettings * )notificationSettings {
    NSLog(@"NotificationSettings: %@", notificationSettings);
    [application registerForRemoteNotifications];
}

-(void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
{
    
    UIApplicationState state = [application applicationState];
    NSString *aps=[[userInfo allKeys]objectAtIndex:0];
    NSLog(@"all objects are ********************* %@",[userInfo objectForKey:aps]);
    NSDictionary *dict =[userInfo objectForKey:aps];
    if (state == UIApplicationStateActive)
    {
        [UIApplication sharedApplication].applicationIconBadgeNumber =1+ [UIApplication sharedApplication].applicationIconBadgeNumber;
        [[NSNotificationCenter defaultCenter] postNotificationName:@"RefreshPushCount" object:dict];
    }
    
    else if (state == UIApplicationStateBackground || state == UIApplicationStateInactive)
    {
        
        [UIApplication sharedApplication].applicationIconBadgeNumber =1+ [UIApplication sharedApplication].applicationIconBadgeNumber;
        if ([[dict objectForKey:@"type"]  isEqualToString:@"meetup_request"] || [[dict objectForKey:@"type"]  isEqualToString:@"meetup_request_accept"] || [[dict objectForKey:@"type"]  isEqualToString:@"meetup_comment"]) {

             [[NSNotificationCenter defaultCenter] postNotificationName:@"meetupInvite" object:nil];
        }
        else if ([[dict objectForKey:@"type"]  isEqualToString:@"bulletin_comment"]) {
            
            [[NSNotificationCenter defaultCenter] postNotificationName:@"bulletinCommentPost" object:nil];

        }

      else  if ([[dict objectForKey:@"type"] isEqualToString:@"connection_request"]) {
            [[NSNotificationCenter defaultCenter] postNotificationName:@"goToConnectionPage" object:nil];
        }
        else if ([[dict objectForKey:@"type"] isEqualToString:@"connection_request_accept"]) {
            [[NSNotificationCenter defaultCenter] postNotificationName:@"goToConnectionPage" object:nil];
        }
        else if ([[dict objectForKey:@"type"] isEqualToString:@"messaging"]){
            [[NSNotificationCenter defaultCenter] postNotificationName:@"goToMessageListPage" object:nil];
            
        }

    }
    
    
}
#pragma mark - ------
- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
}


- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}


- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
}


- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    
//    if ([FBSession activeSession].state == FBSessionStateCreatedTokenLoaded) {
//        [self openActiveSessionWithPermissions:nil allowLoginUI:NO];
//    }
//    
//    [FBAppCall handleDidBecomeActive];

}


- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}
#pragma mark - FireBASE AUTH
//[[FIRAuth auth]
// createUserWithEmail:@"yean@webskitters.com"
// password:@"123456789"
// completion:^(FIRUser *_Nullable user,
//              NSError *_Nullable error) {
//     //
//     //         CreateMessageVC  *registervc = [self.storyboard instantiateViewControllerWithIdentifier:@"CreateMessageVC"];
//     //         [self.appdel.navi pushViewController:registervc animated:YES];
// }];
#pragma mark - FireBASE SEND MESSAGE
//@property(strong, nonatomic) FIRAuthStateDidChangeListenerHandle handle;
//@property (strong, nonatomic) FIRDatabaseReference *ref;
 // self.ref = [[FIRDatabase database] reference];
//self.handle = [[FIRAuth auth]
//               addAuthStateDidChangeListener:^(FIRAuth *_Nonnull auth, FIRUser *_Nullable user) {
//                   NSLog(@"%@",user);
//                   [[[[_ref child:@"Message"] child:user.uid] child:@"username"] setValue:user.email];
//                   [[[[_ref child:@"Message"] child:user.uid] child:@"Message"] setValue:_txt_message.text];
//                   [self receiveMessage];
//               }];
//

#pragma mark - fetch receive message
//NSString *userID = [FIRAuth auth].currentUser.uid;
//if (userID==nil) {
//    return;
//}
//[[_ref child:@"Message"] observeSingleEventOfType:FIRDataEventTypeValue withBlock:^(FIRDataSnapshot * _Nonnull snapshot) {
//    
//    NSDictionary *postDict = snapshot.value;
//    if (![postDict isKindOfClass:[NSNull class]]) {
//        [_arr_message addObject:postDict];
//        [self.tbl_message reloadData];
//    }
//    
//    NSLog(@"%@",postDict);
//    
//} withCancelBlock:^(NSError * _Nonnull error) {
//    
//    NSLog(@"%@", error.localizedDescription);
//    
//}];



@end
