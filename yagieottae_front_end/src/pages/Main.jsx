import AlramList from "components/alramList/AlramList";
import ModalSearchPill from "components/modalSearchPill/ModalSearchPill";
import { IsLoggedIn } from "utils/IsLoggedIn";

function Main()
{
  return (
    <div id="container-main" className="container flex-column-spaceEvenly-center">
      <ModalSearchPill />
      {IsLoggedIn() ? <AlramList /> : ""} {/*로그인 상태에서만 오늘의 약 리스트 표출*/}
    </div>
  );
}

export default Main;
