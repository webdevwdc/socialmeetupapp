//
//  MeetupsVC.h
//  Nationality
//
//  Created by webskitters on 04/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface MeetupsVC : BaseViewController<UITableViewDataSource,UITableViewDelegate,UITextFieldDelegate,UISearchBarDelegate>
@property (weak, nonatomic) IBOutlet UITableView *tblMeetups;
@property (weak, nonatomic) IBOutlet UISearchBar *meetupSearchBar;
- (IBAction)btnAddToMeetupAction:(id)sender;
- (IBAction)btnMeetupsFilterAction:(id)sender;
@property (weak, nonatomic) IBOutlet UILabel *lblMeetupRequest;
@end
