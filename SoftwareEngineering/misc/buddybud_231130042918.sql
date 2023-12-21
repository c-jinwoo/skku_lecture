/* 
 * MySQL Configuration
 * $ mysql -h localhost -u root -p
 * mysql> CREATE USER 'manager'@'%' IDENTIFIED BY 'password';
 * mysql> GRANT ALL PRIVILEGES ON *.* TO 'manager'@'%';
 * mysql> FLUSH PRIVILEGES;
 * mysql> CREATE DATABASE buddybud;
 */
 
 /**************** 테이블 생성 쿼리 ******************/
 /* 사용자 테이블 */
CREATE TABLE M_USER(
    seq INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(30) NOT NULL,
    pw VARCHAR(30) NOT NULL,
    id VARCHAR(30) NOT NULL,
    school VARCHAR(30) NOT NULL,
    dob DATE NOT NULL,
    lang VARCHAR(15) NOT NULL,
    gender VARCHAR(30) NOT NULL,
    state VARCHAR(2) NOT NULL default 'Y'
);

/* 게시글 테이블 */
CREATE TABLE B_POST (
    seq INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    url VARCHAR(255),
    user_no INT NOT NULL,
    board_type ENUM('SNS', 'Notice') NOT NULL,
    created_at DATETIME NOT NULL,
    last_modified_at DATETIME,
    state VARCHAR(2) NOT NULL default 'Y',
    FOREIGN KEY (user_no) REFERENCES M_USER(seq)
);

/* 게시글 좋아요 누른 유저 목록 테이블 */
CREATE TABLE B_POST_LIKE (
    post_no INT NOT NULL,
    user_no INT NOT NULL,
    PRIMARY KEY (post_no, user_no),
    FOREIGN KEY (post_no) REFERENCES B_POST(seq),
    FOREIGN KEY (user_no) REFERENCES M_USER(seq)
);

/* 게시글 댓글 테이블 */
CREATE TABLE B_COMMENT (
    seq INT PRIMARY KEY AUTO_INCREMENT,
    content TEXT,
    post_no INT NOT NULL,
    user_no INT NOT NULL,
    parent_comment_no INT,
    created_at DATETIME NOT NULL,
    state VARCHAR(2) NOT NULL default 'Y',
    FOREIGN KEY (post_no) REFERENCES B_POST(seq),
    FOREIGN KEY (user_no) REFERENCES M_USER(seq),
    FOREIGN KEY (parent_comment_no) REFERENCES B_COMMENT(seq)
);

/* 댓글 좋아요 누른 유저 목록 테이블 */
CREATE TABLE B_COMMENT_LIKE (
    comment_no INT NOT NULL,
    user_no INT NOT NULL,
    PRIMARY KEY (comment_no, user_no),
    FOREIGN KEY (comment_no) REFERENCES B_COMMENT(seq),
    FOREIGN KEY (user_no) REFERENCES M_USER(seq)
);

/* 댓글 싫어요 누른 유저 목록 테이블 */
CREATE TABLE B_COMMENT_HATE (
    comment_no INT NOT NULL,
    user_no INT NOT NULL, 
    PRIMARY KEY (comment_no, user_no),
    FOREIGN KEY (comment_no) REFERENCES B_COMMENT(seq),
    FOREIGN KEY (user_no) REFERENCES M_USER(seq)
);

/* 게시글 스크랩 목록 테이블 */
CREATE TABLE B_SCRAP (
    post_no INT NOT NULL,
    user_no INT NOT NULL,
    created_at DATETIME NOT NULL,
    PRIMARY KEY(post_no, user_no),
    FOREIGN KEY (post_no) REFERENCES B_POST(seq),
    FOREIGN KEY (user_no) REFERENCES M_USER(seq)
);

/* 윌로우 채팅방 목록 테이블 */
CREATE TABLE W_CHAT_ROOM (
    seq INT PRIMARY KEY AUTO_INCREMENT,
    sender_no INT NOT NULL,	
    receiver_no INT NOT NULL,
    FOREIGN KEY (sender_no) REFERENCES M_USER(seq),
    FOREIGN KEY (receiver_no) REFERENCES M_USER(seq)
);

