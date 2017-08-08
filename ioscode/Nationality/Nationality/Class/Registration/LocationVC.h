//
//  LocationVC.h
//  Nationality
//
//  Created by webskitters on 12/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import <CoreLocation/CoreLocation.h>
#import <GoogleMaps/GoogleMaps.h>
@import GooglePlaces;
@import GooglePlacePicker;
@interface LocationVC : UIViewController<GMSMapViewDelegate,CLLocationManagerDelegate,GMSAutocompleteViewControllerDelegate>{
    GMSPlacePicker *placePicker;
}
//@property (strong, nonatomic) CLGeocoder *geocoder;
//@property (strong, nonatomic) CLLocationManager *locationManager;
//@property (strong, nonatomic) IBOutlet UIView *mpView;
//@property (strong, nonatomic) GMSMapView *mapView;
//@property (assign)double latitude;
//@property (assign)double longitude;
//@property (assign)CLLocation *CLLoc;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *addressLabel;

@end
