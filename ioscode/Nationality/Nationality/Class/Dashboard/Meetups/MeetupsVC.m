//
//  MeetupsVC.m
//  Nationality
//
//  Created by webskitters on 04/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "MeetupsVC.h"
#import "MeetupsCell.h"
#import "AddMeetupVC.h"
#import "MeetupDetailsVC.h"
#import "MyMeetupVC.h"
#import "MeetupRequestListVC.h"
#import "OtherUserProfileVC.h"
@interface MeetupsVC (){
    
    NSMutableArray *filteredContentList;
    BOOL isSearching;
    
}

@property (strong, nonatomic) NSArray *arrMeetupsList;

@end

@implementation MeetupsVC

@synthesize meetupSearchBar,arrMeetupsList;

- (void)viewDidLoad {
    [super viewDidLoad];
    
}
-(void)loadMeetupVw{    
    isSearching = NO;
    filteredContentList = [[NSMutableArray alloc]init];
    self.lblMeetupRequest.layer.cornerRadius =self.lblMeetupRequest.frame.size.height/2;
    self.lblMeetupRequest.layer.masksToBounds = YES;
    arrMeetupsList = [[NSMutableArray alloc] init];
    [self getAllMeetup];
    
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:YES];
     [self loadMeetupVw];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(loadMeetupVw) name:@"meetupinviteuser" object:nil];
    
}


-(void)viewWillDisappear:(BOOL)animated
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:@"meetupinviteuser" object:nil];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma mark - Webservice 
-(void)getAllMeetup{
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams geturl:[NSString stringWithFormat:@"%@%@",HTTPS_ALL_MEETUP,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         if(status==0)
         {
             
             NSDictionary *  dict = (NSDictionary*)data;
             arrMeetupsList=[dict st_arrayForKey:@"data"];
             if (arrMeetupsList.count==0) {
                 [[AppController sharedappController]showAlert:@"No Meetups Found" viewController:self];
             }
             if([[dict st_stringForKey:@"meetup_request"] integerValue] > 0)
             {
                 self.lblMeetupRequest.hidden =NO;
                 self.lblMeetupRequest.text = [dict st_stringForKey:@"meetup_request"];
             }
             else{
                  self.lblMeetupRequest.hidden =YES;
             }
             
                  [_tblMeetups reloadData];
             
         }
         else
         {
             
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
             
         }
     }];

}

