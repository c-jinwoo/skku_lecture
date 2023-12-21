# BuddyBud : SKKU International Community Application

## Project Description
BuddyBud is a mobile application Bridging International & Local Campus Friendships.
<br/><br/>

## Project Duration
2023.09 ~ 2023.12
<br/><br/>

## UI & Features
### Login/Register
Users can enter their school to access the school community. It's a semi-anonymous community where nicknames and profile pictures are public.

<img src="misc/splash.jpg" width="250"> <img src="misc/login.jpg" width="250"> <img src="misc/register_1.jpg" width="250">


### Feed
In the feed, users can freely post their daily life updates or questions, and with the ability to translate posts and comments, communication is possible without language barriers.

<img src="misc/feed_main.jpg" width="200">  <img src="misc/feed_translate.jpg" width="200">  <img src="misc/feed_detail.jpg" width="200">  <img src="misc/feed_post.jpg" width="200">

### Board
In the board section, the system automatically crawls and posts various club/event promotional articles from the school's official announcement board and 'Everytime', the largest college student community in Korea. Users can scrap or recommend the posts they find useful.

<img src="misc/board_main.jpg" width="250"> <img src="misc/board_datail.jpg" width="250">

### Willow
Willow is a word created by combining 'willow', the logo of the BuddyBud app, and the concept of 'Follow'. Users can send Willow requests to others they wish to connect with, and upon mutual consent, can become friends and send one-on-one chats.

<img src="misc/willow_main.jpg" width="200"> <img src="misc/willow_chat.jpg" width="200"> <img src="misc/willow_request.jpg" width="200"> <img src="misc/sent_willow.jpg" width="200">

### My Page
In the 'My Page' section, users can view their own posts and the posts they have scrapped, and can also edit their profile as needed.

<img src="misc/account_home.jpg" width="250"> <img src="misc/account_scrap.jpg" width="250"> <img src="misc/account_edit.jpg" width="250"> 
 
## Requirements
### Environment Settings
- Java Development Kit (JDK) 17
- Android Studio minSDK 28
- Spring Boot 3.1.5
- MySQL 8.0.35
- Frontend : com.swe.buddybud.common.ServerConfig
- Backend : application.properties
- [Database Schema](https://github.com/leedayun/BuddyBud/blob/main/misc/buddybud_231130042918.sql)

### Installation
```
$ git clone https://github.com/leedayun/BuddyBud.git
$ cd BuddyBud
```

### Backend
```
$ cd BE
$ ./gradlew bootJar
$ ./gradlew bootRun
```
You can use .jar file to run server either in the foreground
```
$ java -jar JARFILE.jar
```
or in Daemon
```
$ nohup java -jar JARFILE.jar &
```
<br/><br/>

## Tech Stack
### Frontend
![JAVA](https://img.shields.io/badge/java-F05032?style=for-the-badge&logo=java&logoColor=white)
![ANDROIDSTUDIO](https://img.shields.io/badge/Android%20Studio-3DDC84?style=for-the-badge&logo=AndroidStudio&logoColor=white)

### Backend
![SPRING](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white)
![SPRINGBOOT](https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![INTELLIJ](https://img.shields.io/badge/IntelliJ%20IDEA-grey?style=for-the-badge&logo=intellijidea)
![ECLIPSE](https://img.shields.io/badge/Eclipse-darkblue?style=for-the-badge&logo=eclipse&logoColor=white)

### Database
![MYSQL](https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![MYBATIS](https://img.shields.io/badge/MyBatis-8879DD?style=for-the-badge&logo=MyBatis&logoColor=white)

### Enviornment
![UBUNTU](https://img.shields.io/badge/Ubuntu-E95420?style=for-the-badge&logo=Ubuntu&logoColor=white)
![AWS](https://img.shields.io/badge/amazonaws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white)
![ANDROID](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)

### Configuration Management
![GIT](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)
![GITHUB](https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white)

### Communication
![Slack](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=Slack&logoColor=white)
![Zoom](https://img.shields.io/badge/zoom-darkblue?style=for-the-badge&logo=zoom&logoColor=white)
![GoogleMeet](https://img.shields.io/badge/GoogleMeet-00897B?style=for-the-badge&logo=Google%20Meet&logoColor=white)
<br/><br/>

## ERD
![ERD](./misc/erd.png)
