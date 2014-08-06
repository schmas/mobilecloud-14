package org.magnum.dataup.repository.impl;

import org.magnum.dataup.VideoFileManager;
import org.magnum.dataup.model.Video;
import org.magnum.dataup.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author dceschmidt
 * @sinnce 05/08/14
 */
@Repository
public class VideoRepositoryNoPersistentImpl implements VideoRepository {

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

    private void checkAndSetId(Video entity) {
        if (entity.getId() == 0) {
            entity.setId(currentId.incrementAndGet());
        }
    }

    private String getDataUrl(long videoId) {
        String url = getUrlBaseForLocalServer() + "/video/" + videoId + "/data";
        return url;
    }

    private String getUrlBaseForLocalServer() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return "http://" + request.getServerName()
                       + ((request.getServerPort() != 80) ? ":" + request.getServerPort() : "");
    }
}
