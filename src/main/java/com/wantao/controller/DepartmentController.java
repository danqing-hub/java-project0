package com.wantao.controller;

import com.wantao.bean.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wantao.bean.Message;
import com.wantao.service.DepartmentService;

import java.util.List;

/**
 * 处理和部门有关的请求
 */
@Controller
public class DepartmentController {
	@Autowired
	private DepartmentService departmentService;

	/**
	 *
	 * @return返回所有的部门信息（json数据）
	 */
	@GetMapping(value = "/depts")
	@ResponseBody
	public Message getAllDepts() {
		//查出的所有部门信息
		List<Department> list = departmentService.getAllDepts();
		return Message.success().add("depts",list);
	}
}
