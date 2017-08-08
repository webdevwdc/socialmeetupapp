//
//  BulletinClickedVC.m
//  Nationality
//
//  Created by webskitters on 17/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "BulletinClickedVC.h"
#import "BulletinResponseCell.h"
#import "BulletinResponseVC.h"
#import "BulletinCommentDetailsVC.h"
#import "OtherUserProfileVC.h"

@interface BulletinClickedVC (){
    
    NSMutableArray *arrBulletinResponse;
    NSDictionary *dictBulletinClicked;
    BOOL isSetHeight;
    
}

@end

@implementation BulletinClickedVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
     isSetHeight = NO;
  //  arrBulletinResponse = [[NSMutableArray alloc]init];
    
    
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    
    [self getMyAllBulletin];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma --
#pragma mark -GetMyBulletin
-(void)getMyAllBulletin{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams geturl:[NSString stringWithFormat:@"%@%@/%@",HTTPS_BULLETIN_CLICKED,_strBulletinId,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             dictBulletinClicked=[dict st_dictionaryForKey:@"data"];
             arrBulletinResponse =[[dictBulletinClicked st_arrayForKey:@"respond"] mutableCopy];
             [self setBulletinDetails];
             [self.tblBulletinResponseList reloadData];
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
}

-(void)setBulletinDetails{
    
    
    _lblProfileName.text = [dictBulletinClicked st_stringForKey:@"creator_name"];
    //[_imgViewProfile setImageWithURL:[NSURL URLWithString:[dictBulletinClicked st_stringForKey:@"creator_profile_pic"]]];
    _imgViewProfile.layer.cornerRadius = _imgViewProfile.frame.size.width/2;
    _imgViewProfile.layer.masksToBounds = YES;
    [Utility loadCellImage:_imgViewProfile imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[dictBulletinClicked st_stringForKey:@"creator_profile_pic"]]];
    _lblBulletinDate.text = [Utility getFormatedForDate:[dictBulletinClicked st_stringForKey:@"created_at"]];
    _lblBulletinTime.text = [Utility getFormatedForTime:[dictBulletinClicked st_stringForKey:@"created_at"]];
    _lblBulletinName.text = [dictBulletinClicked st_stringForKey:@"title"];
    _lblBulletinTitle.text =[dictBulletinClicked st_stringForKey:@"title"];
//    _lblLikeCount.text = [dictBulletinClicked st_stringForKey:@"total_like"];
//    _lblViewCount.text = [dictBulletinClicked st_stringForKey:@"total_view"];
//    _lblRepliesCount.text = [dictBulletinClicked st_stringForKey:@"total_reply"];
    
    _lblRepliesCount.text=[NSString stringWithFormat:@"%@ %@ | %@ %@ | %@ %@",[dictBulletinClicked st_stringForKey:@"total_view"],([[dictBulletinClicked st_stringForKey:@"total_view"] integerValue]>1?@"Views":@"View"),[dictBulletinClicked st_stringForKey:@"total_reply"],([[dictBulletinClicked st_stringForKey:@"total_reply"] integerValue]>1?@"Replies":@"Reply"),[dictBulletinClicked st_stringForKey:@"total_like"],([[dictBulletinClicked st_stringForKey:@"total_like"] integerValue]>1?@"Likes":@"Like")];
    if ([[dictBulletinClicked st_stringForKey:@"is_creator"] isEqualToString:@"yes"] || [[dictBulletinClicked st_stringForKey:@"is_like"] isEqualToString:@"yes"]) {
        _viewLike.backgroundColor = [Utility getColorFromHexString:@"#7d7d7d"];
        _viewLike.userInteractionEnabled = NO;
    }
    if (!isSetHeight) {
         isSetHeight = YES;
        float txtHeight = [Utility getLabelHeight:([[dictBulletinClicked st_stringForKey:@"content"] length]>0?[dictBulletinClicked st_stringForKey:@"content"]:@"") Width:_lblBulletinDesc.frame.size.width Font:[UIFont systemFontOfSize:15.0]];
        CGRect frame = _lblBulletinDesc.frame;
        frame.size.height = txtHeight + 25;
        _lblBulletinDesc.frame = frame;
        _tblBulletinResponseList.frame = CGRectMake(_tblBulletinResponseList.frame.origin.x, CGRectGetMaxY(_lblBulletinDesc.frame)+5, _tblBulletinResponseList.frame.size.width, _tblBulletinResponseList.frame.size.height-txtHeight+10);
    }
    
    
    _lblBulletinDesc.text = [dictBulletinClicked st_stringForKey:@"content"];
    _lblResponseCount.text = [NSString stringWithFormat:@"(%d) Response",(int)arrBulletinResponse.count];
    
}

