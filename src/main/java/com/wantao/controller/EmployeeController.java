package com.wantao.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wantao.bean.Employee;
import com.wantao.bean.Message;
import com.wantao.service.EmployeeService;

/**
 * @author wantao
 * @date 2019-02-02 21:28
 * @description: talk is cheap show me your code
 */
@Controller
public class EmployeeController {
	Logger logger = Logger.getLogger("EmployeeController.class");
	@Autowired
	EmployeeService employeeService;
		/**
		 * 查询员工，不分页（1000多条）
		 */
	//	@GetMapping(value = "/emps")
//	public String showEmployees() {
//		List<Employee> employees = employeeService.findAllEmployee();
//		return "show";
//	}



	/**
	 * @param
	 * @return String
	 * @description:查询所有员工(分页查询)
	 */
	/*//@GetMapping(value = "/emps")
	public String showEmployees(@RequestParam(defaultValue = "1", value = "pn") Integer pn,Model model) {
	//引入PageHelper分页插件
		PageHelper.startPage(pn, 5);// 后面紧跟的查询为分页查询，页码为pn，每页5个数据
		List<Employee> employees = employeeService.findAllEmployee();
	//用pageInfo封装然后交给页面，传入查询出来的数据
		PageInfo pageInfo=new PageInfo(employees,5);//连续显示5页
		model.addAttribute("pageInfo",pageInfo);
	return "show";
	}
*/

	/**导入jackson包，负责将对象转换为json
	 * 用json发送数据
	 * @param pn
	 * @param model
	 * @return
	 */
	/*@GetMapping(value = "/emps")
	@ResponseBody
	public PageInfo showEmployees(@RequestParam(defaultValue = "1", value = "pn") Integer pn,Model model) {
		//引入PageHelper分页插件
		PageHelper.startPage(pn, 5);// 后面紧跟的查询为分页查询，页码为pn，每页5个数据
		List<Employee> employees = employeeService.findAllEmployee();
		//用pageInfo封装然后交给页面，传入查询出来的数据
		PageInfo pageInfo=new PageInfo(employees,5);//连续显示5页

		return pageInfo;
	}*/

	/**
	 * @param将上面的方法封装一个Message中（优化）
	 * @return PageInfo
	 * @description:查询所有员工(分页查询),以ajax方式返回
	 */
	@GetMapping(value = "/emps")
	@ResponseBody
	public Message showEmployeesWithJson(@RequestParam(defaultValue = "1", name = "pn") Integer pn, Model model) {
		PageHelper.startPage(pn, 10);// 后面紧跟的查询为分页查询，参数值为一页有多少数据。
		List<Employee> employees = employeeService.findAllEmployee();
		PageInfo pageInfo = new PageInfo(employees, 5);// 用pageInfo封装然后交给页面
		return Message.success().add("pageInfo", pageInfo);
	}

	/**员工保存
	 *1.支持JSR303,
	 *2.导入Hibernate-Validator
	 * 校验只需要@Valid注解，把结果封装到BindingResult result
	 */
	@PostMapping(value = "/emp")
	@ResponseBody
	public Message saveEmployee(@Valid Employee employee, BindingResult result) {
		// System.out.println(employee);
		if (result.hasErrors()) {// 后端校验失败,返回校验失败的信息
			Map<String, Object> map = new HashMap<>();
			List<FieldError> errors = result.getFieldErrors();
			for (FieldError error : errors) {
				System.out.println("错误的字段命名：");
				map.put(error.getField(), error.getDefaultMessage());
			}
			return Message.fail().add("errorField", map);
		} else {
			employeeService.saveEmployee(employee);
			return Message.success();
		}
	}

	/**
	 * @param检查用户名是否可用
	 * @return Message
	 * @description:检测用户是否存在 success表示用户不存在可用,fail表示用户存在不可用
	 */
	@PostMapping(value = "/checkSameEmployee")
	@ResponseBody
	public Message checkSameEmployee(@RequestParam("empName") String empName) {
		//先判断用户名是否是合法的表达式
		String regx = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\\u2E80-\\u9FFF]{2,5})";
		//看用户名是否匹配正则表达式
		if(!empName.matches(regx)){//如果匹配失败
			return Message.fail().add("va_msg","用户名必须是6-16位数字和字母的组合或者2-5位中文");
		}
		//数据库用户名重复校验
		boolean b = employeeService.checkSameEmployee(empName);
		if (b) {// 用户名不存在,可用
//    		logger.info(empName+"不存在");
			return Message.success();
		} else {
//    		logger.info(empName+"存在");
			return Message.fail().add("va_msg","用户名不可用");
		}
	}

	/**根据id查询员工
	 * @param
	 * @return Message
	 * @description:修改前将要修改的employee查询出来表单回显
	 */
	@GetMapping(value = "/emp/{id}")//等价于@RequestMapping(value = "/emp/{id}",method=RequestMethod.GET)
	@ResponseBody
	public Message getEmployee(@PathVariable("id") Integer id) {
		Employee employee = employeeService.getEmployee(id);
		return Message.success().add("employee", employee);
	}

	/**保存员工
	 * 遇到的问题：请求体中有数据：
	 * 但是Employee对象封装不上；
	 * SQL语句变成了 update tbl_emp where emp_id = 1014;
	 * 原因：
	 * Tomcat：
	 * 		1.将请求体中的数据，封装成一个map
	 * 		2.request.getParameter("empName")就会从这个map中取值。
	 * 		3.SpringMvc封装pojo的时候会把pojo中的每个属性值，request.getParamter("email");
	 * Ajax发送PUT请求引发的血案
	 * 		PUT请求：请求体中的数据，request.getParameter("empName")拿不到的根本原因就是
	 * 	Tomcat一看是put请求，不会封装请求体中的数据为map，只有post形式的请求才封装请求体为map
	 * @param
	 * @return Message
	 * @description:员工更新 这里ajax请求直接发put请求而不是post请求,那么所有的参数都会获取不到,因为tomcat只会封装post的数据
	 *                   也就是说request.getParameter("empId")为空,springmvc也无法封装Bean
	 *                   解决方法: 1.发送post方法,通过HiddenHttpMethodFilter
	 *                   2.发送put请求,通过HttpPutFormContentFilter(通过web.xml配置)
	 */
	@PutMapping(value = "/emp/{empId}")
	@ResponseBody
	public Message saveUpdateEmployee(Employee employee) {
		 System.out.println(employee);
		//logger.info(employee.toString());
		employeeService.updateEmployee(employee);
		return Message.success();
	}

	/**
	 * @param
	 * @return Message
	 * @description:单个批量删除 单个删除:1 批量删除:1-2-3
	 */
	@DeleteMapping(value = "/emp/{ids}")
	@ResponseBody
	public Message deleteEmployee(@PathVariable("ids") String ids) {
		if (ids.contains("-")) {//包含-就是批量删除，调用方法如下
			String[] str_ids = ids.split("-");//分割成数组
			List<Integer> del_ids = new ArrayList<>();
			for (String id : str_ids) {
				del_ids.add(Integer.parseInt(id));
			}
			employeeService.deleteBatch(del_ids);
		} else {//不包含-就是单个删除，调用方法如下：
			Integer id = Integer.parseInt(ids);
			employeeService.deleteEmployee(id);
		}
		return Message.success();
	}

	/**
	 * 单个删除的方法
	 * @param id
	 * @return
	 */
	@ResponseBody
	//@RequestMapping(value = "/emp/{id}",method =RequestMethod.DELETE)
	public Message deleteEmpById(@PathVariable("id") Integer id){
		employeeService.deleteEmployee(id);
		return Message.success();
	}
}
