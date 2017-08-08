//
//  CMSVC.h
//  Nationality
//
//  Created by WTS-MAC3052016 on 04/05/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CMSVC : UIViewController

@property (strong,nonatomic) NSString *strType;
- (IBAction)btnBackAction:(id)sender;
@property (weak, nonatomic) IBOutlet UILabel *lblHeader;
@property (weak, nonatomic) IBOutlet UITextView *textViewContent;

@end
