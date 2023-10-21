package com.fpt.blog.mappings;

import com.fpt.blog.entities.*;
import com.fpt.blog.models.adward.response.AwardResponse;
import com.fpt.blog.models.category.response.CategoryResponse;
import com.fpt.blog.models.post.response.PostResponse;
import com.fpt.blog.models.user.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface PostMapper {

    @Mapping(source = "awards", target = "awards", qualifiedByName = "awardMapping")
    PostResponse toResponse(Post post);

    @Named("awardMapping")
    static List<AwardResponse> toAwardResponse(List<PostAward> awards) {
       return awards.stream().map(a -> new AwardResponse()
                .setName(a.getAward().getName())
                .setImage(a.getAward().getImage())
                .setDescription(a.getAward().getDescription())).collect(Collectors.toList());
    }

    CategoryResponse toResponse(Category category);

    UserResponse toResponse(User user);

}
