//
//  MessagesListßVC.m
//  Nationality
//
//  Created by webskitters on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "MessagesListVC.h"
#import "MessageListCell.h"
#import "MessagesVC.h"
#import "ConnectionMessageListVC.h"
#import "OtherUserProfileVC.h"

@interface MessagesListVC () {
   
    NSMutableArray *filteredContentList;
    BOOL isSearching;

}

@property (strong, nonatomic) NSMutableArray *arrMessagesList;

@end

@implementation MessagesListVC

@synthesize messageListSearchBar,arrMessagesList;



- (void)viewDidLoad {
    
    [super viewDidLoad];
    
    isSearching = NO;
    filteredContentList = [[NSMutableArray alloc]init];
    arrMessagesList = [[NSMutableArray alloc] init];
    
}
-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
     [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(dataRefreshForNotification) name:@"MessagePageRefresh" object:nil];
   // [self pushBadgesRemove];
    [self getAllChatListOfUser];
    
}
-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    
 //   [[NSNotificationCenter defaultCenter] removeObserver:self name:@"MessagePageRefresh" object:nil];
    
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)dataRefreshForNotification{
    //[self pushBadgesRemove];
    [self getAllChatListOfUser];
}
-(void)pushBadgesRemove{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
    [httpParams setValue:@"message_badge" forKey:@"badge_type"];
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
-(void)getAllChatListOfUser{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
//    [httpParams setObject:[Utility getObjectForKey:FACEBOOKID] forKey:@"senderfbid"];
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams geturl:[NSString stringWithFormat:@"%@%@",HTTPS_RECENT_CHAT_MESSAGE,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             
             NSDictionary *dict = (NSDictionary*)data;
             arrMessagesList=[[dict st_arrayForKey:@"data"] mutableCopy];
             [self.tblMessgeList reloadData];
         }
         
         else
         {
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
    
}
#pragma mark - UITableView Datasource and Delegate

#pragma mark - TableView Dtasource and Delegate

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    if (isSearching) {
        return filteredContentList.count;
    }
    else {
        return arrMessagesList.count;
    }
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *MyIdentifier = @"MessageListCell";
    
    MessageListCell *cell = (MessageListCell*)[tableView dequeueReusableCellWithIdentifier:MyIdentifier];
    
    float txtHeight;
    if (isSearching) {
        
        txtHeight = [Utility getLabelHeight:([[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"message"] length]>0?[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"message"]:@"") Width:cell.lblMessageContent.frame.size.width * wRatio Font:[UIFont systemFontOfSize:13.0]];
    }
    else{
        txtHeight = [Utility getLabelHeight:([[[arrMessagesList objectAtIndex:indexPath.row] st_stringForKey:@"message"] length]>0?[[arrMessagesList objectAtIndex:indexPath.row] st_stringForKey:@"message"]:@"") Width:cell.lblMessageContent.frame.size.width * wRatio Font:[UIFont systemFontOfSize:13.0]];
    }
    if (txtHeight+10>25) {
        return 86.0 * hRatio + txtHeight;
    }
    else
        return 90.0 * hRatio;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *MyIdentifier = @"MessageListCell";
    
    MessageListCell *cell = (MessageListCell*)[tableView dequeueReusableCellWithIdentifier:MyIdentifier];
    cell.viewMessage.autoresizingMask = UIViewAutoresizingFlexibleWidth;
    if (isSearching) {
        cell.lblProfileName.text = [[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"name"];
        
        [Utility loadCellImage:cell.imgViewProfilePic imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"image"]]];
        
        NSTimeInterval interval=[[filteredContentList objectAtIndex:indexPath.row] st_longForKey:@"chat_date_time"];
        cell.lblMessageTime.text = [self timeString:interval];
        cell.lblMessageDate.text = [self dateAsddmmyy:interval];
        
        
        cell.lblBadgeCount.layer.cornerRadius= 10.0;
        cell.lblBadgeCount.layer.masksToBounds = YES;
        if ([[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"badge_count"] integerValue] == 0) {
            cell.lblBadgeCount.hidden= YES;
        }
        else{
            cell.lblBadgeCount.hidden = NO;
            cell.lblBadgeCount.text = [[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"badge_count"];
        }
        
        
        
        float txtHeight = [Utility getLabelHeight:([[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"message"] length]>0?[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"message"]:@"") Width:cell.lblMessageContent.frame.size.width * wRatio Font:[UIFont systemFontOfSize:13.0]];
        
        if (txtHeight>cell.lblMessageContent.frame.size.height) {
            CGRect frame = cell.lblMessageContent.frame;
            frame.size.height =  txtHeight;
            cell.lblMessageContent.frame = frame;
            
            cell.viewMessage.frame = CGRectMake(cell.viewMessage.frame.origin.x , cell.viewMessage.frame.origin.y, cell.viewMessage.frame.size.width, CGRectGetMaxY(cell.lblMessageContent.frame)+10);
        }
        
        cell.lblMessageContent.text=[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"message"];
        
    }
    else {
        cell.lblProfileName.text = [[arrMessagesList objectAtIndex:indexPath.row] st_stringForKey:@"name"];
        
        [Utility loadCellImage:cell.imgViewProfilePic imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrMessagesList objectAtIndex:indexPath.row] st_stringForKey:@"image"]]];
        
        NSTimeInterval interval=[[arrMessagesList objectAtIndex:indexPath.row] st_longForKey:@"chat_date_time"];
        cell.lblMessageTime.text = [self timeString:interval];
        cell.lblMessageDate.text = [self dateAsddmmyy:interval];
        
        
        cell.lblBadgeCount.layer.cornerRadius= 10.0;
        cell.lblBadgeCount.layer.masksToBounds = YES;
        if ([[[arrMessagesList objectAtIndex:indexPath.row] st_stringForKey:@"badge_count"] integerValue] == 0) {
            cell.lblBadgeCount.hidden= YES;
        }
        else{
            cell.lblBadgeCount.hidden = NO;
            cell.lblBadgeCount.text = [[arrMessagesList objectAtIndex:indexPath.row] st_stringForKey:@"badge_count"];
        }
        
        
        float txtHeight = [Utility getLabelHeight:([[[arrMessagesList objectAtIndex:indexPath.row] st_stringForKey:@"message"] length]>0?[[arrMessagesList objectAtIndex:indexPath.row] st_stringForKey:@"message"]:@"") Width:cell.lblMessageContent.frame.size.width * wRatio Font:[UIFont systemFontOfSize:13.0]];
        
        if (txtHeight>cell.lblMessageContent.frame.size.height) {
            CGRect frame = cell.lblMessageContent.frame;
            frame.size.height =  txtHeight;
            cell.lblMessageContent.frame = frame;
            
            cell.viewMessage.frame = CGRectMake(cell.viewMessage.frame.origin.x , cell.viewMessage.frame.origin.y, cell.viewMessage.frame.size.width, CGRectGetMaxY(cell.lblMessageContent.frame)+10);
            

        }
        
        cell.lblMessageContent.text=[[arrMessagesList objectAtIndex:indexPath.row] st_stringForKey:@"message"];
        
    }
    cell.imgViewProfilePic.layer.cornerRadius =cell.imgViewProfilePic.layer.frame.size.height/2 ;
    cell.imgViewProfilePic.layer.masksToBounds = YES;
    
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    [cell.btnUserDetails addTarget:self action:@selector(openUserProfileDetails:) forControlEvents:UIControlEventTouchUpInside];
    
    return cell;
    
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    MessagesVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"MessagesVC"];
    if (isSearching) {
        vc.strReceiverId = [[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:FACEBOOKID];
        vc.strId =[[Utility getObjectForKey:USER_ID] isEqualToString:[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"to_userid"]]?[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"from_userid"]:[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"to_userid"];
        
        vc.toUserID = [[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"to_userid"];
        vc.fromUserID = [[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"from_userid"];
        vc.badge_count = [[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"badge_count"];
        
        
        
        vc.strUniqueId = [[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"chat_token"];
    }
    else{
        vc.strReceiverId = [[arrMessagesList objectAtIndex:indexPath.row] st_stringForKey:FACEBOOKID];
        vc.strId =[[Utility getObjectForKey:USER_ID] isEqualToString:[[arrMessagesList objectAtIndex:indexPath.row] st_stringForKey:@"to_userid"]]?[[arrMessagesList objectAtIndex:indexPath.row] st_stringForKey:@"from_userid"]:[[arrMessagesList objectAtIndex:indexPath.row] st_stringForKey:@"to_userid"];
        
        vc.toUserID = [[arrMessagesList objectAtIndex:indexPath.row] st_stringForKey:@"to_userid"];
        vc.fromUserID = [[arrMessagesList objectAtIndex:indexPath.row] st_stringForKey:@"from_userid"];
        vc.badge_count = [[arrMessagesList objectAtIndex:indexPath.row] st_stringForKey:@"badge_count"];
        
        
        
        vc.strUniqueId = [[arrMessagesList objectAtIndex:indexPath.row] st_stringForKey:@"chat_token"];
    }
    vc.isList = YES;
    [self.navigationController pushViewController:vc animated:YES];
    
}


-(void)openUserProfileDetails:(UIButton *)sender{
    
    CGPoint center= sender.center;
    CGPoint rootViewPoint = [sender.superview convertPoint:center toView:self.tblMessgeList];
    NSIndexPath *indexPath = [self.tblMessgeList indexPathForRowAtPoint:rootViewPoint];
    
    OtherUserProfileVC *other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
    if (isSearching) {
        
        other.userId=[[Utility getObjectForKey:USER_ID] isEqualToString:[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"to_userid"]]?[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"from_userid"]:[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"to_userid"];;
        // other.strFacebookId=[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:FACEBOOKID];
    }
    else{
        other.userId=[[Utility getObjectForKey:USER_ID] isEqualToString:[[arrMessagesList objectAtIndex:indexPath.row] st_stringForKey:@"to_userid"]]?[[arrMessagesList objectAtIndex:indexPath.row] st_stringForKey:@"from_userid"]:[[arrMessagesList objectAtIndex:indexPath.row] st_stringForKey:@"to_userid"];;
        // other.strFacebookId=[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:FACEBOOKID];
    }
    
    
    
    [self.navigationController pushViewController:other animated:YES];
    
}

#pragma mark - Search Implementation


-(void)searchTableList
{
    NSString *searchString = messageListSearchBar.text;
    
    
    for (NSDictionary *dict in self.arrMessagesList)
    {
        
        //////// below comented code is for name's 1stletter search
        
        
        //        NSComparisonResult result = [tempStr compare:searchString options:(NSCaseInsensitiveSearch|NSDiacriticInsensitiveSearch) range:NSMakeRange(0, [searchString length])];
        //
        //        if(result == NSOrderedSame)
        //        {
        //            [filteredContentList addObject:dictUser];
        //        }
        
        
        if ([[dict st_stringForKey:@"message"] rangeOfString:searchString options:NSCaseInsensitiveSearch].location == NSNotFound || [[dict st_stringForKey:@"name"] rangeOfString:searchString options:NSCaseInsensitiveSearch].location == NSNotFound) {
            
        }
        else {
            [filteredContentList addObject:dict];
        }
        
    }
    
    
}


- (void)searchBarTextDidBeginEditing:(UISearchBar *)searchBar
{
    //isSearching = YES;
    self.messageListSearchBar.showsCancelButton =YES;
}
- (void)searchBarTextDidEndEditing:(UISearchBar *)searchBar{
    self.messageListSearchBar.showsCancelButton =NO;
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
        [self.messageListSearchBar resignFirstResponder];
    }
    [self.tblMessgeList reloadData];
}

- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar {
    NSLog(@"Cancel clicked");
    isSearching = NO;
    self.messageListSearchBar.showsCancelButton =NO;
    [self.messageListSearchBar resignFirstResponder];
    [self.tblMessgeList reloadData];
    self.messageListSearchBar.text=@"";
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar {
    NSLog(@"Search Clicked");
    self.messageListSearchBar.showsCancelButton =NO;
    [self searchTableList];
    // self.searchBar.text=@"";
    [self.messageListSearchBar resignFirstResponder];
    
}


- (void)updateSearchResultsForSearchController:(UISearchController *)aSearchController {
    NSLog(@"updateSearchResultsForSearchController");
    
    NSString *searchString = aSearchController.searchBar.text;
    NSLog(@"searchString=%@", searchString);
    
    // Check if the user cancelled or deleted the search term so we can display the full list instead.
    if (![searchString isEqualToString:@""]) {
        [filteredContentList removeAllObjects];
        for (NSDictionary *dict in self.arrMessagesList) {
            if ([searchString isEqualToString:@""] || [[dict st_stringForKey:@"message"] localizedCaseInsensitiveContainsString:searchString] == YES || [[dict st_stringForKey:@"name"] localizedCaseInsensitiveContainsString:searchString] == YES) {
                NSLog(@"str=%@", dict);
                [filteredContentList addObject:dict];
            }
        }
        isSearching = YES;
    }
    else {
        isSearching = NO;
    }
    
    [self.tblMessgeList reloadData];
}


#pragma mark - Button Action


- (IBAction)createMessageWithUser:(id)sender {
    
    ConnectionMessageListVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"ConnectionMessageListVC"];
    [self.navigationController pushViewController:vc animated:YES];

}

#pragma mark - GET DATE AND TIME -
-(NSString *)timeString:(NSTimeInterval)timeInterval

{
    
    if(timeInterval>0.0)
        
    {
        NSDate *date=[NSDate dateWithTimeIntervalSince1970:(timeInterval+3600)/1000];
        
        //    NSDate *date = [NSDate dateWithTimeIntervalSinceReferenceDate:timeInterval];
        
        NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
        
        [dateFormat setDateStyle:NSDateFormatterMediumStyle];
        
        //    [dateFormat setDateFormat:@"hh:mm a"];
        
        [dateFormat setDateFormat:@"hh:mm a"];
        
        return [dateFormat stringFromDate:date];
        
    }
    
    return @"";
    
}
-(NSString *)dateAsddmmyy:(NSTimeInterval)timeInterval

{
    
    if(timeInterval>0.0)
        
    {
        NSDate *date=[NSDate dateWithTimeIntervalSince1970:(timeInterval+3600)/1000];
        
        //    NSDate *date = [NSDate dateWithTimeIntervalSinceReferenceDate:timeInterval];
        
        NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
        
        [dateFormat setDateStyle:NSDateFormatterMediumStyle];
        
        //    [dateFormat setDateFormat:@"hh:mm a"];
        
        [dateFormat setDateFormat:@"MM/dd/yy"];
        
        return [dateFormat stringFromDate:date];
        
    }
    
    return @"";
    
}

@end
