//
//  BulletinCommentDetailsVC.m
//  Nationality
//
//  Created by webskitters on 26/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "BulletinCommentDetailsVC.h"
#import "CommentofCommentCell.h"
#import "OtherUserProfileVC.h"

@interface BulletinCommentDetailsVC (){
    
    NSArray *arrCommentReply;
    NSDictionary *dictBulletinComment;
}

@end

@implementation BulletinCommentDetailsVC

- (void)viewDidLoad {
    [super viewDidLoad];
    _imgViewProfile.layer.cornerRadius = _imgViewProfile.frame.size.height/2;
    _imgViewProfile.layer.masksToBounds = YES;
    // Do any additional setup after loading the view.
    
//    _imgViewProfile.layer.cornerRadius = wRatio*20.0;
//    _imgViewProfile.layer.masksToBounds = YES;
    [self bulletinCommentDetails];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)bulletinCommentDetails{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams geturl:[NSString stringWithFormat:@"%@%@/%@",HTTPS_BULLETIN_COMMENT_DETAILS,[self.dictComment st_stringForKey:@"id"],[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             dictBulletinComment = [dict st_dictionaryForKey:@"data"];
             arrCommentReply =[[dictBulletinComment st_arrayForKey:@"respond_child"] mutableCopy];
             [self setBulletinDetails];
             [self.tableCommentReplyList reloadData];

         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
}

-(void)setBulletinDetails{
    
    
    _lblUserName.text = [dictBulletinComment st_stringForKey:@"name"];
    //[_imgViewProfile setImageWithURL:[NSURL URLWithString:[dictBulletinClicked st_stringForKey:@"creator_profile_pic"]]];
    
    [Utility loadCellImage:_imgViewProfile imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[dictBulletinComment st_stringForKey:@"profile_pic"]]];
    _lblCommentDateTime.text = [NSString stringWithFormat:@"%@ at %@",[Utility getFormatedForDate:[dictBulletinComment st_stringForKey:@"created_at"]] ,[Utility getFormatedForTime:[dictBulletinComment st_stringForKey:@"created_at"]]];
//    _lblBulletinTime.text = [Utility getFormatedForTime:[dictBulletinComment st_stringForKey:@"created_at"]];

    
    _lblComment.text = [dictBulletinComment st_stringForKey:@"comment"];
    _lblCommentLikeCount.text =[NSString stringWithFormat:@"%@ %@",[dictBulletinComment st_stringForKey:@"respond_like"],([[dictBulletinComment st_stringForKey:@"respond_like"] integerValue]>1?@"Likes":@"Like")];
    if ([[dictBulletinComment st_stringForKey:@"is_like"] isEqualToString:@"yes"]) {
        [_btnCommentLike setSelected:YES];
        _btnCommentLike.userInteractionEnabled = NO;
    }
    
    _lblCommentReplyCount.text =[NSString stringWithFormat:@"%d %@",(int)[arrCommentReply count],((int)[arrCommentReply count]>1?@"Replies":@"Reply")];
    
    
   
//    if ([[dictBulletinComment st_stringForKey:@"is_like"] isEqualToString:@"yes"]) {
//        _viewLike.backgroundColor = [Utility getColorFromHexString:@"#7d7d7d"];
//        _viewLike.userInteractionEnabled = NO;
//    }
    

    
}


#pragma mark - TableView Dtasource and Delegate

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    
    return arrCommentReply.count;
    
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    
    float txtHeight = [Utility getLabelHeight:([[[arrCommentReply objectAtIndex:indexPath.row] st_stringForKey:@"comment"] length]>0?[[arrCommentReply objectAtIndex:indexPath.row] st_stringForKey:@"comment"]:@"") Width:_tableCommentReplyList.frame.size.width/2 Font:[UIFont fontWithName:@"OPENSANS-REGULAR" size:10.0]];
    
    
    
    return 100.0 * hRatio + txtHeight;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *MyIdentifier = @"CommentofCommentCell";
    
    CommentofCommentCell *cell = (CommentofCommentCell*)[tableView dequeueReusableCellWithIdentifier:MyIdentifier];
    
//    cell.imgViewProfile.layer.cornerRadius = wRatio*20.0;
//    cell.imgViewProfile.layer.masksToBounds = YES;
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    cell.imgViewProfile.layer.cornerRadius = cell.imgViewProfile.frame.size.height/2;
    cell.imgViewProfile.layer.masksToBounds = YES;
    [Utility loadCellImage:cell.imgViewProfile imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrCommentReply objectAtIndex:indexPath.row] st_stringForKey:@"profile_pic"]]];
    
    cell.lblProfileName.text = [[arrCommentReply objectAtIndex:indexPath.row] st_stringForKey:@"name"];
    
    float txtHeight = [Utility getLabelHeight:([[[arrCommentReply objectAtIndex:indexPath.row] st_stringForKey:@"comment"] length]>0?[[arrCommentReply objectAtIndex:indexPath.row] st_stringForKey:@"comment"]:@"") Width:cell.lblComment.frame.size.width Font:[UIFont fontWithName:@"OPENSANS-REGULAR" size:10.0]];
    CGRect frame = cell.lblComment.frame;
    frame.size.height = txtHeight + 10;
    cell.lblComment.frame = frame;
    
    cell.lblComment.text = [[arrCommentReply objectAtIndex:indexPath.row] st_stringForKey:@"comment"];
    
    cell.viewDown.frame = CGRectMake(cell.viewDown.frame.origin.x, CGRectGetMaxY(cell.lblComment.frame)+5, cell.viewDown.frame.size.width, cell.viewDown.frame.size.height);
    
    //    _lblBulletinTime.text = [Utility getFormatedForTime:[dictBulletinComment st_stringForKey:@"created_at"]];
    [cell.btnLike addTarget:self action:@selector(setLikeInReply:) forControlEvents:UIControlEventTouchUpInside];
    cell.lblCommentDateTime.text = [NSString stringWithFormat:@"%@ at %@",[Utility getFormatedForDate:[[arrCommentReply objectAtIndex:indexPath.row] st_stringForKey:@"created_at"]],[Utility getFormatedForTime:[[arrCommentReply objectAtIndex:indexPath.row] st_stringForKey:@"created_at"]]];
    cell.lblcommentLikeCount.text = [NSString stringWithFormat:@"%@ %@",[[arrCommentReply objectAtIndex:indexPath.row] st_stringForKey:@"respond_like"],([[[arrCommentReply objectAtIndex:indexPath.row] st_stringForKey:@"respond_like"] integerValue]>1?@"Likes":@"Like")];
    if ([[[arrCommentReply objectAtIndex:indexPath.row] st_stringForKey:@"is_like"] isEqualToString:@"yes"]) {
        [cell.btnLike setSelected:YES];
        cell.btnLike.userInteractionEnabled = NO;
    }
    else{
        [cell.btnLike setSelected:NO];
        cell.btnLike.userInteractionEnabled = YES;
    }
    [cell.btnUserDetails addTarget:self action:@selector(openUserProfileDetails:) forControlEvents:UIControlEventTouchUpInside];
    //cell.lblCommentReplyCount.text = [[arrCommentReply objectAtIndex:indexPath.row] st_stringForKey:@"name"];
    
    return cell;
    
}

