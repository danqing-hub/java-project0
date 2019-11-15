package com.wantao.bean;

public class Department {
    private Integer deptId;

    private String deptName;

    //因为生成了有参的构造器，一定要生产无参的构造器，因为反射要经常的使用无参的构造
    public Department() {

    }
    public Department(Integer deptId, String deptName) {
        this.deptId = deptId;
        this.deptName = deptName;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        //this.deptName = deptName == null ? null : deptName.trim();
      this.deptName = deptName;
    }
}