//
//  AttendeesCell.h
//  Nationality
//
//  Created by webskitters on 19/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AttendeesTableCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *imgViewAttendees1;
@property (weak, nonatomic) IBOutlet UIImageView *imgViewAttendees2;
@property (weak, nonatomic) IBOutlet UIImageView *imgViewAttendees3;
@property (weak, nonatomic) IBOutlet UIImageView *imgViewAttendees4;

@property (weak, nonatomic) IBOutlet UILabel *lblAttendees1;
@property (weak, nonatomic) IBOutlet UILabel *lblAttendees2;
@property (weak, nonatomic) IBOutlet UILabel *lblAttendees3;
@property (weak, nonatomic) IBOutlet UILabel *lblAttendees4;

@property (weak, nonatomic) IBOutlet UIButton *btnAttendees1;
@property (weak, nonatomic) IBOutlet UIButton *btnAttendees2;
@property (weak, nonatomic) IBOutlet UIButton *btnAttendees3;
@property (weak, nonatomic) IBOutlet UIButton *btnAttendees4;

@end
