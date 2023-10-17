package com.fpt.blog.models.common.request;

import org.springframework.data.jpa.domain.Specification;

public interface BaseFilterRequest<T> {
    Specification<T> getSpecification();
}
