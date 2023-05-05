package com.danzz.environment;

import com.danzz.transaction.TransactionFactory;
import javax.sql.DataSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Environment {

    String id;

    TransactionFactory transactionFactory;

    DataSource dataSource;
}
