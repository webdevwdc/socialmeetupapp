//
//  UserSettingsVC.m
//  Nationality
//
//  Created by WTS-MAC3052016 on 05/04/17.
//  Copyright © 2017 webskitters. All rights reserved.
//

#import "UserSettingsVC.h"
#import "UserSettingsCell.h"
#import "PrivacySettingsVC.h"
#import "LoginVC.h"
#import "ReportUserFirstVC.h"
#import "EditProfileVC.h"
#import "ChangePasswordVC.h"
#import "BlockingVC.h"
#import "CMSVC.h"
#import "InviteFacebookFriendsVC.h"
#import "SuggestionVC.h"
@interface UserSettingsVC ()
{
    NSArray *arrImages;
    NSArray *arrTitles;
    NSArray * arrFriendList;
    
    
    FBSDKLoginManager *login;
}

@end

@implementation UserSettingsVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    if ([[Utility getObjectForKey:REGISTRATION_TYPE] isEqualToString:@"Fb"]) {
        arrImages = [NSArray arrayWithObjects:@"edit_profile",@"privacy_settings",@"Blocking",@"inviteFB",@"suggestions",@"report_user",@"privacy_policy",@"terms_service",@"logout", nil];
        
        arrTitles = [NSArray arrayWithObjects:@"Edit Your Profile",@"Privacy Settings",@"Blocking",@"Invite Facebook Friends",@"Suggestions",@"Report User",@"Privacy Policy",@"Terms Of Service",@"Logout", nil];
    }
    else{
        arrImages = [NSArray arrayWithObjects:@"change_password",@"edit_profile",@"privacy_settings",@"Blocking",@"inviteFB",@"suggestions",@"report_user",@"privacy_policy",@"terms_service",@"logout", nil];
        
        arrTitles = [NSArray arrayWithObjects:@"Change Password",@"Edit Your Profile",@"Privacy Settings",@"Blocking",@"Invite Facebook Friends",@"Suggestions",@"Report User",@"Privacy Policy",@"Terms Of Service",@"Logout", nil];
    }
   // arrImages = [NSArray arrayWithObjects:@"change_password",@"edit_profile",@"privacy_settings",@"Blocking",@"inviteFB",@"report_user",@"privacy_policy",@"terms_service",@"logout", nil];
    
   // arrTitles = [NSArray arrayWithObjects:@"Change Password",@"Edit Your Profile",@"Privacy Settings",@"Blocking",@"Invite Facebook Friends",@"Report User",@"Privacy Policy",@"Terms Of Service",@"Logout", nil];
}

#pragma --
#pragma mark - TableView Delegate And Data Source
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return arrTitles.count;
}
- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *MyIdentifier = @"UserSettingsCell";
    
    UserSettingsCell *cell = (UserSettingsCell*)[tableView dequeueReusableCellWithIdentifier:MyIdentifier];
    cell.lblTitle.text = [arrTitles objectAtIndex:indexPath.row];
    cell.imageView.image = [UIImage imageNamed:[arrImages objectAtIndex:indexPath.row]];
    
    if(indexPath.row % 2 == 0)
    {
        cell.backgroundColor = [UIColor colorWithRed:234.0/255.0 green:234.0/255.0 blue:234.0/255.0 alpha:1.0];
    }
    
    else
    {
        cell.backgroundColor = [UIColor whiteColor];
    }
    
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    return cell;
}



- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    return 48.0 * hRatio;                  
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (![[Utility getObjectForKey:REGISTRATION_TYPE] isEqualToString:@"Fb"]) {
        
        switch (indexPath.row) {
            case 0:            {
                ChangePasswordVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"ChangePasswordVC"];
                [self.navigationController pushViewController:vc animated:YES];
            }
                
                break;
                
            case 1: {
                EditProfileVC *edit=[self.storyboard instantiateViewControllerWithIdentifier:@"EditProfileVC"];
                [self.navigationController pushViewController:edit animated:YES];
            }
                
                break;
                
            case 2:
            {
                PrivacySettingsVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"PrivacySettingsVC"];
                [self.navigationController pushViewController:vc animated:YES];
            }
                
                break;
                
            case 3:
            {
                BlockingVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"BlockingVC"];
                [self.navigationController pushViewController:vc animated:YES];
            }
                
                break;
                
            case 4:  {
                
                if([[Utility getObjectForKey:@"is_Facebook_User"] boolValue] == YES){
                    
                    FBSDKAppInviteContent *content =[[FBSDKAppInviteContent alloc] init];
                    content.appLinkURL = [NSURL URLWithString:@"https://fb.me/215566242280401"];
                    //optionally set previewImageURL
                    content.appInvitePreviewImageURL = [NSURL URLWithString:@"http://nationality.dedicatedresource.net/upload/profile_image/thumbs/image-14937969811367.jpeg"];
                    
                    // Present the dialog. Assumes self is a view controller
                    // which implements the protocol `FBSDKAppInviteDialogDelegate`.
                    [FBSDKAppInviteDialog showFromViewController:self
                                                     withContent:content
                                                        delegate:self];
                    
                }
                else{
                    [self callFacebookForLogin];
                }
                
            }
                break;
                
            case 5:                 {
                SuggestionVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"SuggestionVC"];
                [self.navigationController pushViewController:vc animated:YES];
                
            }
                break;
                
                
            case 6: {
                ReportUserFirstVC *loginvc = [self.storyboard instantiateViewControllerWithIdentifier:@"ReportUserFirstVC"];
                [self.navigationController pushViewController:loginvc animated:YES];
            }
                
                break;
                
                
                
                
            case 7:                 {
                CMSVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"CMSVC"];
                vc.strType = @"privacy-policy";
                [self.navigationController pushViewController:vc animated:YES];
                
            }
                
                break;
                
            case 8:    {
                CMSVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"CMSVC"];
                vc.strType = @"terms-of-services";
                [self.navigationController pushViewController:vc animated:YES];
            }
                
                break;
                
            case 9: {
                [self showAlert:@"Do you want to logout?"];
            }
                
                break;
                
                
            default:
                break;
        }

    }
    else{
        switch (indexPath.row) {
                
            case 0: {
                EditProfileVC *edit=[self.storyboard instantiateViewControllerWithIdentifier:@"EditProfileVC"];
                [self.navigationController pushViewController:edit animated:YES];
            }
                
                break;
                
            case 1:
            {
                PrivacySettingsVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"PrivacySettingsVC"];
                [self.navigationController pushViewController:vc animated:YES];
            }
                
                break;
                
            case 2:
            {
                BlockingVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"BlockingVC"];
                [self.navigationController pushViewController:vc animated:YES];
            }
                
                break;
                
            case 3:  {
                
                if([[Utility getObjectForKey:@"is_Facebook_User"] boolValue] == YES){
                    
                    FBSDKAppInviteContent *content =[[FBSDKAppInviteContent alloc] init];
                    content.appLinkURL = [NSURL URLWithString:@"https://fb.me/215566242280401"];
                    //optionally set previewImageURL
                    content.appInvitePreviewImageURL = [NSURL URLWithString:@"http://nationality.dedicatedresource.net/upload/profile_image/thumbs/image-14937969811367.jpeg"];
                    
                    // Present the dialog. Assumes self is a view controller
                    // which implements the protocol `FBSDKAppInviteDialogDelegate`.
                    [FBSDKAppInviteDialog showFromViewController:self
                                                     withContent:content
                                                        delegate:self];
                    
                }
                else{
                    [self callFacebookForLogin];
                }
                
            }
                break;
                
            case 4:                 {
                SuggestionVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"SuggestionVC"];
                [self.navigationController pushViewController:vc animated:YES];
                
            }
                break;
                
                
            case 5: {
                ReportUserFirstVC *loginvc = [self.storyboard instantiateViewControllerWithIdentifier:@"ReportUserFirstVC"];
                [self.navigationController pushViewController:loginvc animated:YES];
            }
                
                break;
                
                
                
                
            case 6:                 {
                CMSVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"CMSVC"];
                vc.strType = @"privacy-policy";
                [self.navigationController pushViewController:vc animated:YES];
                
            }
                
                break;
                
            case 7:    {
                CMSVC *vc = [self.storyboard instantiateViewControllerWithIdentifier:@"CMSVC"];
                vc.strType = @"terms-of-services";
                [self.navigationController pushViewController:vc animated:YES];
            }
                
                break;
                
            case 8: {
                [self showAlert:@"Do you want to logout?"];
            }
                
                break;
                
                
            default:
                break;
        }
    }
 }


