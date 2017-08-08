//
//  DashboardVC.m
//  Nationality
//
//  Created by webskitters on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "DashboardVC.h"
#import "ConnectionVC.h"
#import "bulletinCell.h"
#import "meetupCell.h"
#import "friendsCell.h"
#import "UserSettingsVC.h"
#import "ConnectionVC.h"
#import "MeetupsVC.h"
#import "MyMeetupVC.h"
#import "OtherUserProfileVC.h"
#import "AttendeesVC.h"
#import "AllBulletinVC.h"
#import "MeetupDetailsVC.h"
#import "BulletinClickedVC.h"
#import "LocationPopUp.h"
@interface DashboardVC ()

{
    NSMutableArray *arr_section_data;
    NSArray *arr_meetup;
    NSArray *arr_bulletin;
    NSArray *arr_event;
    NSArray *arr_photos;
    CGFloat newHeight;
}

@end

@implementation DashboardVC

- (void)viewDidLoad {
    [super viewDidLoad];
    [self currentLocation];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:YES];
    newHeight=180;
    [self loadDashboardVw];
}

-(void)loadDashboardVw{
    arr_meetup=[NSArray new];
    arr_bulletin=[NSArray new];
    arr_event=[NSArray new];
    [self request_count];
    if ([[Utility getObjectForKey:@"registration"] isEqualToString:@"yes"]) {
        [self showCurrentLocation];
    }
    else{
         [self getDashboardData];
    }
}

