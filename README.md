# customer-server

---

## 소개

Single Sign-On (SSO) 은 분명히 사용자와 개발자에게 편리함을 가져다준다. \
하지만 SSO만으로는 특화된 비즈니스를 만족시킬 수 없기에 사용자 관리 데이터베이스를 설계하고 구축한다. \
사용자 경험은 개선했지만 비즈니스가 여러 개라면 비즈니스마다 데이터베이스를 설계하고 구축해야 한다.\
SSO를 이용하면서 사용자 정보 또한 한곳에서 관리하는 impati만의 방법을 소개한다.

## 흐름

<img src="docs/seq.png" width="700">

- client-server 는 redirectUrl과 함께 로그인 요청을 위임한다.
- customer-server 는 로그인에 성공한 사용자에 대해 redirectAttribute 에 엑세스 토큰을 발급받을 수 있는 인가 토큰을 발급한다.
- client-server 는 인가코드로 customer-server 로부터 엑세스 토큰을 발급받는다.
- 발급 받은 엑세스 토큰으로 client-server 는 customer-server 로부터 사용자 정보를 가져온다.

---

## 기술

* Spring Boot 2.7.7
* Spring Security
* Java 17
* Spring Web
* Spring Data JPA
* Thymeleaf
* Spring Security
* MySQL Driver
* JJWT

---

## 주요 기능

#### 로그인 기능

- SSO 로그인 지원 (google, Kakao , Naver ,Keycloak)

  GET /auth/login?redirectUrl=${REDIRECT_URL}}

  HOST : service-hub.org

#### 회원가입 기능

- 회원 가입 기능 지원 (keycloak)

#### 토큰 발급

- 로그인에 성공하면 엑세스 토큰을 얻을 수 있는 토큰 발급

#### 엑세스 토큰 발급

- 사용자 정보를 조회할 수 있는 엑세스 토큰 발급

#### 사용자 정보

- 엑세스 토큰으로 사용자 정보를 가져올 수 있는 기능

#### Client 등록

- 등록된 Client만 앤드포인트를 이용할 수 있는 기능