//
//  currentLocation.m
//  Nationality
//
//  Created by webskitters on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "currentLocation.h"

@implementation currentLocation

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/
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


- (IBAction)btnFilterOptionAction:(id)sender {
}
@end
