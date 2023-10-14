import { Server_URL } from "constants/ServerURL";
import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { setUser } from "redux/reducers/UserReducer";
import axiosInstance from "utils/AxiosInstance";

function UseMyPageComponent()
{
    const dispatch = useDispatch();
    const user = useSelector(state => state.UserReducer.user);
    const updateUser = (userInfo) => dispatch(setUser(userInfo));
    const [userInfo, setUserInfo] = useState(user);

    const onChangeUserInfo = (e) =>
    {
        e.preventDefault();
        const { name, value } = e.target;

        setUserInfo({
            ...userInfo,
            [name]: value,
        })
    }

    const onChangeProfileImg = async (e) =>
    {
        e.preventDefault();
        const file = e.target.files[0];
        const formData = new FormData();
        formData.append("file", file);

        const url = Server_URL + "/upload/";
        const headers = {
            "Content-Type": "multipart/form-data"
        }

        try
        {
            const result = await axiosInstance.post(url, formData, { headers });

            const { body } = result.data;
            setUserInfo({
                ...userInfo,
                profileImgPath: body.fileUrl,
            })
        } catch (error)
        {
            alert(error.message);
        }
    }

    const onClickSaveBtn = async () =>
    {
        const url = Server_URL + "/user/updateInfo";

        try
        {
            const result = await axiosInstance.put(url, userInfo);
            const { message, body } = result.data;
            updateUser(body.userInfo);
            alert(message);
            window.location.reload();
        } catch (error)
        {
            alert(error.message);
        }
    }

    return (
        { user, userInfo, onChangeUserInfo, onChangeProfileImg, onClickSaveBtn }
    )
}

export default UseMyPageComponent;