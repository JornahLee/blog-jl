/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/8/3 16:23
 **/
package com.wip.service.attach;

import com.github.pagehelper.PageInfo;
import com.wip.model.Attach;
import com.wip.model.dto.AttAchDto;

/**
 * 文件相关接口
 */
public interface AttAchService {

    /**
     * 添加单个附件信息
     * @param attach
     */
    void addAttAch(Attach attach);

    /**
     * 获取所有的附件信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<AttAchDto> getAtts(int pageNum, int pageSize);

    /**
     * 通过ID获取附件信息
     * @param id
     * @return
     */
    AttAchDto getAttAchById(Integer id);

    /**
     * 通过ID删除附件信息
     * @param id
     */
    void deleteAttAch(Integer id);
}
