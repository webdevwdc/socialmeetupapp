//
//  MessagesVC.h
//  Nationality
//
//  Created by webskitters on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MessagesVC : BaseViewController


@property (weak, nonatomic) IBOutlet UITableView *tableMessage;

@property (weak, nonatomic) IBOutlet UIView *viewMessage;
@property (weak, nonatomic) IBOutlet UITextView *txtVwMessage;

@property (strong,nonatomic) NSMutableArray *arr_message;
@property(strong, nonatomic) FIRAuthStateDidChangeListenerHandle handle;
@property (strong, nonatomic) FIRDatabaseReference *ref;
@property (nonatomic, strong) NSString *strReceiverId;
@property (nonatomic, strong) NSString *strId;
@property (nonatomic, strong) NSString *strUniqueId;
@property (weak, nonatomic) IBOutlet UIButton *btnSend;
@property BOOL isList;
@property (nonatomic, strong) NSString *toUserID;
@property (nonatomic, strong) NSString *fromUserID;
@property (nonatomic, strong) NSString *badge_count;
- (IBAction)sendMessageAction:(id)sender;
- (IBAction)backToPrev:(id)sender;
@end
