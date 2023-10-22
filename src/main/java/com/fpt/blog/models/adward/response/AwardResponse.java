package com.fpt.blog.models.adward.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AwardResponse {
    private long id;

    private String name;

    private String description;

    private String image;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
