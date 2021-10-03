package com.jornah.model;

import com.jornah.model.newP.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author licong
 * @date 2021/10/2 01:53
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class Music extends BaseEntity {
    private String src;
    private String title;
    private String pic;
    private String artist;
    private String md5;
}
