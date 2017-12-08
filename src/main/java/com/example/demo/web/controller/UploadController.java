package com.example.demo.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {
	
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
