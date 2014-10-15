package com.jherenu.phrases.controllers

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

@ContextConfiguration(loader = SpringApplicationContextLoader.class)
@IntegrationTest("server.port:9000")
class PhraseControllerSpec extends Specification {

//    @Shared
//    ConfigurableApplicationContext context
//
//    void setupSpec() {
//        Future future = Executors
//                .newSingleThreadExecutor().submit(
//                new Callable() {
//                    @Override
//                    public ConfigurableApplicationContext call() throws Exception {
//                        return (ConfigurableApplicationContext) SpringApplication
//                                .run(Application.class)
//                    }
//                })
//        context = future.get(60, TimeUnit.SECONDS)
//    }
//
//    void cleanupSpec() {
//        if (context != null) {
//            context.close()
//        }
//    }

    @Value('${local.server.port}')
    int port

    void "Do my test"() {
        setup:
        println port
        ResponseEntity entity = new RestTemplate().getForEntity("http://localhost:9000/phrases", String.class)

        expect:
        entity.statusCode == HttpStatus.OK
        entity.body == 'No Greetings from Spring Boot!'
    }
}
