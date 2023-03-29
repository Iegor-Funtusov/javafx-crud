package ua.com.alevel.controller;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import ua.com.alevel.config.LoaderPage;
import ua.com.alevel.dto.DepartmentDto;
import ua.com.alevel.persistence.dao.DepartmentDao;
import ua.com.alevel.persistence.dao.impl.DepartmentDaoImpl;
import ua.com.alevel.persistence.entity.Department;
import ua.com.alevel.persistence.type.DepartmentType;
import ua.com.alevel.reactive.rx.PubSubRX;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

public class DepartmentController implements Initializable {

    @FXML
    private TextField depIdText;

    @FXML
    private TextField depTypeText;

    @FXML
    private Button attachButton;

    @FXML
    private TableView<DepartmentDto> departmentTable;

    @FXML
    private TableColumn<DepartmentDto, Long> idColumn;

    @FXML
    private TableColumn<DepartmentDto, String> typeColumn;

    @FXML
    private TableColumn<DepartmentDto, Integer> employeeCountColumn;

    private final DepartmentDao departmentDao = new DepartmentDaoImpl();
    private ObservableList<DepartmentDto> departments = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        departments.addAll(findAll());
        idColumn.setCellValueFactory(new PropertyValueFactory<DepartmentDto, Long>("id"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<DepartmentDto, String>("type"));
        employeeCountColumn.setCellValueFactory(new PropertyValueFactory<DepartmentDto, Integer>("employeeCount"));
        departmentTable.setItems(departments);
        attachButton.setDisable(true);

        departmentTable.setRowFactory(tv -> {
            TableRow<DepartmentDto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                DepartmentDto rowData = row.getItem();
                depIdText.setText(String.valueOf(rowData.getId()));
                depTypeText.setText(rowData.getType().name());
                attachButton.setDisable(false);
            });
            return row;
        });

        PubSubRX.getInstance().subscribeLoaderPage(loaderPageObserver());
    }

    public void attachButton() {
        Department department = new Department();
        department.setId(Long.parseLong(depIdText.getText()));
        department.setDepartmentType(DepartmentType.valueOf(depTypeText.getText()));
        PubSubRX.getInstance().publishLoaderPage(LoaderPage.ATTACH);
        PubSubRX.getInstance().publishDepartment(department);
    }

    private Collection<DepartmentDto> findAll() {
        return departmentDao.findEmployeeCountByDepartmentType();
    }

    private Observer<LoaderPage> loaderPageObserver() {
        return new Observer<>() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {

            }

            @Override
            public void onNext(@NonNull LoaderPage page) {
                departments = FXCollections.observableArrayList();
                departments.addAll(findAll());
                departmentTable.setItems(departments);
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
