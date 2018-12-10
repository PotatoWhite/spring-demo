# Ribbon Rest CLient
 Load Balanced 기능을 포함한 Rest Client를 만들어 본다.


## Dependency
1. Dependencies
 - Web, Ribbon, Lombok
   
## Resource
1. application.properties
    ~~~conf
    simple-rest-server.ribbon.eureka.enabled=false
    simple-rest-server.ribbon.listOfServers=localhost:8081,localhost:8082
    simple-rest-server.ribbon.ServerListRefreshInterval=15000
    ~~~

## SimpleController
1. spring boot initializer를 통해 프로젝트를 생성한다.

2. 패키지 구조를 만든다.
    