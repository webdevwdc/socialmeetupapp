//
//  MessagesCell.h
//  Nationality
//
//  Created by User on 11/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MessagesCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UILabel *lblUserName;
@property (weak, nonatomic) IBOutlet UIView *viewMessageReceiver;
@property (weak, nonatomic) IBOutlet UIView *viewMessageSender;
@property (weak, nonatomic) IBOutlet UILabel *lblMessageTextRceiver;
@property (weak, nonatomic) IBOutlet UILabel *lblMessageTestSender;
@property (weak, nonatomic) IBOutlet UILabel *lblDate;
@property (weak, nonatomic) IBOutlet UILabel *lblTime;
@property (weak, nonatomic) IBOutlet UIImageView *imageNodeReceiver;
@property (weak, nonatomic) IBOutlet UIImageView *imageNodeSender;
@end
