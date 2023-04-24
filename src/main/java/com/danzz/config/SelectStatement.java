package com.danzz.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SelectStatement {

    private String id;

    private String parameterType;

    private String resultType;

    private String sqlStatement;
}
