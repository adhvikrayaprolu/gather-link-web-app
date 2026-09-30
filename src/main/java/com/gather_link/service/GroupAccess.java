package com.gather_link.service;
import com.gather_link.model.*;
import com.gather_link.repository.GroupMembershipRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
@Service
public class GroupAccess {
 private final GroupMembershipRepository memberships;
 public GroupAccess(GroupMembershipRepository memberships){this.memberships=memberships;}
 public void requireMember(Groups group,Users user){if(group==null)throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Group not found");if(user==null || (!group.getOwner().getUserId().equals(user.getUserId()) && memberships.findByGroupAndUser(group,user)==null))throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Join this group first");}
 public void requireOwner(Groups group,Users user){if(group==null)throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Group not found");if(user==null || !group.getOwner().getUserId().equals(user.getUserId()))throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Only the owner can change roles");}
}
