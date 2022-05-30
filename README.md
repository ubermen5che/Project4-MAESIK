# Project-4 - 매일이 식목일(Back-End) 🪴

---

## 💁‍♂️팀원 소개
| [<img src="https://github.com/ubermen5che.png" width="100px">](https://github.com/ubermen5che) | [<img src="https://github.com/Kimngeunhye.png" width="100px">](https://github.com/Kimngeunhye) |
| :--------------------------------------------------------------------------------------: | :----------------------------------------------------------------------------------------------: |
|                          [권용준](https://github.com/ubermen5che)                           |                            [김은혜](https://github.com/Kimngeunhye)                             |

---

## 🔧기술 스택

<img src="[https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white)">

<img src="[https://img.shields.io/badge/SpringSecurity-6DB33F?style=for-the-badge&logo=SpringSecurity&logoColor=white](https://img.shields.io/badge/SpringSecurity-6DB33F?style=for-the-badge&logo=SpringSecurity&logoColor=white)">

<img src="[https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white)">

---

## ✔️개발 현황

- Spring Security + Oauth2.0 +JWT를 이용한 소셜 로그인 구현
- 13개의 REST API 구현
- JUnit5를 이용한 테스트 코드 작성
- WebFlux를 이용한 비동기 API 호출 코드 구현
- Spring Data JPA + JPQL을 이용한 CRUD 구현
- AWS EC2를 이용해 React 연동 후 배포

---

## 📃REST API Spec

**[링크](https://www.notion.so/e458beafc112498f9d7f7a6e27f78089)**

---

## 🔮Backed Feature

- github와 연동시켜서 자신의 활동내역을 불러들인다.
- github와 만든 사이트가 별개로 진행되는 것이 아니기 때문에 이중으로 Repository 를 생성할 필요가 없다.
- Frontend와 추후 연동시켜서 자신의 커밋내용을 저장하면 그룹 내 팀원들과 커밋갯수를 한 눈에 알아 볼 수 있다.

---

## 📍배포 서버 주소

- [http://3.35.212.150:3000/](http://3.35.212.150:3000/)
- 보여지는 프론트는 소셜 로그인 기능이 작동하는지 테스트하기 위한 임시 프론트입니다.

---

# 개발하면서 어려웠던 점

## Spring Security + JWT를 이용한 소셜로그인 연동

### 어려웠던 부분

- Spring security에 대한 지식이 없는 상태로 개발을 진행했다.
- 단순히 구글에 ‘Spring Security를 이용한 소셜로그인 구현’ 이라는 키워드로 구글링해서 따라해보려고 했지만 우리 프로젝트의 상황과 딱 맞아떨어지는 경우가 없어서 customizing을 해야했음.
- 그러나 spring security 구조에 및 Spring Oauth2.0 프레임워크에 대한 이해가 부족해 버그가 발생해도 잡기가 매우 어려웠었음.

### 해결 방법

- 공식문서와 기본구조에 대한 블로그 글을 참고하여 기본구조에 대해 익히고 프레임워크단 코드 디버깅을 하면서 어떤 데이터가 오고가는지, flow는 어떻게 되는지 파악후 해결하였음.

---

# 추후 개발해야할 것들

- 커밋 독려 알림 서비스 기능 개발
- 그룹 대표 이미지 등록 기능 개발
- 리팩토링

---

