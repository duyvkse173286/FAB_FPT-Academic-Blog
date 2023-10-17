package com.fpt.blog.entities;


import com.fpt.blog.enums.Collection;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "category")
@Accessors(chain = true)
@FieldNameConstants
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String image;

    @Column(columnDefinition = "text")
    private String description;

    private Collection collection;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<Post> posts;

}

