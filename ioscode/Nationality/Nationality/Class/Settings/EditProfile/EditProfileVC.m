//
//  EditProfileVC.m
//  Nationality
//
//  Created by webskitters on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "EditProfileVC.h"

@interface EditProfileVC (){
    CGFloat lati;
    CGFloat longi;
    NSMutableArray * arr_language;
    NSMutableArray * arr_language_name;

}

@end

@implementation EditProfileVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [self currentLocation];
    arr_language=[NSMutableArray new];
    arr_select_language=[NSMutableArray new];
    arr_language_name=[NSMutableArray new];
    languageId=@"";
    _languageVw.hidden=YES;
    [self loadEditProfileVw];
   }

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)loadEditProfileVw{
    _imgViewProfile.layer.cornerRadius = _imgViewProfile.frame.size.height/2;
    
    _imgViewProfile.layer.masksToBounds = YES;
    [Utility addTextFieldPadding:_txtHomeCity];
    [Utility addTextFieldPadding:_txtTags];
    [Utility addTextFieldPadding:_txtLanguages];
//    _txtviewAddress.pagingEnabled = YES;
//    _txtViewShortBio.pagingEnabled = YES;
    _txtviewAddress.layer.borderWidth=1.0;
    _txtviewAddress.layer.borderColor=[UIColor blackColor].CGColor;
    _txtViewShortBio.layer.borderWidth=1.0;
    _txtViewShortBio.layer.borderColor=[UIColor blackColor].CGColor;
    if ([[Utility getObjectForKey:SHORT_BIO]length]==0) {
        _txtViewShortBio.text=@"Please introduce yourself with a few sentences";
    }
    else
    _txtViewShortBio.text=[Utility getObjectForKey:SHORT_BIO];
   
    _txtviewAddress.text=[Utility getObjectForKey:ADDRESS];
    _txtTags.text=[Utility getObjectForKey:TAG];
    if ([Utility getObjectForKey:ADDRESS]) {
        _txtviewAddress.text=[Utility getObjectForKey:ADDRESS];
    }
    else{
    _txtviewAddress.text=@" --Address--";
    }
    _txtviewAddress.textColor=[UIColor grayColor];
    _txtHomeCity.text=[Utility getObjectForKey:HOME_CITY];
    if ([Utility getObjectForKey:@"profile_pic"]==nil) {
        [Utility loadCellImage:_imgViewProfile imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,@"no_image.jpeg"]];
    
    }
    else{
        [Utility loadCellImage:_imgViewProfile imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[Utility getObjectForKey:@"profile_pic"]]];
    }
  
    
  
    [self getLanguage];
    
}
-(void)doneClicked{
    [_txtViewShortBio resignFirstResponder];
}

-(void)getLanguage{

    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams geturl:[NSString stringWithFormat:@"%@",HTTPS_LANGUAGES] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         if(status==0)
         {
             NSDictionary *dict=(NSDictionary*)data;
             arr_language=[[dict st_arrayForKey:@"data"] mutableCopy];
             NSArray *arr_idList=[NSArray new];
             NSMutableArray*arr_language_name=[NSMutableArray new];
             languageId=[Utility getObjectForKey:LANGUAGE_ID];
             arr_idList=[languageId componentsSeparatedByString:@","];
             for (int i=0; i<arr_language.count; i++) {
                 for (int j=0; j<arr_idList.count; j++) {
                     if ([[[arr_language objectAtIndex:i] st_stringForKey:@"id"]isEqualToString:[arr_idList objectAtIndex:j] ]) {
                         [arr_language_name addObject:[[arr_language objectAtIndex:i] st_stringForKey:@"name"]];
                     }
                 }
             }
             _txtLanguages.text=[arr_language_name componentsJoinedByString:@","];

         }
         else
         {
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
         
     }];
    
}

- (void)finishQuestionnaire:(id)sender
{
    [_txtviewAddress resignFirstResponder];
}




