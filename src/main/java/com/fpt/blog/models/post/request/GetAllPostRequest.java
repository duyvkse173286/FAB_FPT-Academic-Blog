package com.fpt.blog.models.post.request;

import com.fpt.blog.entities.*;
import com.fpt.blog.enums.PostStatus;
import com.fpt.blog.models.common.request.BaseFilterRequest;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Accessors(chain = true)
@NoArgsConstructor
public class GetAllPostRequest extends BaseFilterRequest<Post> {

    private PostStatus status;

    private String search;

    private Long categoryId;

    private Long userId;

    private String tag;

    @Override
    public Specification<Post> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(cb.equal(root.get(Post.Fields.status), status));
            }

            if (categoryId != null) {
                predicates.add(cb.equal(root.join(Post.Fields.category).get(Category.Fields.id), categoryId));
            }

            if (search != null && !search.isBlank()) {
                String searchTrim = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get(Post.Fields.title)), searchTrim));
            }

            if (userId != null) {
                predicates.add(cb.equal(root.join(Post.Fields.user).get(User.Fields.id), userId));
            }

            if (tag != null && !tag.isBlank()) {
                predicates.add(cb.equal(cb.lower(root.join(Post.Fields.tags).get(Tag.Fields.name)), tag.trim().toLowerCase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