#pragma mark - TableView Dtasource and Delegate

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    
    return arrBulletinResponse.count;
   
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    
    float txtHeight = [Utility getLabelHeight:([[[arrBulletinResponse objectAtIndex:indexPath.row] st_stringForKey:@"comment"] length]>0?[[arrBulletinResponse objectAtIndex:indexPath.row] st_stringForKey:@"comment"]:@"") Width:_tblBulletinResponseList.frame.size.width/3 Font:[UIFont systemFontOfSize:12.0]];
    
    
    
    return 130.0 + txtHeight;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *MyIdentifier = @"BulletinResponseCell";
    
    BulletinResponseCell *cell = (BulletinResponseCell*)[tableView dequeueReusableCellWithIdentifier:MyIdentifier];
    
    
    [Utility loadCellImage:cell.imgViewReplyPerson imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrBulletinResponse objectAtIndex:indexPath.row] st_stringForKey:@"profile_pic"]]];
    cell.lblProfileName.text = [[arrBulletinResponse objectAtIndex:indexPath.row] st_stringForKey:@"name"];
 
    cell.btnLike.tag=indexPath.row;
    
    cell.lblResponseDetails.text = [[arrBulletinResponse objectAtIndex:indexPath.row] st_stringForKey:@"comment"];
    
    cell.lblDateTime.text = [NSString stringWithFormat:@"%@ at %@",[Utility getFormatedForDate:[[arrBulletinResponse objectAtIndex:indexPath.row] st_stringForKey:@"created_at"]],[Utility getFormatedForTime:[[arrBulletinResponse objectAtIndex:indexPath.row] st_stringForKey:@"created_at"]]];
    
    cell.lblResponseCount.text = [NSString stringWithFormat:@"(%@) %@ (%@) %@",[[arrBulletinResponse objectAtIndex:indexPath.row] st_stringForKey:@"respond_reply"],([[[arrBulletinResponse objectAtIndex:indexPath.row] st_stringForKey:@"respond_reply"] integerValue]>1?@"Replies":@"Reply"),[[arrBulletinResponse objectAtIndex:indexPath.row] st_stringForKey:@"respond_like"],([[[arrBulletinResponse objectAtIndex:indexPath.row] st_stringForKey:@"respond_like"] integerValue]>1?@"Likes":@"Like")];
    
    if ([[[arrBulletinResponse objectAtIndex:indexPath.row] st_stringForKey:@"is_like"] isEqualToString:@"yes"]) {
        cell.viewLike.backgroundColor = [Utility getColorFromHexString:@"#7d7d7d"];
        cell.viewLike.userInteractionEnabled = NO;
    }
    else{
        cell.viewLike.backgroundColor = [Utility getColorFromHexString:@"#048BCD"];
        cell.viewLike.userInteractionEnabled = YES;
    }
    
    [cell.btnLike addTarget:self action:@selector(likeInBulletinComment:) forControlEvents:UIControlEventTouchUpInside];
    [cell.btnResponse addTarget:self action:@selector(commentInBulletinComment:) forControlEvents:UIControlEventTouchUpInside];
    [cell.btnAllCommentReply addTarget:self action:@selector(showAllReplyOnBulletinComment:) forControlEvents:UIControlEventTouchUpInside];
    [cell.btnUserDetails addTarget:self action:@selector(showUserProfileDetails:) forControlEvents:UIControlEventTouchUpInside];
    
    cell.imgViewReplyPerson.layer.cornerRadius = cell.imgViewReplyPerson.frame.size.width/2;
    cell.imgViewReplyPerson.layer.masksToBounds = YES;
    
    
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    
    return cell;
    
}

