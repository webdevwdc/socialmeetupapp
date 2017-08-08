
//  MeetupReplyCommentVC.m
//  Nationality
//  Created by webskitters on 17/04/17.
//  Copyright © 2017 webskitters. All rights reserved.

#import "MeetupReplyCommentVC.h"
#import "CommentofCommentCell.h"
#import "OtherUserProfileVC.h"

@interface MeetupReplyCommentVC (){
    
    NSMutableArray *arrCommentOfComment;
    
}

@end

@implementation MeetupReplyCommentVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [Utility addTextFieldPadding:_txtWriteComment];
    [self loadMeetupreplycomment];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)loadMeetupreplycomment{
    arr_comment_reply=[NSMutableArray new];
    self.lblCommentReplyCount.text=[NSString stringWithFormat:@"%lu  %@",(unsigned long)[arr_comment_reply count],[arr_comment_reply count]>1?@"Replies":@"Reply"];
    _lblProfileName.text=_name;
    _lblComment.text=_mainComment;
    _lblCommentDateTime.text=_dateTime;
   // lblCommentReplyCount.text=
    _lblCommentLikeCount.text=[NSString stringWithFormat:@"%@  %@",_like,[_like intValue]>1?@"Likes":@"Like"];
    self.imgViewProfile.layer.cornerRadius = self.imgViewProfile.layer.frame.size.height/2;
    self.imgViewProfile.layer.masksToBounds = YES;
    [Utility loadCellImage:_imgViewProfile imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,_imgStr]];
    self.lblComment.text=_mainComment;
    self.lblReplyToName.text=_name;
    
    [self getcommentReply];
    arrCommentOfComment = [[NSMutableArray alloc]init];
}
-(void)getcommentReply{
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams geturl:[NSString stringWithFormat:@"%@%@/%@",HTTPS_MEETUP_COMMENT_DETAILS,_commentId,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
            arr_comment_reply=[[[dict st_dictionaryForKey:@"data"]st_arrayForKey:@"respond_child"] mutableCopy];
          self.lblCommentReplyCount.text=[NSString stringWithFormat:@"%lu  %@",(unsigned long)[arr_comment_reply count],[arr_comment_reply count]>1?@"Replies":@"Reply"];
             [self.tblCommentofCommentlist reloadData];
//
             
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];


}
-(void)sendComment{
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:_txtWriteComment.text forKey:@"comment"];
    [httpParams setValue:_commentId forKey:@"parent_meetup_id"];
    [httpParams setValue:_meetupId forKey:@"meetup_id"];
    [httpParams setValue:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@%@",HTTPS_MEETUP_COMMENT_REPLY,_meetupId] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         if(status==0)
         {
             NSDictionary *dict1 = (NSDictionary*)data;
             NSDictionary *value=[NSDictionary new];
             value=[[dict1 st_dictionaryForKey:@"result"] st_dictionaryForKey:@"data"];
             [arr_comment_reply addObject:value];
              self.lblCommentReplyCount.text=[NSString stringWithFormat:@"%lu  %@",(unsigned long)[arr_comment_reply count],[arr_comment_reply count]>1?@"Replies":@"Reply"];
             [self.tblCommentofCommentlist reloadData];
             _txtWriteComment.text=@"";
         }
         else
         {
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
             
         }
     }];
    
    
}


#pragma mark - UITextFields Delegate

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    
    [self.view insertSubview:_ScrollView belowSubview:_tblCommentofCommentlist];
    
   
    
    return YES;
}

- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField {
    
    [self.view insertSubview:_tblCommentofCommentlist belowSubview:_ScrollView];
    if (textField==_txtWriteComment) {
        point=CGPointMake(0,0);
        af=[[UIScreen mainScreen] bounds] ;
       // [self moveScrollView:textField];
    }
 
    
    return YES;
}
//-(void)moveScrollView:(UIView *)theView
//{
//    CGFloat vcy=theView.center.y;
//    
//    CGFloat fsh=af.size.height;
//    CGFloat sa=0.0;
//    //if(IS_IPHONE_5)
//    sa=vcy-fsh/4.0;
//    // else
//    // sa=vcy-fsh/4.5;
//    
//    if(sa<0)
//        sa=0;
//    
//    self.ScrollView.contentSize=CGSizeMake(af.size.width,af.size.height+sa);
//    NSLog(@"%f-%f-%f,%f",self.ScrollView.contentSize.height,af.size.height,kb.size.height,sa);
//    [ self.ScrollView setContentOffset:CGPointMake(0,sa) animated:YES];
//}

