//
//  CommentofCommentCell.h
//  Nationality
//
//  Created by webskitters on 17/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CommentofCommentCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *imgViewProfile;
@property (weak, nonatomic) IBOutlet UILabel *lblProfileName;
@property (weak, nonatomic) IBOutlet UILabel *lblComment;
@property (weak, nonatomic) IBOutlet UILabel *lblCommentDateTime;
@property (weak, nonatomic) IBOutlet UILabel *lblcommentLikeCount;
@property (weak, nonatomic) IBOutlet UILabel *lblCommentReplyCount;
@property (weak, nonatomic) IBOutlet UIButton *btnLike;
@property (weak, nonatomic) IBOutlet UIView *viewDown;
@property (weak, nonatomic) IBOutlet UIButton *btnUserDetails;

@end
