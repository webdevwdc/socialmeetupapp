//
//  PrivacySettingsVC.m
//  Nationality
//
//  Created by WTS-MAC3052016 on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "PrivacySettingsVC.h"

@interface PrivacySettingsVC ()

@end

@implementation PrivacySettingsVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.milegeSlider.maximumValue= 75.0;
    self.milegeSlider.minimumValue = 0.0;
    self.milegeSlider.value = 50.0;
    self.butViewFindMe.selected = YES;
    self.butViewProfileContent.selected = YES;
    
    [self getSettingsValues];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma mark - Button Action


- (IBAction)milegeSliderValueChanged:(id)sender {
    
    self.lblSliderValue.text = [NSString stringWithFormat:@"%d",(int)self.milegeSlider.value];
}

- (IBAction)btnFindMeAction:(id)sender
{
    if([self.butViewFindMe isSelected])
    {
        self.butViewFindMe.selected = NO;
    }
    
    else
    {
        self.butViewFindMe.selected = YES;
    }
}

- (IBAction)btnProfileContentAction:(id)sender
{
    if([self.butViewProfileContent isSelected])
    {
        self.butViewProfileContent.selected = NO;
    }
    
    else
    {
        self.butViewProfileContent.selected = YES;
    }

}

- (IBAction)btnBulletinPostAction:(id)sender
{
    if([self.butViewBulletinPost isSelected])
    {
        self.butViewBulletinPost.selected = NO;
    }
    
    else
    {
        self.butViewBulletinPost.selected = YES;
    }

}

- (IBAction)butDoneAction:(id)sender {
    
    [self insertSettingsValues];
}




-(IBAction)backSettings:(id)sender{
    [self.navigationController popViewControllerAnimated:YES];
}


#pragma mark - Webservice Call

-(void)getSettingsValues{
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams geturl:[NSString stringWithFormat:@"%@%@",HTTPS_USER_SETTINGS_GET,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         if(status==0)
         {
             NSDictionary *dict=(NSDictionary*)data;
             
             [self setSettings:[dict st_dictionaryForKey:@"data"]];
             
         }
         else
         {
             
             NSString *str = (NSString*)data;
            self.butViewFindMe.selected = YES;
            self.butViewProfileContent.selected = YES;
         }
         
     }];

}

-(void)insertSettingsValues{    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    [httpParams setObject:[Utility getObjectForKey:USER_ID] forKey:@"user_id"];
    if (self.butViewFindMe.isSelected) {
       [httpParams setObject:@"Yes" forKey:@"is_anyone_find_me"];
    }
    else{
        [httpParams setObject:@"No" forKey:@"is_anyone_find_me"];
    }
    
    if (self.butViewProfileContent.isSelected) {
        [httpParams setObject:@"Yes" forKey:@"is_anyone_see_my_profile"];
    }
    else{
        [httpParams setObject:@"No" forKey:@"is_anyone_see_my_profile"];
    }

    


    [httpParams setObject:self.lblSliderValue.text forKey:@"mile"];
    
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams posturl:[NSString stringWithFormat:@"%@",HTTPS_USER_SETTINGS_INSERT] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         if(status==0)
         {
             NSDictionary *dict=(NSDictionary*)data;
             
             [[AppController sharedappController] showAlert:@"Successfully saved." viewController:self];

             
            

                          
         }
         else
         {
             NSString *str = (NSString*)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
         
     }];
    
}


-(void)setSettings:(NSDictionary *)dictData{
    if (!dictData) {
        return;
    }
    self.milegeSlider.value = [[dictData st_stringForKey:@"mile"] floatValue];
    self.lblSliderValue.text = [dictData st_stringForKey:@"mile"];
    
    if ([[dictData st_stringForKey:@"is_anyone_find_me"] isEqualToString:@"Yes"]) {
        self.butViewFindMe.selected = YES;
    }
    else{
        self.butViewFindMe.selected = NO;
    }
    
    if ([[dictData st_stringForKey:@"is_anyone_see_my_profile"] isEqualToString:@"Yes"]) {
        self.butViewProfileContent.selected = YES;
    }
    else{
        self.butViewProfileContent.selected = NO;
    }


    
}


@end
