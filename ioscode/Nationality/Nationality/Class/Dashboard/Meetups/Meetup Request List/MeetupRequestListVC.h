//
//  MeetupRequestListVC.h
//  Nationality
//
//  Created by WTS-MAC3052016 on 02/05/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MeetupRequestListVC : BaseViewController
@property (weak, nonatomic) IBOutlet UITableView *tblMeetupRequestList;

- (IBAction)btnBackAction:(id)sender;
@end
