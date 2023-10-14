import { useEffect, useState } from "react";
import { FetchAlramList } from "utils/FetchAlramList";

function UseAlramList()
{
    const [showModal, setShowModal] = useState(false);
    const [todayAlramList, setTodayAlramList] = useState([]);
    const [alldayAlramList, setAlldayAlramList] = useState([]);

    useEffect(() => 
    {
        FetchAlramList(true, setTodayAlramList);
    }, [])

    const onClickShowAllAlramListBtn = () =>
    {
        setShowModal(true);
        FetchAlramList(false, setAlldayAlramList);
    }

    return {
        showModal, setShowModal, todayAlramList, alldayAlramList, onClickShowAllAlramListBtn
    }
}

export default UseAlramList;