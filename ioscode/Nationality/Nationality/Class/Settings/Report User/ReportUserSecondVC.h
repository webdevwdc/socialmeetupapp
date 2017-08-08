//
//  ReportUserSecondVC.h
//  Nationality
//
//  Created by WTS-MAC3052016 on 17/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ReportUserSecondVC : UIViewController


@property (weak, nonatomic) IBOutlet UIImageView *profileImage;
@property (weak, nonatomic) IBOutlet UILabel *lblName;
@property (weak, nonatomic) IBOutlet UILabel *lblCity;
@property (weak, nonatomic) IBOutlet UITextView *textView;

@property (nonatomic,strong) NSString *strReportUserId;
@property (nonatomic,strong) NSString *strName;
@property (nonatomic,strong) NSString *strCity;
@property (nonatomic,strong) NSString *strImageLink;

- (IBAction)btnBackAction:(id)sender;
- (IBAction)btnSubmitAction:(id)sender;
@end
