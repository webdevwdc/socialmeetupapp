//
//  ChangePasswordVC.h
//  Nationality
//
//  Created by WTS-MAC3052016 on 26/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ChangePasswordVC : UIViewController
@property (weak, nonatomic) IBOutlet UITextField *txtOldPassword;
@property (weak, nonatomic) IBOutlet UITextField *txtNewPassword;
@property (weak, nonatomic) IBOutlet UITextField *txtConfirmPassword;

- (IBAction)btnBackAction:(id)sender;
- (IBAction)btnUpdateAction:(id)sender;
@end
