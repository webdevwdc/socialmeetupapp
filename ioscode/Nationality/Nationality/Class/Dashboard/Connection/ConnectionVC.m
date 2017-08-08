//
//  ConnectionVC.m
//  Nationality
//
//  Created by webskitters on 04/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "ConnectionVC.h"
#import "CustomConnectionCell.h"
#import "Constant.h"
#import "OtherUserProfileVC.h"
#import "ConnectionRequestVC.h"
#import "MessagesVC.h"

#import "ConnectionPopupCell.h"

@interface ConnectionVC ()
{
    NSArray *arrSortedContacts;
    NSString *charStr;
    
    NSMutableArray *arrTotalContacts;
    
    NSMutableArray *filteredContentList;
    BOOL isSearching;

    
}

@end

@implementation ConnectionVC

@synthesize arrContacts;
@synthesize arrSearchList;
@synthesize keyArray;

- (void)viewDidLoad {
    [super viewDidLoad];
    isSearching = NO;
    arrContacts=[NSArray new];
    arrSearchList=[NSArray new];
    self.tblConnectionList.tableFooterView = [UIView new];
    self.tblConnectionList.tag = 1;
    
    self.frndSearchBox.tag = 1;
    
    filteredContentList = [[NSMutableArray alloc]init];
    

       // Do any additional setup after loading the view.
}
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    [self getConnectionList];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)loadConnectionVw{
    self.lblFrndRequestCount.layer.cornerRadius = wRatio * 7.0;
    self.lblFrndRequestCount.layer.masksToBounds = YES;
    
    keyArray = [[NSMutableArray alloc] init];
    arrTotalContacts = [[NSMutableArray alloc] init];
    arrSortedContacts = [[NSMutableArray alloc] init];
//        arrContacts = [[NSArray alloc] initWithObjects:@"psjdbajh",@"rnjxncvn",@"anjkhfjs",@"bnjhnzfn",@"cjikjdfgj",@"djkcgij", @"dnknvbc", @"ejxknvdfj",@"Bear", @"Black Swan", @"Buffalo", @"Camel", @"Cockatoo", @"Dog", @"Donkey", @"Emu", @"Giraffe", @"Greater Rhea", @"Hippopotamus", @"Horse", @"Koala", @"Lion", @"Llama", @"Manatus", @"Meerkat", @"Panda", @"Peacock", @"Pig", @"Platypus", @"Polar Bear", @"Rhinoceros", @"Seagull", @"Tasmania Devil", @"Whale", @"Whale Shark", @"Wombat", nil];
    
    NSSortDescriptor  *brandDescriptor = [[NSSortDescriptor alloc] initWithKey:@"first_name" ascending:YES];
    NSArray *sortDescriptors = [NSArray arrayWithObject:brandDescriptor];
    if (!isSearching) {
        arrSortedContacts = arrContacts;
    }
    else{
        arrSortedContacts = filteredContentList;
    }
    
    
    for(NSDictionary*dic in arrSortedContacts)
    {
        char charval=[[[dic valueForKey:@"first_name"] uppercaseString] characterAtIndex:0];   // Get the first character of your string which will be your key
        // charStr=[NSString stringWithUTF8String:&charval];
        charStr=[NSString stringWithFormat:@"%c",charval];
        NSLog(@"%@",[NSString stringWithUTF8String:&charval]);
        
        if(![keyArray containsObject:charStr]){
            [keyArray addObject:charStr];
            
            NSMutableArray *arr = [[NSMutableArray alloc] init];
            
            for (NSDictionary*dic in arrSortedContacts) {
                
                char charval=[[[dic valueForKey:@"first_name"] uppercaseString] characterAtIndex:0];   // Get the first character of your string which will be your key
                // charStr=[NSString stringWithUTF8String:&charval];
                NSString *str=[NSString stringWithFormat:@"%c",charval];
                if ([str isEqualToString:charStr]) {
                    [arr addObject:dic];
                }
            }
            
            
            
            [arrTotalContacts addObject:arr];
            
        }
    }
  
}
#pragma mark - WebService
-(void)getConnectionList{
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams geturl:[NSString stringWithFormat:@"%@%@",HTTPS_CONNECTION_LIST,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             arrContacts=[dict st_arrayForKey:@"data"];
              [self loadConnectionVw];
             if ([[dict st_stringForKey:@"friend_request_number"] integerValue]>0) {
                 self.lblFrndRequestCount.hidden=NO;
                   self.lblFrndRequestCount.text = [dict st_stringForKey:@"friend_request_number"];
             }
             else{
                  self.lblFrndRequestCount.hidden=YES;
             }
           
             [self.tblConnectionList reloadData];
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
    
    
}

-(void)getSearchResult:(NSString *)searchString withSearchPressed:(BOOL)isSearchPressed{
    
    /*if ([searchString characterAtIndex:0] == 'M') {
        _connectionPopupView.tblSuggestedFriendList.hidden = NO;
        [_connectionPopupView.tblSuggestedFriendList reloadData];
    }*/
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:searchString forKey:@"keyword"];
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@%@",HTTPS_SEARCH_USER,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             arrSearchList=[[dict st_dictionaryForKey:@"result"] st_arrayForKey:@"data"];
             
             _connectionPopupView.tblSuggestedFriendList.hidden = NO;
             [_connectionPopupView.tblSuggestedFriendList reloadData];
         }
         
         else
         {
             NSString *str = (NSString *)data;
            // [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];

    
    
    
}


