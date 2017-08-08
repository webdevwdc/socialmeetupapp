//
//  InviteFacebookFriendsVC.m
//  Nationality
//
//  Created by webskitters on 04/05/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "InviteFacebookFriendsVC.h"
#import "FriendListCell.h"

@interface InviteFacebookFriendsVC ()

@end

@implementation InviteFacebookFriendsVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _tblFacebookFriendList.tableFooterView = [UIView new];
    
    _arrFriendList = [Utility getObjectForKey:@"User_FB_Friend_List"];
    [_tblFacebookFriendList reloadData];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma mark - UITableviewDatasourceand Delegate

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return _arrFriendList.count;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *MyIdentifier = @"FriendListCell";
    
    FriendListCell *cell = (FriendListCell*)[tableView dequeueReusableCellWithIdentifier:MyIdentifier];
    
    cell.lblFriendName.text = [[_arrFriendList objectAtIndex:indexPath.row] valueForKey:@"name"];
    
    NSString *str = [NSString stringWithFormat:@"https://graph.facebook.com/%@/picture?type=large",[[_arrFriendList objectAtIndex:indexPath.row] valueForKey:@"id"]];
    NSData *imageData = [NSData dataWithContentsOfURL:[NSURL URLWithString:str]];
    UIImage *profilePic = [UIImage imageWithData:imageData];
    cell.FriendImageView.image = profilePic;
    
    [cell.btnInviteFriend addTarget:self action:@selector(inviteFriendMethod:) forControlEvents:UIControlEventTouchUpInside];
    
    return cell;
    
}

#pragma mark - Button Action

-(void)inviteFriendMethod:(UIButton*)btn
{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setObject:[[_arrFriendList objectAtIndex:btn.tag] valueForKey:@"id"] forKey:@"facebookId"];
    [httpParams setObject:[Utility getObjectForKey:USER_ID] forKey:@"from_userid"];
    
    
    [self inviteFBFriendServicxe:httpParams];
}

-(void)inviteFBFriendServicxe:(NSMutableDictionary *)dictParam{
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:dictParam posturl:[NSString stringWithFormat:@"%@",HTTPS_USER_INVITE_FBFRIEND] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         if(status==0)
         {
             NSDictionary *dict=(NSDictionary*)data;
             
             [[AppController sharedappController] showAlert:[dict st_stringForKey:@"message"] viewController:self];
             
             
             
             
             
         }
         else
         {
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
         
     }];
    
}

- (IBAction)btnBackAction:(id)sender {
    
     [self.navigationController popViewControllerAnimated:YES];
    
}
@end
