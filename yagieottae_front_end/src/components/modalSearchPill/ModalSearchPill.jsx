import { Button } from "react-bootstrap";
import "components/modalSearchPill/styles/modalSearchPill.scss";
import ModalInstance from "utils/ModalInstance";
import Drugs from 'assets/imgs/drugs.png';
import ModalPillInfo from "components/modalPillInfo/ModalPillInfo";
import UseModalSearchPill from "components/modalSearchPill/hooks/UseModalSearchPill";
import { Pagination } from "@mui/material";

const { Input } = require("@mui/joy");

function ModalSearchPill()
{
    const { showModal, setShowModal, pillName, pillList, pagination, onClickSearchBtn, onChangePillName, onChangeCurrentPage } = UseModalSearchPill();
    const { currentPage, itemCountPerPage, totalItemCount } = pagination;

    return (
        <div id="container-search-pill" className="flex-column-center-center">
            <div className="search-pill flex-row-spaceEvenly-center">
                <Input value={pillName} onChange={onChangePillName} id='input-search-pill' variant="soft" placeholder="약 이름을 검색해주세요" />
                <Button id="btn-search" onClick={onClickSearchBtn}>검색하기</Button>
                <ModalInstance show={showModal} setShow={setShowModal} title={"검색 결과"} body={
                    <div className="flex-column-center-center pill-list">
                        {
                            pillList.map((pill) =>
                            {
                                return (
                                    <div className='flex-row-center-center pill' key={pill.id}>
                                        <img src={pill.imagePath == null ? { Drugs } : pill.imagePath} alt='drugs' className='img-modal-title-pill' onError={(e) => { e.target.src = Drugs; }} />
                                        <div className="div-text-modal-body-pill">
                                            <p className='text-modal-body-pill'> {pill.itemName} </p>
                                        </div>
                                        <ModalPillInfo pill={pill} />
                                    </div>
                                );
                            })
                        }

                        <div className="flex-row-center-center">
                            <Pagination
                                count={Math.ceil(totalItemCount / itemCountPerPage)}
                                page={currentPage}
                                onChange={onChangeCurrentPage}
                                color={"primary"}
                            />
                        </div>
                    </div>
                }
                    contentClassName={"modal-search-pill"}
                />
            </div>
        </div>
    )
}

export default ModalSearchPill;