//
//  AddMeetupVC.m
//  Nationality
//
//  Created by WTS-MAC3052016 on 07/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "AddMeetupVC.h"
#import "MeetupInviteVC.h"
#import "InviteUserCollectionCell.h"
@interface AddMeetupVC (){
    
    NSString *strDateTimeForSend;
    
}

@end

@implementation AddMeetupVC
@synthesize longi,lati,arr_invite_user,arr_mutual_user;
- (void)viewDidLoad {
    [super viewDidLoad];
    
    //arr_invite_user=[NSMutableArray new];
    arr_added_user=[NSMutableArray new];
    _arr_selected_ids=[NSMutableArray new];
    
    status=@"Public"; 
    
    if ([_event isEqualToString:@"Add"]) {
        [self currentLocation];
        //_meetupId=nil;
        _lbl_status_mtup.text=@"Add Meetup";
        [_btnEvent setTitle:@"CREATE MEETUP" forState:UIControlStateNormal];
        isforUpdate = NO;
        
    }
    else{
        _lbl_status_mtup.text=@"Edit Meetup";
        isforUpdate = YES;
        [self setMeetupDetails];
        [_btnEvent setTitle:@"UPDATE MEETUP" forState:UIControlStateNormal];
        
    }
    
    [Utility addTextFieldPadding:self.txtMeetupTitle];
    [Utility addTextFieldPadding:self.txtAddress];
    [Utility addTextFieldPadding:_txtDayTime];
}
-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:YES];
    _addmtupVw.frame=CGRectMake(self.addmtupVw.frame.origin.x, self.addmtupVw.frame.origin.y, self.addmtupVw.frame.size.width, self.addmtupVw.frame.size.height);

        if (arr_invite_user.count>0) {
             _collectionInviteUser.hidden=NO;
            [_collectionInviteUser reloadData];
            _collectionInviteUser.frame=CGRectMake(self.addmtupVw.frame.origin.x, self.addmtupVw.frame.origin.y+self.addmtupVw.frame.size.height, self.collectionInviteUser.frame.size.width, self.collectionInviteUser.frame.size.height);
            _btnEvent.frame=CGRectMake(self.btnEvent.frame.origin.x, self.collectionInviteUser.frame.origin.y+self.collectionInviteUser.frame.size.height, self.btnEvent.frame.size.width, self.btnEvent.frame.size.height);
              self.scrollMeetup.contentSize = CGSizeMake(0,CGRectGetMaxY(_collectionInviteUser.frame)+ _btnEvent.frame.size.height + (60 * hRatio));   ///CGRectGetMaxY(_btnEvent.frame)
        }
        else{
            _collectionInviteUser.hidden=YES;
         _btnEvent.frame=CGRectMake(self.btnEvent.frame.origin.x, self.addmtupVw.frame.origin.y+self.addmtupVw.frame.size.height, self.btnEvent.frame.size.width, self.btnEvent.frame.size.height);
              self.scrollMeetup.contentSize = CGSizeMake(0,CGRectGetMaxY(_btnEvent.frame)+CGRectGetHeight(_btnEvent.frame)+100);
            
        }
    //}
//    if ([_event isEqualToString:@"Add"]){
//    }
//    else{
//       // isforUpdate = YES;
//        [self setMeetupDetails];
//        [_btnEvent setTitle:@"UPDATE EVENT" forState:UIControlStateNormal];
//
//    }
    
    
   
}
#pragma mark - UICollectionView
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
//    if (isforUpdate) {
//        return arr_mutual_user.count;
//    }
//    else
//    {
        return arr_invite_user.count;
//    }
    
}


- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    
    
    InviteUserCollectionCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"InviteUserCollectionCell" forIndexPath:indexPath];

//    if (!isforUpdate) {
        [Utility loadCellImage:cell.imgInviteUser imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arr_invite_user objectAtIndex:indexPath.row]st_stringForKey:@"profile_pic"] ]];
        cell.lblInviteUserName.text=[NSString stringWithFormat:@"%@ %@",[[arr_invite_user objectAtIndex:indexPath.row]st_stringForKey:@"first_name"],[[arr_invite_user objectAtIndex:indexPath.row]st_stringForKey:@"last_name"]];
        cell.imgInviteUser.layer.cornerRadius=cell.imgInviteUser.frame.size.height/2;
        cell.imgInviteUser.layer.borderWidth=1.0;
        cell.imgInviteUser.layer.borderColor=[Utility getColorFromHexString:@"#9BC531"].CGColor;
        cell.imgInviteUser.clipsToBounds=YES;
        


    return cell;
}
- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{

    
}

