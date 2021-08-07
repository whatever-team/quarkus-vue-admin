package xyz.easyboot.common.generator;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成器
 * @author wujiawei
 * @see
 * @since 2021/7/1 3:24 下午
 */
@NoArgsConstructor
public class CodeGenerator {
    
    public static String BUSINESS_PACKAGE = "demo";
    public static boolean STOP_WHEN_ERROR = true;
    public static String AUTHOR = "Quarkus-Vue-Admin";
    private static final String PROJECT_PATH = System.getProperty("user.dir");
    private static final String TEMPLATE_FILE_PATH;
    private static final String JAVA_PATH = "/src/main/java";
    private static final String RESOURCES_PATH = "/src/main/resources";
    private static final String BASE_PACKAGE = "xyz.easyboot";
    private static final String DATE;
    private static String CONTROLLER_PACKAGE;
    private static String JPA_SERVICE_PACKAGE;
    private static String PACKAGE_PATH_CONTROLLER;
    private static String PACKAGE_PATH_JPA_SERVICE;
    
    private static void init() {
        CONTROLLER_PACKAGE = BASE_PACKAGE + "." + BUSINESS_PACKAGE + ".resource";
        JPA_SERVICE_PACKAGE = BASE_PACKAGE + "." + BUSINESS_PACKAGE + ".service";
        PACKAGE_PATH_CONTROLLER = packageConvertPath(CONTROLLER_PACKAGE);
        PACKAGE_PATH_JPA_SERVICE = packageConvertPath(JPA_SERVICE_PACKAGE);
    }
    
    public static void genCode(Class... entityClasses) {
        init();
        Class[] var1 = entityClasses;
        int var2 = entityClasses.length;
        
        for(int var3 = 0; var3 < var2; ++var3) {
            Class clazz = var1[var3];
            genController(clazz);
            genJPAService(clazz);
            genApiJs(clazz);
            genVueIndex(clazz);
            genVueForm(clazz);
        }
    }
    
    private static void genController(Class entityClass) {
        Map<String, Object> data = genData(entityClass);
        String entityName = data.get("entityName").toString();
        File file = new File(PROJECT_PATH + JAVA_PATH + PACKAGE_PATH_CONTROLLER + entityName + "Resource.java");
        writeFile(file, data, "resource.txt");
    }
    
    private static void genJPAService(Class entityClass) {
        Map<String, Object> data = genData(entityClass);
        String entityName = data.get("entityName").toString();
        File file = new File(PROJECT_PATH + JAVA_PATH + PACKAGE_PATH_JPA_SERVICE + entityName + "Service.java");
        writeFile(file, data, "service.txt");
    }
    
    private static void genApiJs(Class entityClass) {
        Map<String, Object> data = genData(entityClass);
        String entityName = data.get("entityName").toString();
        String lowerHyphenEntityName = data.get("lowerHyphenEntityName").toString();
        File file = new File(PROJECT_PATH + RESOURCES_PATH + "/gen_web/" + entityName + "/" + lowerHyphenEntityName + ".js");
        writeFile(file, data, "api_js.txt");
    }
    
    private static void genVueIndex(Class entityClass) {
        Map<String, Object> data = genData(entityClass);
        String entityName = data.get("entityName").toString();
        String lowerHyphenEntityName = data.get("lowerHyphenEntityName").toString();
        File file = new File(PROJECT_PATH + RESOURCES_PATH + "/gen_web/" + entityName + "/" + "List.vue");
        writeFile(file, data, "vue_index.txt");
    }
    
    private static void genVueForm(Class entityClass) {
        Map<String, Object> data = genData(entityClass);
        String entityName = data.get("entityName").toString();
        String lowerHyphenEntityName = data.get("lowerHyphenEntityName").toString();
        File file = new File(PROJECT_PATH + RESOURCES_PATH + "/gen_web/" + entityName + "/" + "Edit.vue");
        writeFile(file, data, "vue_form.txt");
    }
    
    private static Configuration getConfiguration() throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_FILE_PATH));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        return cfg;
    }
    
    private static String tableNameConvertLowerCamel(String tableName) {
        return StrUtil.toCamelCase(tableName);
    }
    
    private static String tableNameConvertUpperCamel(String tableName) {
        return StrUtil.upperFirst(StrUtil.toCamelCase(tableName));
    }
    
    private static String tableNameConvertMappingPath(String tableName) {
        tableName = tableName.toLowerCase();
        return "/" + (tableName.contains("_") ? tableName.replaceAll("_", "/") : tableName);
    }
    
    private static void writeFile(File file, Map<String, Object> data, String tplName) {
        if (file.exists()) {
            System.out.println("[跳过] " + file.getName() + " 文件已存在: " + file.getPath());
        } else {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            
            try {
                Configuration cfg = getConfiguration();
                cfg.getTemplate(tplName).process(data, new FileWriter(file));
                System.out.println("[创建] " + file.getName() + " 生成成功: " + file.getPath());
            } catch (Exception var4) {
                System.out.println("[失败] " + file.getName() + " 生成失败");
                if (STOP_WHEN_ERROR) {
                    throw new RuntimeException(var4);
                }
                
                var4.printStackTrace();
            }
            
        }
    }
    
    private static String packageConvertPath(String packageName) {
        return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
    }
    
    private static String upperToLower(String source) {
        return StrUtil.lowerFirst(source);
    }
    
    private static String lowerToUpper(String source) {
        return StrUtil.upperFirst(source);
    }
    
    private static String upperCamelToLowerHyphen(String source) {
        return StrUtil.toSymbolCase(source, '-');
    }
    
    private static Map<String, Object> genData(Class entityClass) {
        String entityName = entityClass.getSimpleName();
        Map<String, Object> data = new HashMap();
        data.put("date", DATE);
        data.put("author", AUTHOR);
        data.put("entityName", entityName);
        data.put("fullEntityName", entityClass.getName());
        data.put("lowerCamelEntityName", upperToLower(entityName));
        data.put("lowerHyphenEntityName", upperCamelToLowerHyphen(entityName));
        data.put("basePackage", BASE_PACKAGE);
        data.put("businessPackage", "." + BUSINESS_PACKAGE);
        data.put("servicePackage", JPA_SERVICE_PACKAGE);
        data.put("webPackage", CONTROLLER_PACKAGE);
        
        // fields
        List<Map<String, Object>> fieldList = new ArrayList<>();
        Field[] fields = entityClass.getFields();
        for (Field field : fields) {
            Map<String, Object> var = new HashMap<>();
            var.put("name", field.getName());
            var.put("type", field.getType().getSimpleName());
            fieldList.add(var);
        }
        data.put("fields", fieldList);
        
        return data;
    }
    
    static {
        TEMPLATE_FILE_PATH = PROJECT_PATH + "/src/test/resources/code-gen-tpl";
        DATE = DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN);
        CONTROLLER_PACKAGE = null;
        JPA_SERVICE_PACKAGE = null;
        PACKAGE_PATH_CONTROLLER = null;
        PACKAGE_PATH_JPA_SERVICE = null;
    }

}
