package com.chris.cheese.cheesemusic.controller.songorder;

import com.chris.cheese.cheesemusic.pojo.SongDO;
import com.chris.cheese.cheesemusic.pojo.SongOrderDO;
import com.chris.cheese.cheesemusic.pojo.UserDO;
import com.chris.cheese.cheesemusic.pojo.songmodel.Song;
import com.chris.cheese.cheesemusic.service.SongOrderService;
import com.chris.cheese.cheesemusic.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/songOrder")
public class SongOrderRestController {
    @Autowired
    private SongOrderService songOrderService;
    @Autowired
    private SongService songService;

    @PostMapping("/pickSong")
    public String pickSong(SongOrderDO songOrderDO, HttpServletRequest request) {
        UserDO user = (UserDO) request.getSession().getAttribute("user");
        if (user != null) {
            songOrderDO.setUserId(user.getId());
            Song song = songService.getSingleSong(songOrderDO.getSongId());
            SongDO songDO = new SongDO();
            songDO.setId(song.getId());
            songDO.setSongName(song.getName());
            songDO.setSongSinger(song.getSinger());
            songDO.setSongTime(song.getTime());
            songDO.setUrl(song.getUrl());
            songDO.setLrc(song.getLrc());
            songDO.setPic(song.getPic());
            songOrderService.doSave(songOrderDO, songDO);
            return "success";
        } else {
            return "fail";
        }
    }

    @PostMapping("/changed/{songOrderId}")
    public boolean changeStatus(@PathVariable(value = "songOrderId") Long songOrderId) {
        if (songOrderId != null && songOrderId > 0) {
            songOrderService.doChangeStatus(songOrderId);
            return true;
        }
        return false;
    }
}
