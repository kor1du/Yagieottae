import { FaBars, FaUser, FaUserCircle } from 'react-icons/fa';
import 'components/nav/styles/nav.scss';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { AiTwotoneHome } from 'react-icons/ai';
import { HiOutlineLogout } from 'react-icons/hi';
import Logo from 'assets/imgs/logo.png'
import { Button } from 'react-bootstrap';
import { Server_URL } from 'constants/ServerURL';
import UseNav from './hooks/UseNav';
import { IsLoggedIn } from 'utils/IsLoggedIn';
import { Logout } from 'utils/Logout';

function Nav()
{
	const { showLeftSide, toggleNavLeftSide } = UseNav();

	return (
		<div id="container-nav">
			<div className="flex-row-spaceBetween-center">
				<div className="flex-row-center-center">
					<FaBars size="30" onClick={toggleNavLeftSide} />
				</div>
				<div className="flex-row-center-center">
					<span id="text-logo">약이어때</span>
					<img src={Logo} alt="logo" id="img-logo" />
				</div>
			</div>

			{showLeftSide ?
				<div className="nav-left-side flex-column-spaceEvenly-flexStart animate__animated animate__fadeInLeft animate__faster">
					<Link to="/" className='flex-row-center-center' onClick={toggleNavLeftSide}>
						<AiTwotoneHome size="40" />
						<span>홈으로</span>
					</Link>
					{IsLoggedIn() ?
						<>
							<Link onClick={() => { Logout(Server_URL); toggleNavLeftSide(); }}>
								<HiOutlineLogout size="40" />
								<span>로그아웃</span>
							</Link>
							<Link to="/user/mypage" className='flex-row-center-center' onClick={toggleNavLeftSide}>
								<FaUser size="40" />
								<span>마이페이지</span>
							</Link>
						</>
						:
						<Link to="/user/login" className='flex-row-center-center' onClick={toggleNavLeftSide}>
							<FaUserCircle size="40" />
							<span>로그인</span>
						</Link>
					}
				</div>
				: ''}
		</div>
	);
}

export default Nav;
