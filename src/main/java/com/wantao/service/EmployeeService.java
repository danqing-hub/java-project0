package com.wantao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wantao.bean.Employee;
import com.wantao.bean.EmployeeExample;
import com.wantao.bean.EmployeeExample.Criteria;
import com.wantao.dao.EmployeeMapper;

@Service
public class EmployeeService {
	@Autowired
	EmployeeMapper employeeMapper;

	public List<Employee> findAllEmployee() {
		//return employeeMapper.selectByExampleWithDept(null);
		//查询所有的，所以调节为null
		return employeeMapper.selectByExampleWithDept(null);
	}

	/**
	 * @param
	 * @return void
	 * @description 新增用户
	 */
	public void saveEmployee(Employee employee) {
		employeeMapper.insertSelective(employee);
	}

	/**
	 * @param
	 * @return boolean
	 * @description 数据库中是否有empName,有返回false.没有返回true，true代表可用
	 */
	public boolean checkSameEmployee(String empName) {
		EmployeeExample example = new EmployeeExample();
		Criteria criteria = example.createCriteria();
		criteria.andEmpNameEqualTo(empName);
		long count = employeeMapper.countByExample(example);
		return count == 0;
	}

	/**
	 * 按照员工id查询员工
	 * @param id
	 * @return
	 */
	public Employee getEmployee(Integer id) {
		Employee employee = employeeMapper.selectByPrimaryKey(id);
		return employee;
	}

	/**
	 * 员工更新
	 * @param employee
	 */
	public void updateEmployee(Employee employee) {
		employeeMapper.updateByPrimaryKeySelective(employee);//根据主键有选择的更新
	}

	/**
	 * 员工删除
	 * @param empId
	 */
	public void deleteEmployee(Integer empId) {
		employeeMapper.deleteByPrimaryKey(empId);
	}

	/***
	 * 删除多个员工（批量）
	 * @param ids
	 */
	public void deleteBatch(List<Integer> ids) {

		EmployeeExample example = new EmployeeExample();
		Criteria criteria = example.createCriteria();
		//delete from xxx where emp_id in(1,2,3)
		criteria.andEmpIdIn(ids);
		employeeMapper.deleteByExample(example);

	}

}