-(void)showCurrentLocation{
        UIAlertController * alert=   [UIAlertController
                                      alertControllerWithTitle:@""
                                      message:@"Nationality needs your location to connect you to your local community"
                                      preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction* ok = [UIAlertAction
                             actionWithTitle:@"OK"
                             style:UIAlertActionStyleDefault
                             handler:^(UIAlertAction * action)
                             {
                                 [self updateLocation];
                                 [alert dismissViewControllerAnimated:YES completion:NULL];
                             }];
    
        [alert addAction:ok];
        [self presentViewController:alert animated:YES completion:nil];
}

-(void)updateLocation{
        
        NSMutableDictionary *httpParams=[NSMutableDictionary new];
        if(self.address)
        {
            [httpParams setValue:self.address forKey:@"address"];
        }
    
        else
        {
            [httpParams setValue:@"" forKey:@"address"];
        }
    
        [httpParams setValue:[NSString stringWithFormat:@"%f",self.latitude] forKey:@"latitude"];
        [httpParams setValue:[NSString stringWithFormat:@"%f",self.longitude] forKey:@"longitude"];
    
        
        [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams puturl:[NSString stringWithFormat:@"%@%@",HTTP_USER_LOCATION_UPDATE,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
         {
             
             if(status==0)
             {
                 [Utility saveObjectInUserDefaults:@"no" forKey:@"registration"];
                  [self getDashboardData];
             }
             
             else
             {
                 NSString *str = (NSString *)data;
                 [[AppController sharedappController] showAlert:str viewController:self];
                 
             }
         }];
    

}
-(void)setScrollView{
     self.Vwscroll.contentSize = CGSizeMake(0,CGRectGetMaxY(_collectin_frnds.frame)+CGRectGetHeight(_collectin_frnds.frame));
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#pragma mark -Dashboard Web Service

-(void)getDashboardData{
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    NSLog(@"user id %@",[Utility getObjectForKey:USER_ID]);
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams geturl:[NSString stringWithFormat:@"%@%@",HTTPS_USER_DASHBOARD,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             [self saveData:[dict objectForKeyDict:@"data"]];
              arr_section_data=[[NSMutableArray alloc]initWithObjects:@"LATEST BULLETIN",@"YOUR NEXT MEETUP",@"UPCOMING MEETUP", nil];
             _lbl_Name.text=[NSString stringWithFormat:@"  Welcome back, %@!",[[dict objectForKeyDict:@"data" ]st_stringForKey:FIRST_NAME]];
             arr_meetup=[[dict objectForKeyDict:@"data"] objectForKeyArray:@"your_meetup"];
             arr_bulletin=[[dict objectForKeyDict:@"data"] objectForKeyArray:@"bulletin"];
             arr_event=[[dict objectForKeyDict:@"data"] objectForKeyArray:@"next_meetup"];
             self.tbl_dashboard.delegate=self;
             self.tbl_dashboard.dataSource=self;
             [self.tbl_dashboard reloadData];
//             if (arr_bulletin.count==1) {
//                 newHeight+=120;
//             }
//             if (arr_meetup.count==1) {
//                 newHeight+=120;
//             }
//             if (arr_meetup.count==2) {
//                 newHeight+=120;
//                 
//             }
//             if (arr_event.count==1) {
//                 newHeight+=120;
//             }
//             if (arr_event.count==2) {
//                 newHeight+=120;
//             }

             self.tbl_dashboard.frame=CGRectMake(self.tbl_dashboard.frame.origin.x, self.tbl_dashboard.frame.origin.y, self.tbl_dashboard.frame.size.width, newHeight);
             [self getConnectionList];
             
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
    

}
-(void)saveData:(NSDictionary*)dict{
    [Utility saveObjectInUserDefaults:[dict st_stringForKey:USER_ID] forKey:USER_ID];
    [Utility saveObjectInUserDefaults:[dict st_stringForKey:NATIONALITY_ID] forKey:NATIONALITY_ID];
    [Utility saveObjectInUserDefaults:[dict st_stringForKey:LONGITUDE] forKey:LONGITUDE];
    [Utility saveObjectInUserDefaults:[dict st_stringForKey:HOME_CITY] forKey:HOME_CITY];
    [Utility saveObjectInUserDefaults:[dict st_stringForKey:ADDRESS] forKey:ADDRESS];
    [Utility saveObjectInUserDefaults:[dict st_stringForKey:LATITUDE] forKey:LATITUDE];
    [Utility saveObjectInUserDefaults:[dict st_stringForKey:TAG] forKey:TAG];
    [Utility saveObjectInUserDefaults:[dict st_stringForKey:SHORT_BIO] forKey:SHORT_BIO];
    [Utility saveObjectInUserDefaults:[dict st_stringForKey:LANGUAGE_ID] forKey:LANGUAGE_ID];
    [Utility saveObjectInUserDefaults:[dict st_stringForKey:INTEREST] forKey:INTEREST];
    [Utility saveObjectInUserDefaults:[dict st_stringForKey:FIRST_NAME] forKey:FIRST_NAME];
    [Utility saveObjectInUserDefaults:[dict st_stringForKey:LAST_NAME] forKey:LAST_NAME];
    [Utility saveObjectInUserDefaults:[dict st_stringForKey:@"profile_pic"] forKey:@"profile_pic"];
}
#pragma mark - WebService

-(void)getConnectionList{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    
    [[ServiceRequestHandler sharedRequestHandler] getConnectionListData:httpParams geturl:[NSString stringWithFormat:@"%@%@",HTTPS_CONNECTION_LIST,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             arr_photos=[dict st_arrayForKey:@"data"];
             [_btn_seeall setTitle:[NSString stringWithFormat:@"See All (%lu)",(unsigned long)arr_photos.count] forState:UIControlStateNormal];            
             _btn_seeall.titleLabel.textColor = [UIColor blackColor];
             [self.collectin_frnds reloadData];
             [self setView];
              [self performSelector:@selector(setScrollView) withObject:nil afterDelay:0.0];
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
    
    
}

#pragma mark - UITableViewDelegate
- (UIView *) tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{

    UIView *tempView=[[UIView alloc]initWithFrame:CGRectMake(0,0,_tbl_dashboard.bounds.size.width, 60)];
    tempView.backgroundColor=[Utility getColorFromHexString:@"#00A0B6"];
    UIButton *tempLabel=[[UIButton alloc]initWithFrame:CGRectMake(15,0,_tbl_dashboard.bounds.size.width,30)];
    [tempLabel setTitle:[arr_section_data objectAtIndex:section] forState:UIControlStateNormal];
    [tempLabel setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    tempLabel.titleLabel.font = [UIFont fontWithName:@"OPENSANS-BOLD" size:15.0];
    tempLabel.contentHorizontalAlignment = UIControlContentHorizontalAlignmentLeft;
    tempLabel.tag=section;
    //[tempLabel addTarget:self action:@selector(expandDetails:) forControlEvents:UIControlEventTouchUpInside];
    [tempView addSubview:tempLabel];

    return tempView;
}
//-(void)expandDetails:(UIButton *)sender{
//    if (sender.tag==0) {
//        [self showAllBulletin];
//    }
//    else if (sender.tag==1){
//        [self showNextMeetup];
//    }
//    else{
//        [self showMyMeetup];
//    }
//    
//}
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return arr_section_data.count;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    
         return [arr_section_data objectAtIndex:section];
  
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    if (section==0) {
        if (arr_bulletin.count>1) {
            return 1;
        }
        else
         return arr_bulletin.count;
    }
    else if (section==1){
        if (arr_meetup.count>2) {
            return 2;
        }
        else
        return arr_meetup.count;
        
    }
    else
        if (arr_event.count>2) {
            return 2;
        }
        else
            return arr_event.count;
        
    
   
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
   if (indexPath.section==0) {
         CGFloat cellHeigth;
         NSString *msg =[[arr_bulletin objectAtIndex:indexPath.row] valueForKey:@"content"];
         cellHeigth = [Utility getLabelHeight:msg Width:self.tbl_dashboard.frame.size.width Font:[UIFont systemFontOfSize:15]];
         [[NSUserDefaults standardUserDefaults] setValue:[NSNumber numberWithFloat:newHeight] forKey:@"height"];
         newHeight+=90+cellHeigth;
       
         return 98+cellHeigth;
       
    }
    
  newHeight+=60;
    return 79;
    
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    if (indexPath.section==0) {
        static NSString *simpleTableIdentifier = @"bulletinCell";
        
        bulletinCell* cell =[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
        
        if (cell == nil)
        {
            NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"bulletinCell" owner:self options:nil];
            cell = [nib objectAtIndex:0];
        }
        cell.lbl_title.text=[[arr_bulletin objectAtIndex:indexPath.row] st_stringForKey:@"title"];
        cell.lbl_content.text=[[arr_bulletin objectAtIndex:indexPath.row] st_stringForKey:@"content"];
       
        cell.lbl_date.text=[NSString stringWithFormat:@"%@,%@",[[arr_bulletin objectAtIndex:indexPath.row] st_stringForKey:@"bulletin_creator"],[Utility getFormatedDateForReciptPrint:[[arr_bulletin objectAtIndex:indexPath.row]  st_stringForKey:@"created_at"]]];
        //[cell.img_profile setImageWithURL:[NSURL URLWithString:[[arr_bulletin objectAtIndex:indexPath.row] st_stringForKey:@"bulletin_creator_pic"]]];
        [Utility loadCellImage:cell.img_profile imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arr_bulletin objectAtIndex:indexPath.row] st_stringForKey:@"bulletin_creator_pic"] ]];

        return cell;

    }
    else if (indexPath.section==1){
        static NSString *simpleTableIdentifier = @"meetupCell";
        
        meetupCell* cell =[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
        
        if (cell == nil)
        {
            NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"meetupCell" owner:self options:nil];
            cell = [nib objectAtIndex:0];
        }
        
        cell.lbl_mtup_frnd.text=[NSString stringWithFormat:@"%@ @ %@",[[arr_meetup objectAtIndex:indexPath.row] st_stringForKey:@"title"],[[arr_meetup objectAtIndex:indexPath.row] st_stringForKey:@"place"]];
        NSString *update_dt=[[arr_meetup objectAtIndex:indexPath.row]st_stringForKey:@"date_time"];
        cell.lbl_date.text= [Utility getFormatedForDate:update_dt];
        cell.lbl_time.text=[Utility getFormatedForTime:update_dt];
       // [cell.img_profile setImageWithURL:[NSURL URLWithString:[[arr_meetup objectAtIndex:indexPath.row] st_stringForKey:@"meetup_creator_pic"]]];
        [Utility loadCellImage:cell.img_profile imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arr_meetup objectAtIndex:indexPath.row] st_stringForKey:@"meetup_creator_pic"] ]];
        cell.lbl_no_of_ppl.text=[[arr_meetup objectAtIndex:indexPath.row] st_stringForKey:@"people_add"];
         cell.lbl_added_ppl.text=[[[arr_meetup objectAtIndex:indexPath.row] st_stringForKey:@"people_add"] integerValue]>1?@"People are added":@"Person is added";
        return cell;
    }
    static NSString *simpleTableIdentifier = @"meetupCell";
    
    meetupCell* cell =[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if (cell == nil)
    {
        NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"meetupCell" owner:self options:nil];
        cell = [nib objectAtIndex:0];
    }
    cell.lbl_mtup_frnd.text=[NSString stringWithFormat:@"%@ @ %@",[[arr_event objectAtIndex:indexPath.row] st_stringForKey:@"title"],[[arr_event objectAtIndex:indexPath.row] st_stringForKey:@"place"]];
    NSString *update_dt=[[arr_event objectAtIndex:indexPath.row]st_stringForKey:@"date_time"];
    cell.lbl_date.text= [Utility getFormatedForDate:update_dt];
    cell.lbl_time.text=[Utility getFormatedForTime:update_dt];
    //[cell.img_profile setImageWithURL:[NSURL URLWithString:[[arr_event objectAtIndex:indexPath.row] st_stringForKey:@"meetup_creator_pic"]]];
    [Utility loadCellImage:cell.img_profile imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arr_event objectAtIndex:indexPath.row] st_stringForKey:@"meetup_creator_pic"] ]];
    cell.lbl_no_of_ppl.text=[[arr_event objectAtIndex:indexPath.row] st_stringForKey:@"people_add"];
    
    cell.lbl_added_ppl.text=[[[arr_event objectAtIndex:indexPath.row] st_stringForKey:@"people_add"] integerValue]>1?@"People are added":@"Person is added";
    return cell;
   
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.section==0) {
        BulletinClickedVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"BulletinClickedVC"];
      
            vc.strBulletinId = [[arr_bulletin objectAtIndex:indexPath.row] st_stringForKey:@"id"];
        [self.navigationController pushViewController:vc animated:YES];
    }
    else if (indexPath.section==1){
        MeetupDetailsVC * meetDetails=[self.storyboard instantiateViewControllerWithIdentifier:@"MeetupDetailsVC"];
        meetDetails.meetupId=[[arr_meetup objectAtIndex:indexPath.row] st_stringForKey:@"id"];
        meetDetails.isAccept=YES;
        
        if ([[[arr_meetup objectAtIndex:indexPath.row] st_stringForKey:@"type"]isEqualToString:@"public"]) {
            meetDetails.isPublic=YES;
        }
        else{
            meetDetails.isPublic=NO;
        }

        [self.navigationController pushViewController:meetDetails animated:YES];
    }
    else{
        MeetupDetailsVC * meetDetails=[self.storyboard instantiateViewControllerWithIdentifier:@"MeetupDetailsVC"];
        meetDetails.meetupId=[[arr_event objectAtIndex:indexPath.row] st_stringForKey:@"id"];
        meetDetails.isAccept=YES;
       
  
        if ([[[arr_event objectAtIndex:indexPath.row] st_stringForKey:@"type"]isEqualToString:@"public"]) {
            meetDetails.isPublic=YES;
        }
        else{
            meetDetails.isPublic=NO;
        }
         [self.navigationController pushViewController:meetDetails animated:YES];

    }

    
}
#pragma mark - SHOW ALL BuLLETIN

