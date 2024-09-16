import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const SetNickname = () => {
    const [nickname, setNickname] = useState('');
    const navigate = useNavigate();

    const handleSetNickname = async (e) => {
        e.preventDefault();
        try {
            const token = window.localStorage.getItem("access");
            const response = await fetch("http://localhost:8080/api/v1/nickname/set", {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ nickname })
            });
            if (response.ok) {
                window.localStorage.setItem("nickname", nickname); // 로컬스토리지에 닉네임 저장
                window.localStorage.setItem("name", nickname);     // 로컬스토리지의 name(실명)을 nickname으로 덮어쓰기
                alert("닉네임 설정 완료");
                navigate("/");  // 닉네임 설정 후 Home으로 리다이렉트
            } else {
                alert("닉네임 설정 실패");
            }
        } catch (error) {
            console.log("닉네임 설정 중 오류:", error);
        }
    }

    return (
        <div className="set-nickname">
            <h1>닉네임 설정</h1>
            <form onSubmit={handleSetNickname}>
                <p><span className='label'>Nickname</span>
                    <input className='input-class' type="text" value={nickname} onChange={(e) => setNickname(e.target.value)} placeholder="nickname" /></p>
                <input type="submit" value="Set Nickname" className="form-btn" />
            </form>
        </div>
    );
}

export default SetNickname;