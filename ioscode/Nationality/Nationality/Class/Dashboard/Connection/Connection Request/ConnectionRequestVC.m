//
//  ConnectionRequestVC.m
//  Nationality
//
//  Created by WTS-MAC3052016 on 07/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "ConnectionRequestVC.h"
#import "ConnectionRequestCell.h"
#import "OtherUserProfileVC.h"
@interface ConnectionRequestVC ()
{
    NSArray *arrRequestList;
}

@end

@implementation ConnectionRequestVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.tblConnectionList.tableFooterView=[UIView new];
        // Do any additional setup after loading the view.
    [self getRequestConnectionList];
}

-(void)viewWillAppear:(BOOL)animated
{
    
    [super viewWillAppear:YES];
    [self pushBadgesRemove];

    
}

-(void)pushBadgesRemove{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
    [httpParams setValue:@"connection_badge" forKey:@"badge_type"];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@",HTTPS_REMOVE_PUSH_BADGES] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
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
#pragma mark -getRequestConnectionList
-(void)getRequestConnectionList{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams geturl:[NSString stringWithFormat:@"%@%@",HTTPS_FRIEND_REQUEST_LIST,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
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
            // [[AppController sharedappController] showAlert:str viewController:self];
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
    static NSString *MyIdentifier = @"ConnectionRequestCell";
    ConnectionRequestCell *cell = (ConnectionRequestCell*)[tableView dequeueReusableCellWithIdentifier:MyIdentifier];
       [Utility loadCellImage:cell.profileImage imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrRequestList objectAtIndex:indexPath.row] st_stringForKey:@"profile_pic"]]];
       cell.profileImage.layer.cornerRadius=cell.profileImage.frame.size.height/2;
       cell.profileImage.clipsToBounds=YES;
    cell.lblName.text = [[arrRequestList objectAtIndex:indexPath.row] st_stringForKey:@"name"];
    cell.lblCity.text = [[arrRequestList objectAtIndex:indexPath.row] st_stringForKey:@"home_city"];
    cell.butViewAccept.tag = indexPath.row;
    cell.butViewReject.tag = indexPath.row;
    [cell.butViewAccept addTarget:self action:@selector(btnAcceptAction:) forControlEvents:UIControlEventTouchUpInside];
    [cell.butViewReject addTarget:self action:@selector(btnRejectAction:) forControlEvents:UIControlEventTouchUpInside];
    [cell.butViewBlock addTarget:self action:@selector(btnBlockAction:) forControlEvents:UIControlEventTouchUpInside];
       
     cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    return cell;
}



- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    return 115.0 * hRatio;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    OtherUserProfileVC *other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
    other.userId=[[arrRequestList objectAtIndex:indexPath.row]st_stringForKey:@"from_userid"];
    [self.navigationController pushViewController:other animated:YES];
    
}

-(void)btnAcceptAction : (UIButton*)btn
{
    NSMutableDictionary *httpParams = [NSMutableDictionary new];
    [httpParams setObject:[Utility getObjectForKey:USER_ID] forKey:@"to_userid"];
     [httpParams setObject:[[arrRequestList objectAtIndex:btn.tag] st_stringForKey:@"from_userid"] forKey:@"from_userid"];
    [httpParams setObject:@"Accept" forKey:@"is_accept"];
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:HTTPS_FRIEND_REQUEST_ACCEPT_REJECT getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             [self getRequestConnectionList];
             //[self.navigationController popViewControllerAnimated:YES];
             
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];

}

-(void)btnRejectAction : (UIButton*)btn
{
    NSMutableDictionary *httpParams = [NSMutableDictionary new];
    [httpParams setObject:[Utility getObjectForKey:USER_ID] forKey:@"to_userid"];
    [httpParams setObject:[[arrRequestList objectAtIndex:btn.tag] st_stringForKey:@"from_userid"] forKey:@"from_userid"];
    [httpParams setObject:@"Reject" forKey:@"is_accept"];
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:HTTPS_FRIEND_REQUEST_ACCEPT_REJECT getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             
             [self getRequestConnectionList];
            // [self.navigationController popViewControllerAnimated:YES];
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];

}

-(void)btnBlockAction : (UIButton*)btn
{
    
}


#pragma mark - Asynchronous Image Loading Method

//- (void)loadCellImage:(UIImageView *)imageView imageUrl:(NSString *)imageURL
//{
//    if (imageURL) {
//        [[imageView viewWithTag:99] removeFromSuperview];
//        
//        __block UIActivityIndicatorView *activityIndicator;
//        __weak UIImageView *weakImageView = imageView;
//        [imageView sd_setImageWithURL:[NSURL URLWithString:imageURL]
//                     placeholderImage:nil
//                              options:SDWebImageProgressiveDownload
//                             progress:^(NSInteger receivedSize, NSInteger expectedSize) {
//                                 if (!activityIndicator) {
//                                     [weakImageView addSubview:activityIndicator = [UIActivityIndicatorView.alloc initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhite]];
//                                     activityIndicator.tag = 99;
//                                     activityIndicator.center = weakImageView.center;
//                                     [activityIndicator startAnimating];
//                                 }
//                             }
//                            completed:^(UIImage *image, NSError *error, SDImageCacheType cacheType, NSURL *imageURL) {
//                                [activityIndicator removeFromSuperview];
//                                activityIndicator = nil;
//                            }];
//    }
//}
//
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction)backConnection:(id)sender{
    [self.navigationController popViewControllerAnimated:YES];
}

@end
