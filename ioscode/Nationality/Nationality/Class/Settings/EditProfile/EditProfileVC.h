//
//  EditProfileVC.h
//  Nationality
//
//  Created by webskitters on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import <CoreLocation/CoreLocation.h>
#import <GoogleMaps/GoogleMaps.h>
@import GooglePlaces;
@import GooglePlacePicker;

@interface EditProfileVC : BaseViewController<UITextFieldDelegate,UIImagePickerControllerDelegate,UINavigationControllerDelegate,GMSAutocompleteViewControllerDelegate,GMSMapViewDelegate>{
//    GMSPlacesClient *_placesClient;
//    GMSPlacePicker *placePicker;
    NSMutableArray *arr_select_language;
    NSString *languageId;
    NSData *imageData;
}
@property (weak, nonatomic) IBOutlet UITableView* tbl_language;
@property (weak, nonatomic) IBOutlet UIImageView *imgViewProfile;
@property (weak, nonatomic)NSString*CurrentCity;
@property (weak, nonatomic) IBOutlet UITextField *txtHomeCity;
@property (weak, nonatomic) IBOutlet UITextField *txtTags;
@property (weak, nonatomic) IBOutlet UITextField *txtLanguages;
@property (weak, nonatomic) IBOutlet UITextView *txtViewShortBio;
@property (weak, nonatomic) IBOutlet UITextView *txtviewAddress;
@property (weak, nonatomic) IBOutlet UIView *languageVw;
@property (weak, nonatomic) IBOutlet UIScrollView*keyboardVw;
@property(nonatomic,strong)DropDownListView * Dropobj;
@property(nonatomic,assign)NSInteger index_tag;
- (IBAction)btnChangeImageAction:(id)sender;
- (IBAction)btnFinishAction:(id)sender;
@property(strong,nonatomic) CLLocationManager *locationManager;
@end
