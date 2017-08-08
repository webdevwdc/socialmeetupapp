//
//  AttendeesVC.m
//  Nationality
//
//  Created by webskitters on 19/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "AttendeesVC.h"
#import "AttendeesTableCell.h"
#import "OtherUserProfileVC.h"
#define NumberOfCell 25

@interface AttendeesVC ()

@end

@implementation AttendeesVC

- (void)viewDidLoad {
    [super viewDidLoad];
    _lblHeaderTitle.text=_header_status;
    [_tblAllAttendees reloadData];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - TableView Dtasource and Delegate

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    
    return (_arr_attendees_list.count/4 + 1);
    
    
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *MyIdentifier = @"AttendeesTableCell";
    
    AttendeesTableCell *cell = (AttendeesTableCell*)[tableView dequeueReusableCellWithIdentifier:MyIdentifier];
    
    cell.btnAttendees1.tag = indexPath.row*4+0;
    cell.btnAttendees2.tag = indexPath.row*4+1;
    cell.btnAttendees3.tag = indexPath.row*4+2;
    cell.btnAttendees4.tag = indexPath.row*4+3;
    [cell.btnAttendees1 addTarget:self action:@selector(cell1stImageTapped:) forControlEvents:UIControlEventTouchUpInside];
    [cell.btnAttendees2 addTarget:self action:@selector(cell2ndImageTapped:) forControlEvents:UIControlEventTouchUpInside];
    [cell.btnAttendees3 addTarget:self action:@selector(cell3rdImageTapped:) forControlEvents:UIControlEventTouchUpInside];
    [cell.btnAttendees4 addTarget:self action:@selector(cell4thImageTapped:) forControlEvents:UIControlEventTouchUpInside];
    cell.imgViewAttendees1.layer.borderWidth=1.0;
    cell.imgViewAttendees2.layer.borderWidth=1.0;
    cell.imgViewAttendees3.layer.borderWidth=1.0;
    cell.imgViewAttendees4.layer.borderWidth=1.0;
    cell.imgViewAttendees1.layer.borderColor=[UIColor greenColor].CGColor;
    cell.imgViewAttendees2.layer.borderColor=[UIColor greenColor].CGColor;
    cell.imgViewAttendees3.layer.borderColor=[UIColor greenColor].CGColor;
    cell.imgViewAttendees4.layer.borderColor=[UIColor greenColor].CGColor;
    cell.imgViewAttendees1.clipsToBounds=YES;
    cell.imgViewAttendees2.clipsToBounds=YES;
    cell.imgViewAttendees3.clipsToBounds=YES;
    cell.imgViewAttendees4.clipsToBounds=YES;
    NSLog(@"%ld",(long)indexPath.row);
    
    if (indexPath.row*4+0 < _arr_attendees_list.count) {
        cell.imgViewAttendees1.hidden = NO;
        cell.lblAttendees1.hidden = NO;
        
        cell.imgViewAttendees1.layer.cornerRadius = hRatio*35;
        cell.imgViewAttendees1.layer.masksToBounds = YES;
        [Utility loadCellImage:cell.imgViewAttendees1 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[_arr_attendees_list objectAtIndex:indexPath.row*4+0] st_stringForKey:@"profile_pic"]]];
        cell.lblAttendees1.text=[NSString stringWithFormat:@"%@ %@",[[_arr_attendees_list objectAtIndex:indexPath.row*4+0] st_stringForKey:@"first_name"],[[_arr_attendees_list objectAtIndex:indexPath.row*4+0] st_stringForKey:@"last_name"]];
    }
    else{
        cell.imgViewAttendees1.hidden = YES;
        cell.lblAttendees1.hidden = YES;
    }
    
    

    if (indexPath.row*4+1 < _arr_attendees_list.count) {
        
        cell.imgViewAttendees2.hidden = NO;
        cell.lblAttendees2.hidden = NO;
        
        cell.imgViewAttendees2.layer.cornerRadius = hRatio*35;
        cell.imgViewAttendees2.layer.masksToBounds = YES;
   [Utility loadCellImage:cell.imgViewAttendees2 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[_arr_attendees_list objectAtIndex:indexPath.row*4+1] st_stringForKey:@"profile_pic"]]];
        cell.lblAttendees2.text=[NSString stringWithFormat:@"%@ %@",[[_arr_attendees_list objectAtIndex:indexPath.row*4+1] st_stringForKey:@"first_name"],[[_arr_attendees_list objectAtIndex:indexPath.row*4+1] st_stringForKey:@"last_name"]];
    }
    else{
        cell.imgViewAttendees2.hidden = YES;
        cell.lblAttendees2.hidden = YES;
    }
    
    

    if (indexPath.row*4+2 < _arr_attendees_list.count) {
        
        cell.imgViewAttendees3.hidden = NO;
        cell.lblAttendees3.hidden = NO;
        
        cell.imgViewAttendees3.layer.cornerRadius = hRatio*35;
        cell.imgViewAttendees3.layer.masksToBounds = YES;
           [Utility loadCellImage:cell.imgViewAttendees3 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[_arr_attendees_list objectAtIndex:indexPath.row*4+2] st_stringForKey:@"profile_pic"]]];
        cell.lblAttendees3.text=[NSString stringWithFormat:@"%@ %@",[[_arr_attendees_list objectAtIndex:indexPath.row*4+2] st_stringForKey:@"first_name"],[[_arr_attendees_list objectAtIndex:indexPath.row*4+2] st_stringForKey:@"last_name"]];    }
    else{
        cell.imgViewAttendees3.hidden = YES;
        cell.lblAttendees3.hidden = YES;
    }
    
    

    if (indexPath.row*4+3 < _arr_attendees_list.count) {
        
        cell.imgViewAttendees4.hidden = NO;
        cell.lblAttendees4.hidden = NO;
        cell.imgViewAttendees4.layer.cornerRadius = hRatio*35;
        cell.imgViewAttendees4.layer.masksToBounds = YES;
        [Utility loadCellImage:cell.imgViewAttendees4 imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,[[_arr_attendees_list objectAtIndex:indexPath.row*4+3] st_stringForKey:@"profile_pic"]]];
        cell.lblAttendees4.text=[NSString stringWithFormat:@"%@ %@",[[_arr_attendees_list objectAtIndex:indexPath.row*4+3] st_stringForKey:@"first_name"],[[_arr_attendees_list objectAtIndex:indexPath.row*4+3] st_stringForKey:@"last_name"]];
    }
    else{
        cell.imgViewAttendees4.hidden = YES;
        cell.lblAttendees4.hidden = YES;
    }

    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    
    return cell;
    
}
/*- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
     if (indexPath.row*4+0 < _arr_attendees_list.count) {
         OtherUserProfileVC * other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
         other.userId=[[_arr_attendees_list objectAtIndex:indexPath.row]st_stringForKey:@""];
         [self.navigationController pushViewController:other animated:YES];
         
     }
}*/



