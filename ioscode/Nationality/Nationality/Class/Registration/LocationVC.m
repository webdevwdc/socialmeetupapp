//
//  LocationVC.m
//  Nationality
//
//  Created by webskitters on 12/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "LocationVC.h"

@interface LocationVC (){
     GMSPlacesClient *_placesClient;
}

@end

@implementation LocationVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    _placesClient = [GMSPlacesClient sharedClient];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
//- (IBAction)onLaunchClicked:(id)sender {
//    GMSAutocompleteViewController *acController = [[GMSAutocompleteViewController alloc] init];
//    acController.delegate = self;
//    [self presentViewController:acController animated:YES completion:nil];
//}
//
//// Handle the user's selection.
//- (void)viewController:(GMSAutocompleteViewController *)viewController
//didAutocompleteWithPlace:(GMSPlace *)place {
//    // Do something with the selected place.
//    NSLog(@"Place name %@", place.name);
//    NSLog(@"Place address %@", place.formattedAddress);
//    NSLog(@"Place attributions %@", place.attributions.string);
//    [self dismissViewControllerAnimated:YES completion:nil];
//}
//
//- (void)viewController:(GMSAutocompleteViewController *)viewController
//didFailAutocompleteWithError:(NSError *)error {
//    // TODO: handle the error.
//    NSLog(@"error: %ld", [error code]);
//    [self dismissViewControllerAnimated:YES completion:nil];
//}
//
//// User canceled the operation.
//- (void)wasCancelled:(GMSAutocompleteViewController *)viewController {
//    NSLog(@"Autocomplete was cancelled.");
//    [self dismissViewControllerAnimated:YES completion:nil];
//}

- (IBAction)getCurrentPlace:(UIButton *)sender {
    [_placesClient currentPlaceWithCallback:^(GMSPlaceLikelihoodList *placeLikelihoodList, NSError *error){
        if (error != nil) {
            NSLog(@"Pick Place error %@", [error localizedDescription]);
            return;
        }
        
        self.nameLabel.text = @"No current place";
        self.addressLabel.text = @"";
        
        if (placeLikelihoodList != nil) {
            GMSPlace *place = [[[placeLikelihoodList likelihoods] firstObject] place];
            if (place != nil) {
                self.nameLabel.text = place.name;
                self.addressLabel.text = [[place.formattedAddress componentsSeparatedByString:@", "]
                                          componentsJoinedByString:@"\n"];
            }
        }
    }];
}
- (IBAction)pickPlace:(UIButton *)sender {
    
    CLLocationCoordinate2D center = CLLocationCoordinate2DMake(37.788204, -122.411937);
    CLLocationCoordinate2D northEast = CLLocationCoordinate2DMake(center.latitude + 0.001,
                                                                  center.longitude + 0.001);
    CLLocationCoordinate2D southWest = CLLocationCoordinate2DMake(center.latitude - 0.001,
                                                                  center.longitude - 0.001);
    GMSCoordinateBounds *viewport = [[GMSCoordinateBounds alloc] initWithCoordinate:northEast
                                                                         coordinate:southWest];
    GMSPlacePickerConfig *config = [[GMSPlacePickerConfig alloc] initWithViewport:viewport];
    placePicker = [[GMSPlacePicker alloc] initWithConfig:config];
    
    [placePicker pickPlaceWithCallback:^(GMSPlace *place, NSError *error) {
        if (error != nil) {
            NSLog(@"Pick Place error %@", [error localizedDescription]);
            return;
        }
        
        if (place != nil) {
            self.nameLabel.text = place.name;
            self.addressLabel.text = [[place.formattedAddress
                                       componentsSeparatedByString:@", "] componentsJoinedByString:@"\n"];
        } else {
            self.nameLabel.text = @"No place selected";
            self.addressLabel.text = @"";
        }
    }];
}
@end
