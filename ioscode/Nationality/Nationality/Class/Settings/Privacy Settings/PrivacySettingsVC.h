//
//  PrivacySettingsVC.h
//  Nationality
//
//  Created by WTS-MAC3052016 on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface PrivacySettingsVC : BaseViewController
{
    
}

@property (weak, nonatomic) IBOutlet UIButton *butViewFindMe;
@property (weak, nonatomic) IBOutlet UIButton *butViewProfileContent;
@property (weak, nonatomic) IBOutlet UIButton *butViewBulletinPost;
@property (weak, nonatomic) IBOutlet UISlider *milegeSlider;
@property (weak, nonatomic) IBOutlet UILabel *lblSliderValue;

- (IBAction)milegeSliderValueChanged:(id)sender;

- (IBAction)btnFindMeAction:(id)sender;
- (IBAction)btnProfileContentAction:(id)sender;
- (IBAction)btnBulletinPostAction:(id)sender;
- (IBAction)butDoneAction:(id)sender;

@end
