package com.fpt.blog.mappings;

import com.fpt.blog.entities.Reaction;
import com.fpt.blog.models.reaction.response.ReactionResponse;
import org.mapstruct.Mapper;

@Mapper
public interface ReactionMapper {
    ReactionResponse toResponse(Reaction reaction);
}
