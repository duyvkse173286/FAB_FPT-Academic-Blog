package com.fpt.blog.services.impl;

import com.fpt.blog.entities.Award;
import com.fpt.blog.mappings.AwardMapper;
import com.fpt.blog.models.adward.response.AwardResponse;
import com.fpt.blog.models.common.request.BaseFilterRequest;
import com.fpt.blog.repositories.AwardRepository;
import com.fpt.blog.services.AwardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AwardServiceImpl implements AwardService {

    private final AwardRepository awardRepository;

    private final AwardMapper awardMapper;

    @Override
    public List<AwardResponse> getAlAwards(BaseFilterRequest<Award> request) {
        return awardRepository.findAll(request != null ? request.getSpecification() : null)
                .stream().map(awardMapper::toResponse)
                .collect(Collectors.toList());
    }

}
