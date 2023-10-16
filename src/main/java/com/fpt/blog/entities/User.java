package com.fpt.blog.entities;

import com.fpt.blog.enums.MemberType;
import com.fpt.blog.enums.Role;
import com.fpt.blog.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
@Accessors(chain = true)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String avatar;

    private String password;

    private String phoneNumber;

    private String name;

    private LocalDate dob;

    private UserStatus status = UserStatus.ACTIVE;

    private MemberType memberType = MemberType.COPPER;

    private LocalDateTime bannedAt;

    private LocalDateTime inactiveAt;

    @Column(columnDefinition = "text")
    private String description;

    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Reaction> reactions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Comment> comments;

}

