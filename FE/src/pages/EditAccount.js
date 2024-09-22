import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const EditAccount = () => {
    const [email, setEmail] = useState('');
    const [nickname, setNickname] = useState('');
    const [profilePic, setProfilePic] = useState('');
    const [bio, setBio] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        // 기본 사용자 정보를 로딩 (예: API를 통해 가져옴)
        const token = window.localStorage.getItem("access");
        fetch("http://localhost:8080/api/v1/user/me", {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                setEmail(data.email);
                setNickname(data.nickname);
                setProfilePic(data.profilePic);
                setBio(data.bio);
            })
            .catch(error => console.log("계정 정보를 로드하는 중 오류 발생:", error));
    }, []);

    const handleUpdateAccount = async (e) => {
        e.preventDefault();
        const token = window.localStorage.getItem("access");
        try {
            const response = await fetch("http://localhost:8080/api/v1/user/update", {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email, nickname, profilePic, bio })
            });
            if (response.ok) {
                alert("계정 정보가 성공적으로 업데이트되었습니다.");
                navigate("/profile"); // 프로필 페이지로 리디렉션
            } else {
                alert("계정 정보 업데이트 실패");
            }
        } catch (error) {
            console.log("계정 정보 업데이트 중 오류:", error);
        }
    }

    return (
        <div className="edit-account">
            <h1>내 계정 정보 수정</h1>
            <form onSubmit={handleUpdateAccount}>
                <p><span className='label'>이메일</span>
                    <input className='input-class' type="email" value={email} disabled /></p>
                <p><span className='label'>닉네임</span>
                    <input className='input-class' type="text" value={nickname} onChange={(e) => setNickname(e.target.value)} /></p>
                <p><span className='label'>프로필 사진 URL</span>
                    <input className='input-class' type="text" value={profilePic} onChange={(e) => setProfilePic(e.target.value)} /></p>
                <p><span className='label'>소개글</span>
                    <textarea className='input-class' value={bio} onChange={(e) => setBio(e.target.value)} /></p>
                <input type="submit" value="Update Account" className="form-btn" />
            </form>
        </div>
    );
}

export default EditAccount;