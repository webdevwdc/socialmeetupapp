//
//  MyMeetupCell.h
//  Nationality
//
//  Created by WTS-MAC3052016 on 19/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MyMeetupCell : UITableViewCell


@property (weak, nonatomic) IBOutlet UIImageView *profileImage;
@property (weak, nonatomic) IBOutlet UILabel *lblName;
@property (weak, nonatomic) IBOutlet UILabel *lblMeetupName;
@property (weak, nonatomic) IBOutlet UIImageView *imgFriend1;
@property (weak, nonatomic) IBOutlet UIImageView *imgFriend2;
@property (weak, nonatomic) IBOutlet UIImageView *imgFriend3;
@property (weak, nonatomic) IBOutlet UILabel *lblDate;
@property (weak, nonatomic) IBOutlet UILabel *lblTime;
@property (weak, nonatomic) IBOutlet UILabel *lblLikeCount;
@property (weak, nonatomic) IBOutlet UIButton *butViewEdit;
@property (weak, nonatomic) IBOutlet UILabel *lblMoreThanThreedesc;
@property (weak, nonatomic) IBOutlet UIButton *butViewDelete;

@end
