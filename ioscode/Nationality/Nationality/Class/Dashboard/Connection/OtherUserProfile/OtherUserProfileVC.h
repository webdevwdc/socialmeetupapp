//
//  OtherUserProfileVC.h
//  Nationality
//
//  Created by WTS-MAC3052016 on 06/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface OtherUserProfileVC : BaseViewController
{
    
}
@property (weak, nonatomic) IBOutlet UIImageView *profileImageView;
@property (weak, nonatomic) IBOutlet UILabel *lblName;
@property (weak, nonatomic) IBOutlet UILabel *lblCity;
@property (weak, nonatomic) IBOutlet UIView *taggedInterestView;
@property (weak, nonatomic) IBOutlet UILabel *lblTaggedMeHeading;
@property (weak, nonatomic) IBOutlet UILabel *lblTaggedMeContent;
@property (weak, nonatomic) IBOutlet UILabel *lblInterestHeading;
@property (weak, nonatomic) IBOutlet UILabel *lblInterestContent;
@property (weak, nonatomic) IBOutlet UILabel *lblLanguageHeading;
@property (weak, nonatomic) IBOutlet UILabel *lblLanguageContent;
@property (weak, nonatomic) IBOutlet UIView *mutualConnectionView;
@property (weak, nonatomic) IBOutlet UILabel *lblMutualConnectionCount;
@property (weak, nonatomic) IBOutlet UICollectionView *collectionMutualConnection;
@property (weak, nonatomic) IBOutlet UITableView *tbl_details;
@property (strong, nonatomic) NSString *userId;
@property (strong, nonatomic) NSString *strFacebookId;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollView;
@property (weak, nonatomic) IBOutlet UIButton *btnMutualConnection;
@property (weak, nonatomic) IBOutlet UIButton *btnSeeAll;
@property (weak, nonatomic) IBOutlet UIButton *btnConnect;
@property (weak, nonatomic) IBOutlet UIButton *btnMessage;
- (IBAction)btnSeeAllAction:(id)sender;

- (IBAction)btnConnectAction:(id)sender;
- (IBAction)btnMessageAction:(id)sender;
- (IBAction)backClicked:(id)sender;

@end
