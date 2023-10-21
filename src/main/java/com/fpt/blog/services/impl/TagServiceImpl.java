package com.fpt.blog.services.impl;

import com.fpt.blog.entities.Tag;
import com.fpt.blog.mappings.TagMapper;
import com.fpt.blog.models.tag.GetAllTagRequest;
import com.fpt.blog.models.tag.TagResponse;
import com.fpt.blog.repositories.TagRepository;
import com.fpt.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    private final TagMapper tagMapper;

    @Override
    public List<TagResponse> getAllTags() {
        return tagRepository.findAll()
                .stream().map(tagMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TagResponse> getAllTags(GetAllTagRequest request) {
        return tagRepository.findAll(request.getSpecification())
                .stream().map(tagMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TagResponse createTag(String name) {
        Tag tag = tagRepository.findFirstByName(name)
                .orElse(null);

        if (tag != null) {
            return tagMapper.toResponse(tag);
        }

        tag = new Tag(name);
        Tag savedTag = tagRepository.save(tag);

        return tagMapper.toResponse(savedTag);
    }

    @Override
    public TagResponse getTag(String name) {
        Tag tag = tagRepository.findFirstByName(name)
                .orElse(null);
        return tagMapper.toResponse(tag);
    }

    @Override
    @SneakyThrows
    @Transactional
    public TagResponse updateTag(long tagId, String name) {

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new Exception("Tag not found"));

        tag.setName(name);
        Tag savedTag = tagRepository.save(tag);

        return tagMapper.toResponse(savedTag);
    }

    @Override
    @SneakyThrows
    public TagResponse removeTag(long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new Exception("Tag not found"));

        tagRepository.delete(tag);

        return tagMapper.toResponse(tag);
    }

}
