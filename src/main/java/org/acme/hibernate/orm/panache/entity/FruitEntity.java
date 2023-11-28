package org.acme.hibernate.orm.panache.entity;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

/**
 * Hibernate entity using Active Record pattern.
 */

@Entity
@NamedQueries({
    @NamedQuery(name = "FruitEntity.getByName", query = "from FruitEntity where name = ?1")
})
@Cacheable
public class FruitEntity extends PanacheEntity {

    @Column(length = 40, unique = true)
    public String name;

    public FruitEntity() {
    }

    public FruitEntity(String name) {
        this.name = name;
    }

    public static FruitEntity findByName(String name){
        return find("#FruitEntity.getByName", name).firstResult();
    }    
}
