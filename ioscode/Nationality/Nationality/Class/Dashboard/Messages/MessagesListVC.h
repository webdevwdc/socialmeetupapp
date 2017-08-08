//
//  MessagesListVC.h
//  Nationality
//
//  Created by webskitters on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MessagesListVC : BaseViewController<UITableViewDataSource,UITableViewDelegate>


@property (weak, nonatomic) IBOutlet UISearchBar *messageListSearchBar;

@property (weak, nonatomic) IBOutlet UITableView *tblMessgeList;

- (IBAction)createMessageWithUser:(id)sender;


@end
