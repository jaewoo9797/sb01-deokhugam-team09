# deokhugam Project 🚀

## 📖 프로젝트 소개

<div>
    <h3><b>📚책 읽는 즐거움을 공유하고, 지식과 감상을 나누는 책 덕후들의 커뮤니티 서비스</b></h3>
</div>

<br/>

> 코드잇 중급 프로젝트명: 덕후감(deokhugam)  
> 진행 기간: 2025.04.16 ~ 2025.05.12
> 주요 기능: 도서 이미지 OCR 및 ISBN 매칭 기능을 포함한 Spring 기반 백엔드 구축

<br/>

## 🧑‍💻나의 담당 역할

<div align=center>

| 분야        | 상세 내용                                                                  |
|-----------|------------------------------------------------------------------------|
| 백엔드 개발    | Spring Boot 기반 API 개발 - 알림 도메인 구현                                      |
| 인프라 & 배포  | AWS ECS, RDS, S3 환경 구성 및 CI/CD 설정, Secret Manager와 IAM 정책 커스텀, OIDC 인증 |
| 모니터링 구축   | Prometheus + Grafana 활용 서버 모니터링                                        |
| 테스트 코드 작성 | RestAssured, H2 DB 기반 통합, 단위 테스트 구현                                    |

</div>

<br/>

## 🔍프로젝트 성과 및 회고

프로젝트를 진행한 후 회고를 정리하여 문서화했습니다.

### 🧩성과 및 회고 요약

본 프로젝트를 통해 개발, 배포, 운영 모니터링 등 전체 개발 프로세스를 경험하였습니다.   
특히, 다음과 같은 기술을 처음으로 적용하였습니다.

- AWS ECS Fargate를 활용한 컨테이너 기반 배포
- Spring Metrics, Prometheus, Grafana를 활용한 모니터링 시스템 구축

이외에도 다음과 같은 성과를 달성했습니다.

- Docker 이미지 최적화를 통한 빌드 효율성 향상
- AWS 서비스를 활용하여 보안 강화를 위한 키 관리 및 노출 방지
- Spring Metrics를 활용한 모니터링 도구 구성 및 스레드 처리 방식에 대한 학습

기술의 단순한 사용법을 익히는 것을 넘어, 관련 개념을 심도 있게 학습하고 성과를 비교 분석함으로써 개발 역량을 향상 시킬 수 있었습니다.

<br/>

<div align=center>
    <p>실제 Docker 최적화 관련 적용을 통한 최적화 결과 분석</p>
    <img src="https://github.com/user-attachments/assets/a5b94d0e-e197-4529-af23-1571ffb46f5b" alt="docker image optimization" width="700">
</div>

<br/>

### 👉 자세한 회고는 블로그에서 확인하실 수 있습니다!

### <a href="https://doitwojae.tistory.com/entry/2025-05-%EC%BD%94%EB%93%9C%EC%9E%87-%EC%A4%91%EA%B8%89-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%9A%8C%EA%B3%A0" target="_blank">🔗 2025 코드잇 중급 프로젝트 회고</a>

<br/>

## 🧠기억에 남는 PR 및 이슈

### 로그인 유저 주입 기능 리팩토링

- <a href="https://github.com/CodeitSB-Team9/sb01-deokhugam-team09/pull/78" target="_blank">PR #78: Resolver와 Interceptor를 활용하여 로그인 유저 관리를
  중앙 집중화하였습니다.
- PR에 사용 방법과 설명을 포함시켜 팀원들이 각자 필요한 곳에서 사용하였습니다.

<div align="center">
  <img src="https://github.com/user-attachments/assets/67f25c4b-493f-4b52-83c5-fc617d90cbda" alt="로그인 유저 리졸버 흐름도" width="700"/>
</div>

<br/>

팀원들이 쉽게 활용할 수 있도록 사용법을 문서화했으며, 각자의 기능에 적용해 활용했습니다.

<br/>

### HTTP 상태 코드에 대한 이해 개선

- <a href="https://github.com/CodeitSB-Team9/sb01-deokhugam-team09/pull/70" target="_blank">PR #70: HTTP 상태 코드에 대한 잘못된 이해를 팀원들과 논의하며, 정확한
  개념을 정리했습니다.

<div align="center">
    <img src="https://github.com/user-attachments/assets/b34dab6e-6211-4699-9634-13539ac8c6f9" alt="PR 리뷰" width="600"/>
</div>

<br/>

### 예외 처리에 대한 논의

- <a href="https://github.com/CodeitSB-Team9/sb01-deokhugam-team09/pull/91" target="_blank">PR #91: 예상되는 예외 상황에 대해 코멘트를 남기고 팀원들과 소통함으로써, 보다
  견고한 예외 처리를 구현하였습니다.

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

## Scheduler & Async

### 특정 시간대에 실행되는 스케줄러 플로우 차트

```mermaid
flowchart TD
  A[앱 구동] --> B[EnableScheduling 활성화]
  B --> C[ScheduledTaskRegistrar 등록]
  C --> D[스레드풀 초기화]
  D --> E{트리거 시간 도달?}
  E -- 아니오 --> D
  E -- 예 --> F[Runnable 생성]
  F --> G[ThreadPoolTaskScheduler submit]
  G --> H[메서드 로직 수행]
  H --> I{예외 발생?}
  I -- 예 --> J[AsyncUncaughtExceptionHandler 호출]
  I -- 아니오 --> K[정상 종료]
  J --> L[예외 로깅 및 알림]
  L --> D
  K --> D
```

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
