import ModalInstance from "utils/ModalInstance";
import UseAlramList from "./hooks/UseAlramList";
import { Button } from "react-bootstrap";
import 'components/alramList/styles/alramList.scss';
import AlramListItem from "./AlramListItem";
import ModalPillInfo from "components/modalPillInfo/ModalPillInfo";

function AlramList()
{
    const { showModal, setShowModal, todayAlramList, alldayAlramList, onClickShowAllAlramListBtn } = UseAlramList();

    return (
        <div id="container-alram-list">
            <div className='flex-row-flexStart-center'>
                <Button onClick={onClickShowAllAlramListBtn}>알람 전체 보기</Button>
                <ModalInstance
                    show={showModal}
                    setShow={setShowModal}
                    title={"알람 전체 보기"}
                    body={
                        <>
                            {
                                alldayAlramList.map((alram) =>
                                {
                                    return (<AlramListItem alram={alram} />)
                                })
                            }
                        </>
                    }
                    contentClassName={"modal-view-all-alrams"} />
            </div>
            <p className="today-pill-list" id="text-today-durg-list">오늘 복용해야할 약 리스트</p>

            {
                todayAlramList.map((alram) =>
                {
                    return (<AlramListItem key={alram.id} alram={alram} />)
                })
            }
        </div>
    )
}

export default AlramList;