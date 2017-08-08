//
//  meetupCell.m
//  Nationality
//
//  Created by webskitters on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "meetupCell.h"

@implementation meetupCell

- (void)awakeFromNib {
    [super awakeFromNib];
    _meetupVw.layer.borderWidth=0.5;
    _meetupVw.layer.borderColor=[Utility getColorFromHexString:@"#9BC531"].CGColor;
     _Vwcount.layer.cornerRadius=_Vwcount.frame.size.height/2;
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
