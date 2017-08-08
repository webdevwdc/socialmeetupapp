//
//  SuggestionVC.h
//  Nationality
//
//  Created by WTS-MAC3052016 on 25/05/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SuggestionVC : UIViewController
@property (weak, nonatomic) IBOutlet UITextView *textViewSuggestion;

- (IBAction)btnBackAction:(id)sender;
- (IBAction)btnSubmitAction:(id)sender;
@end
