//
//  MeetupDetailsVC.m
//  Nationality
//
//  Created by webskitters on 14/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "MeetupDetailsVC.h"
#import "AttendeesCell.h"
#import "CommentVC.h"
#import "MeetupReplyCommentVC.h"
#import "AttendeesVC.h"
#import "MeetupLocationVC.h"
#import "AddMeetupVC.h"
#import "OtherUserProfileVC.h"
@interface MeetupDetailsVC (){
    NSArray*arr_attendees;
    NSDictionary *dict;
    int totalLikes;
    BOOL isLike;
    BOOL isCommentLike;
    
    BOOL isJoin;
}

@end

@implementation MeetupDetailsVC

- (void)viewDidLoad {
    [super viewDidLoad];
  
    
}
-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:YES];
     [self loadMeetupDetails]; 
}
-(void)loadMeetupDetails{
    arr_attendees=[NSArray new];
    dict=[NSDictionary new];
    [self getMeetupDetails];
    _descriptionVw.layer.borderWidth=1.0;
    _descriptionVw.layer.borderColor=[Utility getColorFromHexString:@"#9BC531"].CGColor;
    _imgViewCommentPerson.layer.cornerRadius = 30.0;
    _imgViewCommentPerson.layer.masksToBounds = YES;
    

}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#pragma mark - Web Service
-(void)showAlert
{
    if (!isJoin) {
        UIAlertController * alert=   [UIAlertController
                                      alertControllerWithTitle:@""
                                      message:@"Do you want to join this meetup?"
                                      preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction* ok = [UIAlertAction
                             actionWithTitle:@"OK"
                             style:UIAlertActionStyleDefault
                             handler:^(UIAlertAction * action)
                             {
                                 [self joinMeetup];
                                 [alert dismissViewControllerAnimated:YES completion:nil];
                                 
                             }];
        UIAlertAction* cancel = [UIAlertAction
                                 actionWithTitle:@"CANCEL"
                                 style:UIAlertActionStyleDefault
                                 handler:^(UIAlertAction * action)
                                 {
                                     [alert dismissViewControllerAnimated:YES completion:nil];
                                     
                                 }];
        
        [alert addAction:ok];
        [alert addAction:cancel];
        [self presentViewController:alert animated:YES completion:nil];
    }
    
    else{
        
        UIAlertController * alert=   [UIAlertController
                                      alertControllerWithTitle:@""
                                      message:@"Do you want to unjoin this meetup?"
                                      preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction* ok = [UIAlertAction
                             actionWithTitle:@"OK"
                             style:UIAlertActionStyleDefault
                             handler:^(UIAlertAction * action)
                             {
                                 [self unJoinMeetup];
                                 [alert dismissViewControllerAnimated:YES completion:nil];
                                 
                             }];
        UIAlertAction* cancel = [UIAlertAction
                                 actionWithTitle:@"CANCEL"
                                 style:UIAlertActionStyleDefault
                                 handler:^(UIAlertAction * action)
                                 {
                                     [alert dismissViewControllerAnimated:YES completion:nil];
                                     
                                 }];
        
        [alert addAction:ok];
        [alert addAction:cancel];
        [self presentViewController:alert animated:YES completion:nil];
    }
    
   
}
-(void)editMeetup{
    AddMeetupVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"AddMeetupVC"];
   
    vc.event=@"Edit";
    
  
        vc.meetupId = _meetupId;
        vc.dictMeetupDetails =[dict st_dictionaryForKey:@"data"];
        vc.lati=[[dict st_stringForKey:@"meetup_lat"] floatValue];
        vc.longi=[[dict st_stringForKey:@"meetup_long"] floatValue];
    vc.arr_invite_user=[NSMutableArray new];
        vc.arr_invite_user=[[dict st_arrayForKey:@"attendee"] mutableCopy];
        vc.mtup_place_name=[dict st_stringForKey:@"place"];
    
 

    [self.navigationController pushViewController:vc animated:YES];
}
-(void)unJoinMeetup{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
    [httpParams setValue:_meetupId forKey:@"meetup_id"];

    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@",HTTPS_MEETUP_UNJOIN] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict1 = (NSDictionary*)data;
             [[AppController sharedappController]showAlert:[[dict1 st_dictionaryForKey:@"result"] st_stringForKey:@"message"] viewController:self];
             [_btnStatus setImage:[UIImage imageNamed:@"top_bar_join"] forState:UIControlStateNormal];
             isJoin = NO;
             //[self.navigationController popViewControllerAnimated:YES];
             
         }
         
         else
         {
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
             
         }
     }];
    
    
}

