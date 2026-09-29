package com.gather_link.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gather_link.model.Groups;
import com.gather_link.model.Posts;


public interface PostRepository extends JpaRepository<Posts, Long> {
 long countByGroup(Groups group);
	List<Posts> findByGroup_GroupId(Long groupId);
    List<Posts> findByPostCreator_UserId(Long userId);
    List<Posts> findByGroup(Groups group);
}
