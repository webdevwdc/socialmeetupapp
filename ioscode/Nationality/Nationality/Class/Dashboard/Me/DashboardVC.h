//
//  DashboardVC.h
//  Nationality
//  Created by webskitters on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <GoogleMaps/GoogleMaps.h>
@interface DashboardVC : BaseViewController<GMSMapViewDelegate>
@property(nonatomic,strong) UITabBarController *tabBarController;
@property(nonatomic,weak)IBOutlet UITableView *tbl_dashboard;
@property(nonatomic,weak)IBOutlet UIScrollView *Vwscroll;
@property(nonatomic,weak)IBOutlet UICollectionView *collectin_frnds;
@property(nonatomic,weak)IBOutlet UILabel*lbl_Name;
@property(nonatomic,weak)IBOutlet UIButton *btn_seeall;
@property(nonatomic,weak)IBOutlet UIButton *btn_connection;
@property(assign)BOOL isRegistration;

@property (strong, nonatomic) GMSMapView *mapView;
@property (strong, nonatomic) GMSMarker *marker;

@end
