package com.jornah.controller.v1.admin;

import com.github.pagehelper.PageInfo;
import com.jornah.api.QiNiuCloudService;
import com.jornah.constant.ErrorConstant;
import com.jornah.constant.LogActions;
import com.jornah.constant.Types;
import com.jornah.constant.WebConst;
import com.jornah.controller.BaseController;
import com.jornah.dao.AttachDao;
import com.jornah.exception.BusinessException;
import com.jornah.model.Attach;
import com.jornah.model.newP.User;
import com.jornah.model.dto.AttAchDto;
import com.jornah.service.attach.AttAchService;
import com.jornah.service.log.LogService;
import com.jornah.utils.APIResponse;
import com.jornah.utils.Commons;
import com.jornah.utils.TaleUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.buf.HexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Api("文件管理")
@RestController
@RequestMapping("/admin/attach")
@CrossOrigin
public class AttachController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttachController.class);

    public static final String CLASSPATH = TaleUtils.getUploadFilePath();

    @Autowired
    private AttAchService attAchService;
    @Autowired
    private AttachDao attachDao;

    @Autowired
    private LogService logService;


    @ApiOperation("文件管理首页")
    @GetMapping(value = "")
    public String index(
            HttpServletRequest request,
            @ApiParam(name = "page", value = "页数", required = false)
            @RequestParam(name = "page", required = false, defaultValue = "1")
                    int page,
            @ApiParam(name = "limit", value = "条数", required = false)
            @RequestParam(name = "limit", required = false, defaultValue = "12")
                    int limit
    ) {
        PageInfo<AttAchDto> atts = attAchService.getAtts(page, limit);
        request.setAttribute("attachs", atts);
        request.setAttribute(Types.ATTACH_URL.getType(), Commons.site_option(Types.ATTACH_URL.getType(), Commons.site_url()));
        request.setAttribute("max_file_size", WebConst.MAX_FILE_SIZE / 1024);
        return "admin/attach";
    }

    @ApiOperation("小文件上传")
    @PostMapping(value = "/upload")
    public APIResponse<String> fileUpload(HttpServletRequest request, @RequestParam(name = "file") MultipartFile file) {
        return this.uploadFiles(request, new MultipartFile[]{file});
    }


    @ApiOperation("多文件上传")
    @PostMapping(value = "upload-files")
    public APIResponse<String> uploadFiles(
            HttpServletRequest request,
            @ApiParam(name = "file", value = "文件数组", required = true)
            @RequestParam(name = "file") MultipartFile[] files) {
        try {
            List<String> fileUrlList = new ArrayList<>();
            for (MultipartFile file : files) {
                byte[] bytes = DigestUtils.md5(file.getInputStream());
                String md5 = HexUtils.toHexString(bytes);
                Attach byMd5 = attachDao.findByMd5(md5);
                if (Objects.nonNull(byMd5)) {
                    fileUrlList.add(byMd5.getFkey());
                    continue;
                }

                String fileName = TaleUtils.getFileKey(file.getOriginalFilename().replaceFirst("/", ""));

                String fileUrl = QiNiuCloudService.upload(file, fileName);
                fileUrlList.add(fileUrl);

                Attach attach = new Attach();
                HttpSession session = request.getSession();
                User sessionUser = (User) session.getAttribute(WebConst.LOGIN_SESSION_KEY);
                if (Objects.nonNull(sessionUser)) {
//                    attach.setAuthorId(sessionUser.getUid());
                }
                attach.setFtype(TaleUtils.isImage(file.getInputStream()) ? Types.IMAGE.getType() : Types.FILE.getType());
                attach.setFname(fileName);
                attach.setFkey(QiNiuCloudService.QINIU_UPLOAD_SITE + fileName);
                attAchService.addAttAch(attach);
            }
            String result = String.join(",", fileUrlList);
            return APIResponse.success(result);

        } catch (IOException e) {
            e.printStackTrace();
            throw BusinessException.of(ErrorConstant.Att.ADD_NEW_ATT_FAIL);

        }

    }

    @ApiOperation("删除文件")
    @PostMapping(value = "/delete")
    @ResponseBody
    public APIResponse deleteFileInfo(
            HttpServletRequest request,
            @ApiParam(name = "id", value = "文件主键", required = true)
            @RequestParam(name = "id", required = true)
                    Integer id
    ) {
        try {
            AttAchDto attach = attAchService.getAttAchById(id);
            if (null == attach) {
                throw BusinessException.of(ErrorConstant.Att.DELETE_ATT_FAIL + ": 文件不存在");
            }
            attAchService.deleteAttAch(id);
            // 写入日志
            logService.addLog(LogActions.DEL_ATTACH.getAction(), this.user(request).getUsername() + "用户", request.getRemoteAddr(), this.getUid(request));
            return APIResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            throw BusinessException.of(e.getMessage());
        }
    }

}
