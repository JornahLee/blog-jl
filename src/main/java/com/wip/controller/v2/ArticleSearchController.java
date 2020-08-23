package com.wip.controller.v2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wip.controller.BaseController;
import com.wip.model.dto.ArticleSearchRequest;
import com.wip.model.dto.ContentEsDTO;
import com.wip.model.dto.SearchResult;
import com.wip.service.article.ContentService;
import com.wip.service.es.EsContentService;
import com.wip.utils.APIResponse;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/article")
@RestController
public class ArticleSearchController extends BaseController {

    @Autowired
    private EsContentService esContentService;

    @RequestMapping("/search")
    public APIResponse<List<SearchResult>> search(ArticleSearchRequest request) {
        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
        String str = gson.toJson(request);
        System.out.println("--licg---     str : " + str + "    -----");
        String inputStr = request.getSearchStr();
        int pageNum = request.getPageNum() <= 0 ? 1 : request.getPageNum();
        int pageSize = request.getPageSize() <= 0 ? 5 : request.getPageSize();
        List<SearchResult> res = esContentService.findByContentOrTitle(inputStr, pageNum, pageSize);
        System.out.println("--licg---     gson.toJson(APIResponse.success(res)) : " + gson.toJson(APIResponse.success(res)) + "    -----");
        return APIResponse.success(res);
    }
    @RequestMapping("/random-add")
    public APIResponse<?> randomAdd(String title,String content) {
        ContentEsDTO  contentEsDTO=new ContentEsDTO();
        if (title==null) {
            contentEsDTO.setTitle("title "+ RandomUtils.nextInt(100,1000));
        }else{
            contentEsDTO.setTitle(title);
        }
        if (content==null) {
            contentEsDTO.setContent("content "+ RandomUtils.nextInt(100,1000));
        }else{
            contentEsDTO.setContent(content);
        }

        esContentService.add(contentEsDTO);
        return APIResponse.success();
    }


}
