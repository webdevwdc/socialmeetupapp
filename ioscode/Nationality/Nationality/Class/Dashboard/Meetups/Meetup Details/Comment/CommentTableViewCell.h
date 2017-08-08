//
//  CommentTableViewCell.h
//  Nationality
//
//  Created by webskitters on 20/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CommentTableViewCell : UITableViewCell
@property(nonatomic,weak) IBOutlet UILabel *lbl_name;
@property(nonatomic,weak) IBOutlet UILabel *lbl_comment;
@property(nonatomic,weak) IBOutlet UILabel *lbl_date;
@property(nonatomic,weak) IBOutlet UILabel *lbl_reply;
@property(nonatomic,weak) IBOutlet UILabel *lbl_like;
@property(nonatomic,weak) IBOutlet UIImageView *imgProfile;
@property(nonatomic,weak)IBOutlet UIButton *btn_reply;
@property(nonatomic,weak)IBOutlet UIButton *btn_like;
@property(nonatomic,weak)IBOutlet UIView *likeVw;
@property (weak, nonatomic) IBOutlet UIButton *btnUserDetails;
@end
