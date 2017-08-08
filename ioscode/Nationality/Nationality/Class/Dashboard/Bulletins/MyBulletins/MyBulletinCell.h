//
//  MyBulletinCell.h
//  Nationality
//
//  Created by WTS-MAC3052016 on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MyBulletinCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *lblComents;
@property (weak, nonatomic) IBOutlet UILabel *lblBulletinName;
@property (weak, nonatomic) IBOutlet UILabel *lblBulletinDesc;
@property (weak, nonatomic) IBOutlet UILabel *lblBulletinDate;
@property (weak, nonatomic) IBOutlet UILabel *lblBulletinTime;
@property (weak, nonatomic) IBOutlet UILabel *lblViewsNumber;
@property (weak, nonatomic) IBOutlet UILabel *lblLikesNumber;
@property (weak, nonatomic) IBOutlet UIButton *butViewEdit;
@property (weak, nonatomic) IBOutlet UIButton *butViewDelete;

@end
