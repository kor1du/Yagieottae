import { Server_URL } from "constants/ServerURL";
import axiosInstance from "./AxiosInstance";

const url = Server_URL + "/alram/getAlrams";

export const FetchAlramList = async (getToday, setAlramList) =>
{
    const params =
    {
        getToday: getToday,
    }

    try
    {
        const result = await axiosInstance.get(url, { params });

        setAlramList(result.data.body.alrams);
    } catch (error)
    {
        alert(error.message);
    }
}