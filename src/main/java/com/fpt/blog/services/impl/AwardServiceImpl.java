package com.fpt.blog.services.impl;

import com.fpt.blog.entities.Award;
import com.fpt.blog.mappings.AwardMapper;
import com.fpt.blog.models.adward.request.CreateAwardRequest;
import com.fpt.blog.models.adward.request.UpdateAwardRequest;
import com.fpt.blog.models.adward.response.AwardResponse;
import com.fpt.blog.models.common.request.BaseFilterRequest;
import com.fpt.blog.repositories.AwardRepository;
import com.fpt.blog.services.AwardService;
import com.fpt.blog.services.FileService;
import com.fpt.blog.utils.UploadMedia;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AwardServiceImpl implements AwardService {

    private final AwardRepository awardRepository;

    private final AwardMapper awardMapper;

    private final FileService fileService;

    @Override
    public List<AwardResponse> getAlAwards(BaseFilterRequest<Award> request) {
        return awardRepository.findAll(request != null ? request.getSpecification() : null)
                .stream().map(awardMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AwardResponse createAward(CreateAwardRequest request) {

        Award award = new Award()
                .setName(request.getName())
                .setDescription(request.getDescription());

        if (!request.getImage().isEmpty()) {
            UploadMedia uploadMedia = fileService.uploadFile(request.getImage());
            award.setImage(uploadMedia.getUrl());
        }

        Award savedAward = awardRepository.save(award);

        return awardMapper.toResponse(savedAward);

    }

    @Override
    @SneakyThrows
    @Transactional
    public AwardResponse updateAward(long id, UpdateAwardRequest request) {

        Award award = awardRepository.findById(id)
                .orElseThrow(() -> new Exception("Award not found"));

        award.setName(request.getName());
        award.setDescription(request.getDescription());

        if (!request.getImage().isEmpty()) {
            UploadMedia uploadMedia = fileService.uploadFile(request.getImage());
            award.setImage(uploadMedia.getUrl());
        }

        Award savedAward = awardRepository.save(award);

        return awardMapper.toResponse(savedAward);

    }

    @Override
    @SneakyThrows
    @Transactional
    public AwardResponse deleteAward(long id) {
        Award award = awardRepository.findById(id)
                .orElseThrow(() -> new Exception("Award not found"));


        awardRepository.delete(award);

        return awardMapper.toResponse(award);

    }

    @Override
    public AwardResponse getAward(long id) {
        Award award = awardRepository.findById(id)
                .orElse(null);

        return awardMapper.toResponse(award);
    }
}
