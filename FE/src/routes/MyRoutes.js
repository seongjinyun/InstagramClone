import { Routes, Route } from 'react-router-dom';
import JoinForm from '../pages/Join';
import LoginForm from '../pages/Login';
import Home from '../pages/Home';
import OAuth2Redirect from '../services/Oauth2Redirect';
import Admin from '../pages/Admin';
import Logout from '../pages/Logout';
import Follow from '../pages/Follow';
import Post from '../pages/Post';
import SetNickname from '../pages/SetNickname';
import EditAccount from '../pages/EditAccount';
import ChangePassword from '../pages/ChangePassword';
import { useLogin } from '../contexts/AuthContext';

const MyRoutes = () => {
  const { isLoggedIn } = useLogin();
  // 로그인 여부에 따라서 조건부 라우팅
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      {!isLoggedIn && <Route path="/login" element={<LoginForm />} />}
      {!isLoggedIn && <Route path="/join" element={<JoinForm />} />}
      {isLoggedIn && <Route path="/logout" element={<Logout />} />}
      {isLoggedIn && <Route path="/follow" element={<Follow />} />}
      {isLoggedIn && <Route path="/post" element={<Post />} />}
      {isLoggedIn && <Route path="/edit-account" element={<EditAccount />} />}
      {isLoggedIn && <Route path="/change-password" element={<ChangePassword />} />}
      <Route path="/admin" element={<Admin />} />
      <Route path="/oauth2-jwt-header" element={<OAuth2Redirect />} />
      <Route path="/set-nickname" element={<SetNickname />} />
    </Routes>
  );
}

export default MyRoutes;