

import Drugs from 'assets/imgs/drugs.png';
import { Textarea } from "@mui/joy";
import "components/modalAlramReview/styles/modalReview.scss";
import { AiFillStar } from "react-icons/ai";
import { useSelector } from "react-redux";
import axiosInstance from "utils/AxiosInstance";
import moment from "moment/moment";
import { Pagination } from "@mui/material";
import { Button } from "react-bootstrap";
import UseModalReview from "./hooks/UseModalReview";
import ModalInstance from "utils/ModalInstance";
import { IsLoggedIn } from 'utils/IsLoggedIn';

function ModalReview({ pill })
{
    const {
        showModal,
        setShowModal,
        onClickModalOpenBtn,
        onClickDeleteReviewBtn,
        reviewList,
        pagination,
        onChangeCurrentPage,
        review,
        onClickStars,
        onClickSubmitBtn,
        onChangeReviewMessage
    } = UseModalReview(pill);
    const { currentPage, itemCountPerPage, totalItemCount } = pagination;

    const user = useSelector(state => state.UserReducer.user);

    const displayStars = (size, rate, useOnClickStars) =>
    {
        const arr = [];

        for (let i = 1; i <= 5; i++)
        {
            arr.push(
                <AiFillStar
                    key={`review-star-${i}`}
                    size={size}
                    className="icon-review-star"
                    color={rate >= i ? 'RGB(254, 157, 0)' : 'lightgray'}
                    onClick={() =>
                    {
                        if (useOnClickStars)
                        {
                            onClickStars(i);
                        }
                    }}
                />)
        }

        return arr;
    }

    return (
        <>
            <div id="container-modal-review">
                <Button variant="dark" onClick={onClickModalOpenBtn}>리뷰 보기</Button>
                <ModalInstance
                    show={showModal}
                    setShow={setShowModal}
                    title={
                        <>
                            <div className="flex-row-center-center">
                                <img src={pill.imagePath === null ? { Drugs } : pill.imagePath} className='img-modal-title-pill' alt='img-pill' onError={(e) => { e.target.src = Drugs; }} />
                                <div className="div-text-modal-title-pill">
                                    <p className="text-modal-title-pill">{pill.itemName}</p>
                                </div>
                            </div >
                        </>
                    }
                    body={
                        <>
                            <div className="flex-column-center-center">
                                {
                                    reviewList && reviewList.length > 0 ? (
                                        <div>
                                            {reviewList.map((item) =>
                                            {
                                                const editDate = new Date(item.editDate);

                                                return (
                                                    <div className="div-written-review">
                                                        <div className="flex-row-spaceBetween-center div-profile">
                                                            <img src={item.profileImgPath} alt="img-profile-img" className="img-profile-img" />
                                                            {user.nickname === item.nickname ?
                                                                <>
                                                                    <div className="div-btn-edit-delete">
                                                                        <Button variant="danger" onClick={() => { onClickDeleteReviewBtn(item.id) }}>삭제</Button>
                                                                    </div>
                                                                </>
                                                                :
                                                                ""
                                                            }
                                                        </div>
                                                        <div className="review-info">
                                                            <p className="text-nickname">{item.nickname}</p>
                                                            <p className="text-rate">평점 : {displayStars(15, item.rate, false)}</p>
                                                            <p className="text-edit-date">최종 수정일 : {moment(editDate).format("YYYY-MM-DD HH:mm:ss")}</p>
                                                        </div>
                                                        <p>{item.reviewMessage}</p>
                                                    </div>
                                                );
                                            })}
                                            <div className="flex-row-center-center">
                                                <Pagination
                                                    defaultPage={1}
                                                    count={Math.ceil(totalItemCount / itemCountPerPage)}
                                                    page={currentPage}
                                                    onChange={onChangeCurrentPage}
                                                    color={"primary"}
                                                />
                                            </div>
                                        </div>
                                    ) :
                                        "리뷰가 없습니다!"
                                }
                                {IsLoggedIn() ?
                                    <>
                                        {IsLoggedIn() ?
                                            <>
                                                <div className="flex-row-center-center div-icon-review-star">
                                                    {displayStars(30, review.rate, true)}
                                                </div>
                                                <Textarea minRows={2} placeholder={'리뷰를 남겨주세요!'} className="textarea-review" value={review.reviewMessage} onChange={onChangeReviewMessage} />
                                                <Button id="btn-review-submit" onClick={onClickSubmitBtn}>등록</Button>
                                            </>
                                            :
                                            ""
                                        }
                                    </>
                                    :
                                    ""
                                }
                            </div>
                        </>
                    }
                    contentClassName={"modal-review"} />
            </div>
        </>
    )
}

export default ModalReview;