-(void)addFriendWithFriendID:(NSString *)to_userID{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:[Utility getObjectForKey:USER_ID] forKey:@"from_userid"];
    [httpParams setValue:to_userID forKey:@"to_userid"];
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:HTTP_ADD_FRIEND getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             [self showAlert];
           
             
            
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];

}
-(void)showAlert{
 
    UIAlertController * alert=   [UIAlertController
                                  alertControllerWithTitle:@""
                                  message:@"Request send successfully"
                                  preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction
                         actionWithTitle:@"OK"
                         style:UIAlertActionStyleDefault
                         handler:^(UIAlertAction * action)
                         {
                             [alert dismissViewControllerAnimated:YES completion:nil];
                             [_connectionPopupView removeFromSuperview];
                             
                         }];
    
    [alert addAction:ok];
    
    [self presentViewController:alert animated:YES completion:nil];
}
#pragma mark - TableView Datasource and Delegate




- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    if (tableView.tag == 1) {
        return [keyArray count];
    }
    else{
        return 1;
    }
    
}


/*- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    return [keyArray objectAtIndex:section];
}*/

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    
    if (tableView.tag == 1) {
       return [[arrTotalContacts objectAtIndex:section] count];
    }
    else {
        return arrSearchList.count;
    }
    
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    NSLog(@"index %ld",indexPath.row);
    if (tableView.tag == 1) {
        static NSString *MyIdentifier = @"CustomConnectionCell";
        
        CustomConnectionCell *cell = (CustomConnectionCell*)[tableView dequeueReusableCellWithIdentifier:MyIdentifier];
        
        cell.lblUserName.text = [NSString stringWithFormat:@"%@ %@",[[[arrTotalContacts objectAtIndex:indexPath.section] objectAtIndex:indexPath.row] st_stringForKey:@"first_name"],[[[arrTotalContacts objectAtIndex:indexPath.section] objectAtIndex:indexPath.row] st_stringForKey:@"last_name"]];
        cell.lblUserDesc.text=[[[arrTotalContacts objectAtIndex:indexPath.section]objectAtIndex:indexPath.row] st_stringForKey:HOME_CITY];
        cell.btnMessage.tag=indexPath.row;
        [cell.btnMessage addTarget:self action:@selector(sendMessage:) forControlEvents:UIControlEventTouchUpInside];
        NSString *str_imag=[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[[arrTotalContacts objectAtIndex:indexPath.section]objectAtIndex:indexPath.row]st_stringForKey:@"profile_pic"] ];
        NSLog(@"Image %@",[[[arrTotalContacts objectAtIndex:indexPath.section]objectAtIndex:indexPath.row]st_stringForKey:@"profile_pic"]);
        [Utility loadCellImage:cell.imgViewProfile imageUrl:str_imag];
        cell.imgViewProfile.layer.cornerRadius = cell.imgViewProfile.frame.size.height/2;
        cell.imgViewProfile.layer.masksToBounds = YES;
        cell.selectionStyle=UITableViewCellSelectionStyleNone;
        
        return cell;
 
    }
    else {
        static NSString *MyIdentifier = @"ConnectionPopupCell";
        
        ConnectionPopupCell *cell = (ConnectionPopupCell*)[tableView dequeueReusableCellWithIdentifier:MyIdentifier];
        
        cell.lblProfileName.text = [NSString stringWithFormat:@"%@ %@",[[arrSearchList objectAtIndex:indexPath.row] st_stringForKey:@"first_name"],[[arrSearchList objectAtIndex:indexPath.row] st_stringForKey:@"last_name"]];
        
        cell.lblCityName.text = [[arrSearchList objectAtIndex:indexPath.row] st_stringForKey:@"home_city"];
        
         [Utility loadCellImage:cell.imgviewProfile imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrSearchList objectAtIndex:indexPath.row]st_stringForKey:@"profile_pic"] ]];
        
        cell.imgviewProfile.frame = CGRectMake( cell.imgviewProfile.frame.origin.x,  cell.imgviewProfile.frame.origin.y, wRatio*35, wRatio*35);
        cell.imgviewProfile.layer.cornerRadius =  cell.imgviewProfile.frame.size.width / 2;
        cell.imgviewProfile.layer.masksToBounds = YES;
        
        cell.btnSendRequest.tag = indexPath.row;
        
        [cell.btnSendRequest addTarget:self action:@selector(btnSendRequestClicked:) forControlEvents:UIControlEventTouchUpInside];
        
        cell.selectionStyle=UITableViewCellSelectionStyleNone;
        
        return cell;
  
    }
    
    
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    
    if (tableView.tag == 1) {
        OtherUserProfileVC *other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
        other.userId=[[[arrTotalContacts objectAtIndex:indexPath.section]objectAtIndex:indexPath.row]st_stringForKey:@"id"];
        other.strFacebookId=[[[arrTotalContacts objectAtIndex:indexPath.section]objectAtIndex:indexPath.row]st_stringForKey:FACEBOOKID];
        [self.navigationController pushViewController:other animated:YES];
    }
    else{
        
        OtherUserProfileVC *other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
        other.userId=[[arrSearchList objectAtIndex:indexPath.row]st_stringForKey:@"id"];
        other.strFacebookId=[[arrSearchList objectAtIndex:indexPath.row]st_stringForKey:FACEBOOKID];
        [_connectionPopupView removeFromSuperview];
        [self.navigationController pushViewController:other animated:YES];
        
    }
    
    
    
}


