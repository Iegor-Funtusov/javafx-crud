package ua.com.alevel.persistence.dao;

import ua.com.alevel.dto.DepartmentDto;
import ua.com.alevel.persistence.entity.Department;
import ua.com.alevel.persistence.entity.Employee;

import java.util.Collection;
import java.util.Optional;

public interface DepartmentDao {

    void create(Department entity);
    void update(Department entity);
    void delete(Long id);
    Optional<Department> findById(Long id);
    Collection<Department> findAll();
    Collection<DepartmentDto> findEmployeeCountByDepartmentType();
    void attachEmployeesToDepartment(Department department, Employee employee);
    void detachEmployeesToDepartment(Department department, Employee employee);
}
