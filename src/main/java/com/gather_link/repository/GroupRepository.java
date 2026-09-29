package com.gather_link.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gather_link.model.Groups;
import com.gather_link.model.Users;

@Repository
public interface GroupRepository extends JpaRepository<Groups, Long> {

 @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
 @org.springframework.data.jpa.repository.Query("select g from Groups g where g.groupId = :id")
 java.util.Optional<Groups> findLockedById(@org.springframework.data.repository.query.Param("id") Long id);
	Groups findByGroupName(String group_name);
	
	List<Groups> findByOwner(Users owner);
	
}
