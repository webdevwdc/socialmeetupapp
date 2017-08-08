//
//  AppDelegate.h
//  Nationality
//
//  Created by webskitters on 03/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import <FBSDKCoreKit/FBSDKCoreKit.h>
@import Firebase;
//#import "SocketIO.h"
@interface AppDelegate : UIResponder <UIApplicationDelegate,UINavigationControllerDelegate>
{
   // SocketIO *socketIO;
}
@property (strong, nonatomic) UIWindow *window;
@property(strong,nonatomic)UINavigationController *navi;
@property(nonatomic,strong) UITabBarController *tabBarController;
@property(nonatomic,strong)AppDelegate *appdel;
@end

