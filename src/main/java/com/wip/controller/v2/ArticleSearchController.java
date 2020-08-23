package com.wip.controller.v2;

import com.wip.controller.BaseController;
import com.wip.model.dto.ArticleSearchRequest;
import com.wip.model.dto.ContentEsDTO;
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
    public APIResponse<SearchHits<ContentEsDTO>> search(ArticleSearchRequest request) {
        String inputStr = request.getSearchStr();
        int pageNum = request.getPageNum() <= 0 ? 1 : request.getPageNum();
        int pageSize = request.getPageSize() <= 0 ? 5 : request.getPageSize();;
        SearchHits<ContentEsDTO> hits = esContentService.findByContentOrTitle(inputStr, pageNum, pageSize);
        return APIResponse.success(hits);
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
