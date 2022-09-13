# star_project
별애별 모임

박선후 김지민 황우빈 김민균 이가영

# 개발목적
천체 관측가들을 위하여 미래의 미세먼지와 기상조건을 정보를 토대로 관측지를 추천해주는 커뮤니티

# 개발기간
- 2022.07.15 ~ 2022.08.26

# 설계도 
## 기능설계서
[기능설계서](https://docs.google.com/spreadsheets/d/1NgKfBHfHfzXg1j538HpBY_wSZg3jVN0bUqPu7uzfdKE/edit#gid=489500311, "기능 설계서")
## 화면설계서
[화면설계서](https://ovenapp.io/view/8v5Urdj5kv3RY7UBRAnXWz1JgZ8lrbMN/, "화면 설계서")
## ERD
![ERD](/imgs/STAR_ERD.png)

#사용 툴
* log4j2 1.16
* Json-Simple 1.1.1
* bootstrap 5.2
* gsmtp
* MySQL 8.0.30
* kakaoMapAPI
* 공공데이터 API ([기상청_단기예보 조회 서비스](https://www.data.go.kr/iim/api/selectAPIAcountView.do))

# 팀원 및 역할 분담
- 박선후 - 조장
 - 데이터 분석
   - 미세먼지 원인 분석 및 시각화
   - 중국 미세먼지와 우리나라 대기 조건 및 미세먼지 데이터를 이용한 미세먼지 예측

- 김지민
  - 프론트
    - 전국 시-도별 미세먼지 분석 및 시각화
    - 회원 탈퇴 기능 구현, 신고하기 기능 구현, 프론트

- 황우빈
  - 백엔드
    - 서울과 인천의 월별 미세먼지 시각화
    - 회원가입 기능 구현, 계정 찾기 기능 구현, 정보 변경 기능 구현, 관리자 페이지 기능 구현, 상세 조회 페이지 구현

- 김민균
  - 백엔드
    - 서울과 인천의 월별 미세먼지 시각화
    - 로그인 기능 구현, 검색 기능 구현, 댓글 기능 구현, 상세 날씨 조회 기능 구현


- 이가영
  - 백엔드
    - 미세먼지 원인 분석 및 시각화
    - 게시판 리스트 조회 기능 구현, 마이 페이지 기능 구현, 게시글 이미지 기능 구현, 페이징 처리, 카카오 맵 API 연동, 오픈 API 연동


# 코드 작성 방식
## MVC2 모델을 사용하여 게시판 기능 구현

### Model
- star_project/Star/src/main/java/com/star/service/

### View
- star_project/Star/src/main/resources/templates/

### Controller
- star_project/Star/src/main/java/com/star/controller/

# 한계점 및 시행착오

LGBM을 사용할 때 정확도가 높지 않아 LSTM 바꿔서 모델을 제작.

지도를 불러오는 Ajax에서 API를 불러오는 데 시간이 오래 걸리는 문제점이 발생
따로 Controller와 Model로 기능을 분류해서 스케줄러로 돌리는 방법을 찾음
그래서 기능을 분류는 하였지만 시간적으로 부족한 점이 발생하면서 스케줄까지는 돌리지 못했다.

개인 인증을 휴대폰번호로 할 예정이었으나 비용적인 문제로 포기하고 이메일 인증으로 진행함.

초기엔 로그인이 된 유저인지 아니인지 구분을 model에 담아 post방식으로 구분하는것으로 구현을 했었는데,
redirect를 사용 할 때 새로고침을 할 때 마다 정보가 날라가는 현상을 발견하여 로그인 정보를 HttpSession 라이브러리를 이용해 세션에 담아 구분하는것으로 바꿈.

깃헙을 이용하여 협업을 할 때 서로의 코드가 얽히다 보니 시행착오가 있었음.

프로젝트 제작 과정중 코로나 이슈와 학원 침수 이슈로 제작 일정을 제대로 소화하지 못했다.

// 디자인적으로 기능을 더 추가하고싶었지만 그러지 못해 아쉬웠다.
