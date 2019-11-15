package com.wantao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wantao.bean.Department;
import com.wantao.bean.Message;
import com.wantao.dao.DepartmentMapper;

@Service("departmentService")
public class DepartmentService {
	@Autowired
    private DepartmentMapper departmentMapper;
	public List<Department> getAllDepts() {
		List<Department> list=departmentMapper.selectByExample(null);
		return list;
	}
}
