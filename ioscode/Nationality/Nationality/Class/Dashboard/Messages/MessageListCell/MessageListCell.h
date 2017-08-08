//
//  MessageListCell.h
//  Nationality
//
//  Created by webskitters on 19/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MessageListCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *imgViewProfilePic;
@property (weak, nonatomic) IBOutlet UILabel *lblProfileName;
@property (weak, nonatomic) IBOutlet UILabel *lblMessageContent;
@property (weak, nonatomic) IBOutlet UILabel *lblMessageDate;
@property (weak, nonatomic) IBOutlet UILabel *lblMessageTime;
@property (weak, nonatomic) IBOutlet UIView *viewMessage;
@property (strong, nonatomic) IBOutlet UILabel *lblBadgeCount;
@property (weak, nonatomic) IBOutlet UIButton *btnUserDetails;

@end
