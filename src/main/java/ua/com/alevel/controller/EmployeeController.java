package ua.com.alevel.controller;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import ua.com.alevel.persistence.dao.EmployeeDao;
import ua.com.alevel.persistence.dao.impl.EmployeeDaoImpl;
import ua.com.alevel.persistence.entity.Employee;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {

    @FXML
    private TextField empIdText;

    @FXML
    private TextField firstNameText;

    @FXML
    private TextField lastNameText;

    @FXML
    private TextField ageText;

    @FXML
    private TableView<Employee> employeeTable;

    @FXML
    private TableColumn<Employee, Long> idColumn;

    @FXML
    private TableColumn<Employee, String> firstNameColumn;

    @FXML
    private TableColumn<Employee, String> lastNameColumn;

    @FXML
    private TableColumn<Employee, Integer> ageColumn;

    private ObservableList<Employee> employees = FXCollections.observableArrayList();
    private final EmployeeDao employeeDao = new EmployeeDaoImpl();
    private final BehaviorSubject<Boolean> publisher = BehaviorSubject.createDefault(true);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        employees.addAll(findAll());
        idColumn.setCellValueFactory(new PropertyValueFactory<Employee, Long>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("lastName"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("age"));
        employeeTable.setItems(employees);

        employeeTable.setRowFactory(tv -> {
            TableRow<Employee> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                Employee rowData = row.getItem();
                empIdText.setText(String.valueOf(rowData.getId()));
                firstNameText.setText(rowData.getFirstName());
                lastNameText.setText(rowData.getLastName());
                ageText.setText(String.valueOf(rowData.getAge()));
            });
            return row;
        });

        publisher.subscribe(observCDUEmployee());
    }

    @FXML
    public void insertButton() {
        Employee employee = new Employee();
        employee.setFirstName(firstNameText.getText());
        employee.setLastName(lastNameText.getText());
        employee.setAge(Integer.parseInt(ageText.getText()));
        employeeDao.create(employee);
        publisher.onNext(true);
    }

    @FXML
    public void updateButton() {
        Employee employee = new Employee();
        employee.setId(Long.parseLong(empIdText.getText()));
        employee.setFirstName(firstNameText.getText());
        employee.setLastName(lastNameText.getText());
        employee.setAge(Integer.parseInt(ageText.getText()));
        employeeDao.update(employee);
        publisher.onNext(true);
    }

    @FXML
    public void deleteButton() {
        employeeDao.delete(Long.parseLong(empIdText.getText()));
        publisher.onNext(true);
    }

    private Collection<Employee> findAll() {
        return employeeDao.findAll();
    }

    private Observer<Boolean> observCDUEmployee() {
        return new Observer<Boolean>() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {

            }

            @Override
            public void onNext(@NonNull Boolean aBoolean) {
                if (aBoolean) {
                    employees = FXCollections.observableArrayList();
                    employees.addAll(findAll());
                    employeeTable.setItems(employees);
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
}
