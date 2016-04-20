package com.innominds;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.innominds.config.DatabaseConfig;
import com.innominds.entity.Person;
import com.innominds.service.PersonService;

public class Application {

    public static void main(String[] args) {
        final AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(DatabaseConfig.class);
        ctx.refresh();

        final PersonService personService = ctx.getBean(PersonService.class);
        System.err.println("----------------------");

        Person person = new Person();
        person.setName("BHANU");
        personService.savePerson(person);

        person = new Person();
        person.setName("KARTHI");
        personService.savePerson(person);

        System.err.println(personService.getAllPerson());

        final Long id = personService.getAllPerson().get(0).getId();

        System.err.println(personService.findOne(id));

        System.err.println(personService.findOne(id));

        ctx.close();
    }
}
