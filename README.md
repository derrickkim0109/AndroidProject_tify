# AndroidProject_tify
 @copyright :  김태현, 최현아,이민우,최지석        
 ### 시연 동영상 
   https://youtu.be/K-URbYZJWVI

 ### 이슈 탭에서 개발 화면을 확인할 수 있습니다. 
   https://github.com/thlbyl0109/AndroidProject_tify/issues/1
 
# Logo
 
  
  
  <img src ="https://github.com/thlbyl0109/AndroidProject_tify/blob/main/document/tifyLogo.png">
     
     
     
     
     
     
 # 시스템 흐름도
   
   <img src ="https://github.com/thlbyl0109/AndroidProject_tify/blob/main/document/system.png">
 
 # ERD 
   <img src ="https://github.com/thlbyl0109/AndroidProject_tify/blob/main/document/ERD.jpeg">
 # 1.Build gradle에 추가를 한다.
 
 
    ///태현 2021.01.06
    // 리사이클러뷰
    implementation 'androidx.recyclerview:recyclerview:1.0.0'
  
    //circular Imageview
    implementation 'com.github.mohammadatif:CircularImageView:1.0.1'
    implementation 'com.squareup.okhttp3:okhttp:4.10.0-RC1'
    /////이미지 불러오기 - 태현
    annotationProcessor 'com.github.bumptech.glide:compiler:4.9.0'
    implementation 'com.github.bumptech.glide:glide:4.11.0'

    // 민우------------------------
    //noinspection GradleCompatible
    implementation 'com.android.support:design:28.0.0'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    //noinspection GradleCompatible
    implementation 'com.android.support:cardview-v7:28.0.0'
    //noinspection GradleCompatible
    implementation 'com.android.support:recyclerview-v7:28.0.0'
    implementation 'androidx.recyclerview:recyclerview:1.1.0'
    implementation 'de.hdodenhof:circleimageview:3.1.0'
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    //-----------------------------


    //현아 서클 인클레이터
    implementation 'me.relex:circleindicator:1.2.2'
    implementation 'com.android.support:recyclerview-v7:30.0.0'

    // 카카오로그인(지석)
    implementation group: 'com.kakao.sdk', name: 'usermgmt', version: project.KAKAO_SDK_VERSION

    implementation "com.kakao.sdk:v2-user:2.2.0" // 카카오 로그인
    implementation "com.kakao.sdk:v2-talk:2.2.0" // 친구, 메시지(카카오톡)
    implementation "com.kakao.sdk:v2-story:2.2.0" // 카카오스토리
    implementation "com.kakao.sdk:v2-link:2.2.0" // 메시지(카카오링크)
    implementation "com.kakao.sdk:v2-navi:2.2.0" // 카카오내비
    implementation "com.kakao.sdk:usermgmt:1.15.1" // 구글지도
   
    구글맵
    implementation 'com.google.maps.android:android-maps-utils:0.5'
    implementation 'com.google.android.gms:play-services-maps:17.0.0'
    implementation 'com.google.android.gms:play-services-location:17.1.0'
    implementation 'noman.placesapi:placesAPI:1.1.3'
    
****

# 2.Andorid에 권한을 추가한다
    
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /> <!-- 외장권한 -->
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.RECEIVE_SMS" />
    <uses-permission android:name="android.permission.SEND_SMS" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.READ_PHONE_NUMBERS" />
****

# 3.Tomcat jsp,image연동
     cos.jar , mysql-connector-java-8.0.17.jar 를 tomcat 폴더의 /lib/에 넣어야됨.
    tomcat 폴더의 /webapps/ROOT/ 안에 넣으면 됨.
****

# 4.Mysql

    tify.sql 파일
    

        
