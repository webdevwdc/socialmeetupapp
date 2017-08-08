//
//  currentLocation.h
//  Nationality
//
//  Created by webskitters on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface currentLocation: UIView<GMSMapViewDelegate>

@property (assign, nonatomic)  double latitude;
@property (assign, nonatomic) double longitude;

@property (weak, nonatomic) IBOutlet UIView *mpView;



@end
