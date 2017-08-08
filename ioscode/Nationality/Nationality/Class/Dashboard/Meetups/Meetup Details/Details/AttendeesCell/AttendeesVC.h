//
//  AttendeesVC.h
//  Nationality
//
//  Created by webskitters on 19/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AttendeesVC : UIViewController
@property (weak, nonatomic) IBOutlet UITableView *tblAllAttendees;
@property(strong,nonatomic)NSMutableArray *arr_attendees_list;
@property(strong,nonatomic)NSString *header_status;
@property (weak, nonatomic) IBOutlet UILabel *lblHeaderTitle;
@property (assign) BOOL isFromMeetupDetails;
@end
