package ua.com.alevel.config;

import javafx.stage.Stage;
import ua.com.alevel.persistence.jdbc.JdbcService;
import ua.com.alevel.util.ResourcesUtil;

public class AppConfig {

    public static void init(Stage stage, Class<?> mainClass) {
        JdbcService.getInstance().initDBState(ResourcesUtil.getResources(mainClass.getClassLoader()));
        LoaderFactory loaderFactory = new LoaderFactory(mainClass);
        PageConfig pageConfig = new PageConfig(stage, loaderFactory);
    }
}
