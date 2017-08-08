//
//  ConnectionVC.h
//  Nationality
//
//  Created by webskitters on 04/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ConnectionPopup.h"

@interface ConnectionVC : BaseViewController<UITextFieldDelegate,UITableViewDataSource,UITableViewDelegate,UISearchBarDelegate>


@property (weak, nonatomic) IBOutlet UILabel *lblFrndRequestCount;
@property (weak, nonatomic) IBOutlet UITableView *tblConnectionList;
@property (weak, nonatomic) IBOutlet UISearchBar *frndSearchBox;


@property (strong,nonatomic) ConnectionPopup *connectionPopupView;

@property (strong , nonatomic) NSArray *arrContacts;
@property (strong , nonatomic) NSArray *arrSearchList;
@property (nonatomic,retain)NSMutableArray *keyArray;


- (IBAction)btnAddFrndAction:(id)sender;
- (IBAction)btnFrndRequestAction:(id)sender;



@end
