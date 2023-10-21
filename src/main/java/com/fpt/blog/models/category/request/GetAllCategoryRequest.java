package com.fpt.blog.models.category.request;

import com.fpt.blog.entities.Category;
import com.fpt.blog.enums.Collection;
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
@NoArgsConstructor
@Accessors(chain = true)
public class GetAllCategoryRequest implements BaseFilterRequest<Category> {

    private String search;

    private Collection collection;


    @Override
    public Specification<Category> getSpecification() {
        return ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get(Category.Fields.name)), "%" + search.trim().toLowerCase() + "%"));
            }

            if (collection != null) {
                predicates.add(cb.equal(root.get(Category.Fields.collection), collection));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }
}
