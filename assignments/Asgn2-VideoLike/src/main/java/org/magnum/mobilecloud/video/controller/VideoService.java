package org.magnum.mobilecloud.video.controller;

import com.google.common.collect.Lists;
import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * @author dceschmidt
 * @since 27/08/14
 */
@RestController
public class VideoService {

	@Autowired
	private VideoRepository repository;

	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.POST)
	public Video addVideo(@RequestBody Video video) {
		return repository.save(video);
	}

	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH, method = RequestMethod.GET)
	public Collection<Video> findAll() {
		return repository.findAll();
	}

	@RequestMapping(value = VideoSvcApi.VIDEO_TITLE_SEARCH_PATH, method = RequestMethod.GET)
	public Collection<Video> findByName(@RequestParam(VideoSvcApi.TITLE_PARAMETER) String title) {
		return repository.findByName(title);
	}

}
