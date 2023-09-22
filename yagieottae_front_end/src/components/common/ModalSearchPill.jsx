import axiosInstance from "utils/AxiosInstance";

import { Button } from "react-bootstrap";
import { useSelector } from "react-redux";
import ModalPillInfo from "components/common/ModalPillInfo";
import Drugs from 'assets/imgs/drugs.png';
import { Pagination } from "@mui/material";
import { useState } from "react";
import "styles/common/modalSearchPill.scss";
import ModalInstance from "utils/ModalInstance";

const { Input } = require("@mui/joy");

function ModalSearchPill()
{
    const backEndURL = useSelector(state => state.ServerURLReducer.backEndURL);
    const [showModal, setShowModal] = useState(false);
    const [pillName, setPillName] = useState("");
    const [pillList, setPillList] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const COUNT_PER_PAGE = 5;
    const MAX_ITEM_COUNT = pillList.length;

    const changePillName = (e) =>
    {
        e.preventDefault();
        setPillName(e.target.value);
    }

    const changeCurrentPage = (e, page) =>
    {
        e.preventDefault();
        setCurrentPage(page)
    }


    //약 이름으로 검색
    const searchPill = () =>
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

        const url = backEndURL + "/pill/findPill";

        axiosInstance.get(url, {
            params: {
                itemName: pillName,
            }
        }).then((result) =>
        {
            const data = result.data.body.pillList;
            setShowModal(true);
            setPillList(data);
        }).catch((error) =>
        {
            alert(error.message);
        })
    }

    const modalBody = () =>
    {
        return (
            <div className="flex-column-center-center pill-list">
                {
                    pillList.slice((currentPage - 1) * COUNT_PER_PAGE, currentPage * COUNT_PER_PAGE).map((item) =>
                    {
                        return (
                            <div className='flex-row-center-center pill' key={item.itemSeq}>
                                <img src={item.imagePath == null ? { Drugs } : item.imagePath} alt='drugs' className='img-modal-title-pill' onError={(e) => { e.target.src = Drugs; }} />
                                <div className="div-text-modal-body-pill">
                                    <p className='text-modal-body-pill'> {item.itemName} </p>
                                </div>
                                <ModalPillInfo key={item.id} item={item} />
                            </div>
                        );
                    })
                }

                <div className="flex-row-center-center">
                    <Pagination
                        // showFirstButton
                        // showLastButton
                        count={Math.ceil(MAX_ITEM_COUNT / COUNT_PER_PAGE)}
                        page={currentPage}
                        onChange={changeCurrentPage}
                        color={"primary"}
                    >
                    </Pagination>
                </div>
            </div>
        )
    }

    return (
        <div id="container-search-pill" className="flex-column-center-center">
            <div className="search-pill flex-row-spaceEvenly-center">
                <Input value={pillName} onChange={(e) => { changePillName(e) }} id='input-search-pill' variant="soft" placeholder="약 이름을 검색해주세요" />
                <Button id="btn-search" onClick={searchPill}>검색하기</Button>
                <ModalInstance show={showModal} setShow={setShowModal} title={"검색 결과"} body={modalBody()} contentClassName={"modal-search-pill"} />
            </div>
        </div>
    )
}

export default ModalSearchPill;