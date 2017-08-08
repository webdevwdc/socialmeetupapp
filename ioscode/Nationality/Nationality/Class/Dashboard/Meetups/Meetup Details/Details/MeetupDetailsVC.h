//
//  MeetupDetailsVC.h
//  Nationality
//
//  Created by webskitters on 14/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MeetupDetailsVC : UIViewController<UICollectionViewDataSource,UICollectionViewDelegate>

@property (weak, nonatomic) IBOutlet UILabel *lblMeetupName;
@property (weak, nonatomic) IBOutlet UILabel *lblNumberofPeople;
@property (weak, nonatomic) IBOutlet UILabel *lblMeetupDate;
@property (weak, nonatomic) IBOutlet UILabel *lblMeetupTiming;

@property (weak, nonatomic) IBOutlet UILabel *lblnumberofViews;
@property (weak, nonatomic) IBOutlet UILabel *lblNumberofReplies;
@property (weak, nonatomic) IBOutlet UILabel *lblNumberofLikes;

- (IBAction)btnLikeAction:(id)sender;
- (IBAction)btnCommentAction:(id)sender;

@property (weak, nonatomic) IBOutlet UIImageView *imgViewMeetupAdmin;
@property (weak, nonatomic) IBOutlet UILabel *lblMeetupAdminName;
@property (weak, nonatomic) IBOutlet UILabel *lblMeetupLoction;
@property (weak, nonatomic) IBOutlet UILabel *lblMeetupAddress;
@property (weak, nonatomic) IBOutlet UILabel *lblMeetupDesc;
@property (weak, nonatomic) IBOutlet UIView *likeVw;
@property (weak, nonatomic) IBOutlet UIImageView *imgViewCommentPerson;
@property (weak, nonatomic) IBOutlet UILabel *lblCommentPersonName;
@property (weak, nonatomic) IBOutlet UILabel *lblComment;
@property (weak, nonatomic) IBOutlet UILabel *lblCommentDateTime;
@property (weak, nonatomic) IBOutlet UILabel *lblLikeCount;
@property (weak, nonatomic) IBOutlet UIButton *btnReplyCount;
@property (weak, nonatomic)NSString *meetupId;
- (IBAction)btnCommentLikeAction:(id)sender;
- (IBAction)btnCommentReplyAction:(id)sender;
@property (weak, nonatomic)NSString *str_status;
@property (weak, nonatomic) IBOutlet UILabel *lblAttendeesCount;
@property (weak, nonatomic) IBOutlet UILabel *lblTitle;
- (IBAction)btnSeeAllAction:(id)sender;
@property (weak, nonatomic) IBOutlet UICollectionView *attendeesCollectionView;
@property (weak, nonatomic) IBOutlet UILabel *lbl_added_pl;
@property (weak, nonatomic) IBOutlet UILabel *lblDes;
@property (weak, nonatomic) IBOutlet UIScrollView *detailsScrollView;
@property (weak, nonatomic) IBOutlet UIView *descriptionVw;
@property (weak, nonatomic) IBOutlet UIView *commentVW;
@property (weak, nonatomic) IBOutlet UIView *userCommentVW;
@property (weak, nonatomic) IBOutlet UIView *replyVw;
@property (weak, nonatomic) IBOutlet UIView *attendeesVw;
@property (weak, nonatomic) IBOutlet UIView *commentLikeView;
@property (weak, nonatomic) IBOutlet UIButton *btnStatus;
@property (assign)BOOL isAccept;
@property (assign)BOOL isPublic;
- (IBAction)openUserProfileDetails:(id)sender;
@end







