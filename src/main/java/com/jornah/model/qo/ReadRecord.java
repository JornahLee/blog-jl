package com.jornah.model.qo;

import com.jornah.anno.GsonIgnore;
import com.jornah.model.entity.BaseEntity;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.time.LocalTime;

/**
 * @author licong
 * @date 2021/10/30 11:22
 */
@Data
public class ReadRecord extends BaseEntity {
    private Long articleId;
    private Long userId;
    private String title;
    private Long readDuration;
    private Instant startReadTime;
}
