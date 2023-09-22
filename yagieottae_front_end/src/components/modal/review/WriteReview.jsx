import { Textarea } from "@mui/joy";
import { isLoggedIn } from "components/user/User";
import axiosInstance from "utils/AxiosInstance";
import { useState } from "react";
import { Button } from "react-bootstrap";
import { AiFillStar } from "react-icons/ai";
import { useSelector } from "react-redux";

function WriteReview({ pill, getReviews })
{
    const backEndURL = useSelector(state => state.ServerURLReducer.backEndURL);

    const reviewInitialState = {
        id: 0, //PK
        rate: 0, //평점
        reviewMessage: '', //리뷰메시지
        pillId: pill.id, //약ID
        regDate: Date.now(), //작성시간
        editDate: Date.now(),
    };

    const [review, setReview] = useState(reviewInitialState);

    const MAX_RATE = 5;

    const clickStar = (index) =>
    {
        setReview({ ...review, rate: index });
    }

    const changeReviewMessage = (e) =>
    {
        e.preventDefault();
        setReview({
            ...review,
            reviewMessage: e.target.value
        });
    }

    const clickSubmitBtn = () =>
    {
        const url = backEndURL + "/review/save";

        axiosInstance.post(url, review)
            .then((result) =>
            {
                alert(result.data.message);
                getReviews();
                setReview(reviewInitialState);
            }).catch((error) =>
            {
                alert(error.message);
            })
    }

    return (
        <>
            {isLoggedIn() ?
                <>
                    <div className="flex-row-center-center div-icon-review-star">
                        {/* {displayStars(30, review.rate)} */}
                    </div>
                    <Textarea minRows={2} placeholder={`${pill.itemName}에 대한 리뷰를 남겨주세요!`} className="textarea-review" value={review.reviewMessage} onChange={(e) => { changeReviewMessage(e) }}></Textarea >
                    <Button id="btn-review-submit" onClick={clickSubmitBtn}>등록</Button>
                </>
                :
                ""
            }
        </>
    )
}

export default WriteReview;