//
//  AllBulletinVC.m
//  Nationality
//
//  Created by WTS-MAC3052016 on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "AllBulletinVC.h"
#import "AllBulletinCell.h"
#import "AddBulletinVC.h"
#import "MyBulletinVC.h"
#import "BulletinClickedVC.h"
#import "OtherUserProfileVC.h"

@interface AllBulletinVC (){
    
    NSMutableArray *filteredContentList;
    BOOL isSearching;

}

@end

@implementation AllBulletinVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    //self.searchBar.placeholder = @"";
    _tableBulletin.tableFooterView = [UIView new];
   // [self getAllBulletin];
    [self pushBadgesRemove];
    isSearching = NO;
    
    filteredContentList = [[NSMutableArray alloc]init];
    //[self SearchbarCustom];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self getAllBulletin];
}
-(void)pushBadgesRemove{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
    [httpParams setValue:@"bulletin_badge" forKey:@"badge_type"];
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

-(void)getAllBulletin{
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams geturl:[NSString stringWithFormat:@"%@%@",HTTPS_BULLETIN_LIST,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             _arrAllBulletin=[dict st_arrayForKey:@"data"];
            // [self loadConnectionVw];
             [self.tableBulletin reloadData];
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
    
}

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
    
    if (isSearching) {
        return filteredContentList.count;
    }
    else {
        return self.arrAllBulletin.count;
    }
}
- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *MyIdentifier = @"AllBulletinCell";
    
    AllBulletinCell *cell = (AllBulletinCell*)[tableView dequeueReusableCellWithIdentifier:MyIdentifier];
    
    cell.profileImageView.layer.cornerRadius = cell.profileImageView.frame.size.height /2;
    cell.profileImageView.layer.masksToBounds = YES;
    [cell.butViewLike addTarget:self action:@selector(btnLikeAction:) forControlEvents:UIControlEventTouchUpInside];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    if (isSearching)
    {
        if ([[Utility getObjectForKey:USER_ID] isEqualToString:[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"user_id"]] || [[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"is_like"] isEqualToString:@"yes"]) {
            [cell.butViewLike setSelected:YES];
            cell.butViewLike.userInteractionEnabled = NO;
        }
        else{
            [cell.butViewLike setSelected:NO];
            cell.butViewLike.userInteractionEnabled = YES;
            
        }
        cell.lblViewsNumber.text=[NSString stringWithFormat:@"%@ %@ | %@ %@ | %@ %@",[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"total_view"],([[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"total_view"] integerValue]>1?@"Views":@"View"),[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"total_reply"],([[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"total_reply"] integerValue]>1?@"Replies":@"Reply"),[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"total_like"],([[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"total_like"] integerValue]>1?@"Likes":@"Like")];
        cell.lblBulletinName.text = [[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"title"];
        
        float txtHeight = [Utility getLabelHeight:([[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"content"] length]>0?[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"content"]:@"") Width:_tableBulletin.frame.size.width/2 Font:[UIFont fontWithName:@"OPENSANS-REGULAR" size:14.0]];
        //float txtActualHeight = [Utility getLabelActualHeight:([[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"content"] length]>0?[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"content"]:@"") Width:_tableBulletin.frame.size.width/2 Font:[UIFont fontWithName:@"OPENSANS-REGULAR" size:14.0]];
        
        CGRect frame = cell.lblBulletinDescription.frame;
        frame.size.height = txtHeight + 25;
        cell.lblBulletinDescription.frame = frame;
        
        cell.lblBulletinDescription.text = [[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"content"];
        cell.lblViewsNumber.frame = CGRectMake(cell.lblViewsNumber.frame.origin.x, CGRectGetMaxY(cell.lblBulletinDescription.frame)+2, cell.lblViewsNumber.frame.size.width, cell.lblViewsNumber.frame.size.height);
        
        [Utility loadCellImage:cell.profileImageView imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"bulletin_creator_pic"]]];
        
        cell.lblUserName.text = [[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"bulletin_creator"];
        cell.lblDate.text = [Utility getFormatedForDate:[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"created_at"]];
        cell.lblTime.text = [Utility getFormatedForTime:[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"created_at"]];
        
        return cell;
    }
    
    
    if ([[Utility getObjectForKey:USER_ID] isEqualToString:[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:@"user_id"]] || [[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:@"is_like"] isEqualToString:@"yes"]) {
        [cell.butViewLike setSelected:YES];
        cell.butViewLike.userInteractionEnabled = NO;
    }
    else{
        [cell.butViewLike setSelected:NO];
        cell.butViewLike.userInteractionEnabled = YES;
    
    }
    cell.lblViewsNumber.text=[NSString stringWithFormat:@"%@ %@ | %@ %@ | %@ %@",[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:@"total_view"],([[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:@"total_view"] integerValue]>1?@"Views":@"View"),[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:@"total_reply"],([[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:@"total_reply"] integerValue]>1?@"Replies":@"Reply"),[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:@"total_like"],([[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:@"total_like"] integerValue]>1?@"Likes":@"Like")];
    cell.lblBulletinName.text = [[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:@"title"];
    
    float txtHeight = [Utility getLabelHeight:([[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:@"content"] length]>0?[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:@"content"]:@"") Width:_tableBulletin.frame.size.width/2 Font:[UIFont fontWithName:@"OPENSANS-REGULAR" size:14.0]];
    
     //float txtActualHeight = [Utility getLabelActualHeight:([[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:@"content"] length]>0?[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:@"content"]:@"") Width:_tableBulletin.frame.size.width/2 Font:[UIFont fontWithName:@"OPENSANS-REGULAR" size:14.0]];
    CGRect frame = cell.lblBulletinDescription.frame;
    frame.size.height = txtHeight + 25;
    cell.lblBulletinDescription.frame = frame;
    
    cell.lblBulletinDescription.text = [[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:@"content"];
    cell.lblViewsNumber.frame = CGRectMake(cell.lblViewsNumber.frame.origin.x, CGRectGetMaxY(cell.lblBulletinDescription.frame)+5, cell.lblViewsNumber.frame.size.width, cell.lblViewsNumber.frame.size.height);
    [Utility loadCellImage:cell.profileImageView imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:@"bulletin_creator_pic"]]];
    
   /* if ([[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:@"bulletin_creator_pic"] length]>0) {

        [Utility loadCellImage:cell.profileImageView imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:@"bulletin_creator_pic"]]];
    }
    else
        cell.profileImageView.image = [UIImage imageNamed:@"bulletin-proimg"];
    
    */
    
    cell.lblUserName.text = [[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:@"bulletin_creator"];
    cell.lblDate.text = [Utility getFormatedForDate:[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:@"created_at"]];
    cell.lblTime.text = [Utility getFormatedForTime:[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:@"created_at"]];
    
    [cell.btnUserDetails addTarget:self action:@selector(openUserProfileDetails:) forControlEvents:UIControlEventTouchUpInside];

    
    return cell;
}



- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    float txtHeight;
    
    if (isSearching) {
        txtHeight = [Utility getLabelHeight:([[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"content"] length]>0?[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"content"]:@"") Width:_tableBulletin.frame.size.width/2 Font:[UIFont fontWithName:@"OPENSANS-REGULAR" size:13.0]];
    }
    else
        txtHeight = [Utility getLabelHeight:([[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:@"content"] length]>0?[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:@"content"]:@"") Width:_tableBulletin.frame.size.width/2 Font:[UIFont fontWithName:@"OPENSANS-REGULAR" size:13.0]];
    
    
    
    return 113.0 * hRatio + txtHeight ;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [_searchBar resignFirstResponder];
    BulletinClickedVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"BulletinClickedVC"];
    if (isSearching) {
        vc.strBulletinId = [[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"id"];
    }
    else
        vc.strBulletinId = [[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:@"id"];
    [self.navigationController pushViewController:vc animated:YES];
    
}

#pragma -mark btnLikeAction

-(IBAction)btnLikeAction:(UIButton *)sender
{
    
    CGPoint center= sender.center;
    CGPoint rootViewPoint = [sender.superview convertPoint:center toView:self.tableBulletin];
    NSIndexPath *indexPath = [self.tableBulletin indexPathForRowAtPoint:rootViewPoint];
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setObject:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
    if (isSearching ==YES) {
        [httpParams setObject:[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"id"] forKey:@"bulletin_id"];
    }
    else{
        [httpParams setObject:[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:@"id"] forKey:@"bulletin_id"];
    }
    
    [httpParams setObject:@"Yes" forKey:@"is_like"];
    
    
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:HTTPS_BULLETIN_LIKE getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             [self getAllBulletin];
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
}

-(void)openUserProfileDetails:(UIButton *)sender{
    
    CGPoint center= sender.center;
    CGPoint rootViewPoint = [sender.superview convertPoint:center toView:self.tableBulletin];
    NSIndexPath *indexPath = [self.tableBulletin indexPathForRowAtPoint:rootViewPoint];
    
    OtherUserProfileVC *other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
    if (isSearching) {
        
        other.userId=[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"user_id"];
        // other.strFacebookId=[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:FACEBOOKID];
    }
    else{
        other.userId=[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:@"user_id"];
        // other.strFacebookId=[[_arrAllBulletin objectAtIndex:indexPath.row] st_stringForKey:FACEBOOKID];
    }
    
    
    
    [self.navigationController pushViewController:other animated:YES];
    
}

#pragma mark - Search Implementation


-(void)searchTableList
{
    NSString *searchString = self.searchBar.text;
    
    
    for (NSDictionary *dict in self.arrAllBulletin)
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
//    [_tableBulletin reloadData];
    
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
    [self.tableBulletin reloadData];
}

- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar {
    NSLog(@"Cancel clicked");
    isSearching = NO;
    self.searchBar.showsCancelButton =NO;
    [self.searchBar resignFirstResponder];
    [self.tableBulletin reloadData];
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
        for (NSDictionary *dict in self.arrAllBulletin) {
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
    
    [self.tableBulletin reloadData];
}

- (IBAction)btnCreateNewBulletinAction:(id)sender
{
    [_searchBar resignFirstResponder];
    AddBulletinVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"AddBulletinVC"];
    [self.navigationController pushViewController:vc animated:YES];
}

- (IBAction)btnMyBulletinAction:(id)sender
{
    [_searchBar resignFirstResponder];
    MyBulletinVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"MyBulletinVC"];
    [self.navigationController pushViewController:vc animated:YES];
}

- (IBAction)filterBulletinAction:(id)sender {
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setObject:@"yes" forKey:@"filter"];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams geturl:[NSString stringWithFormat:@"%@%@",HTTPS_BULLETIN_LIST,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             _arrAllBulletin=[dict st_arrayForKey:@"data"];
             // [self loadConnectionVw];
             [self.tableBulletin reloadData];
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
    
}

@end
