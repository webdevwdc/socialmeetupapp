//
//  ReportUserFirstVC.m
//  Nationality
//
//  Created by WTS-MAC3052016 on 17/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "ReportUserFirstVC.h"
#import "ReportUserCell.h"
#import "ReportUserSecondVC.h"

@interface ReportUserFirstVC ()
{
    NSArray *arrUserList;
}
@end

@implementation ReportUserFirstVC

- (void)viewDidLoad {
    
    [super viewDidLoad];
    self.tblUserList.tableFooterView=[UIView new];
    [self getConnectionList];
    [self SearchbarCustom];
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
             [self.tblUserList reloadData];
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

#pragma --
#pragma mark - TableView Delegate And Data Source
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return arrUserList.count;
}
- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *MyIdentifier = @"ReportUserCell";
    ReportUserCell *cell = (ReportUserCell*)[tableView dequeueReusableCellWithIdentifier:MyIdentifier];
    cell.profileImg.layer.cornerRadius=cell.profileImg.frame.size.width/2;
    cell.profileImg.clipsToBounds=YES;
    [Utility loadCellImage:cell.profileImg imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"profile_pic"]]];
    //cell.profileImg.image
    cell.lblName.text = [NSString stringWithFormat:@"%@ %@",[[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"first_name"],[[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"last_name"]];
    cell.lblCity.text = [[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"home_city"];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    return 89.0 * hRatio;
    
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    ReportUserSecondVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"ReportUserSecondVC"];
    vc.strReportUserId = [[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"id"];
    vc.strName = [NSString stringWithFormat:@"%@ %@",[[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"first_name"],[[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"last_name"]];
    vc.strCity = [[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"home_city"];
    vc.strImageLink = [[arrUserList objectAtIndex:indexPath.row] st_stringForKey:@"profile_pic"];
    [self.navigationController pushViewController:vc animated:YES];
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(IBAction)backUserSettings:(id)sender{
    [self.navigationController popViewControllerAnimated:YES];
}


@end
