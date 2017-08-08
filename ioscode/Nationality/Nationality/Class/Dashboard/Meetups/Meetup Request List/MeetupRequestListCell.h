//
//  MeetupRequestListCell.h
//  Nationality
//
//  Created by WTS-MAC3052016 on 02/05/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MeetupRequestListCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *profileImage;
@property (weak, nonatomic) IBOutlet UILabel *lblMeetupName;
@property (weak, nonatomic) IBOutlet UILabel *lblCreatorName;
@property (weak, nonatomic) IBOutlet UILabel *lblDate;
@property (weak, nonatomic) IBOutlet UILabel *lblTime;
@property (weak, nonatomic) IBOutlet UIButton *butViewAccept;
@property (weak, nonatomic) IBOutlet UIButton *butViewReject;
@property (weak, nonatomic) IBOutlet UILabel *lblMeetupLocation;
@property (weak, nonatomic) IBOutlet UIButton *btnUserDetails;

@end
