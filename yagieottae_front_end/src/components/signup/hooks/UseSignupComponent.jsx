import { Server_URL } from "constants/ServerURL";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "utils/AxiosInstance";

function UseSignupComponent()
{
    const navigate = useNavigate();

    const [signupForm, setSignupForm] = useState({
        id: '',
        password: '',
        passwordConfirm: '',
        nickname: '',
        address: '',
        addressDetail: '',
        phone: '',
    })

    const [showDaumPostModal, setShowDaumPostModal] = useState(false);

    const checkSignupFormValidation = () =>
    {
        const { id, password, passwordConfirm, address, addressDetail, phone } = signupForm;

        if (id === "")
        {
            alert("아이디를 입력해주세요!");
            return false;
        }

        if (password === "" || passwordConfirm === "")
        {
            alert("비밀번호를 입력해주세요!");
            return false;
        }

        if (password !== passwordConfirm)
        {
            alert("입력하신 비밀번호와 비밀번호 확인이 동일하지 않습니다.");
            return false;
        }

        if (address === "")
        {
            alert("주소를 입력해주세요!");
            return false;
        }

        if (addressDetail === "")
        {
            alert("상세주소를 입력해주세요!");
            return false;
        }

        if (phone === "")
        {
            alert("전화번호를 입력해주세요!");
            return false;
        }

        const regPhone = /^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/;

        if (!regPhone.test(phone))
        {
            alert("올바른 전화번호 형식이 아닙니다.");
            return false;
        }

        return true;
    }

    const onClickSignupBtn = async () =>
    {
        if (!checkSignupFormValidation())
        {
            return false;
        }

        const url = Server_URL + "/user/signup";

        const data =
        {
            'userId': signupForm.id,
            'password': signupForm.password,
            'passwordConfirm': signupForm.passwordConfirm,
            'nickname': signupForm.nickname,
            'address': signupForm.address,
            'addressDetail': signupForm.addressDetail,
            'phone': signupForm.phone,
        }

        try
        {
            const result = await axiosInstance.post(url, data);
            alert(result.data.message);
            navigate("/user/login");
        } catch (error)
        {
            alert(error.message);
        }
    }

    const onClickDaumPostModalOpenBtn = () =>
    {
        setShowDaumPostModal(true);
    }

    const onChangeSignupForm = (e) =>
    {
        e.preventDefault();
        const { name, value } = e.target;

        setSignupForm({
            ...signupForm,
            [name]: value,
        });
    }

    const onCompleteDaumPostModal = (data) =>
    {
        setSignupForm({
            ...signupForm,
            address: data.address,
        });
        setShowDaumPostModal(false);
    }

    return (
        {
            signupForm,
            showDaumPostModal,
            setShowDaumPostModal,
            onClickDaumPostModalOpenBtn,
            onChangeSignupForm,
            onClickSignupBtn,
            onCompleteDaumPostModal
        }
    )
}

export default UseSignupComponent;