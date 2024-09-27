import { useLogin } from "../contexts/AuthContext";
import React, { useEffect, useState } from 'react';

const Home = () => {
    const { isLoggedIn, loginUser, setLoginUser } = useLogin();
    const [posts, setPosts] = useState([]);
    
    // S3 버킷 정보
    const s3BucketName = "instagram-clonecoding-project"; // 실제 S3 버킷 이름
    const region = "ap-northeast-2"; // 실제 S3 버킷 리전

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
            <hr></hr>
            {isLoggedIn && posts.length > 0 ? (
                <div>
                    {posts.map((post, index) => (
                        <div key={index} className="post">
                            <p>작성자: {post.writer}</p>
                            <p>{post.content}</p>
                            <div className="media-container">
                                {post.mediaUrls && post.mediaUrls.map((url, idx) => {
                                    const isVideo = url.endsWith('.mp4') || url.endsWith('.mov'); // 비디오 파일 확장자 확인
                                    const s3Url = `${url}`; // S3 URL 구성
    
                                    return isVideo ? (
                                        <video key={idx} controls style={{ width: '200px', height: '200px', objectFit: 'contain' }}>
                                            <source src={s3Url} type="video/mp4" />
                                            Your browser does not support the video tag.
                                        </video>
                                    ) : (
                                        <img key={idx} src={s3Url} alt={`Media ${idx}`} style={{ width: '200px', height: '200px', objectFit: 'contain' }} />
                                    );
                                })}
                            </div>
                                <hr></hr>
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
