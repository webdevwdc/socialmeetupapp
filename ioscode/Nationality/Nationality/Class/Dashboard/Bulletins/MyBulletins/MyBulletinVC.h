//
//  MyBulletinVC.h
//  Nationality
//
//  Created by WTS-MAC3052016 on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MyBulletinVC : BaseViewController
@property (weak, nonatomic) IBOutlet UITableView *tableMyBulletin;


@property (nonatomic, strong) NSArray *arrMyBulletin;

- (IBAction)backToPrev:(id)sender;

@end
