import { useState } from 'react';
import { Button, Form } from 'react-bootstrap';
import Input from '@mui/joy/Input';
import { useSelector } from 'react-redux';
import axiosInstance from 'utils/AxiosInstance';
import ModalInstance from 'utils/ModalInstance';
import "components/modalSetAlram/styles/modalSetAlram.scss";
import UseModalSetAlram from './hooks/UseModalSetAlram';
import { IsLoggedIn } from 'utils/IsLoggedIn';

function ModalSetAlram({ pill, existingAlram })
{
	const { alram, showModal, setShowModal, onChangeAlram, onChangeAlramTime, onChangeDays, onClickSaveBtn, onClickDeleteBtn, onClickModalOpenBtn } = UseModalSetAlram(pill, existingAlram);

	const refDays = [['월'], ['화'], ['수'], ['목'], ['금'], ['토'], ['일']]; //일주일

	const isNewAlram = alram.id === 0 ? true : false;

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

	return (
		<>
			{IsLoggedIn() ? <Button variant='primary' onClick={onClickModalOpenBtn}>알람 {isNewAlram ? "추가하기" : "수정하기"}</Button > : ""
			}
			<ModalInstance
				show={showModal}
				setShow={setShowModal}
				title={"알람설정"}
				body={<>
					<h4>알람 시간</h4>
					<div className='flex-row-center-center div-item'>
						<Form.Select id="set-alram-hour" value={alram.alramTime.hour} onChange={onChangeAlramTime} name="hour">{setTimeOptions('hour')}</Form.Select>
						<span className='colon'>:</span>
						<Form.Select id="set-alram-minute" value={alram.alramTime.minute} onChange={onChangeAlramTime} name="minute">{setTimeOptions('minute')}</Form.Select>
					</div>
					<br />
					<h4>요일 반복</h4>
					<div className='week flex-row-spaceEvenly-center div-item'>
						{
							refDays.map((item) =>
							{
								return (
									<div className={`checkbox ${alram.days.includes(item[0]) ? "active" : ""}`} onClick={onChangeDays} key={item[0]}>
										{item[0]}
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
						<h4>복용시간</h4>
						<div className='time-to-take-medicine flex-row-center-center'>
							<Form.Select id='select-taking-time' value={alram.beforeMeal} onChange={onChangeAlram} name='beforeMeal'>
								<option value={true}>식전</option>
								<option value={false}>식후</option>
							</Form.Select>
							<Input variant='soft' id='input-dosing-time' value={alram.dosingTime} onChange={onChangeAlram} name='dosingTime' />
							<label htmlFor='input-dosing-time' id='label-dosing-time'>분</label>
						</div>
					</div>
					<br />
					<div className='flex-row-center-center'>
						<Button variant='success' className='btn-set-alram' onClick={onClickSaveBtn}>{isNewAlram ? "저장" : "수정"}</Button>
						{isNewAlram ? "" : <Button variant="danger" className='btn-set-alram' onClick={onClickDeleteBtn}>삭제</Button>}
					</div>
				</>}
				contentClassName={"modal-set-alram"} />
		</>
	);
}

export default ModalSetAlram;
