//
//  CommentVC.m
//  Nationality
//
//  Created by webskitters on 20/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "CommentVC.h"
#import "CommentTableViewCell.h"
#import "MeetupReplyCommentVC.h"
#import "OtherUserProfileVC.h"

@interface CommentVC ()

@end

@implementation CommentVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [Utility addTextFieldPadding:_txtComment];
    _arr_comment=[NSMutableArray new];
    _lblReplyMeetup.text=_reply;
    [self getAllMeetup];
    
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#pragma mark -
-(void)getAllMeetup{
        
        NSMutableDictionary *httpParams=[NSMutableDictionary new];
        
        [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams geturl:[NSString stringWithFormat:@"%@%@/%@",HTTPS_MEETUP_DETAILS,_mtupId,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
         {
             if(status==0)
             {
              NSDictionary*   dict = (NSDictionary*)data;
                 
                 _arr_comment=[[[dict st_dictionaryForKey:@"data"] st_arrayForKey:@"respond"] mutableCopy];
                 if (_arr_comment.count>5) {
//                     NSIndexPath* ipath = [NSIndexPath indexPathForRow: [_arr_comment count]-1 inSection:0];
//                     [self.tbl_comment scrollToRowAtIndexPath: ipath atScrollPosition: UITableViewScrollPositionBottom animated: YES];
                 }
//
                 [_tbl_comment reloadData];
                 
             }
             else
             {
                 NSString *str = (NSString*)data;
                 [[AppController sharedappController] showAlert:str viewController:self];
                 
             }
         }];
}

#pragma mark - TableView Dtasource and Delegate
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    NSString *msg=[[_arr_comment objectAtIndex:indexPath.row] st_stringForKey:@"comment"];
    CGFloat newHeight;
    newHeight=[Utility getLabelHeight:msg Width:self.tbl_comment.frame.size.width Font:[UIFont fontWithName:@"OPENSANS-REGULAR" size:9.0]];
    return 120+newHeight;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    
    return _arr_comment.count;
    
    
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *MyIdentifier = @"CommentTableViewCell";
    CommentTableViewCell *cell = (CommentTableViewCell*)[tableView dequeueReusableCellWithIdentifier:MyIdentifier];
    
    cell.lbl_name.text=[[_arr_comment objectAtIndex:indexPath.row] st_stringForKey:@"name"];
    cell.lbl_comment.text=[[_arr_comment objectAtIndex:indexPath.row] st_stringForKey:@"comment"];
//    cell.lbl_date.text= [Utility getFormatedForDate:update_dt];
//    cell.lbl_time.text=[Utility getFormatedForTime:update_dt];
    cell.lbl_date.text=[NSString stringWithFormat:@"%@ at %@",[Utility getFormatedForDate:[[_arr_comment objectAtIndex:indexPath.row] st_stringForKey:@"updated_at"]],[Utility getFormatedForTime:[[_arr_comment objectAtIndex:indexPath.row] st_stringForKey:@"updated_at"]]];
    cell.lbl_like.text=[NSString stringWithFormat:@"%@ %@",[[_arr_comment objectAtIndex:indexPath.row] st_stringForKey:@"respond_like"],[[[_arr_comment objectAtIndex:indexPath.row] st_stringForKey:@"respond_like"] integerValue]>1?@"Likes":@"Like"];
    [cell.btn_reply setTitle:[NSString stringWithFormat:@"/  %@ %@",[[_arr_comment objectAtIndex:indexPath.row] st_stringForKey:@"respond_reply"],([[[_arr_comment objectAtIndex:indexPath.row] st_stringForKey:@"respond_reply"] integerValue]>1?@"Replies":@"Reply")] forState:UIControlStateNormal];
     cell.btn_reply.tag=indexPath.row;
    cell.btn_like.tag=indexPath.row;
    cell.imgProfile.layer.cornerRadius=cell.imgProfile.frame.size.height/2;
    cell.imgProfile.clipsToBounds=YES;
    [Utility loadCellImage:cell.imgProfile imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[_arr_comment objectAtIndex:indexPath.row] st_stringForKey:@"profile_pic"]]];
   // cell.lbl_name.text=[[_arr_comment objectAtIndex:indexPath.row] st_stringForKey:@"meetup_creator"];
    [cell.btn_reply addTarget:self action:@selector(addReply:) forControlEvents:UIControlEventTouchUpInside];
    [cell.btnUserDetails addTarget:self action:@selector(openUserProfileDetails:) forControlEvents:UIControlEventTouchUpInside];
       [cell.btn_like addTarget:self action:@selector(likeComment:) forControlEvents:UIControlEventTouchUpInside];
    if ([[[_arr_comment objectAtIndex:indexPath.row] st_stringForKey:@"is_like"] isEqualToString:@"no"]) {
        [cell.btn_like setSelected:NO];
     
    }
    else{
        
            [cell.btn_like setSelected:YES];
        //cell.btn_like.userInteractionEnabled=NO;
      
    }
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    return cell;
    
}


