package com.jornah.model.vo;

import com.jornah.model.entity.Category;
import com.jornah.model.entity.Tag;
import com.jornah.utils.APIResponse;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author licong
 * @date 2023/8/7 00:30
 */
@Data
@AllArgsConstructor(staticName="from")
public class SiteInfo {
    private UserVo ownerInfo;
    private List<Category> cateList;
    private List<Tag> tagList;
    private List<String> statsInfoList;


}
