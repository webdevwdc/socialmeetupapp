//
//  SuggestionVC.m
//  Nationality
//
//  Created by WTS-MAC3052016 on 25/05/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "SuggestionVC.h"

@interface SuggestionVC ()

@end

@implementation SuggestionVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma -mark btnBackAction
- (IBAction)btnBackAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma -mark btnSubmitAction
- (IBAction)btnSubmitAction:(id)sender
{
    self.textViewSuggestion.text = [self.textViewSuggestion.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    
    if (self.textViewSuggestion.text.length == 0 || [self.textViewSuggestion.text isEqualToString:@"Leave suggestions, comments, or glitches found. Leave your information if it’s okay for us to follow up with you!"])
    {
        [[AppController sharedappController] showAlert:@"Please input suggestion text." viewController:self];
    }
    
    else
    {
        [self postSuggestionText];
    }

}

#pragma -mark showThankyouPopup
-(void)showThankyouPopup
{
    UIAlertController * alert = [UIAlertController
                                 alertControllerWithTitle:@""
                                 message:@"Thank you!"
                                 preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* ok = [UIAlertAction
                         actionWithTitle:@"Okay"
                         style:UIAlertActionStyleDefault
                         handler:^(UIAlertAction * action)
                         {
                             [self postSuggestionText];
                             
                         }];
    
    [alert addAction:ok];
    
    [self presentViewController:alert animated:YES completion:nil];
}

#pragma -mark reportPost
-(void)postSuggestionText{
    
    NSMutableDictionary *httpParams = [NSMutableDictionary new];
    [httpParams setObject:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
    [httpParams setObject:self.textViewSuggestion.text forKey:@"comment"];
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:HTTPS_SUGGESTION_POST getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             [self showAlert:@"Thank you!"];
             
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
    
    UIAlertController * alert = [UIAlertController
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



#pragma -mark Text View Delegate
- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text{
    
    if([text isEqualToString:@"\n"]) {
        [textView resignFirstResponder];
        
        return NO;
    }
    
    return YES;
}


- (BOOL) textViewShouldBeginEditing:(UITextView *)textView
{
    if ([textView.text  isEqualToString:@"Leave suggestions, comments, or glitches found. Leave your information if it’s okay for us to follow up with you!"])
    {
        textView.text=@"";
    }

    return YES;
}

@end
