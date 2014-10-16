package com.jherenu.phrases.controllers

import com.jherenu.phrases.Application
import com.jherenu.phrases.domain.Phrase
import com.jherenu.phrases.repository.PhraseRepository
import org.apache.http.HttpStatus
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration

import static com.jayway.restassured.RestAssured.*
import static com.jayway.restassured.matcher.RestAssuredMatchers.*
import static org.hamcrest.Matchers.*

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class PhraseControllerTest {

    @Autowired
    PhraseRepository repository

    Phrase serONoSer
    Phrase toBeOrNotToBe
    Phrase youCompleteMe

    @Value('${local.server.port}')
    int thePort

    @Before
    void setUp() {
        serONoSer = new Phrase(language: 'spanish', phrase: 'Ser o no ser')
        toBeOrNotToBe = new Phrase(language: 'english', phrase: 'To be or not to be')
        youCompleteMe = new Phrase(language: 'english', phrase: 'You complete me')

        repository.deleteAll();
        repository.save([serONoSer, toBeOrNotToBe, youCompleteMe]);

        port = thePort;
    }

    @Test
    void canGetPhrase() {
        def phraseId = serONoSer.getId()

        when().
            get("/phrases/{id}", phraseId).
        then().
            statusCode(HttpStatus.SC_OK).
            body("language", Matchers.is("spanish")).
            body("phrase", Matchers.is("Ser o no ser")).
            body("id", Matchers.is(phraseId))
    }

    @Test
    void shouldReturn404WhenPhraseDoesNotExist() {
        def notFoundPhraseId = "NotAnId"

        when().
            get("/phrases/{id}", notFoundPhraseId).
        then().
            statusCode(HttpStatus.SC_NOT_FOUND)
    }

    @Test
    void canGetAllPhrases() {
        when()
            .get("/phrases").
        then().
            statusCode(HttpStatus.SC_OK).
            body("phrase", hasItems('Ser o no ser', 'To be or not to be', 'You complete me'))
    }

    @Test
    void canGetByLanguage() {
        given()
                .queryParam("language", "english").
        when()
            .get("/phrases/find").
        then().
            statusCode(HttpStatus.SC_OK).
            body("phrase", hasItems('To be or not to be', 'You complete me')).
            body("phrase", not(hasItem('Ser o no ser')))
    }

    @Test
    void canGetByAnotherLanguage() {
        given()
             .queryParam("language", "spanish").
        when()
            .get("/phrases/find").
        then().
            statusCode(HttpStatus.SC_OK).
            body("phrase", hasItem('Ser o no ser')).
            body("phrase", not(hasItems('To be or not to be', 'You complete me')))
    }

    @Test
    void shouldReturn404WhenLanguageDoesNotMatchAnyPhrase() {
        given()
            .queryParam("language", "french").
        when()
            .get("/phrases/find").
        then().
            statusCode(HttpStatus.SC_NOT_FOUND)
    }

}