package com.fpt.blog.models.post.request;

import com.fpt.blog.entities.Category;
import com.fpt.blog.entities.Post;
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
public class GetAllPostRequest implements BaseFilterRequest<Post> {

    private PostStatus status;

    private String search;

    private Long categoryId;

    @Override
    public Specification<Post> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status == null) {
                predicates.add(cb.equal(root.get(Post.Fields.status), status));
            }

            if (categoryId != null) {
                predicates.add(cb.equal(root.join(Post.Fields.category).get(Category.Fields.id), categoryId));
            }

            if (search != null && !search.isBlank()) {
                search = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get(Post.Fields.title)), search));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
