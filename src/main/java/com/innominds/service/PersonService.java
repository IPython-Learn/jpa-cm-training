package com.innominds.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innominds.dao.PersonDao;
import com.innominds.entity.Person;

@Service
public class PersonService {

    @Autowired
    PersonDao personDao;

    public void savePerson(Person person) {
        personDao.insert(person);
    }

    public List<Person> getAllPerson() {
        return personDao.findAll();
    }

    public Person findOne(Long id) {
        return personDao.findOne(id);
    }
}
