import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useLogin } from '../contexts/AuthContext';

const Follow = () => {
    const navigate = useNavigate();
    const { isLoggedIn, loginUser } = useLogin();
    const [followers, setFollowers] = useState([]);
    const [following, setFollowing] = useState([]);

    // 로그인되지 않은 사용자는 접근하지 못하게 설정
    useEffect(() => {
        if (!isLoggedIn) {
            alert("로그인이 필요합니다.");
            navigate("/login", { replace: true });
        }
    }, [isLoggedIn, navigate]);

    // 팔로워 목록 가져오기
    const fetchFollowers = async () => {
        try {
            const token = window.localStorage.getItem("access");
            if (!token) {
                alert("로그인이 필요합니다.");
                return;
            }
            const response = await fetch(`http://localhost:8080/api/v1/${loginUser}/followers`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                }
            });
            if (response.ok) {
                const data = await response.json();
                setFollowers(data);
            } else {
                alert("팔로워 목록을 불러오는데 실패했습니다.");
            }
        } catch (error) {
            console.log("팔로워 목록 불러오기 실패: ", error);
        }
    };

    // 팔로잉 목록 가져오기
    const fetchFollowing = async () => {
        try {
            const token = window.localStorage.getItem("access");
            if (!token) {
                alert("로그인이 필요합니다.");
                return;
            }
            const response = await fetch(`http://localhost:8080/api/v1/${loginUser}/following`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                }
            });
            if (response.ok) {
                const data = await response.json();
                setFollowing(data);
            } else {
                alert("팔로잉 목록을 불러오는데 실패했습니다.");
            }
        } catch (error) {
            console.log("팔로잉 목록 불러오기 실패: ", error);
        }
    };

    // 팔로우 기능
    const handleFollow = async (memberId) => {
        try {
            const token = localStorage.getItem('access');
            if (!token) {
                console.log("Access Token이 없습니다.");
                alert("로그인이 필요합니다.");
                navigate("/login");
                return;
            }

            const response = await fetch(`http://localhost:8080/api/v1/${loginUser}/follow/${memberId}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                alert('팔로우 성공');
                fetchFollowing(); // 팔로우 목록 업데이트
            } else {
                alert('팔로우 실패');
            }
        } catch (error) {
            console.log("팔로우 요청 실패: ", error);
        }
    };

    // 언팔로우 기능
    const handleUnfollow = async (memberId) => {
        try {
            const token = localStorage.getItem('access');
            if (!token) {
                console.log("Access Token이 없습니다.");
                alert("로그인이 필요합니다.");
                navigate("/login");
                return;
            }

            const response = await fetch(`http://localhost:8080/api/v1/${loginUser}/unfollow/${memberId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (response.ok) {
                alert('언팔로우 성공');
                fetchFollowing(); // 팔로잉 목록 업데이트
            } else {
                alert('언팔로우 실패');
            }
        } catch (error) {
            console.log("언팔로우 요청 실패: ", error);
        }
    };

    useEffect(() => {
        if (isLoggedIn) {
            fetchFollowers();
            fetchFollowing();
        }
    }, [isLoggedIn]);

    return (
        <div>
            <h1>{loginUser}의 팔로우 관리</h1>

            <div>
                <h2>팔로워 목록</h2>
                <ul>
                    {followers.map((follower, index) => (
                        <li key={index}>{follower.followerUsername}</li>
                    ))}
                </ul>
            </div>

            <div>
                <h2>팔로잉 목록</h2>
                <ul>
                    {following.map((followed, index) => (
                        <li key={index}>
                            {followed.memberUsername}
                            <button onClick={() => handleUnfollow(followed.followsId)}>언팔로우</button>
                        </li>
                    ))}
                </ul>
            </div>

            <div>
                <h2>사용자 팔로우</h2>
                <input type="text" placeholder="팔로우할 사용자 ID" id="followUserId"/>
                <button onClick={() => handleFollow(document.getElementById('followUserId').value)}>팔로우</button>
            </div>
        </div>
    );
};

export default Follow;