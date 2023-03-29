package ua.com.alevel.dto;

import ua.com.alevel.persistence.type.DepartmentType;

public class DepartmentDto {

    private Long id;
    private DepartmentType type;
    private int employeeCount;

    public DepartmentDto(Long id, DepartmentType type, int employeeCount) {
        this.id = id;
        this.type = type;
        this.employeeCount = employeeCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DepartmentType getType() {
        return type;
    }

    public void setType(DepartmentType type) {
        this.type = type;
    }

    public int getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(int employeeCount) {
        this.employeeCount = employeeCount;
    }
}
