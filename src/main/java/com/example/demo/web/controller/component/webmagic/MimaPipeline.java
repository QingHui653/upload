package com.example.demo.web.controller.component.webmagic;

import com.example.demo.spring.service.model.nove.Nove;
import com.example.demo.spring.service.model.nove.Title;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * 爬虫数据的持久化
 */
@Service
public class MimaPipeline implements Pipeline {


    @Autowired(required = false)
    private MongoTemplate mongoTemplate;

    @Override
    public void process(ResultItems resultItems, Task task) {
        String tid = resultItems.get("tid");
        String name = resultItems.get("name");
        String noveType = resultItems.get("noveType");
        String titleType = resultItems.get("titleType");
        List<String> index = resultItems.get("index");
        List<String> content = resultItems.get("content");

        try {
            if(tid!=null) {
                Nove nove = new Nove(tid, name,noveType,titleType);

                List<Nove> noves = mongoTemplate.find(new Query(Criteria.where("id").is(tid)), Nove.class);
                if(noves.size()==0){
                    mongoTemplate.insert(nove);
                }else {
//                    mongoTemplate.remove(noves.get(0));
//                    mongoTemplate.insert(nove);
                }
                Title title;
                for (int i = 0, j = index.size(); i < j; i++) {
                    title = new Title(tid, Integer.valueOf(index.get(i)), content.get(i));
                    Query query = new Query();
                    query.addCriteria(Criteria.where("noveId").is(tid).and("index").is(Integer.valueOf(index.get(i))));
                    List<Title> titles = mongoTemplate.find(query, Title.class);
                    if(titles.size()==0){
                        mongoTemplate.insert(title);
                    }else {
//                        mongoTemplate.remove(titles.get(0));
//                        mongoTemplate.insert(title);
                    }
                }
            }
        }catch (Exception e){
        }
    }
}
