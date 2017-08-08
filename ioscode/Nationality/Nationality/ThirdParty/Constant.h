//
//  Constant.h
//  Nationality
//
//  Created by WTS-MAC3052016 on 10/04/17.
//  Copyright © 2016 webskitters. All rights reserved.
//

#ifndef Constant_h
#define Constant_h

#define IS_IPHONE_4 (fabs((double)[[UIScreen mainScreen]bounds].size.height - (double)480) < DBL_EPSILON)
#define IS_IPHONE_5 (fabs((double)[[UIScreen mainScreen]bounds].size.height - (double)568) < DBL_EPSILON)
#define IS_IPHONE_6 (fabs((double)[[UIScreen mainScreen]bounds].size.height - (double)667) < DBL_EPSILON)
#define IS_IPHONE_6_PLUS (fabs((double)[[UIScreen mainScreen]bounds].size.height - (double)736) < DBL_EPSILON)

#define hRatio [UIScreen mainScreen].bounds.size.height / 667
#define wRatio [UIScreen mainScreen].bounds.size.width / 375


#define DEVICE_TOKEN @"token_id"
#define FIRST_NAME @"first_name"
#define LAST_NAME @"last_name"
#define EMAIL @"email"
#define PASSWORD @"password"
#define ADDRESS @"address"
#define HOME_CITY @"home_city"
#define REGISTRATION_TYPE @"registration_type"
#define DEVICE_TYPE @"device_type"
#define NATIONALITY_ID @"nationality_id"
#define USER_ID @"id"
#define LONGITUDE @"longitude"
#define LATITUDE @"latitude"
#define TAG @"tag"
#define SHORT_BIO @"short_bio"
#define LANGUAGE_ID @"language_id"
#define INTEREST @"interest"
#define FACEBOOKID @"facebookId"


//#define HTTPS_BASE_URL @"http://app.nationality.dedicatedresource.net/api/v2/"
#define HTTPS_BASE_URL @"http://api.nationalityapp.com/api/v2/"
#define HTTPS_LANGUAGES (HTTPS_BASE_URL@"languages")
#define HTTPS_NATIONALITY (HTTPS_BASE_URL@"nationalities")
#define HTTPS_HOMECITIES (HTTPS_BASE_URL@"cities")

#define HTTPS_REPORT_POST (HTTPS_BASE_URL@"post_report")

#define THUMB_IMAGE @"http://nationalityapp.com/upload/profile_image/thumbs/"
///User

#define HTTPS_USER_SIGNUP (HTTPS_BASE_URL@"user/signup")
#define HTTPS_USER_LOGIN (HTTPS_BASE_URL@"user/signin")
#define HTTPS_USER_DASHBOARD (HTTPS_BASE_URL@"user/dashboard/")
#define HTTPS_CONNECTION_LIST (HTTPS_BASE_URL@"user/friends/")
#define HTTPS_USER_DETAILS (HTTPS_BASE_URL@"user/")
#define HTTPS_USER_LOG_OUT (HTTPS_BASE_URL @"user/logout/")
#define HTTPS_FRIEND_REQUEST_LIST (HTTPS_BASE_URL@"user/friend_request/")
#define HTTPS_FORGOT_PASSWORD (HTTPS_BASE_URL@"user/forgot_password")
#define HTTPS_FRIEND_REQUEST_ACCEPT_REJECT (HTTPS_BASE_URL@"user/friend_accept_reject")
#define HTTPS_CHANGE_PASSWORD (HTTPS_BASE_URL@"user/change_password")
#define HTTPS_SEARCH_USER (HTTPS_BASE_URL@"user/search_user/")
#define HTTP_ADD_FRIEND (HTTPS_BASE_URL@"user/add_friend")
#define HTTP_ALL_USER (HTTPS_BASE_URL@"user/all_user")
#define HTTP_USER_REQUEST_COUNT (HTTPS_BASE_URL@"user/request_count/")
#define HTTPS_CMS_CONTENT (HTTPS_BASE_URL@"cms/")
#define HTTP_BLOCK_USER (HTTPS_BASE_URL@"user/block_user")
#define HTTP_BLOCKED_USER_LIST (HTTPS_BASE_URL@"user/block/")
#define HTTP_UNBLOCKED_USER (HTTPS_BASE_URL@"user/unblock_user")
#define HTTPS_REMOVE_PUSH_BADGES (HTTPS_BASE_URL@"user/remove_push_badge")
#define HTTP_REMOVE_FRIEND (HTTPS_BASE_URL@"user/friend_remove")
#define HTTP_USER_LOCATION_UPDATE (HTTPS_BASE_URL@"user/location_update/")


