// action 정의
const GET_OPEN_MODALS = "ModalReducer/GET_OPEN_MODALS";
const ADD_MODAL = "ModalReducer/ADD_MODAL";
const REMOVE_MODAL = "ModalReducer/REMOVE_MODAL";

// action object 생성
export const getOpenModals = () => ({ type: GET_OPEN_MODALS });
export const addModal = modal => ({ type: ADD_MODAL, payload: modal });
export const removeModal = modal => ({ type: REMOVE_MODAL, payload: modal });

// initial state
const initialState = {
    openModals: [], //열려있는 모달들
};

function ModalReducer(state = initialState, action)
{
    switch (action.type)
    {
        case GET_OPEN_MODALS: {
            return state.openModals;
        }

        case ADD_MODAL: {
            const newModal = action.payload;
            return {
                ...state,
                openModals: [...state.openModals, newModal],
            };
        }

        case REMOVE_MODAL: {
            const removeModal = action.payload;
            const updatedModals = state.openModals.filter(modal => modal !== removeModal);
            return {
                ...state,
                openModals: updatedModals,
            }
        }

        default: {
            return state;
        }
    }
}

export default ModalReducer;
