//
//  MessagesVC.m
//  Nationality
//
//  Created by webskitters on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "MessagesVC.h"
#import "MessagesCell.h"
#import "MessagesListVC.h"
#import "OtherUserProfileVC.h"
#import "ConnectionVC.h"
#define maxWidth 229
@interface MessagesVC (){
    
//    NSString *strUniqueId;
    NSString *strLastMessage;
}

@end

@implementation MessagesVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.ref = [[FIRDatabase database] reference];
    _tableMessage.tableFooterView = [UIView new];
    _arr_message =[NSMutableArray new];
    strLastMessage = @"";
    if (_isList) {
       // self.ref = [[FIRDatabase database] reference];
        [SVProgressHUD showWithStatus:@"loading" maskType:SVProgressHUDMaskTypeClear];
        [self retriveMessageFromFireBase];
        [self badgeCountRemoveService];

    }
    else
        [self getUniqueIdFromServerForMessage];
    
//    self.ref = [[FIRDatabase database] reference];
//    [self retriveMessageFromFireBase];
    
    UIToolbar* keyboardDoneButtonView = [[UIToolbar alloc] init];
    [keyboardDoneButtonView sizeToFit];
    UIBarButtonItem* doneButton = [[UIBarButtonItem alloc] initWithTitle:@"Done" style:UIBarButtonItemStyleBordered target:self action:@selector(doneClicked)];
    [keyboardDoneButtonView setItems:[NSArray arrayWithObjects:doneButton, nil]];
    _txtVwMessage.inputAccessoryView = keyboardDoneButtonView;
    
    
}
-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(pushNotificationCome:) name:@"MessagePageRefresh" object:nil];
    //[self pushBadgesRemove];
    
}
-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    
    [[NSNotificationCenter defaultCenter] removeObserver:self name:@"MessagePageRefresh" object:nil];
    
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)pushNotificationCome:(NSNotification *)notification{
    NSDictionary *pushInfo =notification.object;
    
    if ([[pushInfo objectForKey:@"from_user_id"] isEqualToString:_strId]) {
        _badge_count=@"1";
        [self badgeCountRemoveService];
    }
}

-(void)pushBadgesRemove{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
    [httpParams setValue:@"message_badge" forKey:@"badge_type"];
    [[ServiceRequestHandler sharedRequestHandler] getService:httpParams posturl:[NSString stringWithFormat:@"%@",HTTPS_REMOVE_PUSH_BADGES] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         if(status==0)
         {
             
             NSDictionary *  dict = (NSDictionary*)data;
             [UIApplication sharedApplication].applicationIconBadgeNumber =0;
             [self request_count];
             
             
         }
         else
         {
             
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
             
         }
     }];
    
}


