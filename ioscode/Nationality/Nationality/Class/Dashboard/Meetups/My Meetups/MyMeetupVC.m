//
//  MyMeetupVC.m
//  Nationality
//
//  Created by WTS-MAC3052016 on 19/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "MyMeetupVC.h"
#import "MyMeetupCell.h"
#import "AddMeetupVC.h"
#import "MeetupDetailsVC.h"
@interface MyMeetupVC ()
{
    NSMutableArray *filteredContentList;
    BOOL isSearching;
}

@end

@implementation MyMeetupVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    self.tblMyMeetupList.tableFooterView = [UIView new];
     [self SearchbarCustom];
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self getMyMeetups];
}

#pragma -- Webservice Call
#pragma mark -getMyMeetups
-(void)getMyMeetups{
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams geturl:[NSString stringWithFormat:@"%@%@",HTTPS_GET_MY_MEETUP,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             self.arrMyMeetupList = [[dict st_arrayForKey:@"data"] mutableCopy];
             
             [self.tblMyMeetupList reloadData];
             
         }
//         if(status==1)
//         {
//             NSDictionary *dict = (NSDictionary*)data;
//             self.arrMyMeetupList = [[dict st_arrayForKey:@"data"] mutableCopy];
//             
//             [self.tblMyMeetupList reloadData];
//             //[[AppController sharedappController] showAlert:str viewController:self];
//             
//         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
}

-(void)deleteMyMeetups:(NSString *)strMeetupID{
    
    
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:nil deleteurl:[NSString stringWithFormat:@"%@%@",HTTPS_DELETE_MY_MEETUP,strMeetupID] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             [[AppController sharedappController]showAlert:[[dict st_dictionaryForKey:@"result"] st_stringForKey:@"message"] viewController:self];
              [self getMyMeetups];
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
}


#pragma -mark SearchbarCustom
-(void)SearchbarCustom
{
    
    self.searchBar.layer.cornerRadius  = 6;
    self.searchBar.clipsToBounds       = YES;
    
    
    //search bar background color
    for (UIView *subView in self.searchBar.subviews) {
        for(id field in subView.subviews){
            if ([field isKindOfClass:[UITextField class]]) {
                UITextField *textField = (UITextField *)field;
                [textField setBackgroundColor:[UIColor whiteColor]];
            }
        }
    }
    
    for(id subview in [self.searchBar subviews])
    {
        if ([subview isKindOfClass:[UIButton class]]) {
            [subview setEnabled:YES];
        }
    }
    
    for (UIView *subview in [[self.searchBar.subviews lastObject] subviews]) {
        if ([subview isKindOfClass:NSClassFromString(@"UISearchBarBackground")]) {
            [subview setAlpha:0.0];
            break;
        }
    }
    
    isSearching = NO;
    
    filteredContentList = [[NSMutableArray alloc]init];
    
    
    
}

