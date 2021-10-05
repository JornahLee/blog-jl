package com.jornah.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jornah.dao.MusicDao;
import com.jornah.exception.BusinessException;
import com.jornah.model.Music;
import com.jornah.model.converter.MusicConverter;
import com.jornah.model.qo.MusicUploadQo;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

@Service
public class MusicServiceImpl implements MusicService {
    @Value("${music.disk.path:./}")
    public String DISK_MUSIC_PATH;
    @Value("${music.url.prefix:http://localhost:8077/}")
    public String MUSIC_URL_PREFIX;
    @Autowired
    private MusicDao musicDao;

    @Override
    @Transactional
    public boolean uploadMusic(MusicUploadQo musicUploadQo) throws IOException {
        MultipartFile file = musicUploadQo.getFile();
        InputStream inputStream = file.getInputStream();
        byte[] fileBytes = IOUtils.readAllBytes(inputStream);
        String md5 = DigestUtils.md5Hex(fileBytes);
        Music music = MusicConverter.INSTANCE.toEntity(musicUploadQo);
        music.setMd5(md5);
        music.setSrc(MUSIC_URL_PREFIX + md5 + musicUploadQo.getFileExtend());

        Music findInDb = musicDao.selectOne(new LambdaQueryWrapper<Music>().eq(Music::getMd5, md5));
        if (Objects.nonNull(findInDb)) {
            throw BusinessException.of("已存在");
        }
        boolean isSuccess = musicDao.insert(music) > 0;
        saveToDisk(music.getMd5() + musicUploadQo.getFileExtend(), fileBytes);
        return isSuccess;
    }

    private void saveToDisk(String filename, byte[] bytes) throws IOException {
        File file = new File(DISK_MUSIC_PATH + filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        }
    }

    /**
     * 所有已上传音乐
     *
     * @return
     */
    @Override
    public List<Music> allMusic() {
        return musicDao.selectList(new QueryWrapper<>());
    }
}
