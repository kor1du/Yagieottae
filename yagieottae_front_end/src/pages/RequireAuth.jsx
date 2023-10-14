import { IsLoggedIn } from "utils/IsLoggedIn";

const { Navigate } = require("react-router-dom");

function RequireAuth({ children })
{
    if (!IsLoggedIn())
    {
        alert("로그인이 필요합니다!");
        return <Navigate to="/user/login" />;
    }
    return children;
}

export default RequireAuth;