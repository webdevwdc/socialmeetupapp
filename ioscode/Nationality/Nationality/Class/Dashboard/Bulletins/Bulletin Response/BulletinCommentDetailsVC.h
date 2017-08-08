//
//  BulletinCommentDetailsVC.h
//  Nationality
//
//  Created by webskitters on 26/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "BaseViewController.h"
#import "TPKeyboardAvoidingScrollView.h"

@interface BulletinCommentDetailsVC : BaseViewController

@property (weak, nonatomic) IBOutlet TPKeyboardAvoidingScrollView *scrollViewComment;

@property (weak, nonatomic) IBOutlet UIImageView *imgViewProfile;
@property (weak, nonatomic) IBOutlet UILabel *lblUserName;
@property (weak, nonatomic) IBOutlet UILabel *lblComment;
@property (weak, nonatomic) IBOutlet UILabel *lblCommentDateTime;
@property (weak, nonatomic) IBOutlet UILabel *lblCommentLikeCount;
@property (weak, nonatomic) IBOutlet UILabel *lblCommentReplyCount;
@property (weak, nonatomic) IBOutlet UITableView *tableCommentReplyList;

@property (nonatomic, strong) NSMutableDictionary *dictComment;
@property (weak, nonatomic) IBOutlet UIButton *btnCommentLike;
- (IBAction)commentLike:(id)sender;

- (IBAction)backToPrev:(id)sender;
- (IBAction)userProfileDetails:(id)sender;

@end