#pragma mark - FBDialog Delegate Method

- (void)appInviteDialog:(FBSDKAppInviteDialog *)appInviteDialog didCompleteWithResults:(NSDictionary *)results
{
    NSLog(@" result %@",results);
}
- (void)appInviteDialog:(FBSDKAppInviteDialog *)appInviteDialog didFailWithError:(NSError *)error
{
    NSLog(@"error =  %@", error);
    NSString *message = error.userInfo[FBSDKErrorLocalizedDescriptionKey] ?:
    @"There was a problem sending the invite, please try again later.";
    NSString *title = error.userInfo[FBSDKErrorLocalizedTitleKey] ?: @"Oops!";
    
    [[[UIAlertView alloc] initWithTitle:title message:message delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
    
}






#pragma mark - Login To Facebook For Non-FB User

-(void)callFacebookForLogin{
    
    
    login = [[FBSDKLoginManager alloc] init];
    [login logInWithReadPermissions:@[@"public_profile", @"email", @"user_friends"] fromViewController:self handler:^(FBSDKLoginManagerLoginResult *result, NSError *error)
     {
         if (error)
         {
             // Process error
             NSLog(@"error is :%@",error);
         }
         else if (result.isCancelled)
         {
             // Handle cancellations
             NSLog(@"error is :%@",error);
         }
         else
         {
             if ([result.grantedPermissions containsObject:@"email"])
             {
                 NSLog(@"Login successfull");
                 [self fetchUserInfo];
                 
                 
                 
                 
             }
         }
     }];
    
}


-(void)fetchUserInfo
{
    if ([FBSDKAccessToken currentAccessToken])
    {
        [[[FBSDKGraphRequest alloc] initWithGraphPath:@"me" parameters:@{@"fields": @"id,name,link,first_name, last_name, picture.type(large), email, birthday,friends,gender,age_range,cover"}]
         startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
             if (!error)
             {
                 //NSLog(@"result is :%@",result);
                 
                 NSDictionary *dict = (NSDictionary *)result;
                 
                 NSLog(@"User ID : %@",[result valueForKey:@"id"]);
                 NSLog(@"User Name : %@",[result valueForKey:@"name"]);
                 NSLog(@"User First Name :%@",[result valueForKey:@"first_name"]);
                 NSLog(@"User Last Name :%@",[result valueForKey:@"last_name"]);
                 NSLog(@"USER Email is :%@",[result valueForKey:@"email"]);
                 NSLog(@"User fb_Link : %@",[result valueForKey:@"link"]);
                 NSLog(@"User Birthday : %@",[result valueForKey:@"birthday"]);
                 NSLog(@"FB Profile Photo Link :%@",[[[result valueForKey:@"picture"]objectForKey:@"data"]objectForKey:@"url"]);
                 NSLog(@"User total friends : %@",[[[result valueForKey:@"friends"]objectForKey:@"summary"]valueForKey:@"total_count"]);
                 NSLog(@"User Gender : %@",[result valueForKey:@"gender"]);
                 NSLog(@"User age_range : %@",[[result valueForKey:@"age_range"]objectForKey:@"min"]);
                 NSLog(@"User cover Photo Link : %@",[[result valueForKey:@"cover"]objectForKey:@"source"]);
                 
                 //Friend List ID And Name
                 arrFriendList = [[result valueForKey:@"friends"]objectForKey:@"data"];
                 
                 NSMutableArray *fb_friend_Name = [[NSMutableArray alloc]init];
                 NSMutableArray *fb_friend_id  =  [[NSMutableArray alloc]init];
                 
                 for (int i=0; i<[arrFriendList count]; i++)
                 {
                     [fb_friend_Name addObject:[[[[result valueForKey:@"friends"]objectForKey:@"data"] objectAtIndex:i] valueForKey:@"name"]];
                     
                     [fb_friend_id addObject:[[[[result valueForKey:@"friends"]objectForKey:@"data"] objectAtIndex:i] valueForKey:@"id"]];
                     
                 }
                 NSLog(@"Friends ID : %@",fb_friend_id);
                 NSLog(@"Friends Name : %@",fb_friend_Name);
                 
                 [Utility saveObjectInUserDefaults:[[result valueForKey:@"friends"]objectForKey:@"data"] forKey:@"User_FB_Friend_List"];
                 
                 FBSDKAppInviteContent *content =[[FBSDKAppInviteContent alloc] init];
                 content.appLinkURL = [NSURL URLWithString:@"https://fb.me/215566242280401"];
                 //optionally set previewImageURL
                 content.appInvitePreviewImageURL = [NSURL URLWithString:@"http://nationality.dedicatedresource.net/upload/profile_image/thumbs/image-14937969811367.jpeg"];
                 
                 // Present the dialog. Assumes self is a view controller
                 // which implements the protocol `FBSDKAppInviteDialogDelegate`.
                 [FBSDKAppInviteDialog showFromViewController:self
                                                  withContent:content
                                                     delegate:self];
                 
                 
             }
         }];
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#pragma mark - Webservice
-(void)logout{
    
    NSMutableDictionary *httpParams=[NSMutableDictionary new];
    NSLog(@"user id %@",[Utility getObjectForKey:USER_ID]);
    
    [[ServiceRequestHandler sharedRequestHandler] getServiceData:httpParams geturl:[NSString stringWithFormat:@"%@%@",HTTPS_USER_LOG_OUT,[Utility getObjectForKey:USER_ID]] getServiceDataCallBack:^(NSInteger status, NSObject *data)
     {
         
         if(status==0)
         {
             
             NSDictionary *dict = (NSDictionary*)data;
             [Utility saveObjectInUserDefaults:nil forKey:USER_ID];
             LoginVC *loginvc = [self.storyboard instantiateViewControllerWithIdentifier:@"LoginVC"];
            
             if ([[Utility getObjectForKey:@"Login"]isEqualToString:@"login"]) {
                 
                 [(UINavigationController *)self.appdel.window.rootViewController pushViewController:loginvc animated:YES];
                 [self.appdel.window makeKeyAndVisible];
             }
             else{
                 
                 [(UINavigationController *)self.appdel.window.rootViewController pushViewController:loginvc animated:YES];
                 [self.appdel.window makeKeyAndVisible];
                 
                 }
                      }
         
         else
         {
             NSString *str = (NSString *)data;
             [[AppController sharedappController] showAlert:str viewController:self];
         }
  
     }];
    

}
-(void)showAlert : (NSString*)strMsg
{
    UIAlertController * alert=   [UIAlertController
                                  alertControllerWithTitle:@""
                                  message:strMsg
                                  preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* yes = [UIAlertAction
                          actionWithTitle:@"YES"
                          style:UIAlertActionStyleDefault
                          handler:^(UIAlertAction * action)
                          {
                              [alert dismissViewControllerAnimated:YES completion:nil];
                              [self logout];
                          }];
    
    UIAlertAction* no = [UIAlertAction
                         actionWithTitle:@"NO"
                         style:UIAlertActionStyleDefault
                         handler:^(UIAlertAction * action)
                         {
                             [alert dismissViewControllerAnimated:YES completion:nil];
                         }];
    
    [alert addAction:yes];
    [alert addAction:no];
    
    [self presentViewController:alert animated:YES completion:nil];
}
-(IBAction)backToDashboard:(id)sender{
    
    [self.navigationController popViewControllerAnimated:YES];
    
}
@end