#pragma mark - Image Picker

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    _imgViewProfile.image=[Utility imageWithImage:[info objectForKey:UIImagePickerControllerOriginalImage] scaledToSize:CGSizeMake(100, 100)];
    //[info objectForKey:UIImagePickerControllerOriginalImage];
    imageData = UIImageJPEGRepresentation(_imgViewProfile.image, 0.5);
    [self dismissViewControllerAnimated:YES completion:nil];
    [picker dismissViewControllerAnimated:YES completion:NULL];
    
}



- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
    
    [picker dismissViewControllerAnimated:YES completion:NULL];
    
}
#pragma mark - UITextField

- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField{
    if (textField==_txtLanguages) {
        [textField resignFirstResponder];
        [self showDropDown];
        return NO;
    }
    return YES;
}
- (BOOL)textViewShouldBeginEditing:(UITextView *)textView{
    if (textView==_txtviewAddress) {
        _txtviewAddress.text=@"";
        [_txtviewAddress resignFirstResponder];
       // [self getCurrentPlace];
        
        return NO;
    }
    else if ([textView.text  isEqualToString:@"Please introduce yourself with a few sentences"]){
        textView.text=@"";
    }
    return YES;
}
- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text{
    if (textView==_txtviewAddress) {
        
       // [self getCurrentPlace];
        return NO;
    }
    else if (textView==_txtViewShortBio){
        if ([text isEqualToString:@"\n"]) {
            [textView resignFirstResponder];
            return NO;
        }
    }
    return YES;
}
#pragma mark - LOCATION TRACE
//- (IBAction)getCurrentPlace{
//    lati=[[Utility getObjectForKey:LATITUDE] floatValue];
//    longi=[[Utility getObjectForKey:LONGITUDE] floatValue];
//    CLLocationCoordinate2D center = CLLocationCoordinate2DMake(lati,longi);
//    CLLocationCoordinate2D northEast = CLLocationCoordinate2DMake(center.latitude + 0.001,
//                                                                  center.longitude + 0.001);
//    CLLocationCoordinate2D southWest = CLLocationCoordinate2DMake(center.latitude - 0.001,
//                                                                  center.longitude - 0.001);
//    GMSCoordinateBounds *viewport = [[GMSCoordinateBounds alloc] initWithCoordinate:northEast
//                                                                         coordinate:southWest];
//    GMSPlacePickerConfig *config = [[GMSPlacePickerConfig alloc] initWithViewport:viewport];
//    placePicker = [[GMSPlacePicker alloc] initWithConfig:config];
//    
//    [placePicker pickPlaceWithCallback:^(GMSPlace *place, NSError *error) {
//        if (error != nil) {
//            NSLog(@"Pick Place error %@", [error localizedDescription]);
//            return;
//        }
//        
//        if (place != nil) {
//            //            NSLog(@"current...........lat%f",place.coordinate.latitude);
//            lati=place.coordinate.latitude;
//            longi=place.coordinate.longitude;
//            self.txtviewAddress.text = [[place.formattedAddress componentsSeparatedByString:@", "]
//                                     componentsJoinedByString:@"\n"];
//            NSArray*arr=place.addressComponents;
//            // GMSAddressComponent *gsm=[GMSAddressComponent new];
//            NSDictionary *dic=[NSDictionary new];
//            
////            for (int i=0;i<arr.count;i++) {
////                GMSAddressComponent* myObject = (GMSAddressComponent*)arr[i];
//////                if ([myObject.type isEqualToString:@"locality"]) {
//////                    self.txtviewAddress.text =myObject.name;
//////                    break;
//////                    
//////                }
////                
////            }
//            
//        }
//        else {
//            self.txtviewAddress.text = @"";
//           
//        }
//    }];
//    
//    
//    
//    
//}
//-(void)currentLocation{
//    self.locationManager = [[CLLocationManager alloc] init];
//    [self.locationManager requestWhenInUseAuthorization];
//    
//    
//    self.locationManager.delegate = self;
//    // self.locationManager.desiredAccuracy = kCLLocationAccuracyBest;
//    
//    [self.locationManager startUpdatingLocation];
//    
//}
//- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error
//{
//    NSLog(@"didFailWithError: %@", error);
//    UIAlertView *errorAlert = [[UIAlertView alloc]
//                               initWithTitle:@"Error" message:@"Failed to Get Your Location" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
//    [errorAlert show];
//}
//
//- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation
//{
//    NSLog(@"didUpdateToLocation: %@", newLocation);
//    CLLocation *currentLocation = newLocation;
//    
//    if (currentLocation != nil) {
//        lati =  currentLocation.coordinate.latitude;
//        longi = currentLocation.coordinate.longitude;
//    }
//}

