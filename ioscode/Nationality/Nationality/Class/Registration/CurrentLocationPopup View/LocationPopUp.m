

#import "LocationPopUp.h"


#define IS_IPHONE_4 (fabs((double)[[UIScreen mainScreen]bounds].size.height - (double)480) < DBL_EPSILON)
#define IS_IPHONE_5 (fabs((double)[[UIScreen mainScreen]bounds].size.height - (double)568) < DBL_EPSILON)
#define IS_IPHONE_6 (fabs((double)[[UIScreen mainScreen]bounds].size.height - (double)667) < DBL_EPSILON)
#define IS_IPHONE_6_PLUS (fabs((double)[[UIScreen mainScreen]bounds].size.height - (double)736) < DBL_EPSILON)


#import <UIKit/UIKit.h>


@implementation LocationPopUp
@synthesize mapView,latitude,longitude,marker;

-(id) initAlertwithFrame:(CGRect ) rect type:(NSInteger)type
{
    self = [self init];
    
    self.popupType = type;

    return self;
}


-(id) initAlertwithFrame:(CGRect ) rect {
   
    self = [self init];
    
    
    [self buildPoupUI:rect];
    
    return self;
}

-(void)dismiss{
   
    
}

-(void)buildPoupUI:(CGRect ) rect

{
    self.frame = rect;
    self.backgroundColor = [UIColor colorWithRed:0.0/255.0 green:0.0/255.0 blue:0.0/255.0 alpha:0.5f];
    NSString *xibName = @"LocationPopUp";
    customAlertView = [[[NSBundle mainBundle] loadNibNamed:xibName owner:self options:nil] firstObject];
    customAlertView.center = self.center;
    customAlertView.layer.cornerRadius=5.0;
    [self addSubview:customAlertView];
    [self currentLocation];
    NSLog(@"customAlertView = %f",customAlertView.frame.size.width);
    
    
    appdel=(AppDelegate *)[[UIApplication sharedApplication]delegate];
    
    [self buildUI];
}
-(void)buildUI
{
    //customAlertView.layer.cornerRadius = 40.0;
    customAlertView.layer.borderWidth = 5.0;
    customAlertView.layer.borderColor = [UIColor clearColor].CGColor;
    customAlertView.layer.masksToBounds = true;
    //customAlertView.backgroundColor = [Utility getColorWithRed:255.0 green:255.0 blue:255.0 andOpacity:1.0];
    
    for(UIView *vw in [customAlertView subviews])
    {
        if ([vw isKindOfClass:[UIButton class]])
        {
            UIButton *btn = (UIButton *)vw;
            btn.titleLabel.lineBreakMode = NSLineBreakByWordWrapping;
            btn.titleLabel.textAlignment = NSTextAlignmentCenter;
            //btn.titleLabel.font = [UIFont fontWithName:FONT_CALIBRI_REGULAR size:27.0];
            // [btn setCornerRadius:12.0];
            
            switch (btn.tag) {
                case 102:
                    
                    [btn addTarget:self action:@selector(btnCancel:) forControlEvents:UIControlEventTouchUpInside];
                    break;
                    
                case 103:
                    
                    [btn addTarget:self action:@selector(btnOk:) forControlEvents:UIControlEventTouchUpInside];
                    break;
                    
                default:
                    break;
            }
        }
        
        if ([vw isKindOfClass:[UILabel class]])
        {
            UILabel *lbl = (UILabel *)vw;
            switch (lbl.tag) {
                case 100:
                    
                    break;
                    
                case 101:
                    //lbl.font = [UIFont fontWithName:FONT_CALIBRI_REGULAR size:24.0];
                    break;
                    
                case 102:
                    //lbl.font = [UIFont fontWithName:FONT_CALIBRI_REGULAR size:24.0];
                    break;
                    
                default:
                    break;
            }
        }
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
    NSLog(@"didFailWithError: %@", error);
    UIAlertView *errorAlert = [[UIAlertView alloc]
                               initWithTitle:@"Error" message:@"Failed to Get Your Location" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
    [errorAlert show];
}

- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation
{
    NSLog(@"didUpdateToLocation: %@", newLocation);
    CLLocation *currentLocation = newLocation;
    
    if (currentLocation != nil) {
        latitude =  currentLocation.coordinate.latitude;
        longitude = currentLocation.coordinate.longitude;
        [self getcurrentlocation];
    }
}

-(void)getcurrentlocation
{
    
    GMSCameraPosition *camera = [GMSCameraPosition cameraWithLatitude:latitude
                                                            longitude:longitude
                                                                 zoom:20];
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

#pragma --
#pragma btnCancel

- (IBAction)btnCancel:(id)sender
{
    NSString *str=@"cancel";
    if(self.callBack)
    {
        self.callBack(str);
    }
    [self removeFromSuperview];
}



#pragma --
#pragma btnOk

- (IBAction)btnOk:(id)sender
{
    
    [self removeFromSuperview];
    
}
-(IBAction)btn_ok:(id)sender{
    [self removeFromSuperview];
}
#pragma --
#pragma showInView

-(void)showInView:(UIView *)inView
{
    [inView addSubview:self];
}


#pragma --
#pragma showAlertWithVal




-(void) show : (UIViewController *) parent{
    
}

-(void) hide{
    
    [self removeFromSuperview];
    
}

- (void)bounce1AnimationStopped {
    [UIView beginAnimations:nil context:nil];
    [UIView setAnimationDuration:.2];
    [UIView setAnimationDelegate:self];
    [UIView setAnimationDidStopSelector:@selector(bounce2AnimationStopped)];
    self.transform = CGAffineTransformScale(CGAffineTransformIdentity, 0.9, 0.9);
    [UIView commitAnimations];
}

- (void)bounce2AnimationStopped {
    [UIView beginAnimations:nil context:nil];
    [UIView setAnimationDuration:.2];
    self.transform = CGAffineTransformIdentity;
    [UIView commitAnimations];
}


-(void) setBorderOnly:(UIView *) theView withBGColor:(UIColor *) color withCornerRadius :(float) radius andBorderWidth :(float) borderWidth andBorderColor :(UIColor *) bgColor WithAlpha:(float) curAlpha
{
    theView.layer.borderWidth = borderWidth;
    theView.layer.cornerRadius = radius;
    theView.layer.borderColor = [color CGColor];
    UIColor *c = [color colorWithAlphaComponent:curAlpha];
    theView.layer.backgroundColor = [c CGColor];
}

#pragma mark-
#pragma mark-UITextField




@end
