// const { default: axiosInstance } = require("utils/AxiosInstance");
// const { useState } = require("react");
// const { Button } = require("react-bootstrap");
// const { useSelector } = require("react-redux");

// function WrittenReviews({ writtenReviews })
// {
//     const backEndURL = useSelector(state => state.ServerURLReducer.backEndURL);
//     const [currentPage, setCurrentPage] = useState(1);
//     const COUNT_PER_PAGE = 3;

//     const changeCurrentPage = (e, page) =>
//     {
//         e.preventDefault();
//         setCurrentPage(page)
//     }

//     const clickDeleteReviewBtn = (id) =>
//     {
//         const url = backEndURL + "/review/delete";
//         axiosInstance.delete(url, { params: { reviewId: id } })
//             .then((result) =>
//             {
//                 alert(result.data.message);
//                 getReviews();
//             }).catch((error) =>
//             {
//                 alert(error);
//             })
//     }

//     return (
//         <>
//             {
//                 writtenReviews !== undefined && writtenReviews.length > 0 ? (
//                     <div>
//                         {writtenReviews.slice((currentPage - 1) * COUNT_PER_PAGE, currentPage * COUNT_PER_PAGE).map((item) =>
//                         {
//                             const editDate = new Date(item.editDate);
//                             return (
//                                 <div key={item.id} className="div-written-review">
//                                     <div className="flex-row-spaceBetween-center div-profile">
//                                         <img src={item.profileImgPath} alt="img-profile-img" className="img-profile-img" />
//                                         <div className="div-btn-edit-delete">
//                                             <Button variant="success">수정</Button>
//                                             <Button variant="danger" onClick={() => { clickDeleteReviewBtn(item.id) }}>삭제</Button>
//                                         </div>
//                                     </div>
//                                     <div className="review-info">
//                                         <p className="text-nickname">{item.nickname}</p>
//                                         <p className="text-rate">평점 : {displayStars(15, item.rate)}</p>
//                                         <p className="text-edit-date">최종 수정일 : {moment(editDate).format("YYYY-MM-DD HH:mm:ss")}</p>
//                                     </div>
//                                     <p>{item.reviewMessage}</p>
//                                 </div>
//                             )
//                         })}
//                         <div className="flex-row-center-center">
//                             <Pagination
//                                 defaultPage={1}
//                                 count={Math.ceil(MAX_ITEM_COUNT / COUNT_PER_PAGE)}
//                                 page={currentPage}
//                                 onChange={changeCurrentPage}
//                                 color={"primary"}
//                             />
//                         </div>
//                     </div>
//                 ) :
//                     "리뷰가 없습니다!"
//             }
//         </>
//     )
// }