#pragma mark - TableView Dtasource and Delegate

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
  
    if (isSearching) {
        return filteredContentList.count;
    }
    else {
        return arrMeetupsList.count;
    }

}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *MyIdentifier = @"MeetupsListCell";
    
    MeetupsCell *cell = (MeetupsCell*)[tableView dequeueReusableCellWithIdentifier:MyIdentifier];
    
    if (isSearching) {
        cell.lblMeetupsName.text = [NSString stringWithFormat:@"%@@%@",[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"title"],[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"place"]];
        if ([[Utility getObjectForKey:USER_ID] isEqualToString:[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"user_id"]] || [[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"is_like"] isEqualToString:@"yes"]) {
            
            cell.likeVw.backgroundColor=[Utility getColorFromHexString:@"#7d7d7d"];
            cell.btnLike.userInteractionEnabled = NO;
        }
        else{
          
             cell.likeVw.backgroundColor=[Utility getColorFromHexString:@"#9BC531"];
            cell.btnLike.userInteractionEnabled = YES;
        }
        if ([[filteredContentList objectAtIndex:indexPath.row] st_arrayForKey:@"attendee"].count>3) {
            cell.lblMeetupsNum.text=[NSString stringWithFormat:@"and %lu others",[[filteredContentList objectAtIndex:indexPath.row] st_arrayForKey:@"attendee"].count-3];
        }
        else{
            cell.lblMeetupsNum.text=@"";
        }
        cell.lblProfileName.text=[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"meetup_creator"];
        cell.lblMeetupDate.text=[Utility getFormatedForDate:[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"date_time"]];
        cell.lblMeetupTime.text=[Utility getFormatedForTime:[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"date_time"]];
        cell.imgViewProfile.layer.cornerRadius = cell.imgViewProfile.frame.size.height/2;
  
        cell.imgViewProfile.layer.masksToBounds = YES;
        [Utility loadCellImage:cell.imgViewProfile imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"meetup_creator_pic"] ]];

        cell.btnLike.tag=indexPath.row;
        if ([[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"is_like"] isEqualToString:@"yes"]) {
            
            cell.btnLike.userInteractionEnabled=NO;
        }
        else{
            cell.btnLike.userInteractionEnabled=YES;
            [cell.btnLike addTarget:self action:@selector(pressLike:) forControlEvents:UIControlEventTouchUpInside];
        }
        cell.lblNumberofLike.text=[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"total_like"];
        
        
        cell.meetupCandidateImg1.layer.cornerRadius = cell.meetupCandidateImg1.frame.size.height/2;
        cell.meetupCandidateImg2.layer.cornerRadius =  cell.meetupCandidateImg2.frame.size.height/2;
        cell.meetupCandidateImg3.layer.cornerRadius =  cell.meetupCandidateImg3.frame.size.height/2;
        cell.meetupCandidateImg1.layer.masksToBounds = YES;
        cell.meetupCandidateImg2.layer.masksToBounds = YES;
        cell.meetupCandidateImg3.layer.masksToBounds = YES;
        NSArray *arr_image=[[filteredContentList objectAtIndex:indexPath.row] st_arrayForKey:@"attendee"];
        if (arr_image.count==1) {
            cell.meetupCandidateImg2.hidden=YES;
            cell.meetupCandidateImg3.hidden=YES;
            cell.meetupCandidateImg1.hidden=NO;

            [Utility loadCellImage:cell.meetupCandidateImg1 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arr_image objectAtIndex:0] st_stringForKey:@"attendee_picture"] ]];
            
        }
        if (arr_image.count==2) {
            cell.meetupCandidateImg2.hidden=NO;
            cell.meetupCandidateImg3.hidden=YES;
            cell.meetupCandidateImg1.hidden=NO;

            [Utility loadCellImage:cell.meetupCandidateImg1 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arr_image objectAtIndex:0] st_stringForKey:@"attendee_picture"] ]];
            
            [Utility loadCellImage:cell.meetupCandidateImg2 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arr_image objectAtIndex:1] st_stringForKey:@"attendee_picture"] ]];
           cell.meetupCandidateImg3.hidden=YES;
        }
        if (arr_image.count==3 || arr_image.count>3) {
            cell.meetupCandidateImg2.hidden=NO;
            cell.meetupCandidateImg3.hidden=NO;
            cell.meetupCandidateImg1.hidden=NO;

            [Utility loadCellImage:cell.meetupCandidateImg1 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arr_image objectAtIndex:0] st_stringForKey:@"attendee_picture"] ]];
            
            [Utility loadCellImage:cell.meetupCandidateImg2 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arr_image objectAtIndex:1] st_stringForKey:@"attendee_picture"] ]];
            [Utility loadCellImage:cell.meetupCandidateImg3 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arr_image objectAtIndex:2] st_stringForKey:@"attendee_picture"] ]];
           
        }

    }
    else {
        cell.lblMeetupsName.text = [NSString stringWithFormat:@"%@@%@",[[arrMeetupsList objectAtIndex:indexPath.row] st_stringForKey:@"title"],[[arrMeetupsList objectAtIndex:indexPath.row] st_stringForKey:@"place"]];
        if ([[Utility getObjectForKey:USER_ID] isEqualToString:[[arrMeetupsList objectAtIndex:indexPath.row] st_stringForKey:@"user_id"]] || [[[arrMeetupsList objectAtIndex:indexPath.row] st_stringForKey:@"is_like"] isEqualToString:@"yes"]) {
            
            cell.likeVw.backgroundColor=[Utility getColorFromHexString:@"#7d7d7d"];
            cell.btnLike.userInteractionEnabled = NO;
        }
        else{
            cell.likeVw.backgroundColor=[Utility getColorFromHexString:@"#9BC531"];
            cell.btnLike.userInteractionEnabled = YES;
        }
        if ([[arrMeetupsList objectAtIndex:indexPath.row] st_arrayForKey:@"attendee"].count>3) {
            cell.lblMeetupsNum.text=[NSString stringWithFormat:@"and %lu others",[[arrMeetupsList objectAtIndex:indexPath.row] st_arrayForKey:@"attendee"].count-3];
        }
        else{
            cell.lblMeetupsNum.text=@"";
        }
        cell.lblProfileName.text=[[arrMeetupsList objectAtIndex:indexPath.row] st_stringForKey:@"meetup_creator"];
        cell.lblMeetupDate.text=[Utility getFormatedForDate:[[arrMeetupsList objectAtIndex:indexPath.row] st_stringForKey:@"date_time"]];
        cell.lblMeetupTime.text=[Utility getFormatedForTime:[[arrMeetupsList objectAtIndex:indexPath.row] st_stringForKey:@"date_time"]];
        cell.imgViewProfile.layer.cornerRadius = cell.imgViewProfile.frame.size.height/2;
        cell.imgViewProfile.layer.masksToBounds = YES;
        [Utility loadCellImage:cell.imgViewProfile imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrMeetupsList objectAtIndex:indexPath.row] st_stringForKey:@"meetup_creator_pic"] ]];
 
        cell.btnLike.tag=indexPath.row;
        if ([[[arrMeetupsList objectAtIndex:indexPath.row] st_stringForKey:@"is_like"] isEqualToString:@"yes"]) {
            
            cell.btnLike.userInteractionEnabled=NO;
        }
        else{
            cell.btnLike.userInteractionEnabled=YES;
        }
        cell.lblNumberofLike.text=[[arrMeetupsList objectAtIndex:indexPath.row] st_stringForKey:@"total_like"];
        [cell.btnLike addTarget:self action:@selector(pressLike:) forControlEvents:UIControlEventTouchUpInside];
        cell.meetupCandidateImg1.layer.cornerRadius = cell.meetupCandidateImg1.frame.size.height/2;
        cell.meetupCandidateImg2.layer.cornerRadius =  cell.meetupCandidateImg2.frame.size.height/2;
        cell.meetupCandidateImg3.layer.cornerRadius =  cell.meetupCandidateImg3.frame.size.height/2;
        cell.meetupCandidateImg1.layer.masksToBounds = YES;
        cell.meetupCandidateImg2.layer.masksToBounds = YES;
        cell.meetupCandidateImg3.layer.masksToBounds = YES;
        NSArray *arr_image=[[arrMeetupsList objectAtIndex:indexPath.row] st_arrayForKey:@"attendee"];
        if (arr_image.count==1) {
          
            cell.meetupCandidateImg2.hidden=YES;
            cell.meetupCandidateImg3.hidden=YES;
            cell.meetupCandidateImg1.hidden=NO;
            [Utility loadCellImage:cell.meetupCandidateImg1 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arr_image objectAtIndex:0] st_stringForKey:@"attendee_picture"] ]];
            
        }
        if (arr_image.count==2) {
            
            cell.meetupCandidateImg3.hidden=YES;
            cell.meetupCandidateImg1.hidden=NO;
            cell.meetupCandidateImg2.hidden=NO;
            [Utility loadCellImage:cell.meetupCandidateImg1 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arr_image objectAtIndex:0] st_stringForKey:@"attendee_picture"] ]];
            
            [Utility loadCellImage:cell.meetupCandidateImg2 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arr_image objectAtIndex:1] st_stringForKey:@"attendee_picture"] ]];
        }
        if (arr_image.count==3 || arr_image.count>3) {
            
            cell.meetupCandidateImg2.hidden=NO;
            cell.meetupCandidateImg3.hidden=NO;
            cell.meetupCandidateImg1.hidden=NO;
            [Utility loadCellImage:cell.meetupCandidateImg1 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arr_image objectAtIndex:0] st_stringForKey:@"attendee_picture"] ]];
            [Utility loadCellImage:cell.meetupCandidateImg2 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arr_image objectAtIndex:1] st_stringForKey:@"attendee_picture"] ]];
            [Utility loadCellImage:cell.meetupCandidateImg3 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arr_image objectAtIndex:2] st_stringForKey:@"attendee_picture"] ]];
            
        }

    }
       cell.selectionStyle = UITableViewCellSelectionStyleNone;
    [cell.btnUserDetails addTarget:self action:@selector(openUserProfileDetails:) forControlEvents:UIControlEventTouchUpInside];
    
    return cell;
    
}
- (void)handleImageTap:(UIImageView *)gestureRecognizer {
     NSIndexPath *indexPath = [NSIndexPath indexPathForRow:gestureRecognizer.tag inSection:0];
    //MeetupsCell *cell=[_tblMeetups cellForRowAtIndexPath:indexPath];
   

    OtherUserProfileVC *other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
    other.userId=[[[arrMeetupsList objectAtIndex:indexPath.row]objectAtIndex:indexPath.row]st_stringForKey:@"user_id"];
  // other.strFacebookId=[[[arrMeetupsList objectAtIndex:indexPath.section]objectAtIndex:indexPath.row]st_stringForKey:FACEBOOKID];
    [self.navigationController pushViewController:other animated:YES];
}
-(void)showProfileDetails:(NSString*)userId{
    NSLog(@"%@",userId);
}
-(void)pressLike:(UIButton *)sender{
    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:[sender tag] inSection:0];
    MeetupsCell *cell=[_tblMeetups cellForRowAtIndexPath:indexPath];
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
    [httpParams setValue:[[arrMeetupsList objectAtIndex:sender.tag] st_stringForKey:@"id"] forKey:@"meetup_id"];
    [httpParams setValue:@"YES" forKey:@"is_like"];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@",HTTPS_MEETUP_LIKE] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         if(status==0)
         {
             NSDictionary *dict1 = (NSDictionary*)data;
             NSString *total_like=[[arrMeetupsList objectAtIndex:sender.tag] st_stringForKey:@"total_like"];
             int like=[total_like intValue]+1;
             cell.lblNumberofLike.text=[NSString stringWithFormat:@"%d",like];
             cell.likeVw.backgroundColor=[Utility getColorFromHexString:@"#7d7d7d"];
             cell.btnLike.userInteractionEnabled=NO;
             
         }
         else
         {
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
             
         }
     }];
    
    
}

