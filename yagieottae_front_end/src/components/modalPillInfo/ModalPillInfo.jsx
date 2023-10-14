import { useState } from 'react';
import { Button } from 'react-bootstrap';
import Drugs from 'assets/imgs/drugs.png';
import 'components/modalPillInfo/styles/modalPillInfo.scss';
import ModalInstance from 'utils/ModalInstance';
import UseModalPillInfo from 'components/modalPillInfo/hooks/UseModalPillInfo';
import ModalReview from 'components/modalAlramReview/ModalReview';
import ModalSetAlram from 'components/modalSetAlram/ModalSetAlarm';

function ModalPillInfo({ pill, existingAlram })
{
    const { showModal, setShowModal } = UseModalPillInfo();

    //약 속성값 한글로 변경
    const getTranslatedFieldName = (field) =>
    {
        switch (field)
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

    return (
        <>
            <Button className='btn-pill-info' onClick={() => { setShowModal(true); }}>상세보기</Button>
            <ModalInstance
                show={showModal}
                setShow={setShowModal}
                title={
                    <div className="flex-row-center-center">
                        <img src={pill.imagePath === null ? { Drugs } : pill.imagePath} className='img-modal-title-pill' alt='drugs' onError={(e) => { e.target.src = Drugs; }} />
                        <p className="text-itemName">{pill.itemName}</p>
                    </div>
                }
                body={
                    <>
                        <div className='pill-description'>
                            <Button variant='success' className="title">제품명</Button>
                            <p className="pill-name description">{pill.itemName}</p>
                        </div>
                        {Object.keys(pill).map((field) =>
                        {
                            const translatedFieldName = getTranslatedFieldName(field); //한국어로 번역된 사용법 제목

                            return (
                                <div key={field}>
                                    {translatedFieldName === '' || pill[field] === '' ?
                                        '' :
                                        <div>
                                            <div className='pill-description'>
                                                <Button variant='success' className="title">{getTranslatedFieldName(field)}</Button>
                                                <p className="description">{pill[field]}</p>
                                            </div>
                                        </div>
                                    }
                                </div>
                            );
                        })}

                        <div className='flex-row-spaceBetween-center'>
                            <ModalReview pill={pill} />
                            <ModalSetAlram pill={pill} existingAlram={existingAlram} />
                        </div>
                    </>
                }
                contentClassName={"modal-pill-info"}
            />
        </>
    )
}

export default ModalPillInfo;