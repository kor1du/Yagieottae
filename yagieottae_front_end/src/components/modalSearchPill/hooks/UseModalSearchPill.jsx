import { Server_URL } from "constants/ServerURL";
import { useEffect, useState } from "react";
import axiosInstance from "utils/AxiosInstance";

export default function UseModalSearchPill()
{
    const [showModal, setShowModal] = useState(false);
    const [pillName, setPillName] = useState("");
    const [pillList, setPillList] = useState([]);
    const [pagination, setPagination] = useState({
        currentPage: 1,
        itemCountPerPage: 5,
        totalItemCount: 0,
    })

    //약 이름 입력시
    const onChangePillName = (e) =>
    {
        e.preventDefault();
        setPillName(e.target.value);
    }

    //pagination 페이지 변경
    const onChangeCurrentPage = (e) =>
    {
        e.preventDefault();
        const changedPage = parseInt(e.target.innerText);
        fetchPillData(changedPage);
    }

    //검색결과 fetchData
    const fetchPillData = async (page) =>
    {
        const url = Server_URL + "/pill/getPill";

        const params = {
            itemName: pillName,
            page: page - 1,
            size: pagination.itemCountPerPage,
        }

        try
        {
            const result = await axiosInstance.get(url, { params: params });
            const data = result.data.body.pillList;
            setShowModal(true);
            setPillList(data.content);
            setPagination(
                {
                    ...pagination,
                    currentPage: page,
                    totalItemCount: data.totalElements,
                }
            );
        } catch (error)
        {
            alert(error.message);
        }
    }

    //약 이름으로 검색
    const onClickSearchBtn = async () =>
    {
        if (pillName === undefined || pillName === "")
        {
            alert("약 이름을 입력해주세요.");
            return;
        }

        if (pillName.length < 2)
        {
            alert("약 이름은 두글자 이상으로 검색해주세요!");
            return;
        }

        fetchPillData(1);
    }

    return {
        showModal,
        setShowModal,
        pillName,
        pillList,
        pagination,
        onClickSearchBtn,
        onChangePillName,
        onChangeCurrentPage
    }
}