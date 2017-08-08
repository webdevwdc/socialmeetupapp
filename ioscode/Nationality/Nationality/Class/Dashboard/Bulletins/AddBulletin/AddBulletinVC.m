//
//  AddBulletinVC.m
//  Nationality
//
//  Created by WTS-MAC3052016 on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "AddBulletinVC.h"

@interface AddBulletinVC ()

@end

@implementation AddBulletinVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [Utility addTextFieldPadding:_txtTitle];
    UIToolbar* keyboardDoneButtonView = [[UIToolbar alloc] init];
    [keyboardDoneButtonView sizeToFit];
    UIBarButtonItem* doneButton = [[UIBarButtonItem alloc] initWithTitle:@"Done"
                                                                   style:UIBarButtonItemStyleBordered target:self
                                                                  action:@selector(doneClicked:)];
    [keyboardDoneButtonView setItems:[NSArray arrayWithObjects:doneButton, nil]];
    _txtViewContent.inputAccessoryView = keyboardDoneButtonView;
    
    if (_isEdit) {
        [self setBulletinDetails];
    }
}
-(void)setBulletinDetails{
    _lblTitle.text = @"Edit Bulletin";
    _txtTitle.text = [_dictBulletin st_stringForKey:@"title"];
    _txtViewContent.text = [_dictBulletin st_stringForKey:@"content"];
    _lblStatus.text = [NSString stringWithFormat:@"Posting as %@?",[_dictBulletin st_stringForKey:@"type"]];
    if ([[_dictBulletin st_stringForKey:@"type"] isEqualToString:@"public"]) {
        [_btnStatus setSelected:YES];
    }
}
-(void)doneClicked:(id)sender
{
    NSLog(@"Done Clicked.");
    if ([_txtViewContent.text isEqualToString:@""]) {
        _txtViewContent.text=@"Your Content goes here";
    }
    [self.view endEditing:YES];
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
    if([textView.text isEqualToString:@"Your content goes here"])
        textView.text = @"";
    textView.textColor = [UIColor blackColor];
    textView.layer.borderColor = [UIColor blackColor].CGColor;
    return YES;
}

#pragma Text Field Delegate

- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField{
    
    return YES;
}
- (void)textFieldDidBeginEditing:(UITextField *)textField
{
   
}
- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    
    return YES;
}


#pragma -mark btnPrivateAction
- (IBAction)btnPrivateAction:(UIButton *)sender
{
    if (sender.selected) {
        [sender setSelected:NO];
        _lblStatus.text = @"Posting as public?";
        self.imagePublicPrivate.image = [UIImage imageNamed:@"public"];
    }
    else{
        [sender setSelected:YES];
        _lblStatus.text = @"Posting as private?";
        self.imagePublicPrivate.image = [UIImage imageNamed:@"private"];
    }
    
    
}

#pragma -mark btnPostAction
- (IBAction)btnPostAction:(UIButton *)sender
{
    
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    
//    if ([_txtViewContent.text isEqualToString:@"Your content goes here"]) {
//        _txtViewContent.text=@"";
//    }
    if (![Utility istextEmpty:_txtTitle withError:@" ENTER BULLETIN TITLE/TOPIC"] && ![Utility istextViewEmpty:_txtViewContent withError:@"Your content goes here"]){
        [httpParams setObject:_txtTitle.text forKey:@"bulletin_title"];
        [httpParams setObject:_txtViewContent.text forKey:@"bulletin_content"];
        [httpParams setObject:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
        [httpParams setObject:_btnStatus.selected == YES?@"public":@"private" forKey:@"bulletin_type"];
        [httpParams setObject:[[AppController sharedappController].formatDateSendServer stringFromDate:[NSDate date]] forKey:@"bulletin_date_time"];
        [self postBulletinToServer:httpParams];
        
    }
    
    
}

-(void)postBulletinToServer:(NSMutableDictionary *)httpParams{
    if (_isEdit) {
        
        [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams puturl:[NSString stringWithFormat:@"%@%@",HTTPS_BULLETIN_CLICKED,[_dictBulletin st_stringForKey:@"id"]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
         {
             
             if(status==0)
             {
                 NSDictionary *dict = (NSDictionary*)data;
                 [self btnback:nil];
             }
             
             else
             {
                 NSString *str = (NSString *)data;
                 [[AppController sharedappController] showAlert:str viewController:self];
             }
         }];
    }
    else {
        [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:HTTPS_BULLETIN_ADD getServiceDataCallBack:^(NSInteger status, NSObject *data)
         {
             
             if(status==0)
             {
                 NSDictionary *dict = (NSDictionary*)data;
                 [self btnback:nil];
             }
             
             else
             {
                 NSString *str = (NSString *)data;
                 [[AppController sharedappController] showAlert:str viewController:self];
             }
         }];
        
    }
}

-(IBAction)btnback:(id)sender{
    [self.navigationController popViewControllerAnimated:YES];
}
@end
