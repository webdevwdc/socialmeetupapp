//
//  AddMeetupVC.h
//  Nationality
//
//  Created by WTS-MAC3052016 on 07/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import <CoreLocation/CoreLocation.h>
#import <GoogleMaps/GoogleMaps.h>
@import GooglePlaces;
@import GooglePlacePicker;



@interface AddMeetupVC : BaseViewController{
    
    NSString *status;
    
    NSMutableArray *arr_added_user;
    
    BOOL isforUpdate;
}
@property (weak, nonatomic) IBOutlet UITextField *txtMeetupTitle;
@property (weak, nonatomic) IBOutlet UITextField *txtDayTime;
@property (weak, nonatomic) IBOutlet UITextField *txtAddress;
@property (weak, nonatomic) IBOutlet UITextField *txtLocationName;
@property (weak, nonatomic) IBOutlet UITextView *textViewNotes;
@property (weak, nonatomic) IBOutlet UIButton *btnEvent;
@property (strong, nonatomic) IBOutlet UIButton *btnStatusType;

@property (weak, nonatomic) IBOutlet UICollectionView *collectionInviteUser;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollMeetup;
@property (weak, nonatomic) IBOutlet UIView *addmtupVw;
@property (nonatomic, strong)UIDatePicker *timePick;

- (IBAction)btnInviteUserAction:(id)sender;
- (IBAction)btnCreateEventAction:(id)sender;
@property(strong,nonatomic)NSString *event;
@property(strong,nonatomic)NSString *meetupId;
//@property(strong,nonatomic)NSString *lati;
@property(strong,nonatomic)NSString *mtup_place_name;
@property(strong,nonatomic)IBOutlet UILabel *lbl_status_mtup;
@property(strong,nonatomic) CLLocationManager *locationManager;
@property (strong,nonatomic) NSDictionary *dictMeetupDetails;
@property (strong,nonatomic)NSMutableArray *arr_selected_ids;
@property (strong,nonatomic)NSMutableArray *arr_invite_user;
@property (strong,nonatomic)NSMutableArray *arr_mutual_user;

@property (assign)CGFloat lati;
@property (assign)CGFloat longi;
@property(assign)BOOL isforUpdate;
@end
