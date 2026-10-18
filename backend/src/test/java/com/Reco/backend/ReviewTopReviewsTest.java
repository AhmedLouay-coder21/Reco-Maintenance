package com.Reco.backend;

import com.Reco.backend.model.Category;
import com.Reco.backend.model.Product;
import com.Reco.backend.model.Review;
import com.Reco.backend.model.Role;
import com.Reco.backend.model.User;
import com.Reco.backend.repository.CategoryRepository;
import com.Reco.backend.repository.ProductRepository;
import com.Reco.backend.repository.ReviewRepository;
import com.Reco.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.flyway.enabled=false"
})
@Transactional
class ReviewTopReviewsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        Category category = categoryRepository.save(Category.builder()
                .name("Top Reviews Category")
                .description("category for top reviews test")
                .build());

        product = productRepository.save(Product.builder()
                .name("Top Reviews Product")
                .description("a product used by the top reviews test")
                .mainImageUrl("http://example.com/product.png")
                .price(new BigDecimal("19.99"))
                .stockQuantity(10)
                .tags("test")
                .category(category)
                .build());

        User alice = saveCustomer("alice", "alice@example.com");
        User bob = saveCustomer("bob", "bob@example.com");
        User carol = saveCustomer("carol", "carol@example.com");

        reviewRepository.save(Review.builder().rating(3).comment("meh").product(product).user(alice).build());
        reviewRepository.save(Review.builder().rating(5).comment("great").product(product).user(bob).build());
        reviewRepository.save(Review.builder().rating(4).comment("good").product(product).user(carol).build());
        reviewRepository.flush();
    }

    @Test
    void topReviewsArePublicAndOrderedByRatingDescending() throws Exception {
        mockMvc.perform(get("/api/v1/reviews/top"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].rating").value(5))
                .andExpect(jsonPath("$[1].rating").value(4))
                .andExpect(jsonPath("$[2].rating").value(3))
                .andExpect(jsonPath("$[0].comment").value("great"))
                .andExpect(jsonPath("$[0].username").value("bob"))
                .andExpect(jsonPath("$[0].productId").value(product.getId()));
    }

    @Test
    void topReviewsHonorsLimit() throws Exception {
        mockMvc.perform(get("/api/v1/reviews/top").param("limit", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].rating").value(5))
                .andExpect(jsonPath("$[1].rating").value(4));
    }

    private User saveCustomer(String username, String email) {
        return userRepository.save(User.builder()
                .firstName(username)
                .lastName("Customer")
                .username(username)
                .email(email)
                .passwordHash("$2a$10$notarealhashnotarealhashnotarealhashnotarealhash")
                .role(Role.CUSTOMER)
                .build());
    }
}
