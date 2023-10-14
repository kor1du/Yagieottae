import './App.css';
import { Route, Routes } from 'react-router-dom';
import Main from 'pages/Main';
import 'components/common/styles/common.scss';
import Nav from 'components/nav/Nav';
import Signup from 'pages/SignupPage';
import MyPage from 'pages/MyPage';
import PageNotFound from 'pages/PageNotFound';
import RequireAuth from 'pages/RequireAuth';
import LoginPage from 'pages/LoginPage';

function App()
{
	return (
		<>
			<Nav />
			<Routes>
				<Route path="/" element={<Main />} />
				<Route path="/user/login" element={<LoginPage />} />
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
