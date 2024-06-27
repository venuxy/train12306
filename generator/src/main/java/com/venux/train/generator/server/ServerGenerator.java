package com.venux.train.generator.server;

import com.venux.train.generator.util.FreemarkerUtil;
import freemarker.template.TemplateException;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServerGenerator {
    static String toPath = "generator/src/main/java/com/venux/train/generator/test/";
    static String pomPath = "generator/pom.xml";
    static {
        new File(toPath).mkdirs();
    }

    public static void main(String[] args) throws Exception, TemplateException {
        SAXReader saxReader = new SAXReader();
        Map<String, String> map = new HashMap<String, String>();
        map.put("pom", "http://maven.apache.org/POM/4.0.0");
        saxReader.getDocumentFactory().setXPathNamespaceURIs(map);
        Document document = saxReader.read(pomPath);
        Node node = document.selectSingleNode("//pom:configurationFile");
        System.out.println(node.getText());

//        FreemarkerUtil.initConfig("test.ftl");
//        Map<String, Object> param = new HashMap<>();
//        param.put("domain", "Test");
//        FreemarkerUtil.generator(toPath + "Test.java", param);

    }
}
