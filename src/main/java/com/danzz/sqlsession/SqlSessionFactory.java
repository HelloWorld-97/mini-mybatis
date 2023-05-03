package com.danzz.sqlsession;

import com.danzz.config.Configuration;
import com.danzz.config.XmlConfigurationParser;
import java.io.InputStream;
import lombok.Data;

@Data
public class SqlSessionFactory<T> {

    private String fileName;
    private Configuration configuration;

    public SqlSessionFactory fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public SqlSessionFactory build() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(this.fileName);
        XmlConfigurationParser xmlConfigurationParser = new XmlConfigurationParser(is);
        this.configuration = xmlConfigurationParser.parse();
        return this;
    }

    public SqlSession<T> openSession() {
        return new SqlSession<T>(this.configuration);
    }

}