#pragma mark - TableView Dtasource and Delegate

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    
    return arr_comment_reply.count;
    
    
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *MyIdentifier = @"CommentofCommentCell";
    
    CommentofCommentCell *cell = (CommentofCommentCell*)[tableView dequeueReusableCellWithIdentifier:MyIdentifier];
    
    cell.imgViewProfile.layer.cornerRadius =cell.imgViewProfile.frame.size.height/2;
    cell.imgViewProfile.layer.masksToBounds = YES;
    cell.btnLike.tag=indexPath.row;
    [cell.btnLike addTarget:self action:@selector(setLikeInReplycomment:) forControlEvents:UIControlEventTouchUpInside];
    [Utility loadCellImage:cell.imgViewProfile imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arr_comment_reply objectAtIndex:indexPath.row] st_stringForKey:@"profile_pic"]]];
    cell.lblProfileName.text=[[arr_comment_reply objectAtIndex:indexPath.row] st_stringForKey:@"name"];
    cell.lblComment.text=[[arr_comment_reply objectAtIndex:indexPath.row] st_stringForKey:@"comment"];
    cell.lblCommentDateTime.text=[NSString stringWithFormat:@"%@ at %@",[Utility getFormatedForDate:[[arr_comment_reply objectAtIndex:indexPath.row] st_stringForKey:@"updated_at"]],[Utility getFormatedForTime:[[arr_comment_reply objectAtIndex:indexPath.row] st_stringForKey:@"updated_at"]]];
    cell.lblcommentLikeCount.text=[NSString stringWithFormat:@"%@ %@",[[arr_comment_reply objectAtIndex:indexPath.row] st_stringForKey:@"respond_like"],[[[arr_comment_reply objectAtIndex:indexPath.row] st_stringForKey:@"respond_like"] intValue]>1?@"Likes":@"Like"];
    
   [cell.btnUserDetails addTarget:self action:@selector(openUserProfileDetails:) forControlEvents:UIControlEventTouchUpInside];
    if ([[[arr_comment_reply objectAtIndex:indexPath.row] st_stringForKey:@"is_like"] isEqualToString:@"yes"]) {
        [cell.btnLike setSelected:YES];
    }
    else{
        [cell.btnLike setSelected:NO];
    }

    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    
    return cell;
    
}

-(void)setLikeInReplycomment:(UIButton *)btnLike{
    if ([[[arr_comment_reply objectAtIndex:btnLike.tag] st_stringForKey:@"is_like"] isEqualToString:@"yes"]) {
       return;
    }

    CGPoint center= btnLike.center;
    CGPoint rootViewPoint = [btnLike.superview convertPoint:center toView:self.tblCommentofCommentlist];
    NSIndexPath *indexPath = [self.tblCommentofCommentlist indexPathForRowAtPoint:rootViewPoint];
    NSMutableDictionary *httpParams = [NSMutableDictionary dictionaryWithObjectsAndKeys:[Utility getObjectForKey:USER_ID],@"user_id",[[arr_comment_reply objectAtIndex:indexPath.row] st_stringForKey:@"id"],@"comment_id",@"Yes",@"is_like",nil];
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:HTTPS_MEETUP_COMMENT_LIKE getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             [self getcommentReply];

         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
}

-(void)openUserProfileDetails:(UIButton *)sender{
    
    CGPoint center= sender.center;
    CGPoint rootViewPoint = [sender.superview convertPoint:center toView:self.tblCommentofCommentlist];
    NSIndexPath *indexPath = [self.tblCommentofCommentlist indexPathForRowAtPoint:rootViewPoint];
    
    OtherUserProfileVC *other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
    
    other.userId=[[arr_comment_reply objectAtIndex:indexPath.row] st_stringForKey:@"user_id"];
    
    
    
    
    [self.navigationController pushViewController:other animated:YES];
    
}

#pragma mrk - Button Action

- (IBAction)btnSendAction:(id)sender {
    
    [self.view insertSubview:_ScrollView belowSubview:_tblCommentofCommentlist];
    
    NSString* aString= _txtWriteComment.text;
    NSString *newString = [aString stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    if (newString.length==0) {
        return;
    }
    
    [self sendComment];
    
    [self.view endEditing:YES];
    
}

- (IBAction)showProfileDetails:(id)sender {
    
    OtherUserProfileVC *other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
    
    other.userId=_userID;
    
    
    [self.navigationController pushViewController:other animated:YES];
}
-(IBAction)backComment:(id)sender{
    [self.navigationController popViewControllerAnimated:YES];
}
@end
