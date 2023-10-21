package com.fpt.blog.models.tag;

import com.fpt.blog.entities.Tag;
import com.fpt.blog.models.common.request.BaseFilterRequest;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAllTagRequest implements BaseFilterRequest<Tag> {

    private String search;

    @Override
    public Specification<Tag> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(search)) {
                search = search.trim().toLowerCase();
                predicates.add(cb.like(cb.lower(root.get(Tag.Fields.name)), "%" + search + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
