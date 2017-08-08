//
//  InviteFacebookFriendsVC.h
//  Nationality
//
//  Created by webskitters on 04/05/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface InviteFacebookFriendsVC : UIViewController<UITableViewDataSource,UITableViewDelegate>

@property(strong, nonatomic) NSArray *arrFriendList;
@property (weak, nonatomic) IBOutlet UITableView *tblFacebookFriendList;

- (IBAction)btnBackAction:(id)sender;
@end
