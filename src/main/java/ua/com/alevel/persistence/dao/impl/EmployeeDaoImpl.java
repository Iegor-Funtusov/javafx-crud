package ua.com.alevel.persistence.dao.impl;

import ua.com.alevel.persistence.dao.EmployeeDao;
import ua.com.alevel.persistence.entity.Employee;
import ua.com.alevel.persistence.jdbc.JdbcService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class EmployeeDaoImpl implements EmployeeDao {

    private final JdbcService jdbcService = JdbcService.getInstance();

    private static final String CREATE_EMPLOYEE = "insert into employees values (default, ?, ?, ?)";
    private static final String UPDATE_EMPLOYEE = "update employees set first_name = ?, last_name = ?, age = ? where id = ?";
    private static final String DELETE_EMPLOYEE = "delete from employees where id = ?";
    private static final String FIND_EMPLOYEE_BY_ID = "select * from employees where id = ";
    private static final String FIND_EMPLOYEES = "select * from employees";
    private static final String FIND_ALL_EMPLOYEES_BY_DEPARTMENT = "select id, first_name, last_name, age from employees left join dep_emp de on de.emp_id = employees.id where de.dep_id = ";
    private static final String FIND_ALL_EMPLOYEES_BY_NON_DEPARTMENT = "select id, first_name, last_name, age from employees where id not in\n" +
            "                                                           (select id from employees left join dep_emp de on de.emp_id = employees.id where de.dep_id = ?)";

    @Override
    public void create(Employee entity) {
        try(PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(CREATE_EMPLOYEE)) {
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setInt(3, entity.getAge());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("e = " + e);
        }
    }

    @Override
    public void update(Employee entity) {
        try(PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(UPDATE_EMPLOYEE)) {
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setInt(3, entity.getAge());
            preparedStatement.setLong(4, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("e = " + e);
        }
    }

    @Override
    public void delete(Long id) {
        try(PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(DELETE_EMPLOYEE)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("e = " + e);
        }
    }

    @Override
    public Optional<Employee> findById(Long id) {
        try(ResultSet resultSet = jdbcService.getStatement().executeQuery(FIND_EMPLOYEE_BY_ID + id)) {
            while (resultSet.next()) {
                return Optional.of(generateEmployeeByResultSet(resultSet));
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public Collection<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        try(ResultSet resultSet = jdbcService.getStatement().executeQuery(FIND_EMPLOYEES)) {
            while (resultSet.next()) {
                employees.add(generateEmployeeByResultSet(resultSet));
            }
        } catch (SQLException e) {
            return employees;
        }
        return employees;
    }

    @Override
    public Collection<Employee> findByDepartment(Long depId) {
        List<Employee> employees = new ArrayList<>();
        try(ResultSet resultSet = jdbcService.getStatement().executeQuery(FIND_ALL_EMPLOYEES_BY_DEPARTMENT + depId)) {
            while (resultSet.next()) {
                employees.add(generateEmployeeByResultSet(resultSet));
            }
        } catch (SQLException e) {
            return employees;
        }
        return employees;
    }

    @Override
    public Collection<Employee> findByNonDepartment(Long depId) {
        List<Employee> employees = new ArrayList<>();
        try(PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(FIND_ALL_EMPLOYEES_BY_NON_DEPARTMENT)) {
            preparedStatement.setLong(1, depId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                employees.add(generateEmployeeByResultSet(resultSet));
            }
        } catch (SQLException e) {
            return employees;
        }
        return employees;
    }

    private Employee generateEmployeeByResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        int age = resultSet.getInt("age");
        Employee employee = new Employee();
        employee.setId(id);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setAge(age);
        return employee;
    }
}