-(void)openUserProfileDetails:(UIButton *)sender{
    
    CGPoint center= sender.center;
    CGPoint rootViewPoint = [sender.superview convertPoint:center toView:self.tblMeetups];
    NSIndexPath *indexPath = [self.tblMeetups indexPathForRowAtPoint:rootViewPoint];
    
    OtherUserProfileVC *other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
    if (isSearching) {
        
        other.userId=[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"user_id"];
    }
    else{
        other.userId=[[arrMeetupsList objectAtIndex:indexPath.row] st_stringForKey:@"user_id"];
    }
    
    [self.navigationController pushViewController:other animated:YES];
    
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    MeetupDetailsVC * meetDetails=[self.storyboard instantiateViewControllerWithIdentifier:@"MeetupDetailsVC"];
    meetDetails.str_status=@"Join";
    meetDetails.isAccept=YES;
    if (isSearching) {
        meetDetails.meetupId=[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"id"];
        if ([[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"type"]isEqualToString:@"public"]) {
            meetDetails.isPublic=YES;
        }
        else{
            meetDetails.isPublic=NO;
        }

    }
    else{
       meetDetails.meetupId=[[arrMeetupsList objectAtIndex:indexPath.row] st_stringForKey:@"id"];
        if ([[[arrMeetupsList objectAtIndex:indexPath.row] st_stringForKey:@"type"]isEqualToString:@"public"]) {
            meetDetails.isPublic=YES;
        }
        else{
            meetDetails.isPublic=NO;
        }
    }
    [self.navigationController pushViewController:meetDetails animated:YES];
}


