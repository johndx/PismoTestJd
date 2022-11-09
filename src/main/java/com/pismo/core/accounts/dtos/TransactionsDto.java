package com.pismo.core.accounts.dtos;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * DTO Class acting as a Payload of Transaction attributes submitted
 * over REST, that can then be assimilated into the internal application
 * Object Model.
 */
public class TransactionsDto {

    /* DTO instance properties */
    @NotNull
    @Min(value = 1, message = "Must be a valid Account Id")
    private long accountId;

    @NotNull
    @Min(value = 1, message = "Operation Type Id is in range 1 to 4")
    @Max(value = 4, message = "Operation Type Id is in range 1 to 4")
    private long operationTypeId;

    @NotNull
    @Min(value = 0, message = "Transaction amount must be greater than zero")
    private float amount;

    public TransactionsDto() {
    }

    public TransactionsDto(long accountId, long operationTypeId, float amount) {
        this.accountId = accountId;
        this.operationTypeId = operationTypeId;
        this.amount = amount;
    }


    public long getAccountId() {
        return accountId;
    }

    public long getOperationTypeId() {
        return operationTypeId;
    }

    public float getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "TransactionsDto{" +
                "accountId=" + accountId +
                ", operationTypeId=" + operationTypeId +
                ", amount=" + amount +
                '}';
    }
}
