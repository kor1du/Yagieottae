import './App.css';
import { Route, Routes } from 'react-router-dom';
import Main from 'pages/Main';
import './styles/common/common.scss';
import Nav from 'components/nav/Nav';
import Login from 'pages/Login';
import Signup from 'pages/Signup';
import MyPage from 'pages/MyPage';
import PageNotFound from 'pages/PageNotFound';
import RequireAuth from 'pages/RequireAuth';

function App()
{
	return (
		<>
			<Nav />
			<Routes>
				<Route path="/" element={<Main />} />
				<Route path="/user/login" element={<Login />} />
				<Route path="/user/signup" element={<Signup />} />
				<Route path="/user/mypage" element={
					<RequireAuth>
						<MyPage />
					</RequireAuth>
				} />
				<Route path="*" element={<PageNotFound />} />
			</Routes>
		</>
	);
}

export default App;
