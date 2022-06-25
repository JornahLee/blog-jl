package com.jornah.model.entity;

import lombok.Data;

/**
 * @author licong
 * @date 2022/6/26 01:49
 */
@Data
public class Config extends BaseEntity{
    private String configKey;
    private String value1;
    private String value2;
    private String value3;
    private String value4;
    private String value5;
}
