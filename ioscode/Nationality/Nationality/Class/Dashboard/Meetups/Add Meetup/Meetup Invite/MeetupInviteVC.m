//
//  MeetupInviteVC.m
//  Nationality
//
//  Created by WTS-MAC3052016 on 28/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "MeetupInviteVC.h"
#import "MeetupInviteCell.h"
#import "OtherUserProfileVC.h"

@interface MeetupInviteVC ()
{
    NSArray *arrUserList;
    NSMutableArray *filteredContentList;
    BOOL isSearching;
   

}

@end

@implementation MeetupInviteVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
   // _arrSelectedIds = [[NSMutableArray alloc] init];
    
    [self getAllUserList];
    [self SearchbarCustom];
   // self.tblUserList.hidden = YES;
    if (![_event isEqualToString:@"Add"]) {
        _arrSelectedIds=[NSMutableArray new];
    }
}
#pragma mark - WebService
-(void)getAllUserList{
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:@"" forKey:@"keyword"];
    [httpParams setValue:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
    [httpParams setValue:[NSString stringWithFormat:@"%f",_Latitude] forKey:@"latitude"];
    [httpParams setValue:[NSString stringWithFormat:@"%f",_Longitude] forKey:@"longitude"];
    [httpParams setValue:_mtupId forKey:@"meetup_id"];

    
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@",HTTP_ALL_USER] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             arrUserList = [[dict st_dictionaryForKey:@"result"] st_arrayForKey:@"data"];
             [self.tblUserList reloadData];
         }
         
         else
         {
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
             
         }
     }];
    
    
}

