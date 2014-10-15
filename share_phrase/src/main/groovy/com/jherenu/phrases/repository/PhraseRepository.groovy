package com.jherenu.phrases.repository

import com.jherenu.phrases.domain.Phrase
import org.springframework.data.mongodb.repository.MongoRepository

public interface PhraseRepository extends MongoRepository<Phrase, String> {

    List findByLanguage(String language)
}
