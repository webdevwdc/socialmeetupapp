//
//  BaseViewController.h
//  Nationality
//
//  Created by webskitters on 04/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"
#import <GoogleMaps/GoogleMaps.h>
@import GooglePlaces;
@import GooglePlacePicker;
@interface BaseViewController : UIViewController<UITextFieldDelegate,GMSMapViewDelegate>{
    GMSPlacePicker *placePicker;
    GMSPlacesClient *placesClient;
}
@property(nonatomic,strong)AppDelegate *appdel;
-(void)showAlert:(NSString *)messagel;
//@property(nonatomic,strong) UITabBarController *tabBarController;

@property(nonatomic,strong)NSDictionary *fbresponse;
-(void)facebookIntegration;
-(void)request_count;
@property (strong, nonatomic) CLLocationManager *locationManager;
@property (assign, nonatomic)  double latitude;
@property (assign, nonatomic) double longitude;
@property(nonatomic,strong)NSString * address;
-(void)currentLocation;
@end