-(void)getUniqueIdFromServerForMessage{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setObject:_strReceiverId forKey:@"recieverfbid"];
    [httpParams setObject:[Utility getObjectForKey:FACEBOOKID] forKey:@"senderfbid"];
    [httpParams setObject:@"Private" forKey:@"chat_type"];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@",HTTPS_GET_MESSAGE_TOKEN] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = [(NSDictionary*)data st_dictionaryForKey:@"result"];
             _strUniqueId = [[dict st_dictionaryForKey:@"data"] st_stringForKey:@"unique_chatid"];
             
             //self.ref = [[FIRDatabase database] reference];
             [self retriveMessageFromFireBase];
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
             [self backToPrev:nil];
         }
     }];
}
#pragma mark - fetch receive message
-(void)retriveMessageFromFireBase{
    
   // NSDictionary *dict=nil;
//    NSString *userID = [FIRAuth auth].currentUser.uid;
//    if (userID==nil) {
//        return;
//    }
    
   // [hud setOpacity:0.3f];
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, 2 * NSEC_PER_SEC), dispatch_get_main_queue(), ^{
        dispatch_async(dispatch_get_main_queue(), ^{
            [SVProgressHUD dismiss];
        });
    });
    [[[_ref child:@"Messages"] child:_strUniqueId] observeEventType:FIRDataEventTypeChildAdded withBlock:^(FIRDataSnapshot * _Nonnull snapshot) {
        
        if(![snapshot isKindOfClass:[NSNull null]]){
        
         NSDictionary *dict = [NSDictionary dictionaryWithObjectsAndKeys:snapshot.value[@"message"],@"message",snapshot.value[@"senddate"],@"senddate",snapshot.value[@"sender_id"],@"sender_id",snapshot.value[@"receiver_id"],@"receiver_id",snapshot.value[@"name"],@"name",snapshot.value[@"type"],@"type",snapshot.value[@"msgstatus"],@"msgstatus",snapshot.value[@"image_url"],@"image_url",nil];

        NSLog(@"%@",snapshot.value);
        [_arr_message addObject:dict];
            
            [UIView setAnimationsEnabled:NO];
            [_tableMessage beginUpdates];
            
            NSIndexPath *index=[NSIndexPath indexPathForRow:[_arr_message count]-1 inSection:0];
            //                    NSArray *paths = [NSArray arrayWithObject:[NSIndexPath indexPathForRow:[_arrOfChatContent count]-1 inSection:0]];
            //                    [_chatTableView insertRowsAtIndexPaths:paths withRowAnimation:UITableViewRowAnimationTop];
            [_tableMessage insertRowsAtIndexPaths:@[index] withRowAnimation:UITableViewRowAnimationNone];
            [_tableMessage endUpdates];
            [UIView setAnimationsEnabled:YES];
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, 1 * NSEC_PER_SEC), dispatch_get_main_queue(), ^{
                if( _tableMessage.contentSize.height>_tableMessage.bounds.size.height)
                {
                    //                            CGPoint bottomOffset = CGPointMake(0, _chatTableView.contentSize.height - _chatTableView.bounds.size.height);
                    //                            [_chatTableView setContentOffset:bottomOffset animated:YES];
                    if(_arr_message.count>1)
                    {
                        if([_tableMessage contentSize].height > _tableMessage.bounds.size.height)
                        {
                            //                                    CGPoint bottomOffset = CGPointMake(0, height - _chatTableView.bounds.size.height);
                            //                                    [_chatTableView setContentOffset:bottomOffset animated:YES];
                            [_tableMessage scrollToRowAtIndexPath:[NSIndexPath indexPathForRow:_arr_message.count-1 inSection:0] atScrollPosition:UITableViewScrollPositionBottom animated:NO];
                        }
                        //                                [UIView animateWithDuration:0.3 animations:^{
                        //                                    [_chatTableView scrollToRowAtIndexPath:[NSIndexPath indexPathForRow:_arrOfChatContent.count-1 inSection:0] atScrollPosition:UITableViewScrollPositionBottom animated:NO];
                        //                                } completion:^(BOOL finished){
                        //                                    //do something
                        //                                }];
                    }
                    
                }
            });
            
        }
        if([_arr_message count]<=1)
        
            [_tableMessage reloadData];
            
    }];
    
    [_tableMessage reloadData];
    
    [[[_ref child:@"Message"] child:@"001"] observeSingleEventOfType:FIRDataEventTypeChildChanged andPreviousSiblingKeyWithBlock:^(FIRDataSnapshot * _Nonnull snapshot, NSString * _Nullable prevKey) {
        
        NSLog(@"%@",snapshot.value);
    }];
    
 /*   [[[_ref child:@"Message"] child:@"002"] observeSingleEventOfType:FIRDataEventTypeChildAdded andPreviousSiblingKeyWithBlock:^(FIRDataSnapshot * _Nonnull snapshot, NSString * _Nullable prevKey) {
        
        NSLog(@"%@",snapshot.value);
    }];
    
    [[[_ref child:@"Message"] child:@"002"] observeSingleEventOfType:FIRDataEventTypeValue withBlock:^(FIRDataSnapshot * _Nonnull snapshot) {
    
        
        // dict = [NSDictionary dictionaryWithObjectsAndKeys:snapshot.value[@"message"],@"message",snapshot.value[@"senddate"],@"senddate",snapshot.value[@"senderid"],@"senderid",snapshot.value[@"reciverid_single"],@"reciverid",snapshot.value[@"msgstatus"],@"msgstatus",snapshot.value[@"isurl"],@"isurl",snapshot.value[USER_IMAGE],USER_IMAGE,nil];
       // [showRef observeEventType:FEventTypeChildAdded withBlock:^(FDataSnapshot *snapshot) {
        // observeEventType:FEventTypeChildChanged
        NSDictionary *postDict = snapshot.value;
        if (![postDict isKindOfClass:[NSNull class]]) {
          //  [_arr_message addObject:postDict];
            [self.tableMessage reloadData];
        }
    
        NSLog(@"%@",postDict);
    
    } withCancelBlock:^(NSError * _Nonnull error) {
    
        NSLog(@"%@", error.localizedDescription);
        
    }];*/
}

