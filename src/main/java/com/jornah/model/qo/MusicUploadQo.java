package com.jornah.model.qo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author licong
 * @date 2021/10/2 01:48
 */
@Data
public class MusicUploadQo {
    private String title;
    private String pic;
    private String artist;
    private String fileExtend;
    private MultipartFile file;
}
