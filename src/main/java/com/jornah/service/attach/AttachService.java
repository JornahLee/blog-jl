/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/8/3 16:23
 **/
package com.jornah.service.attach;

import com.github.pagehelper.PageInfo;
import com.jornah.model.Attach;
import com.jornah.model.dto.AttachDto;

/**
 * 文件相关接口
 */
public interface AttachService {

    /**
     * 添加单个附件信息
     * @param attach
     */
    void addAttach(Attach attach);

    /**
     * 获取所有的附件信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<AttachDto> getAtts(int pageNum, int pageSize);

    /**
     * 通过ID获取附件信息
     * @param id
     * @return
     */
    AttachDto getAttachById(Integer id);

    /**
     * 通过ID删除附件信息
     * @param id
     */
    void deleteAttach(Integer id);
}