#pragma -mark setMeetupDetails

-(void)setMeetupDetails
{
    
    self.txtMeetupTitle.text = [self.dictMeetupDetails st_stringForKey:@"title"];
    
    
    
    NSDate *date = [[[AppController sharedappController] formatDateSendServer] dateFromString:[self.dictMeetupDetails st_stringForKey:@"date_time"]];
    self.txtDayTime.text=[[[AppController sharedappController] addMeetupShowDate] stringFromDate:date];
    
   // self.txtDayTime.text = [self.dictMeetupDetails st_stringForKey:@"date_time"];
    strDateTimeForSend = [self.dictMeetupDetails st_stringForKey:@"date_time"];
    self.txtAddress.text = [self.dictMeetupDetails st_stringForKey:@"location"];
    self.textViewNotes.text = [self.dictMeetupDetails st_stringForKey:@"comment"];
    self.txtLocationName.text = [self.dictMeetupDetails st_stringForKey:@"place"];
    lati=[[self.dictMeetupDetails st_stringForKey:@"meetup_lat"] floatValue];
    longi=[[self.dictMeetupDetails st_stringForKey:@"meetup_long"] floatValue];
    status = [self.dictMeetupDetails st_stringForKey:@"type"];
    _btnStatusType.selected=[status isEqualToString:@"public"]?NO:YES;
    arr_added_user = [[self.dictMeetupDetails st_arrayForKey:@"attendee"] mutableCopy];
    if (arr_added_user.count>0) {
        _collectionInviteUser.hidden=NO;
        [_collectionInviteUser reloadData];
        _collectionInviteUser.frame=CGRectMake(self.addmtupVw.frame.origin.x, self.addmtupVw.frame.origin.y+self.addmtupVw.frame.size.height, self.collectionInviteUser.frame.size.width, self.collectionInviteUser.frame.size.height);
        _btnEvent.frame=CGRectMake(self.btnEvent.frame.origin.x, self.collectionInviteUser.frame.origin.y+self.collectionInviteUser.frame.size.height, self.btnEvent.frame.size.width, self.btnEvent.frame.size.height);
        self.scrollMeetup.contentSize = CGSizeMake(0,CGRectGetMaxY(_collectionInviteUser.frame)+ _btnEvent.frame.size.height + (60 * hRatio));   ///CGRectGetMaxY(_btnEvent.frame)
    }
    else{
        _collectionInviteUser.hidden=YES;
        _btnEvent.frame=CGRectMake(self.btnEvent.frame.origin.x, self.addmtupVw.frame.origin.y+self.addmtupVw.frame.size.height, self.btnEvent.frame.size.width, self.btnEvent.frame.size.height);
        self.scrollMeetup.contentSize = CGSizeMake(0,CGRectGetMaxY(_btnEvent.frame)+CGRectGetHeight(_btnEvent.frame)+100);
        
    }

    [self.collectionInviteUser reloadData];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma -mark Text View Delegate
- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text{
    
    if([text isEqualToString:@"\n"]) {
        [textView resignFirstResponder];
        
        return NO;
    }
    
    return YES;
}


- (BOOL) textViewShouldBeginEditing:(UITextView *)textView
{
    
    if([textView.text isEqualToString:@"Add Notes,Talking Points,and Information Here."])
        textView.text = @"";
    else if ([textView.text isEqualToString:@"ENTER YOUR CONTENT"])
    textView.text = @"";
    
    textView.layer.borderColor = [UIColor blackColor].CGColor;
    textView.textColor = [UIColor blackColor];
    return YES;
}

#pragma Text Field Delegate

- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField{
    if(textField==_txtDayTime){
        [self showdateTime];
    }
    else if (textField==_txtAddress){
        
        [self getCurrentPlace];
        return NO;
    }
    return YES;
}
- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    
}




