package com.innominds.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.innominds.entity.Person;

@Repository("personDao")
@Transactional(propagation = Propagation.REQUIRED)
public class PersonDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void insert(Person person) {
        entityManager.persist(person);
    }

    public List<Person> findAll() {
        return entityManager.createQuery("SELECT p FROM Person p", Person.class).getResultList();
    }

    public Person findOne(Long id) {
        final TypedQuery<Person> query = entityManager.createQuery("SELECT p FROM Person p WHERE p.id=:id", Person.class);
        query.setParameter("id", id);
        return query.getSingleResult();

    }

}
