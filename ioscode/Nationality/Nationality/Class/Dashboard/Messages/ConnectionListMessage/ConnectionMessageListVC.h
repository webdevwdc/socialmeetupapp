//
//  ConnectionMessageListVC.h
//  Nationality
//
//  Created by webskitters on 28/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "BaseViewController.h"

@interface ConnectionMessageListVC : BaseViewController

@property (nonatomic, strong) IBOutlet UITableView *tblConnectionList;

- (IBAction)backToPrev:(id)sender;
@end
