import { Server_URL } from "constants/ServerURL";
import { useState } from "react";
import axiosInstance from "utils/AxiosInstance";

function UseModalReview(pill)
{
    const [showModal, setShowModal] = useState(false);
    const [review, setReview] = useState({
        id: 0,
        rate: 0,
        reviewMessage: '',
        pillId: pill.id,
    })
    const [reviewList, setReviewList] = useState([]);
    const [pagination, setPagination] = useState({
        currentPage: 1,
        itemCountPerPage: 3,
        totalItemCount: 0,
    })

    const onClickStars = (i) =>
    {
        setReview({
            ...review,
            rate: i,
        })
    }

    const onClickSubmitBtn = async () =>
    {
        const url = Server_URL + "/review/save";

        try
        {
            const result = await axiosInstance.post(url, review);
            alert(result.data.message);
            fetchReviews(1);
        } catch (error)
        {
            alert(error.message);
        }
    }

    const onClickModalOpenBtn = () =>
    {
        setShowModal(true);
        fetchReviews(1);
    }

    const onChangeCurrentPage = (e) =>
    {
        e.preventDefault();
        const changedPage = parseInt(e.target.innerText);
        fetchReviews(changedPage);
    }

    const onChangeReviewMessage = (e) =>
    {
        setReview({
            ...review,
            reviewMessage: e.target.value,
        })
    }

    const onClickDeleteReviewBtn = async (id) =>
    {
        const url = Server_URL + "/review/delete";

        try
        {
            const params =
            {
                reviewId: id
            };

            const result = await axiosInstance.delete(url, { params });
            alert(result.data.message);
            fetchReviews(pagination.currentPage);
        } catch (error)
        {
            alert(error.message);
        }
    }

    const fetchReviews = async (page) =>
    {
        const url = Server_URL + "/review/read";

        try
        {
            const params =
            {
                pillId: pill.id,
                page: page - 1,
                size: pagination.itemCountPerPage,
            }

            const result = await axiosInstance.get(url, { params });
            const reviewList = result.data.body.reviewList;
            setPagination({
                ...pagination,
                currentPage: page,
                totalItemCount: reviewList.totalElements,
            })
            setReviewList(result.data.body.reviewList.content);
        } catch (error)
        {
            alert(error.message);
        }
    }

    return {
        showModal,
        setShowModal,
        onClickModalOpenBtn,
        onClickDeleteReviewBtn,
        reviewList,
        pagination,
        onChangeCurrentPage,
        review,
        setReview,
        onClickStars,
        onClickSubmitBtn,
        onChangeReviewMessage,
    }
}

export default UseModalReview;