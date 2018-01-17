package com.example.demo.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {

	/**
	 * 模板引擎的 选择 (Thymeleaf 性能过低不建议 Beetl 性能最好 。还有Velocity FreeMarker 可选)
	 * 建议使用vue 来进行 前后端分离
	 * @param map
	 * @return
	 */
	@RequestMapping("/thymeleaf")
	public String index(ModelMap map) {
		// 加入一个属性，用来在模板中读取
		map.addAttribute("host", "thymeleaf");
		// return模板文件的名称，对应src/main/resources/templates/index.html
		return "thymeleaf";
	}

	@RequestMapping("/hello")
	@ResponseBody
	public String index() {
		return "Hello World";
	}

	@RequestMapping("upload")
	@ResponseBody
	public Object file(@RequestParam("file")  MultipartFile file) {
		System.out.println(file.getOriginalFilename());
		Map<String, String> map =new HashMap<>();
		map.put("status", "success");
		map.put("message", "上传成功");
		return map;
	}
}
