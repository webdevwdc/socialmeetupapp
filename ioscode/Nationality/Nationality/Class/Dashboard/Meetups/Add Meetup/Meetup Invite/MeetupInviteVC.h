//
//  MeetupInviteVC.h
//  Nationality
//
//  Created by WTS-MAC3052016 on 28/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol MeetupInviteDelegate <NSObject>

-(void)updateEventTga:(NSArray *)strEvent;

@end

@interface MeetupInviteVC : UIViewController

@property (weak, nonatomic) IBOutlet UITableView *tblUserList;
@property (weak, nonatomic) IBOutlet UISearchBar *searchBar;
@property (strong, nonatomic)NSString *mtupId;
@property (strong, nonatomic)NSString *event;
@property (strong, nonatomic)NSMutableArray *arr_selected_invite_user;
@property (strong, nonatomic)NSMutableArray *arrSelectedIds;
@property (assign, nonatomic)double Latitude;
@property (assign, nonatomic)double Longitude;
@property (weak, nonatomic) id<MeetupInviteDelegate>delegate;


- (IBAction)btnBackAction:(id)sender;
- (IBAction)btnSendAction:(id)sender;
@end
