//
//  MeetupsCell.h
//  Nationality
//
//  Created by webskitters on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MeetupsCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *imgViewProfile;
@property (weak, nonatomic) IBOutlet UILabel *lblMeetupsName;
@property (weak, nonatomic) IBOutlet UILabel *lblProfileName;

@property (weak, nonatomic) IBOutlet UILabel *lblMeetupsNum;
@property (weak, nonatomic) IBOutlet UILabel *lblMeetupDate;
@property (weak, nonatomic) IBOutlet UILabel *lblMeetupTime;
@property (weak, nonatomic) IBOutlet UIView*likeVw;
@property (weak, nonatomic) IBOutlet UIImageView *meetupCandidateImg1;
@property (weak, nonatomic) IBOutlet UIImageView *meetupCandidateImg2;
@property (weak, nonatomic) IBOutlet UIImageView *meetupCandidateImg3;

@property (weak, nonatomic) IBOutlet UILabel *lblNumberofLike;
@property (weak, nonatomic) IBOutlet UIButton *btnUser1;
@property (weak, nonatomic) IBOutlet UIButton *btnUser2;
@property (weak, nonatomic) IBOutlet UIButton *btnUser3;
@property (weak, nonatomic) IBOutlet UIButton *btnLike;
@property (weak, nonatomic) IBOutlet UIButton *btnUserDetails;
@end
