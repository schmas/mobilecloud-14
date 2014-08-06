package org.magnum.dataup.controller;

import org.magnum.dataup.model.Video;
import org.magnum.dataup.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by dceschmidt on 05/08/14.
 */
@Controller
public class VideoController {

    @Autowired
    private VideoRepository repository;

    @RequestMapping(value = "/video", method = RequestMethod.POST)
    public
    @ResponseBody
    Video addVideo(@RequestBody Video v) {
        final Video savedVideo = repository.save(v);
        return savedVideo;
    }

}