/* 윌로두 채팅방 메시지 테이블 */
CREATE TABLE W_CHAT_MESSAGE (
    seq INT PRIMARY KEY AUTO_INCREMENT,
    chat_room_no INT NOT NULL,
    sender_no INT NOT NULL,
    content TEXT,
    created_at DATETIME NOT NULL,
    FOREIGN KEY (chat_room_no) REFERENCES W_CHAT_ROOM(seq),
    FOREIGN KEY (sender_no) REFERENCES M_USER(seq)
);

/* 윌로우 신청 목록 테이블 */
CREATE TABLE W_WILLOW (
    sender_no INT NOT NULL,
    receiver_no INT NOT NULL,
    created_at DATETIME NOT NULL,
    state VARCHAR(2) NOT NULL default 'N',
    PRIMARY KEY (sender_no, receiver_no),
    FOREIGN KEY (sender_no) REFERENCES M_USER(seq),
    FOREIGN KEY (receiver_no) REFERENCES M_USER(seq)
);


/************************** 사용자 쿼리 *********************************/
INSERT INTO	M_USER (email, pw, id, school, dob, lang, gender)
VALUES ( "1", "1", "test1", "seoul", str_to_date("20000101", '%Y%m%d'), "Korean", "Man");
INSERT INTO	M_USER (email, pw, id, school, dob, lang, gender)
VALUES ( "2", "2", "test2", "yonsei", str_to_date("20000102", '%Y%m%d'), "English", "Woman");
INSERT INTO	M_USER (email, pw, id, school, dob, lang, gender)
VALUES ( "3", "3", "test3", "korea", str_to_date("20000103", '%Y%m%d'), "Chinese", "Transgender");
INSERT INTO	M_USER (email, pw, id, school, dob, lang, gender)
VALUES ( "4", "4", "test4", "skku", str_to_date("20000104", '%Y%m%d'), "Japanese", "Non-binary");
INSERT INTO	M_USER (email, pw, id, school, dob, lang, gender)
VALUES ( "5", "5", "test5", "hanyang", str_to_date("20000105", '%Y%m%d'), "Korean", "No Comment");
INSERT INTO	M_USER (email, pw, id, school, dob, lang, gender)
VALUES ( "6", "6", "test6", "seogang", str_to_date("20000106", '%Y%m%d'), "Chinese", "Man");
INSERT INTO	M_USER (email, pw, id, school, dob, lang, gender)
VALUES ( "7", "7", "test7", "chungang", str_to_date("20000107", '%Y%m%d'), "English", "Woman");
INSERT INTO	M_USER (email, pw, id, school, dob, lang, gender)
VALUES ( "8", "8", "test8", "kyunghee", str_to_date("20000108", '%Y%m%d'), "Japanese", "Transgender");
INSERT INTO	M_USER (email, pw, id, school, dob, lang, gender)
VALUES ( "9", "9", "test9", "kaist", str_to_date("20000109", '%Y%m%d'), "Korean", "Man");
INSERT INTO	M_USER (email, pw, id, school, dob, lang, gender)
VALUES ( "10", "10", "test10", "postech", str_to_date("20000110", '%Y%m%d'), "English", "Woman");
INSERT INTO	M_USER (email, pw, id, school, dob, lang, gender)
VALUES ("11", "11",	"mililili",	"univ11", str_to_date("20000111", '%Y%m%d'), "English",	"Man");    
INSERT INTO	M_USER (email, pw, id, school, dob, lang, gender)
VALUES ("12","12","superSon","univ12",str_to_date("20000112", '%Y%m%d'),"Korean","Man");  
INSERT INTO	M_USER (email, pw, id, school, dob, lang, gender)
VALUES ("13","13","realisshoman","univ13",str_to_date("20000113", '%Y%m%d'),"Japanese","Man");  
INSERT INTO	M_USER (email, pw, id, school, dob, lang, gender)
VALUES ("14","14","nakedbibi","univ14",str_to_date("20000114", '%Y%m%d'),"English","Woan");  
INSERT INTO	M_USER (email, pw, id, school, dob, lang, gender)
VALUES ("15","15","calmdownman","univ15",str_to_date("20000115", '%Y%m%d'),"Chinese","Transgender"); 
INSERT INTO	M_USER (email, pw, id, school, dob, lang, gender)
VALUES ("16","16","Woosik12","univ16",str_to_date("20000116", '%Y%m%d'),"Korean","No Comment");  


