import { Server_URL } from "constants/ServerURL";
import { useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { setUser } from "redux/reducers/UserReducer";
import axiosInstance from "utils/AxiosInstance";
import { setCookie } from "utils/Cookies";

function UseLogin()
{
    const [loginForm, setLoginForm] = useState({
        id: '',
        password: '',
    })

    const onChangeLoginForm = (e) =>
    {
        e.preventDefault();
        const { name, value } = e.target;

        setLoginForm({
            ...loginForm,
            [name]: value,
        })
    }

    const navigator = useNavigate();
    const dispatch = useDispatch();
    const setUserInfo = (userInfo) => dispatch(setUser(userInfo));

    const onClickLoginBtn = async () =>
    {
        const data = {
            userId: loginForm.id,
            password: loginForm.password
        }

        let url = Server_URL + "/user/login";

        try
        {
            const result = await axiosInstance.post(url, data);
            const { jwtToken, userInfo } = result.data.body;
            setCookie('jwtAccessToken', jwtToken.accessToken, '/');
            setCookie('jwtRefreshToken', jwtToken.refreshToken, '/');
            setUserInfo(userInfo);
            alert(result.data.message);
            navigator('/');
        } catch (error)
        {
            alert(error.message);
        }
    }

    return { loginForm, onChangeLoginForm, onClickLoginBtn };
}

export default UseLogin;