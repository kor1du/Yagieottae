import Drugs from "assets/imgs/drugs.png";
import ModalPillInfo from "components/modalPillInfo/ModalPillInfo";
import ModalSetAlram from "components/modalSetAlram/ModalSetAlarm";
import { Button } from "react-bootstrap";

function AlramListItem({ alram })
{
    return (
        <>
            <div className="today-pill flex-column-center-flexStart" key={alram.pill.itemSeq}>
                <div className="div-pill-info flex-row-center-center">
                    <img src={alram.pill.imagePath === null ? Drugs : alram.pill.imagePath} className="img-pills" alt="img-pills" />
                    <div className="div-pill-name flex-row-spaceBetween-center">
                        <p className="text-pill-name">{alram.pill.itemName}</p>
                        <ModalPillInfo pill={alram.pill} existingAlram={alram} />
                        <ModalSetAlram pill={alram.pill} existingAlram={alram} />
                    </div>
                </div>
                <div className='div-pill-description'>
                    <Button variant='success' className="btn-alram-description">알람시간 : {alram.alramTime}</Button>
                    <Button variant='success' className="btn-alram-description">요일반복 : {alram.days}</Button>
                    <Button variant='success' className="btn-alram-description">복용시간 : {alram.beforeMeal ? "식전" : "식후"} {alram.dosingTime}분</Button>
                </div>
            </div>
        </>
    )
}

export default AlramListItem;