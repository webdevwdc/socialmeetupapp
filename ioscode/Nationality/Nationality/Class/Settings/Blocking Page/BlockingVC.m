//
//  BlockingVC.m
//  Nationality
//
//  Created by WTS-MAC3052016 on 27/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "BlockingVC.h"
#import "BlockingCell.h"
#import "BlockedUserListVC.h"

@interface BlockingVC ()
{
    NSArray *arrUserList;
    NSMutableArray *filteredContentList;
    BOOL isSearching;
}

@end

@implementation BlockingVC

- (void)viewDidLoad {
    [super viewDidLoad];
    //[self getConnectionList];
    self.tblConnectionList.tableFooterView = [UIView new];
    [self SearchbarCustom];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self getConnectionList];
}

#pragma mark - WebService
-(void)getConnectionList{
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams geturl:[NSString stringWithFormat:@"%@%@",HTTPS_CONNECTION_LIST,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             arrUserList=[dict st_arrayForKey:@"data"];
             [self.tblConnectionList reloadData];
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
    
    
}
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
        return arrUserList.count;
    }

}
- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *MyIdentifier = @"BlockingCell";
    
    BlockingCell *cell = (BlockingCell*)[tableView dequeueReusableCellWithIdentifier:MyIdentifier];
     if (isSearching)
     {
        cell.butViewBlock.tag = indexPath.row;
        [cell.butViewBlock addTarget:self action:@selector(btnBlockAction:) forControlEvents:UIControlEventTouchUpInside];
         
        cell.profileImage.layer.cornerRadius=cell.profileImage.frame.size.height/2;
        cell.profileImage.clipsToBounds=YES;
        [Utility loadCellImage:cell.profileImage imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"profile_pic"]]];
         
        cell.lblName.text = [NSString stringWithFormat:@"%@ %@",[[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"first_name"],[[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"last_name"]];
        cell.lblCity.text = [[filteredContentList objectAtIndex:indexPath.row] st_stringForKey:@"home_city"];
     }
    
    else
    {
        cell.butViewBlock.tag = indexPath.row;
        [cell.butViewBlock addTarget:self action:@selector(btnBlockAction:) forControlEvents:UIControlEventTouchUpInside];
        
        cell.profileImage.layer.cornerRadius=cell.profileImage.frame.size.height/2;
        cell.profileImage.clipsToBounds=YES;
        [Utility loadCellImage:cell.profileImage imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"profile_pic"]]];
        
        cell.lblName.text = [NSString stringWithFormat:@"%@ %@",[[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"first_name"],[[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"last_name"]];
        cell.lblCity.text = [[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"home_city"];

    }
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    return 89.0 * hRatio;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
}

-(IBAction)btnBlockAction:(UIButton*)sender
{
    NSMutableDictionary *httpParams = [NSMutableDictionary new];
    [httpParams setObject:[Utility getObjectForKey:USER_ID] forKey:@"from_userid"];
    [httpParams setObject:[[arrUserList objectAtIndex:sender.tag] st_stringForKey:@"id"] forKey:@"to_userid"];
   
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:HTTP_BLOCK_USER getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             // [[AppController sharedappController] showAlert:@"Meet" viewController:<#(UIViewController *)#>]
             [self getConnectionList];
             
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];

}

#pragma mark - Search Implementation


-(void)searchTableList
{
    NSString *searchString = self.searchBar.text;
    
    
    for (NSDictionary *dict in arrUserList)
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
    [self.tblConnectionList reloadData];
}

- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar {
    NSLog(@"Cancel clicked");
    isSearching = NO;
    self.searchBar.showsCancelButton =NO;
    [self.searchBar resignFirstResponder];
    [self.tblConnectionList reloadData];
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
    
    [self.tblConnectionList reloadData];
}



- (IBAction)btnBlockedUserAction:(id)sender
{
    BlockedUserListVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"BlockedUserListVC"];
    [self.navigationController pushViewController:vc animated:YES];
    
}

- (IBAction)btnBackAction:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}
@end
