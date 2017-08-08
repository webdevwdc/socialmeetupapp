//
//  DropDownListView.h
//  tales2content_new
//
//  Created by debika mondal on 25/11/15.
//  Copyright (c) 2015 debika mondal. All rights reserved.
//

#import <UIKit/UIKit.h>
@protocol kDropDownListViewDelegate;
@interface DropDownListView : UIView<UITableViewDataSource,UITableViewDelegate>{
    UITableViewCell *cell;
    UITableView *_kTableView;
    NSString *_kTitleText;
    NSArray *_kDropDownOption;
    CGFloat R,G,B,A;
    BOOL isMultipleSelection;
}
/////added by debika
//typedef BOOL (^DropdownCustomBlock)(NSInteger status, NSObject *data);
//@property (nonatomic, copy) DropdownCustomBlock callBack;
/////added

@property(nonatomic,strong)NSMutableArray *arryData;
@property (nonatomic, assign) id<kDropDownListViewDelegate> delegate;
- (void)fadeOut;
- (id)initWithTitle:(NSString *)aTitle options:(NSArray *)aOptions xy:(CGPoint)point size:(CGSize)size isMultiple:(BOOL)isMultiple;
// If animated is YES, PopListView will be appeared with FadeIn effect.
- (void)showInView:(UIView *)aView animated:(BOOL)animated;
-(void)SetBackGroundDropDown_R:(CGFloat)r G:(CGFloat)g B:(CGFloat)b alpha:(CGFloat)alph;
@end

@protocol kDropDownListViewDelegate <NSObject>
- (void)DropDownListView:(DropDownListView *)dropdownListView didSelectedIndex:(NSInteger)anIndex;
- (void)DropDownListView:(DropDownListView *)dropdownListView Datalist:(NSMutableArray*)ArryData;
- (void)DropDownListViewDidCancel;
@end

