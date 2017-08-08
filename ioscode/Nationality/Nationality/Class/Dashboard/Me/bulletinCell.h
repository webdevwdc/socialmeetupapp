//
//  bulletinCell.h
//  Nationality
//
//  Created by webskitters on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface bulletinCell : UITableViewCell
@property(nonatomic,weak)IBOutlet UIImageView *img_profile;
@property(nonatomic,weak)IBOutlet UILabel *lbl_title;
@property(nonatomic,weak)IBOutlet UILabel *lbl_date;
@property(nonatomic,weak)IBOutlet UILabel *lbl_content;
@property(nonatomic,weak)IBOutlet UIView *bulletinVw;

@property (assign) CGFloat height;
+(CGFloat)height;
@end
