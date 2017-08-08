//
//  BlockedUserListVC.m
//  Nationality
//
//  Created by WTS-MAC3052016 on 03/05/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "BlockedUserListVC.h"
#import "BlockedUserListCell.h"

@interface BlockedUserListVC ()
{
    NSArray *arrBlockedUser;
}

@end

@implementation BlockedUserListVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.tblBlockedUser.tableFooterView = [UIView new];
    [self getUnblockedUser];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - WebService
-(void)getUnblockedUser{
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams geturl:[NSString stringWithFormat:@"%@%@",HTTP_BLOCKED_USER_LIST,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             arrBlockedUser=[dict st_arrayForKey:@"data"];
             [self.tblBlockedUser reloadData];
         }
         
         else
         {
             NSString *str = (NSString *)data;
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
    
        return arrBlockedUser.count;
}
- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *MyIdentifier = @"BlockedUserListCell";
    
    BlockedUserListCell *cell = (BlockedUserListCell*)[tableView dequeueReusableCellWithIdentifier:MyIdentifier];
            cell.butViewUnBlock.tag = indexPath.row;
        [cell.butViewUnBlock addTarget:self action:@selector(btnUnBlockAction:) forControlEvents:UIControlEventTouchUpInside];
        
        cell.profileImage.layer.cornerRadius=cell.profileImage.frame.size.height/2;
        cell.profileImage.clipsToBounds=YES;
        [Utility loadCellImage:cell.profileImage imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrBlockedUser objectAtIndex:indexPath.row] st_stringForKey:@"block_user_image"]]];
        
        cell.lblName.text = [[arrBlockedUser objectAtIndex:indexPath.row] st_stringForKey:@"block_user_name"];
        //cell.lblCity.text = [[arrBlockedUser objectAtIndex:indexPath.row] st_stringForKey:@"home_city"];
        
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    return 78.0 * hRatio;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
}

-(IBAction)btnUnBlockAction:(UIButton*)sender
{
    NSMutableDictionary *httpParams = [NSMutableDictionary new];
    [httpParams setObject:[Utility getObjectForKey:USER_ID] forKey:@"from_userid"];
    [httpParams setObject:[[arrBlockedUser objectAtIndex:sender.tag] st_stringForKey:@"to_userid"] forKey:@"to_userid"];
    
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:HTTP_UNBLOCKED_USER getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             [self getUnblockedUser];

             // [[AppController sharedappController] showAlert:@"Meet" viewController:@""];
         }
         
         else
         {
             
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
             
         }
     }];
    
}


- (IBAction)btnBackAction:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}
@end
