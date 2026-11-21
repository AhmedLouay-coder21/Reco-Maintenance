package com.Reco.backend.controller;

import com.Reco.backend.dto.response.CacheListResponse;
import com.Reco.backend.dto.response.RefreshResponse;
import com.Reco.backend.dto.response.TrendingListResponse;
import com.Reco.backend.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Recommendation", description = "Trending items, cross-sell hints and the recommendation cache")
@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Operation(summary = "List trending products by popularity score")
    @GetMapping("/popular")
    public ResponseEntity<TrendingListResponse> getTrending() {
        return ResponseEntity.ok(recommendationService.getTrending());
    }

    @Operation(summary = "List products frequently bought together with a product")
    @GetMapping("/products/{productId}/frequently-bought-together")
    public ResponseEntity<Map<String, Object>> getFrequentlyBought(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(recommendationService.getFrequentlyBoughtTogether(productId, limit));
    }

    @Operation(summary = "List the cached recommendation entries")
    @GetMapping("/cache")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CacheListResponse> getCache() {
        return ResponseEntity.ok(recommendationService.getCache());
    }

    @Operation(summary = "Recompute and store the recommendation cache")
    @PostMapping("/refresh")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RefreshResponse> refreshCache() {
        return ResponseEntity.ok(recommendationService.refreshCache());
    }

    @Operation(summary = "Delete a single cache entry")
    @DeleteMapping("/cache/{cacheId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCacheEntry(@PathVariable Long cacheId) {
        recommendationService.deleteCacheEntry(cacheId);
        return ResponseEntity.noContent().build();
    }

}