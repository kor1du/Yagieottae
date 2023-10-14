import { Input } from "@mui/joy";
import UseMyPageComponent from "./hooks/UseMyPageComponent";
import { Button } from "react-bootstrap";
import "components/myPage/styles/mypage.scss";

function MyPageComponent()
{
    const { user, userInfo, onChangeUserInfo, onChangeProfileImg, onClickSaveBtn } = UseMyPageComponent();

    return (
        <>
            {userInfo === undefined ?
                "" :
                <>
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
                            <Input type="file" id="profileImgPath" className="value" onChange={onChangeProfileImg} name="profileImgPath">이미지 업로드</Input>
                        </div>

                        <div className="div-profile flex-row-flexStart-center">
                            <label htmlFor="nickname" className="key">별명</label>
                            <Input id="nickname" className="value" value={userInfo.nickname} onChange={onChangeUserInfo} name="nickname" />
                        </div>
                        <div className="div-profile flex-row-flexStart-center">
                            <label htmlFor="phone" className="key">전화번호</label>
                            <Input id="phone" className="value" value={userInfo.phone} onChange={onChangeUserInfo} name="phone" />
                        </div>
                        <div className="div-profile flex-row-flexStart-center">
                            <label htmlFor="address" className="key">주소</label>
                            <Input id="address" className="value" value={userInfo.address} onChange={onChangeUserInfo} name="address" />
                        </div>
                        <div className="div-profile flex-row-flexStart-center">
                            <label htmlFor="addressDetail" className="key">상세주소</label>
                            <Input id="addressDetail" className="value" value={userInfo.addressDetail} onChange={onChangeUserInfo} name="addressDetail" />
                        </div>
                    </div>
                    <div className="div-btn-save flex-row-center-center">
                        <Button id="btn-save" onClick={onClickSaveBtn}>저장</Button>
                    </div>
                </>
            }
        </>
    )
}

export default MyPageComponent;