/*-(void)getSearchResult:(NSString *)searchString withSearchPressed:(BOOL)isSearchPressed{
    
   
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:searchString forKey:@"keyword"];
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@%@",HTTPS_SEARCH_USER,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             arrUserList=[[dict st_dictionaryForKey:@"result"] st_arrayForKey:@"data"];
             
             self.tblUserList.hidden = NO;
             [self.tblUserList reloadData];
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
    
    
    
    
}*/
#pragma -mark SearchbarCustom
-(void)SearchbarCustom   //for customising the search bar background
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

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma --
#pragma mark - TableView Delegate And Data Source
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (isSearching){
        return filteredContentList.count;
    }
    else
    return arrUserList.count;
    
    
}
- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    /*static NSString *MyIdentifier = @"MeetupInviteCell";
    
    MeetupInviteCell *cell = (MeetupInviteCell*)[tableView dequeueReusableCellWithIdentifier:MyIdentifier];
    
        cell.profileImage.layer.cornerRadius=cell.profileImage.frame.size.height/2;
        cell.profileImage.clipsToBounds=YES;
        [Utility loadCellImage:cell.profileImage imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"profile_pic"]]];
        
        cell.lblName.text = [NSString stringWithFormat:@"%@ %@",[[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"first_name"],[[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"last_name"]];
        cell.lblCity.text = [[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"home_city"];
        
    
    
    if([[tableView indexPathsForSelectedRows] containsObject:indexPath]) {
        cell.accessoryType = UITableViewCellAccessoryCheckmark;
    } else {
        cell.accessoryType = UITableViewCellAccessoryNone;
    }
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;*/
    
    
    static NSString *MyIdentifier = @"MeetupInviteCell";
    
    MeetupInviteCell *cell = (MeetupInviteCell*)[tableView dequeueReusableCellWithIdentifier:MyIdentifier];
    if (isSearching)
    {
        
        cell.profileImage.layer.cornerRadius=cell.profileImage.frame.size.height/2;
        cell.profileImage.clipsToBounds=YES;
        [Utility loadCellImage:cell.profileImage imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"profile_pic"]]];
        
        cell.lblName.text = [NSString stringWithFormat:@"%@ %@",[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"first_name"],[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"last_name"]];
        cell.lblCity.text = [[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"home_city"];
        
        if([[AppController sharedappController].arrSelectedIndexesForMeetup containsObject:[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"id"]]) {
            cell.accessoryType = UITableViewCellAccessoryCheckmark;
        } else {
            cell.accessoryType = UITableViewCellAccessoryNone;
        }

    }
    
    else
    {
        
        cell.profileImage.layer.cornerRadius=cell.profileImage.frame.size.height/2;
        cell.profileImage.clipsToBounds=YES;
        [Utility loadCellImage:cell.profileImage imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"profile_pic"]]];
        
        cell.lblName.text = [NSString stringWithFormat:@"%@ %@",[[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"first_name"],[[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"last_name"]];
        cell.lblCity.text = [[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"home_city"];
        
        if([[AppController sharedappController].arrSelectedIndexesForMeetup containsObject:[[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"id"]]) {
            cell.accessoryType = UITableViewCellAccessoryCheckmark;
        } else {
            cell.accessoryType = UITableViewCellAccessoryNone;
        }

        
    }
    
    [cell.btnUserDetails addTarget:self action:@selector(openUserProfileDetails:) forControlEvents:UIControlEventTouchUpInside];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;

    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    return 79.0 * hRatio;
}



- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell* cell = [tableView cellForRowAtIndexPath:indexPath];
    if (cell.accessoryType == UITableViewCellAccessoryCheckmark) {
        if (isSearching){

            [_arrSelectedIds removeObject:[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"id"]];
            [_arr_selected_invite_user removeObject:[filteredContentList objectAtIndex:indexPath.row]];
            [[AppController sharedappController].arrSelectedIndexesForMeetup removeObject:[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"id"]];
        }
        else{
            [_arrSelectedIds removeObject:[[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"id"]];
            [_arr_selected_invite_user removeObject:[arrUserList objectAtIndex:indexPath.row]];
            [[AppController sharedappController].arrSelectedIndexesForMeetup removeObject:[[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"id"]];

        }
       
        cell.accessoryType = UITableViewCellAccessoryNone;
        
    }
    else {
        if (isSearching){
            [_arrSelectedIds addObject:[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"id"]];
            [_arr_selected_invite_user addObject:[filteredContentList objectAtIndex:indexPath.row]];
            [[AppController sharedappController].arrSelectedIndexesForMeetup addObject:[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"id"]];
        }
        else{
        [_arrSelectedIds addObject:[[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"id"]];
        [_arr_selected_invite_user addObject:[arrUserList objectAtIndex:indexPath.row]];
            [[AppController sharedappController].arrSelectedIndexesForMeetup addObject:[[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"id"]];
        
       
        }
        
        cell.accessoryType = UITableViewCellAccessoryCheckmark;
    }

    
}

-(void)openUserProfileDetails:(UIButton *)sender{
    
    CGPoint center= sender.center;
    CGPoint rootViewPoint = [sender.superview convertPoint:center toView:self.tblUserList];
    NSIndexPath *indexPath = [self.tblUserList indexPathForRowAtPoint:rootViewPoint];
    
    OtherUserProfileVC *other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
    if (isSearching) {
        
        other.userId=[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"id"];
        // other.strFacebookId=[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:FACEBOOKID];
    }
    else{
        other.userId=[[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"id"];
        // other.strFacebookId=[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:FACEBOOKID];
    }
    
    
    
    [self.navigationController pushViewController:other animated:YES];
    
}


#pragma mark -Search Bar Method Implementation

-(void)searchTableList
{
    NSString *searchString = self.searchBar.text;
    
    
    for (NSDictionary *dict in arrUserList)
    {
        
        
        if ([dict st_stringForKey:@"first_name"].length==0){
            
        }
      else if ([[dict st_stringForKey:@"first_name"] rangeOfString:searchString options:NSCaseInsensitiveSearch].location == NSNotFound) {
            
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
    [self.tblUserList reloadData];
}

- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar {
    NSLog(@"Cancel clicked");
    isSearching = NO;
    self.searchBar.showsCancelButton =NO;
    [self.searchBar resignFirstResponder];
    [self.tblUserList reloadData];
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
        for (NSDictionary *dict in arrUserList)
        {
            if ([searchString isEqualToString:@""] || [[dict st_stringForKey:@"first_name"] localizedCaseInsensitiveContainsString:searchString] == YES)
            {
                NSLog(@"str=%@", dict);
                [filteredContentList addObject:dict];
            }
        }
        isSearching = YES;
    }
    else {
        isSearching = NO;
    }
    
    [self.tblUserList reloadData];
}



#pragma mark - Search Implementation


/*-(void)searchTableList
{
    NSString *searchString = self.searchBar.text;
    
    
    for (NSDictionary *dict in arrUserList)
    {
        
        if ([[dict st_stringForKey:@"first_name"] rangeOfString:searchString options:NSCaseInsensitiveSearch].location == NSNotFound) {
            
        }
        else {
            [filteredContentList addObject:dict];
        }
    }
}

- (void)searchBarTextDidBeginEditing:(UISearchBar *)searchBar
{
    [arrSelectedIds removeAllObjects];
    self.searchBar.showsCancelButton =YES;
}
- (void)searchBarTextDidEndEditing:(UISearchBar *)searchBar{
    self.searchBar.showsCancelButton =NO;
}

- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText
{
    
    
    if([searchText length] == 0)
    {
        //[self searchBackgroundClear];
        self.tblUserList.hidden=YES;
        
        [searchBar performSelector: @selector(resignFirstResponder)
                        withObject: nil
                        afterDelay: 0.01];
        
        int64_t delayInSeconds = 0.01;
        dispatch_time_t popTime = dispatch_time(DISPATCH_TIME_NOW, delayInSeconds * NSEC_PER_SEC);
        dispatch_after(popTime, dispatch_get_main_queue(), ^(void)
                       {
                           self.tblUserList.hidden=YES;
                       });
        
        
        
    }
    else if ([searchText length] < 4)
    {
        //[self searchBackgroundClear];
        self.tblUserList.hidden=YES;
        
        
        int64_t delayInSeconds = 0.01;
        dispatch_time_t popTime = dispatch_time(DISPATCH_TIME_NOW, delayInSeconds * NSEC_PER_SEC);
        dispatch_after(popTime, dispatch_get_main_queue(), ^(void)
                       {
                           self.tblUserList.hidden=YES;
                       });
        
        
    }
    else
    {
        NSString *tmp=[searchText stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
        [self getSearchResult:tmp withSearchPressed:NO];
    }

}

- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar {
    NSLog(@"Cancel clicked");
    [arrSelectedIds removeAllObjects];
    
    self.searchBar.showsCancelButton =NO;
    [self.searchBar resignFirstResponder];
    self.tblUserList.hidden = YES;
    self.searchBar.text=@"";
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar {
    NSLog(@"Search Clicked");
    self.searchBar.showsCancelButton =NO;
    NSString *tmp=[searchBar.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    [self getSearchResult:tmp withSearchPressed:YES];
    
    [self.searchBar resignFirstResponder];
    
}

- (void)updateSearchResultsForSearchController:(UISearchController *)aSearchController {
    NSLog(@"updateSearchResultsForSearchController");
    
    NSString *searchString = aSearchController.searchBar.text;
    NSLog(@"searchString=%@", searchString);
    
    // Check if the user cancelled or deleted the search term so we can display the full list instead.
    if (![searchString isEqualToString:@""]) {
        [filteredContentList removeAllObjects];
        for (NSDictionary *dict in arrUserList)
        {
            if ([searchString isEqualToString:@""] || [[dict st_stringForKey:@"first_name"] localizedCaseInsensitiveContainsString:searchString] == YES)
            {
                NSLog(@"str=%@", dict);
                [filteredContentList addObject:dict];
            }
        }
        isSearching = YES;
    }
    else {
        isSearching = NO;
    }
    
    [self.tblUserList reloadData];
}*/

#pragma -mark btnBackAction
- (IBAction)btnBackAction:(id)sender
{
    //[self.delegate updateEventTga:@"Add"];
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma -mark btnSendAction
- (IBAction)btnSendAction:(id)sender
{
    if(_arrSelectedIds.count == 0)
    {
        [[AppController sharedappController] showAlert:@"Please select user" viewController:self];
    }

    else
    {
        [self.delegate updateEventTga:_arrSelectedIds];
        [self.navigationController popViewControllerAnimated:YES];
       // [self postData];
    }
   
}
    
#pragma -mark postData
@end
