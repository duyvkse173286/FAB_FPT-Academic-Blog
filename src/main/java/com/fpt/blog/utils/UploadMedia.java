package com.fpt.blog.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UploadMedia {

    private long id;

    private String url;

    private String contentType;

    private String fileName;

    private String description;

}
