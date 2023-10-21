package com.fpt.blog.data;

import com.fpt.blog.entities.Award;
import com.fpt.blog.repositories.AwardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AwardSeeder implements CommandLineRunner {

    private final AwardRepository awardRepository;

    /**
     * Import initialize awards data
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        if (awardRepository.count() == 0) {
            List<Award> awards = Arrays.asList(
                    new Award()
                            .setName("Bài viết ấn tượng")
                            .setDescription("Danh hiệu dành cho bài viết ấn tượng")
                            .setImage("https://images.careerbuilder.vn/content/images/hay-biet-tao-an-tuong-ban-dau-careerbuilder.jpg"),
                    new Award()
                            .setName("Bài viết cảm xúc")
                            .setDescription("Danh hiệu dành cho bài viết giàu cảm xúc")
                            .setImage("https://images.careerbuilder.vn/content/images/hay-biet-tao-an-tuong-ban-dau-careerbuilder.jpg"),
                    new Award()
                            .setName("Bài viết kiến thức hay")
                            .setDescription("Danh hiệu dành cho bài viết chia sẻ kiến thức hay")
                            .setImage("https://images.careerbuilder.vn/content/images/hay-biet-tao-an-tuong-ban-dau-careerbuilder.jpg"),
                    new Award()
                            .setName("Bài viết nổi bật")
                            .setDescription("Danh hiệu cho bài viết nổi bật")
                            .setImage("https://images.careerbuilder.vn/content/images/hay-biet-tao-an-tuong-ban-dau-careerbuilder.jpg")
            );


            awardRepository.saveAll(awards);
        }
    }
}