- (IBAction)btnInviteUserAction:(id)sender {
    if (_txtAddress.text.length==0) {
        return [[AppController sharedappController]showAlert:@"Please select address before inviting friends" viewController:self];
    }
    MeetupInviteVC *mtupInvite=[self.storyboard instantiateViewControllerWithIdentifier:@"MeetupInviteVC"];
    mtupInvite.delegate = self;
    mtupInvite.mtupId=_meetupId;
    mtupInvite.Latitude=lati;
    mtupInvite.Longitude=longi;
    //mtupInvite.arr_selected_invite_user=[NSMutableArray new];
    mtupInvite.arr_selected_invite_user=arr_invite_user;
    mtupInvite.arrSelectedIds=_arr_selected_ids;
    mtupInvite.event=_event;
    [self.navigationController pushViewController:mtupInvite animated:YES];
    
}

-(void)postData
{
    if (_arr_selected_ids.count==0) {
        return;
    }
    NSString *strId = [_arr_selected_ids componentsJoinedByString:@","];
    //    for (int i = 1; i<arrSelectedIds.count; i++) {
    //        strId = [strId stringByAppendingString:[NSString stringWithFormat:@",%@",[arrSelectedIds objectAtIndex:i]]];
    //    }
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:strId forKey:@"user_id"];
    [httpParams setValue:_meetupId forKey:@"meetup_id"];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@",HTTPS_MEETUP_INVITE_USER]getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             [self meetupSuccessAlert:[[dict st_dictionaryForKey:@"result"]st_stringForKey:@"message"]];
         }
         
         else
         {
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
             
         }
     }];
    
    
    
}

-(void)inviteUser{
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:@"" forKey:@"user_id"];
    [httpParams setValue:_meetupId forKey:@"meetup_id"];
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@",HTTPS_MEETUP_INVITE_USER] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             [self.navigationController popViewControllerAnimated:YES];
             
         }
         
         else
         {
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
             
         }
     }];
    

}
- (IBAction)btnCreateEventAction:(id)sender {
    
    if (![Utility istextEmpty:_txtMeetupTitle withError:@"ENTER TITLE"]&& ![Utility istextEmpty:_txtDayTime withError:@"SELECT DATE & TIME"]&&![Utility istextEmpty:_txtAddress withError:@"SELECT ADDRESS"] && ![Utility istextEmpty:_txtLocationName withError:@"SELECT LOCATION"] && ![Utility istextViewEmpty:_textViewNotes withError:@"ENTER YOUR CONTENT"] && ![self istextViewEmpty:_textViewNotes withError:@"ENTER YOUR CONTENT"]) {
        
        [self meetupAlert];
    }
    
}

-(void)meetupAlert{
    UIAlertController* alert = [UIAlertController alertControllerWithTitle:@""
                                                                   message:[NSString stringWithFormat:@"Do you want to post meetup on %@?",status]
                                                            preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault
                                                          handler:^(UIAlertAction * action) {
                                                              
                                                              if ([_event isEqualToString:@"Add"]) {
                                                                  [self addMeetup];
                                                                 
                                                              }
                                                              else{
                                                               [self updateMeetup];
                                                              }
                                                              
                                                          }];
    UIAlertAction* cancel = [UIAlertAction actionWithTitle:@"CANCEL" style:UIAlertActionStyleDefault
                                               handler:^(UIAlertAction * action) {
                
                                                   [self dismissViewControllerAnimated:YES completion:NULL];

             }];
    
    [alert addAction:ok];
    [alert addAction:cancel];
    [self presentViewController:alert animated:YES completion:nil];
}

-(void)meetupSuccessAlert:(NSString *)strMessage {
   
        UIAlertController* alert = [UIAlertController alertControllerWithTitle:@""
                                                                       message:strMessage
                                                                preferredStyle:UIAlertControllerStyleAlert];
        
        UIAlertAction* ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault
                                                   handler:^(UIAlertAction * action) {
                                                       
                                                       [self dismissViewControllerAnimated:YES completion:nil];
                                                       [self.navigationController popViewControllerAnimated:YES
                                                        ];
                                                   }];
        [alert addAction:ok];
        
        [self presentViewController:alert animated:YES completion:nil];
    

}

