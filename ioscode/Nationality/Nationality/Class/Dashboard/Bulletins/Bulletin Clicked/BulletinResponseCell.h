//
//  BulletinResponseCell.h
//  Nationality
//
//  Created by webskitters on 17/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BulletinResponseCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *imgViewReplyPerson;
@property (weak, nonatomic) IBOutlet UILabel *lblProfileName;
@property (weak, nonatomic) IBOutlet UILabel *lblResponseDetails;
@property (weak, nonatomic) IBOutlet UIView *viewMessage;
@property (weak, nonatomic) IBOutlet UIView *viewDown;

@property (weak, nonatomic) IBOutlet UILabel *lblDateTime;
@property (weak, nonatomic) IBOutlet UIButton *btnResponse;
@property (weak, nonatomic) IBOutlet UIButton *btnLike;
@property (weak, nonatomic) IBOutlet UILabel *lblResponseCount;
@property (weak, nonatomic) IBOutlet UIView *viewLike;
@property (weak, nonatomic) IBOutlet UIButton *btnAllCommentReply;
@property (weak, nonatomic) IBOutlet UIButton *btnUserDetails;



@end
