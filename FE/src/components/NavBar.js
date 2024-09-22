import { Link } from 'react-router-dom';
import { useLogin } from '../contexts/AuthContext';

const NavBar = () => {
    const { isLoggedIn } = useLogin();
    return (
        <nav>
            <Link to="/">Home</Link>
            {!isLoggedIn && <Link to="/join">Join</Link>}
            {!isLoggedIn && <Link to="/login">Login</Link>}
            {isLoggedIn && <Link to="/logout">Logout</Link>}
            {isLoggedIn && <Link to="/follow">Follow</Link>}
            {isLoggedIn && <Link to="/post">Post</Link>}
            {isLoggedIn && <Link to="/edit-account">Edit Account</Link>}
            {isLoggedIn && <Link to="/change-password">Change Password</Link>}
        </nav>
    );
}

export default NavBar;