- (NSInteger)tableView:(UITableView *)tableView sectionForSectionIndexTitle:(NSString *)title atIndex:(NSInteger)index
{
    if (tableView.tag == 1) {
        return [keyArray indexOfObject:title];
    }
    else
        return 0;
    
}


- (NSArray *)sectionIndexTitlesForTableView:(UITableView *)tableView
{
    if (tableView.tag == 1) {
        return keyArray;
    }
    else {
        return nil;
    }
}

-(void)sendMessage:(UIButton *)sender{
    
    CGPoint buttonPosition = [sender convertPoint:CGPointZero
                                           toView:self.tblConnectionList];
    NSIndexPath *tappedIP = [self.tblConnectionList indexPathForRowAtPoint:buttonPosition];
    MessagesVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"MessagesVC"];
    vc.strReceiverId = [[[arrTotalContacts objectAtIndex:tappedIP.section]objectAtIndex:tappedIP.row]st_stringForKey:FACEBOOKID];;
    vc.strId = [[[arrTotalContacts objectAtIndex:tappedIP.section]objectAtIndex:tappedIP.row]st_stringForKey:@"id"];;
    [self.navigationController pushViewController:vc animated:YES];
    
}
#pragma mark - SearchBar Delegate Methods

-(void)searchTableList
{
    NSString *searchString = self.frndSearchBox.text;
    
    
    for (NSDictionary *dict in arrContacts)
    {
        
        //////// below comented code is for name's 1stletter search
        
        
        //        NSComparisonResult result = [tempStr compare:searchString options:(NSCaseInsensitiveSearch|NSDiacriticInsensitiveSearch) range:NSMakeRange(0, [searchString length])];
        //
        //        if(result == NSOrderedSame)
        //        {
        //            [filteredContentList addObject:dictUser];
        //        }
        
        
        if ([[dict st_stringForKey:@"first_name"] rangeOfString:searchString options:NSCaseInsensitiveSearch].location == NSNotFound) {
            
        }
        else {
            [filteredContentList addObject:dict];
        }
        
    }
    
    [self loadConnectionVw];
    
    
}

- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText{
    
    if (searchBar.tag == 1) {
        [filteredContentList removeAllObjects];
        if([searchText length] != 0)
        {
            isSearching = YES;
            [self searchTableList];
        }
        else
        {
            isSearching = NO;
            [self.frndSearchBox resignFirstResponder];
        }
        [self.tblConnectionList reloadData];

    }
    else {
        if([searchText length] == 0)
        {
            //[self searchBackgroundClear];
            _connectionPopupView.tblSuggestedFriendList.hidden=YES;
            
            [searchBar performSelector: @selector(resignFirstResponder)
                            withObject: nil
                            afterDelay: 0.01];
            
            int64_t delayInSeconds = 0.01;
            dispatch_time_t popTime = dispatch_time(DISPATCH_TIME_NOW, delayInSeconds * NSEC_PER_SEC);
            dispatch_after(popTime, dispatch_get_main_queue(), ^(void)
                           {
                               _connectionPopupView.tblSuggestedFriendList.hidden=YES;
                           });
            
            
            
        }
        else if ([searchText length] < 3)
        {
            //[self searchBackgroundClear];
            _connectionPopupView.tblSuggestedFriendList.hidden=YES;
            int64_t delayInSeconds = 0.01;
            dispatch_time_t popTime = dispatch_time(DISPATCH_TIME_NOW, delayInSeconds * NSEC_PER_SEC);
            dispatch_after(popTime, dispatch_get_main_queue(), ^(void)
                           {
                               _connectionPopupView.tblSuggestedFriendList.hidden=YES;
                           });
            
            
        }
        else
        {
            NSString *tmp=[searchText stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
            [self getSearchResult:tmp withSearchPressed:NO];
        }

    }
    
    
}

/*- (void)searchBarTextDidEndEditing:(UISearchBar *)searchBar
{
    _connectionPopupView.FriendSearchBar.showsCancelButton =NO;
    
    [_connectionPopupView.FriendSearchBar resignFirstResponder];
    
    NSString *tmp=[searchBar.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    [self getSearchResult:tmp withSearchPressed:YES];
}*/




- (void)searchBarTextDidBeginEditing:(UISearchBar *)searchBar
{
    //isSearching = YES;
    if (searchBar.tag == 1) {
        self.frndSearchBox.showsCancelButton =YES;
    }
    else{
        _connectionPopupView.FriendSearchBar.showsCancelButton =YES;

    }
}

- (void)searchBarTextDidEndEditing:(UISearchBar *)searchBar{
    if (searchBar.tag == 1) {
        return;
    }
    else{
       self.frndSearchBox.showsCancelButton =NO;
    }
    
}


- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar
{
    // [searchBar resignFirstResponder];
    //[self searchBackgroundClear]; // arnab
    if (searchBar.tag == 1) {
        NSLog(@"Cancel clicked");
        isSearching = NO;
        self.frndSearchBox.showsCancelButton =NO;
        [self.frndSearchBox resignFirstResponder];
        [self loadConnectionVw];
        [self.tblConnectionList reloadData];
        self.frndSearchBox.text=@"";

    }
    else{
        _connectionPopupView.FriendSearchBar.showsCancelButton =NO;
        [_connectionPopupView.FriendSearchBar resignFirstResponder];
        _connectionPopupView.tblSuggestedFriendList.hidden=YES;
        
        _connectionPopupView.FriendSearchBar.text = @"";

    }
    
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar {
   
    if (searchBar.tag == 1) {
        NSLog(@"Search Clicked");
        self.frndSearchBox.showsCancelButton =NO;
        [self searchTableList];
        // self.searchBar.text=@"";
        [self.frndSearchBox resignFirstResponder];
    }
    else{
        _connectionPopupView.FriendSearchBar.showsCancelButton =NO;
        
        [_connectionPopupView.FriendSearchBar resignFirstResponder];
        
        NSString *tmp=[searchBar.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
        [self getSearchResult:tmp withSearchPressed:YES];

    }
    
    
}




#pragma mark - UITextField Datasource and Delegate

- (BOOL)textFieldShouldReturn:(UITextField *)textField{
    
    [textField resignFirstResponder];
    return YES;
}



#pragma mark - Button Actions

-(void)btnSendRequestClicked:(UIButton*)btn{
    
    [self addFriendWithFriendID:[[arrSearchList objectAtIndex:btn.tag] st_stringForKey:@"id"]];
    
}

-(void)popupDissapear:(UIButton *)btn{
    [_connectionPopupView removeFromSuperview];
}



- (IBAction)btnAddFrndAction:(id)sender {
    
    _connectionPopupView = (ConnectionPopup *)[[NSBundle mainBundle]loadNibNamed:@"ConnectionPopup" owner:self options:nil][0];
    
    _connectionPopupView.frame = CGRectMake(0, 0, [UIScreen mainScreen].bounds.size.width, [UIScreen mainScreen].bounds.size.height);
    
    
    [self.view addSubview:_connectionPopupView];
    
    
    _connectionPopupView.contentView.layer.cornerRadius = 8.0;
    _connectionPopupView.contentView.layer.borderWidth = 4.0;
    _connectionPopupView.contentView.layer.borderColor = [UIColor blackColor].CGColor;
    _connectionPopupView.contentView.layer.masksToBounds = YES;

    
    [_connectionPopupView.tblSuggestedFriendList registerClass:[ConnectionPopupCell class] forCellReuseIdentifier:@"ConnectionPopupCell"];
    
    [_connectionPopupView.tblSuggestedFriendList registerNib:[UINib nibWithNibName:@"ConnectionPopupCell" bundle:nil] forCellReuseIdentifier:@"ConnectionPopupCell"];
    _connectionPopupView.tblSuggestedFriendList.dataSource = self;
    _connectionPopupView.tblSuggestedFriendList.delegate = self;
    _connectionPopupView.tblSuggestedFriendList.tag = 2;
    
    _connectionPopupView.tblSuggestedFriendList.hidden = YES;
    _connectionPopupView.tblSuggestedFriendList.tableFooterView= [UIView new];
    
    _connectionPopupView.FriendSearchBar.delegate = self;
    _connectionPopupView.FriendSearchBar.tag = 2;
    
    [_connectionPopupView.btnDissappear addTarget:self action:@selector(popupDissapear:) forControlEvents:UIControlEventTouchUpInside];
    
    
    
    
    
}
- (IBAction)btnFrndRequestAction:(id)sender {
    ConnectionRequestVC *connection=[self.storyboard instantiateViewControllerWithIdentifier:@"ConnectionRequestVC"];
    [self.navigationController pushViewController:connection animated:YES];
}
@end
