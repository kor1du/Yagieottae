import "styles/main/main.scss";
import DrugStore from "assets/imgs/drugstore.png";
import AlramList from "components/main/AlramList";
import ModalSearchPill from "components/common/ModalSearchPill";
import { isLoggedIn } from "components/user/User";


function Main()
{
  return (
    <>
      <div id="container-main" className="container">
        <ModalSearchPill />
        {isLoggedIn() ? <AlramList /> : ""} {/*로그인 상태에서만 오늘의 약 리스트 표출*/}
        <div className="drug-store-list">
          <p className="find-hospital">근처 약국 찾기</p>
          <div className="flex-row-spaceEvenly-center">
            <div className="drug-store flex-column-center-center">
              <img src={DrugStore} className="img-drug-store" alt="img-drug-store" />
              <span className="drug-store-name">A약국</span>
            </div>
            <div className="drug-store flex-column-center-center">
              <img src={DrugStore} className="img-drug-store" alt="img-drug-store" />
              <span className="drug-store-name">B약국</span>
            </div>
            <div className="drug-store flex-column-center-center">
              <img src={DrugStore} className="img-drug-store" alt="img-drug-store" />
              <span className="drug-store-name">C약국</span>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default Main;
