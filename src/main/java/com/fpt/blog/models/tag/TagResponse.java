package com.fpt.blog.models.tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagResponse {

    private long id;

    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