-(void)showAllBulletin{

    self.tabBarController.selectedIndex=3;
 

}
-(void)showNextMeetup{
 
    self.tabBarController.selectedIndex=1;

    
}
-(void)showMyMeetup{
    MyMeetupVC *mymt=[self.storyboard instantiateViewControllerWithIdentifier:@"MyMeetupVC"];
    
    [self.navigationController pushViewController:mymt animated:YES];
}


#pragma mark - SET UIVIEW
-(void)setView{
  
    _btn_connection.frame=CGRectMake(_btn_connection.frame.origin.x, CGRectGetMaxY(_tbl_dashboard.frame)+5, _btn_connection.frame.size.width,  _btn_connection.frame.size.height);
    _btn_seeall.frame=CGRectMake(_btn_seeall.frame.origin.x, _btn_connection.frame.origin.y, _btn_seeall.frame.size.width,  _btn_seeall.frame.size.height);
    _collectin_frnds.frame=CGRectMake(_collectin_frnds.frame.origin.x, CGRectGetMaxY(_btn_connection.frame)+5, _collectin_frnds.frame.size.width,  _collectin_frnds.frame.size.height);

}
#pragma mark - UICollectionView
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return arr_photos.count;
}


- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    friendsCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"friendsCell" forIndexPath:indexPath];    
    [Utility loadCellImage:cell.img_frnds imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arr_photos objectAtIndex:indexPath.row] st_stringForKey:@"profile_pic"]]];
    cell.lbl_name.text=[NSString stringWithFormat:@"%@ %@",[[arr_photos objectAtIndex:indexPath.row] st_stringForKey:@"first_name"],[[arr_photos objectAtIndex:indexPath.row] st_stringForKey:@"last_name"]];
    
    cell.img_frnds.layer.cornerRadius=cell.img_frnds.frame.size.height/2;
    cell.img_frnds.layer.borderWidth=1.0;
    cell.img_frnds.layer.borderColor=[Utility getColorFromHexString:@"#9BC531"].CGColor;
    cell.img_frnds.clipsToBounds=YES;
    
    return cell;
}
- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    OtherUserProfileVC *other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
    other.userId=[[arr_photos objectAtIndex:indexPath.row]st_stringForKey:@"id"];
    [self.navigationController pushViewController:other animated:YES];
}

