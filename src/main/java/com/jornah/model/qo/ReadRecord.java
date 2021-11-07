package com.jornah.model.qo;

import com.jornah.anno.GsonIgnore;
import com.jornah.model.entity.BaseEntity;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalTime;

/**
 * @author licong
 * @date 2021/10/30 11:22
 */
@Data
public class ReadRecord extends BaseEntity {
    @NotNull
    private Long articleId;
    @NotNull
    private Long userId;
    private String title;
    private Long readDuration;
    private Instant startReadTime;
}
