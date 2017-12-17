package com.example.demo.web.tool;

import lombok.Data;

import java.util.List;

@Data
public class Pager {

    private List<Object> list;

    private Integer count;

    private Integer pageSize = 20;

    private Integer currentPage =1;

}