#pragma mark - Webservice Method

-(void)badgeCountRemoveService{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:_toUserID forKey:@"to_userid"];
    [httpParams setValue:_fromUserID forKey:@"from_userid"];
    [httpParams setValue:_badge_count forKey:@"badge_number"];
    [httpParams setValue:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
    
    [[ServiceRequestHandler sharedRequestHandler] getService:httpParams posturl:[NSString stringWithFormat:@"%@",HTTPS_MESSAGE_BADGE_REMOVE] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         if(status==0)
         {
             
             NSDictionary *  dict = (NSDictionary*)data;
             [UIApplication sharedApplication].applicationIconBadgeNumber =0;
             [self request_count];
             
             
         }
         else
         {
             
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
             
         }
     }];
    
}
#pragma --
#pragma mark - TableView Delegate And Data Source
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
//     return 1;
    return _arr_message.count;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *MyIdentifier = @"MessagesCell";
    
    MessagesCell *cell = (MessagesCell*)[tableView dequeueReusableCellWithIdentifier:MyIdentifier];
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    CGSize txtHeight = [Utility getLabelActualHeight:([[[_arr_message objectAtIndex:indexPath.row] st_stringForKey:@"message"] length]>0?[[_arr_message objectAtIndex:indexPath.row] st_stringForKey:@"message"]:@"") Width:(_tableMessage.frame.size.width/2 * wRatio) Font:[UIFont systemFontOfSize:15.0]];
    
    cell.lblUserName.text = [[_arr_message objectAtIndex:indexPath.row] st_stringForKey:@"name"];
    
    if ([[[_arr_message objectAtIndex:indexPath.row] st_stringForKey:@"sender_id"] isEqualToString:[Utility getObjectForKey:FACEBOOKID]] ) {
        cell.viewMessageSender.hidden =NO;
        cell.viewMessageReceiver.hidden = YES;
        cell.imageNodeSender.hidden= NO;
        cell.imageNodeReceiver.hidden =YES;
        cell.lblMessageTestSender.text = [[_arr_message objectAtIndex:indexPath.row] st_stringForKey:@"message"];
      // CGSize txtHeight = [Utility getLabelActualHeight:([cell.lblMessageTestSender.text length]>0?cell.lblMessageTestSender.text:@"") Width:(_tableMessage.frame.size.width/2 * wRatio) Font:[UIFont systemFontOfSize:19.0]];
        cell.lblUserName.textAlignment = NSTextAlignmentRight;
            
            CGRect frame = cell.lblMessageTestSender.frame;
        frame.size.height = txtHeight.height + (IS_IPHONE_6_PLUS==YES? 15:12);
        if (txtHeight.width>maxWidth-10) {
            frame.size.width = txtHeight.width  + (txtHeight.width > 15?8:5);//IS_IPHONE_6_PLUS==YES? 30:30 ;
            cell.lblMessageTestSender.frame = frame;
        }
        else{
            frame.size.width = txtHeight.width ;//IS_IPHONE_6_PLUS==YES? 30:30 ;
            cell.lblMessageTestSender.frame = frame;
        }
        
        cell.viewMessageSender.frame = CGRectMake(cell.imageNodeSender.frame.origin.x - (CGRectGetMaxX(cell.lblMessageTestSender.frame)+6) , cell.viewMessageSender.frame.origin.y, CGRectGetMaxX(cell.lblMessageTestSender.frame)+6, CGRectGetMaxY(cell.lblMessageTestSender.frame)+6);
        
    }
    else {
        cell.viewMessageSender.hidden =YES;
        cell.viewMessageReceiver.hidden = NO;
        cell.imageNodeSender.hidden= YES;
        cell.imageNodeReceiver.hidden =NO;
        cell.lblMessageTestSender.autoresizingMask = YES;
        cell.lblMessageTextRceiver.text = [[_arr_message objectAtIndex:indexPath.row] st_stringForKey:@"message"];
      
         //   CGSize txtHeight = [Utility getLabelActualHeight:([cell.lblMessageTextRceiver.text length]>0?cell.lblMessageTextRceiver.text:@"") Width:(_tableMessage.frame.size.width/2 * wRatio) Font:[UIFont systemFontOfSize:17.0]];
        cell.lblUserName.textAlignment = NSTextAlignmentLeft;
            CGRect frame = cell.lblMessageTextRceiver.frame;
            frame.size.height = txtHeight.height + (IS_IPHONE_6_PLUS==YES? 15:12) ;
        if (txtHeight.width>maxWidth-10) {
            frame.size.width = txtHeight.width  + (txtHeight.width > 15?8:5);//IS_IPHONE_6_PLUS==YES? 30:30 ;
            cell.lblMessageTestSender.frame = frame;
        }
        else{
            frame.size.width = txtHeight.width ;//IS_IPHONE_6_PLUS==YES? 30:30 ;
            cell.lblMessageTestSender.frame = frame;
        }
            cell.lblMessageTextRceiver.frame = frame;

        cell.viewMessageReceiver.frame = CGRectMake(cell.viewMessageReceiver.frame.origin.x, cell.viewMessageReceiver.frame.origin.y, CGRectGetMaxX(cell.lblMessageTextRceiver.frame)+10, CGRectGetMaxY(cell.lblMessageTextRceiver.frame)+6);
       // cell.lblMessageTextRceiver.text = [[_arr_message objectAtIndex:indexPath.row] st_stringForKey:@"message"];
        
    }
    NSTimeInterval interval=[[_arr_message objectAtIndex:indexPath.row] st_longForKey:@"senddate"];
    cell.lblTime.text = [self timeString:interval];
    cell.lblDate.text = [self dateAsddmmyy:interval];
    
    return cell;
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    CGSize txtHeight = [Utility getLabelActualHeight:([[[_arr_message objectAtIndex:indexPath.row] st_stringForKey:@"message"] length]>0?[[_arr_message objectAtIndex:indexPath.row] st_stringForKey:@"message"]:@"") Width:((_tableMessage.frame.size.width/2) * wRatio)  Font:[UIFont systemFontOfSize:15.0]];
    
   
    if (txtHeight.height>24*hRatio) {
        return 62.0 * hRatio + txtHeight.height +20;
    }
    else
        return 86.0 * hRatio + 20;
}


