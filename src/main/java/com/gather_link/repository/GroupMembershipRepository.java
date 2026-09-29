package com.gather_link.repository;

import com.gather_link.model.GroupMemberships;
import com.gather_link.model.Groups;
import com.gather_link.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMembershipRepository extends JpaRepository<GroupMemberships, Long> {
	
    List<GroupMemberships> findByUser(Users user);

	List<GroupMemberships> findByGroup(Groups group);

 long countByGroup(Groups group);
	GroupMemberships findByGroupAndUser(Groups group, Users user);
}