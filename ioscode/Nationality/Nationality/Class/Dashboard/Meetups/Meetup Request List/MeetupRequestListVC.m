//
//  MeetupRequestListVC.m
//  Nationality
//
//  Created by WTS-MAC3052016 on 02/05/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "MeetupRequestListVC.h"
#import "MeetupRequestListCell.h"
#import "MeetupDetailsVC.h"
#import "OtherUserProfileVC.h"

@interface MeetupRequestListVC ()
{
    NSArray *arrRequestList;
}

@end

@implementation MeetupRequestListVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.tblMeetupRequestList.tableFooterView = [UIView new];
    
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
      [self pushBadgesRemove];
    [self getMeetupRequest];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma --
#pragma mark -getMyMeetups
-(void)getMeetupRequest{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams geturl:[NSString stringWithFormat:@"%@%@",HTTPS_MEETUP_REQUEST_LIST,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             arrRequestList = [dict st_arrayForKey:@"data"];
             [self.tblMeetupRequestList reloadData];
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
}

-(void)pushBadgesRemove{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
    [httpParams setValue:@"meetup_badge" forKey:@"badge_type"];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@",HTTPS_REMOVE_PUSH_BADGES] getServiceDataCallBack:^(NSInteger status, NSObject *data)
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
    
   return arrRequestList.count;
}
- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *MyIdentifier = @"MeetupRequestListCell";
    MeetupRequestListCell *cell = (MeetupRequestListCell*)[tableView dequeueReusableCellWithIdentifier:MyIdentifier];
    cell.lblMeetupName.text = [NSString stringWithFormat:@"%@@%@",[[arrRequestList objectAtIndex:indexPath.row] st_stringForKey:@"title"],[[arrRequestList objectAtIndex:indexPath.row]st_stringForKey:@"place"]];
    cell.lblDate.text = [NSString stringWithFormat:@"%@ at %@",[Utility getFormatedForDate:[[arrRequestList objectAtIndex:indexPath.row] st_stringForKey:@"date_time"]],[Utility getFormatedForTime:[[arrRequestList objectAtIndex:indexPath.row] st_stringForKey:@"date_time"]]];
   // cell.lblTime.text = [Utility getFormatedForTime:[[arrRequestList objectAtIndex:indexPath.row] st_stringForKey:@"date_time"]];
    cell.lblCreatorName.text = [[arrRequestList objectAtIndex:indexPath.row] st_stringForKey:@"name"];
//    cell.lblMeetupLocation.text=[[arrRequestList objectAtIndex:indexPath.row]st_stringForKey:@"location"];
    cell.profileImage.layer.cornerRadius=cell.profileImage.frame.size.height/2;
    cell.profileImage.clipsToBounds=YES;
    [Utility loadCellImage:cell.profileImage imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrRequestList objectAtIndex:indexPath.row] st_stringForKey:@"image"]]];
    cell.butViewAccept.tag = indexPath.row;
    [cell.butViewAccept addTarget:self action:@selector(btnAcceptAction:) forControlEvents:UIControlEventTouchUpInside];
    cell.butViewReject.tag = indexPath.row;
    [cell.butViewReject addTarget:self action:@selector(btnRejectAction:) forControlEvents:UIControlEventTouchUpInside];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    [cell.btnUserDetails addTarget:self action:@selector(openUserProfileDetails:) forControlEvents:UIControlEventTouchUpInside];
    
    return cell;
}



- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    return 148.0 * hRatio;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    MeetupDetailsVC *mtup=[self.storyboard instantiateViewControllerWithIdentifier:@"MeetupDetailsVC"];
      mtup.meetupId=[[arrRequestList objectAtIndex:indexPath.row] st_stringForKey:@"id"];
    mtup.isAccept=NO;
    [self.navigationController pushViewController:mtup animated:YES];
    
    
}

-(void)openUserProfileDetails:(UIButton *)sender{
    
    CGPoint center= sender.center;
    CGPoint rootViewPoint = [sender.superview convertPoint:center toView:self.tblMeetupRequestList];
    NSIndexPath *indexPath = [self.tblMeetupRequestList indexPathForRowAtPoint:rootViewPoint];
    
    OtherUserProfileVC *other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
    
    other.userId=[[arrRequestList objectAtIndex:indexPath.row] st_stringForKey:@"user_id"];
    
    
    
    
    [self.navigationController pushViewController:other animated:YES];
    
}


#pragma -mark btnAcceptAction
-(IBAction)btnAcceptAction:(UIButton*)sender
{
    NSMutableDictionary *httpParams = [NSMutableDictionary new];
    [httpParams setObject:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
    [httpParams setObject:[[arrRequestList objectAtIndex:sender.tag] st_stringForKey:@"id"] forKey:@"meetup_id"];
    [httpParams setObject:@"Yes" forKey:@"is_attend"];
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:HTTPS_MEETUP_ACCEPT_REJECT getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             [self getMeetupRequest];
//             [[AppController sharedappController] showAlert:[[dict st_dictionaryForKey:@"result"] st_stringForKey:@"message"] viewController:self];
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];

}
#pragma -mark btnRejectAction
-(IBAction)btnRejectAction:(UIButton*)sender
{
    NSMutableDictionary *httpParams = [NSMutableDictionary new];
    [httpParams setObject:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
    [httpParams setObject:[[arrRequestList objectAtIndex:sender.tag] st_stringForKey:@"id"] forKey:@"meetup_id"];
    [httpParams setObject:@"No" forKey:@"is_attend"];
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:HTTPS_MEETUP_ACCEPT_REJECT getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
            
             [self getMeetupRequest];
            
             
         }
         
         else
         {
             NSString* str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
}
#pragma -mark btnBackAction
- (IBAction)btnBackAction:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

@end