/************************** 게시판 쿼리 *********************************/
INSERT INTO `buddybud`.`B_POST` (`title`, `content`, `url`, `user_no`, `board_type`, `created_at`, `last_modified_at`) VALUES ('2023학년도 겨울 계절수업 교양 교과목 증원 신청 안내', '2023학년도 겨울 계절수업 교양 교과목 증원 신청과 관련하여 다음과 같이 안내드리오니 기한 내 신청하여 주시기 바랍니다.\n\n□ 신청기간: 2023.11.27.(월) ~ 2023.11.29.(수) 24:00\n□ 신청방법: 네이버폼 접속 후 신청(URL: \nhttps://naver.me/FxXLWG8o) □ 증원결과: 2023.12. 4.(월) 10시 학부대학 홈페이지 공지 예정(증원된 T/O로 선착순 방식 수강신청 진행) □ 유의사항 ○ 계절수업은 GLS로 증원 신청 할 수 없으며, 상기 URL 접속 후 신청 ○ 증원 신청은 수강신청을 보장하지 않으며, 최종 증원 T/O 결정 후 선착순 수강신청 필요 * 수강신청(2차): 2023.12. 5.(화) 08:00 ~2023.12. 6.(수) 17:00 ○ 졸업예정자 직권 수강신청의 경우 별도의 절차로 진행(학교 홈페이지 관련 공지 참고 또는 소속 학과 행정실 문의)', 'https://naver.me/FxXLWG8o', '1', 'Notice', '2023-11-27 14:59:18', '2023-11-27 14:59:18');
INSERT INTO `buddybud`.`B_POST` (`title`, `content`, `url`, `user_no`, `board_type`, `created_at`, `last_modified_at`) VALUES ('2023학년도 2학기 기말강의평가 시행 안내 (11. 27.(월) 10:00 ~ 12. 8.(금) 23:00)', '❗❗❗ 참여 안 할 시 성적공시 불이익 있음. ❗❗❗ 2023년 12월 8일 (금) 23시까지  2023학년도 2학기 기말강의평가 및 강의평가 결과 공개를 아래와 같이 시행합니다.  우리 대학 강의 개선을 위한 귀중한 자료로 활용될 강의평가에 학생 여러분의 솔직하고 성의 있는 응답 부탁드립니다.  ❑ 강의평가 기간: 11. 27.(월) 10:00 ~ 12. 8.(금) 23:00  ❑ 대상 과목: 학사과정, 일반대학원, 전문대학원, 특수대학원 모든 개설 과목 ※ 개별연구, 현장실습, 군사학 과목 제외  ❑ 평가방법 가. 인터넷: GLS – 학업영역 – 강의평가 - 금학기강의평가 나. 모바일/태블릿 PC: KINGO M – GLS – 성적/학업영역 – 금학기강의평가  ❑ 유의사항 가. 강의평가기간 중 강의평가를 모두 완료한 학생에 한하여 성적공시기간 내에 성적열람 및 이의신청 가능 ※ 군복무 중 학점인정 수강 중인 군휴학생은 강의평가 참여 불가(추후 성적공시 확인 및 이의신청은 가능) 나. 강의평가(ABEEK 강좌 「학습성과기여도평가」 포함)를 완료하지 못한 학생은 성적공시 최종일에 강의평가 완료 후 하루 동안만 성적열람 및 이의신청 가능 다. 강의평가는 제출 후 수정이 절대 불가하오니, 신중히 평가에 임해주시기 바랍니다. (* 수강과목명, 강의평가문항 및 선택지를 반드시 정확히 확인하고 응답해주시기 바랍니다.) 라. 모든 강의평가는 비공개로 실시되며, 참여자에 대한 익명이 보장됩니다. 마. 학생 스스로 본인의 학습태도를 평가하는 학습자 자기평가문항을 운영하오니 솔직하고 성실하게 문항에 응답해주시기 바랍니다.(추후 금학기성적공시 메뉴를 통해 자기평가점수 확인 가능)  [강의평가 결과 공개]  * 강의평가 결과 공개 제도: 학생들에게 충분한 교육정보를 제공함으로써 합리적인 수업 선택이 가능하도록 강의평가 결과를 공개하는 제도  ❑ 결과 공개 기간: 2024. 1. 15.(월) 10:00 ~ 2024학년도 1학기 수강변경 및 확인기간까지  ❑ 유의사항 가. 최근 1년 간의 강의평가 결과를 ‘GLS-책가방’ 메뉴에서 확인 가능합니다. 나. 학수번호-담당교강사 기준으로 30개 강좌까지 평가결과 조회 가능합니다. (학수번호가 같아도 분반, 담당교강사가 다르면 강의평가 조회가능 횟수가 차감되며, 결과를 조회해서 횟수가 차감된 과목은 결과 공개기간 내 동일하게 다시 조회하여도 횟수가 차감되지 않음)  관련 문의사항은 학사콜센터(Tel. 1811-8585)로 문의 바랍니다.  감사합니다.', 'https://www.skku.edu/skku/campus/skk_comm/notice01.do?mode=view&articleNo=111312&article.offset=0&articleLimit=10', '1', 'Notice', '2023-11-27 13:10:25', '2023-11-27 13:10:25');
INSERT INTO `buddybud`.`B_POST` (`title`, `content`, `user_no`, `board_type`, `created_at`, `last_modified_at`) VALUES ('[2024년도 한국은행 입행설명회]', '[2024년도 한국은행 입행설명회] 일시: 2023년 11월 28일 화요일 19:00~ 진행방식: 오프라인 설명회 장소: 경영관 33B101호(경영관 지하 1층 계단강의실)  2024년도 한국은행 입행을 위한 설명회를 개최합니다. 경제직렬, 경영직렬, 통계직렬, IT직렬 합격자들이 진행할 예정입니다. 간단한 설명 및 Q&A시간을 가진 후, 내년도 입행을 위한 스터디를 결성해드릴 계획입니다. 당장 내년에 지원하시는 게 아니더라도, 학년에 상관없이 한국은행에 관심 있으신 학우 여러분들도 참석하시면 좋은 정보 얻어 가실 수 있을 것이라 생각합니다.  * 구체적인 신청방법과 Q&A 작성은 kingo-m 푸시 알림과 학생인재개발팀 홈페이지를 참고해주시면 감사하겠습니다. * 출석 인정을 위한 행사참가확인서 발급됩니다.  추가로 게시글에 질문하시고 싶으신 것들을 댓글로 달아주시면 댓글을 참고하여 Q&A 시간 준비하도록 하겠습니다.  본 설명회는 한국은행 인사팀이 아닌 24행번 합격자 선배들 주관으로 진행되는 비공식 설명회입니다.  한국은행에 관심을 가지고 있는 학우분들의 많은 참석 부탁드립니다.', '1', 'Notice', '2023-11-22 22:02:01', '2023-11-22 22:02:01');
INSERT INTO `buddybud`.`B_POST` (`title`, `content`, `user_no`, `board_type`, `created_at`, `last_modified_at`) VALUES ('2023.2학기 중대형온라인강의 기말시험 안내', '2023학년도 2학기 중대형온라인강의 기말시험 일정을 아래와 같이 안내하오니 수강생 여러분은 해당 과목 시험에 임해 주시기 바라오며, 시험시간을 준수하여 주시기 바랍니다.  - 시험시간: 배정된 50분 내에서 교강사 자율 시행 (과목 공지사항 확인 必) - 시험방법: 교강사 자율 시행 (과목 공지사항 확인 必)  [온라인시험 유의사항] - 온라인시험 응시 환경: * 본 시험 응시 전 반드시 과목별 모의시험 참여를 통해 개인 응시 환경을 사전 점검하여 주시기 바랍니다. * 응시 기기: 스마트폰, 태블릿 대신 컴퓨터를 사용해주세요. ※ 스마트폰으로 응시할 경우, 메뉴가 보이지 않거나 파일 업로드 오류가 발생할 수 있습니다. * 인터넷 브라우저: 타 인터넷 브라우저가 아닌 Chrome 브라우저로 응시해주세요. * 응시 장소: 카페, 대중교통, 야외 등 인터넷 환경이 불안정한 장소를 피해주세요. * 시험 응시 도중 개인 PC/네트워크 환경으로 인하여 예기치 못한 시스템 오류가 발생할 경우, 오류 화면과 당시 시각을 화면 캡처 또는 사진/동영상 촬영하여 담당 교수님 또는 TA님에게 제출하여 주시기 바랍니다.  [출석시험 유의사항] 1. 고사장 확인: GLS \'수업영역- 학부수강신청- 온라인수업고사실조회\' 메뉴에서 조회 가능(11월 22일부터) 2. 준비물: 필기구, 신분증 필히 지참 (학생증, 주민등록증, 운전면허증, 여권 중 하나) ※ 모바일학생증은 사진이 있어서 본인 확인이 가능한 경우만 인정 ※ 신분증 미지참자는 고사 중 사진촬영 및 채점시 불이익이 있을 수도 있으니 반드시 지참하시기 바랍니다.', '1', 'Notice', '2023-11-08 16:07:18', '2023-11-08 16:07:18');
INSERT INTO `buddybud`.`B_POST` (`title`, `content`, `url`, `user_no`, `board_type`, `created_at`, `last_modified_at`) VALUES ('🔔성균관대 창업지원사업 통합설명회🔔', '이번주 금요일 12월1일 11~12시에 성균관대 창업지원사업 통합설명회가 진행됩니다.\n\n2024년도에 성균관대에서 참여할 수 있는 지원 사업에 대해 설명 드리는 자리이니, 많은 관심 부탁드립니다.\n\n★2024년도 성균관대 창업지원사업 통합설명회★\n\n▷ 대상: 창업에 관심 있는 성균관대 소속 누구나 (학부생, 수료 및 휴학생, 박사후연구원, 교원 등)\n▷ 일정: 2023. 12. 1.(금) 11:00 ~ 12:00\n▷ 장소: 성균관대학교 자연과학캠퍼스 삼성학술정보관 지하1층 오디토리움\n▷ 온라인 사전등록 링크: \nhttps://forms.gle/45MHksQRz5bEjbg98 (당일 현장 등록 가능)', 'https://forms.gle/45MHksQRz5bEjbg98', '2', 'Notice', '2023-11/-29 09:40:25', '2023-11/-29 09:40:25');
INSERT INTO `buddybud`.`B_POST` (`title`, `content`, `url`, `user_no`, `board_type`, `created_at`, `last_modified_at`) VALUES ('[K-디지털플랫폼 AI 경진대회 팀원 1명 모집]', '[K-디지털플랫폼 AI 경진대회 팀원 1명 모집]\n안녕하세요. K-디지털플랫폼 공모전에 함께 하실 1명의 팀원을 모집합니다.\n높은 상 수상을 목표로 하고 있고, 실력 있는 팀원들이 모여있습니다.\nMicrosoft 공식 AI 자격증도 획득할 수 있으니 좋은 기회가 될 것 입니다.\n오늘 29일 오후 3시까지 신청이니 관심 있으신 분은 ‼️오후 2시까지‼️ 연락 부탁드립니다.\n대회 링크: \nhttps://k-hp.co.kr/  <팀 구성> 인공지능(AI) 실무 교육과정 부트캠프 과정 3명 https://www.skku.edu/skku/campus/skk_comm/notice01.do?mode=view&articleNo=105946&article.offset=70&articleLimit=10&srSearchVal=%EC%9D%B8%EA%B3%B5%EC%A7%80%EB%8A%A5 전원 딥러닝, 머신러닝, 자연어처리, 추천시스템, 객체 인식 프로젝트 경험 6월 26일부터 교육, 최종 프로젝트 진행 중  <주제> 자연어처리, GPT API를 활용 전문 강사님과 협의 하에 주제 선정 완료  <권장사항> 자연어처리, AI 프로젝트, 파이썬, 웹 개발 中 1개 경험  <참여방법> 프로젝트 준비 온라인 참여 가능 3명의 팀원은 판교 경기스타트업캠퍼스에서 프로젝트 진행 주말 오전 성남에서 프로젝트 회의 진행 예정  <조건> 12월 12~13일 1박 2일 간 본선 대회장에 참여 가능자 12월 6~7일 MS AI 900 시험 구체적인 사항은 대회 링크를 참여해주세요  <문의링크> https://open.kakao.com/o/sixdr1Uf', 'https://k-hp.co.kr/', '2', 'Notice', '2023-11-29 10:15:56', '2023-11-29 10:15:56');
INSERT INTO `buddybud`.`B_POST` (`title`, `content`, `url`, `user_no`, `board_type`, `created_at`, `last_modified_at`) VALUES ('[DEAR XENA] ✨데이터 서포터즈 모집✨', '[DEAR XENA] 데이터 서포터즈 모집\n\n📢나를 위한 선물을 해보신 적 있나요?\n\n내년 상반기 출시 예정인 「DEAR XENA : 소중한 나」가 데이터 서포터즈를 모집합니다. 오직 나를 위한 선물 패키지를 제작하는 「DEAR XENA」는 다양한 성격, 성향, 가치관, 커리어를 가진 여러분들의 취향 데이터를 수집하고 있습니다🫧\n참여해주신 분들 중 성의 있게 작성해주신 분을 선정하여 「원박스 만들기 체험권」을 증정해드려요!\n\n평소 나를 위한 선물을 계획하셨던 분들, 나에게는 무관심했던 분들 모두 많은 관심과 참여 바랍니다.\n\n소중한 나를 위해 선물을 해주세요!🍀\n\n🧃상품 안내\n•「원박스」 만들기 체험권(10명)\n: 나만의 선물 상자부터 하나뿐인 편지 작성, 성향에 맞는 선물 추천까지\n•아메리카노 기프티콘(20명) 증정\n\n🧃참여방법\n1. 하단 링크에 접속하여 성의 있게 응답해주세요.\n2. 선정자에게는 작성하신 연락처로 연락이 갑니다.\n\n🧃모집 일정\n•기간: 11/28(화) ~ 12/9(토)\n\n*본 클래스는 \'원박스 만들기\' 일일 클래스로 3000원의 체험비가 소요됩니다.\n\n———\n\n📎\nhttps://smore.im/form/c2EN6nJaPD', 'https://smore.im/form/c2EN6nJaPD', '2', 'Notice', '2023-11-28 22:26:09', '2023-11-28 22:26:09');
INSERT INTO `buddybud`.`B_POST` (`title`, `content`, `url`, `user_no`, `board_type`, `created_at`, `last_modified_at`) VALUES ('설문조사✨ 싸이버거 증정', '🎓대학(원)생·사회초년생 🏠부동산 계약 경험, 이해도에 관한 설문\n\n안녕하세요, 성균관대학교 SeTA(Social entrepreneurship Team Academy) 14기 \'NEXT NEST\' 팀입니다.\n대학(원)생·사회초년생 부동산 계약 과정의 불편과 어려움을 해소하는 비즈니스 모델을 구상 중에 있습니다.\n여러분이 응답해주신 내용은 보다 효과적이고 실질적인 도움을 제공할 수 있는 비즈니스 모델 개발에 활용될 예정입니다.\n\n설문 응답 기간: ~11.30(목) 23:59\n설문 소요 시간: 약 3분 내외\n설문 링크: \nhttps://forms.gle/gactubT71TkbLPoL6  설문에 참여해 주신 분들 중 추첨을 통해 5분께 커피 상품권☕을, 양질의 정보를 제공해주신 성실 응답자 2분께 싸이버거 세트🍔를 드릴 예정입니다.', 'https://forms.gle/gactubT71TkbLPoL6', '2', 'Notice', '2023-11-28 20:08:36', '2023-11-28 20:08:36');
INSERT INTO `buddybud`.`B_POST` (`title`, `content`, `url`, `user_no`, `board_type`, `created_at`, `last_modified_at`) VALUES ('[보드게임 동아리 보동보동]', '[보드게임 동아리 보동보동]\n\n보드게임 동아리 보동보동 입니다 !\n보드게임을 즐기고자 하는 모든사람들이 지원이 가능합니다 🎲\n살면서 이정도 보드게임은 해봐야죠 🎲\n왕초보, ssap고수, 고인물, 마이너스의손 다 환영입니다 ✋🏻\n방학기간 친구도 사귀고 즐겨보시죠 ✋🏻\n\n[상세 일정]\n- 모집기간 : ~ 23년 12월 24일\n- 운영기간 : 23년 12월 25일 ~ 24년 2월\n주에 1회 공식 활동이 있으며 한달에 한번은 참여 필수입니다.\n\n[공식활동]\n1회차 - 부루마블\n2회차 - 루미큐브\n3회차 - 스플렌더\n4회차 - 달무티\n5회차 - 뱅\n6회차 - 다빈치코드\n7회차 - 각종 미니 보드게임 (잼블로, 할리갈리 등)\n8회차 - 자유게임\n9회차 - 쫑파티\n\n[부가활동]\n친목 및 번개 활동 가능 ❤️\n\n▶️ 바로 지원 ◀️\nhttps://docs.google.com/forms/d/1L2vFAiBEiHFjZlLZOUCl2QvkeJH4tKfBfFLKT2i2RdQ/edit  ▶️동아리장(문의)◀️ 건대생🎲 kakao. megi_megi T. 010-4829-8635', 'https://docs.google.com/forms/d/1L2vFAiBEiHFjZlLZOUCl2QvkeJH4tKfBfFLKT2i2RdQ/edit', '3', 'Notice', '2023-11-28 17:30:45', '2023-11-28 17:30:45');
INSERT INTO `buddybud`.`B_POST` (`title`, `content`, `url`, `user_no`, `board_type`, `created_at`, `last_modified_at`) VALUES ('Draw:in_바쁜 일상 속 힐링, 온라인 전시회', 'Draw:in_바쁜 일상 속 힐링, 온라인 전시회\n\n과제와 시험에 치여 사는 일상 속, 낙서를 하며 힐링했던 경험이 있으신가요?\nDraw:in은 끄적끄적 그리고 버리는 낙서를 넘어 각자 원하는 작품을 그리고 온라인 전시회를 열어보고자 만든 동아리입니다.\n\n힐링을 위한 그림인 만큼 일상에서 잠시 벗어나 온전히 나에게 집중하는 시간을 가지며 나에 대한 그림을 그리고 노션을 통해 온라인 전시회를 열어보고자 합니다.\nDraw:in은 각자 편한 도구로 그림을 그리고 서로의 그림에 대한 이야기를 나누며 함께 온라인 전시장을 꾸미고 작품을 전시하는 것을 최종 목표로 하고 있습니다.\n\nDraw:in은 그림만 그리거나 전시회 관람을 가는 동아리가 아닙니다!\n\n▫모집 대상\n(1) 그림 그리는 것을 좋아하시는 분\n(2) 학교 생활을 하며 과제, 시험 등 여러 가지 스트레스로 지치신 분\n(3) 내가 좋아하는 그림을 한 번쯤 전시해보고 싶었던 분\n\n▫모집 기간\n: 11.27(월) ~ 12.15(금)\n\n▫활동 기간\n: 약 2~3개월, 격주에 1~2회 만남 예정\n\n✔꼭 그림을 잘 그리는 분들이 아니어도 얼마든지 편하게 지원 가능합니다! 운영진 4명 중 3명이 비전공자입니다ㅎㅎ\n\n▫커리큘럼(진행 속도에 따라 변동 가능)\n0회차 : 조별 OT\n1회차 : 프로필 카드 작성, 캐릭터 그리기(내면/외면)\n2회차 : 재료 탐구 시간(물감, 색연필, 오일파스텔, 캔버스 등)\n3회차 : 밑그림 작업\n4회차 : 채색 단계\n5회차 : 완성 단계\n\n▫지원 링크\nhttps://forms.gle/uC2HMGhdmZscH9FX7  ▫️인스타 @draw_in_01 https://www.instagram.com/draw_in_01/', 'https://forms.gle/uC2HMGhdmZscH9FX7', '3', 'Notice', '2023-11-27 22:46:17', '2023-11-27 22:46:17');
INSERT INTO `buddybud`.`B_POST` (`title`, `content`, `url`, `user_no`, `board_type`, `created_at`, `last_modified_at`) VALUES ('20대 중후반 여행 동아리 ☃️윈터☃️', '20대 중/후반 여행 동아리 ☃️윈터☃️에서 1기 신입 부원을 모집합니다!\n\n❄️동아리 소개❄️\n☃️윈터☃️는 2023년 겨울에 20대 중/후반 23~29살 또래들과 20대를 재밌는 추억으로 채우고자 만들어진 여행 동아리입니다. 20대 끝자락을 좋은 사람들과 좋은 시간으로 채우고 싶은 많은분들의 지원을 기다립니다!\n*여행 동아리인 만큼 운전자(특히 자차 보유자) 우대합니다!\n\n❄️활동 지역❄️\n여행은 원하는곳 어디든지! 번개는 주로 서울내 지역에서 열릴 예정입니다.\n\n❄️활동 기간❄️\n2023년 11월 중순~2024년 3월 말\n\n❄️운영 방식❄️\n정기 여행은 톡방에서의 투표를 통해 12월부터 3월까지 한달에 최소 두번 계획될 예정입니다! 1달에 최소 1번 참여는 필수입니다.\n정기 여행은 같은 날 투표하신 분들끼리 운영진이 조를 짜드립니다. 각자 조에서 여행 중 어떤 활동을 할지는 자유롭게 조원들끼리 정하시고, 동아리 회장에게 말씀해주시면 됩니다.\n11월은 동아리원들끼리 여행 전 친해지는 기간으로, 정기 여행없이 번개 모임과 OT로만 진행될 계획입니다!\n\n❄️활동 보증금❄️\n❕3만원\n활동 보증금 제도는 정기 여행에 꾸준한 참여를 촉진하도록 하기 위해 실시합니다. 한달에 최소 1번 참여가 지켜지지 않을 때마다 1만원이 차감되며, 남은 금액은 활동 종료 후 반환해 드립니다. 차감된 보증금은 운영진 수고비로 지출될 예정입니다.\n▫️1달 참여: 0원 반환, 2달 참여: 1만원 반환, 3달 참여: 2만원 반환, 4달 참여: 3만원 전액 반환\n예) 12월에만 2번 참여 후 잠수➡️0원 반환, 12월 1회, 2월 1회 참여➡️1만원 반환\n\n❄️모집 일정❄️\n[지원서 제출 기간] 11/16(목)~11/22(수) 23:59\n❕면접 없습니다❕=>지원서 꼼꼼히 작성 부탁드립니다😊\n[최종 합격자 발표] 11/17(금)~11/23(목) 23:59까지 실시간으로 합격 문자 발송\n[톡방 초대] 11/23(목)\n[첫 모임(OT)] 11/24(금), 11/25(토), 11/26(일) 중 동아리원 투표로 결정될 예정\n\n❄️모집 대상❄️\n서울 내 4년제 대학교 재학/휴학/졸업 01, 00, 99, 98, 97, 96, 95년생 누구나!\n(23살~29살)\n\n❄️모집 인원❄️\n20~30명 (성비는 1:1로 맞출 예정이고, 총 인원은 지원자 수에 따라 추후 변동될 수 있습니다.)\n\n❄️지원폼❄️\nhttps://forms.gle/AAsNGHFJmodLstfV6  ❄️ 각종 문의 사항은 하단의 카카오톡 오픈채팅 링크 이용 부탁드립니다. ☃️Winter☃️ https://open.kakao.com/o/s4H1rBSf  ❄️운영주체❄️ 본 동아리는 대학생들에 의해 자치적으로 운영되며 정치, 종교, 시민단체와 전혀 관련이 없습니다. 운영진은 23살 이화여대생 두명과 25살 한양대생으로 구성되어있습니다!', 'https://forms.gle/AAsNGHFJmodLstfV6', '3', 'Notice', '2023-11-22 20:05:10', '2023-11-22 20:05:10');