- (BOOL)istextViewEmpty:(UITextView *)textView withError:(NSString*)message
{
    NSString* aString= textView.text;
    if ([aString isEqualToString:@"Add Notes,Talking Points,and Information Here."]) {
        aString=@"";
    }
    NSString *newString = [aString stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    BOOL emptyLogin = newString.length == 0;
    textView.layer.borderWidth=1.0;
    textView.layer.borderColor = emptyLogin ? [UIColor redColor].CGColor : [UIColor blackColor].CGColor;
    if (emptyLogin) {
        textView.text=message;
        textView.textColor = [UIColor redColor];
        //[textView becomeFirstResponder];
    }
    
    return emptyLogin;
}


-(void)addMeetup{
    if ([status isEqualToString:@"private"]) {
    
    if (arr_invite_user.count==0) {
        return [[AppController sharedappController]showAlert:@"Please invite user" viewController:self];
    }
    }
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:_txtMeetupTitle.text forKey:@"title"];
    [httpParams setValue:strDateTimeForSend forKey:@"date_time"];
    [httpParams setValue:_txtAddress.text forKey:@"location"];
    [httpParams setValue:_textViewNotes.text forKey:@"comment"];
    [httpParams setValue:[NSString stringWithFormat:@"%f",lati] forKey:@"meetup_lat"];
    [httpParams setValue:[NSString stringWithFormat:@"%f",longi] forKey:@"meetup_long"];
    [httpParams setValue:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
    [httpParams setValue:status forKey:@"type"];
    [httpParams setValue:_mtup_place_name forKey:@"meetup_place"];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@",HTTPS_ADD_MEETUP] getServiceDataCallBack:^(NSInteger statusCode, NSObject *data)
     {
         
         if(statusCode==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
            _meetupId= [[[dict st_dictionaryForKey:@"result"] st_dictionaryForKey:@"data"] st_stringForKey:@"id"];
             
             if ([status isEqualToString:@"public"]) {
                 [[AppController sharedappController]showAlert:[[dict st_dictionaryForKey:@"result"] st_stringForKey:@"message"] viewController:self];
             }
            [self postData];
                         //[self.navigationController popViewControllerAnimated:YES];
             
         }
         
         else
         {
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
             
         }
     }];

}
-(void)updateMeetup{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:_txtMeetupTitle.text forKey:@"title"];
    [httpParams setValue:strDateTimeForSend forKey:@"date_time"];
    [httpParams setValue:_txtAddress.text forKey:@"location"];
    [httpParams setValue:_textViewNotes.text forKey:@"comment"];
    [httpParams setValue:[NSString stringWithFormat:@"%f",lati] forKey:@"meetup_lat"];
    [httpParams setValue:[NSString stringWithFormat:@"%f",longi] forKey:@"meetup_long"];
    //[httpParams setValue:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
    [httpParams setValue:status forKey:@"type"];
    [httpParams setValue:_mtup_place_name forKey:@"meetup_place"];
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams puturl:[NSString stringWithFormat:@"%@%@",HTTPS_EDIT_MEETUP,_meetupId] getServiceDataCallBack:^(NSInteger status_code, NSObject *data)
     {
         
         if(status_code==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             
             status= [[dict st_dictionaryForKey:@"data"]st_stringForKey:@"type"];
           // if ([status isEqualToString:@"Public"]) {
                
                [[AppController sharedappController]showAlert:[dict st_stringForKey:@"message"] viewController:self];
           
             
                 [self postData];
//             }
//             else{
//                 [self.navigationController popViewControllerAnimated:YES];
//             }
             //[self.navigationController popViewControllerAnimated:YES];
             
         }
         
         else
         {
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
             
         }
     }];
    
}

-(IBAction)back:(id)sender{
    [[AppController sharedappController].arrSelectedIndexesForMeetup removeAllObjects];
    [self.navigationController popViewControllerAnimated:YES];
}


