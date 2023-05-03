package com.danzz.config;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Mapper {

    String namespace;

    // id2select
    Map<String, SelectStatement> method2sql;

    List<SelectStatement> selectStatements;
}