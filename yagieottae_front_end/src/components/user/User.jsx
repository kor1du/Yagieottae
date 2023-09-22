import axiosInstance from "utils/AxiosInstance";
import { getCookie, removeCookie, setCookie } from "utils/Cookies";
import { persistor } from "index";

export const isLoggedIn = () =>
{
    return getCookie('jwtAccessToken') !== undefined && getCookie('jwtRefreshToken') !== undefined;
}

export const logout = (backEndURL) =>
{
    const url = backEndURL + "/user/logout";
    const data = {
        accessToken: getCookie('jwtAccessToken'),
    }
    axiosInstance.post(url, data)
        .then(async (result) =>
        {
            const { message } = result.data;
            removeCookie('jwtAccessToken', '/');
            removeCookie('jwtRefreshToken', '/');
            await persistor.purge();
            alert(message);
            window.location.href = "/";
        })
        .catch((error) =>
        {
            alert(error.message);
        });
}