# Simple Rest Server
간단한 Restfull Api Server를 만들어 본다.


## Dependency
Gradle
~~~conf
dependencies {
    implementation('org.springframework.boot:spring-boot-starter-data-jpa')
    implementation('org.springframework.boot:spring-boot-starter-web')
    runtimeOnly('com.h2database:h2')
    compileOnly('org.projectlombok:lombok')
    testImplementation('org.springframework.boot:spring-boot-starter-test')
}
~~~

## 순서
1. spring boot initializer를 통해 프로젝트를 생성한다.

2. 패키지 구조를 만든다.
    1. simple
    - 본 내용에서 다룰 내용은 'Simple' 이라는 entity를 저장하고, Rest 형태로 제공할 간단한 프로젝트 이다.
    - package root 에 'simple'라는 이름의 package를 만든다.
  
3.  simple이라는 model을 표현할 class simple을 만든다.
    - @Data는 lombok이며, Getter, Setter, toString등을 자동으로 만들어 준다.
        - Lombok의 경우 피가되고 살이 될터이니 미리미리 알아두자.
    - @Entity는 Java persistence의 entity 이다.
    - simple.Simple

    ~~~java
    package me.potato.demo.simplerestserver.simple;

    import lombok.Data;

    import javax.persistence.Entity;
    import javax.persistence.GeneratedValue;
    import javax.persistence.Id;

    @Data
    @Entity
    public class Simple {

        @Id
        @GeneratedValue
        private Long id;

        private String dataString;
        private Integer dataInteger;

    }
    ~~~

4. Simple을 저장할 Respository를 만든다

    ~~~java
    package me.potato.demo.simplerestserver.simple;

    import org.springframework.data.jpa.repository.JpaRepository;

    public interface SimpleRepository extends JpaRepository<Simple, Long> {

    }
    ~~~