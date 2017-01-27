package com.sarvan.medicineplus.realm;

/**
 * DepartmentModel Class Definition for departments in home screen
 */

public class DepartmentModel {
    private int deptId;
    private String departmentName;
    private String departmentDesc;

    //Constructor
    public DepartmentModel() {
    }

    //Constructor
    public DepartmentModel(int deptId, String departmentName, String departmentDesc) {
        this.deptId = deptId;
        this.departmentName = departmentName;
        this.departmentDesc = departmentDesc;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentDesc() {
        return departmentDesc;
    }

    public void setDepartmentDesc(String departmentDesc) {
        this.departmentDesc = departmentDesc;
    }
}
