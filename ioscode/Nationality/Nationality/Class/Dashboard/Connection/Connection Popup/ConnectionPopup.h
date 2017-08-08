//
//  ConnectionPopup.h
//  Nationality
//
//  Created by webskitters on 26/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ConnectionPopup : UIView
@property (weak, nonatomic) IBOutlet UISearchBar *FriendSearchBar;
@property (weak, nonatomic) IBOutlet UITableView *tblSuggestedFriendList;
@property (weak, nonatomic) IBOutlet UIButton *btnDissappear;
@property (weak, nonatomic) IBOutlet UIView *contentView;

@end
