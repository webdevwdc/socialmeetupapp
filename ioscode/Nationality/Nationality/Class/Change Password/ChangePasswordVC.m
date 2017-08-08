//
//  ChangePasswordVC.m
//  Nationality
//
//  Created by WTS-MAC3052016 on 26/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "ChangePasswordVC.h"

@interface ChangePasswordVC ()

@end

@implementation ChangePasswordVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [Utility addTextFieldPadding:self.txtOldPassword];
    [Utility addTextFieldPadding:self.txtNewPassword];
    [Utility addTextFieldPadding:self.txtConfirmPassword];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma -mark btnBackAction
- (IBAction)btnBackAction:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)btnUpdateAction:(id)sender
{
    if (![Utility istextEmpty:_txtOldPassword withError:@" Enter Old Password"] && ![Utility istextEmpty:_txtNewPassword withError:@" Enter New Password"] && ![Utility istextEmpty:_txtConfirmPassword withError:@" Enter Confirm Password"])
        {
            if(![_txtNewPassword.text isEqualToString:_txtConfirmPassword.text])
            {
                [[AppController sharedappController] showAlert:@"New password mismatch with confirm password." viewController:self];
            }
            else
            {
                [self updatePassword];
            }
        }
}

#pragma -mark updatePassword
-(void)updatePassword
{
    NSMutableDictionary *httpParams = [NSMutableDictionary new];
    [httpParams setObject:[Utility getObjectForKey:USER_ID] forKey:@"id"];
    [httpParams setObject:self.txtOldPassword.text forKey:@"old_password"];
    [httpParams setObject:self.txtNewPassword.text forKey:@"new_password"];
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:HTTPS_CHANGE_PASSWORD getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             [self showAlert:[[dict st_dictionaryForKey:@"result"] st_stringForKey:@"message"]];
             
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];

}

#pragma -mark showAlert
-(void)showAlert : (NSString*)strMsg
{
    UIAlertController * alert=   [UIAlertController
                                  alertControllerWithTitle:@""
                                  message:strMsg
                                  preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction
                         actionWithTitle:@"OK"
                         style:UIAlertActionStyleDefault
                         handler:^(UIAlertAction * action)
                         {
                             [alert dismissViewControllerAnimated:YES completion:nil];
                             [self.navigationController popViewControllerAnimated:YES];
                         }];
    
    [alert addAction:ok];
    
    [self presentViewController:alert animated:YES completion:nil];
}


@end
