// action 정의
const GET_BACKEND_URL = "ServerURLReducer/GET_BACKEND_URL";

// action object 생성
export const getBackEndURL = () => ({ type: GET_BACKEND_URL, });

// initial state
const initialState = {
    backEndURL: "http://localhost:8080",
};

function ServerURLReducer(state = initialState, action)
{
    switch (action.type)
    {
        default: {
            return state;
        }
    }
}

export default ServerURLReducer;
