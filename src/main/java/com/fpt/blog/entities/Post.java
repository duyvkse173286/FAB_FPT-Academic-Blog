package com.fpt.blog.entities;

import com.fpt.blog.enums.PostStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post")
@Accessors(chain = true)
@FieldNameConstants
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String title;

    private String thumbnail;

    @Column(columnDefinition = "text")
    private String content;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private PostStatus status = PostStatus.WAITING;

    private LocalDateTime approvedAt;

    private LocalDateTime rejectedAt;

    private String rejectReason;

    private Boolean commentEnabled;

    private int viewCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 50)
    private Set<Reaction> reactions = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 50)
    private Set<Comment> comments = new HashSet<>();

    @ManyToMany(mappedBy = "posts", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 50)
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "review_by")
    private User reviewBy;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostAward> awards;


}
