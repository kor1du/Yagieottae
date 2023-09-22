import { Modal } from "react-bootstrap";
import "styles/common/modal.scss";
import { useDispatch, useSelector } from "react-redux";
import { addModal, removeModal } from "redux/reducers/ModalReducer";

function ModalInstance({ show, setShow, title, body, contentClassName })
{
    const dispatch = useDispatch();
    const openedModals = useSelector(state => state.ModalReducer.openModals);
    const reducerAddModal = (modal) => dispatch(addModal(modal));
    const reducerRemoveModal = (modal) => dispatch(removeModal(modal));

    const handleShow = () =>
    {
        reducerAddModal(contentClassName);
        setShow(true);

        //Modal이 열리기 전 마지막 Modal의 dispay none처리
        const openedModalCount = openedModals.length;

        if (openedModalCount > 0)
        {
            const lastOpenedModalClassName = openedModals[openedModalCount - 1];
            document.querySelector('.' + lastOpenedModalClassName).style.display = "none";
        }
    }

    const handleClose = () =>
    {
        reducerRemoveModal(contentClassName);
        setShow(false);

        //Modal이 닫히기 전 마지막으로 열렸던 Modal의 display flex 처리
        const openedModalCount = openedModals.length;

        if (openedModalCount > 1)
        {
            const lastOpenedModalClassName = openedModals[openedModalCount - 2];
            document.querySelector('.' + lastOpenedModalClassName).style.display = "flex";
        }
    }

    return (
        <>
            <Modal show={show} onShow={handleShow} onHide={handleClose} centered scrollable contentClassName={contentClassName}>
                <Modal.Header closeButton>
                    <Modal.Title>{title}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {body}
                </Modal.Body>
            </Modal>
        </>
    );
}

export default ModalInstance;
