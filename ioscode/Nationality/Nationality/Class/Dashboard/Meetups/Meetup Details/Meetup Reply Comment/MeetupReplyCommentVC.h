//
//  MeetupReplyCommentVC.h
//  Nationality
//
//  Created by webskitters on 17/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TPKeyboardAvoidingScrollView.h"
@interface MeetupReplyCommentVC : UIViewController<UITextFieldDelegate>{
    NSMutableArray *arr_comment_reply;
    CGRect kb;
    CGRect af;
    CGSize svos;
    CGPoint point;
}
@property (weak, nonatomic) IBOutlet UIImageView *imgViewProfile;
@property (weak, nonatomic) IBOutlet UILabel *lblProfileName;
@property (weak, nonatomic) IBOutlet UILabel *lblComment;
@property (weak, nonatomic) IBOutlet UILabel *lblCommentDateTime;
@property (weak, nonatomic) IBOutlet UILabel *lblCommentLikeCount;
@property (weak, nonatomic) IBOutlet UILabel *lblCommentReplyCount;
@property (weak, nonatomic) IBOutlet UITableView *tblCommentofCommentlist;
@property (weak, nonatomic) IBOutlet UITextField *txtWriteComment;
@property (weak, nonatomic) IBOutlet UILabel *lblReplyToName;
@property (weak, nonatomic) IBOutlet UIScrollView *ScrollView;
@property (strong, nonatomic)NSString *commentId;
@property (strong, nonatomic)NSString *userID;
@property (strong, nonatomic)NSString *mainComment;
@property (strong, nonatomic)NSString *name;
@property (strong, nonatomic)NSString *dateTime;
@property (strong, nonatomic)NSString *like;
@property (strong, nonatomic)NSString *reply;
@property (strong, nonatomic)NSString *meetupId;
@property (strong, nonatomic)NSString *imgStr;
- (IBAction)btnSendAction:(id)sender;
- (IBAction)showProfileDetails:(id)sender;

@end
