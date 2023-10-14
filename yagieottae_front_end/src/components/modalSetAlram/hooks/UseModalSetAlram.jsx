import { Server_URL } from "constants/ServerURL";
import { useEffect, useState } from "react";
import { json } from "react-router-dom";
import axiosInstance from "utils/AxiosInstance";

export default function UseModalSetAlram(pill, existingAlram)
{
    const [alram, setAlram] = useState(
        {
            id: 0,
            alramTime: {
                hour: "00",
                minute: "00",
            },
            days: [],
            beforeMeal: true,
            dosingTime: 0,
            pill: pill,
        }
    )

    const [showModal, setShowModal] = useState(false);

    useEffect(() =>
    {
        if (existingAlram)
        {
            setAlram({
                ...existingAlram,
                alramTime: {
                    hour: existingAlram.alramTime.split(':')[0],
                    minute: existingAlram.alramTime.split(':')[1]
                },
                days: existingAlram.days.split(',')
            });
        }
    }, []);

    const onClickModalOpenBtn = () =>
    {
        setShowModal(true);
    }

    //알람 시간의 시, 분 변경
    //type = hour : 시(hour)에 해당하는 값 변경
    //type != hour : 분(minute)에 해당하는 값 변경
    const onChangeAlramTime = (e) =>
    {
        e.preventDefault();
        const { name, value } = e.target;
        setAlram({
            ...alram,
            alramTime: {
                ...alram.alramTime,
                [name]: value,
            },
        });
    }

    const onChangeAlram = (e) =>
    {
        let { name, value } = e.target;

        if (name === "beforeMeal")
        {
            value = JSON.parse(value);
        }

        e.preventDefault();
        setAlram(prevAlram => ({
            ...prevAlram,
            [name]: value,
        }));
    }

    //요일 반복 변경
    const onChangeDays = (e) =>
    {
        e.preventDefault();
        const { innerText, classList } = e.target;

        if (classList.contains('active')) //해당 요일이 선택된 상태면 아래 조건문 실행
        {
            setAlram(prevAlram => ({
                ...prevAlram,
                days: prevAlram.days.filter((day) => day !== innerText),
            }))
        }

        else //해당 요일이 선택되지 않은 상태면 아래 조건문 실행
        {
            setAlram(prevAlram => ({
                ...prevAlram,
                days: [...prevAlram.days, innerText]
            }))
        }

        classList.toggle('active'); //선택된 요일의 classList에서 'active' 클래스를 추가 / 삭제
    };

    // 요일을 정렬하는 비교 함수
    const compareDays = (a, b) =>
    {
        const daysOrder = ['월', '화', '수', '목', '금', '토', '일'];
        return daysOrder.indexOf(a) - daysOrder.indexOf(b);
    };

    //알람 저장
    const onClickSaveBtn = () =>
    {
        if (alram.alramTime === 0)
        {
            alert("알람 시간을 설정해주세요.");
            return false;
        }
        if (alram.days.length === 0)
        {
            alert("요일을 선택해주세요.");
            return false;
        }
        if (alram.dosingTime.length === 0)
        {
            alert("복용시간을 입력해주세요");
            return false;
        }
        if (alram.dosingTime > 59)
        {
            alert("복용시간은 60분 내외로만 설정 가능합니다.");
            return false;
        }

        const data =
        {
            id: alram.id,
            alramTime: alram.alramTime.hour + ":" + alram.alramTime.minute,
            days: alram.days.sort(compareDays).join(','),
            beforeMeal: alram.beforeMeal,
            dosingTime: alram.dosingTime,
            userId: alram.userId,
            pillId: alram.pill.id,
        }

        const url = `${Server_URL}/alram/save`;

        axiosInstance.post(url, data)
            .then((result) =>
            {
                alert(result.data.message);
                window.location.reload();
            })
            .catch((error) =>
            {
                alert(error.message);
            });
    }

    //알람 삭제
    const onClickDeleteBtn = () =>
    {
        if (window.confirm("알람을 삭제하시겠습니까?"))
        {
            const url = Server_URL + "/alram/deleteAlram"
            const params = {
                alramId: alram.id
            }
            axiosInstance.delete(url, { params: params })
                .then((result) =>
                {
                    alert(result.data.message);
                    window.location.reload();
                }).catch((error) =>
                {
                    alert(error.message);
                })
        }
    }

    return ({ alram, showModal, setShowModal, onChangeAlram, onChangeAlramTime, onChangeDays, onClickSaveBtn, onClickDeleteBtn, onClickModalOpenBtn })
}