/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import Drugs from 'assets/imgs/drugs.png'
import { Button } from "react-bootstrap";
import axiosInstance from "utils/AxiosInstance";

import ModalPillInfo from "components/common/ModalPillInfo";
import ModalInstance from "utils/ModalInstance";

function AlramList()
{
    const backEndURL = useSelector(state => state.ServerURLReducer.backEndURL);
    const user = useSelector(state => state.UserReducer.user);
    const [todayPillList, setTodayPillList] = useState([]); //오늘 날짜의 약 목록
    const [pillListAll, setPillListAll] = useState([]); //전체 약 목록
    const [showModal, setShowModal] = useState(false);

    //알람 조회
    //getToday = True : 오늘 날짜의 알람목록 조회, getToday = False : 전체 알람목록 조회
    const getAlrams = (getToday, isShowModal) =>
    {
        const params = {
            userId: user.userId,
            getToday: getToday
        }

        const url = backEndURL + '/alram/findAlrams';

        axiosInstance.get(url, { params })
            .then((result) =>
            {
                const data = result.data.body.alrams;

                if (getToday)
                {
                    setTodayPillList(data);
                }
                else
                {
                    setPillListAll(data);
                }

                if (isShowModal)
                {
                    setShowModal(true);
                }
            }).catch((error) =>
            {
                alert(error.message);
            });
    }

    const modalBody = () =>
    {
        return (
            <>
                {
                    pillListAll.map((item) =>
                    {
                        return (
                            <div className='flex-row-spaceEvenly-center pill-list' key={item.id}>
                                <img src={item.pill.imagePath == null ? { Drugs } : item.pill.imagePath} alt='drugs' className='img-drug' onError={(e) => { e.target.src = Drugs; }} />
                                <div className="div-item-name">
                                    <p className='text-item-name'> {item.pill.itemName} </p>
                                </div>
                                <ModalPillInfo key={item.id} existingAlram={item} item={item.pill} />
                            </div>
                        );
                    })
                }
            </>
        )
    }

    useEffect(() =>
    {
        getAlrams(true, false);
    }, []);



    return (
        <div id="container-alram-list">
            <div className="list-box">
                <div className='flex-row-flexStart-center'>
                    <Button onClick={() => { getAlrams(false, true) }}>알람 전체 보기</Button>
                    <ModalInstance show={showModal} setShow={setShowModal} title={"알람 전체 보기"} body={modalBody()} contentClassName={"modal-view-all-alrams"} />
                </div>
                <p className="today-pill-list" id="text-today-durg-list">오늘의 약 리스트</p>

                {todayPillList !== undefined && todayPillList.length > 0 ?
                    todayPillList.map((item) =>
                    {
                        return (
                            <div className="today-pill flex-column-center-flexStart" key={item.pill.itemSeq}>
                                <div className="div-pill-info flex-row-center-center">
                                    <img src={item.pill.imagePath === null ? Drugs : item.pill.imagePath} className="img-pills" alt="img-pills" />
                                    <div className="div-pill-name">
                                        <p className="text-pill-name">{item.pill.itemName}</p>
                                    </div>
                                    <ModalPillInfo item={item.pill} existingAlram={item} />
                                </div>
                                <div className='div-pill-description'>
                                    <Button variant='success' className="btn-alram-description">알람시간 : {item.alramTime}</Button>
                                    <Button variant='success' className="btn-alram-description">복용시간 : {item.beforeMeal ? "식전" : "식후"} {item.dosingTime}분</Button>
                                </div>
                            </div>
                        )
                    })
                    :
                    ""
                }
            </div>
        </div>
    )
}

export default AlramList;