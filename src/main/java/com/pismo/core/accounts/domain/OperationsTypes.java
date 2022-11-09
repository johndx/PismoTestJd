package com.pismo.core.accounts.domain;

import javax.persistence.*;
import java.io.Serializable;


/**
 * OperationsTypes models Operation Type Base Data
 * Note that depending on the amount of Operations data that
 * would be required and whether it would change dynamically
 * this class could just be implemented as an Enum.
 */
@Entity
@Table(name = "operations_types")
public class OperationsTypes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OperationsTypes)) {
            return false;
        }
        return id != null && id.equals(((OperationsTypes) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


    @Override
    public String toString() {
        return "OperationsTypes{" +
                "id=" + getId() +
                ", description='" + getDescription() + "'" +
                "}";
    }
}
