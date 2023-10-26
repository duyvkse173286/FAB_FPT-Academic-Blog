package com.fpt.blog.models.adward.request;

import com.fpt.blog.entities.Award;
import com.fpt.blog.entities.Category;
import com.fpt.blog.enums.Collection;
import com.fpt.blog.models.common.request.BaseFilterRequest;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GetAllAwardRequest extends BaseFilterRequest<Award> {

    private String search;

    @Override
    public Specification<Award> getSpecification() {
        return ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(search)) {
                search = search.trim().toLowerCase();
                predicates.add(cb.like(cb.lower(root.get(Award.Fields.name)), "%" + search + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }
}
