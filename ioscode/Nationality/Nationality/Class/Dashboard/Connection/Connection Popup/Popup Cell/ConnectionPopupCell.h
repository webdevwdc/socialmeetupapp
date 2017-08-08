//
//  ConnectionPopupCell.h
//  Nationality
//
//  Created by webskitters on 26/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ConnectionPopupCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *imgviewProfile;
@property (weak, nonatomic) IBOutlet UILabel *lblProfileName;
@property (weak, nonatomic) IBOutlet UIButton *btnSendRequest;
@property (strong, nonatomic) IBOutlet UILabel *lblCityName;

@end
