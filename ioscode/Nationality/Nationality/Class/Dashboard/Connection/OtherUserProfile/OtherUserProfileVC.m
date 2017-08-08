//
//  OtherUserProfileVC.m
//  Nationality
//
//  Created by WTS-MAC3052016 on 06/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "OtherUserProfileVC.h"
#import "friendsCell.h"
#import "AttendeesVC.h"
#import "MessagesVC.h"
//#import "Constant.h"
@interface OtherUserProfileVC (){
    NSMutableArray *arr_mutual_frnd;
    NSMutableArray *arr_section_data;
    NSMutableArray *arr_details;
    CGFloat newHeight;
}

@end

@implementation OtherUserProfileVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.tbl_details.tableFooterView=[UIView new];
    [self loadOtherProfileVw];

}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)loadOtherProfileVw{
    arr_details=[NSMutableArray new];
    arr_mutual_frnd=[NSMutableArray new];
    arr_section_data=[[NSMutableArray alloc]initWithObjects:@"SELF DESCRIPTION",@"LANGUAGES", nil];
    _btnMessage.hidden=NO;
    [self getUserDetails];
}

#pragma mark - WebService
-(void)getUserDetails{
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams geturl:[NSString stringWithFormat:@"%@%@/%@",HTTPS_USER_DETAILS,_userId,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             NSDictionary *dictValue=[NSDictionary new];
             dictValue=[dict st_dictionaryForKey:@"data"];
             if ([[dictValue st_stringForKey:@"profile_access"]isEqualToString:@"Yes"]) {
                 self.scrollView.hidden=NO;
             }
             else{
                 self.scrollView.hidden=YES;
             }
             _strFacebookId=[dictValue st_stringForKey:@"facebookId"];
             if ([[dictValue st_stringForKey:@"id"]isEqualToString:[Utility getObjectForKey:USER_ID]]) {
                 [_btnConnect setTitle:@"My Profile" forState:UIControlStateNormal];
                 _btnConnect.backgroundColor=[Utility getColorFromHexString:@"#7F007F"];
                 _btnConnect.userInteractionEnabled=NO;
                 _btnMessage.hidden=YES;
             }
            else if ([[dictValue st_stringForKey:@"connection_status"]isEqualToString:@"Accept"]) {
                 [_btnConnect setTitle:@" Connected" forState:UIControlStateNormal];
                 _btnConnect.backgroundColor=[Utility getColorFromHexString:@"#2CC393"];
                 _btnConnect.userInteractionEnabled=YES;
                _btnMessage.hidden=NO;
             }
             else if ([[dictValue st_stringForKey:@"connection_status"]isEqualToString:@"Pending"]) {
                 [_btnConnect setTitle:@" Request Sent" forState:UIControlStateNormal];
                 _btnConnect.backgroundColor=[Utility getColorFromHexString:@"#7D7D7D"];
                 _btnConnect.userInteractionEnabled=NO;
                 _btnMessage.hidden=NO;
             }
             else if ([[dictValue st_stringForKey:@"connection_status"]isEqualToString:@"Block"]) {
                 [_btnConnect setTitle:@" Block" forState:UIControlStateNormal];
                 _btnMessage.hidden=YES;
                 _btnConnect.backgroundColor=[Utility getColorFromHexString:@"#E01818"];
                 _btnConnect.userInteractionEnabled=NO;
             }
             else{
                 
                 [_btnConnect setTitle:@" Connect" forState:UIControlStateNormal];
                 _btnConnect.backgroundColor=[Utility getColorFromHexString:@"048BCD"];
                 _btnConnect.userInteractionEnabled=YES;
                 _btnMessage.hidden=NO;
             }
             arr_mutual_frnd=[[dictValue st_arrayForKey:@"connection_lists"] mutableCopy];
             [_btnMutualConnection setTitle:[NSString stringWithFormat:@" CONNECTIONS (%lu)",(unsigned long)arr_mutual_frnd.count] forState:UIControlStateNormal];
             [self.collectionMutualConnection reloadData];
             
             [arr_details addObject:dictValue];
             _lblName.text=[NSString stringWithFormat:@"%@ %@",[dictValue  st_stringForKey:@"first_name"],[dictValue  st_stringForKey:@"last_name"]];
             _lblCity.text=[dictValue st_stringForKey:@"home_city"];
             [Utility loadCellImage:_profileImageView imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[dictValue st_stringForKey:@"profile_pic"] ]];
             self.tbl_details.delegate=self;
             self.tbl_details.dataSource=self;
             [self.tbl_details reloadData];
             
             self.scrollView.contentSize = CGSizeMake(0,CGRectGetMaxY(_mutualConnectionView.frame)+100);
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
}



