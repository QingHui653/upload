package com.example.demo.web.bean;

import lombok.Data;

@Data
public class ResultBean{

	private String code;// 结果编码
	
	private String message;  //返回结果信息

	private Object data;  //返回结果

}