-(void)likeInBulletinComment:(UIButton *)btnLike{
    CGPoint center= btnLike.center;
    CGPoint rootViewPoint = [btnLike.superview convertPoint:center toView:self.tblBulletinResponseList];
    NSIndexPath *indexPath = [self.tblBulletinResponseList indexPathForRowAtPoint:rootViewPoint];
    NSMutableDictionary *httpParams = [NSMutableDictionary dictionaryWithObjectsAndKeys:[Utility getObjectForKey:USER_ID],@"user_id",[[arrBulletinResponse objectAtIndex:indexPath.row] st_stringForKey:@"id"],@"comment_id",@"Yes",@"is_like",nil];
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:HTTPS_BULLETIN_COMMENT_LIKE getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;            
             NSMutableDictionary *dictData = [[arrBulletinResponse objectAtIndex:indexPath.row] mutableCopy];
             [dictData setObject:@"yes" forKey:@"is_like"];
             int like = [[dictData st_stringForKey:@"respond_like"] intValue];
             like = like +1;
             [dictData setObject:[NSString stringWithFormat:@"%d",like] forKey:@"respond_like"];
             [arrBulletinResponse replaceObjectAtIndex:indexPath.row withObject:dictData];
             [self.tblBulletinResponseList reloadData];
             //;
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
    
}
-(void)commentInBulletinComment:(UIButton *)btnLike{
    
    CGPoint center= btnLike.center;
    CGPoint rootViewPoint = [btnLike.superview convertPoint:center toView:self.tblBulletinResponseList];
    NSIndexPath *indexPath = [self.tblBulletinResponseList indexPathForRowAtPoint:rootViewPoint];
    
    NSMutableDictionary *dict = [NSMutableDictionary dictionaryWithObjectsAndKeys:[[arrBulletinResponse objectAtIndex:indexPath.row] st_stringForKey:@"created_at"],@"created_at",[[arrBulletinResponse objectAtIndex:indexPath.row] st_stringForKey:@"comment"],@"content",[[arrBulletinResponse objectAtIndex:indexPath.row] st_stringForKey:@"name"],@"bulletin_title",[dictBulletinClicked st_stringForKey:@"user_id"],@"user_id",[dictBulletinClicked st_stringForKey:@"id"],@"bulletin_id",[[arrBulletinResponse objectAtIndex:indexPath.row] st_stringForKey:@"id"],@"parent_bulletin_id", nil];
    BulletinResponseVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"BulletinResponseVC"];
    vc.dictBulletinData = dict;
    [self.navigationController pushViewController:vc animated:YES];
}

-(void)showAllReplyOnBulletinComment:(UIButton *)btnComment{
    
    CGPoint center= btnComment.center;
    CGPoint rootViewPoint = [btnComment.superview convertPoint:center toView:self.tblBulletinResponseList];
    NSIndexPath *indexPath = [self.tblBulletinResponseList indexPathForRowAtPoint:rootViewPoint];
    
    BulletinCommentDetailsVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"BulletinCommentDetailsVC"];
    vc.dictComment = [arrBulletinResponse objectAtIndex:indexPath.row];
    [self.navigationController pushViewController:vc animated:YES];
    

}

- (IBAction)backToList:(id)sender {
    
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)btnRespondAction:(id)sender {
    NSMutableDictionary *dict = [NSMutableDictionary dictionaryWithObjectsAndKeys:[dictBulletinClicked st_stringForKey:@"created_at"],@"created_at",[dictBulletinClicked st_stringForKey:@"content"],@"content",[dictBulletinClicked st_stringForKey:@"title"],@"bulletin_title",[dictBulletinClicked st_stringForKey:@"user_id"],@"user_id",[dictBulletinClicked st_stringForKey:@"id"],@"bulletin_id",@"0",@"parent_bulletin_id", nil];
    BulletinResponseVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"BulletinResponseVC"];
    vc.dictBulletinData = dict;
    [self.navigationController pushViewController:vc animated:YES];
}

- (IBAction)btnLikeAction:(id)sender {
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setObject:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
    [httpParams setObject:[dictBulletinClicked st_stringForKey:@"id"] forKey:@"bulletin_id"];
    [httpParams setObject:@"Yes" forKey:@"is_like"];
    
    
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:HTTPS_BULLETIN_LIKE getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;

               _lblRepliesCount.text=[NSString stringWithFormat:@"%@ %@ | %@ %@ | %d %@",[dictBulletinClicked st_stringForKey:@"total_view"],([[dictBulletinClicked st_stringForKey:@"total_view"] integerValue]>1?@"Views":@"View"),[dictBulletinClicked st_stringForKey:@"total_reply"],([[dictBulletinClicked st_stringForKey:@"total_reply"] integerValue]>1?@"Replies":@"Reply"),[[dictBulletinClicked st_stringForKey:@"total_like"] intValue]+1,([[dictBulletinClicked st_stringForKey:@"total_like"] integerValue]>1?@"Likes":@"Like")];
                  _viewLike.backgroundColor=[Utility getColorFromHexString:@"#7d7d7d"];
                  _btnLike.userInteractionEnabled=NO;
             
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
}

-(void)showUserProfileDetails:(UIButton *)sender{
    
    CGPoint center= sender.center;
    CGPoint rootViewPoint = [sender.superview convertPoint:center toView:self.tblBulletinResponseList];
    NSIndexPath *indexPath = [self.tblBulletinResponseList indexPathForRowAtPoint:rootViewPoint];
    
    OtherUserProfileVC *other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
    other.userId=[[arrBulletinResponse objectAtIndex:indexPath.row] st_stringForKey:@"user_id"];
    
    
    
    [self.navigationController pushViewController:other animated:YES];

}

- (IBAction)openUserProfileDetails:(id)sender {
    
    
    OtherUserProfileVC *other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
    
    other.userId=[dictBulletinClicked st_stringForKey:@"user_id"];
        // other.strFacebookId=[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:FACEBOOKID];
    
    [self.navigationController pushViewController:other animated:YES];

}
@end
