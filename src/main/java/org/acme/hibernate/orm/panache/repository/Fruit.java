package org.acme.hibernate.orm.panache.repository;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * Hibernate entity using Repository pattern.
 * 
 * Note:
 * If you don’t want to bother defining getters/setters for your entities, 
 * you can make them extend PanacheEntityBase and Quarkus will generate them for you. 
 * You can even extend PanacheEntity and take advantage of the default ID it provides.
 */

@Entity
@Cacheable
public class Fruit {

    @Id
    @GeneratedValue
    public Long id;

    @Column(length = 40, unique = true)
    public String name;

    public Fruit() {
    }

    public Fruit(String name) {
        this.name = name;
    }
}
