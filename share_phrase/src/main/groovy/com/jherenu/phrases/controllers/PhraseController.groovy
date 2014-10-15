package com.jherenu.phrases.controllers

import com.jherenu.phrases.domain.Phrase
import com.jherenu.phrases.repository.PhraseRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class PhraseController {

    @Autowired
    PhraseRepository phrases

    @RequestMapping(value = "/phrases", method=RequestMethod.GET)
    def getAllPhrases() {
        return phrases.findAll()
    }

    @RequestMapping(value = "/phrases", method=RequestMethod.POST)
    def addPhrase(@RequestBody Phrase phrase) {
        phrases.save(phrase)
    }

    @RequestMapping(value = "/phrases/{id}", method = RequestMethod.GET)
    def getPhrase(@PathVariable("id") String id) {
        def phrase = phrases.findOne(id)
        return phrase ?: new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value='/phrases/find', method=RequestMethod.GET)
	public @ResponseBody List findByLanguage(
			// Tell Spring to use the "language" parameter in the HTTP request's query
			// string as the value for the language method parameter
			@RequestParam('language') String language) {
		return phrases.findByLanguage(language);
	}
}
