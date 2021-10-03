package com.jornah.model.converter;

import com.jornah.model.Music;
import com.jornah.model.qo.MusicUploadQo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author licong
 * @date 2021/10/2 02:03
 */
@Mapper
public interface MusicConverter {
    MusicConverter INSTANCE = Mappers.getMapper( MusicConverter.class );

//    @Mapping(source = "numberOfSeats", target = "seatCount")
//    MusicUploadQo toDto(Music car);

//    @Mapping(source = "title", target = "title")
    Music toEntity(MusicUploadQo car);

}
