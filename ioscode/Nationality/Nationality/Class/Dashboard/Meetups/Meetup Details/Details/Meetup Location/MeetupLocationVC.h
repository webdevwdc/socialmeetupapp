//
//  MeetupLocationVC.h
//  Nationality
//
//  Created by webskitters on 12/05/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <GoogleMaps/GoogleMaps.h>


@interface MeetupLocationVC : UIViewController<GMSMapViewDelegate>

@property (assign, nonatomic)  double latitude;
@property (assign, nonatomic) double longitude;
@property (weak, nonatomic) IBOutlet UIView *mpView;

- (IBAction)btnBackAction:(id)sender;

@end
