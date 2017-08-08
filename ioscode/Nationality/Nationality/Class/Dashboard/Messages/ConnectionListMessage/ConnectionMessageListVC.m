//
//  ConnectionMessageListVC.m
//  Nationality
//
//  Created by webskitters on 28/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "ConnectionMessageListVC.h"
#import "CustomConnectionCell.h"
#import "MessagesVC.h"
#import "OtherUserProfileVC.h"

@interface ConnectionMessageListVC ()

@end

@implementation ConnectionMessageListVC{
    
    NSArray *arrRequestList;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    _tblConnectionList.tableFooterView=[UIView new];
    [self getRequestConnectionList];
}
-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    //[]
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma --
#pragma mark -getRequestConnectionList
-(void)getRequestConnectionList{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams geturl:[NSString stringWithFormat:@"%@%@",HTTPS_CONNECTION_LIST,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             arrRequestList=[dict st_arrayForKey:@"data"];
             [self.tblConnectionList reloadData];
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
    
    return arrRequestList.count;
}
- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *MyIdentifier = @"CustomConnectionCell";
    
    CustomConnectionCell *cell = (CustomConnectionCell*)[tableView dequeueReusableCellWithIdentifier:MyIdentifier];
    cell.lblUserName.text = [NSString stringWithFormat:@"%@ %@",[[arrRequestList objectAtIndex:indexPath.row] st_stringForKey:@"first_name"],[[arrRequestList objectAtIndex:indexPath.row] st_stringForKey:@"last_name"]];
    cell.imgViewProfile.layer.cornerRadius=cell.imgViewProfile.frame.size.height/2;
    cell.imgViewProfile.clipsToBounds=YES;
    [Utility loadCellImage:cell.imgViewProfile imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrRequestList objectAtIndex:indexPath.row] st_stringForKey:@"profile_pic"]]];
    cell.lblUserDesc.text = [[arrRequestList objectAtIndex:indexPath.row] st_stringForKey:@"home_city"];
    
    [cell.btnUserDetails addTarget:self action:@selector(openUserProfileDetails:) forControlEvents:UIControlEventTouchUpInside];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    return cell;
}



- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    return 80.0 * hRatio;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    MessagesVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"MessagesVC"];
     vc.strReceiverId = [[arrRequestList objectAtIndex:indexPath.row] st_stringForKey:FACEBOOKID];
    vc.strId = [[arrRequestList objectAtIndex:indexPath.row] st_stringForKey:USER_ID];
    [self.navigationController pushViewController:vc animated:YES];
}

-(void)openUserProfileDetails:(UIButton *)sender{
    
    CGPoint center= sender.center;
    CGPoint rootViewPoint = [sender.superview convertPoint:center toView:self.tblConnectionList];
    NSIndexPath *indexPath = [self.tblConnectionList indexPathForRowAtPoint:rootViewPoint];
    
    OtherUserProfileVC *other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
    
    other.userId=[[arrRequestList objectAtIndex:indexPath.row] st_stringForKey:@"id"];
        // other.strFacebookId=[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:FACEBOOKID];
    
    
    
    [self.navigationController pushViewController:other animated:YES];
    
}

- (IBAction)backToPrev:(id)sender {
    
    [self.navigationController popViewControllerAnimated:YES];
}
@end