-(void)joinMeetup{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
    [httpParams setValue:_meetupId forKey:@"meetup_id"];
    [httpParams setValue:@"YES" forKey:@"is_attend"];
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@",HTTPS_MEETUP_JOIN] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict1 = (NSDictionary*)data;
             [[AppController sharedappController]showAlert:[[dict1 st_dictionaryForKey:@"result"] st_stringForKey:@"message"] viewController:self];
             isJoin = YES;
             [_btnStatus setImage:[UIImage imageNamed:@"unjoin"] forState:UIControlStateNormal];
             //[self.navigationController popViewControllerAnimated:YES];
             
         }
         
         else
         {
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
             
         }
     }];
    

}
-(void)getMeetupDetails{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams geturl:[NSString stringWithFormat:@"%@%@/%@",HTTPS_MEETUP_DETAILS,_meetupId,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         if(status==0)
           {
             dict = (NSDictionary*)data;
               NSLog(@"%@",[[[dict st_dictionaryForKey:@"data"]st_stringForKey:@"is_join"] uppercaseString]);
               if ([[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"user_id"] isEqualToString:[Utility getObjectForKey:USER_ID]]) {
                   
                   [_btnStatus setImage:[UIImage imageNamed:@"top_bar_edit"] forState:UIControlStateNormal];
                   _btnStatus.hidden=NO;
                   [_btnStatus addTarget:self action:@selector(editMeetup) forControlEvents:UIControlEventTouchUpInside];
               }
               else if ([[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"type"]isEqualToString:@"public"]) {
                   if ([[[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"is_join"] uppercaseString]isEqualToString:@"NO"]) {
                       [_btnStatus setImage:[UIImage imageNamed:@"top_bar_join"] forState:UIControlStateNormal];
                       _btnStatus.hidden=NO;
                       isJoin = NO;
                       [_btnStatus addTarget:self action:@selector(showAlert) forControlEvents:UIControlEventTouchUpInside];
                   }
                   else{
                       
                       [_btnStatus setImage:[UIImage imageNamed:@"unjoin"] forState:UIControlStateNormal];
                       _btnStatus.hidden=NO;
                       isJoin = YES;
                       [_btnStatus addTarget:self action:@selector(showAlert) forControlEvents:UIControlEventTouchUpInside];
                   }
                       
              
               }
               
               else{
                   _btnStatus.hidden=YES;
               }

               [self setPageView];
               
         }
         else
         {
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
             
         }
     }];
    
}
-(void)pressLike{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
    [httpParams setValue:_meetupId forKey:@"meetup_id"];
    [httpParams setValue:@"YES" forKey:@"is_like"];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@",HTTPS_MEETUP_LIKE] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         if(status==0)
         {
             NSDictionary *dict1 = (NSDictionary*)data;
            // NSString *total_like=[[dict1 st_dictionaryForKey:@"data"] st_stringForKey:@"total_like"];
             int like=totalLikes+1;
             isLike = YES;
             _lblnumberofViews.text=[NSString stringWithFormat:@"%@ %@ | %@ %@ | %d %@",[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"total_view"],([[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"total_view"] integerValue]>1?@"Views":@"View"),[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"total_reply"],([[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"total_reply"] integerValue]>1?@"Replies":@"Reply"),like,(like>1?@"Likes":@"Like")];
              _likeVw.backgroundColor=[Utility getColorFromHexString:@"#7d7d7d"];
             //[self setPageView:dict];
         }
         else
         {
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
             
         }
     }];
    

}
-(void)commentLike
{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"id"] forKey:@"comment_id"];
    [httpParams setValue:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
    [httpParams setValue:@"YES" forKey:@"is_like"];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@",HTTPS_MEETUP_COMMENT_LIKE] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         if(status==0)
         {
             NSDictionary *dict1 = (NSDictionary*)data;
             NSString*like= _lblLikeCount.text;
             int total=[like intValue]+1;
             _lblLikeCount.text=[NSString stringWithFormat:@"%d %@",total,total>1?@"Likes":@"Like"];
             
             _commentLikeView.backgroundColor=[Utility getColorFromHexString:@"#7d7d7d"];
             
             isCommentLike = YES;
         }
         else
         {
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
             
         }
     }];
    

}
//-(void)pressView{
//    NSMutableDictionary *httpParams=[NSMutableDictionary new];
//    [httpParams setValue:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
//    [httpParams setValue:_meetupId forKey:@"meetup_id"];
//    [httpParams setValue:@"YES" forKey:@"is_like"];
//    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@",HTTPS_MEETUP_LIKE] getServiceDataCallBack:^(NSInteger status, NSObject *data)
//     {
//         if(status==0)
//         {
//             NSDictionary *dict1 = (NSDictionary*)data;
//             NSString *total_like=[[dict1 st_dictionaryForKey:@"data"] st_stringForKey:@"total_like"];
//             int like=[total_like intValue]+1;
//             _lblnumberofViews.text=[NSString stringWithFormat:@"%@ %@ | %@ %@ | %d %@",[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"total_view"],([[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"total_view"] integerValue]>1?@"Views":@"View"),[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"total_reply"],([[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"total_reply"] integerValue]>1?@"Replies":@"Reply"),like,(like>1?@"Likes":@"Like")];
//             //[self setPageView:dict];
//         }
//         else
//         {
//             NSString *str = (NSString*)data;
//             [[AppController sharedappController] showAlert:str viewController:self];
//             
//         }
//     }];
//    
//    
//}
//
//
#pragma mark - Page SetUp
-(void)setPageView{
    _lblMeetupName.text=[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"place"];
    _lblTitle.text=[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"title"];
    
    
    if ([[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"is_like"]isEqualToString:@"yes"]) {
        
        isLike = YES;
        _likeVw.backgroundColor=[Utility getColorFromHexString:@"#7d7d7d"];
        
    }
    else{
        isLike = NO;
        _likeVw.backgroundColor=[Utility getColorFromHexString:@"#9BC531"];
    }
    [Utility loadCellImage:_imgViewMeetupAdmin imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"user_photo"] ]];
    _imgViewMeetupAdmin.clipsToBounds=YES;
    NSString *update_dt=[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"date_time"];
    _lblMeetupDate.text= [Utility getFormatedForDate:update_dt];
    _lblMeetupTiming.text=[Utility getFormatedForTime:update_dt];
    _lblNumberofPeople.text=[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"invited_user_count"];
    _lbl_added_pl.text=[[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"invited_user_count"] integerValue]>1 ? @"People are added":@"Person is added";
    NSString *total_view=[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"total_view"];
    int view=[total_view intValue];
    totalLikes = [[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"total_like"]intValue];
    _lblnumberofViews.text=[NSString stringWithFormat:@"%d %@ | %@ %@ | %@ %@",view,(view>1?@"Views":@"View"),[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"total_reply"],([[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"total_reply"] integerValue]>1?@"Replies":@"Reply"),[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"total_like"],([[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"total_like"] integerValue]>1?@"Likes":@"Like")];
    _lblMeetupAdminName.text=[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"user_name"];
    _lblMeetupAddress.text=[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"location"];
    _lblMeetupDesc.text=[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"comment"];
    if ([[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"].count>0) {
    _lblComment.text=[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"comment"];
    _lblCommentPersonName.text=[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"name"];
    _lblLikeCount.text=[NSString stringWithFormat:@"%@  %@",[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"respond_like"],[[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"respond_like"] intValue]>1?@"Likes":@"Like"];
    [Utility loadCellImage:_imgViewCommentPerson imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"profile_pic"]]];
        
        
    if ([[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"is_like"]isEqualToString:@"yes"]) {
        
        isCommentLike = YES;
        _commentLikeView.backgroundColor= [Utility getColorFromHexString:@"#7d7d7d"];
    }
    else{
        
        isCommentLike = NO;
         _commentLikeView.backgroundColor= [Utility getColorFromHexString:@"#9BC531"];
    }
    
        [_btnReplyCount setTitle:[NSString stringWithFormat:@"%@ %@",[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"respond_reply"],([[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"respond_reply"] integerValue]>1 ?@"Replies":@"Reply")] forState:UIControlStateNormal];
        [_btnReplyCount addTarget:self action:@selector(showAllReplies) forControlEvents:UIControlEventTouchUpInside];
        //[NSString stringWithFormat:@"%@ %@",[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"respond_reply"],([[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"respond_reply"] integerValue]>1 ?@"Replies":@"Reply")];
    NSString *dt=[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"updated_at"];
 
    
    _lblCommentDateTime.text=[NSString stringWithFormat:@"%@ at %@",[Utility getFormatedForDate:dt],[Utility getFormatedForTime:dt]];
    }
    
    
    arr_attendees=[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"attendees_list"];
    [_attendeesCollectionView reloadData];
    [self dynamicPageDesign];
    _lblAttendeesCount.text=[NSString stringWithFormat:@"ATTENDEES (%lu)",(unsigned long)arr_attendees.count];
    
}
-(void)showAllReplies{
    if (!_isAccept) {
        return [[AppController sharedappController]showAlert:@"You can't comment this event.You have to accept this meetup." viewController:self];
    }
    MeetupReplyCommentVC *mtReply=[self.storyboard instantiateViewControllerWithIdentifier:@"MeetupReplyCommentVC"];
    mtReply.commentId=[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"id"];
    mtReply.meetupId=[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"meetup_id"];
    mtReply.mainComment=[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"comment"];
    mtReply.like=[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"respond_like"];
    mtReply.dateTime=[NSString stringWithFormat:@"%@ at %@",[Utility getFormatedForDate:[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"updated_at"]],[Utility getFormatedForTime:[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"updated_at"]]];
    mtReply.name=[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"name"];
    mtReply.imgStr=[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"profile_pic"];
    [self.navigationController pushViewController:mtReply animated:YES];
}
-(void)dynamicPageDesign{
    CGFloat newheight;
    newheight=[Utility getLabelHeight:[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"comment"] Width:_lblMeetupDesc.frame.size.width Font:[UIFont fontWithName:@"OPENSANS-REGULAR" size:11.0] ];
    _lblDes.frame=CGRectMake(self.lblDes.frame.origin.x, self.lblDes.frame.origin.y, self.lblDes.frame.size.width, 30);
     self.descriptionVw.frame=CGRectMake(self.descriptionVw.frame.origin.x,  self.descriptionVw.frame.origin.y, self.descriptionVw.frame.size.width, 30+newheight+_lblMeetupDesc.frame.size.height);
    if ([[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"].count>0) {
    
    newheight=[Utility getLabelHeight:[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"comment"] Width:_lblMeetupDesc.frame.size.width Font:[UIFont fontWithName:@"OPENSANS-REGULAR" size:11.0]];
    }
   
    self.userCommentVW.frame=CGRectMake(self.userCommentVW.frame.origin.x, self.userCommentVW.frame.origin.y, self.userCommentVW.frame.size.width, newheight+_lblMeetupDate.frame.size.height+_lblMeetupTiming.frame.size.height+10);
     self.replyVw.frame=CGRectMake(self.replyVw.frame.origin.x, CGRectGetMaxY(self.userCommentVW.frame), self.replyVw.frame.size.width, 60);
    
     self.commentVW.frame=CGRectMake(self.commentVW.frame.origin.x,10+ CGRectGetMaxY(self.descriptionVw.frame),self.commentVW.frame.size.width , self.userCommentVW.frame.size.height+60);
    
    _imgViewCommentPerson.frame=CGRectMake(self.imgViewCommentPerson.frame.origin.x, self.imgViewCommentPerson.frame.origin.y, 60, 60);
   
    if ([[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"].count>0) {
        self.commentVW.hidden=NO;
         self.attendeesVw.frame=CGRectMake(self.attendeesVw.frame.origin.x, CGRectGetMaxY(self.commentVW.frame),self.attendeesVw.frame.size.width , self.attendeesVw.frame.size.height);
    }
    else{
        self.commentVW.hidden=YES;
        self.attendeesVw.frame=CGRectMake(self.attendeesVw.frame.origin.x, CGRectGetMaxY(self.descriptionVw.frame),self.attendeesVw.frame.size.width , self.attendeesVw.frame.size.height);
    }
      _detailsScrollView.contentSize = CGSizeMake(0,CGRectGetMaxY(_attendeesCollectionView.frame)+CGRectGetHeight(_attendeesCollectionView.frame)+400);
}
#pragma mark - Collection View Datasource and Delegate

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
 
    return arr_attendees.count;
}

- (__kindof UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *identifier = @"AttendeesCell";
    AttendeesCell *attendeesCell = (AttendeesCell*) [collectionView dequeueReusableCellWithReuseIdentifier:identifier forIndexPath:indexPath];
    attendeesCell.lblAttendeesName.text=[[arr_attendees objectAtIndex:indexPath.row] st_stringForKey:@"name"];
    attendeesCell.imgViewAttendees.layer.cornerRadius = 35.0;
    attendeesCell.imgViewAttendees.layer.masksToBounds = YES;
    [Utility loadCellImage:attendeesCell.imgViewAttendees imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arr_attendees objectAtIndex:indexPath.row] st_stringForKey:@"profile_pic"]]];
    
    return attendeesCell;
    
}
- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    
    OtherUserProfileVC *other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
    other.userId=[[arr_attendees objectAtIndex:indexPath.row]st_stringForKey:@"user_id"];
    //other.strFacebookId=[[[arr_attendees objectAtIndex:indexPath.section]objectAtIndex:indexPath.row]st_stringForKey:FACEBOOKID];
    [self.navigationController pushViewController:other animated:YES];
    
}
#pragma mark - Button Actions


