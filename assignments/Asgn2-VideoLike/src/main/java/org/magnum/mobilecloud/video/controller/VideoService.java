package org.magnum.mobilecloud.video.controller;

import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.magnum.mobilecloud.video.exception.VideoNotFoundException;
import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

	@RequestMapping(value = VideoSvcApi.VIDEO_DURATION_SEARCH_PATH, method = RequestMethod.GET)
	public Collection<Video> findByDurationLessThan(@RequestParam(VideoSvcApi.DURATION_PARAMETER) long duration) {
		return repository.findByDurationLessThan(duration);
	}

	private Video findOne(final long id) {
		final Video video = repository.findOne(id);
		if (video == null) {
			throw new VideoNotFoundException();
		}
		return video;
	}

	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Video> getById(@PathVariable long id) {
		try {
			final Video video = findOne(id);
			return new ResponseEntity<>(video, HttpStatus.OK);
		} catch (VideoNotFoundException e) {
			return new ResponseEntity<>(e.getHttpStatus());
		}
	}

	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}/like")
	public ResponseEntity like(@PathVariable long id, Principal principal) {
		try {
			Video video = findOne(id);
			User activeUser = (User) ((Authentication) principal).getPrincipal();
			final boolean like = video.like(activeUser.getUsername());
			if (like) {
				repository.save(video);
				return new ResponseEntity(HttpStatus.OK);
			}
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		} catch (VideoNotFoundException e) {
			return new ResponseEntity(e.getHttpStatus());
		}
	}

	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}/unlike")
	public ResponseEntity unlike(@PathVariable long id, Principal principal) {
		try {
			Video video = findOne(id);
			User activeUser = (User) ((Authentication) principal).getPrincipal();
			final boolean unlike = video.unlike(activeUser.getUsername());
			if (unlike) {
				repository.save(video);
				return new ResponseEntity(HttpStatus.OK);
			}
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		} catch (VideoNotFoundException e) {
			return new ResponseEntity(e.getHttpStatus());
		}
	}

	@RequestMapping(value = VideoSvcApi.VIDEO_SVC_PATH + "/{id}/likedby")
	public ResponseEntity<Collection<String>> likedby(@PathVariable long id) {
		try {
			Video video = findOne(id);
			final Collection<String> likeUsers = video.getLikeUsers();
			return new ResponseEntity<>(likeUsers, HttpStatus.OK);
		} catch (VideoNotFoundException e) {
			return new ResponseEntity<>(e.getHttpStatus());
		}
	}

}
