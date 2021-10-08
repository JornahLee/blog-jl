package com.jornah.model.converter;

import com.jornah.model.entity.User;
import com.jornah.model.vo.UserVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author licong
 * @date 2021/10/3 10:08
 */
@Mapper
public interface UserConverter {
    UserConverter INSTANCE = Mappers.getMapper( UserConverter.class );

    UserVo toVo(User user);

}
