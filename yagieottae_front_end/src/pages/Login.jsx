import { Input } from "@mui/joy"
import { Button } from "react-bootstrap"
import SmileFace from 'assets/imgs/smilingFace.png'
import "styles/login/login.scss"
import { Link, useNavigate } from "react-router-dom"
import { useDispatch, useSelector } from "react-redux"
import { useState } from "react"
import { setUser } from "redux/reducers/UserReducer"
import axiosInstance from "utils/AxiosInstance"
import { setCookie } from "utils/Cookies"

function Login()
{
    const [id, setID] = useState('');
    const [password, setPassword] = useState('');

    const backEndURL = useSelector(state => state.ServerURLReducer.backEndURL);
    const navigator = useNavigate();
    const dispatch = useDispatch();
    const setUserInfo = (userInfo) => dispatch(setUser(userInfo));

    const login = () =>
    {
        const data = {
            userId: id,
            password: password
        }

        let url = backEndURL + "/user/login";
        axiosInstance.post(url, data)
            .then((result) =>
            {
                const { jwtToken, userInfo } = result.data.body;
                setCookie('jwtAccessToken', jwtToken.accessToken, '/');
                setCookie('jwtRefreshToken', jwtToken.refreshToken, '/');
                setUserInfo(userInfo);
                alert(result.data.message);
                navigator('/');
            })
            .catch((error) =>
            {
                alert(error.message);
            });
    }

    return (
        <div id="container" className="flex-column-center-center">
            <div id="div-login" className="flex-column-center-center">
                <div className="div-greeting flex-row-center-center">
                    <span>약이어때에 오신걸 환영해요!</span>
                    <img src={SmileFace} alt="smile-face" className='img-smile-face' />
                </div>
                <div className="div-input-info">
                    <p>아이디</p>
                    <Input id="input-ID" fullWidth value={id} onChange={(e) => { setID(e.target.value) }} placeholder="ID를 입력해주세요" />
                </div>
                <div className="div-input-info">
                    <p>비밀번호</p>
                    <Input id="input-PW" fullWidth value={password} onChange={(e) => setPassword(e.target.value)} type="password" placeholder="PW를 입력해주세요" />
                </div>
                <div className="flex-row-center-center">
                    <Button id="btn-login" onClick={login}>로그인</Button>
                    <Link to="/user/signup">
                        <Button id="btn-signup" variant="success">
                            회원가입
                        </Button>
                    </Link>
                </div>
            </div>
        </div>
    )
}

export default Login