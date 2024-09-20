import { useLogin } from "../contexts/AuthContext";
import React, { useEffect } from 'react';


const Home = () => {
    const { isLoggedIn, loginUser, setLoginUser } = useLogin();

    useEffect(() => {
        const storedNickname = window.localStorage.getItem("nickname");
        if (storedNickname) {
            setLoginUser(storedNickname);  // 닉네임을 loginUser로 설정
        }
    }, []);

    return (
        <div>
            <h1>Home</h1>
            {isLoggedIn && <span>{loginUser}님 환영합니다.</span>}
        </div>
    );
};

export default Home;