package com.jornah.controller;

import com.jornah.anno.AccessControl;
import com.jornah.model.Music;
import com.jornah.model.Role;
import com.jornah.model.qo.MusicUploadQo;
import com.jornah.service.MusicService;
import com.jornah.utils.APIResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Api("音乐管理")
@RestController
@RequestMapping("/music")
@CrossOrigin
public class MusicController extends BaseController {

    @Autowired
    private MusicService musicService;


    @ApiOperation("音乐上传")
    @PostMapping(value = "/upload")
    @AccessControl
    public APIResponse<String> musicUpload(MusicUploadQo musicUploadQo) throws IOException {
        boolean ret = musicService.uploadMusic(musicUploadQo);
        return APIResponse.success(ret ? "成功" : "失败");

    }
    @ApiOperation("所有音乐")
    @GetMapping(value = "/list/all")
    public APIResponse<List<Music>> allMusic() throws IOException {
        List<Music> musicList = musicService.allMusic();
        return APIResponse.success(musicList);

    }
    @ApiOperation("歌单下音乐")
    @GetMapping(value = "/music-list/{id}}")
    public APIResponse<String> allMusic(@PathVariable Long id) throws IOException {
//        boolean ret = musicService.uploadMusic(musicUploadQo);
        return APIResponse.success(false ? "成功" : "失败");

    }
    @ApiOperation("所有歌单")
    @GetMapping(value = "/song-list/all")
    public APIResponse<String> allSongList() throws IOException {
        return APIResponse.success(false ? "成功" : "失败");

    }

}