#pragma --
#pragma mark - TableView Delegate And Data Source
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    if (isSearching) {
        return filteredContentList.count;
    }
    else {
        return self.arrMyMeetupList.count;
    }
}
- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *MyIdentifier = @"MyMeetupCell";
    
    MyMeetupCell *cell = (MyMeetupCell*)[tableView dequeueReusableCellWithIdentifier:MyIdentifier];
    
    if (isSearching)
    {
        cell.lblMeetupName.text = [[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"title"];
        cell.lblDate.text = [Utility getFormatedForDate:[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"date_time"]];
        cell.lblTime.text = [Utility getFormatedForTime:[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"date_time"]];
        cell.lblLikeCount.text = [[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"total_like"];
        cell.lblName.text = [[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"meetup_creator"];
        
        cell.profileImage.layer.cornerRadius=cell.profileImage.frame.size.height/2;
        
        cell.profileImage.clipsToBounds=YES;
        [Utility loadCellImage:cell.profileImage imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"meetup_creator_pic"]]];
        
        NSArray *arrAttendee = [[filteredContentList objectAtIndex:indexPath.row] st_arrayForKey:@"attendee"];
        
        cell.imgFriend1.layer.cornerRadius=cell.imgFriend1.frame.size.height/2;
        cell.imgFriend1.clipsToBounds=YES;
        
        cell.imgFriend2.layer.cornerRadius=cell.imgFriend2.frame.size.height/2;
        cell.imgFriend2.clipsToBounds=YES;
        
        cell.imgFriend3.layer.cornerRadius=cell.imgFriend3.frame.size.height/2;
        cell.imgFriend3.clipsToBounds=YES;
        
        if(arrAttendee.count == 0)
        {
            cell.imgFriend1.hidden = YES;
            cell.imgFriend2.hidden = YES;
            cell.imgFriend3.hidden = YES;
            cell.lblMoreThanThreedesc.hidden = YES;
        }
        
        else if (arrAttendee.count == 1)
        {
            cell.imgFriend1.hidden = NO;
            cell.imgFriend2.hidden = YES;
            cell.imgFriend3.hidden = YES;
            [Utility loadCellImage:cell.imgFriend1 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrAttendee objectAtIndex:0] st_stringForKey:@"attendee_picture"]]];
            
            cell.lblMoreThanThreedesc.hidden = YES;
        }
        
        else if (arrAttendee.count == 2)
        {
            cell.imgFriend1.hidden = NO;
            cell.imgFriend2.hidden = NO;
            [Utility loadCellImage:cell.imgFriend1 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrAttendee objectAtIndex:0] st_stringForKey:@"attendee_picture"]]];
            [Utility loadCellImage:cell.imgFriend2 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrAttendee objectAtIndex:1] st_stringForKey:@"attendee_picture"]]];
            cell.lblMoreThanThreedesc.hidden = YES;
        }
        
        else if (arrAttendee.count == 3)
        {
            cell.imgFriend1.hidden = NO;
            cell.imgFriend2.hidden = NO;
            cell.imgFriend3.hidden = NO;
            [Utility loadCellImage:cell.imgFriend1 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrAttendee objectAtIndex:0] st_stringForKey:@"attendee_picture"]]];
            [Utility loadCellImage:cell.imgFriend2 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrAttendee objectAtIndex:1] st_stringForKey:@"attendee_picture"]]];
            [Utility loadCellImage:cell.imgFriend3 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrAttendee objectAtIndex:2] st_stringForKey:@"attendee_picture"]]];
            
            cell.lblMoreThanThreedesc.hidden = YES;
        }
        
        else if (arrAttendee.count > 3)
        {
            [Utility loadCellImage:cell.imgFriend1 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrAttendee objectAtIndex:0] st_stringForKey:@"attendee_picture"]]];
            [Utility loadCellImage:cell.imgFriend2 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrAttendee objectAtIndex:1] st_stringForKey:@"attendee_picture"]]];
            [Utility loadCellImage:cell.imgFriend3 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrAttendee objectAtIndex:2] st_stringForKey:@"attendee_picture"]]];
            cell.lblMoreThanThreedesc.hidden = YES;
            cell.lblMoreThanThreedesc.text = [NSString stringWithFormat:@"and %@ others",[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"other_attendee_number"]];
        }

    }
    else
    {
        cell.lblMeetupName.text = [[self.arrMyMeetupList objectAtIndex:indexPath.row] st_stringForKey:@"title"];
        cell.lblDate.text = [Utility getFormatedForDate:[[self.arrMyMeetupList objectAtIndex:indexPath.row] st_stringForKey:@"date_time"]];
        cell.lblTime.text = [Utility getFormatedForTime:[[self.arrMyMeetupList objectAtIndex:indexPath.row] st_stringForKey:@"date_time"]];
        cell.lblLikeCount.text = [[self.arrMyMeetupList objectAtIndex:indexPath.row] st_stringForKey:@"total_like"];
        cell.lblName.text = [[self.arrMyMeetupList objectAtIndex:indexPath.row] st_stringForKey:@"meetup_creator"];
        
        cell.profileImage.layer.cornerRadius=cell.profileImage.frame.size.height/2;
       
        cell.profileImage.clipsToBounds=YES;
        [Utility loadCellImage:cell.profileImage imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[self.arrMyMeetupList objectAtIndex:indexPath.row] st_stringForKey:@"meetup_creator_pic"]]];
        
        NSArray *arrAttendee = [[self.arrMyMeetupList objectAtIndex:indexPath.row] st_arrayForKey:@"attendee"];
        
        cell.imgFriend1.layer.cornerRadius=cell.imgFriend1.frame.size.height/2;
        cell.imgFriend1.clipsToBounds=YES;
        
        cell.imgFriend2.layer.cornerRadius=cell.imgFriend2.frame.size.height/2;
        cell.imgFriend2.clipsToBounds=YES;
        
        cell.imgFriend3.layer.cornerRadius=cell.imgFriend3.frame.size.height/2;
        cell.imgFriend3.clipsToBounds=YES;
        
        if(arrAttendee.count == 0)
        {
            cell.imgFriend1.hidden = YES;
            cell.imgFriend2.hidden = YES;
            cell.imgFriend3.hidden = YES;
            cell.lblMoreThanThreedesc.hidden = YES;
        }
        
        else if (arrAttendee.count == 1)
        {
            cell.imgFriend1.hidden = NO;
            cell.imgFriend2.hidden = YES;
            cell.imgFriend3.hidden = YES;
             [Utility loadCellImage:cell.imgFriend1 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrAttendee objectAtIndex:0] st_stringForKey:@"attendee_picture"]]];
            
            cell.lblMoreThanThreedesc.hidden = YES;
        }
        
        else if (arrAttendee.count == 2)
        {
            cell.imgFriend1.hidden = NO;
            cell.imgFriend2.hidden = NO;
            [Utility loadCellImage:cell.imgFriend1 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrAttendee objectAtIndex:0] st_stringForKey:@"attendee_picture"]]];
             [Utility loadCellImage:cell.imgFriend2 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrAttendee objectAtIndex:1] st_stringForKey:@"attendee_picture"]]];
            cell.lblMoreThanThreedesc.hidden = YES;
        }
        
        else if (arrAttendee.count == 3)
        {
            cell.imgFriend1.hidden = NO;
            cell.imgFriend2.hidden = NO;
            cell.imgFriend3.hidden = NO;
            [Utility loadCellImage:cell.imgFriend1 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrAttendee objectAtIndex:0] st_stringForKey:@"attendee_picture"]]];
            [Utility loadCellImage:cell.imgFriend2 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrAttendee objectAtIndex:1] st_stringForKey:@"attendee_picture"]]];
            [Utility loadCellImage:cell.imgFriend3 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrAttendee objectAtIndex:2] st_stringForKey:@"attendee_picture"]]];
          
            cell.lblMoreThanThreedesc.hidden = YES;
        }
        
        else if (arrAttendee.count > 3)
        {
            [Utility loadCellImage:cell.imgFriend1 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrAttendee objectAtIndex:0] st_stringForKey:@"attendee_picture"]]];
            [Utility loadCellImage:cell.imgFriend2 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrAttendee objectAtIndex:1] st_stringForKey:@"attendee_picture"]]];
            [Utility loadCellImage:cell.imgFriend3 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrAttendee objectAtIndex:2] st_stringForKey:@"attendee_picture"]]];
            cell.lblMoreThanThreedesc.hidden = YES;
            cell.lblMoreThanThreedesc.text = [NSString stringWithFormat:@"and %@ others",[[self.arrMyMeetupList objectAtIndex:indexPath.row] st_stringForKey:@"other_attendee_number"]];
        }

    }

    

   
    cell.butViewEdit.tag = indexPath.row;
    [cell.butViewEdit addTarget:self action:@selector(btnEditAction:) forControlEvents:UIControlEventTouchUpInside];
    
    cell.butViewDelete.tag = indexPath.row;
    [cell.butViewDelete addTarget:self action:@selector(btnDeleteAction:) forControlEvents:UIControlEventTouchUpInside];
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    return cell;
}



- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    return 211.0 * hRatio;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (isSearching)
    {
        MeetupDetailsVC * meetDetails=[self.storyboard instantiateViewControllerWithIdentifier:@"MeetupDetailsVC"];
        meetDetails.meetupId=[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"id"];
        meetDetails.str_status=@"Edit";
        meetDetails.isAccept=YES;
        [self.navigationController pushViewController:meetDetails animated:YES];
    }
    else{
    MeetupDetailsVC * meetDetails=[self.storyboard instantiateViewControllerWithIdentifier:@"MeetupDetailsVC"];
    meetDetails.meetupId=[[_arrMyMeetupList objectAtIndex:indexPath.row] st_stringForKey:@"id"];
    meetDetails.str_status=@"Edit";
    meetDetails.isAccept=YES;
    [self.navigationController pushViewController:meetDetails animated:YES];
    }
}

#pragma -mark btnEditAction
-(IBAction)btnEditAction:(UIButton*)sender
{
    AddMeetupVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"AddMeetupVC"];
    // vc.arr_mutual_user=[NSMutableArray new];
    // vc.isforUpdate=YES;
    vc.event=@"Edit";
    
    if (isSearching) {
        vc.meetupId = [[filteredContentList objectAtIndex:sender.tag] st_stringForKey:@"id"];
        vc.dictMeetupDetails = [filteredContentList objectAtIndex:sender.tag];
        vc.lati=[[filteredContentList objectAtIndex:sender.tag] st_floatForKey:@"meetup_lat"];
        vc.longi=[[filteredContentList objectAtIndex:sender.tag] st_floatForKey:@"meetup_long"];
        vc.arr_invite_user=[[[filteredContentList objectAtIndex:sender.tag] st_arrayForKey:@"attendee"] mutableCopy];
        vc.mtup_place_name=[[filteredContentList objectAtIndex:sender.tag]st_stringForKey:@"place"];
    }
    else
    {
        vc.meetupId = [[self.arrMyMeetupList objectAtIndex:sender.tag] st_stringForKey:@"id"];
        vc.dictMeetupDetails = [self.arrMyMeetupList objectAtIndex:sender.tag];
        vc.lati=[[[self.arrMyMeetupList objectAtIndex:sender.tag] st_stringForKey:@"meetup_lat"] floatValue];
        vc.longi=[[[self.arrMyMeetupList objectAtIndex:sender.tag] st_stringForKey:@"meetup_long"] floatValue];
        vc.arr_invite_user=[[[_arrMyMeetupList objectAtIndex:sender.tag] st_arrayForKey:@"attendee"] mutableCopy];
        vc.mtup_place_name=[[_arrMyMeetupList objectAtIndex:sender.tag]st_stringForKey:@"place"];
    }
    
    [self.navigationController pushViewController:vc animated:YES];
}