-(void)userConnectService {
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:_userId forKey:@"to_userid"];
    [httpParams setValue:[Utility getObjectForKey:USER_ID] forKey:@"from_userid"];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@",HTTP_ADD_FRIEND] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dictResponse = (NSDictionary *)data;
             
             NSString *strMsg = [[dictResponse st_dictionaryForKey:@"result"] st_stringForKey:@"message"];
             
             [self otherUserProfileAlertWithOneOptions:strMsg];
             
             
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];

}

-(void)userDeleteService {
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setValue:_userId forKey:@"to_userid"];
    [httpParams setValue:[Utility getObjectForKey:USER_ID] forKey:@"from_userid"];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@",HTTP_REMOVE_FRIEND] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             
             NSDictionary *dictResponse = (NSDictionary *)data;
             
             NSString *strMsg = [[dictResponse st_dictionaryForKey:@"result"] st_stringForKey:@"message"];
             
             [self otherUserProfileAlertWithOneOptions:strMsg];
             
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];

}




-(void)setView{


}
#pragma mark - UITableView
- (UIView *) tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    
    UIView *tempView=[[UIView alloc]initWithFrame:CGRectMake(0,0,_tbl_details.bounds.size.width, 60)];
    tempView.backgroundColor=[Utility getColorFromHexString:@"#9BC531"];
    
    UIButton *tempLabel=[[UIButton alloc]initWithFrame:CGRectMake(15,0,_tbl_details.bounds.size.width,30)];
    //tempLabel.backgroundColor=[UIColor clearColor];
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
//        
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
    if (section==0){
        
            return 1;
        
    }
    else
        return 1;
    
    
    
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if ([arr_section_data objectAtIndex:0]) {
        
             NSString *msg =[[arr_details objectAtIndex:indexPath.row] valueForKey:@"tag"];
            if (msg==nil) {
                _mutualConnectionView.frame=CGRectMake(self.mutualConnectionView.frame.origin.x,180+25, _mutualConnectionView.frame.size.width,_mutualConnectionView.frame.size.height);
                
              _tbl_details.frame=CGRectMake(self.tbl_details.frame.origin.x,0, _tbl_details.frame.size.width,180+25);
                return 25;
            }
            newHeight = [Utility getLabelHeight:msg Width:self.tbl_details.frame.size.width Font:[UIFont systemFontOfSize:15]];
            [[NSUserDefaults standardUserDefaults] setValue:[NSNumber numberWithFloat:newHeight] forKey:@"height"];
       _tbl_details.frame=CGRectMake(self.tbl_details.frame.origin.x,0, _tbl_details.frame.size.width,35+newHeight+180);
         _mutualConnectionView.frame=CGRectMake(self.mutualConnectionView.frame.origin.x, 35+newHeight+180, _mutualConnectionView.frame.size.width,_mutualConnectionView.frame.size.height);
        return 35+newHeight;
        
        
    }
    else if ([arr_section_data objectAtIndex:1]){
        
            NSString *msg =[[arr_details objectAtIndex:indexPath.row] valueForKey:@"interest"];
            if (msg==nil) {
                  _mutualConnectionView.frame=CGRectMake(self.mutualConnectionView.frame.origin.x, self.mutualConnectionView.frame.origin.y+25, _mutualConnectionView.frame.size.width,_mutualConnectionView.frame.size.height);
                _tbl_details.frame=CGRectMake(self.tbl_details.frame.origin.x,0, _tbl_details.frame.size.width,_tbl_details.frame.size.height+25);
                return 25;
            }
            newHeight = [Utility getLabelHeight:msg Width:self.tbl_details.frame.size.width Font:[UIFont systemFontOfSize:15]];
            [[NSUserDefaults standardUserDefaults] setValue:[NSNumber numberWithFloat:newHeight] forKey:@"height"];
        _tbl_details.frame=CGRectMake(self.tbl_details.frame.origin.x,0, _tbl_details.frame.size.width,_tbl_details.frame.size.height+35+newHeight);
          _mutualConnectionView.frame=CGRectMake(self.mutualConnectionView.frame.origin.x, self.mutualConnectionView.frame.origin.y+35+newHeight, _mutualConnectionView.frame.size.width,_mutualConnectionView.frame.size.height);
        
        return 35+newHeight;
    }
    else if ([arr_section_data objectAtIndex:2]){
    _mutualConnectionView.frame=CGRectMake(self.mutualConnectionView.frame.origin.x, self.mutualConnectionView.frame.origin.y+35, _mutualConnectionView.frame.size.width,_mutualConnectionView.frame.size.height);
    _tbl_details.frame=CGRectMake(self.tbl_details.frame.origin.x,0, _tbl_details.frame.size.width,_tbl_details.frame.size.height+35);
    return 35;
       
    }
    return 0;
    
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
//    if (indexPath.section==0) {
//        static NSString *simpleTableIdentifier = @"cell";
//        
//        UITableViewCell* cell =[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
//        
//        if (cell == nil) {
//            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:simpleTableIdentifier];
//        }
//        cell.textLabel.text=[[arr_details objectAtIndex:indexPath.row] st_stringForKey:@"tag"];
//       
//        
//        return cell;
//        
//    }
//    else
    if (indexPath.section==0){
        static NSString *simpleTableIdentifier = @"cell";
        
        UITableViewCell* cell =[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
        
        if (cell == nil) {
            cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:simpleTableIdentifier];
        }
        cell.textLabel.text=[[arr_details objectAtIndex:indexPath.row] st_stringForKey:@"short_bio"];
        cell.textLabel.numberOfLines = 0;
        cell.textLabel.lineBreakMode = UILineBreakModeWordWrap;
        cell.textLabel.font=[UIFont fontWithName:@"OPENSANS-REGULAR" size:10.0];
        return cell;
    }
    static NSString *simpleTableIdentifier = @"cell";
    
    UITableViewCell* cell =[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:simpleTableIdentifier];
    }    cell.textLabel.text=[[arr_details objectAtIndex:indexPath.row] st_stringForKey:@"language"];
    return cell;
    
    
}

