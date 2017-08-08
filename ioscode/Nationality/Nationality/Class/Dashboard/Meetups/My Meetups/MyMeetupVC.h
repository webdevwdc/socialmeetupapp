//
//  MyMeetupVC.h
//  Nationality
//
//  Created by WTS-MAC3052016 on 19/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MyMeetupVC : UIViewController


@property (weak, nonatomic) IBOutlet UISearchBar *searchBar;
@property (weak, nonatomic) IBOutlet UITableView *tblMyMeetupList;

@property (strong, nonatomic) NSMutableArray *arrMyMeetupList;
- (IBAction)btnBackAction:(id)sender;

@end