#pragma mark - Go To Connection Page

-(IBAction)seeAll:(id)sender{
    
    AttendeesVC *attendees=[self.storyboard instantiateViewControllerWithIdentifier:@"AttendeesVC"];
    attendees.arr_attendees_list=[arr_photos mutableCopy];
    attendees.header_status=@"CONNECTED TO";
    [self.navigationController pushViewController:attendees animated:YES];
    
}
#pragma mark - UITabBar
-(void)tabBarDesign{
    self.tabBarController.tabBar.barTintColor=[Utility getColorFromHexString:@"#00A0B6"];
    
    [[UITabBarItem appearance] setTitleTextAttributes:@{NSFontAttributeName : [UIFont fontWithName:@"HelveticaNeue-Bold" size:10.0f],
                                                        NSForegroundColorAttributeName : [UIColor whiteColor]
                                                        }
                                             forState:UIControlStateNormal];
    
    
    UITabBar *tabBar = self.tabBarController.tabBar;
    UITabBarItem *item0 = [tabBar.items objectAtIndex:0];
    UITabBarItem *item1 = [tabBar.items objectAtIndex:1];
    UITabBarItem *item2 = [tabBar.items objectAtIndex:2];
    UITabBarItem *item3 = [tabBar.items objectAtIndex:3];
    UITabBarItem *item4 = [tabBar.items objectAtIndex:4];
    
    item0.selectedImage = [[UIImage imageNamed:@"connection-select"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    item0.image = [[UIImage imageNamed:@"connection"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    item1.selectedImage = [[UIImage imageNamed:@"Meetups-selected"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    item1.image = [[UIImage imageNamed:@"Meetups"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    item2.selectedImage = [[UIImage imageNamed:@"Messages-selected"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    item2.image = [[UIImage imageNamed:@"Messages"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    item3.selectedImage = [[UIImage imageNamed:@"Bulletins-selected"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    item3.image = [[UIImage imageNamed:@"Bulletins"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    
    item4.selectedImage = [[UIImage imageNamed:@"Me-selected"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    item4.image = [[UIImage imageNamed:@"Me"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal ];
    
}

#pragma mark - Go To Settings Page

-(IBAction)goToSettings:(id)sender{
    UserSettingsVC *settings=[self.storyboard instantiateViewControllerWithIdentifier:@"UserSettingsVC"];
    [self.navigationController pushViewController:settings animated:YES];
}
//#pragma mark - Current Location
//-(void)currentLocation{
//    
//    
//    self.locationManager = [[CLLocationManager alloc] init];
//    [self.locationManager requestWhenInUseAuthorization];
//    
//    
//    self.locationManager.delegate = self;
//    // self.locationManager.desiredAccuracy = kCLLocationAccuracyBest;
//    
//    [self.locationManager startUpdatingLocation];
//}
//- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error
//{
//    NSLog(@"didFailWithError: %@", error);
//    UIAlertView *errorAlert = [[UIAlertView alloc]
//                               initWithTitle:@"Error" message:@"Failed to Get Your Location" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
//    [errorAlert show];
//}
//
//- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation
//{
//    NSLog(@"didUpdateToLocation: %@", newLocation);
//    CLLocation *currentLocation = newLocation;
//    
//    if (currentLocation != nil) {
//        _latitude =  currentLocation.coordinate.latitude;
//        _longitude = currentLocation.coordinate.longitude;
//       
//    }
//    
//}

@end