#pragma mark - Search Implementation
-(void)SearchbarCustom
{
    self.meetupSearchBar.layer.cornerRadius  = 6;
    self.meetupSearchBar.clipsToBounds = YES;
    for (UIView *subView in self.meetupSearchBar.subviews) {
        for(id field in subView.subviews){
            if ([field isKindOfClass:[UITextField class]]) {
                UITextField *textField = (UITextField *)field;
                [textField setBackgroundColor:[UIColor whiteColor]];
            }
        }
    }
    for(id subview in [self.meetupSearchBar subviews])
    {
        if ([subview isKindOfClass:[UIButton class]]) {
            [subview setEnabled:YES];
        }
    }
    
    for (UIView *subview in [[self.meetupSearchBar.subviews lastObject] subviews]) {
        if ([subview isKindOfClass:NSClassFromString(@"UISearchBarBackground")]) {
            [subview setAlpha:0.0];
            break;
        }
    }
}


-(void)searchTableList
{
    NSString *searchString = meetupSearchBar.text;
    
    
    for (NSDictionary *dict in arrMeetupsList)
    {
        
    
        
        
        if ([[dict st_stringForKey:@"title"] rangeOfString:searchString options:NSCaseInsensitiveSearch].location == NSNotFound) {
            
        }
        else {
            [filteredContentList addObject:dict];
            [self.tblMeetups reloadData];
        }
        
    }
    
    
}






