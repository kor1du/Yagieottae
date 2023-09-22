//
// import { useRef, useState } from "react";
// import Drugs from 'assets/imgs/drugs.png';
// import { Textarea } from "@mui/joy";
// import "styles/common/modalReview.scss";
// import { AiFillStar, AiOutlineStar } from "react-icons/ai";
// import { useSelector } from "react-redux";
// import axiosInstance from "utils/AxiosInstance";
// import { isLoggedIn } from "components/user/User";
// import moment from "moment/moment";
// import { Pagination } from "@mui/material";
// import { Button } from "react-bootstrap";

// function ModalReview({ pill })
// {
//     const backEndURL = useSelector(state => state.ServerURLReducer.backEndURL);
//     const [showModal, setShowModal] = useState(false);
//     const [writtenReviews, setWrittenReviews] = useState();
//     const MAX_ITEM_COUNT = writtenReviews === undefined ? 0 : writtenReviews.length;
//     const reviewInitialState = {
//         id: 0, //PK
//         rate: 0, //평점
//         reviewMessage: '', //리뷰메시지
//         pillId: pill.id, //약ID
//         regDate: Date.now(), //작성시간
//         editDate: Date.now(),
//     };

//     const [review, setReview] = useState(reviewInitialState);

//     const MAX_RATE = 5;

//     const clickStar = (index) =>
//     {
//         setReview({ ...review, rate: index });
//     }

//     const changeReviewMessage = (e) =>
//     {
//         e.preventDefault();
//         setReview({
//             ...review,
//             reviewMessage: e.target.value
//         });
//     }

//     const displayStars = (size, rate) =>
//     {
//         const arr = [];

//         for (let i = 1; i <= MAX_RATE; i++)
//         {
//             arr.push(<AiFillStar key={`review-star-${i}`} size={size} className="icon-review-star" onClick={() => { clickStar(i) }} color={rate >= i ? 'RGB(254, 157, 0)' : 'lightgray'} />)
//         }

//         return arr;
//     }

//     const clickSubmitBtn = () =>
//     {
//         const url = backEndURL + "/review/save";

//         axiosInstance.post(url, review)
//             .then((result) =>
//             {
//                 alert(result.data.message);
//                 getReviews();
//                 setReview(reviewInitialState);
//             }).catch((error) =>
//             {
//                 alert(error.message);
//             })
//     }

//     const modalTitle = () =>
//     {
//         return (
//             <>
//                 <div className="flex-row-center-center">
//                     < img src={pill.imagePath === null ? { Drugs } : pill.imagePath} className='img-modal-title-pill' alt='img-pill' onError={(e) => { e.target.src = Drugs; }
//                     } />
//                     < div className="div-text-modal-title-pill" >
//                         <p className="text-modal-title-pill">{pill.itemName}</p>
//                     </div >
//                 </div >
//             </>
//         )
//     }

//     const modalBody = () =>
//     {
//         return (
//             <div className="flex-column-center-center">
//                 {
//                     writtenReviews !== undefined && writtenReviews.length > 0 ? (
//                         <div>
//                             {writtenReviews.slice((currentPage - 1) * COUNT_PER_PAGE, currentPage * COUNT_PER_PAGE).map((item) =>
//                             {
//                                 const editDate = new Date(item.editDate);
//                                 return (
//                                     <div key={item.id} className="div-written-review">
//                                         <div className="flex-row-spaceBetween-center div-profile">
//                                             <img src={item.profileImgPath} alt="img-profile-img" className="img-profile-img" />
//                                             <div className="div-btn-edit-delete">
//                                                 <Button variant="success">수정</Button>
//                                                 <Button variant="danger" onClick={() => { clickDeleteReviewBtn(item.id) }}>삭제</Button>
//                                             </div>
//                                         </div>
//                                         <div className="review-info">
//                                             <p className="text-nickname">{item.nickname}</p>
//                                             <p className="text-rate">평점 : {displayStars(15, item.rate)}</p>
//                                             <p className="text-edit-date">최종 수정일 : {moment(editDate).format("YYYY-MM-DD HH:mm:ss")}</p>
//                                         </div>
//                                         <p>{item.reviewMessage}</p>
//                                     </div>
//                                 )
//                             })}
//                             <div className="flex-row-center-center">
//                                 <Pagination
//                                     defaultPage={1}
//                                     count={Math.ceil(MAX_ITEM_COUNT / COUNT_PER_PAGE)}
//                                     page={currentPage}
//                                     onChange={changeCurrentPage}
//                                     color={"primary"}
//                                 />
//                             </div>
//                         </div>
//                     ) :
//                         "리뷰가 없습니다!"
//                 }
//                 {isLoggedIn() ?
//                     <div className="flex-column-center-center">
//                         <div className="flex-row-center-center div-icon-review-star">
//                             {displayStars(30, review.rate)}
//                         </div>
//                         <Textarea minRows={2} placeholder={`${pill.itemName}에 대한 리뷰를 남겨주세요!`} className="textarea-review" value={review.reviewMessage} onChange={(e) => { changeReviewMessage(e) }}></Textarea >
//                         <Button id="btn-review-submit" onClick={clickSubmitBtn}>등록</Button>
//                     </div>
//                     :
//                     ""
//                 }
//             </div>
//         )
//     }

//     const clickModalOpenBtn = () =>
//     {
//         setShowModal(true);
//         // setReview(reviewInitialState);
//         setCurrentPage(1);
//         getReviews();
//     }

//     const getReviews = () =>
//     {
//         const url = backEndURL + "/review/read";

//         axiosInstance.get(url, {
//             params: {
//                 pillId: pill.id
//             }
//         }).then((result) =>
//         {
//             const data = result.data.body.reviewList;
//             setWrittenReviews(data);
//         }).catch((error) =>
//         {
//             alert(error.message);
//         })
//     }

//     return (
//         <>
//             <div id="container-modal-review">
//                 <Button variant="dark" onClick={clickModalOpenBtn}>리뷰 보기</Button>
//                 <ModalInstance show={showModal} setShow={setShowModal} title={modalTitle()} body={modalBody()} contentClassName={"modal-review"} />
//             </div>
//         </>
//     )
// }

// export default ModalReview;