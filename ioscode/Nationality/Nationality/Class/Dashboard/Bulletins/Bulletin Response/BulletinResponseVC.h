//
//  BulletinResponseVC.h
//  Nationality
//
//  Created by WTS-MAC3052016 on 07/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface BulletinResponseVC : BaseViewController <UITextViewDelegate>

@property (weak, nonatomic) IBOutlet UILabel *lblDate;
@property (weak, nonatomic) IBOutlet UILabel *lblTime;
@property (weak, nonatomic) IBOutlet UILabel *lblMainComment;
@property (weak, nonatomic) IBOutlet UITextView *txtViewComment;
@property (weak, nonatomic) IBOutlet UILabel *lblBulletinTitle;

@property (nonatomic, strong) NSMutableDictionary *dictBulletinData;
- (IBAction)btnActionPost:(id)sender;
- (IBAction)btnBack:(id)sender;

@end
