package com.jornah.service;

import com.jornah.model.Music;
import com.jornah.model.qo.MusicUploadQo;

import java.io.IOException;
import java.util.List;

public interface MusicService {
    boolean uploadMusic(MusicUploadQo musicUploadQo) throws IOException;

    List<Music> allMusic();
}
