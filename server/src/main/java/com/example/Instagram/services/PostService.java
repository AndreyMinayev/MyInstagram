package com.example.Instagram.services;


import com.example.Instagram.dto.PostDTO;
import com.example.Instagram.entity.Image;
import com.example.Instagram.entity.Post;
import com.example.Instagram.entity.User;
import com.example.Instagram.exception.PostNotFoundException;
import com.example.Instagram.repository.ImageRepository;
import com.example.Instagram.repository.PostRepository;
import com.example.Instagram.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Service
public class PostService {
    public static final Logger LOG = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, ImageRepository imageRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    public Post createPost(PostDTO postDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        Post post = new Post();
        post.setUser(user);
        post.setCaption(postDTO.getCaption());
        post.setLocation(postDTO.getLocation());
        post.setTitle(postDTO.getTitle());
        post.setLikes(0);

        LOG.info("Saving Post for User:  {}", user.getEmail());
        return postRepository.save(post);

    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByDateOfCreationDesc();
    }

    public Post getPostById(Long postId, Principal principal) {
        User user = getUserByPrincipal(principal);
        return postRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

    }

    public List<Post> getAllPostForUser(Principal principal) {
        User user = getUserByPrincipal(principal);
        return postRepository.findAllByUserOrderByDateOfCreationDesc(user);
    }

    public Post likeDislikePost(Long postId, String username) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found"));

        Optional<String> userLiked = post.getLikedUsers().stream().filter(u -> u.equals(username)).findAny();

        if (userLiked.isPresent()) {
            post.setLikes(post.getLikes() - 1);
            post.getLikedUsers().remove(username);
        } else {
            post.setLikes(post.getLikes() + 1);
            post.getLikedUsers().add(username);
        }
        return postRepository.save(post);
    }


    public void deletePost(Long postId, Principal principal){
        Post post = getPostById(postId, principal);
        Optional<Image> image = imageRepository.findByPostId(postId);
        postRepository.delete(post);
        image.ifPresent(imageRepository::delete);
    }


    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));

    }
}
