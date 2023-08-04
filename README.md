# 과제 필수 사항

## 지원자의 성명

임승택

## 애플리케이션의 실행 방법 (엔드포인트 호출 방법 포함)

### 요구 사항
1. local 환경에서 3306번 포트를 Mysql 8.0 버전에 연결해주세요
2. JAVA 17 환경이 필요합니다.

### 실행 방법
1. clone
```text
git clone https://github.com/namest504/wanted-pre-onboarding-challenge-be-task-July
```

2. application-dev.yml 수정
```text
cd wanted-pre-onboarding-challenge-be-task-July/src/main/resources
vi application-dev.yml
```

3. 변수 설정
```yaml
jwt:
  secret: 시크릿 코드 값 입력 필요
```

4.빌드
```text
./gradlew clean build
```

5.jar 파일로 경로 이동
```text
cd wanted-pre-onboarding-challenge-be-task-July/build/libs
```

6.실행
```text
java -jar -Dspring.profiles.active=dec board-0.0.1-SNAPSHOT.jar
```

### 엔드포인트 호출 방법

과제 1. 회원가입
```text
POST /register
```
과제 2. 로그인
```text
POST /login
```
과제 3. 새로운 게시글 생성
```text
POST /article
```
과제 4. 게시글 목록 조회

```text
POST /article/page/{page}
```
과제 5. 특정 게시글 조회
```text
GET /article/{id}
```
과제 6. 특정 게시글 수정
```text
PATCH /article/{id}
```
과제 7. 특정 게시글 삭제
```text
DELETE /article/{id}
```

## 데이터베이스 테이블 구조

![db](https://github.com/namest504/wanted-pre-onboarding-backend/assets/61047602/3b227d48-e102-4b87-b5a5-f0ae8069138a)

## 구현한 API의 동작을 촬영한 데모 영상 링크

[Demo](https://youtu.be/vN92AWLYvHE)

## 구현 방법 및 이유에 대한 간략한 설명

1. validation을 사용하였습니다.
   - 요청의 유저의 이메일과 비밀번호의 유효성을 판단을 하기위해 사용하였습니다.

2. BCryptPasswordEncoder를 사용하였습니다.
   - 비밀번호를 암호화 하여 저장하고, 단방향 암호화를 구현 하기위해서 사용하였습니다.

3. JWT 토큰 방식을 사용하였습니다.
    -  인증 코드를 서버에서 저장하지 않고 클라이언트에 저장하여 서버의 부담을 줄일 수 있어서 사용하였습니다.

4. Spring Data JPA를 사용하였습니다.
    - 비교적 간단한 CRUD와 Pagination을 구현하기 위해 사용하였습니다.

5. Spring Security를 사용하였습니다.
   - 엔드포인트를 호출하는 클라이언트를 특정하기 위한 인증 로직을 구현하기 위해 사용하였습니다.

## API 명세(request/response 포함)

[Docs](https://documenter.getpostman.com/view/26517295/2s9XxvUviB)

### 명세서 테이블
| 과제 요구사항 번호 | API 용도       | 엔드 포인트               | 메소드 타입 |
|------------|--------------|----------------------|--------|
| 1          | 회원가입         | /register            | POST   |
| 2          | 로그인          | /login               | POST   |
| 3          | 게시글 생성       | /article             | POST   |
| 4          | 게시글 목록 조회    | /article/page/{page} | GET    |
| 5          | 특정 게시글 조회    | /article/{id}        | GET    |
| 6          | 특정 게시글 수정    | /article/{id}        | PATCH  |
| 7          | 특정 게시글 삭제    | /article/{id}        | DELETE |

---

### 과제 1. 회원가입
[Request]
```text
POST /register
{
  "userEmail": "test@example.com",
  "userPassword": "testtest"
}
```
[Response]
```text
{
  test@example.com 회원가입 성공
}
```
[Error Code]
```text
400: 부적절한 입력값
```

---

### 과제 2. 로그인
[Request]
```text
POST /login
{
  "userEmail": "test@example.com",
  "userPassword": "testtest"
}
```
[Response]
```text
{
  "accessToken": "...AccessToken Value..."
}
```
[Error Code]
```text
400: 부적절한 입력값
404: 존재하지 않는 아이디
```

---

### 과제 3. 새로운 게시글 생성
[Request]
```text
POST /article
header
    {
        "Authorization" : "Bearer ...Access Token value..."
    }
body
    {
      "title": "입력된 게시글 제목",
      "content": "입력된 게시글 내용"
    }
```
[Response]
```text

{
  [게시글 번호] 게시글 생성 성공
}
```
[Error Code]
```text
401: AccessToken 문제
```

---

### 과제 4. 게시글 목록 조회
[Request]
```text
POST /article/page/{page}
body
    {
      "title": "입력된 게시글 제목",
      "content": "입력된 게시글 내용"
    }
```
[Response]
```text
{
   "totalPage": 1,
   "articleResponses": [
     {
         "id": 1,
         "title": "1번 게시글 제목",
         "content": "입력된 게시글 내용",
         "writer": "test@example.com"
     },
     {
         "id": 2,
         "title": "2번 게시글 제목",
         "content": "입력된 게시글 내용",
         "writer": "test2@example.com"
     }
   ]
}
```

---

### 과제 5. 특정 게시글 조회
[Request]
```text
GET /article/{id}
```
[Response]
```text
{
   "id": 1,
   "title": "입력된 게시글 제목",
   "content": "입력된 게시글 내용",
   "writer": "test@example.com"
}
```
[Error Code]
```text
404: 존재하지 않는 게시글
```

---

### 과제 6. 특정 게시글 수정
[Request]
```text
PATCH /article/{id}
header
    {
        "Authorization" : "Bearer ...Access Token value..."
    }
```
[Response]
```text
{
    "title": "수정된 제목",
    "content": "수정된 내용"
}
```
[Error Code]
```text
400: 본인 글만 수정 가능
401: AccessToken 문제
404: 존재하지 않는 게시글
```

---

### 과제 7. 특정 게시글 삭제
[Request]
```text
DELETE /article/{id}
header
    {
        "Authorization" : "Bearer ...Access Token value..."
    }
```
[Response]
```text
{
  [게시글 번호] 게시글 삭제 성공
}
```
[Error Code]
```text
400: 본인 글만 삭제 가능
401: AccessToken 문제
404: 존재하지 않는 게시글
```

# 추가 기능

## 테스트 코드 작성

- 회원가입 성공
- 회원가입 이메일 형식 문제 실패
- 비밀번호 최소 길이 문제 실패

## docker-compose를 통한 실행 환경 구축

### docker-compose를 통한 실행 방법

1. Git clone
```text
git clone https://github.com/namest504/wanted-pre-onboarding-challenge-be-task-July
```
2. build
```text
./gradlew clean build
```
3. docker-compose 컨테이너 환경 실행
```text
docker-compose up --build
```

## AWS를 통한 실행 환경 구축

### AWS 배포 환경 URL

```text
https://list-api.link
```

### AWS Architecture

![AWS Architecture](https://github.com/namest504/wanted-pre-onboarding-backend/assets/61047602/9650d3b0-69e1-4f1e-88db-80c256f4bd00)

[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Fnamest504%2Fwanted-pre-onboarding-backend&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)
