//
//  CMSVC.m
//  Nationality
//
//  Created by WTS-MAC3052016 on 04/05/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "CMSVC.h"

@interface CMSVC ()
{
    NSString *strServiceURL;
}

@end

@implementation CMSVC

- (void)viewDidLoad {
    [super viewDidLoad];
    if([self.strType isEqualToString:@"privacy-policy"])
    {
      strServiceURL = [NSString stringWithFormat:@"%@%@",HTTPS_CMS_CONTENT,self.strType];
    }
    
    else if ([self.strType isEqualToString:@"terms-of-services"])
    {
       strServiceURL = [NSString stringWithFormat:@"%@%@",HTTPS_CMS_CONTENT,self.strType];
    }
    
    [self getContent];
    // Do any additional setup after loading the view.
}


#pragma mark - WebService
-(void)getContent{
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams geturl:strServiceURL getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             NSDictionary *dict = (NSDictionary*)data;
             [self updateUI:dict];
        }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
     }];
    
}

#pragma -mark updateUI
-(void)updateUI : (NSDictionary*)dictDetails
{
    self.lblHeader.text = [[dictDetails st_dictionaryForKey:@"data"] st_stringForKey:@"cms_title"];
    self.textViewContent.text = [[dictDetails st_dictionaryForKey:@"data"] st_stringForKey:@"cms_desc"];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (IBAction)btnBackAction:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}
@end
