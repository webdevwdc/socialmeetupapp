//
//  BlockedUserListVC.h
//  Nationality
//
//  Created by WTS-MAC3052016 on 03/05/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BlockedUserListVC : UIViewController
@property (weak, nonatomic) IBOutlet UITableView *tblBlockedUser;
- (IBAction)btnBackAction:(id)sender;

@end
