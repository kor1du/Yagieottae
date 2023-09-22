const { Cookies } = require("react-cookie");

const cookies = new Cookies();

export const setCookie = (name, value, path, expires) =>
{
    cookies.set(name, value, {
        path: path,
        expires: new Date(expires),
    })
}

export const removeCookie = (name, path) =>
{
    cookies.remove(name, { path: path });
}

export const getCookie = (name) =>
{
    return cookies.get(name);
}