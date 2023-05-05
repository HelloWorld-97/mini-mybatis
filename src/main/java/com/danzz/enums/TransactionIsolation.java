package com.danzz.enums;

import java.sql.Connection;
import lombok.Getter;

@Getter
public enum TransactionIsolation {
    NONE(Connection.TRANSACTION_NONE),
    READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),
    READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),
    REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),
    SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);
    int level;

    TransactionIsolation(int level) {
        this.level = level;
    }
}
