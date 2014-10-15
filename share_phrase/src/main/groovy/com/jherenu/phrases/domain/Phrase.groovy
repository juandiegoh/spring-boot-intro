package com.jherenu.phrases.domain

import org.springframework.data.annotation.Id

class Phrase {

    @Id
    String id
    String language
    String phrase

    String toString() {
        "Phrase[phrase:$phrase, language:$language]"
    }
}
