package com.fpt.blog.models.user.request;

import com.fpt.blog.entities.Category;
import com.fpt.blog.entities.Post;
import com.fpt.blog.entities.Tag;
import com.fpt.blog.entities.User;
import com.fpt.blog.enums.Role;
import com.fpt.blog.enums.UserStatus;
import com.fpt.blog.models.common.request.BaseFilterRequest;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetAllUsersRequest implements BaseFilterRequest<User> {

    private String search;

    private UserStatus status;

    private Role role;

    @Override
    public Specification<User> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(cb.equal(root.get(User.Fields.status), status));
            }

            if (search != null && !search.isBlank()) {
                String searchTrim = "%" + search.trim().toLowerCase() + "%";

                List<Predicate> searchPredicates = new ArrayList<>();
                searchPredicates.add(cb.like(root.get(User.Fields.name), searchTrim));
                searchPredicates.add(cb.like(root.get(User.Fields.email), searchTrim));
                searchPredicates.add(cb.like(root.get(User.Fields.phoneNumber), searchTrim));

                predicates.add(
                        cb.or(searchPredicates.toArray(new Predicate[0]))
                );
            }
            
            if (role != null) {
                predicates.add(cb.equal(root.get(User.Fields.role), role));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
