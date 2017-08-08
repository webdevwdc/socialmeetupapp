//
//  AllBulletinVC.h
//  Nationality
//
//  Created by WTS-MAC3052016 on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AllBulletinVC : BaseViewController
- (IBAction)btnCreateNewBulletinAction:(id)sender;
- (IBAction)btnMyBulletinAction:(id)sender;
- (IBAction)filterBulletinAction:(id)sender;



@property (weak, nonatomic) IBOutlet UISearchBar *searchBar;
@property (weak, nonatomic) IBOutlet UITableView *tableBulletin;


@property (strong , nonatomic) NSArray *arrAllBulletin;

@end
