import { Input } from '@mui/joy';
import ThumbsUp from 'assets/imgs/thumbsUp.png';
import { Button } from 'react-bootstrap';
import ModalInstance from 'utils/ModalInstance';
import UseSignupComponent from './hooks/UseSignupComponent';
import DaumPostcode from "react-daum-postcode";
import 'components/signup/styles/signup.scss';

function SignupComponent()
{
    const { signupForm,
        showDaumPostModal,
        setShowDaumPostModal,
        onClickDaumPostModalOpenBtn,
        onChangeSignupForm,
        onClickSignupBtn,
        onCompleteDaumPostModal
    } = UseSignupComponent();
    const { id, password, passwordConfirm, nickname, address, addressDetail, phone } = signupForm;

    return (
        <>
            <div className="flex-column-center-center">
                <div className="div-greeting flex-row-center-center">
                    <span>약이어때에 오신걸 환영합니다</span>
                    <img src={ThumbsUp} alt="thumbs-up" id="img-thumbs-up" />
                </div>
                <div className="div-input-info">
                    <p>아이디</p>
                    <Input id="input-ID" value={id} onChange={onChangeSignupForm} name="id" placeholder="아이디를 입력해주세요" />
                </div>
                <div className="div-input-info">
                    <p>비밀번호</p>
                    <Input id="input-PW" value={password} onChange={onChangeSignupForm} name="password" placeholder="비밀번호를 입력해주세요" type="password" />
                </div>
                <div className="div-input-info">
                    <p>비밀번호확인</p>
                    <Input id="input-confirm-PW" value={passwordConfirm} onChange={onChangeSignupForm} name="passwordConfirm" placeholder="비밀번호를 입력해주세요" type="password" />
                </div>
                <div className="div-input-info">
                    <p>별명</p>
                    <Input id="input-nickname" value={nickname} onChange={onChangeSignupForm} name="nickname" placeholder="별명을 입력해주세요" />
                </div>
                <div className="div-input-info">
                    <p>주소</p>
                    <Input id="input-address" value={address} onClick={onClickDaumPostModalOpenBtn} readOnly placeholder="주소를 입력해주세요" fullWidth />
                    <ModalInstance
                        show={showDaumPostModal}
                        setShow={setShowDaumPostModal}
                        title={"주소 찾기"}
                        body={
                            <>
                                <DaumPostcode onComplete={onCompleteDaumPostModal} />
                            </>
                        }
                    />
                </div>
                <div className="div-input-info">
                    <p>상세 주소</p>
                    <Input id="input-address-detail" value={addressDetail} onChange={onChangeSignupForm} name="addressDetail" placeholder="상세주소를 입력해주세요" />
                </div>
                <div className="div-input-info">
                    <p>전화번호</p>
                    <Input id="input-phone" value={phone} onChange={onChangeSignupForm} name="phone" placeholder="전화번호를 입력해주세요"></Input>
                </div>
                <Button id="btn-signup" onClick={onClickSignupBtn} variant="success">회원가입</Button>
            </div>
        </>
    )
}

export default SignupComponent;