-(void)doneClicked{
    if([_txtVwMessage.text isEqualToString:@""])
        _txtVwMessage.text = @"Type a message";
    [_txtVwMessage resignFirstResponder];
    [UIView beginAnimations:nil context:nil];
    [UIView setAnimationDuration:.3];
    [UIView setAnimationBeginsFromCurrentState:TRUE];
    self.viewMessage.frame = CGRectMake(self.viewMessage.frame.origin.x, self.viewMessage.frame.origin.y + 240., self.viewMessage.frame.size.width, self.viewMessage.frame.size.height);
    _tableMessage.frame = CGRectMake(_tableMessage.frame.origin.x, _tableMessage.frame.origin.y , _tableMessage.frame.size.width, _tableMessage.frame.size.height+240);
    
    
    [UIView commitAnimations];
}

- (BOOL)textViewShouldBeginEditing:(UITextView *)textView{
    
    if([textView.text isEqualToString:@"Type a message"])
        textView.text = @"";
    
    [UIView beginAnimations:nil context:nil];
    [UIView setAnimationDuration:.3];
    [UIView setAnimationBeginsFromCurrentState:TRUE];
    self.viewMessage.frame = CGRectMake(self.viewMessage.frame.origin.x, self.viewMessage.frame.origin.y -240., self.viewMessage.frame.size.width, self.viewMessage.frame.size.height);
    _tableMessage.frame = CGRectMake(_tableMessage.frame.origin.x, _tableMessage.frame.origin.y, _tableMessage.frame.size.width, _tableMessage.frame.size.height-240);
    
    NSIndexPath *index=[NSIndexPath indexPathForRow:[_arr_message count]-1 inSection:0];
    //                    NSArray *paths = [NSArray arrayWithObject:[NSIndexPath indexPathForRow:[_arrOfChatContent count]-1 inSection:0]];
    //                    [_chatTableView insertRowsAtIndexPaths:paths withRowAnimation:UITableViewRowAnimationTop];
   // [_tableMessage insertRowsAtIndexPaths:@[index] withRowAnimation:UITableViewRowAnimationNone];
    
    if (_arr_message.count>0) {
        [_tableMessage endUpdates];
        [_tableMessage scrollToRowAtIndexPath:[NSIndexPath indexPathForRow:_arr_message.count-1 inSection:0] atScrollPosition:UITableViewScrollPositionBottom animated:NO];
    }
    
//    CGRect frame = _tableMessage.frame;
//    frame.size.height=frame.size.height-240;
//    _tableMessage.frame = frame;
    [UIView commitAnimations];
    return YES;
}

- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text{
    
    return YES;
}


#pragma mark - FireBASE SEND MESSAGE
- (IBAction)sendMessageAction:(id)sender {
    
    _btnSend.userInteractionEnabled = NO;
   // self.ref = [[FIRDatabase database] reference];
    
//    self.ref = [[FIRDatabase database] referenceFromURL:[NSString stringWithFormat:@"https://nationality-83054.firebaseio.com/messages/%@",@"0001"]];
     //self.ref = [[Firebase alloc] initWithUrl:[NSString stringWithFormat:@"https://nationality-83054.firebaseio.com/messages/%@",extraKey]];
    
    NSString *newString = [_txtVwMessage.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    BOOL emptyLogin = newString.length == 0;
    
    if (emptyLogin ||[newString isEqualToString:@"Type a message"]) {
        _btnSend.userInteractionEnabled = YES;
        return;
    }
    long timestamp = [[NSDate date] timeIntervalSince1970];
    NSString *strName= [NSString stringWithFormat:@"%@ %@",[Utility getObjectForKey:FIRST_NAME],[Utility getObjectForKey:LAST_NAME]];
    NSString *strTime = [self GetCurrentTimeStamp];
    
    NSDictionary *dict = [[NSDictionary alloc] initWithObjectsAndKeys:[Utility getObjectForKey:FACEBOOKID],@"sender_id",newString,@"message",_strReceiverId,@"receiver_id",strName,@"name",strTime,@"senddate",@"text",@"type",@"",@"image_url",@"read",@"msgstatus", nil];
    
    [[[[_ref child:@"Messages"] child:_strUniqueId] childByAutoId] setValue:dict withCompletionBlock:^(NSError * _Nullable error, FIRDatabaseReference * _Nonnull ref) {
        
        if (error) {
            
            NSLog(@"Data could not be saved.");
            
        } else {
           // NSLog(@"Data saved successfully.ids are %@ - %@",[self senderId],[self reciverId]);
            
          // [self retriveMessageFromFireBase];
            strLastMessage = newString;
            _txtVwMessage.text = @"Type a message";
            _btnSend.userInteractionEnabled = YES;
            [self doneClicked];
            [self sendMessageToServer:strTime];
        }
        
    }];
    
}

-(void)sendMessageToServer:(NSString *)strTime{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setObject:_strId forKey:@"to_userid"];
    [httpParams setObject:[Utility getObjectForKey:USER_ID] forKey:@"from_userid"];
    [httpParams setObject:strTime forKey:@"message_date_time"];
    [httpParams setObject:strLastMessage forKey:@"message"];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@",HTTPS_MESSAGE_PUSH_SENT] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         if(status==0)
         {
             
             NSDictionary *  dict = (NSDictionary*)data;
            // [self request_count];
             
             
         }
         else
         {
             
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
             
         }
     }];

}

