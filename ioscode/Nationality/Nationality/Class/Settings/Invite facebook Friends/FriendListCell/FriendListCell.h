//
//  FriendListCell.h
//  Facebook_Friends_Demo
//
//  Created by webskitters on 03/05/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface FriendListCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *FriendImageView;
@property (weak, nonatomic) IBOutlet UILabel *lblFriendName;
@property (weak, nonatomic) IBOutlet UIButton *btnInviteFriend;

@end
