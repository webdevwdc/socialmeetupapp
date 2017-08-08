//
//  BlockingVC.h
//  Nationality
//
//  Created by WTS-MAC3052016 on 27/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BlockingVC : UIViewController
@property (weak, nonatomic) IBOutlet UISearchBar *searchBar;
@property (weak, nonatomic) IBOutlet UITableView *tblConnectionList;

- (IBAction)btnBlockedUserAction:(id)sender;
- (IBAction)btnBackAction:(id)sender;

@end
