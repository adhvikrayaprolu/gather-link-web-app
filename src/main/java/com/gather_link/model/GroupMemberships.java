package com.gather_link.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "group_memberships", uniqueConstraints=@UniqueConstraint(columnNames={"group_id","user_id"}))
public class GroupMemberships {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_membership_id")
    private Long groupMembershipId;
	
	@ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
	
	@ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Groups group;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;
	
	@Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;
	
	@PrePersist
    protected void onJoin() {
        this.joinedAt = LocalDateTime.now();
    }
	
	public Long getGroupMembershipId() {
        return groupMembershipId;
    }

    public void setGroupMembershipId(Long groupMembershipId) {
        this.groupMembershipId = groupMembershipId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Groups getGroup() {
        return group;
    }

    public void setGroup(Groups group) {
        this.group = group;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }
}
