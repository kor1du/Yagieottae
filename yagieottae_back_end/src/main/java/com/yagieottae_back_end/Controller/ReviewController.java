package com.yagieottae_back_end.Controller;

import com.yagieottae_back_end.Dto.ResponseDto;
import com.yagieottae_back_end.Dto.ReviewDto;
import com.yagieottae_back_end.Service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/review")
@RestController
@RequiredArgsConstructor
public class ReviewController
{
    private final ReviewService reviewService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDto> save(@Valid @RequestBody ReviewDto.Request reviewRequestDto)
    {
        return ResponseEntity.ok(reviewService.save(reviewRequestDto));
    }

    @GetMapping("/read")
    public ResponseEntity<ResponseDto> read(@RequestParam Long pillId, Pageable page)
    {
        return ResponseEntity.ok(reviewService.read(pillId, page));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> delete(@RequestParam Long reviewId)
    {
        return ResponseEntity.ok(reviewService.delete(reviewId));
    }
}
