//
//  MyBulletinVC.m
//  Nationality
//
//  Created by WTS-MAC3052016 on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "MyBulletinVC.h"
#import "MyBulletinCell.h"
#import "AddBulletinVC.h"
#import "BulletinClickedVC.h"

@interface MyBulletinVC ()

@end

@implementation MyBulletinVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    _tableMyBulletin.tableFooterView = [UIView new];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)viewWillAppear:(BOOL)animated{
    
    [self getMyAllBulletin];
}

#pragma --
#pragma mark -GetMyBulletin
-(void)getMyAllBulletin{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams geturl:[NSString stringWithFormat:@"%@%@",HTTPS_MYBULLETIN_LIST,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             _arrMyBulletin=[dict st_arrayForKey:@"data"];
             // [self loadConnectionVw];
             [self.tableMyBulletin reloadData];
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
}

#pragma --
#pragma mark - TableView Delegate And Data Source
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return _arrMyBulletin.count;
}
- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *MyIdentifier = @"MyBulletinCell";
    
    MyBulletinCell *cell = (MyBulletinCell*)[tableView dequeueReusableCellWithIdentifier:MyIdentifier];
    [cell.butViewEdit addTarget:self action:@selector(btnEditAction:) forControlEvents:UIControlEventTouchUpInside];
    [cell.butViewDelete addTarget:self action:@selector(btnDeleteAction:) forControlEvents:UIControlEventTouchUpInside];
    
    cell.lblBulletinName.text = [[_arrMyBulletin objectAtIndex:indexPath.row] st_stringForKey:@"title"];
    cell.lblBulletinDesc.text = [[_arrMyBulletin objectAtIndex:indexPath.row] st_stringForKey:@"content"];
    cell.lblBulletinDate.text = [Utility getFormatedForDate:[[_arrMyBulletin objectAtIndex:indexPath.row] st_stringForKey:@"created_at"]];
    cell.lblBulletinTime.text = [Utility getFormatedForTime:[[_arrMyBulletin objectAtIndex:indexPath.row] st_stringForKey:@"created_at"]];
//    cell.lblComents.text = [[_arrMyBulletin objectAtIndex:indexPath.row] st_stringForKey:@"num_comment"];
//    cell.lblLikesNumber.text = [[_arrMyBulletin objectAtIndex:indexPath.row] st_stringForKey:@"num_comment"];
//    cell.lblViewsNumber.text = [[_arrMyBulletin objectAtIndex:indexPath.row] st_stringForKey:@"num_comment"];
    
    cell.lblViewsNumber.text=[NSString stringWithFormat:@"%@ %@ | %@ %@ | %@ %@",[[_arrMyBulletin objectAtIndex:indexPath.row] st_stringForKey:@"total_view"],([[[_arrMyBulletin objectAtIndex:indexPath.row] st_stringForKey:@"total_view"] integerValue]>1?@"Views":@"View"),[[_arrMyBulletin objectAtIndex:indexPath.row] st_stringForKey:@"total_reply"],([[[_arrMyBulletin objectAtIndex:indexPath.row] st_stringForKey:@"total_reply"] integerValue]>1?@"Replies":@"Reply"),[[_arrMyBulletin objectAtIndex:indexPath.row] st_stringForKey:@"total_like"],([[[_arrMyBulletin objectAtIndex:indexPath.row] st_stringForKey:@"total_like"] integerValue]>1?@"Likes":@"Like")];
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    return cell;
}



- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    return 107.0 * hRatio;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    BulletinClickedVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"BulletinClickedVC"];
    vc.strBulletinId = [[_arrMyBulletin objectAtIndex:indexPath.row] st_stringForKey:@"id"];
    [self.navigationController pushViewController:vc animated:YES];
    
}

#pragma -mark btnEditAction
-(IBAction)btnEditAction:(UIButton *)sender{
    
    CGPoint center= sender.center;
    CGPoint rootViewPoint = [sender.superview convertPoint:center toView:self.tableMyBulletin];
    NSIndexPath *indexPath = [self.tableMyBulletin indexPathForRowAtPoint:rootViewPoint];
    
    AddBulletinVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"AddBulletinVC"];
    vc.isEdit = YES;
    vc.dictBulletin = [_arrMyBulletin objectAtIndex:indexPath.row];
    [self.navigationController pushViewController:vc animated:YES];

    
    
}

#pragma -mark btnDeleteAction
-(IBAction)btnDeleteAction:(UIButton *)sender
{
    CGPoint center= sender.center;
    CGPoint rootViewPoint = [sender.superview convertPoint:center toView:self.tableMyBulletin];
    NSIndexPath *indexPath = [self.tableMyBulletin indexPathForRowAtPoint:rootViewPoint];
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams deleteurl:[NSString stringWithFormat:@"%@%@",HTTPS_BULLETIN_CLICKED,[[_arrMyBulletin objectAtIndex:indexPath.row] st_stringForKey:@"id"]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             [self showAlert:[[dict st_dictionaryForKey:@"result"] st_stringForKey:@"message"]];
//             [self getMyAllBulletin];
//             // [self loadConnectionVw];
//             [self.tableMyBulletin reloadData];
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
    
}
-(void)showAlert : (NSString*)strMsg
{
    UIAlertController * alert=   [UIAlertController
                                  alertControllerWithTitle:@""
                                  message:strMsg
                                  preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction
                         actionWithTitle:@"OK"
                         style:UIAlertActionStyleDefault
                         handler:^(UIAlertAction * action)
                         {
                             [alert dismissViewControllerAnimated:YES completion:nil];
                             [self.navigationController popViewControllerAnimated:YES];
                         }];
    
    [alert addAction:ok];
    
    [self presentViewController:alert animated:YES completion:nil];
}
- (IBAction)backToPrev:(id)sender {
    
    [self.navigationController popViewControllerAnimated:YES];
}
@end
