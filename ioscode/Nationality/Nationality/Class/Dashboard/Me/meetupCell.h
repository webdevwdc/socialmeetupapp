//
//  meetupCell.h
//  Nationality
//
//  Created by webskitters on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface meetupCell : UITableViewCell
@property(nonatomic,weak)IBOutlet UIImageView *img_profile;
@property(nonatomic,weak)IBOutlet UILabel *lbl_mtup_frnd;
@property(nonatomic,weak)IBOutlet UILabel *lbl_added_ppl;
@property(nonatomic,weak)IBOutlet UILabel *lbl_no_of_ppl;
@property(nonatomic,weak)IBOutlet UILabel *lbl_date;
@property(nonatomic,weak)IBOutlet UILabel *lbl_time;
@property(nonatomic,weak)IBOutlet UIView *Vwcount;
@property(nonatomic,weak)IBOutlet UIView *meetupVw;
@end
