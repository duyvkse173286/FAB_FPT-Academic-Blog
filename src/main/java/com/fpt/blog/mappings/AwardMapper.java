package com.fpt.blog.mappings;

import com.fpt.blog.entities.Award;
import com.fpt.blog.models.adward.response.AwardResponse;
import org.mapstruct.Mapper;

@Mapper
public interface AwardMapper {

    AwardResponse toResponse(Award award);

}
