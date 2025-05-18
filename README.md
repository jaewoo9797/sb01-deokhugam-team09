# deokhugam Project 🚀

<div>
    <h3><b>📚책 읽는 즐거움을 공유하고, 지식과 감상을 나누는 책 덕후들의 커뮤니티 서비스</b></h3>
</div>

<br/>

## 📖 프로젝트 개요

**📚 책 읽는 즐거움을 공유하고, 지식과 감상을 나누는 책 덕후들의 커뮤니티 서비스**

> 코드잇 중급 프로젝트 - 덕후감  
> 도서 이미지 OCR 및 ISBN 매칭 서비스 SPRING 백엔드 구축  
> **개발 기간:** 2025.04.16 ~ 2025.05.12

<br/>

## 🧑‍💻나의 담당 역할
<div align=center>
    
| 분야        | 상세 내용                              |
| --------- | ---------------------------------- |
| 백엔드 개발    | Spring Boot 기반 API 개발 - 알림 도메인 구현 |
| 인프라 & 배포  | AWS ECS, RDS, S3 환경 구성 및 CI/CD 설정, Secret Manager와 IAM 정책 커스텀, OIDC 인증  |
| 모니터링 구축   | Prometheus + Grafana 활용 서버 모니터링    |
| 테스트 코드 작성 | RestAssured, H2 DB 기반 통합, 단위 테스트 구현    |

</div>

<br/>

## 🔍프로젝트 성과 및 회고
프로젝트를 진행한 후 회고를 작성하였습니다. 

### 🧩성과 및 회고 요약
이번 프로젝트에서는 개발, 배포, 운영 모니터링까지 전 과정을 경험했습니다.   
처음 시도해본 것은  `AWS ECS Fargate` 기반 배포, `Spring Metric`, `Prometheus + Grafana` 를 활용한 모니터링이 있었습니다.

- Docker 이미지 최적화
- AWS 서비스를 이용해서 키 관련 보안 강화 (실제 코드 상에 들어나는 키 값 노출 X)
- Spring Metric에 대한 정리 + 모니터링 툴
    - 실제 모니터링을 해보면서 `스프링 톰캣이 어떻게 동시에 여러 요청을 처리할 수 있는가`에 대해 관심
    - 스레드에 관해 기본 개념부터 학습 중

기술의 사용방법만 익히기 보다, 관련 개념 학습과 성과 비교를 해보며 몰입할 수 있었습니다.

<br/>

<div align=center>
    <p>실제 Docker 최적화 관련 적용을 통한 최적화 결과 분석</p>
    <img src="https://github.com/user-attachments/assets/a5b94d0e-e197-4529-af23-1571ffb46f5b" alt="docker image optimization" width="700">
</div>

<br/>

👉 자세한 회고는 블로그에서 확인하실 수 있습니다!  

<a href="https://doitwojae.tistory.com/entry/2025-05-%EC%BD%94%EB%93%9C%EC%9E%87-%EC%A4%91%EA%B8%89-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%9A%8C%EA%B3%A0" target="_blank">🔗 2025 코드잇 중급 프로젝트 회고</a>

<br/>

## 🧠기억에 남는 PR 및 이슈

- Resolver, Interceptor를 이용하여 로그인 유저 관리 한 곳으로 응집
- PR에 사용 방법과 설명 포함을 포함시켜 가져다 사용하기 쉽게 하였습니다.
- 🔗 <a href="https://github.com/CodeitSB-Team9/sb01-deokhugam-team09/pull/78" target="_blank">PR #78 - 로그인 유저 주입 기능 리팩토링 </a>

<div align="center">
  <img src="https://github.com/user-attachments/assets/67f25c4b-493f-4b52-83c5-fc617d90cbda" alt="로그인 유저 리졸버 흐름도" width="700"/>
</div>

<br/>

팀원들이 쉽게 이용할 수 있도록 사용 방법에 대해서 설명해두었고 실제로 팀원들이 각자 필요한 곳에서 사용하였습니다. 

<br/>

- 내가 잘 못 알고 있었던 HTTP status 에 대해서 이야기하며 다시 한 번 정리할 수 있었습니다.
- 🔗 <a href="https://github.com/CodeitSB-Team9/sb01-deokhugam-team09/pull/70" target="_blank">PR#53 - User</a>

<div align="center">
    <img src="https://github.com/user-attachments/assets/b34dab6e-6211-4699-9634-13539ac8c6f9" alt="PR 리뷰" width="600"/>
</div>

<br/>

- 예상되는 예외에 대해서 코멘트를 남기고 소통

- 🔗 <a href="https://github.com/CodeitSB-Team9/sb01-deokhugam-team09/pull/91" target="_blank">PR#53 - 유저 관련 컨트롤러</a>

<div align="center">
    <img src="https://github.com/user-attachments/assets/96a18d59-284c-46b2-84c0-710757855d2c" alt="PR 리뷰" width="600"/>
