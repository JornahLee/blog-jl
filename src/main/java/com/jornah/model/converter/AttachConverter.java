package com.jornah.model.converter;

import com.jornah.model.entity.Attach;
import com.jornah.model.dto.AttachDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author licong
 * @date 2021/10/2 02:03
 */
@Mapper
public interface AttachConverter {
    AttachConverter INSTANCE = Mappers.getMapper( AttachConverter.class );

    AttachDto toDto(Attach entity);

}