#pragma mark - DropDown
-(void)showDropDown{
  _languageVw.hidden=NO;
    _tbl_language.delegate=self;
    _tbl_language.dataSource=self;
    
    [_tbl_language reloadData];
}
-(IBAction)done:(id)sender{
    if (arr_language_name.count==0) {
        return [[AppController sharedappController] showAlert:@"Please select a language" viewController:self];
    }
   languageId = [arr_select_language componentsJoinedByString:@","];
   _txtLanguages.text=[arr_language_name componentsJoinedByString:@","];
  _languageVw.hidden=YES;
}
-(IBAction)cancel:(id)sender{
    _languageVw.hidden=YES;
}
#pragma mark - DataSource

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return arr_language.count;
}
- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath

{
    static NSString *MyIdentifier = @"cell";
    UITableViewCell *cell =[tableView dequeueReusableCellWithIdentifier:MyIdentifier];
    if (cell == nil)
    {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:MyIdentifier];
    }
    cell.textLabel.text = [[arr_language objectAtIndex:indexPath.row] st_stringForKey:@"name"];
    return cell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
    
    if (cell.accessoryType == UITableViewCellAccessoryCheckmark) {
        cell.accessoryType = UITableViewCellAccessoryNone;
        [arr_select_language removeObject:[[arr_language objectAtIndex:indexPath.row] st_stringForKey:@"id"]];
        [arr_language_name removeObject:[[arr_language objectAtIndex:indexPath.row] st_stringForKey:@"name"]];
    } else {
        [arr_select_language addObject:[[arr_language objectAtIndex:indexPath.row] st_stringForKey:@"id"]];
        [arr_language_name addObject:[[arr_language objectAtIndex:indexPath.row] st_stringForKey:@"name"]];
         //[[arr_language objectAtIndex:indexPath.row] st_stringForKey:@"name"];
        cell.accessoryType = UITableViewCellAccessoryCheckmark;
    }
}

#pragma mark - UIButton Action

-(IBAction)back:(id)sender{
    [self.navigationController popViewControllerAnimated:YES];
}
- (IBAction)btnChangeImageAction:(id)sender {
    
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"Choose Options" message:@""
                                                            preferredStyle:UIAlertControllerStyleActionSheet];
    UIAlertAction *firstAction = [UIAlertAction actionWithTitle:@"Take Photo From camera"
                                                          style:UIAlertActionStyleDefault handler:^(UIAlertAction * action) {
                                                              NSLog(@"You pressed button one");
                                                              if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera]){
                                                                  UIImagePickerController *picker = [[UIImagePickerController alloc] init];
                                                                  picker.delegate = self;
                                                                  picker.allowsEditing = YES;
                                                                  picker.sourceType = UIImagePickerControllerSourceTypeCamera;
                                                                  [self presentViewController:picker animated:YES completion:NULL];
                                                              }
                                                              else {
                                                                  UIAlertController * alert=   [UIAlertController
                                                                                                alertControllerWithTitle:@""
                                                                                                message:@"Camera is not vailable here"
                                                                                                preferredStyle:UIAlertControllerStyleAlert];
                                                                  
                                                                  UIAlertAction* ok = [UIAlertAction
                                                                                       actionWithTitle:@"OK"
                                                                                       style:UIAlertActionStyleDefault
                                                                                       handler:^(UIAlertAction * action)
                                                                                       {
                                                                                           [alert dismissViewControllerAnimated:YES completion:nil];
                                                                                           
                                                                                       }];
                                                                  
                                                                  [alert addAction:ok];
                                                                  
                                                                  [self presentViewController:alert animated:YES completion:nil];
                                                                  
                                                              }
                                                              
                                                          }];
    UIAlertAction *secondAction = [UIAlertAction actionWithTitle:@"Import Photo From Gallery"
                                                           style:UIAlertActionStyleDefault handler:^(UIAlertAction * action) {
                                                               NSLog(@"You pressed button two");
                                                               UIImagePickerController *picker = [[UIImagePickerController alloc] init];
                                                               picker.delegate = self;
                                                               picker.allowsEditing = YES;
                                                               picker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
                                                               
                                                               [self presentViewController:picker animated:YES completion:NULL];
                                                           }];
    
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"Cancel"
                                                           style:UIAlertActionStyleDefault handler:^(UIAlertAction * action) {
                                                               
                                                               
                                                               [alert dismissViewControllerAnimated:YES completion:nil];
                                                           }];
    
    [alert addAction:firstAction];
    [alert addAction:secondAction];
    [alert addAction:cancelAction];
    [self presentViewController:alert animated:YES completion:nil];
    
}

