//
//  AllBulletinCell.h
//  Nationality
//
//  Created by WTS-MAC3052016 on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AllBulletinCell : UITableViewCell


@property (weak, nonatomic) IBOutlet UIImageView *profileImageView;
@property (weak, nonatomic) IBOutlet UILabel *lblBulletinName;
@property (weak, nonatomic) IBOutlet UILabel *lblUserName;
@property (weak, nonatomic) IBOutlet UILabel *lblBulletinDescription;
@property (weak, nonatomic) IBOutlet UILabel *lblDate;
@property (weak, nonatomic) IBOutlet UILabel *lblTime;
@property (weak, nonatomic) IBOutlet UILabel *lblViewsNumber;
@property (weak, nonatomic) IBOutlet UILabel *lblRepliesNumber;
@property (weak, nonatomic) IBOutlet UILabel *lblLikesNumber;
@property (weak, nonatomic) IBOutlet UIButton *butViewLike;
@property (weak, nonatomic) IBOutlet UIButton *btnUserDetails;
@end