#pragma -mark btnDeleteAction
-(IBAction)btnDeleteAction:(UIButton*)sender {
    
    [self myMeetupAlertWithTwoOptions:sender.tag];
}

#pragma mark - Search Implementation


-(void)searchTableList
{
    NSString *searchString = self.searchBar.text;
    
    
    for (NSDictionary *dict in self.arrMyMeetupList)
    {
        
        //////// below comented code is for name's 1stletter search
        
        
        //        NSComparisonResult result = [tempStr compare:searchString options:(NSCaseInsensitiveSearch|NSDiacriticInsensitiveSearch) range:NSMakeRange(0, [searchString length])];
        //
        //        if(result == NSOrderedSame)
        //        {
        //            [filteredContentList addObject:dictUser];
        //        }
        
        
        if ([[dict st_stringForKey:@"title"] rangeOfString:searchString options:NSCaseInsensitiveSearch].location == NSNotFound) {
            
        }
        else {
            [filteredContentList addObject:dict];
        }
        
    }
    
    
}


- (void)searchBarTextDidBeginEditing:(UISearchBar *)searchBar
{
    //isSearching = YES;
    self.searchBar.showsCancelButton =YES;
}
- (void)searchBarTextDidEndEditing:(UISearchBar *)searchBar{
    self.searchBar.showsCancelButton =NO;
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
        [self.searchBar resignFirstResponder];
    }
    [self.tblMyMeetupList reloadData];
}

- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar {
    NSLog(@"Cancel clicked");
    isSearching = NO;
    self.searchBar.showsCancelButton =NO;
    [self.searchBar resignFirstResponder];
    [self.tblMyMeetupList reloadData];
    self.searchBar.text=@"";
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar {
    NSLog(@"Search Clicked");
    self.searchBar.showsCancelButton =NO;
    [self searchTableList];
    // self.searchBar.text=@"";
    [self.searchBar resignFirstResponder];
    
}


- (void)updateSearchResultsForSearchController:(UISearchController *)aSearchController {
    NSLog(@"updateSearchResultsForSearchController");
    
    NSString *searchString = aSearchController.searchBar.text;
    NSLog(@"searchString=%@", searchString);
    
    // Check if the user cancelled or deleted the search term so we can display the full list instead.
    if (![searchString isEqualToString:@""]) {
        [filteredContentList removeAllObjects];
        for (NSDictionary *dict in self.arrMyMeetupList) {
            if ([searchString isEqualToString:@""] || [[dict st_stringForKey:@"title"] localizedCaseInsensitiveContainsString:searchString] == YES) {
                NSLog(@"str=%@", dict);
                [filteredContentList addObject:dict];
            }
        }
        isSearching = YES;
    }
    else {
        isSearching = NO;
    }
    
    [self.tblMyMeetupList reloadData];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma -mark btnBackAction
- (IBAction)btnBackAction:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}


#pragma mark - Alert Action

-(void)myMeetupAlertWithTwoOptions:(NSInteger)index{
    UIAlertController* alert = [UIAlertController alertControllerWithTitle:@""
                                                                   message:[NSString stringWithFormat:@"Are you sure you want to delete this Meetup?"]
                                                            preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction actionWithTitle:@"YES" style:UIAlertActionStyleDefault
                                               handler:^(UIAlertAction * action) {
                                                   
                                                   NSString *strMeetupID=@"";
                                                   
                                                   if (isSearching) {
                                                      strMeetupID = [[filteredContentList objectAtIndex:index] st_stringForKey:@"id"];
                                                   }
                                                   else{
                                                       strMeetupID = [[_arrMyMeetupList objectAtIndex:index] st_stringForKey:@"id"];
                                                   }
                                                   
                                                   
                                                   [self dismissViewControllerAnimated:YES completion:NULL];
                                                   [self deleteMyMeetups:strMeetupID];
                                                   
                                               }];
    UIAlertAction* cancel = [UIAlertAction actionWithTitle:@"NO" style:UIAlertActionStyleDefault
                                                   handler:^(UIAlertAction * action) {
                                                       
                                                       [self dismissViewControllerAnimated:YES completion:NULL];
                                                       
                                                   }];
    
    [alert addAction:ok];
    [alert addAction:cancel];
    [self presentViewController:alert animated:YES completion:nil];
}





@end
