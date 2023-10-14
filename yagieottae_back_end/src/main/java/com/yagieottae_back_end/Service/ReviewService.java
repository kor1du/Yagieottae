package com.yagieottae_back_end.Service;

import com.yagieottae_back_end.Dto.ResponseDto;
import com.yagieottae_back_end.Dto.ReviewDto;
import com.yagieottae_back_end.Entity.Pill;
import com.yagieottae_back_end.Entity.Review;
import com.yagieottae_back_end.Entity.User;
import com.yagieottae_back_end.Exception.CustomBadRequestException;
import com.yagieottae_back_end.Repository.PillRepository;
import com.yagieottae_back_end.Repository.ReviewRepository;
import com.yagieottae_back_end.Repository.UserRepository;
import com.yagieottae_back_end.Util.JsonUtil;
import com.yagieottae_back_end.Util.SessionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService
{
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PillRepository pillRepository;

    //리뷰 등록 / 수정
    public ResponseDto save(ReviewDto.Request reviewRequestDto)
    {
        Boolean isNewRequest = reviewRequestDto.getId() == 0;

        try
        {
            User user = SessionUtil.getUserFromDB();
            Pill pill = pillRepository
                    .findById(reviewRequestDto.getPillId())
                    .orElseThrow(() -> new CustomBadRequestException("약 정보가 존재하지 않습니다."));

            List<Review> reviews = reviewRepository
                    .findByUserIdAndPillId(user.getId(), pill.getId())
                    .orElseThrow(() -> new CustomBadRequestException(pill.getItemName() + "에 작성한 리뷰가 null 입니다!"));

            if (reviews
                    .stream()
                    .count() > 2) //유저가 작성한 약 리뷰가 3개 이상
            {
                throw new CustomBadRequestException("리뷰는 약 하나당 3개까지만 등록 가능합니다. 추가 등록을 원하시는 경우 작성했던 리뷰를 삭제 후 등록해주세요!");
            }

            Date regDate = isNewRequest ? new Date() : reviewRequestDto.getRegDate();
            Date editDate = isNewRequest ? new Date() : reviewRequestDto.getEditDate();

            Review review = Review
                    .builder()
                    .id(reviewRequestDto.getId())
                    .rate(reviewRequestDto.getRate())
                    .reviewMessage(reviewRequestDto.getReviewMessage())
                    .user(user)
                    .pill(pill)
                    .regDate(regDate)
                    .editDate(editDate)
                    .build();

            reviewRepository.save(review);

            return ResponseDto
                    .builder()
                    .httpStatus(HttpStatus.OK.value())
                    .message(String.format("리뷰가 정상적으로 %s되었습니다.", isNewRequest ? "등록" : "수정"))
                    .build();
        }
        catch (CustomBadRequestException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            log.error("Unhandled Exception occured", e);
            throw new CustomBadRequestException(String.format("리뷰 %s에 실패했습니다. 잠시 후 다시 시도해주세요!", isNewRequest ? "등록" : "수정"));
        }
    }

    //리뷰 조회
    public ResponseDto read(Long pillId, Pageable page)
    {
        try
        {
            Page<ReviewDto.Response> reviewList = reviewRepository
                    .findByPillId(pillId, page)
                    .get();

            return ResponseDto
                    .builder()
                    .httpStatus(HttpStatus.OK.value())
                    .body(JsonUtil.ObjectToJsonObject("reviewList", reviewList))
                    .message("리뷰 목록 조회 성공")
                    .build();
        }
        catch (Exception e)
        {
            log.error("Unhandled Exception occured", e);
            throw new CustomBadRequestException("리뷰 목록 조회에 실패하였습니다. 잠시 후 다시 시도해주세요!");
        }
    }

    public ResponseDto delete(Long reviewId)
    {
        try
        {
            User user = SessionUtil.getUserFromDB();

            Review review = reviewRepository
                    .findById(reviewId)
                    .orElseThrow(() -> new CustomBadRequestException("해당 리뷰가 존재하지 않습니다!"));

            if (review
                    .getUser()
                    .getId() != user.getId()) //리뷰 작성자가 아닌 다른 유저가 삭제 요청시 세션에 저장되어있는 유저 정보 비교
            {
                throw new CustomBadRequestException("삭제 권한이 존재하지 않습니다!");
            }

            reviewRepository.deleteById(reviewId);

            return ResponseDto
                    .builder()
                    .httpStatus(HttpStatus.OK.value())
                    .message("리뷰가 삭제되었습니다.")
                    .build();
        }
        catch (CustomBadRequestException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            log.error("Unhandled Exception occured", e);
            throw new CustomBadRequestException("리뷰 삭제에 실패하였습니다. 잠시 후 다시 시도해주세요!");
        }
    }
}
