

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <GoogleMaps/GoogleMaps.h>

#import "AppDelegate.h"


typedef void (^LocationPopUpCallbackBlock)(NSString *strcity);

@interface LocationPopUp : UIView<NSXMLParserDelegate,UITextFieldDelegate,UITableViewDataSource,UITableViewDelegate,GMSMapViewDelegate>{
    
    UIView *customAlertView;
    AppDelegate *appdel;

}


-(id) initAlertwithFrame:(CGRect ) rect;
-(id) initAlertwithFrame:(CGRect ) rect type:(NSInteger)type;

-(void) hide;
-(void)dismiss;

-(void)showInView:(UIView *)inView;

@property (nonatomic, copy)LocationPopUpCallbackBlock callBack;

@property (assign) NSInteger popupType;
@property (assign, nonatomic)  double latitude;
@property (assign, nonatomic) double longitude;
@property (strong, nonatomic) GMSMapView *mapView;
@property (strong, nonatomic) GMSMarker *marker;
@property (weak, nonatomic) IBOutlet UIView *mpView;
@property(strong,nonatomic) CLLocationManager *locationManager;
@end