-(void)openUserProfileDetails:(UIButton *)sender{
    
    CGPoint center= sender.center;
    CGPoint rootViewPoint = [sender.superview convertPoint:center toView:self.tbl_comment];
    NSIndexPath *indexPath = [self.tbl_comment indexPathForRowAtPoint:rootViewPoint];
    
    OtherUserProfileVC *other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
    
    other.userId=[[_arr_comment objectAtIndex:indexPath.row] st_stringForKey:@"user_id"];
    
    [self.navigationController pushViewController:other animated:YES];
    
}


-(void)addReply:(UIButton *)sender{
    
    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:[sender tag] inSection:0];
    CommentTableViewCell *cell=[_tbl_comment cellForRowAtIndexPath:indexPath];
    MeetupReplyCommentVC *mtReply=[self.storyboard instantiateViewControllerWithIdentifier:@"MeetupReplyCommentVC"];
    mtReply.commentId=[[_arr_comment objectAtIndex:indexPath.row] st_stringForKey:@"id"];
    mtReply.mainComment=[[_arr_comment objectAtIndex:indexPath.row]st_stringForKey:@"comment"];
    mtReply.like=[[_arr_comment objectAtIndex:indexPath.row]st_stringForKey:@"respond_like"];
    mtReply.dateTime=[NSString stringWithFormat:@"%@ at %@",[Utility getFormatedForDate:[[_arr_comment objectAtIndex:indexPath.row] st_stringForKey:@"updated_at"]],[Utility getFormatedForTime:[[_arr_comment objectAtIndex:indexPath.row] st_stringForKey:@"updated_at"]]];
    mtReply.name=[[_arr_comment objectAtIndex:indexPath.row]st_stringForKey:@"name"];
    mtReply.meetupId=_mtupId;
    mtReply.imgStr=[[_arr_comment objectAtIndex:indexPath.row] st_stringForKey:@"profile_pic"];
    [self.navigationController pushViewController:mtReply animated:YES];
    
}
-(IBAction)backToMainPage:(id)sender{
    [self.navigationController popViewControllerAnimated:YES];
}
-(IBAction)send:(id)sender{
    NSString* aString= _txtComment.text;
    NSString *newString = [aString stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    if (newString.length==0) {
        return;
    }
    [self sendComment];
}
- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField{
//    [UIView animateWithDuration:0.3 animations:^{
//        CGRect f = self.commentVw.frame;
//        f.origin.y = -230;
//        self.commentVw.frame = f;
//    }];
    [self.view insertSubview:_scrollViewAddComment belowSubview:_tbl_comment];

    return YES;
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField{
     [self.view insertSubview:_scrollViewAddComment belowSubview:_tbl_comment];
//    [UIView animateWithDuration:0.3 animations:^{
//        CGRect f = self.commentVw.frame;
//        f.origin.y = 0;
//        self.commentVw.frame = f;
//        
//    }];
    [textField resignFirstResponder];
    return YES;
}
#pragma mark - Webservice
-(void)sendComment{
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:_txtComment.text forKey:@"comment"];
    [httpParams setValue:@"0" forKey:@"parent_meetup_id"];
    [httpParams setValue:_mtupId forKey:@"meetup_id"];
    [httpParams setValue:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@%@",HTTPS_MEETUP_COMMENT_REPLY,_mtupId] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         if(status==0)
         {
             NSDictionary *dict1 = (NSDictionary*)data;
             NSDictionary *value=[NSDictionary new];
             value=[[dict1 st_dictionaryForKey:@"result"] st_dictionaryForKey:@"data"];
             [_arr_comment addObject:value];
             [self.tbl_comment reloadData];
             _txtComment.text=@"";
             //[self setPageView:dict];
         }
         else
         {
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
             
         }
     }];
    

}
-(void)likeComment:(UIButton *)sender{
    if ([[[_arr_comment objectAtIndex:sender.tag] st_stringForKey:@"is_like"] isEqualToString:@"yes"]) {
        return;
        
    }

    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:[sender tag] inSection:0];
    CommentTableViewCell *cell=[_tbl_comment cellForRowAtIndexPath:indexPath];
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:[[_arr_comment objectAtIndex:sender.tag] st_stringForKey:@"id"] forKey:@"comment_id"];
    [httpParams setValue:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
    [httpParams setValue:@"YES" forKey:@"is_like"];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@",HTTPS_MEETUP_COMMENT_LIKE] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         if(status==0)
         {
             NSDictionary *dict1 = (NSDictionary*)data;
          NSString*like= cell.lbl_like.text;
             int total=[like intValue]+1;
             cell.lbl_like.text=[NSString stringWithFormat:@"%d %@",total,total>1?@"Likes":@"Like"];
             //cell.btn_like.userInteractionEnabled=NO;
             [self getAllMeetup];
         }
         else
         {
             NSString *str = (NSString*)data;
            [[AppController sharedappController] showAlert:str viewController:self];
        }
     }];
    

}
@end