- (void)searchBarTextDidBeginEditing:(UISearchBar *)searchBar
{
    //isSearching = YES;
    self.meetupSearchBar.showsCancelButton =YES;
}
- (void)searchBarTextDidEndEditing:(UISearchBar *)searchBar{
    self.meetupSearchBar.showsCancelButton =NO;
}

- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText
{
    [filteredContentList removeAllObjects];
    if([searchText length] != 0)
    {
        isSearching = YES;
        [self searchTableList];
    }
    else
    {
        isSearching = NO;
        [self.meetupSearchBar resignFirstResponder];
    }
    [self.tblMeetups reloadData];
}

- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar {
    NSLog(@"Cancel clicked");
    isSearching = NO;
    self.meetupSearchBar.showsCancelButton =NO;
    [self.meetupSearchBar resignFirstResponder];
    [self.tblMeetups reloadData];
    self.meetupSearchBar.text=@"";
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar {
    NSLog(@"Search Clicked");
    self.meetupSearchBar.showsCancelButton =NO;
    [self searchTableList];
    // self.searchBar.text=@"";
    [self.meetupSearchBar resignFirstResponder];
    
}


- (void)updateSearchResultsForSearchController:(UISearchController *)aSearchController {
    NSLog(@"updateSearchResultsForSearchController");
    
    NSString *searchString = aSearchController.searchBar.text;
    NSLog(@"searchString=%@", searchString);
    
    // Check if the user cancelled or deleted the search term so we can display the full list instead.
    if (![searchString isEqualToString:@""]) {
        [filteredContentList removeAllObjects];
        for (NSDictionary *dic in arrMeetupsList) {
            if ([searchString isEqualToString:@""] || [[dic st_stringForKey:@"meetup_creator"] localizedCaseInsensitiveContainsString:searchString] == YES) {
                NSLog(@"str=%@", [dic st_stringForKey:@"meetup_creator"]);
                [filteredContentList addObject:[dic st_stringForKey:@"meetup_creator"]];
            }
        }
        isSearching = YES;
    }
    else {
        isSearching = NO;
    }
    
    [self.tblMeetups reloadData];
}



#pragma mark - Button Actions

- (IBAction)btnMeetupsFilterAction:(id)sender {
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:@"yes" forKey:@"filter"];
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams geturl:[NSString stringWithFormat:@"%@%@",HTTPS_MEETUP_FILTER,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         if(status==0)
         {
             NSDictionary *  dict = (NSDictionary*)data;
             arrMeetupsList=[dict st_arrayForKey:@"data"];
             [_tblMeetups reloadData];
             
         }
         else
         {
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
             
         }
     }];

    
}
- (IBAction)btnAddToMeetupAction:(id)sender {
    AddMeetupVC *meetup =[self.storyboard instantiateViewControllerWithIdentifier:@"AddMeetupVC"];
    meetup.event=@"Add";
    meetup.arr_invite_user=[NSMutableArray new];
    meetup.meetupId=@"";
    [self.navigationController pushViewController:meetup animated:YES];
}
-(IBAction)myMeetup:(id)sender{
    MyMeetupVC *mymeet=[self.storyboard instantiateViewControllerWithIdentifier:@"MyMeetupVC"];
    [self.navigationController pushViewController:mymeet animated:YES];
}

-(void)submitFilter:(UIButton *)btn {
   // [_meetupFilterView removeFromSuperview];
}

-(void)cancelFilter:(UIButton *)btn {
   // [_meetupFilterView removeFromSuperview];
}
- (IBAction)btnMeetupRequestAction:(id)sender {
    
    MeetupRequestListVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"MeetupRequestListVC"];
    [self.navigationController pushViewController:vc animated:YES];
}
@end
