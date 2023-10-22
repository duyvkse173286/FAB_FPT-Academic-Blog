package com.fpt.blog.services;

import com.fpt.blog.entities.Award;
import com.fpt.blog.models.adward.request.CreateAwardRequest;
import com.fpt.blog.models.adward.request.UpdateAwardRequest;
import com.fpt.blog.models.adward.response.AwardResponse;
import com.fpt.blog.models.common.request.BaseFilterRequest;

import java.util.List;

public interface AwardService {

    List<AwardResponse> getAlAwards(BaseFilterRequest<Award> request);

    AwardResponse createAward(CreateAwardRequest request);

    AwardResponse updateAward(long id, UpdateAwardRequest request);

    AwardResponse deleteAward(long id);

    AwardResponse getAward(long id);

}
