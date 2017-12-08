package com.example.demo.web.controller.component.webmagic;

import com.example.demo.spring.service.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * 爬虫数据的持久化
 */
@Service
public class MongoSaveMoviePipeline implements Pipeline {
	
	
	@Autowired(required=false)
	private MongoTemplate mongoTemplate;

    @Override
    public void process(ResultItems resultItems, Task task) {
    	Movie movie = resultItems.get("movie");

    	if (movie != null) {
    		mongoTemplate.insert(movie);
        }
    }
}
