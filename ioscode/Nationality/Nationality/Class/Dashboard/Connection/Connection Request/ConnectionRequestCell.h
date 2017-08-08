//
//  ConnectionRequestCell.h
//  Nationality
//
//  Created by WTS-MAC3052016 on 07/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ConnectionRequestCell : UITableViewCell
{
    
}
@property (weak, nonatomic) IBOutlet UIImageView *profileImage;
@property (weak, nonatomic) IBOutlet UILabel *lblName;
@property (weak, nonatomic) IBOutlet UILabel *lblCity;
@property (weak, nonatomic) IBOutlet UIButton *butViewAccept;
@property (weak, nonatomic) IBOutlet UIButton *butViewReject;
@property (weak, nonatomic) IBOutlet UIButton *butViewBlock;

@end
