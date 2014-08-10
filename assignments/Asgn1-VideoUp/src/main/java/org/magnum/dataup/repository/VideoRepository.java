package org.magnum.dataup.repository;

import org.magnum.dataup.model.Video;

import javax.servlet.ServletOutputStream;
import java.io.InputStream;
import java.util.Collection;

/**
 * @author dceschmidt
 * @sinnce 05/08/14
 */
public interface VideoRepository {

    Video save(Video entity);

    Collection<Video> getVideos();

    Video getVideo(long id);

    Video saveVideoData(Video video, InputStream inputStream);

    boolean hasVideoData(Video video);

    void copyVideoData(Video video, ServletOutputStream outputStream);
}
