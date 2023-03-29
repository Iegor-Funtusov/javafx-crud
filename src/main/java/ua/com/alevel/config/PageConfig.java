package ua.com.alevel.config;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import ua.com.alevel.reactive.rx.PubSubRX;

import java.io.IOException;

public class PageConfig {

    private final Stage primaryStage;
    private final LoaderFactory loaderFactory;
    private BorderPane rootLayout;
    private AnchorPane employeeView;
    private AnchorPane departmentView;
    private AnchorPane attachView;

    public PageConfig(Stage stage, LoaderFactory loaderFactory) {
        this.primaryStage = stage;
        this.loaderFactory = loaderFactory;
        this.primaryStage.setTitle("Java FX crud reactive application");
        initRootLayout(this.loaderFactory.getLoaderPageMap().get(LoaderPage.LAYOUT.getView()));

        // RX Java
        PubSubRX.getInstance().subscribeLoaderPage(loaderPageObserver());

        // core java
//        showDepartmentView(this.loaderFactory.getLoaderPageMap().get(LoaderPage.DEPARTMENT.getView()));
//        PubSub.getInstance().subscribe(this::switchPage);
    }

    private void initRootLayout(FXMLLoader loader) {
        try {
            rootLayout = loader.load();
            Rectangle2D screenBounds = Screen.getPrimary().getBounds();
            double maxX = screenBounds.getMaxX();
            double maxY = screenBounds.getMaxY();
            Scene scene = new Scene(rootLayout, maxX / 2 + 100, maxY / 2);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showDepartmentView(FXMLLoader loader) {
        try {
            if (departmentView == null) {
                departmentView = loader.load();
            }
            rootLayout.setCenter(departmentView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showEmployeeView(FXMLLoader loader) {
        try {
            if (employeeView == null) {
                employeeView = loader.load();
            }
            rootLayout.setCenter(employeeView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAttachView(FXMLLoader loader) {
        try {
            if (attachView == null) {
                attachView = loader.load();
            }
            rootLayout.setCenter(attachView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchPage(LoaderPage page) {
        switch (page) {
            case DEPARTMENT -> showDepartmentView(this.loaderFactory.getLoaderPageMap().get(LoaderPage.DEPARTMENT.getView()));
            case EMPLOYEE -> showEmployeeView(this.loaderFactory.getLoaderPageMap().get(LoaderPage.EMPLOYEE.getView()));
            case ATTACH -> showAttachView(this.loaderFactory.getLoaderPageMap().get(LoaderPage.ATTACH.getView()));
        }
    }

    private Observer<LoaderPage> loaderPageObserver() {
        return new Observer<>() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {

            }

            @Override
            public void onNext(@NonNull LoaderPage page) {
                switchPage(page);
            }

            @Override
            public void onError(@NonNull Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        };
    }
}
