package com.venux.train.generator.server;

import com.venux.train.generator.util.FreemarkerUtil;
import freemarker.template.TemplateException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServerGenerator {
    static String serverPath = "[module]/src/main/java/com/venux/train/[module]/";
    static String pomPath = "generator/pom.xml";
    static {
        new File(serverPath).mkdirs();
    }

    public static void main(String[] args) throws Exception, TemplateException {
        //获取mabatis-generator.xml文件路径
        String generatorPath = getGeneratorPath();
        String module = generatorPath.replace("src/main/resources/generator-config-", "").replace(".xml", "");
        System.out.println("module: " + module);
        serverPath = serverPath.replace("[module]", module);
        System.out.println("servicePath: " + serverPath);

        //读取table节点

        Document document = new SAXReader().read("generator/" + generatorPath);
        Node table = document.selectSingleNode("//table");
        System.out.println(table);
        Node tableName = table.selectSingleNode("@tableName");
        Node domainObjectName = table.selectSingleNode("@domainObjectName");
        System.out.println(tableName.getText() + "/" + domainObjectName.getText());

        String Domain = domainObjectName.getText();

        String domain = Domain.substring(0, 1).toLowerCase() + Domain.substring(1);

        //数据库表名一般是venux_test，用下划线给单词隔开，controller层一般用-隔开.
        String do_main = tableName.getText().replace("_", "-");

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("Domain", Domain);
        param.put("domain", domain);
        param.put("do_main", do_main);
        System.out.println("组装参数: " + param);

        gen(Domain, param, "service");
        gen(Domain, param, "controller");

    }

    private static void gen(String Domain, Map<String, Object> param, String target) throws IOException, TemplateException {
        FreemarkerUtil.initConfig(target + ".ftl");
        String toPath = serverPath + target + "/";

        new File(toPath).mkdirs();
        String Target = target.substring(0, 1).toUpperCase() + target.substring(1);
        String fileName = toPath + Domain + Target + ".java";
        System.out.println("开始生成: " + fileName);
        FreemarkerUtil.generator(fileName, param);
    }

    private static String getGeneratorPath() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Map<String, String> map = new HashMap<String, String>();
        map.put("pom", "http://maven.apache.org/POM/4.0.0");
        saxReader.getDocumentFactory().setXPathNamespaceURIs(map);
        Document document = saxReader.read(pomPath);
        Node node = document.selectSingleNode("//pom:configurationFile");
        System.out.println(node.getText());
        return node.getText();
    }
}
