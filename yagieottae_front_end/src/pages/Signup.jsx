import { Input } from "@mui/joy";
import { Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import 'styles/signup/signup.scss';
import ThumbsUp from 'assets/imgs/thumbsUp.png';
import { useState } from "react";
import { useSelector } from "react-redux";
import axiosInstance from "utils/AxiosInstance";
import DaumPostcode from "react-daum-postcode";
import ModalInstance from "utils/ModalInstance";


function Signup()
{
    const navigate = useNavigate();
    const backEndURL = useSelector(state => state.ServerURLReducer.backEndURL);

    const [id, setID] = useState("");
    const [pw, setPW] = useState("");
    const [confirmPW, setConfirmPW] = useState("");
    const [nickname, setNickname] = useState("");
    const [address, setAddress] = useState("");
    const [addressDetail, setAddressDetail] = useState("");
    const [phone, setPhone] = useState("");
    const [daumPosModalshow, setDaumPostModalShow] = useState(false);

    const idCheck = () =>
    {
        if (id === "")
        {
            alert("아이디를 입력해주세요!");
            return false;
        }

        return true;
    }

    const passwordCheck = () =>
    {
        if (pw === "" || confirmPW === "")
        {
            alert("비밀번호를 입력해주세요!");
            return false;
        }

        else if (pw !== confirmPW)
        {
            alert("입력하신 비밀번호와 비밀번호 확인이 동일하지 않습니다.");
            return false;
        }

        return true;
    }

    const addressCheck = () =>
    {
        if (address === "")
        {
            alert("주소를 입력해주세요!");
            return false;
        }

        if (addressDetail === "")
        {
            alert("상세주소를 입력해주세요!");
            return false;
        }

        return true;
    }

    const phoneCheck = () =>
    {
        if (phone === "")
        {
            alert("전화번호를 입력해주세요!");
            return false;
        }

        const regPhone = /^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/;

        if (!regPhone.test(phone))
        {
            alert("올바른 전화번호 형식이 아닙니다.");
            return false;
        }

        return true;
    }

    const signup = () =>
    {
        if (!idCheck())
        {
            return false;
        }

        else if (!passwordCheck())
        {
            return false;
        }

        else if (!addressCheck())
        {
            return false;
        }

        else if (!phoneCheck())
        {
            return false;
        }

        const url = backEndURL + "/user/signup";
        const data =
        {
            'userId': id,
            'password': pw,
            'passwordConfirm': confirmPW,
            'nickname': nickname,
            'address': address,
            'addressDetail': addressDetail,
            'phone': phone,
        }

        axiosInstance.post(url, data)
            .then((result) =>
            {
                alert(result.data.message);
                navigate("/user/login");
            })
            .catch((error) =>
            {
                alert(error.message);
            });
    }

    const daumPostModalBody = () =>
    {
        return (
            <>
                <DaumPostcode onComplete={(data) => { setAddress(data.address); setDaumPostModalShow(false); }} />
            </>
        )
    }

    return (
        <div id="container-signup" className="flex-column-center-center container">
            <div className="flex-column-center-center">
                <div className="div-greeting flex-row-center-center">
                    <span>약이어때에 오신걸 환영합니다</span>
                    <img src={ThumbsUp} alt="thumbs-up" id="img-thumbs-up" />
                </div>
                <div className="div-input-info">
                    <p>아이디</p>
                    <Input id="input-ID" value={id} onChange={(e) => { setID(e.target.value) }} placeholder="아이디를 입력해주세요" />
                </div>
                <div className="div-input-info">
                    <p>비밀번호</p>
                    <Input id="input-PW" value={pw} onChange={(e) => { setPW(e.target.value) }} placeholder="비밀번호를 입력해주세요" type="password" />
                </div>
                <div className="div-input-info">
                    <p>비밀번호확인</p>
                    <Input id="input-confirm-PW" value={confirmPW} onChange={(e) => { setConfirmPW(e.target.value) }} placeholder="비밀번호를 입력해주세요" type="password" />
                </div>
                <div className="div-input-info">
                    <p>별명</p>
                    <Input id="input-nickname" value={nickname} onChange={(e) => { setNickname(e.target.value) }} placeholder="별명을 입력해주세요" />
                </div>
                <div className="div-input-info">
                    <p>주소</p>
                    <Input id="input-address" value={address} onClick={() => { setDaumPostModalShow(true); }} readOnly placeholder="주소를 입력해주세요" fullWidth />
                    <ModalInstance show={daumPosModalshow} setShow={setDaumPostModalShow} title={"주소 찾기"} body={daumPostModalBody()} />
                </div>
                <div className="div-input-info">
                    <p>상세 주소</p>
                    <Input id="input-address-detail" value={addressDetail} onChange={(e) => { setAddressDetail(e.target.value) }} placeholder="상세주소를 입력해주세요" />
                </div>
                <div className="div-input-info">
                    <p>전화번호</p>
                    <Input id="input-phone" value={phone} onChange={(e) => { setPhone(e.target.value) }} placeholder="전화번호를 입력해주세요"></Input>
                </div>
                <Button id="btn-signup" onClick={signup} variant="success">회원가입</Button>
            </div>
        </div>
    )
}

export default Signup;