</div>

<br/>



## <span id="2">👥팀원 소개</span>

- 팀원 : 5명
- 팀장 : 김상호
- 팀원 : 김희수, 김효정, 공병열, 백재우

<div align="center">

|            <img src="https://img.shields.io/badge/Project_Leader-FF5733" />             |              <img src="https://img.shields.io/badge/Team_Member-6DB33F" />              |              <img src="https://img.shields.io/badge/Team_Member-6DB33F" />               |              <img src="https://img.shields.io/badge/Team_Member-6DB33F" />               |              <img src="https://img.shields.io/badge/Team_Member-6DB33F" />               |
|:---------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------:|
| <img src="https://avatars.githubusercontent.com/u/90863663?v=4" width="120px;" alt=""/> | <img src="https://avatars.githubusercontent.com/u/92302468?v=4" width="120px;" alt=""/> | <img src="https://avatars.githubusercontent.com/u/101076275?v=4" width="120px;" alt=""/> | <img src="https://avatars.githubusercontent.com/u/132568348?v=4" width="120px;" alt=""/> | <img src="https://avatars.githubusercontent.com/u/157946706?v=4" width="120px;" alt=""/> |
|                           [김상호](https://github.com/ghtkdrla)                            |                        [김희수](https://github.com/kaya-frog-ramer)                        |                            [김효정](https://github.com/hyojKim2)                            |                          [공병열](https://github.com/byeongyeol12)                          |                           [백재우](https://github.com/jaewoo9797)                           |

</div>

<br/>

## 🛠️기술 스택

<div align=center> 

### ⚙️ Backend

<p>
<img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white"/>
<img src="https://img.shields.io/badge/JPA-59666C?style=flat-square&logo=hibernate&logoColor=white"/>
<img src="https://img.shields.io/badge/QueryDSL-000000?style=flat-square"/>
<img src="https://img.shields.io/badge/Scheduler-6DB33F?style=flat-square&logo=spring&logoColor=white"/>
</p>

### 🗄️ Database & Infrastructure

<p>
<img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=postgresql&logoColor=white"/>
<img src="https://img.shields.io/badge/AWS-232F3E?style=flat-square&logo=amazonaws&logoColor=white"/>
<img src="https://img.shields.io/badge/RDS-527FFF?style=flat-square&logo=amazonrds&logoColor=white"/>
<img src="https://img.shields.io/badge/ECS-FF9900?style=flat-square&logo=amazonecs&logoColor=white"/>
<img src="https://img.shields.io/badge/ECR-FF9900?style=flat-square&logo=amazonaws&logoColor=white"/>
<img src="https://img.shields.io/badge/S3-569A31?style=flat-square&logo=amazons3&logoColor=white"/>
<img src="https://img.shields.io/badge/Secrets_Manager-FF9900?style=flat-square&logo=amazonaws&logoColor=white"/>
<img src="https://img.shields.io/badge/Systems Manager-FF9900?style=flat-square&logo=amazonaws&logoColor=white"/>
</p>

### 🔁 CI/CD & 테스트

<p>
<img src="https://img.shields.io/badge/GitHub_Actions-2088FF?style=flat-square&logo=githubactions&logoColor=white"/>
<img src="https://img.shields.io/badge/Jacoco-C71A36?style=flat-square"/>
<img src="https://img.shields.io/badge/RestAssured-52B54B?style=flat-square"/> 
</p>

### 📊 모니터링

<p>
<img src="https://img.shields.io/badge/Prometheus-E6522C?style=flat-square&logo=prometheus&logoColor=white"/> 
<img src="https://img.shields.io/badge/Grafana-F46800?style=flat-square&logo=grafana&logoColor=white"/>
</p>

### 🔐 인증 & 보안

<p> 
<img src="https://img.shields.io/badge/OIDC-4A90E2?style=flat-square&logo=openid&logoColor=white"/> 
</p>

### 🧠 기타 기술

<p> 
<img src="https://img.shields.io/badge/Tesseract-FFB400?style=flat-square"/> 
</p>

</div>

## 🗂 아키텍처 & ERD

<div align="center">
<h3>Architecture</h3>
<p>
<img src="https://github.com/user-attachments/assets/c736f6b1-8491-4b12-a56f-1c03b80125ae" >
</p>
<h3>ERD</h3>
<p>
<img src="https://github.com/user-attachments/assets/4ffaf03d-1cc4-4c70-a616-fb5202e8c590" width="500px" >
</p>
</div>

<br/>

## 🚀CI / CD Flow

<div align="center"> 
    <img src="https://github.com/user-attachments/assets/edcb1418-6d79-46be-b110-390e873ee400" alt="CI/CD 전체 흐름도" width="700"/> 
    <br/>
    <br/> 
    <img src="https://github.com/user-attachments/assets/ff337d3b-6541-41aa-be0e-4a89cc5fbe25" alt="ECS 세부 구성도" width="700"/> 
</div>
