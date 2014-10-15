package com.jherenu.phrases

import com.jherenu.phrases.domain.Phrase
import com.jherenu.phrases.repository.PhraseRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan
@EnableAutoConfiguration
class Application implements CommandLineRunner {

    @Autowired
    PhraseRepository repository

    static void main(String[] args) {
        SpringApplication.run Application, args
    }

    @Override
    void run(String... args) throws Exception {
        repository.deleteAll();

        repository.save(new Phrase(phrase: 'My first phrase', language: 'english'));
        repository.save(new Phrase(phrase: 'My second phrase', language: 'english'));
        repository.save(new Phrase(phrase: 'Soy alergico a los crustaceos', language: 'spanish'));
    }
}
