import { useState } from 'react';
import { Button, Form } from 'react-bootstrap';
import Input from '@mui/joy/Input';
import { useSelector } from 'react-redux';
import axiosInstance from 'utils/AxiosInstance';
import ModalInstance from 'utils/ModalInstance';
import { isLoggedIn } from 'components/user/User';
import "styles/common/modalSetAlram.scss";

function ModalSetAlram({ pill, existingAlram })
{
	const refDays = [['1', '월'], ['2', '화'], ['3', '수'], ['4', '목'], ['5', '금'], ['6', '토'], ['7', '일']]; //일주일
	const backEndURL = useSelector(state => state.ServerURLReducer.backEndURL);
	const user = useSelector(state => state.UserReducer.user);

	const [alram, setAlram] = useState(
		existingAlram === undefined ?
			{
				id: 0,
				alramTime: {
					hour: "00",
					minute: "00",
				},
				days: [],
				beforeMeal: true,
				dosingTime: 0,
				userId: user.userId,
				pillId: pill.id,
			}
			:
			{
				...existingAlram,
				alramTime: {
					hour: existingAlram.alramTime.split(':')[0],
					minute: existingAlram.alramTime.split(':')[1]
				},
				days: existingAlram.days.split(','),
				userId: user.userId,
				pillId: pill.id,
			}
	)

	const [showModal, setShowModal] = useState(false);

	//알람 시간 select문의 options 생성
	//type = hour : 0 ~ 23 값을 가지는 option 생성
	//type != hour : 0 ~ 59 값을 가지는 option 생성
	const setTimeOptions = (type) =>
	{
		const options = [];
		for (let i = 0; i < (type === 'hour' ? 24 : 60); i++)
		{
			const time = i.toString().padStart(2, "0"); //ex) 한자리 수의 숫자의 표시형식을 두자릿수로 변경 (예시)0 -> 00, 1 -> 01 )
			options.push(<option key={i} value={time}>{time}</option>);
		}
		return options;
	}

	//알람 시간의 시, 분 변경
	//type = hour : 시(hour)에 해당하는 값 변경
	//type != hour : 분(minute)에 해당하는 값 변경
	const changeTime = (e, type) =>
	{
		e.preventDefault();
		setAlram((prevAlram) => ({
			...prevAlram,
			alramTime: {
				...prevAlram.alramTime,
				[type]: e.target.value,
			}
		}));
	}

	//복용시간의 식전 / 식후 변경
	const changeTakingTime = (e) =>
	{
		e.preventDefault();
		setAlram(prevAlram => ({
			...prevAlram,
			beforeMeal: e.target.value === 'beforeMeal' ? true : false,
		}));
	}

	//복용시간의 시간 변경
	const changeDosingTime = (e) =>
	{
		e.preventDefault();
		setAlram(prevAlram => ({
			...prevAlram,
			dosingTime: e.target.value,
		}));
	}

	//요일 반복 변경
	const changeDays = (e, index) =>
	{
		e.preventDefault();
		const dayClassList = document.getElementById('days' + index).classList; //선택된 요일의 classList

		if (dayClassList.contains('active')) //해당 요일이 선택된 상태면 아래 조건문 실행
		{
			setAlram(prevAlram => ({
				...prevAlram,
				days: prevAlram.days.filter((day) => day !== index),
			}))
		}

		else //해당 요일이 선택되지 않은 상태면 아래 조건문 실행
		{
			setAlram(prevAlram => ({
				...prevAlram,
				days: [...prevAlram.days, index]
			}))
		}

		dayClassList.toggle('active'); //선택된 요일의 classList에서 'active' 클래스를 추가 / 삭제
	};

	//알람 저장
	const saveAlram = () =>
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
			days: alram.days.sort().join(','),
			beforeMeal: alram.beforeMeal,
			dosingTime: alram.dosingTime,
			userId: alram.userId,
			pillId: alram.pillId,
		}

		const url = `${backEndURL}/alram/save`;

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
	const deleteAlram = () =>
	{
		if (window.confirm("알람을 삭제하시겠습니까?"))
		{
			const url = backEndURL + "/alram/deleteAlram"
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

	const modalTitle = () =>
	{
		return (
			<>
				<div>
					알람 설정
				</div>
			</>
		)
	}

	const modalBody = () =>
	{
		return (
			<>
				<h4>알람 시간</h4>
				<div className='flex-row-center-center div-item'>
					<Form.Select id="set-alram-hour" value={alram.alramTime.hour} onChange={(e) => changeTime(e, "hour")}>{setTimeOptions('hour')}</Form.Select>
					<span className='colon'>:</span>
					<Form.Select id="set-alram-minute" value={alram.alramTime.minute} onChange={(e) => changeTime(e, "minute")}>{setTimeOptions('minute')}</Form.Select>
				</div>
				<br />
				<h4>요일 반복</h4>
				<div className='week flex-row-spaceEvenly-center div-item'>
					{
						refDays.map((item) =>
						{
							return (
								<div className={`checkbox ${alram.days.includes(item[0]) ? "active" : ""}`} id={'days' + item[0]} value={item[0]} onClick={(e) => changeDays(e, item[0])} key={item[0]}>
									<input type='checkbox' id={'checkbox-days-' + item[0]} />
									<label htmlFor={'checkbox-days-' + item[0]}>{item[1]}</label>
								</div>
							)
						})}
				</div>
				<br />
				<h4>약</h4>
				<div className='flex-row-spaceEvenly-center'>
					<Input fullWidth color='neutral' type='text' placeholder='약을 입력해주세요' readOnly value={pill.itemName} id='alram-pill-name' />
				</div>
				<br />
				<div>
					<h4>복용시간 알림</h4>
					<div className='time-to-take-medicine flex-row-center-center'>
						<Form.Select id='select-taking-time' value={alram.beforeMeal ? 'beforeMeal' : 'afterMeal'} onChange={(e) => changeTakingTime(e)}>
							<option value='beforeMeal'>식전</option>
							<option value='afterMeal'>식후</option>
						</Form.Select>
						<Input variant='soft' id='input-dosing-time' value={alram.dosingTime} onChange={(e) => changeDosingTime(e)} />
						<label htmlFor='input-dosing-time' id='label-dosing-time'>분</label>
					</div>
				</div>
				<br />
				<div className='flex-row-center-center'>
					<Button variant='success' className='btn-set-alram' onClick={saveAlram}>{existingAlram === undefined ? "저장" : "수정"}</Button>
					{existingAlram === undefined ? "" : <Button variant="danger" className='btn-set-alram' onClick={deleteAlram}>삭제</Button>}
				</div>
			</>
		)
	}

	return (
		<>
			{isLoggedIn() ? <Button variant='primary' onClick={() => { setShowModal(true) }}>알람 {existingAlram === undefined ? "추가하기" : "수정하기"}</Button > : ""}
			<ModalInstance show={showModal} setShow={setShowModal} title={modalTitle()} body={modalBody()} contentClassName={"modal-set-alram"} />
		</>
	);
}

export default ModalSetAlram;
