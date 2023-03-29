package ua.com.alevel.config;

import javafx.fxml.FXMLLoader;

import java.util.HashMap;
import java.util.Map;

public class LoaderFactory {

    private final Map<String, FXMLLoader> loaderPageMap = new HashMap<>();

    public LoaderFactory(Class<?> mainClass) {
        final Map<String, String> loaderFactoryMap = Map.ofEntries(
                Map.entry(LoaderPage.LAYOUT.getView(), LoaderPage.LAYOUT.getPage()),
                Map.entry(LoaderPage.DEPARTMENT.getView(), LoaderPage.DEPARTMENT.getPage()),
                Map.entry(LoaderPage.EMPLOYEE.getView(), LoaderPage.EMPLOYEE.getPage()),
                Map.entry(LoaderPage.ATTACH.getView(), LoaderPage.ATTACH.getPage())
        );
        loaderFactoryMap.forEach((k, v) -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(mainClass.getResource(v));
            loaderPageMap.put(k, loader);
        });
    }

    public Map<String, FXMLLoader> getLoaderPageMap() {
        return loaderPageMap;
    }
}
