package com.pismo.core.accounts.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Account  Transactions associated with each account.
 */
@Entity
@Table(name = "transactions")

public class Transactions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @JsonProperty("Transaction Id")
    private Long id;

    @Column(name = "amount")
    private Float amount;

    @Column(name = "event_date")
    private ZonedDateTime eventDate;

    @OneToOne
    @JoinColumn(unique = false)
    @JsonProperty("Operation Type")
    private OperationsTypes operationsTypes;

    @ManyToOne
    @JsonIgnoreProperties(value = { "transactions" }, allowSetters = true)
    @JsonProperty("Account")
    private Accounts accounts;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getAmount() {
        return this.amount;
    }
    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public ZonedDateTime getEventDate() {
        return this.eventDate;
    }

    public void setEventDate(ZonedDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public OperationsTypes getOperationsTypes() {
        return this.operationsTypes;
    }

    public void setOperationsTypes(OperationsTypes operationsTypes) {
        this.operationsTypes = operationsTypes;
    }

    public Accounts getAccounts() {
        return this.accounts;
    }

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transactions)) {
            return false;
        }
        return id != null && id.equals(((Transactions) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transactions{" +
                "id=" + getId() +
                ", amount=" + getAmount() +
                ", eventDate='" + getEventDate() + "'" +
                "}";
    }
}
