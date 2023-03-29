package ua.com.alevel.controller;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import ua.com.alevel.persistence.dao.DepartmentDao;
import ua.com.alevel.persistence.dao.EmployeeDao;
import ua.com.alevel.persistence.dao.impl.DepartmentDaoImpl;
import ua.com.alevel.persistence.dao.impl.EmployeeDaoImpl;
import ua.com.alevel.persistence.entity.Department;
import ua.com.alevel.persistence.entity.Employee;
import ua.com.alevel.reactive.rx.PubSubRX;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

public class AttachViewController implements Initializable {

    @FXML
    private Label departmentTypeLabel;

    @FXML
    private TableView<Employee> employeesAttachedTable;

    @FXML
    private TableColumn<Employee, Long> employeeAttachedIdColumn;

    @FXML
    private TableColumn<Employee, String> employeeAttachedFirstNameColumn;

    @FXML
    private TableColumn<Employee, String> employeeAttachedLastNameColumn;

    @FXML
    private TableColumn<Employee, Integer> employeeAttachedAgeColumn;

    @FXML
    private TableView<Employee> employeesNonAttachedTable;

    @FXML
    private TableColumn<Employee, Long> employeeNonAttachedIdColumn;

    @FXML
    private TableColumn<Employee, String> employeeNonAttachedFirstNameColumn;

    @FXML
    private TableColumn<Employee, String> employeeNonAttachedLastNameColumn;

    @FXML
    private TableColumn<Employee, Integer> employeeNonAttachedAgeColumn;

    @FXML
    private Button attachButton;

    @FXML
    private Button unAttachButton;

    private Department currentDepartment;
    private Employee attachedEmployee;
    private Employee detachedEmployee;
    private ObservableList<Employee> employeesAttached = FXCollections.observableArrayList();
    private ObservableList<Employee> employeesNonAttached = FXCollections.observableArrayList();
    private final EmployeeDao employeeDao = new EmployeeDaoImpl();
    private final DepartmentDao departmentDao = new DepartmentDaoImpl();
    private final BehaviorSubject<Boolean> publisher = BehaviorSubject.createDefault(false);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PubSubRX.getInstance().subscribeDepartment(departmentObserver());
        attachButton.setDisable(true);
        unAttachButton.setDisable(true);

        employeesAttachedTable.setRowFactory(tv -> {
            TableRow<Employee> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                detachedEmployee = row.getItem();
                attachButton.setDisable(true);
                unAttachButton.setDisable(false);
            });
            return row;
        });

        employeesNonAttachedTable.setRowFactory(tv -> {
            TableRow<Employee> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                attachedEmployee = row.getItem();
                attachButton.setDisable(false);
                unAttachButton.setDisable(true);
            });
            return row;
        });

        publisher.subscribe(observAttachDetachEmployee());
    }

    public void attach() {
        departmentDao.attachEmployeesToDepartment(currentDepartment, attachedEmployee);
        publisher.onNext(true);
    }

    public void unAttach() {
        departmentDao.detachEmployeesToDepartment(currentDepartment, detachedEmployee);
        publisher.onNext(true);
    }

    private void initEmployeesTables() {
        employeesAttached.clear();
        employeesAttached.addAll(findAllEmployeesAttached());
        employeeAttachedIdColumn.setCellValueFactory(new PropertyValueFactory<Employee, Long>("id"));
        employeeAttachedFirstNameColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstName"));
        employeeAttachedLastNameColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("lastName"));
        employeeAttachedAgeColumn.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("age"));
        employeesAttachedTable.setItems(employeesAttached);

        employeesNonAttached.clear();
        employeesNonAttached.addAll(findAllEmployeesNonAttached());
        employeeNonAttachedIdColumn.setCellValueFactory(new PropertyValueFactory<Employee, Long>("id"));
        employeeNonAttachedFirstNameColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstName"));
        employeeNonAttachedLastNameColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("lastName"));
        employeeNonAttachedAgeColumn.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("age"));
        employeesNonAttachedTable.setItems(employeesNonAttached);
    }

    private Collection<Employee> findAllEmployeesAttached() {
        return employeeDao.findByDepartment(this.currentDepartment.getId());
    }

    private Collection<Employee> findAllEmployeesNonAttached() {
        return employeeDao.findByNonDepartment(this.currentDepartment.getId());
    }

    private Observer<Boolean> observAttachDetachEmployee() {
        return new Observer<Boolean>() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {

            }

            @Override
            public void onNext(@NonNull Boolean aBoolean) {
                if (aBoolean) {
                    initEmployeesTables();
                }
            }

            @Override
            public void onError(@NonNull Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    private Observer<Department> departmentObserver() {
        return new Observer<Department>() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {

            }

            @Override
            public void onNext(@NonNull Department department) {
                currentDepartment = department;
                departmentTypeLabel.setText(currentDepartment.getDepartmentType().name());
                initEmployeesTables();
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
