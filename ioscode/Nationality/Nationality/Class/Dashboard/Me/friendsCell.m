//
//  friendsCell.m
//  Nationality
//
//  Created by webskitters on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "friendsCell.h"

@implementation friendsCell


- (void)awakeFromNib {
    [super awakeFromNib];
    _img_frnds.layer.cornerRadius=self.img_frnds.frame.size.height/2;    
}
@end
