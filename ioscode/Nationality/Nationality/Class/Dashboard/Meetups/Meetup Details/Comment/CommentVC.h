//
//  CommentVC.h
//  Nationality
//
//  Created by webskitters on 20/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "TPKeyboardAvoidingScrollView.h"
@interface CommentVC : BaseViewController{
    CGRect kb;
    CGRect af;
    CGSize svos;
    CGPoint point;
}
@property(nonatomic,weak)IBOutlet UITableView *tbl_comment;
@property(nonatomic,strong)NSMutableArray *arr_comment;
@property(nonatomic,strong)IBOutlet TPKeyboardAvoidingScrollView* scrollViewAddComment;
@property(nonatomic,strong)IBOutlet UITextField *txtComment;
@property(nonatomic,strong)IBOutlet UIView *commentVw;
@property(nonatomic,strong)IBOutlet UILabel *lblReplyMeetup;
@property(nonatomic,strong)NSString *mtupId;
@property(nonatomic,strong)NSString *reply;
@end
