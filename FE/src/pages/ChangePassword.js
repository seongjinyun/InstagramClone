import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const ChangePassword = () => {
    const [currentPassword, setCurrentPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const navigate = useNavigate();

    const handleChangePassword = async (e) => {
        e.preventDefault();
        if (newPassword !== confirmPassword) {
            alert("새 비밀번호가 일치하지 않습니다.");
            return;
        }
        const token = window.localStorage.getItem("access");
        try {
            const response = await fetch("http://localhost:8080/api/v1/user/change-password", {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ currentPassword, newPassword })
            });
            if (response.ok) {
                alert("비밀번호가 성공적으로 변경되었습니다.");
                navigate("/profile");
            } else {
                alert("비밀번호 변경 실패");
            }
        } catch (error) {
            console.log("비밀번호 변경 중 오류:", error);
        }
    }

    return (
        <div className="change-password">
            <h1>비밀번호 변경</h1>
            <form onSubmit={handleChangePassword}>
                <p><span className='label'>현재 비밀번호</span>
                    <input className='input-class' type="password" value={currentPassword} onChange={(e) => setCurrentPassword(e.target.value)} /></p>
                <p><span className='label'>새 비밀번호</span>
                    <input className='input-class' type="password" value={newPassword} onChange={(e) => setNewPassword(e.target.value)} /></p>
                <p><span className='label'>새 비밀번호 확인</span>
                    <input className='input-class' type="password" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} /></p>
                <input type="submit" value="Change Password" className="form-btn" />
            </form>
        </div>
    );
}

export default ChangePassword;