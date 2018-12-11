# Ribbon Rest CLient
 Load Balanced 기능을 포함한 Rest Client를 만들어 본다.


## Project 생성
1. spring boot initializer를 통해 프로젝트를 생성한다.
2. openjdk 11 을 사용한다.


## Dependency
1. Dependencies
 - Web, Ribbon, Lombok

2. 기타 - initializer에 없어서 별도로 넣어야 함
    ~~~xml
    <dependency>
        <groupId>org.springframework.data</groupId>
        <artifactId>spring-data-commons</artifactId>
    </dependency>
    ~~~
   
## Resource
1. application.properties - eureka 미사용
    ~~~properties
    simple-rest-server.ribbon.eureka.enabled=false
    simple-rest-server.ribbon.listOfServers=localhost:8080
    simple-rest-server.ribbon.ServerListRefreshInterval=15000
    ~~~

2. application.properties - eureka 사용
    ~~~properties
    simple-rest-server.ribbon.eureka.enabled=false
    simple-rest-server.ribbon.listOfServers=localhost:8080
    simple-rest-server.ribbon.ServerListRefreshInterval=15000
    ~~~


## Package 구성
1. 패키지 구조를 만든다.
 - config
 - controller
 - service.proxy.simple
 - service.proxy.util

## Entity 생성 - service.proxy.simple.Simple
- simple-rest-server 로 부터 수신될 simple Payload
    ~~~java
    import lombok.Getter;
    import lombok.Setter;
    import lombok.ToString;
    
    @Getter
    @Setter
    @ToString
    public class Simple {
    
        private Long id;
    
        private String dataString;
        private Integer dataInteger;
    }
    ~~~

## Rest Response를 위한 Util 생성 - service.proxy.simple.util.RestResponsePage
- Simple 정보 수신시 함께 전달되는 부가정보를 수신할 Response Payload
- Rest 의 Response 는 한땀한땀 만들어야 함

    ~~~java
    package me.potato.ribbonhardcodeclient.service.proxy.util;

    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
    import lombok.Getter;
    import lombok.Setter;
    import lombok.ToString;

    import java.util.List;


    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class RestResponsePage<T> {

        private int number;
        private int size;
        private int totalPages;
        private int numberOfElements;
        private long totalElements;
        private boolean previousPage;
        private boolean first;
        private boolean nextPage;
        private boolean last;
        private List<T> content;

    }
    ~~~

## PagedSimpleResponse 생성 - service.proxy.util.RestResponsePage
- getForEntity 때문이닷! 언젠가는 개선되겠지!

    ~~~java
    import me.potato.ribbonhardcodeclient.service.proxy.util.RestResponsePage;

    public class PagedSimpleResponse extends RestResponsePage<Simple> {
    }
    ~~~


## proxy client 생성 - service.proxy.simple.SimpleClient
1. 수집, 가공 단계로 나누어 처리함
2. 수집단계에서는 Connection에 대한 Exception을 처리함
3. 참고로 RestTemplate의 exchange는 exception을 발생하지 않음 handling을 위해서는 별도의 Handler를 등록해야 함

    ~~~java 
    package me.potato.ribbonhardcodeclient.service.proxy.simple;

    import lombok.extern.slf4j.Slf4j;
    import me.potato.ribbonhardcodeclient.service.proxy.util.RestResponsePage;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.cloud.netflix.ribbon.RibbonClient;
    import org.springframework.data.domain.Pageable;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Service;
    import org.springframework.web.client.RestClientException;
    import org.springframework.web.client.RestTemplate;
    import org.springframework.web.util.UriComponentsBuilder;

    import java.util.Collection;
    import java.util.Collections;
    import java.util.Optional;

    @Slf4j
    @Service
    @RibbonClient("simple-rest-server")
    public class SimpleClient {

        @Autowired
        private RestTemplate restTemplate;

        public Collection<Simple> getAllSimples(Pageable page) {

            String url = "http://simple-rest-server/v2/api/simples";
            Optional<PagedSimpleResponse> response = getFromExtService(url, page);

            if (response.isPresent()) {
                RestResponsePage<Simple> pagedResources = response.get();
                return pagedResources.getContent();
            }

            return Collections.emptyList();
        }


        private Optional<PagedSimpleResponse> getFromExtService(String url, Pageable page) throws RestClientException {


            String target = UriComponentsBuilder.fromUriString(url)
                    .queryParam("page", page.getPageNumber())
                    .queryParam("size", page.getPageSize())
                    .toUriString();


            try {
                ResponseEntity<PagedSimpleResponse> response = restTemplate.getForEntity(target, PagedSimpleResponse.class);
                if (response.getStatusCode().is2xxSuccessful())
                    return Optional.of(response.getBody());


            } catch (RestClientException ex) {
                log.error("an error occur : " + ex.getMessage());
                throw ex;
            }

            return Optional.empty();
        }


    }
    ~~~

## Controller 생성
1. controller.SimpleProxyController
2. GET "/proxy/simples"
    ~~~java
    package me.potato.ribbonhardcodeclient.controller;

    import lombok.extern.java.Log;
    import me.potato.ribbonhardcodeclient.service.proxy.simple.Simple;
    import me.potato.ribbonhardcodeclient.service.proxy.simple.SimpleClient;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Pageable;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    import java.util.Collection;

    @Log
    @RestController
    @RequestMapping("/proxy")
    public class SimpleProxyController {

        @Autowired
        private SimpleClient simpleClient;

        @GetMapping("/simples")
        public Collection<Simple> getAllSimples(Pageable pageable) throws Exception {

            Collection<Simple> allSimples = simpleClient.getAllSimples(pageable);
            return allSimples;

        }
    }

    ~~~
 
    