package com.danzz.config;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
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

    private SAXReader reader;

    private String path;

    public XmlConfigurationParser(SAXReader reader, String path) {
        this.reader = reader;
        this.path = path;
    }

    public static void main(String[] args) {
        XmlConfigurationParser parser = new XmlConfigurationParser(new SAXReader(), "");
        Configuration config = parser.parse();
        System.out.println(config);
    }

    @Override
    public Configuration parse() {
        try {
            Document document = reader.read(new File(path));
            Element mapperEle = document.getRootElement();
            Configuration configuration = new Configuration();
            Mapper mapper = new Mapper();
            // 解析mapper
            if (StringUtils.isNotBlank(mapperEle.attributeValue("namespace"))) {
                mapper.setNamespace(mapperEle.attributeValue("namespace"));
            }
            Iterator<Node> iterator = mapperEle.nodeIterator();
            ArrayList<SelectStatement> selectStatements = new ArrayList<SelectStatement>;
            while (iterator.hasNext()) {
                Node node = iterator.next();
                if ("select".equals(node.getName())) {
                    SelectStatement selectStatement = new SelectStatement();
                    Element nodeEle = (Element) node;
                    List<Attribute> attributes = nodeEle.attributes();
                    Field[] fields = selectStatement.getClass().getDeclaredFields();
                    for (Attribute attribute : attributes) {
                        Optional<Field> first = Arrays.stream(fields)
                                .filter(f -> f.getName().equalsIgnoreCase(attribute.getName())).findFirst();
                        if (first.isPresent()) {
                            first.get().set(String.class, attribute.getValue());
                        }
                    }
                    Iterator<Node> sqlNode = nodeEle.nodeIterator();
                    Text sqlText = (Text) sqlNode.next();
                    selectStatement.setSqlStatement(sqlText.getText());
                    selectStatements.add(selectStatement);
                }
            }
            mapper.setSelectStatements(selectStatements);
            configuration.setMapper(mapper);
            return configuration;
        } catch (DocumentException | IllegalAccessException e) {
            log.error("invalid file path:{}", path);
            e.printStackTrace();
        }
        return null;
    }

    // 将xml解析成json，然后再通过json反序列化成对象，不可行应该还是定制化解析
    public void parseElement(Element element, StringBuilder stringBuilder) {
        stringBuilder.append("\"");
        stringBuilder.append(element.getName());
        stringBuilder.append("\":{");
        List<Attribute> attributes = element.attributes();
        for (int i = 0; i < attributes.size(); i++) {
            stringBuilder.append("\"");
            stringBuilder.append(attributes.get(i).getName());
            stringBuilder.append("\":");
            stringBuilder.append("\"");
            stringBuilder.append(attributes.get(i).getValue());
            stringBuilder.append("\",");
        }
        Iterator<Node> iterator = element.nodeIterator();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node instanceof Element) {
                Element eleNode = (Element) node;
                parseElement(eleNode, stringBuilder);
            } else if (node instanceof Text) {

            }
        }
        stringBuilder.append("}");
    }
}
