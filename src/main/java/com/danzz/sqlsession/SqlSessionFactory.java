package com.danzz.sqlsession;

import com.danzz.config.Configuration;
import com.danzz.config.XmlConfigurationParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import com.danzz.io.Resources;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class SqlSessionFactory<T> {

    private String fileName;
    private Configuration configuration;

    public SqlSessionFactory fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public SqlSessionFactory build() {
        try {
            Reader reader = Resources.getResourceAsReader(this.fileName);
            XmlConfigurationParser xmlConfigurationParser = new XmlConfigurationParser(reader);
            this.configuration = xmlConfigurationParser.parse();
            return this;
        } catch (IOException e) {
            log.error("read resources failed:{}",this.fileName);
            e.printStackTrace();
        }
        return this;
    }

    public SqlSession<T> openSession() {
        return new SqlSession<T>(this.configuration);
    }

}