- (IBAction)btnFinishAction:(id)sender {
    if (![Utility istextEmpty:_txtHomeCity withError:@"PLEASE ENTER CURRENT CITY"] /*&&![Utility istextEmpty:_txtLanguages withError:@"PLEASE SELECT LANGUAGE"]*/)
    {

    NSMutableDictionary *httpParams=[NSMutableDictionary new];
     [httpParams setValue:[Utility getObjectForKey:NATIONALITY_ID] forKey:@"nationality_id"];
     [httpParams setValue:[NSString stringWithFormat:@"%f",self.latitude] forKey:@"longitude"];
     [httpParams setValue:[NSString stringWithFormat:@"%f",self.latitude] forKey:@"latitude"];
     [httpParams setValue:[Utility getObjectForKey:FIRST_NAME] forKey:@"first_name"];
     [httpParams setValue:[Utility getObjectForKey:LAST_NAME] forKey:@"last_name"];
     [httpParams setValue:_txtHomeCity.text forKey:@"home_city"];
     [httpParams setValue:[Utility getObjectForKey:TAG] forKey:@"tag"];
        
        if(self.address)
        {
            [httpParams setValue:self.address forKey:@"address"];
        }
        
        else
        {
            [httpParams setValue:@"" forKey:@"address"];
        }
  
     //[httpParams setValue:self.address forKey:@"address"];
     [httpParams setValue:_txtViewShortBio.text forKey:@"short_bio"];
     [httpParams setValue:languageId forKey:@"language_id"];
    [httpParams setValue:[Utility getObjectForKey:INTEREST] forKey:@"interest"];
    if (imageData==nil) {
       [httpParams setValue:@"" forKey:@"profileImage"];
    }
    else{
     [httpParams setValue:[imageData base64EncodedStringWithOptions:0] forKey:@"profileImage"];
    }
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams puturl:[NSString stringWithFormat:@"%@%@",HTTPS_USER_DETAILS,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         if(status==0)
         {
             
             NSDictionary *dic=(NSDictionary*)data;
             NSDictionary *dict=[NSDictionary new];
             dict=[dic st_dictionaryForKey:@"data"];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:USER_ID] forKey:USER_ID];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:NATIONALITY_ID] forKey:NATIONALITY_ID];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:LONGITUDE] forKey:LONGITUDE];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:HOME_CITY] forKey:HOME_CITY];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:ADDRESS] forKey:ADDRESS];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:LATITUDE] forKey:LATITUDE];
            // [Utility saveObjectInUserDefaults:[dict st_stringForKey:TAG] forKey:TAG];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:SHORT_BIO] forKey:SHORT_BIO];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:LANGUAGE_ID] forKey:LANGUAGE_ID];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:INTEREST] forKey:INTEREST];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:FIRST_NAME] forKey:FIRST_NAME];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:LAST_NAME] forKey:LAST_NAME];
             [Utility saveObjectInUserDefaults:[dict st_stringForKey:@"profile_pic"] forKey:@"profile_pic"];

             [[AppController sharedappController] showAlert:[dic st_stringForKey:@"message"] viewController:self];
             
             
         }
         else
         {
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
             
         }
     }];
    }
}


@end