#pragma mark - UICollectionView
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return arr_mutual_frnd.count;
}


- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath

{
    friendsCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"friendsCell" forIndexPath:indexPath];
    [Utility loadCellImage:cell.img_frnds imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[arr_mutual_frnd objectAtIndex:indexPath.row] st_stringForKey:@"profile_pic"]]];
    cell.lbl_name.text=[NSString stringWithFormat:@"%@ %@",[[arr_mutual_frnd objectAtIndex:indexPath.row] st_stringForKey:@"first_name"],[[arr_mutual_frnd objectAtIndex:indexPath.row] st_stringForKey:@"last_name"]];
    cell.img_frnds.layer.cornerRadius=cell.img_frnds.frame.size.height/2;
    cell.img_frnds.layer.borderWidth=1.0;
    cell.img_frnds.layer.borderColor=[Utility getColorFromHexString:@"#9BC531"].CGColor;
    cell.img_frnds.clipsToBounds=YES;
    
    return cell;
}
- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    OtherUserProfileVC *other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
    other.userId=[[arr_mutual_frnd objectAtIndex:indexPath.row]st_stringForKey:@"id"];
    [self.navigationController pushViewController:other animated:YES];
    
}
#pragma mark - UIButtonAction
- (IBAction)btnSeeAllAction:(id)sender {
    AttendeesVC *attendees=[self.storyboard instantiateViewControllerWithIdentifier:@"AttendeesVC"];
    attendees.arr_attendees_list=arr_mutual_frnd;
    attendees.header_status=@"CONNECTIONS";
    [self.navigationController pushViewController:attendees animated:YES];
    
    
}

- (IBAction)btnConnectAction:(id)sender {
    
    
    UIButton *btn =  (UIButton *)sender;
    NSString* aString= btn.titleLabel.text;
    NSString *newString = [aString stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    if ([newString isEqualToString:@"Connect"]) {
        [self userConnectService];
    }
    else if ([newString isEqualToString:@"Connected"]) {
        [self otherUserProfileAlertWithTwoOptions];
    }
    
    
    

    
}


- (IBAction)backClicked:(id)sender {
    
    [self.navigationController popViewControllerAnimated:YES];
}
- (IBAction)btnMessageAction:(id)sender {
    
    MessagesVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"MessagesVC"];
    vc.strReceiverId = _strFacebookId;
    vc.strId = _userId;
    [self.navigationController pushViewController:vc animated:YES];
    
}


#pragma mark - Alert Action

-(void)otherUserProfileAlertWithTwoOptions{
    UIAlertController* alert = [UIAlertController alertControllerWithTitle:@""
                                                                   message:[NSString stringWithFormat:@"Delete connection?"]
                                                            preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction actionWithTitle:@"YES" style:UIAlertActionStyleDefault
                                               handler:^(UIAlertAction * action) {
                                                   
                                                   [self dismissViewControllerAnimated:YES completion:NULL];
                                                   [self userDeleteService];
                                                   
                                               }];
    UIAlertAction* cancel = [UIAlertAction actionWithTitle:@"NO" style:UIAlertActionStyleDefault
                                                   handler:^(UIAlertAction * action) {
                                                       
                                                       [self dismissViewControllerAnimated:YES completion:NULL];
                                                       
                                                   }];
    
    [alert addAction:ok];
    [alert addAction:cancel];
    [self presentViewController:alert animated:YES completion:nil];
}

-(void)otherUserProfileAlertWithOneOptions:(NSString *)strMsg{
    UIAlertController* alert = [UIAlertController alertControllerWithTitle:@""
                                                                   message:strMsg
                                                            preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault
                                               handler:^(UIAlertAction * action) {
                                                   
                                                   [self dismissViewControllerAnimated:YES completion:NULL];
                                                   [self.navigationController popViewControllerAnimated:YES];
                                                   
                                               }];
    
    [alert addAction:ok];
    
    [self presentViewController:alert animated:YES completion:nil];
}


@end
