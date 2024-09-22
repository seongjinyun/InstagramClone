import { useLogin } from "../contexts/AuthContext";
import React, { useEffect, useState } from 'react';

const Home = () => {
    const { isLoggedIn, loginUser, setLoginUser } = useLogin();
    const [posts, setPosts] = useState([]);

    useEffect(() => {
        const storedNickname = window.localStorage.getItem("nickname");
        if (storedNickname) {
            setLoginUser(storedNickname); // 닉네임을 loginUser로 설정
        }
        
        if (isLoggedIn) {
            fetchPosts();
        }
    }, [isLoggedIn]); // isLoggedIn이 변경될 때마다 포스트를 가져옴

    const fetchPosts = async () => {
        try {
            const token = localStorage.getItem('access');
            const response = await fetch('http://localhost:8080/api/v1/posts', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`, // JWT 토큰을 헤더에 추가
                    'Content-Type': 'application/json' // JSON 형식으로 요청
                }
            });
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const text = await response.text();
            const data = JSON.parse(text);
            console.log('Fetched posts:', data);
            setPosts(data);
        } catch (error) {
            console.error('Error fetching posts:', error);
        }
    };

    return (
        <div>
            <h1>Home</h1>
            {isLoggedIn && <span>{loginUser}님 환영합니다.</span>}
            {isLoggedIn && posts.length > 0 ? (
                <div>
                    {posts.map((post, index) => (
                        <div key={index} className="post">
                            <p>{post.content}</p>
                            {post.mediaUrls && post.mediaUrls.map((url, idx) => {
                                // 경로 변환: 백슬래시를 슬래시로 변경
                                const formattedUrl = url.replace(/\\/g, '/'); 
                                const isVideo = formattedUrl.endsWith('.mp4') || formattedUrl.endsWith('.mov'); // 비디오 파일 확장자 확인
                                return isVideo ? (
                                    <video key={idx} controls>
                                        <source src={`http://localhost:8080/images/${formattedUrl.split('/').pop()}`} type="video/mp4" />
                                        Your browser does not support the video tag.
                                    </video>
                                ) : (
                                    <img key={idx} src={`http://localhost:8080/images/${formattedUrl.split('/').pop()}`} alt={`Media ${idx}`} />
                                );
                            })}
                            <p>작성자: {post.writer}</p>
                        </div>
                    ))}
                </div>
            ) : (
                isLoggedIn && <p>업로드된 게시글이 없습니다.</p> // 게시글이 없을 경우 메시지 출력
            )}
        </div>
    );
};

export default Home;
