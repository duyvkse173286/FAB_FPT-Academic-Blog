package com.fpt.blog.entities;

import com.fpt.blog.enums.ReactType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reaction")
@Accessors(chain = true)
public class Reaction  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private ReactType reactType;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private ReactType type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
