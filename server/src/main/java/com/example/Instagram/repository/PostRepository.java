package com.example.Instagram.repository;

import com.example.Instagram.entity.Post;
import com.example.Instagram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUserOrderByDateOfCreationDesc(User user);

    List<Post> findAllByOrderByDateOfCreationDesc();

    Optional<Post> findPostByIdAndUser (Long id, User user);
}
