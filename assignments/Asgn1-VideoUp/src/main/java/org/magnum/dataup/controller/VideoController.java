package org.magnum.dataup.controller;

import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.magnum.dataup.repository.VideoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * @author Diego Schmidt
 */
@Controller
public class VideoController {

    private static Logger LOG = LoggerFactory.getLogger(VideoController.class);

    @Autowired
    private VideoRepository repository;

    @RequestMapping(value = "/video", method = RequestMethod.POST)
    public
    @ResponseBody
    Video addVideo(@RequestBody Video v) {
        return repository.save(v);
    }

    @RequestMapping(value = "/video", method = RequestMethod.GET)
    public
    @ResponseBody
    Collection<Video> getVideos() {
        return repository.getVideos();
    }

    @RequestMapping(value = "/video/{id}/data", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<VideoStatus> uploadVideoFile(@PathVariable("id") long id,
                                                @RequestParam("data") MultipartFile file) {

        if (!file.isEmpty()) {
            Video video = repository.getVideo(id);

            try (InputStream in = file.getInputStream()) {

                repository.saveVideoData(video, in);

                return new ResponseEntity<>(new VideoStatus(VideoStatus.VideoState.READY), HttpStatus.OK);

            } catch (RuntimeException | IOException e) {
                LOG.error("ERROR", e);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @RequestMapping(value = "/video/{id}/data", method = RequestMethod.GET)
    public void getVideoFile(@PathVariable("id") long id, HttpServletResponse response) throws IOException {

        try {
            final Video video = repository.getVideo(id);
            if (repository.hasVideoData(video)) {

                response.setContentType("video/mpeg");
                repository.copyVideoData(video, response.getOutputStream());
                response.flushBuffer();
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            LOG.error("ERROR", e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }

}