-(void)setLikeInReply:(UIButton *)btnLike{
    
    CGPoint center= btnLike.center;
    CGPoint rootViewPoint = [btnLike.superview convertPoint:center toView:self.tableCommentReplyList];
    NSIndexPath *indexPath = [self.tableCommentReplyList indexPathForRowAtPoint:rootViewPoint];
    NSMutableDictionary *httpParams = [NSMutableDictionary dictionaryWithObjectsAndKeys:[Utility getObjectForKey:USER_ID],@"user_id",[[arrCommentReply objectAtIndex:indexPath.row] st_stringForKey:@"id"],@"comment_id",@"Yes",@"is_like",nil];
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:HTTPS_BULLETIN_COMMENT_LIKE getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             [self bulletinCommentDetails];
             [self.tableCommentReplyList reloadData];
             //;
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
}

- (IBAction)commentLike:(id)sender {
    
    NSMutableDictionary *httpParams = [NSMutableDictionary dictionaryWithObjectsAndKeys:[Utility getObjectForKey:USER_ID],@"user_id",[dictBulletinComment st_stringForKey:@"id"],@"comment_id",@"Yes",@"is_like",nil];
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:HTTPS_BULLETIN_COMMENT_LIKE getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             [self bulletinCommentDetails];
             [self.tableCommentReplyList reloadData];
             //;
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
    CGPoint rootViewPoint = [sender.superview convertPoint:center toView:self.tableCommentReplyList];
    NSIndexPath *indexPath = [self.tableCommentReplyList indexPathForRowAtPoint:rootViewPoint];
    
    OtherUserProfileVC *other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
    
    other.userId=[[arrCommentReply objectAtIndex:indexPath.row] st_stringForKey:@"user_id"];
        // other.strFacebookId=[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:FACEBOOKID];
   
    
    
    
    [self.navigationController pushViewController:other animated:YES];
    
}

- (IBAction)backToPrev:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)userProfileDetails:(id)sender {
    
    OtherUserProfileVC *other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
    
    other.userId=[dictBulletinComment st_stringForKey:@"user_id"];
    // other.strFacebookId=[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:FACEBOOKID];
    
    [self.navigationController pushViewController:other animated:YES];
}
@end
