import { Server_URL } from "constants/ServerURL";
import { getCookie, removeCookie } from "utils/Cookies";
import axiosInstance from "utils/AxiosInstance";
import { persistor } from "index";

export const Logout = () =>
{
    const url = Server_URL + "/user/logout";
    const params = {
        accessToken: getCookie('jwtAccessToken'),
    }
    axiosInstance.post(url, null, { params })
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