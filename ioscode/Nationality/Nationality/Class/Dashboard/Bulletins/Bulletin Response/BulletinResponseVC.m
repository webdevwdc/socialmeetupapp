//
//  BulletinResponseVC.m
//  Nationality
//
//  Created by WTS-MAC3052016 on 07/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "BulletinResponseVC.h"

@interface BulletinResponseVC ()

@end

@implementation BulletinResponseVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self setDataForComment];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)setDataForComment{
    _lblBulletinTitle.text = [_dictBulletinData st_stringForKey:@"bulletin_title"];
    _lblMainComment.text = [_dictBulletinData st_stringForKey:@"content"];
    _lblDate.text = [Utility getFormatedForDate:[_dictBulletinData st_stringForKey:@"created_at"]];
    _lblTime.text = [Utility getFormatedForTime:[_dictBulletinData st_stringForKey:@"created_at"]];
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

//- (BOOL)textViewShouldEndEditing:(UITextView *)textView
//{
//    if(textView.text.length == 0)
//    {
//        textView.text = @"Your content goes here";
//    }
//    
//    return YES;
//}

- (IBAction)btnActionPost:(id)sender {
   
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    
    [httpParams setObject:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
    [httpParams setObject:[_dictBulletinData st_stringForKey:@"parent_bulletin_id"] forKey:@"parent_bulletin_id"];
    if (![Utility istextViewEmpty:_txtViewComment withError:@""]){
        
        [httpParams setObject:_txtViewComment.text forKey:@"comment"];
        
        [self postBulletinToServer:httpParams];
        
    }
}

- (IBAction)btnBack:(id)sender {
    
    [self.navigationController popViewControllerAnimated:YES];
}

-(void)postBulletinToServer:(NSMutableDictionary *)param{
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:param posturl:[NSString stringWithFormat:@"%@%@",HTTPS_BULLETIN_COMMENT,[_dictBulletinData st_stringForKey:@"bulletin_id"]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             [self btnBack:nil];
         }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
    
}
@end
