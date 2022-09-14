# springweek7
클론코딩

에러 핸들러 사용방법.

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
