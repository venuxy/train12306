package com.venux.train.generator.gen;

import com.venux.train.generator.util.DbUtil;
import com.venux.train.generator.util.Field;
import com.venux.train.generator.util.FreemarkerUtil;
import freemarker.template.TemplateException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ServerGenerator {
    static boolean readOnly = false;
    static String vuePath = "admin/src/views/main/";

    static String serverPath = "[module]/src/main/java/com/venux/train/[module]/";
    static String pomPath = "generator/pom.xml";
    static String module = "";
//    static {
//        new File(serverPath).mkdirs();
//    }

    public static void main(String[] args) throws Exception, TemplateException {
        //获取mabatis-generator.xml文件路径
        String generatorPath = getGeneratorPath();
        module = generatorPath.replace("src/main/resources/generator-config-", "").replace(".xml", "");
        System.out.println("module: " + module);
        serverPath = serverPath.replace("[module]", module);
        new File(serverPath).mkdirs();
        System.out.println("serverPath: " + serverPath);

        //读取table节点

        Document document = new SAXReader().read("generator/" + generatorPath);
        Node table = document.selectSingleNode("//table");
        System.out.println(table);
        Node tableName = table.selectSingleNode("@tableName");
        Node domainObjectName = table.selectSingleNode("@domainObjectName");
        System.out.println(tableName.getText() + "/" + domainObjectName.getText());


        //为DbUtil设置数据源
        Node connectionURL = document.selectSingleNode("//@connectionURL");
        Node userId = document.selectSingleNode("//@userId");
        Node password = document.selectSingleNode("//@password");
        System.out.println("url: " + connectionURL.getText());
        System.out.println("user: " + userId.getText());
        System.out.println("password: " + password.getText());
        DbUtil.url = connectionURL.getText();
        DbUtil.user = userId.getText();
        DbUtil.password = password.getText();


        String Domain = domainObjectName.getText();

        String domain = Domain.substring(0, 1).toLowerCase() + Domain.substring(1);

        //数据库表名一般是venux_test，用下划线给单词隔开，controller层一般用-隔开.
        String do_main = tableName.getText().replace("_", "-");

        String tableNameCn = DbUtil.getTableComment(tableName.getText());
        List<Field> fieldList = DbUtil.getColumnByTableName(tableName.getText());
        Set<String> typeSet = getJavaTypes(fieldList);


        //组装参数
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("module", module);
        param.put("Domain", Domain);
        param.put("domain", domain);
        param.put("do_main", do_main);
        param.put("tableNameCn", tableNameCn);
        param.put("fieldList", fieldList);
        param.put("typeSet", typeSet);
        param.put("readOnly", readOnly);
        System.out.println("组装参数: " + param);

        gen(Domain, param, "service", "service");
        gen(Domain, param, "controller/admin", "adminController");
        gen(Domain, param, "req", "saveReq");
        gen(Domain, param, "req", "queryReq");
        gen(Domain, param, "resp", "queryResp");
        genVue(do_main, param);

    }
    public static void genVue(String do_main, Map<String, Object> param) throws IOException, TemplateException {
        FreemarkerUtil.initConfig("vue.ftl");
        new File(vuePath + module).mkdirs();
        String fileName = vuePath + module + "/" +  do_main + ".vue";
        System.out.println("开始生成: " + fileName);
        FreemarkerUtil.generator(fileName, param);
    }

    private static void gen(String Domain, Map<String, Object> param, String packageName, String target) throws IOException, TemplateException {
        FreemarkerUtil.initConfig(target + ".ftl");
        String toPath = serverPath + packageName + "/";
        new File(toPath).mkdirs();
        String Target = target.substring(0, 1).toUpperCase() + target.substring(1);
        String fileName = toPath + Domain + Target + ".java";
        System.out.println("开始生成：" + fileName);
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

    /**
     * 获取所有的java类型，使用Set去重
     * @param fieldList
     * @return
     */
    private static Set<String> getJavaTypes(List<Field> fieldList) {
        Set<String> typeSet = new HashSet<>();
        for (Field field : fieldList) {
            typeSet.add(field.getJavaType());
        }
        return typeSet;
    }
}
