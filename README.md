# OpenSourceProject
오픈소스개발프로젝트 수업 프로젝트 계정을 위한 깃허브 레퍼지토리이며 , 건강관리 웹사이트 제작 목표

## 구성원
홍희혁
남연서 
최나현
박조현
김규현

## 사용 언어
![React](https://img.shields.io/badge/-React-20232A?logo=react&logoColor=61DAFB&style=flat) ![Java](https://img.shields.io/badge/-Java-orange?logo=java&logoColor=white&style=flat) ![Spring Boot](https://img.shields.io/badge/-Spring%20Boot-6DB33F?logo=spring-boot&logoColor=white&style=flat) ![Spring Framework](https://img.shields.io/badge/-Spring-6DB33F?logo=spring&logoColor=white&style=flat)

# Board Project

## 📌 프로젝트 소개
게시판 기능을 제공하는 웹 애플리케이션입니다. 사용자 인증, 게시글 CRUD, 이미지 업로드 등의 기능을 제공합니다.

## 🛠 기술 스택

### Frontend
- React 18.2.0
- TypeScript
- React Router DOM
- Axios
- React Cookie
- Zustand (상태관리)

### Backend
- Spring Boot 2.7.x
- Spring Security
- Spring Data JPA
- JWT (JSON Web Token)
- MySQL 8.0

## 🏗 시스템 아키텍처
Client <-> REST API <-> Server <-> Database

## 깃허브 운영 방식(수정될 수 있음)
1. master branch
2. develop branch

## 📁 프로젝트 구조
board/
├── front/
│ ├── src/
│ │ ├── components/ # 재사용 가능한 컴포넌트
│ │ ├── views/ # 페이지 컴포넌트
│ │ ├── stores/ # 상태 관리
│ │ └── apis/ # API 통신 모듈
│ └── package.json
│
└── back/
└── src/
└── main/
├── java/
│ └── com/board/
│ ├── config/ # 설정 파일
│ ├── controller/ # API 엔드포인트
│ ├── service/ # 비즈니스 로직
│ ├── repository/ # 데이터 접근 계층
│ ├── entity/ # 데이터 모델
│ └── dto/ # 데이터 전송 객체
└── resources/
└── application.properties

## ⚙ 주요 기능
1. 사용자 인증
   - 회원가입
   - 로그인/로그아웃
   - JWT 기반 인증

2. 게시판
   - 게시글 작성/조회/수정/삭제
   - 이미지 업로드
   - 페이징 처리

## 🚀 시작하기

### 요구사항
- Node.js
- Java 11 이상
- MySQL 8.0

### 설치 및 실행

1. Frontend
bash
cd board/front
npm install
npm start

2. Backend
bash
cd board/back
./gradlew bootRun


위 두가지로 구성된 branch로 운영하며, master branch에는 오류나 충돌이 없는 완전한 코드만 올리도록 합니다.
그러니 그 외에 개발중이거나 버그가 있는 등등의 코드들은 develop branch에서 관리하도록 합니다.
(master 브랜치는 직접 개개인이 push할 수 없도록 설정해둘 것이기에 pull request를 통해 master branch를 업데이트가 가능하게 할 것.

## 실습파일과 개발파일 구분 안내
수업에서 다루는 실습코드와 개인적인 공부한 내용들은 실습파일에 깔끔하게 정리해서 각자 이름으로된 폴더로 올려주세요.
추후 개발이 시작되면, 관련 코드파일은 전부 개발 파일에 올려주시면 됩니다.
(개발파일은 개발이 시작되면 생성하여 올려두도록 하겠습니다.)

<br/>
