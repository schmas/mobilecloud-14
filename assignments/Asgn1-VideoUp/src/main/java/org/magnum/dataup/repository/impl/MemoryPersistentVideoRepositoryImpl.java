package org.magnum.dataup.repository.impl;

import org.magnum.dataup.VideoFileManager;
import org.magnum.dataup.model.Video;
import org.magnum.dataup.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author dceschmidt
 * @sinnce 05/08/14
 */
@Repository
public class MemoryPersistentVideoRepositoryImpl implements VideoRepository {

    private static final AtomicLong currentId = new AtomicLong(0L);

    @Autowired
    private VideoFileManager videoFileManager;

    private Map<Long, Video> videos = new HashMap<>();

    @Override
    public Video save(Video video) {
        checkAndSetId(video);
        video.setDataUrl(getDataUrl(video.getId()));
        videos.put(video.getId(), video);
        return video;
    }

    @Override
    public Collection<Video> getVideos() {
        return videos.values();
    }

    @Override
    public Video getVideo(long id) {
        return videos.get(id);
    }

    @Override
    public Video saveVideoData(final Video video, final InputStream inputStream) {
        try {
            videoFileManager.saveVideoData(video, inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return video;
    }

    @Override
    public boolean hasVideoData(final Video video) {
        return videoFileManager.hasVideoData(video);
    }

    @Override
    public void copyVideoData(final Video video, final ServletOutputStream outputStream) {
        try {
            videoFileManager.copyVideoData(video, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkAndSetId(Video entity) {
        if (entity.getId() == 0) {
            entity.setId(currentId.incrementAndGet());
        }
    }

    private String getDataUrl(long videoId) {
        return getUrlBaseForLocalServer() + "/video/" + videoId + "/data";
    }

    private String getUrlBaseForLocalServer() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return "http://" + request.getServerName()
                       + ((request.getServerPort() != 80) ? ":" + request.getServerPort() : "");
    }
}
