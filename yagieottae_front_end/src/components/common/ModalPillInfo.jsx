import { useState } from 'react';
import { Button } from 'react-bootstrap';
import ModalSetAlram from 'components/common/ModalSetAlarm';
import Drugs from 'assets/imgs/drugs.png';
import ModalReview from 'components/modal/review/ModalReview';
import 'styles/common/modalPillInfo.scss';
import ModalInstance from 'utils/ModalInstance';

function ModalPillInfo({ item, existingAlram })
{
    const [showModal, setShowModal] = useState(false);

    //약 속성값 한글로 변경
    const getKeyName = (key, selectedPill) =>
    {
        if (selectedPill[key] == null) return '';

        switch (key)
        {
            case 'entpName': {
                return '업체명';
            }
            case 'mainIngredient': {
                return '주 성분'
            }
            case 'efcyQesitm': {
                return '효능';
            }
            case 'useMethodQesitm': {
                return '사용법';
            }
            case 'atpnWarnQesitm': {
                return '필수 주의사항';
            }
            case 'atpnQesitm': {
                return '주의사항';
            }
            case 'intrcQesitm': {
                return '복용시 주의해야할 약 또는 음식';
            }
            case 'seQesitm': {
                return '부작용';
            }
            case 'depositMethodQesitm': {
                return '보관법';
            }

            default: {
                return '';
            }
        }
    };

    const modalTitle = () =>
    {
        return (
            <div className="flex-row-center-center">
                <img src={item.imagePath === null ? { Drugs } : item.imagePath} className='img-modal-title-pill' alt='drugs' onError={(e) => { e.target.src = Drugs; }} />
                <p className="text-itemName">{item.itemName}</p>
            </div>
        )
    }

    const modalBody = (pill) =>
    {
        return (
            <>
                <div className='pill-description'>
                    <Button variant='success' className="title">제품명</Button>
                    <p className="pill-name description">{pill.itemName}</p>
                </div>
                {Object.keys(pill).map((item) =>
                {
                    let keyName = getKeyName(item, pill); //한국어로 번역한 약 상세사항

                    return (
                        <div key={item}>
                            {keyName === '' || pill[item].length === 0 ?
                                '' :
                                (
                                    <div className='pill-description'>
                                        <Button variant='success' className="title">{keyName}</Button>
                                        <p className="description">{pill[item]}</p>
                                    </div>
                                )}
                        </div>
                    );
                })}

                <div className='flex-row-spaceBetween-center'>
                    <ModalSetAlram pill={pill} existingAlram={existingAlram} />
                    <ModalReview pill={pill} />
                </div>
            </>
        )
    }

    return (
        <>
            <Button className='btn-pill-info' onClick={() => { setShowModal(true); }}>상세보기</Button>
            <ModalInstance show={showModal} setShow={setShowModal} title={modalTitle()} body={modalBody(item)} contentClassName={"modal-pill-info"} />
        </>
    )
}

export default ModalPillInfo;