# 📝Pinterest 클론 코딩
<br>

[노션 개발일지](https://www.notion.so/5-5f5481ee2c6a433da01ec4540dfcea1c#9a7b99f76050417dbc57fe5ddd0e628d)
- 와이어프레임, API, ERD

---

### 📌 프로젝트 소개
- 핀터레스트 클론코딩입니다!

<br>

### 📰 제작기간 & 팀원 소개
- 2022-09-09 ~ 2022-09-15

`Front-end`
- 김고은 
- 김선미

`Back-end`
- 이종한
- 김동훈
- 류경현

<br>

### ⛏ 사용 기술

`Back-end`
-   Java 11
-   Spring Boot 2.7.2
-   H2, MySQL
-   Spring security, JWT
-   AWS S3, IAM, EC2

`Front-end`

-   JavaScript
-   React

<br>

### ✔ 구현 기능

1. 로그인/회원가입/로그인 유지
2. 비밀번호 찾기 
3. 사진 등록, 댓글,대댓글 좋아요 기능
4. 내가 작성한 게시물 확인
5. 인피니티 스크롤
6. 게시물 검색 
7. 관심사 별 카테고리 
8. 보드에 유저 별 이미지 저장
9. 팔로잉 팔로워 ,알림

<br>

<br>

### ⚒ Trouble Shooting
FE:
- Pinterest의 메인 컨셉인 masonry layout을 구현하는데 어려움이 있었습니다. 여러가지 방법을 찾아보다 라이브러리를 사용하지 않고 직접 구현하는 방법을 선택했습니다. 
- 최초 구현시 layout을 먼저 구현해보기 위해 외부 사진 API를 적용하여 리스트를 불러오는 작업을 수행하였고, 이후 리덕스를 통해 백엔드에서 만든 API와 연결했습니다.
- 검색 기능을 구현하기 위해 form을 만들고 외부 사진 API에 적용하는 것에는 성공했지만 백엔드 API와 구현하는 것에는 실패해서 아쉬움이 남습니다.

BE:  
- N:M관계를 가지는 저장시스템을 분리한 엔티티를 만들어 N:1 .1:M의 형태로 값을 출력할수 있도록함.
- BindException 오류 : S3를 로컬환경에서 실행시 메타데이터를 찾을수 없어 오류가 발생 -> 환경변수를 설정하고 프로퍼티를 설정하여 해결함.
- enum에 받을 인수 타입을 스트링으로 받았는데 타입오류가 남. -> valueOf를 사용해서 타입을 바꿔주고 실행하여 해결함.


### 에러 핸들러 사용방법.

```java
 throw new BusinessException("로그인 실패",LOGIN_INPUT_INVALID);
```
다음과같이 사용함
BusinessException 클래스의 내용과 ErrorResponse,ErrorCode의 내용을 참조할것.


파일구조
```
    src
    ├─main
    │  ├─java
    │  │  └─spring
    │  │      └─week7
    │  │          ├─Auth //인증 enum을 모아놓은 디렉토리
    │  │          ├─Configuration //스프링 웹 시큐리티,JWT 삽입을 위한 Configuration
    │  │          ├─Controller
    │  │          ├─domain //엔티티를 모아놓은 domin
    │  │          ├─Dto // 응답,요청을 위한 Dto
    │  │          │  ├─Request
    │  │          │  └─Response
    │  │          ├─Errorhandler // 통합 에러처리를 위한 에러핸들러
    │  │          ├─Jwt
    │  │          ├─Repository
    │  │          ├─Service
    │  │          └─Util  //s3 사용을 위한 디렉토리
    │  └─resources
    │      ├─static
    │      └─templates
    └─test
        └─java
            └─spring
                └─week7


```

