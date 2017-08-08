//
//  bulletinCell.m
//  Nationality
//
//  Created by webskitters on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "bulletinCell.h"

@implementation bulletinCell{
    
}

- (void)awakeFromNib {
    [super awakeFromNib];
    _bulletinVw.layer.borderWidth=0.5;
    _bulletinVw.layer.borderColor=[Utility getColorFromHexString:@"#9BC531"].CGColor;
    _height=self.frame.size.height;
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}
+(CGFloat)height{
   
    
    return 180;
}
@end