-(void)cell1stImageTapped:(UIButton *)btn
{
    OtherUserProfileVC * other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
    if(self.isFromMeetupDetails)
    {
        other.userId=[[_arr_attendees_list objectAtIndex:btn.tag]st_stringForKey:@"user_id"];
    }
    else
    {
        other.userId=[[_arr_attendees_list objectAtIndex:btn.tag]st_stringForKey:@"id"];
    }
    
    [self.navigationController pushViewController:other animated:YES];

}

-(void)cell2ndImageTapped:(UIButton *)btn
{
    OtherUserProfileVC * other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
    if(self.isFromMeetupDetails)
    {
        other.userId=[[_arr_attendees_list objectAtIndex:btn.tag]st_stringForKey:@"user_id"];
    }
    else
    {
        other.userId=[[_arr_attendees_list objectAtIndex:btn.tag]st_stringForKey:@"id"];
    }
    [self.navigationController pushViewController:other animated:YES];
    
}


-(void)cell3rdImageTapped:(UIButton *)btn
{
    OtherUserProfileVC * other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
    if(self.isFromMeetupDetails)
    {
        other.userId=[[_arr_attendees_list objectAtIndex:btn.tag]st_stringForKey:@"user_id"];
    }
    else
    {
        other.userId=[[_arr_attendees_list objectAtIndex:btn.tag]st_stringForKey:@"id"];
    }
    [self.navigationController pushViewController:other animated:YES];
    
}


-(void)cell4thImageTapped:(UIButton *)btn
{
    OtherUserProfileVC * other=[self.storyboard instantiateViewControllerWithIdentifier:@"OtherUserProfileVC"];
    other.userId=[[_arr_attendees_list objectAtIndex:btn.tag]st_stringForKey:@"id"];
    [self.navigationController pushViewController:other animated:YES];
    
}


-(IBAction)backDetails:(id)sender{
    
    [self.navigationController popViewControllerAnimated:YES];
    
}

@end
