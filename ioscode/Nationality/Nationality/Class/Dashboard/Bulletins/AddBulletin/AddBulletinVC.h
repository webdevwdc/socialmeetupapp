//
//  AddBulletinVC.h
//  Nationality
//
//  Created by WTS-MAC3052016 on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AddBulletinVC : BaseViewController

@property (weak, nonatomic) IBOutlet UITextField *txtTitle;
@property (weak, nonatomic) IBOutlet UITextView *txtViewContent;
@property (weak, nonatomic) IBOutlet UILabel *lblStatus;
@property (weak, nonatomic) IBOutlet UIButton *btnStatus;
@property (weak, nonatomic) IBOutlet UILabel *lblTitle;
@property (nonatomic, strong) NSMutableDictionary *dictBulletin;
@property BOOL isEdit;
@property (strong, nonatomic) IBOutlet UIImageView *imagePublicPrivate;

- (IBAction)btnPrivateAction:(id)sender;
- (IBAction)btnPostAction:(id)sender;
@end
