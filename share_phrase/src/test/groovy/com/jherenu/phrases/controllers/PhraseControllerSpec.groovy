package com.jherenu.phrases.controllers

import com.jherenu.phrases.Application
import com.jherenu.phrases.domain.Phrase
import com.jherenu.phrases.repository.PhraseRepository
import groovyx.net.http.HttpResponseException
import groovyx.net.http.RESTClient
import net.sf.json.JSONArray
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
class PhraseControllerSpec extends Specification {

    @Autowired
    PhraseRepository repository

    Phrase serONoSer
    Phrase toBeOrNotToBe
    Phrase youCompleteMe

    @Value('${local.server.port}')
    int thePort

    RESTClient client

    void setup() {
        serONoSer = new Phrase(language: 'spanish', phrase: 'Ser o no ser')
        toBeOrNotToBe = new Phrase(language: 'english', phrase: 'To be or not to be')
        youCompleteMe = new Phrase(language: 'english', phrase: 'You complete me')

        repository.deleteAll();
        repository.save([serONoSer, toBeOrNotToBe, youCompleteMe]);

        this.client = new RESTClient( "http://localhost:${thePort}/" )
    }

    void "can get phrase"() {
        given:
        def phraseId = serONoSer.getId()

        when:
        def resp = client.get([path: "/phrases/${phraseId}"])

        then:
        with(resp) {
            status == 200
            contentType == "application/json"
        }

        with(resp.data) {
            language == "spanish"
            phrase == "Ser o no ser"
            id == phraseId
        }
    }

    void "if phrase does not exist return 404"() {
        given:
        def notFoundPhraseId = "NotAnId"

        when:
        client.get([path: "/phrases/${notFoundPhraseId}"])

        then:
        def e = thrown(HttpResponseException)
        with(e.response) {
            status == HttpStatus.NOT_FOUND.value()
        }
    }

    void "can get all phrases"() {
        when:
        ResponseEntity resp = new RestTemplate().getForEntity("http://localhost:${thePort}/phrases", String.class)

        then:
        with(resp) {
            statusCode == HttpStatus.OK
            headers.getContentType() includes(MediaType.APPLICATION_JSON)
        }

        with(resp.body) {
            def json = JSONArray.fromObject(it)
            json.phrase.every { it in ['Ser o no ser', 'To be or not to be', 'You complete me'] }
        }
    }

    void "can get by language"() {
        given:

        when:
        def resp = client.get([path: "/phrases/find", query: [language: 'english']])

        then:
        with(resp) {
            status == 200
            contentType == "application/json"
        }

        with(resp.data) {
            def json = JSONArray.fromObject(it)
            json.phrase.every { it in ['To be or not to be', 'You complete me'] }
            json.phrase.every { it != 'Ser o no ser' }
        }
    }

    void canGetByAnotherLanguage() {
        given:

        when:
        def resp = client.get([path: "/phrases/find", query: [language: 'spanish']])

        then:
        with(resp) {
            status == 200
            contentType == "application/json"
        }

        with(resp.data) {
            def json = JSONArray.fromObject(it)
            !json.phrase.any { it in ['To be or not to be', 'You complete me'] }
            json.phrase.every { it == 'Ser o no ser' }
        }
    }

    void shouldReturn404WhenLanguageDoesNotMatchAnyPhrase() {
        given:
        def unknownLanguage = "geringoso"

        when:
        client.get([path: "/phrases/find", query: [language: "$unknownLanguage"]])

        then:
        def e = thrown(HttpResponseException)
        with(e.response) {
            status == HttpStatus.NOT_FOUND.value()
        }
    }
}
