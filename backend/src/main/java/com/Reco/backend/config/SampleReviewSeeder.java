package com.Reco.backend.config;

import com.Reco.backend.model.Product;
import com.Reco.backend.model.Review;
import com.Reco.backend.model.Role;
import com.Reco.backend.model.User;
import com.Reco.backend.repository.ProductRepository;
import com.Reco.backend.repository.ReviewRepository;
import com.Reco.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MR-05: seeds a few sample reviews so the public "top reviews" endpoint
 * returns content on a fresh installation. Runs only when there are no
 * reviews yet and both products and customer accounts exist.
 */
@Component
@RequiredArgsConstructor
public class SampleReviewSeeder implements CommandLineRunner {

    private static final int[] RATINGS = {5, 5, 4, 4, 3};
    private static final String[] COMMENTS = {
            "Exactly what I hoped for. The finish and weight feel premium.",
            "Fast delivery and the piece looks even better in person.",
            "Great quality overall, packaging could be a little sturdier.",
            "Really happy with this. Would buy from RECO again.",
            "Good product, though it took longer to arrive than expected."
    };

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        if (reviewRepository.count() > 0) {
            return;
        }

        List<Product> products = productRepository.findAll();
        List<User> customers = userRepository.findAll().stream()
                .filter(user -> user.getRole() == Role.CUSTOMER && user.getDeletedAt() == null)
                .collect(Collectors.toList());

        if (products.isEmpty() || customers.isEmpty()) {
            return;
        }

        int maxPairs = products.size() * customers.size();
        int total = Math.min(RATINGS.length, maxPairs);

        for (int i = 0; i < total; i++) {
            User user = customers.get(i % customers.size());
            Product product = products.get((i / customers.size()) % products.size());
            reviewRepository.save(Review.builder()
                    .rating(RATINGS[i])
                    .comment(COMMENTS[i])
                    .user(user)
                    .product(product)
                    .build());
        }
    }
}
