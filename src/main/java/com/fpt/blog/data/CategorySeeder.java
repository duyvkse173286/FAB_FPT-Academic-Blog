package com.fpt.blog.data;

import com.fpt.blog.entities.Category;
import com.fpt.blog.enums.Collection;
import com.fpt.blog.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CategorySeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;


    /**
     * Import initialize category data
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            List<Category> categories = Arrays.asList(
                    new Category()
                            .setName("Thông báo từ BQT")
                            .setDescription("Các thông báo từ ban quản trị")
                            .setImage("https://fuoverflow.com/data/assets/nodeicons/1.png")
                            .setCollection(Collection.OPERATING),
                    new Category()
                            .setName("Nội quy")
                            .setDescription("Nội quy của forum")
                            .setImage("https://fuoverflow.com/data/assets/nodeicons/2.png")
                            .setCollection(Collection.OPERATING),
                    new Category()
                            .setName("Chợ trời")
                            .setDescription("Mua bán, trao đổi,...")
                            .setImage("https://fuoverflow.com/data/assets/nodeicons/6.png")
                            .setCollection(Collection.BUSINESS),
                    new Category()
                            .setName("Tìm mentor")
                            .setDescription("Tìm kiếm mentor")
                            .setImage("https://fuoverflow.com/data/assets/nodeicons/11.png")
                            .setCollection(Collection.BUSINESS),
                    new Category()
                            .setName("Hỏi đáp")
                            .setDescription("Khu vực hỏi đáp")
                            .setImage("https://fuoverflow.com/data/assets/nodeicons/27.png")
                            .setCollection(Collection.FAP),
                    new Category()
                            .setName("Tin tức")
                            .setDescription("Khu vực tin tức")
                            .setImage("https://fuoverflow.com/data/assets/nodeicons/5.png")
                            .setCollection(Collection.COMMUNITY),

                    new Category()
                            .setName("Góp ý")
                            .setDescription("Khu vực ghi nhận những góp ý, đóng góp")
                            .setImage("https://fuoverflow.com/data/assets/nodeicons/5.png")
                            .setCollection(Collection.COMMUNITY),

                    new Category()
                            .setName("Kì 1")
                            .setDescription("Những môn học kì 1")
                            .setImage("https://static-00.iconduck.com/assets.00/open-book-icon-512x512-axlcbmma.png")
                            .setCollection(Collection.DOCUMENT),

                    new Category()
                            .setName("Kì 2")
                            .setDescription("Những môn học kì 2")
                            .setImage("https://static-00.iconduck.com/assets.00/open-book-icon-512x512-axlcbmma.png")
                            .setCollection(Collection.DOCUMENT),

                    new Category()
                            .setName("Kì 3")
                            .setDescription("Những môn học kì 3")
                            .setImage("https://static-00.iconduck.com/assets.00/open-book-icon-512x512-axlcbmma.png")
                            .setCollection(Collection.DOCUMENT),

                    new Category()
                            .setName("Kì 4")
                            .setDescription("Những môn học kì 4")
                            .setImage("https://static-00.iconduck.com/assets.00/open-book-icon-512x512-axlcbmma.png")
                            .setCollection(Collection.DOCUMENT),

                    new Category()
                            .setName("Kì 5")
                            .setDescription("Những môn học kì 5")
                            .setImage("https://static-00.iconduck.com/assets.00/open-book-icon-512x512-axlcbmma.png")
                            .setCollection(Collection.DOCUMENT),

                    new Category()
                            .setName("Kì 6")
                            .setDescription("Những môn học kì 6")
                            .setImage("https://static-00.iconduck.com/assets.00/open-book-icon-512x512-axlcbmma.png")
                            .setCollection(Collection.DOCUMENT),

                    new Category()
                            .setName("Kì 7")
                            .setDescription("Những môn học kì 7")
                            .setImage("https://static-00.iconduck.com/assets.00/open-book-icon-512x512-axlcbmma.png")
                            .setCollection(Collection.DOCUMENT),

                    new Category()
                            .setName("Kì 8")
                            .setDescription("Những môn học kì 8")
                            .setImage("https://static-00.iconduck.com/assets.00/open-book-icon-512x512-axlcbmma.png")
                            .setCollection(Collection.DOCUMENT),

                    new Category()
                            .setName("Kì 9")
                            .setDescription("Những môn học kì 9")
                            .setImage("https://static-00.iconduck.com/assets.00/open-book-icon-512x512-axlcbmma.png")
                            .setCollection(Collection.DOCUMENT)
                    );

            categoryRepository.saveAll(categories);

        }

    }
}
