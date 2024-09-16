import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useLogin } from '../contexts/AuthContext'; // AuthContext에서 로그인 상태와 유저 정보 가져오기

const CreatePost = () => {
    const navigate = useNavigate();
    const { isLoggedIn, loginUser } = useLogin(); // 로그인 정보 사용
    const [content, setContent] = useState(''); // 게시글 내용
    const [mediaFiles, setMediaFiles] = useState([]); // 첨부 파일들
    const [previewUrls, setPreviewUrls] = useState([]); // 첨부 파일 미리보기

    useEffect(() => {
        if (!isLoggedIn) {
            alert("로그인이 필요합니다.");
            navigate("/login", { replace: true });
        }
    }, [isLoggedIn, navigate]);

    // 파일 선택 시 미리보기 설정
    const handleFileChange = (e) => {
        const files = Array.from(e.target.files); // 여러 파일 선택 가능
        setMediaFiles(files); // 선택한 파일 상태에 저장

        // 파일 미리보기 생성
        const filePreviews = files.map((file) => URL.createObjectURL(file));
        setPreviewUrls(filePreviews);
    };

    // 게시글 전송 함수
    const submitPost = async () => {
        const token = localStorage.getItem('access');
        if (!token) {
            alert("로그인이 필요합니다.");
            navigate("/login");
            return;
        }

        const formData = new FormData();
        formData.append('content', content); // 게시글 내용 추가
        mediaFiles.forEach((file) => formData.append('mediaFiles', file)); // 파일 추가

        try {
            const response = await fetch("http://localhost:8080/api/v1/create/posts", {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`, // JWT 인증 헤더 추가
                },
                body: formData,
            });

            if (response.ok) {
                alert("게시글이 성공적으로 등록되었습니다.");
                navigate("/", { replace: true }); // 성공 시 홈으로 리다이렉트
            } else {
                alert("게시글 등록에 실패했습니다.");
            }
        } catch (error) {
            console.log("게시글 등록 실패: ", error);
        }
    };

    // 폼 전송 핸들러
    const handleSubmit = (e) => {
        e.preventDefault();
        submitPost();
    };

    return (
        <div>
            <h1>게시글 작성</h1>

            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="content">내용</label>
                    <textarea
                        id="content"
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                        placeholder="게시글 내용을 입력하세요"
                    />
                </div>

                <div>
                    <label htmlFor="mediaFiles">파일 첨부</label>
                    <input
                        type="file"
                        id="mediaFiles"
                        multiple
                        accept="image/*, video/*"
                        onChange={handleFileChange}
                    />
                </div>

                {/* 파일 미리보기 */}
                <div className="preview">
                    {previewUrls.map((url, idx) => (
                        <img key={idx} src={url} alt={`preview-${idx}`} width="100" />
                    ))}
                </div>

                <button type="submit">게시글 작성</button>
            </form>
        </div>
    );
};

export default CreatePost;
