package com.pismo.core.accounts.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.Set;


/**
 * Accounts Entity for modelling financial accounts
 */
@Entity
@Table(name = "accounts")
public class Accounts implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @JsonProperty("account_id")
    private Long id;

    /* The Document Id is a unique identifier  */
    @Column(name = "document_number", unique = true)
    @JsonProperty("document_number")
    private String documentNumber;

    @OneToMany(mappedBy = "accounts")
    @JsonIgnoreProperties(value = { "operationsTypes", "accounts" }, allowSetters = true)
    @JsonIgnore
    private Set<Transactions> transactions = new HashSet<>();


    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentNumber() {
        return this.documentNumber;
    }
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Set<Transactions> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(Set<Transactions> transactions) {
        if (this.transactions != null) {
            this.transactions.forEach(i -> i.setAccounts(null));
        }
        if (transactions != null) {
            transactions.forEach(i -> i.setAccounts(this));
        }
        this.transactions = transactions;
    }

    public Accounts addTransactions(Transactions transactions) {
        this.transactions.add(transactions);
        transactions.setAccounts(this);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Accounts)) {
            return false;
        }
        return id != null && id.equals(((Accounts) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


    @Override
    public String toString() {
        return "Accounts{" +
                "id=" + getId() +
                ", documentNumber='" + getDocumentNumber() + "'" +
                "}";
    }
}
