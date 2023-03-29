package ua.com.alevel.persistence.dao.impl;

import ua.com.alevel.dto.DepartmentDto;
import ua.com.alevel.persistence.dao.DepartmentDao;
import ua.com.alevel.persistence.entity.Department;
import ua.com.alevel.persistence.entity.Employee;
import ua.com.alevel.persistence.jdbc.JdbcService;
import ua.com.alevel.persistence.type.DepartmentType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DepartmentDaoImpl implements DepartmentDao {

    private final JdbcService jdbcService = JdbcService.getInstance();

    private static final String CREATE_DEPARTMENT = "insert into departments values (default, ?)";
    private static final String UPDATE_DEPARTMENT = "update departments set department_type = ? where id = ?";
    private static final String DELETE_DEPARTMENT = "delete from departments where id = ?";
    private static final String FIND_DEPARTMENT_BY_ID = "select * from departments where id = ";
    private static final String FIND_DEPARTMENTS = "select * from departments";
    private static final String ATTACH_EMPLOYEES_TO_DEPARTMENT = "insert into dep_emp values (?, ?)";
    private static final String DETACH_EMPLOYEES_TO_DEPARTMENT = "delete from dep_emp where dep_id = ? and emp_id = ?";
    private static final String FIND_EMPLOYEE_COUNT_BY_DEPARTMENT_TYPE = "select d.id, d.department_type, count(dep_id) as employee_count" +
            " from departments as d left join dep_emp as de on d.id = de.dep_id group by d.id";

    @Override
    public void create(Department entity) {
        try(PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(CREATE_DEPARTMENT)) {
            preparedStatement.setString(1, entity.getDepartmentType().name());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("e = " + e);
        }
    }

    @Override
    public void update(Department entity) {
        try(PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(UPDATE_DEPARTMENT)) {
            preparedStatement.setString(1, entity.getDepartmentType().name());
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("e = " + e);
        }
    }

    @Override
    public void delete(Long id) {
        try(PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(DELETE_DEPARTMENT)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("e = " + e);
        }
    }

    @Override
    public Optional<Department> findById(Long id) {
        try(ResultSet resultSet = jdbcService.getStatement().executeQuery(FIND_DEPARTMENT_BY_ID + id)) {
            while (resultSet.next()) {
                return Optional.of(generateDepartmentByResultSet(resultSet));
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    public Collection<Department> findAll() {
        List<Department> departments = new ArrayList<>();
        try(ResultSet resultSet = jdbcService.getStatement().executeQuery(FIND_DEPARTMENTS)) {
            while (resultSet.next()) {
                departments.add(generateDepartmentByResultSet(resultSet));
            }
        } catch (SQLException e) {
            return departments;
        }
        return departments;
    }

    @Override
    public Collection<DepartmentDto> findEmployeeCountByDepartmentType() {
        List<DepartmentDto> dtoList = new ArrayList<>();
        try(ResultSet resultSet = jdbcService.getStatement().executeQuery(FIND_EMPLOYEE_COUNT_BY_DEPARTMENT_TYPE)) {
            while (resultSet.next()) {
                dtoList.add(generateDepartmentDto(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("e = " + e.getMessage());
            return Collections.emptyList();
        }
        return dtoList;
    }

    @Override
    public void attachEmployeesToDepartment(Department department, Employee employee) {
        try(PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(ATTACH_EMPLOYEES_TO_DEPARTMENT)) {
            preparedStatement.setLong(1, department.getId());
            preparedStatement.setLong(2, employee.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("e = " + e);
        }
    }

    @Override
    public void detachEmployeesToDepartment(Department department, Employee employee) {
        try(PreparedStatement preparedStatement = jdbcService.getConnection().prepareStatement(DETACH_EMPLOYEES_TO_DEPARTMENT)) {
            preparedStatement.setLong(1, department.getId());
            preparedStatement.setLong(2, employee.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("e = " + e);
        }
    }

    private Department generateDepartmentByResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String type = resultSet.getString("department_type");
        DepartmentType departmentType = DepartmentType.valueOf(type);
        Department department = new Department();
        department.setId(id);
        department.setDepartmentType(departmentType);
        return department;
    }

    private DepartmentDto generateDepartmentDto(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String type = resultSet.getString("department_type");
        DepartmentType departmentType = DepartmentType.valueOf(type);
        int employeeCount = resultSet.getInt("employee_count");
        Department department = new Department();
        department.setId(id);
        department.setDepartmentType(departmentType);
        return new DepartmentDto(department.getId(), departmentType, employeeCount);
    }
}
