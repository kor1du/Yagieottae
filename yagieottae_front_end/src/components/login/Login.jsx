import { Input } from "@mui/joy"
import { Button } from "react-bootstrap"
import SmileFace from 'assets/imgs/smilingFace.png'
import "components/login/styles/login.scss"
import { Link } from "react-router-dom"
import UseLogin from "./hooks/UseLogin"

function Login()
{
    const { loginForm, onChangeLoginForm, onClickLoginBtn } = UseLogin();
    const { id, password } = loginForm;

    return (
        <div id="div-login" className="flex-column-center-center">
            <div className="div-greeting flex-row-center-center">
                <span>약이어때에 오신걸 환영해요!</span>
                <img src={SmileFace} alt="smile-face" className='img-smile-face' />
            </div>
            <div className="div-input-info">
                <p>아이디</p>
                <Input id="input-ID" fullWidth value={id} onChange={onChangeLoginForm} name="id" placeholder="ID를 입력해주세요" />
            </div>
            <div className="div-input-info">
                <p>비밀번호</p>
                <Input id="input-PW" fullWidth value={password} onChange={onChangeLoginForm} name="password" type="password" placeholder="PW를 입력해주세요" />
            </div>
            <div className="flex-row-center-center">
                <Button id="btn-login" onClick={onClickLoginBtn}>로그인</Button>
                <Link to="/user/signup">
                    <Button id="btn-signup" variant="success">
                        회원가입
                    </Button>
                </Link>
            </div>
        </div>
    )
}

export default Login