-(IBAction)private:(UIButton*)sender{
    if (sender.selected) {
        status=@"public";
        sender.selected=NO;
       // [sender setTitle:@"  Posting as Public" forState:UIControlStateNormal];
        
    }
    else{
        status=@"private";
        sender.selected=YES;
       // [sender setTitle:@"  Posting as Private" forState:UIControlStateNormal];
    }
    
}
#pragma mark - Get Current Location
- (void)getCurrentPlace{
    CLLocationCoordinate2D center = CLLocationCoordinate2DMake(lati, longi);
    CLLocationCoordinate2D northEast = CLLocationCoordinate2DMake(center.latitude + 0.001,
                                                                  center.longitude + 0.001);
    CLLocationCoordinate2D southWest = CLLocationCoordinate2DMake(center.latitude - 0.001,
                                                                  center.longitude - 0.001);
    GMSCoordinateBounds *viewport = [[GMSCoordinateBounds alloc] initWithCoordinate:northEast
                                                                         coordinate:southWest];
    GMSPlacePickerConfig *config = [[GMSPlacePickerConfig alloc] initWithViewport:viewport];
    placePicker = [[GMSPlacePicker alloc] initWithConfig:config];
    
    [placePicker pickPlaceWithCallback:^(GMSPlace *place, NSError *error) {
        if (error != nil) {
            NSLog(@"Pick Place error %@", [error localizedDescription]);
            return;
        }
        
        if (place != nil) {
            //            NSLog(@"current...........lat%f",place.coordinate.latitude);
            lati=place.coordinate.latitude;
            longi=place.coordinate.longitude;
            self.txtAddress.text = [[place.formattedAddress componentsSeparatedByString:@", "]
                                     componentsJoinedByString:@"\n"];
            
            if ([self.txtAddress.text isEqualToString:@""]) {
                _mtup_place_name=@"";
                
                _txtLocationName.text = _mtup_place_name;

            }
            else{
                _mtup_place_name=place.name;
                
                _txtLocationName.text = _mtup_place_name;
            }
            
            
            
           // NSArray*arr=place.addressComponents;
            // GMSAddressComponent *gsm=[GMSAddressComponent new];
           // NSDictionary *dic=[NSDictionary new];
            
//            for (int i=0;i<arr.count;i++) {
//                GMSAddressComponent* myObject = (GMSAddressComponent*)arr[i];
//                if ([myObject.type isEqualToString:@"locality"]) {
//                    self.txtAddress.text =myObject.name;
//                    break;
//                    
//                }
//                
//            }
            
        }
        else {
            self.txtAddress.text = @"";
                    }
    }];
    
    
    
    
}

#pragma mark - CLLocation Manager

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
        lati =  currentLocation.coordinate.latitude;
        longi = currentLocation.coordinate.longitude;
       
    }
}
#pragma mark - Date and Time Picker
-(void)showdateTime{
    
    _timePick = [[UIDatePicker alloc]initWithFrame:CGRectMake((self.view.bounds.size.width-self.txtDayTime.frame.size.width)/2, CGRectGetMaxY(self.txtDayTime.frame)+50, 280, 200*hRatio)];
    
    _timePick.datePickerMode =UIDatePickerModeDateAndTime;
    _timePick.backgroundColor=[UIColor whiteColor];
    _timePick.layer.borderWidth=1.0;
    _timePick.layer.borderColor=[UIColor grayColor].CGColor;
    _timePick.clipsToBounds=YES;
    NSCalendar *calendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
    NSDate *currentDate = [NSDate date];
    NSDateComponents *comps = [[NSDateComponents alloc] init];
    [comps setDay:1];
    [comps setYear:5];
    NSDate *maxDate = [calendar dateByAddingComponents:comps toDate:currentDate options:0];
    [comps setYear:0];
    NSDate *minDate = [calendar dateByAddingComponents:comps toDate:currentDate options:0];    
    [_timePick setMaximumDate:maxDate];
    [_timePick setMinimumDate:minDate];
    UIToolbar *toolbar = [[UIToolbar alloc] init];
    toolbar.frame = CGRectMake(0, 0, self.timePick.frame.size.width, 38);
    NSArray *items = [[NSArray alloc] init];
    items = @[[[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil], [[UIBarButtonItem alloc]initWithTitle:@"Done" style:UIBarButtonItemStyleDone target:self action:@selector(setDone)]];
    //[items addObject:[[UIBarButtonItem alloc] initWithTitle:@"Done" style:UIBarButtonItemStyleDone target:self action:@selector(setDone:)]];
    [toolbar setItems:items animated:NO];
    //self.timePick.inputView=toolbar;
    _txtDayTime.inputAccessoryView = toolbar;
    _txtDayTime.inputView = _timePick;
    //    [self.timePick addSubview:toolbar];
    //    [self.view addSubview:_timePick];
}
-(void)setDone{
    
    strDateTimeForSend = [[[AppController sharedappController] formatDateSendServer] stringFromDate:self.timePick.date];
    self.txtDayTime.text=[[[AppController sharedappController] addMeetupShowDate] stringFromDate:self.timePick.date];
    // [self.timePick removeFromSuperview];
    [_txtDayTime resignFirstResponder];
    
    //self.btnCalender.selected=NO;
}

#pragma mark - Meetup Inavite Delegate

-(void)updateEventTga:(NSArray *)arr {
    _arr_selected_ids = [arr mutableCopy];
}

@end
