package com.wip.controller.v2;


import com.wip.dao.DraftDao;
import com.wip.model.Content;
import com.wip.model.DraftStatus;
import com.wip.service.DraftService;
import com.wip.service.article.ContentService;
import com.wip.utils.APIResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static java.time.Instant.now;

@RestController("v2ArticleController")
@RequestMapping("/v2/article")
public class ArticleController {

    @Autowired
    private ContentService contentService;
    @Autowired
    private DraftService draftService;
    @Autowired
    private DraftDao draftDao;

    @ApiOperation("发布新文章")
    @PostMapping(value = "/publish")
    @ResponseBody
    public APIResponse<?> publishArticle(Content content) {

        // 保留分类
        // 直接分类以逗号为分隔符，存分类编号，这样也避免了联查的烦恼，约定值就直接存在localCache里
        // 添加文章
        content.setModified(now());
        content.setCreated(now());
        contentService.addArticle(content);
        return APIResponse.success();
    }

    @ApiOperation("保存草稿")
    @PostMapping(value = "/draft/save")
    public APIResponse<?> saveDraft(Content content) {
        draftService.createDraft(content.getCid(),content.getContent(), DraftStatus.getByString(content.getStatus()));
        //todo 保留分类
        // 直接分类以逗号为分隔符，存分类编号，这样也避免了联查的烦恼，约定值就直接存在localCache里
        // 添加文章

        return APIResponse.success();
    }

    @ApiOperation("删除文章")
    @PostMapping("/delete")
    public APIResponse<?> deleteArticle(@ApiParam(name = "cid", value = "文章ID", required = true)
                                        @RequestParam(name = "cid") Integer cid) {
        // 删除文章
        contentService.deleteArticleById(cid);
        return APIResponse.success();
    }

    @ApiOperation("更新文章")
    @PostMapping("/update")
    @ResponseBody
    public APIResponse<?> updateArticle(Content content) {
        content.setModified(now());
        contentService.updateArticleById(content);
        return APIResponse.success();
    }
    @GetMapping("/test-validate")
    @ResponseBody
    public APIResponse<?> testValidate(@Valid @RequestBody Content content) {
        System.out.println("request : "+content);
        return APIResponse.success();
    }
}
