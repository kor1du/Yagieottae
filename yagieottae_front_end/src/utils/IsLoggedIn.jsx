import { getCookie } from "utils/Cookies";

export const IsLoggedIn = () =>
{
    return getCookie('jwtAccessToken') !== undefined && getCookie('jwtRefreshToken') !== undefined;
}