package com.jornah.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Data
@SuperBuilder
@NoArgsConstructor
@TableName
public class BaseEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Instant created;
    private Instant updated;

}
