package com.danzz.config;

import com.danzz.mapping.SqlCommandTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SelectStatement {

    private SqlCommandTypeEnum commandType;

    private String id;

    private String parameterType;

    private String resultType;

    private String sqlStatement;
}
