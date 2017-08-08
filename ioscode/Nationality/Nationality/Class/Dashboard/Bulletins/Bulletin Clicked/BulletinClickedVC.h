//
//  BulletinClickedVC.h
//  Nationality
//
//  Created by webskitters on 17/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BulletinClickedVC : UIViewController
@property (weak, nonatomic) IBOutlet UIImageView *imgViewProfile;
@property (weak, nonatomic) IBOutlet UILabel *lblProfileName;
@property (weak, nonatomic) IBOutlet UILabel *lblBulletinDate;
@property (weak, nonatomic) IBOutlet UILabel *lblBulletinTime;
@property (weak, nonatomic) IBOutlet UILabel *lblBulletinTitle;
@property (weak, nonatomic) IBOutlet UILabel *lblBulletinName;
@property (weak, nonatomic) IBOutlet UILabel *lblViewCount;
@property (weak, nonatomic) IBOutlet UILabel *lblRepliesCount;
@property (weak, nonatomic) IBOutlet UILabel *lblLikeCount;
@property (weak, nonatomic) IBOutlet UILabel *lblBulletinDesc;
@property (weak, nonatomic) IBOutlet UIView *viewLike;
@property (weak, nonatomic) IBOutlet UIButton *btnLike;
- (IBAction)backToList:(id)sender;

- (IBAction)btnRespondAction:(id)sender;
- (IBAction)btnLikeAction:(id)sender;
@property (weak, nonatomic) IBOutlet UILabel *lblResponseCount;
@property (weak, nonatomic) IBOutlet UITableView *tblBulletinResponseList;

@property (nonatomic, strong) NSString *strBulletinId;

- (IBAction)openUserProfileDetails:(id)sender;

@end
