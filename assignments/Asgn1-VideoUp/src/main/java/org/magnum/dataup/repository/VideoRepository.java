package org.magnum.dataup.repository;

import org.magnum.dataup.model.Video;

import java.util.Collection;

/**
 * @author dceschmidt
 * @sinnce 05/08/14
 */
public interface VideoRepository {

    Video save(Video entity);

    Collection<Video> getVideos();
}
