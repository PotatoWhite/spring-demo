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

## Resource
1. application.properties

    ~~~conf
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format-sql=true
    ~~~

## SimpleController
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

4. Simple을 저장할 Respository를 작성한다.
    - Jpa는 interface만 작성하면 된다.
    - 별도의 Bean 등록과정은 필요 없으며, JpaRespository Interface 내부에서 Bean 등록한다.

    ~~~java
    package me.potato.demo.simplerestserver.simple;

    import org.springframework.data.jpa.repository.JpaRepository;

    public interface SimpleRepository extends JpaRepository<Simple, Long> {

    }
    ~~~

5. 외부의 http Connection을 받아들일 controller를 작성 한다.
    - CRUD를 위해 다음의 Routing 정보를 등록한다.
        - GET /api/simples -> getSimples
        - GET /api/simples/{id} getSimple(@PathVariable Long id)
        - POST /api/simples
        - PATCH /api/simples
        - DELETE /api/simples/{id}
    ~~~java
    package me.potato.demo.simplerestserver.simple;

    import lombok.extern.java.Log;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.Optional;

    @Log
    @RestController
    @RequestMapping("/v1")
    public class SimpleController {

        @Autowired
        private SimpleRepository simpleRepository;


        @GetMapping("/api/simples")
        public List<Simple> getAllSimples() {
            return simpleRepository.findAll();
        }


        @GetMapping("/api/simples/{id}")
        public Simple getSimple(@PathVariable Long id) {
            Optional<Simple> byId = simpleRepository.findById(id);
            return byId.orElse(null);
        }

        @PostMapping("/api/simples")
        public Simple createSimple(@RequestBody Simple simple) {
            return simpleRepository.save(simple);
        }

        @PutMapping("/api/simples")
        public Simple patchSimple(@RequestBody Simple simple) {
            return simpleRepository.save(simple);
        }

        @DeleteMapping("/api/simples/{id}")
        public void deleteSimple(@PathVariable Long id) {
            simpleRepository.deleteById(id);
        }
    }
    ~~~

## V2 Simple Controller
1. /api/simples/{id} 
    - 호출 결과를 

    ~~~java
    package me.potato.demo.simplerestserver.simple;

    import lombok.extern.java.Log;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.Optional;

    @Log
    @RestController
    @RequestMapping("/v2")
    public class V2SimpleController {

        @Autowired
        private SimpleRepository simpleRepository;


        @GetMapping("/api/simples")
        public List<Simple> getAllSimples() {
            return simpleRepository.findAll();
        }


        @GetMapping("/api/simples/{id}")
        public Simple getSimple(@PathVariable Long id) {
            Optional<Simple> byId = simpleRepository.findById(id);
            if (byId.isPresent())
                return byId.get();
            else
                return null;
        }

        @PostMapping("/api/simples")
        public Simple createSimple(@RequestBody Simple simple) {
            return simpleRepository.save(simple);
        }

        @PutMapping("/api/simples")
        public Simple patchSimple(@RequestBody Simple simple) {
            return simpleRepository.save(simple);
        }

        @DeleteMapping("/api/simples/{id}")
        public void deleteSimple(@PathVariable Long id) {
            simpleRepository.deleteById(id);
        }
    }
    ~~~