import { useState } from "react";

function UseNav()
{
    const [showLeftSide, setShowLeftSide] = useState(false);
    const toggleNavLeftSide = () => 
    {
        showLeftSide === true ? setShowLeftSide(false) : setShowLeftSide(true);
    }

    return { showLeftSide, toggleNavLeftSide };
}

export default UseNav;