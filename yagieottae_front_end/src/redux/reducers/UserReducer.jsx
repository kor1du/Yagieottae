import { PURGE } from "redux-persist";

// action 정의
const SET_USER = "UserReducer/SET_USER";
const SET_MY_PAGE_CONFIRM = "UserReducer/SET_MY_PAGE_CONFIRM";

// action object 생성
export const setUser = user => ({ type: SET_USER, payload: user });
export const setMyPageConfirm = myPageConfirm => ({ type: SET_MY_PAGE_CONFIRM, payload: myPageConfirm });

//로그아웃
const initState = () =>
{
    initialState.user.userId = '';
    initialState.user.nickname = '';
    initialState.user.address = '';
    initialState.user.addressDetail = '';
    initialState.user.phone = '';
    initialState.user.profileImgPath = '';
}

// initial state
const initialState = {
    user:
    {
        userId: '',
        nickname: '',
        address: '',
        addressDetail: '',
        phone: '',
        profileImgPath: '',
    }
};

function UserReducer(state = initialState, action)
{
    switch (action.type)
    {
        case SET_USER:
            return {
                ...state,
                user: {
                    ...state.user,
                    userId: action.payload.userId,
                    nickname: action.payload.nickname,
                    address: action.payload.address,
                    addressDetail: action.payload.addressDetail,
                    phone: action.payload.phone,
                    profileImgPath: action.payload.profileImgPath,
                },
            }
        case SET_MY_PAGE_CONFIRM:
            return {
                user: {
                    ...state,
                    myPageConfirm: action.payload,
                }
            }
        case PURGE:
            {
                initState();
                return state;
            }

        default: {
            return state;
        }
    }
}

export default UserReducer;