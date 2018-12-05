# Simple Rest Server
간단한 Restfull Api Server를 만들어 본다.


## Dependency
1. Gradle
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

2. H2
    ~~~conf
    spring.h2.console.enabled=true
    spring.h2.console.path=/h2-console
    ~~~

    - 테스트 용도로 사용할 메모리 DB
    - 실행시마다 초기화 됨
    - console 활성화
    - jdbc:h2:mem:testdb

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

## New Simple Controller
1. Http Status를 이용하여 Client가 상태를 인지 할 수 있어야 한다.
    - HEAD Method는 GET의 Header만을 이용한다.
2. 예컨데
    - 전체 data 조회시 Paging 한다.
    - 개별 조회시 없으면 noContent를 반환한다.


    ~~~java
    package me.potato.demo.simplerestserver.simple;

    import lombok.extern.java.Log;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.Optional;

    import static org.springframework.http.ResponseEntity.ok;

    @Log
    @RestController
    @RequestMapping("/v2")
    public class NewSimpleController {

        @Autowired
        private SimpleRepository simpleRepository;


        @GetMapping("/api/simples")
        public Page<Simple> getAllSimples(Pageable pageable) {
            return simpleRepository.findAll(pageable);

        }

        @GetMapping("/api/simples/{id}")
        public ResponseEntity getSimple(@PathVariable Long id) {
            Optional<Simple> byId = simpleRepository.findById(id);

            if (byId.isPresent())
                return ok().body(byId.get());
            else
                return ResponseEntity.noContent().build();
        }

        @PostMapping("/api/simples")
        public ResponseEntity createSimple(@RequestBody Simple simple) {
            Optional<Simple> byDataString = simpleRepository.findByDataString(simple.getDataString());
            if (byDataString.isPresent())
                return ResponseEntity.status(HttpStatus.CREATED).body(simpleRepository.saveAndFlush(simple));
            else
                return ResponseEntity.badRequest().build();
        }

        @PatchMapping("/api/simples")
        public ResponseEntity patchSimple(@RequestBody Simple simple) {

            Optional<Simple> byId = simpleRepository.findById(simple.getId());
            if (byId.isPresent())
                return ResponseEntity.status(HttpStatus.OK).body(simpleRepository.saveAndFlush(simple));
            else
                return ResponseEntity.status(HttpStatus.CREATED).body(simpleRepository.saveAndFlush(simple));

        }

        @DeleteMapping("/api/simples/{id}")
        public ResponseEntity deleteSimple(@PathVariable Long id) {

            Optional<Simple> byId = simpleRepository.findById(id);

            if (byId.isPresent()) {
                simpleRepository.deleteById(id);
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

        }
    }


    ~~~