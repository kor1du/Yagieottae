import axios from 'axios';
import { getCookie, removeCookie, setCookie } from './Cookies';

const backEndURL = "http://localhost:8080";
const refreshURL = backEndURL + "/user/reissue";

const axiosInstance = axios.create({
    baseURL: backEndURL,
});

axiosInstance.interceptors.request.use(
    (config) =>
    {
        // config.headers['Content-Type'] = 'application/json;charset=utf-8';

        //HTTP Authorization 요청 헤더에 jwt-token을 넣음
        //서버에서 토큰을 확인하고 검증한 후 해당 API에 요청
        let token;
        if (config.url === refreshURL)
        {
            token = getCookie('jwtRefreshToken');
        }
        else
        {
            token = getCookie('jwtAccessToken');
        }

        if (token)
        {
            config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
    },
    (error) =>
    {
        return Promise.reject(error);
    }
);

axiosInstance.interceptors.response.use
    (
        (response) =>
        {
            return response;
        },
        async (error) =>
        {
            const { config } = error;

            if (error.response.status === 401)
            {
                const accessToken = await getNewToken();

                if (accessToken)
                {
                    config.headers.Authorization = accessToken;
                }
            }

            else if (error.response.status === 403)
            {
                error.message = "서버와의 통신중 오류가 발생하였습니다.";
                return Promise.reject(error);
            }

            else
            {
                if (error.response.data.message === undefined)
                {
                    error.message = '잘못된 접근입니다.';
                }
                else
                {
                    error.message = error.response.data.message;
                }
                return Promise.reject(error);
            }

            return axiosInstance(config);
        }
    )

const getNewToken = async () =>
{
    try
    {
        const refreshToken = getCookie('jwtRefreshToken');
        return axios.post(refreshURL, null, { params: { "refreshToken": refreshToken } })
            .then((result) =>
            {
                const { jwtTokenDto } = result.data.body;
                setCookie('jwtAccessToken', jwtTokenDto.accessToken, '/');
                setCookie('jwtRefreshToken', jwtTokenDto.refreshToken, '/');
                return true;
            })
            .catch((error) =>
            {
                removeCookie('jwtAccessToken');
                removeCookie('jwtRefreshToken');
                error.message = "로그인이 필요한 서비스입니다.";
                window.location.href = '/user/login';
                return Promise.reject(error);
            });
    } catch (error)
    {
        alert("토큰 재발급중 오류가 발생하였습니다.");
    }
}

export default axiosInstance;