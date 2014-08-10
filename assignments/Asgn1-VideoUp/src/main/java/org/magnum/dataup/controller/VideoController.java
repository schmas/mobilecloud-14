package org.magnum.dataup.controller;

import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.magnum.dataup.repository.VideoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
    public @ResponseBody Video addVideo(@RequestBody Video v) {
        return repository.save(v);
    }

    @RequestMapping(value = "/video", method = RequestMethod.GET)
    public @ResponseBody Collection<Video> getVideos() {
        return repository.getVideos();
    }

    @RequestMapping(value = "/video/{id}/data", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<VideoStatus> uploadVideoFile(@PathVariable("id") long id,
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
    public @ResponseBody ResponseEntity<byte[]> getVideoFile(@PathVariable("id") long id) {

        try {
            final Video video = repository.getVideo(id);
            if (repository.hasVideoData(video)) {

                final Path file = repository.getFile(video);

                final HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType("video/mpeg"));
                headers.setContentLength(file.toFile().length());
                headers.setContentDispositionFormData("attachment", file.getFileName().toString());
                return new ResponseEntity<>(Files.readAllBytes(file), headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            LOG.error("ERROR", e);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
