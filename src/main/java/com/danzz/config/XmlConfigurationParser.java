package com.danzz.config;

import com.danzz.datasource.DataSourceFactory;
import com.danzz.environment.Environment;
import com.danzz.environment.Environment.EnvironmentBuilder;
import com.danzz.registry.MapperRegistry;
import com.danzz.transaction.TransactionFactory;
import com.danzz.typealias.TypeAliasRegister;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Text;
import org.dom4j.io.SAXReader;

@Slf4j
@Data
@SuperBuilder
public class XmlConfigurationParser implements ConfigParser {

    private InputStream is;

    public XmlConfigurationParser(InputStream is) {
        this.is = is;
    }

    @Override
    public Configuration parse() {
        try {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(is);
            Element root = document.getRootElement();
            Configuration configuration = new Configuration(new MapperRegistry());
            //1.parseElement
            Environment environment = parseEnvironment(root.element("environments"));
            configuration.setEnvironment(environment);
            //2.parseMapper
            List<Mapper> mappers = parseMappers(root);
            for (Mapper mapper : mappers) {
                configuration.addMapper(mapper);
                configuration.addMapper(Class.forName(mapper.getNamespace()));
            }
            return configuration;
        } catch (DocumentException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            log.error("parse xml failed:{}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Mapper parseMapper(String mapperPath) {
        try {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(this.getClass().getClassLoader().getResourceAsStream(mapperPath));
            Element root = document.getRootElement();
            Mapper mapper = new Mapper();
            // 解析mapper
            if (StringUtils.isNotBlank(root.attributeValue("namespace"))) {
                mapper.setNamespace(root.attributeValue("namespace"));
            }
            Iterator<Node> iterator = root.nodeIterator();
            ArrayList<SelectStatement> selectStatements = new ArrayList<SelectStatement>();
            HashMap<String, SelectStatement> method2sql = new HashMap<>();
            while (iterator.hasNext()) {
                Node node = iterator.next();
                if ("select".equals(node.getName())) {
                    SelectStatement selectStatement = new SelectStatement();
                    Element nodeEle = (Element) node;
                    List<Attribute> attributes = nodeEle.attributes();
                    Field[] fields = selectStatement.getClass().getDeclaredFields();
                    // 增加访问权限
                    Arrays.stream(fields).forEach(f -> f.setAccessible(true));
                    for (Attribute attribute : attributes) {
                        Optional<Field> first = Arrays.stream(fields)
                                .filter(f -> f.getName().equalsIgnoreCase(attribute.getName())).findFirst();
                        if (first.isPresent()) {
                            first.get().set(selectStatement, attribute.getValue());
                        }
                    }
                    Iterator<Node> sqlNode = nodeEle.nodeIterator();
                    Text sqlText = (Text) sqlNode.next();
                    selectStatement.setSqlStatement(sqlText.getText());
                    selectStatements.add(selectStatement);
                    method2sql.put(selectStatement.getId(), selectStatement);
                }
            }
            mapper.setSelectStatements(selectStatements);
            mapper.setMethod2sql(method2sql);
            return mapper;
        } catch (DocumentException | IllegalAccessException e) {
            log.error("parse mapper failed:{}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    public List<Mapper> parseMappers(Element root) {
        ArrayList<Mapper> mapperList = new ArrayList<>();
        Iterator<Node> rootItr = root.nodeIterator();
        while (rootItr.hasNext()) {
            Node node = rootItr.next();
            if ("mappers".equals(node.getName())) {
                Element mappers = (Element) node;
                Iterator<Node> mappersItr = mappers.nodeIterator();
                while (mappersItr.hasNext()) {
                    Node mapper = mappersItr.next();
                    if ("mapper".equals(mapper.getName())) {
                        Element mapperEle = (Element) mapper;
                        mapperList.add(parseMapper(mapperEle.attributeValue("resource")));
                    }
                }
            }
        }
        return mapperList;
    }

    public Environment parseEnvironment(Element root) throws InstantiationException, IllegalAccessException {
        EnvironmentBuilder<?, ?> builder = Environment.builder();
        String envDefault = root.attributeValue("default");
        Iterator<Node> iterator = root.nodeIterator();
        while (iterator.hasNext()) {
            Element env = (Element) iterator.next();
            if (envDefault.equals(env.attributeValue("id"))) {
                Iterator<Node> envIterator = env.nodeIterator();
                while (envIterator.hasNext()) {
                    Node node = envIterator.next();
                    if ("transactionManager".equals(node.getName())) {
                        Element txManagerNode = (Element) node;
                        TransactionFactory txFactory = (TransactionFactory) TypeAliasRegister.resolveTypeAlias(
                                txManagerNode.attributeValue("type")).newInstance();
                        builder.transactionFactory(txFactory);
                    }
                    if ("datasource".equals(node.getName())) {
                        Element dataSourceNode = (Element) node;
                        DataSourceFactory dsFactory = (DataSourceFactory) TypeAliasRegister.resolveTypeAlias(
                                dataSourceNode.attributeValue("type")).newInstance();
                        Iterator dsNode = dataSourceNode.nodeIterator();
                        Properties properties = new Properties();
                        while (dsNode.hasNext()) {
                            Node propNode = (Node) dsNode.next();
                            if ("property".equals(propNode.getName())) {
                                Element prop = (Element) propNode;
                                properties.setProperty(prop.attributeValue("name"), prop.attributeValue("value"));
                            }
                        }
                        dsFactory.setProperties(properties);
                        builder.dataSource(dsFactory.getDataSource());
                    }
                }
            }
        }
        return builder.build();
    }
}