//Meetup
#define HTTPS_ADD_MEETUP (HTTPS_BASE_URL@"meetup/insert")
#define HTTPS_EDIT_MEETUP (HTTPS_BASE_URL@"meetup/")
#define HTTPS_MEETUP_DETAILS (HTTPS_BASE_URL@"meetup/")
#define HTTPS_MEETUP_LIKE (HTTPS_BASE_URL@"meetup/like")
#define HTTPS_ALL_MEETUP (HTTPS_BASE_URL@"meetup/all/")
#define HTTPS_MEETUP_COMMENT_REPLY (HTTPS_BASE_URL@"meetup/comment_post/")
#define HTTPS_MEETUP_COMMENT_LIKE (HTTPS_BASE_URL@"meetup/comment/like")
#define HTTPS_GET_MY_MEETUP (HTTPS_BASE_URL@"meetup/my/")
#define HTTPS_MEETUP_COMMENT_DETAILS (HTTPS_BASE_URL@"meetup/comment/")
#define HTTPS_MEETUP_FILTER (HTTPS_BASE_URL@"meetup/all/")
#define HTTPS_MEETUP_INVITE_USER (HTTPS_BASE_URL@"meetup/invite_user")
#define HTTPS_MEETUP_ACCEPT_REJECT (HTTPS_BASE_URL@"meetup/meetup_accept_reject")
#define HTTPS_MEETUP_REQUEST_LIST (HTTPS_BASE_URL@"meetup/meetup_request/")
#define HTTPS_MEETUP_ACCEPT_REJECT (HTTPS_BASE_URL@"meetup/meetup_accept_reject")
#define HTTPS_DELETE_MY_MEETUP (HTTPS_BASE_URL@"meetup/")
#define HTTPS_MEETUP_JOIN (HTTPS_BASE_URL@"meetup/meetup_join")
#define HTTPS_MEETUP_UNJOIN (HTTPS_BASE_URL@"meetup/meetup_unjoin")

//Bulletin

#define HTTPS_BULLETIN_LIST (HTTPS_BASE_URL@"bulletin/all/")
#define HTTPS_MYBULLETIN_LIST (HTTPS_BASE_URL@"bulletin/my/")
#define HTTPS_BULLETIN_CLICKED (HTTPS_BASE_URL@"bulletin/")
#define HTTPS_BULLETIN_ADD (HTTPS_BASE_URL@"bulletin/add")
#define HTTPS_BULLETIN_LIKE (HTTPS_BASE_URL@"bulletin/like")
#define HTTPS_BULLETIN_COMMENT_LIKE (HTTPS_BASE_URL@"bulletin/comment/like")
#define HTTPS_BULLETIN_COMMENT (HTTPS_BASE_URL@"bulletin/comment_post/")
#define HTTPS_BULLETIN_COMMENT_DETAILS (HTTPS_BASE_URL@"bulletin/comment/")


//Message
#define HTTPS_RECENT_CHAT_MESSAGE (HTTPS_BASE_URL@"user/recent_message/")
#define HTTPS_GET_MESSAGE_TOKEN (HTTPS_BASE_URL@"chat-token")
#define HTTPS_MESSAGE_TOKEN_LIST (HTTPS_BASE_URL@"token-list")
#define HTTPS_RECENT_MESSAGE_STORE (HTTPS_BASE_URL@"user/recent_message_store")
#define HTTPS_MESSAGE_PUSH_SENT (HTTPS_BASE_URL@"user/insert_message")
#define HTTPS_MESSAGE_BADGE_REMOVE (HTTPS_BASE_URL@"user/remove_message_push_badge")


//App Settings
#define HTTPS_USER_SETTINGS_INSERT (HTTPS_BASE_URL@"appsetting/insert")
#define HTTPS_USER_SETTINGS_GET (HTTPS_BASE_URL@"appsetting/")
#define HTTPS_USER_INVITE_FBFRIEND (HTTPS_BASE_URL@"user/facebook_add_friend")
#define HTTPS_SUGGESTION_POST (HTTPS_BASE_URL@"suggestions")


//#define HTTPS_MEETUP_VIEW (HTTPS_BASE_URL@"meetup/like")
#endif /* Constant_h */
