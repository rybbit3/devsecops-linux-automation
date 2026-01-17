# [DevSecOps] SonarCloud(SAST) & JaCoCo 기반 CI/CD 파이프라인 구축

## 프로젝트 개요
본 프로젝트는 **Linux 기반 운영 환경**에서의 안정적인 서비스 배포와 장애 대응을 목표로 구축되었습니다.

**'안정적인 인프라 운영'**과 **'효율적인 CI/CD 환경 구성'**을 달성하기 위해, GitHub Actions와 **Bash Shell Script**를 활용하여 배포 과정을 100% 자동화했습니다. 또한, 애플리케이션뿐만 아니라 리눅스 서버 자체의 리소스(Disk, Memory, CPU)를 실시간으로 감시할 수 있는 모니터링 시스템을 구축하여 **잠재적 문제를 사전에 예측**할 수 있도록 설계했습니다.

## Architecture
복잡한 오케스트레이션 도구 없이, **Linux 서버(EC2)** 본연의 기능을 활용한 경량화된 아키텍처입니다.

```mermaid
graph TD
    subgraph "CI Pipeline (GitHub Actions)"
        A[Code Push] --> B(Build & Test)
        B --> C{SonarCloud Analysis}
        C -->|Pass| D[Create Artifact (Jar/Image)]
    end
    
    subgraph "CD Pipeline (Bash Scripting)"
        D -->|SSH Connection| E[Linux Server (EC2)]
        E --> F[Execute deploy.sh]
        F --> G[Health Check Loop]
    end

    subgraph "Monitoring System (Docker Compose)"
        H[Node Exporter] -->|Linux Metrics| I[Prometheus]
        J[App Actuator] -->|App Metrics| I
        I --> K[Grafana Dashboard]
        K -->|Alert| L[Slack Notification]
    end

## 기술스택
- Linux (Rocky/Ubuntu)
- Bash Shell
- Prometheus & Grafana
- GitHub Actions
- SonarCloud & JaCoCo

## 핵심 플로우 (Workflow)
1. CI (Continuous Integration): 코드 품질 검증
[GitHub 개발자]: main 브랜치에 코드를 Push하거나 Pull Request를 생성합니다.

[GitHub Actions]: .github/workflows/sonar-analysis.yml 워크플로우가 트리거됩니다.

[Build & Test]:

./gradlew build 수행 중 test 태스크가 동작합니다.

JaCoCo 플러그인이 테스트 커버리지를 측정하여 jacocoTestReport.xml을 생성합니다.

[Static Analysis]:

Sonar 스캐너가 코드의 정적 분석(버그, 코드 스멜)을 수행하고, JaCoCo 리포트를 통합하여 SonarCloud로 전송합니다.

2. CD (Continuous Deployment): 리눅스 서버 배포
[Artifact Upload]: 품질 검증을 통과한 빌드 결과물(Jar)을 생성합니다.

[SSH & Deploy]:

GitHub Actions가 EC2 리눅스 서버에 SSH로 접속합니다.

서버 내의 **deploy.sh (Bash Script)**를 실행합니다.

스크립트는 기존 프로세스 종료(PID Kill) -> 새 버전 실행 -> Health Check 과정을 자동으로 수행합니다.

3. Monitoring: 장애 감지
[Metrics Collection]:

Node Exporter가 리눅스 서버의 CPU, Disk, Memory 사용량을 수집합니다.

Prometheus가 주기적으로 메트릭을 긁어갑니다(Scraping).

[Visualization & Alert]:

Grafana 대시보드를 통해 서버 상태를 시각화하고, 이상 징후 발생 시 Slack으로 알림을 보냅니다.

## 실습 결과 (Results)
1. 코드 품질 시각화 (SonarCloud & JaCoCo)
JaCoCo가 생성한 코드 커버리지 리포트를 SonarCloud 대시보드에 성공적으로 연동하여, 각 메서드/라인별 커버리지 현황을 시각적으로 확인했습니다. <img width="100%" alt="coverage report" src="https://github.com/user-attachments/assets/6b883216-b4e7-4d7b-8faf-06e8691bcb87" />

2. 보안 취약점 탐지 (SAST)
정적 분석 기능을 통해 '하드코딩된 비밀번호'와 같은 잠재적 보안 취약점을 실제 코드에서 정확히 탐지하고 리포트했습니다. <img width="100%" alt="security report" src="https://github.com/user-attachments/assets/15f72c5e-5812-4f2f-ad7d-461eeb5e85d5" />

3. 리눅스 서버 배포 및 모니터링
(여기에 EC2 터미널 배포 로그 캡처나, Grafana 대시보드 이미지를 추가하면 완벽합니다)

## 회고 (Retrospective)
배운 점
본 프로젝트를 통해 단순한 코드 품질 관리(CI)를 넘어, 실제 운영 환경(Linux)으로의 배포(CD)와 모니터링까지 연결되는 전체 파이프라인을 구축했습니다. 특히 Bash Script를 직접 작성하여 배포 프로세스를 제어하고, Prometheus로 서버의 리소스를 감시하면서 **'안정적인 운영'**이 무엇인지 깊이 이해하게 되었습니다.

어려웠던 점 & 해결
가장 큰 어려움은 Gradle 버전과 SonarQube 플러그인 간의 호환성 문제였습니다. Plugin [id: 'org.sonarqube'] was not found 오류에 직면했으나, AI 도구에만 의존하지 않고 **공식 문서(Release Note)**를 교차 검증하며 버전을 맞추는 과정을 통해 근본적인 문제 해결 능력을 길렀습니다.

향후 개선 계획
현재의 리눅스 단일 서버 구성을 넘어, **고가용성(HA)**을 확보하는 것이 목표입니다.

Scale-out: 트래픽 증가에 대비해 EC2를 오토스케일링 그룹(ASG)으로 묶고 로드밸런서(ALB)를 배치할 계획입니다.

GitOps: ArgoCD를 도입하여 인프라 형상 관리를 고도화할 예정입니다.



## 아키텍처 및 핵심 플로우
<img width="811" height="473" alt="image" src="https://github.com/user-attachments/assets/271b1317-ad61-4a15-91e8-f7579a86f270" />


1. **[GitHub 개발자]:** `main` 브랜치에 코드를 `Push`하거나 Pull Request를 생성합니다.

2. **[GitHub Actions] CI 트리거:** `.github/workflows/sonar-analysis.yml` 워크플로우가 자동으로 실행됩니다.
    
3. **[Gradle + JaCoCo] 빌드 및 테스트:**
    
    - `./gradlew build` 명령어가 실행되며, 이 과정에서 `test` 태스크가 동작합니다.
        
    - `test` 태스크가 실행될 때 **JaCoCo 플러그인**이 코드의 어느 부분이 테스트되었는지 측정하여 `jacocoTestReport.xml` 리포트 파일을 생성합니다.
        
4. **[Gradle + Sonar] 분석 및 리포트:**
    
    - `./gradlew sonar` 명령어가 실행됩니다.
        
    - Sonar 스캐너가 코드 자체의 **정적 분석**(버그, 코드 스멜)을 수행합니다.
        
    - 동시에 3단계에서 생성된 **`jacocoTestReport.xml` 파일을 읽어들여** 테스트 커버리지 데이터를 함께 수집합니다.
        
5. **[SonarCloud] 시각화:**
    
    - Sonar 스캐너가 모든 분석 데이터(정적 분석 + 커버리지)를 SonarCloud 서버로 전송합니다.
        
    - 개발자는 SonarCloud 대시보드에서 분석 결과와 테스트 커버리지 현황을 한눈에 확인합니다.

## 실습결과
1. JaCoCo가 생성한 코드 커버리지 리포트를 SonarCloud 대시보드에 성공적으로 연동하여, 각 메서드/라인별 커버리지 현황을 시각적으로 확인.
<img width="1004" height="551" alt="image" src="https://github.com/user-attachments/assets/6b883216-b4e7-4d7b-8faf-06e8691bcb87" />

2. SonarCloud의 정적 분석 기능을 통해 '하드코딩된 비밀번호'와 같은 잠재적 보안 취약점을 실제 코드에서 정확히 탐지하고 리포트함.
<img width="1011" height="493" alt="image" src="https://github.com/user-attachments/assets/15f72c5e-5812-4f2f-ad7d-461eeb5e85d5" />


## 마무리하며
- **배운점**: 본 프로젝트를 통해 테스트 커버리지(JaCoCo)와 정적 분석(SonarCloud SAST) 데이터를 CI 파이프라인에서 통합 관리함으로써, 개발 초기 단계부터 체계적이고 자동화된 코드 품질 및 보안 관리의 중요성을 깊이 이해했습니다. 이는 안전하고 유지보수하기 쉬운 코드를 만드는 데 필수적인 역량임을 깨달았습니다.
- **어려웠던점**: 가장 큰 어려움은 Gradle버전과 SonarQube Gradle Plugin버전 간의 호환성 문제였습니다. Plugin [id: 'org.sonarqube'] was not found와 같은 다양한 빌드 오류에 직면했으나, 여러 차례의 시행착오와 SonarQube/Gradle 공식 문서를 통해 문제를 성공적으로 해결했습니다. 이 과정에서 AI 도구에 전적으로 의존하기보다, 공식 문서와 디버깅을 통한 근본적인 문제 해결 능력의 중요성을 체감하며 성장할 수 있었습니다.
- **향후 개선 계획**: 이번 SonarCloud 연동 경험을 기반으로, Self-Hosted SonarQube 서버 구축 및 CI/CD 파이프라인 통합을 다음 목표로 설정했습니다. 구체적으로는 AWS EC2/ECS 환경에 SonarQube 서버를 직접 배포하고, Jenkins 또는 GitHub Actions를 활용하여 완전한 온프레미스/클라우드 기반 DevSecOps 파이프라인을 구축함으로써, 시스템 운영 및 관리 역량을 더욱 확장해 나갈 계획입니다.
