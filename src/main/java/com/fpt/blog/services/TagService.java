package com.fpt.blog.services;


import com.fpt.blog.models.tag.GetAllTagRequest;
import com.fpt.blog.models.tag.TagResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TagService {

    List<TagResponse> getAllTags();

    List<TagResponse> getAllTags(GetAllTagRequest request);

    Page<TagResponse> getAllTagsFilterPaging(GetAllTagRequest request);

    TagResponse createTag(String name);

    TagResponse getTag(String name);

    TagResponse updateTag(long tagId, String name);

    TagResponse removeTag(long tagId);

}
