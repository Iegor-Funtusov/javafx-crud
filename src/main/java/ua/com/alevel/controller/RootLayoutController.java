package ua.com.alevel.controller;

import ua.com.alevel.config.LoaderPage;
import ua.com.alevel.reactive.rx.PubSubRX;

public class RootLayoutController {

    public void showDepartments() {
        // RX Java
        PubSubRX.getInstance().publishLoaderPage(LoaderPage.DEPARTMENT);
        // java core
//        PubSub.getInstance().publish(LoaderPage.DEPARTMENT);
    }

    public void showEmployee() {
        // RX Java
        PubSubRX.getInstance().publishLoaderPage(LoaderPage.EMPLOYEE);
        // java core
//        PubSub.getInstance().publish(LoaderPage.EMPLOYEE);
    }
}
