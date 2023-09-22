import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getCookie } from "utils/Cookies";
import { Input } from "@mui/joy";
import { Button } from "react-bootstrap";
import { FaLock } from "react-icons/fa";
import 'styles/mypage/mypage.scss';
import axiosInstance from "utils/AxiosInstance";
import { setUser } from "redux/reducers/UserReducer";

function MyPage()
{
    const dispatch = useDispatch();
    const user = useSelector(state => state.UserReducer.user);
    const updateUser = (userInfo) => dispatch(setUser(userInfo));
    const backEndURL = useSelector(state => state.ServerURLReducer.backEndURL);
    const [userInfo, setUserInfo] = useState(user);

    const changeProfileImg = (e) =>
    {
        e.preventDefault();
        const file = e.target.files[0];
        const formData = new FormData();
        formData.append("file", file);

        const url = backEndURL + "/upload/";
        axiosInstance.post(url, formData, {
            headers: {
                "Content-Type": "multipart/form-data"
            }
        }).then((result) =>
        {
            const { body } = result.data;
            setUserInfo({
                ...userInfo,
                profileImgPath: body.fileUrl,
            })
        }).catch((error) =>
        {
            alert(error.message);
        })
    }

    const save = (e) =>
    {
        e.preventDefault();

        const url = backEndURL + "/user/updateInfo";

        setUserInfo();
        axiosInstance.put(url, userInfo)
            .then((result) =>
            {
                const { message, body } = result.data;
                updateUser(body.userInfo);
                alert(message);
                window.location.reload();
            }).catch((error) =>
            {
                alert(error.message);
            })
    }

    const changeUserInfo = (e, field) =>
    {
        e.preventDefault();
        setUserInfo({
            ...userInfo,
            [field]: e.target.value,
        })
    }

    return (
        <>
            {userInfo === undefined ?
                "" :
                <div id='container-mypage' className="container">
                    <div className="flex-column-center-center">
                        <div className="div-profile-img flex-row-center-center">
                            <img src={userInfo.profileImgPath} alt="profileImgPath" className="profile-img" />
                        </div>
                        <p>{user.nickname}님, 환영해요.</p>
                    </div>
                    <div className="div-account-manage">
                        <p>계정 관리</p>
                        <div className="div-profile flex-row-flexStart-center">
                            <label htmlFor="profileImgPath" className="key">프로필 이미지</label>
                            <Input type="file" id="profileImgPath" className="value" onChange={(e) => changeProfileImg(e)}>이미지 업로드</Input>
                        </div>

                        <div className="div-profile flex-row-flexStart-center">
                            <label htmlFor="nickname" className="key">별명</label>
                            <Input id="nickname" className="value" value={userInfo.nickname} onChange={(e) => changeUserInfo(e, "nickname")} />
                        </div>
                        <div className="div-profile flex-row-flexStart-center">
                            <label htmlFor="phone" className="key">전화번호</label>
                            <Input id="phone" className="value" value={userInfo.phone} onChange={(e) => changeUserInfo(e, "phone")} />
                        </div>
                        <div className="div-profile flex-row-flexStart-center">
                            <label htmlFor="address" className="key">주소</label>
                            <Input id="address" className="value" value={userInfo.address} onChange={(e) => changeUserInfo(e, "address")} />
                        </div>
                        <div className="div-profile flex-row-flexStart-center">
                            <label htmlFor="addressDetail" className="key">상세주소</label>
                            <Input id="addressDetail" className="value" value={userInfo.addressDetail} onChange={(e) => changeUserInfo(e, "addressDetail")} />
                        </div>
                    </div>
                    <div className="div-btn-save flex-row-center-center">
                        <Button id="btn-save" onClick={(e) => { save(e) }}>저장</Button>
                    </div>
                </div >}
        </>
    )
}

export default MyPage;