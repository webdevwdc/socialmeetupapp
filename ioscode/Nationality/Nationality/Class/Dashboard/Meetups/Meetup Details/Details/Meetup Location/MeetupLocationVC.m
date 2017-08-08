//
//  MeetupLocationVC.m
//  Nationality
//
//  Created by webskitters on 12/05/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "MeetupLocationVC.h"

@interface MeetupLocationVC ()

@property (strong, nonatomic) GMSMapView *mapView;
@property (strong, nonatomic) GMSMarker *marker;

@end

@implementation MeetupLocationVC

@synthesize mapView,latitude,longitude,marker;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self getcurrentlocation];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


-(void)getcurrentlocation
{
    
    GMSCameraPosition *camera = [GMSCameraPosition cameraWithLatitude:latitude
                                                            longitude:longitude
                                                                 zoom:16];
    mapView = [GMSMapView mapWithFrame:self.mpView.frame camera:camera];
    mapView.delegate = self;
    mapView.mapType = kGMSTypeNormal;
    mapView.myLocationEnabled = YES;
    mapView.trafficEnabled = YES;
    mapView.center = CGPointMake(mapView.frame.size.width/2, mapView.frame.size.height/2);
    
    
    marker = [[GMSMarker alloc] init];
    marker.position = CLLocationCoordinate2DMake(latitude, longitude);
    marker.icon = [UIImage imageNamed:@"details_address_icon_img"];
    marker.map = mapView;

    
    [self.mpView addSubview:mapView];
    
}


#pragma mark - button Action

- (IBAction)btnBackAction:(id)sender {
    
    [self.navigationController popViewControllerAnimated:YES];
}
@end