- (IBAction)backToPrev:(id)sender {
    //if (strLastMessage.length==0) {
        for (UIViewController *viewCont in self.navigationController.viewControllers) {
            if ([viewCont isKindOfClass:[MessagesListVC class]] || [viewCont isKindOfClass:[OtherUserProfileVC class]]||[viewCont isKindOfClass:[ConnectionVC class]]) {
                [self.navigationController popToViewController:viewCont animated:YES];
            }
        }
        //[self.navigationController popViewControllerAnimated:YES];
  /*  }
    else{
        NSMutableDictionary *httpParams=[NSMutableDictionary new];
        [httpParams setObject:_strId forKey:@"to_userid"];
        [httpParams setObject:[Utility getObjectForKey:USER_ID] forKey:@"from_userid"];
        [httpParams setObject:_strUniqueId forKey:@"chat_token"];
        [httpParams setObject:strLastMessage forKey:@"message"];
        [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@",HTTPS_RECENT_MESSAGE_STORE] getServiceDataCallBack:^(NSInteger status, NSObject *data)
         {
             
             if(status==0)
             {
                 for (UIViewController *viewCont in self.navigationController.viewControllers) {
                     if ([viewCont isKindOfClass:[MessagesListVC class]] || [viewCont isKindOfClass:[OtherUserProfileVC class]]) {
                         [self.navigationController popToViewController:viewCont animated:YES];
                     }
                 }
             }
             
             else
             {
                 NSString *str = (NSString *)data;
                 [[AppController sharedappController] showAlert:str viewController:self];
                 [self.navigationController popViewControllerAnimated:YES];
             }
         }];
        
    }*/
}

- (NSString *)GetCurrentTimeStamp
{
//    NSDateFormatter *objDateformat = [[NSDateFormatter alloc] init];
//    [objDateformat setDateFormat:@"yyyy-MM-dd"];
//    NSString    *strTime = [objDateformat stringFromDate:[NSDate date]];
//    NSString    *strUTCTime = [self GetUTCDateTimeFromLocalTime:strTime];//You can pass your date but be carefull about your date format of NSDateFormatter.
//    NSDate *objUTCDate  = [objDateformat dateFromString:strUTCTime];
    long long milliseconds = (long long)([[NSDate date] timeIntervalSince1970] * 1000.0);
    
    NSString *strTimeStamp = [NSString stringWithFormat:@"%lld",milliseconds];
    NSLog(@"The Timestamp is = %@",strTimeStamp);
    return strTimeStamp;
}

- (NSString *) GetUTCDateTimeFromLocalTime:(NSString *)IN_strLocalTime
{
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
    NSDate  *objDate    = [dateFormatter dateFromString:IN_strLocalTime];
    [dateFormatter setTimeZone:[NSTimeZone timeZoneWithAbbreviation:@"UTC"]];
    NSString *strDateTime   = [dateFormatter stringFromDate:objDate];
    return strDateTime;
}
-(NSString *)timeString:(NSTimeInterval)timeInterval

{
    
    if(timeInterval>0.0)
        
    {
        NSDate *date=[NSDate dateWithTimeIntervalSince1970:(timeInterval+3600)/1000];
        
        //    NSDate *date = [NSDate dateWithTimeIntervalSinceReferenceDate:timeInterval];
        
        NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
        
        [dateFormat setDateStyle:NSDateFormatterMediumStyle];
        
        //    [dateFormat setDateFormat:@"hh:mm a"];
        
        [dateFormat setDateFormat:@"hh:mm a"];
        
        return [dateFormat stringFromDate:date];
        
    }
    
    return @"";
    
}
-(NSString *)dateAsddmmyy:(NSTimeInterval)timeInterval

{
    
    if(timeInterval>0.0)
        
    {
        NSDate *date=[NSDate dateWithTimeIntervalSince1970:(timeInterval+3600)/1000];
        
        //    NSDate *date = [NSDate dateWithTimeIntervalSinceReferenceDate:timeInterval];
        
        NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
        
        [dateFormat setDateStyle:NSDateFormatterMediumStyle];
        
        //    [dateFormat setDateFormat:@"hh:mm a"];
        
        [dateFormat setDateFormat:@"MM/dd/yy"];
        
        return [dateFormat stringFromDate:date];
        
    }
    
    return @"";
    
}

@end
