import { isLoggedIn } from "components/user/User";

const { Navigate } = require("react-router-dom");

function RequireAuth({ children })
{
    if (!isLoggedIn())
    {
        alert("로그인이 필요합니다!");
        return <Navigate to="/user/login" />;
    }
    return children;
}

export default RequireAuth;