- (IBAction)btnLikeAction:(id)sender {
    if (!_isAccept) {
        return [[AppController sharedappController]showAlert:@"You can't like this event.You have to accept this meetup." viewController:self];
    }
     else if (!isLike) {
    [self pressLike];
     }
     else {
         [[AppController sharedappController]showAlert:@"You have already liked this meetup." viewController:self];
     }
    
}

- (IBAction)btnCommentAction:(id)sender {
    if (!_isAccept) {
        return [[AppController sharedappController]showAlert:@"You can't comment this event.You have to accept this meetup." viewController:self];
    }
    CommentVC *comment=[self.storyboard instantiateViewControllerWithIdentifier:@"CommentVC"];
   // comment.arr_comment=[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] mutableCopy];
    comment.mtupId=_meetupId;
    comment.reply=[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"title"];
    [self.navigationController pushViewController:comment animated:YES];
    
}
- (IBAction)btnCommentLikeAction:(id)sender {
    if (!_isAccept) {
        return [[AppController sharedappController]showAlert:@"You can't like this event.You have to accept this meetup." viewController:self];
    }
    else if (!isCommentLike) {
        [self commentLike];
    }
    else {
        [[AppController sharedappController]showAlert:@"You have already liked this Comment." viewController:self];
    }
}

- (IBAction)btnCommentReplyAction:(id)sender {
    if (!_isAccept) {
        return [[AppController sharedappController]showAlert:@"You can't comment this event.You have to accept this meetup." viewController:self];
    }
    MeetupReplyCommentVC *mtReply=[self.storyboard instantiateViewControllerWithIdentifier:@"MeetupReplyCommentVC"];
    mtReply.commentId=[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"id"];
    mtReply.userID = [[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"user_id"];
    mtReply.meetupId=[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"meetup_id"];
    mtReply.mainComment=[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"comment"];
    mtReply.like=[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"respond_like"];
    mtReply.dateTime=[NSString stringWithFormat:@"%@ at %@",[Utility getFormatedForDate:[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"updated_at"]],[Utility getFormatedForTime:[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"updated_at"]]];
    mtReply.name=[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"name"];
    mtReply.imgStr=[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"profile_pic"];
    [self.navigationController pushViewController:mtReply animated:YES];

}
- (IBAction)btnSeeAllAction:(id)sender {
    AttendeesVC *attendees=[self.storyboard instantiateViewControllerWithIdentifier:@"AttendeesVC"];
    attendees.arr_attendees_list=[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"attendees_list"] mutableCopy];
    attendees.header_status=@"ATTENDEES";
    attendees.isFromMeetupDetails = YES;
    [self.navigationController pushViewController:attendees animated:YES];
    
}
-(IBAction)backMeetup:(id)sender{
    [self.navigationController popViewControllerAnimated:YES];
}
- (IBAction)btnGoToMapPageAction:(id)sender {
    
    MeetupLocationVC *locationVC=[self.storyboard instantiateViewControllerWithIdentifier:@"MeetupLocationVC"];
    // comment.arr_comment=[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] mutableCopy];
    locationVC.latitude = [[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"meetup_lat"] doubleValue];
    locationVC.longitude=[[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"meetup_long"] doubleValue];
    
    [self.navigationController pushViewController:locationVC animated:YES];
    
}

-(IBAction)showUserProfileDetails:(UIButton *)sender{
    
    OtherUserProfileVC *other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
    
    other.userId=[[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] objectAtIndex:0] st_stringForKey:@"user_id"];
    // other.strFacebookId=[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:FACEBOOKID];
    
    [self.navigationController pushViewController:other animated:YES];
    
}

- (IBAction)openUserProfileDetails:(id)sender {
    
    OtherUserProfileVC *other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
    
    other.userId=[[dict st_dictionaryForKey:@"data"] st_stringForKey:@"user_id"];
    // other.strFacebookId=[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:FACEBOOKID];
    
    [self.navigationController pushViewController:other animated:YES];
    
}
@end
