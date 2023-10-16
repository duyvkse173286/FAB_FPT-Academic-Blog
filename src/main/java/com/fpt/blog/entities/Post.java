package com.fpt.blog.entities;

import com.fpt.blog.enums.PostStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post")
@Accessors(chain = true)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String title;

    @Column(columnDefinition = "text")
    private String content;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private PostStatus status;

    private LocalDateTime approvedAt;

    private LocalDateTime deniedAt;

    private String deniedReason;

    private Boolean commentEnabled;

    private int viewCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 50)
    private Set<Reaction> reactions;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 50)
    private Set<Comment> comments;

    @ManyToMany(mappedBy = "posts", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 50)
    private Set<Tag> tags;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private Set<Media> medias;

}
