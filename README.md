* * *
# **옷짱** 
온라인 옷장 관리 & 날씨별 옷추천
* * *
<br><br/>
![logo](https://github.com/sonbankclerk/final_project/assets/131222450/73f56f68-07c9-4d95-9a72-89b6c074260b)
<br><br/>
## 🔗 Link
- **Github Repository :** 
https://github.com/jooooooowon/final_project.git

<br><br/>
## 📖 Summary
> ```Introduction```
>  - [💡 Motivation](#-motivation)
>  - [🧑🏻‍💻 Team Member](#-team-member) 
>  - [📌 Features](#-features)  
>  - [🛠 Stack](#-stack)
>  - [💿 Installation](#-installation)  
>  - [📸 Site Screenshot](#-site-screenshot)


#

#
# Introduction
옷은 KOSTA 257기 파이널 프로젝트로 진행 되었으며 
2023.06.12 ~ 2023.07.12 총 1개월간 설계 및 개발하였으며, 5명의 팀원이 함께 하였습니다.
<br><br/>
## 💡 Motivation

변덕스러운 날씨와 기온 변화로 '오늘 뭐입지?' 라는 물음표로 시작된 프로젝트

날씨에 따른 옷차림을 기록하고 온라인 옷장을 관리한다.

'옷짱'은 본인의 옷장과 옷을 좋아하는 유저들의 옷에 짱을 가린다를 합쳐 옷짱이 탄생하였다.

<br><br/>
## 🧑🏻‍💻 Team Member
- 이주원 : https://github.com/jooooooowon
- 봉선호 : https://github.com/Ohnus
- 문승미 : https://github.com/YeeBoss
- 최예은 : https://github.com/moonparks
- 손행원 : https://github.com/sonbankclerk

<br><br/>
## 📌 Features
- 이메일 인증
- 위치별 기온
- 기온별 옷추천
- 네이버 쇼핑 검색
- 옷등록 & 기록 리스트
- 코디 게시판
- 요일별 배틀 게시판
  
<br><br/>
## 🛠 Stack
### ✔️ Front-End
- jQuery v.1.12.4
- JavaScript

### ✔️ Back-End
- Java v17
- Maven v3.6.3
- Spring framework v5.3.7
- Lombok v1.18.20
- Spring Data JPA

### ✔️ DataBase
- Oracle v22.2.1

### ✔️ Server
- Apache Tomcat v9.0.43

### ✔️ Open API 
- 네이버 쇼핑 API
- 기상청 단기예보 API
- 카카오맵 로컬 API
- 한국천문연구원 일ㆍ출몰 API
<br><br/>


## 💿 Installation

> #### ℹ️ Working with OTZZANG (this project) in your IDE

### Steps:
#### ✔️ Prerequisites
아래의 항목들이 설치되어 있어야 합니다.
- Java 8 or newer
- Maven 3.6.3 or 3.6+ (http://maven.apache.org/install.html)
- git command line tool (https://help.github.com/articles/set-up-git)
- Oracle 10.5+
- Tomcat 9.0.43 or 9+
- Your prefered IDE
    - Spring Tools Suite(STS)
    - Eclipse IDE


#### 1️⃣ 프로젝트 클론
~~~ 
git clone https://github.com/jooooooowon/final_project
~~~
---
#### 2️⃣ _STS_ _에서  
   1. clone 한 프로젝트를 import  
      ```File -> Import -> Maven -> Existing Maven project```
   2. Maven sources loading  
      ```프로젝트 우클릭 > Run As > Maven install```
      
#### 2️⃣ _Eclipse_ _에서  
   1. clone 한 프로젝트를 open  
      ```File -> Open```
   2. Maven sources loading  
      ```프로젝트 우클릭 > Maven > Generates sources and Update Folders```
      
---
#### 3️⃣ properties 파일 5개 생성 및 설정
   >> 1. src/main/resources/application.properties/```# oracle set```
   >   ~~~
   >   spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
   >   spring.datasource.url=jdbc:oracle:thin:@localhost:1521/xe
   >   spring.datasource.username=<YOUR_DB_USERNAME>
   >   spring.datasource.password=<YOUR_DB_PASSWORD>
   >   ~~~
   #
   >> 2. src/main/resources/application.properties/```#multipart```
   >   ~~~
   >   spring.servlet.multipart.location=C:/otzzang/
   >   spring.servlet.multipart.max-file-size=5MB
   >   ~~~
   #
   >> 3. src/main/resources/application.properties/```#mail```
   >   ~~~
   >   spring.mail.host=smtp.gmail.com
   >   spring.mail.port=587
   >   spring.mail.username=<YOUR_EMAIL>
   >   spring.mail.password=<YOUR_EMAILPWD>
   >   spring.mail.properties.mail.smtp.auth=true
   >   spring.mail.properties.mail.smtp.starttls.enable=true
   >   spring.mail.properties.mail.smtp.connectiontimeout=18000
   >   spring.mail.properties.mail.smtp.timeout=18000
   >   spring.mail.properties.mail.smtp.writetimeout=18000
   >   ~~~
   #
   >> 4. src/main/resources/application.properties/```#jpa```
   >   ~~~
   >   spring.jpa.generate-ddl=true
   >   spring.jpa.database=oracle
   >   spring.jpa.show-sql=true
   >   ~~~
   #
   >> 5. src/main/resources/application.properties/```#encoding```
   >   ~~~
   >   server.servlet.encoding.charset=UTF-8
   >   server.servlet.encoding.enabled=true
   >   server.servlet.encoding.force=true
   >   ~~~

---
#### 4️⃣ _STS_ or _Eclipse_ > Tomcat Configure 설정 후 > Run
<br><br/>
## 📸 Site Screenshot

**[회원가입]**
⚠️이미지 추가 예정⚠️
<br><br/>

**[로그인]**
⚠️이미지 추가 예정⚠️
<br><br/>

**[아이디 찾기]**
⚠️이미지 추가 예정⚠️
<br><br/>

**[비밀번호 찾기]**
⚠️이미지 추가 예정⚠️
<br><br/>

**[날씨]**
⚠️이미지 추가 예정⚠️
<br><br/>

**[네이버 쇼핑]**
⚠️이미지 추가 예정⚠️

**[네이버 쇼핑 리스트]**
⚠️이미지 추가 예정⚠️

**[옷장]**
⚠️이미지 추가 예정⚠️
<br><br/>

**[ootw]**
⚠️이미지 추가 예정⚠️
<br><br/>

**[코디]**
⚠️이미지 추가 예정⚠️
<br><br/>

**[배틀 신청]**
⚠️이미지 추가 예정⚠️
<br><br/>

**[배틀 투표]**
⚠️이미지 추가 예정⚠️
<br><br/>

**[이 주의 우승자]**
⚠️이미지 추가 예정⚠️
<br><br/>

**[명예의 전당]**
⚠️이미지 추가 예정⚠️
<br><br/>

**[마이페이지]**
⚠️이미지 추가 예정⚠️

**[좋아요 목록]**
⚠️이미지 추가 예정⚠️

**[북마크 목록]**
⚠️이미지 추가 예정⚠️

**[우승 횟수]**
⚠️이미지 추가 예정⚠️

**[내 정보 수정]**
⚠️이미지 추가 예정⚠️

**[주제 선정]**
⚠️이미지 추가 예정⚠️

**[후보자 선정]**
⚠️이미지 추가 예정⚠️

**[신고목]**
⚠️이미지 추가 예정⚠️

**-Fin-**
