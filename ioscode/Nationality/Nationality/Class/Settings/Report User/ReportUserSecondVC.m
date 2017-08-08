//
//  ReportUserSecondVC.m
//  Nationality
//
//  Created by WTS-MAC3052016 on 17/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "ReportUserSecondVC.h"

@interface ReportUserSecondVC ()

@end

@implementation ReportUserSecondVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    self.lblCity.text = self.strCity;
    self.lblName.text = self.strName;
    [Utility loadCellImage:self.profileImage imageUrl:_strImageLink];
    
     [Utility loadCellImage:self.profileImage imageUrl:[NSString stringWithFormat:@"%@%@",THUMB_IMAGE,_strImageLink]];
    //self.profileImage.image =
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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
    return YES;
}


- (IBAction)btnBackAction:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)btnSubmitAction:(id)sender
{
    self.textView.text = [self.textView.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
   
    if (self.textView.text.length == 0)
    {
        [[AppController sharedappController] showAlert:@"Please input report text" viewController:self];
    }
    
    else
    {
        [self reportPost];
    }

}

#pragma -mark reportPost
-(void)reportPost{
    
    NSMutableDictionary *httpParams = [NSMutableDictionary new];
    [httpParams setObject:[Utility getObjectForKey:USER_ID] forKey:@"from_user_id"];
    [httpParams setObject:self.strReportUserId forKey:@"to_user_id"];
    [httpParams setObject:self.textView.text forKey:@"comment"];
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:HTTPS_REPORT_POST getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             [self showAlert:@"Your report has been post"];
             
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

@end
