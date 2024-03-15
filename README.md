# 경기대학교 컴퓨터 공학부 심화 캡스톤
## 공구친구












# 데이터 베이스 다이어그램

## **Field Description**

- Rooms Table

방 번호: 방 고유 번호
방 제목: 방 텍스트 제목
방 참여자/방장: 방 참여자 정보, 방장 여부(외부 테이블)
방 분류: 택시, 공동 구매, 넷플릭스 등등, int 타입 번호로 관리
채팅 내용: 말 그대로 채팅 내용 (외부 테이블)
모집인원: 남은 모집 인원
현재인원: 현재 모집된 인원

- Taxi Recurit Exclusive (Rooms table 외부 테이블)

출발지: 출발지가 어딘지 Text 형태로 저장
(point로 저장하여 위도 경도 데이터를 구글 지도 API와 연동하는 것 고려)
목적지: 동일 
예상 비용: 예상 택시비
~~해시태그 (보류, 외부 테이블)~~

## **구현**

1. 모집 채팅방 데이터 테이블

```sql
CREATE TABLE Rooms (
	RoomID INT AUTO_INCREMENT PRIMARY KEY,
	RoomTitle VARCHAR(255),
	RoomCategory INT, -- 방 분류 (택시, 공동구매 등)
	RecruitNum INT, -- 모집 인원
	CurrentNum INT, -- 현재 인원
);
```

```sql
CREATE TABLE Rooms (
	RoomID INT AUTO_INCREMENT PRIMARY KEY,
	RoomTitle VARCHAR(255),
	RoomCategory INT, -- 방 분류 (택시, 공동구매 등)
	RecruitNum INT, -- 모집 인원
	CurrentNum INT, -- 현재 인원
);
```

1. 채팅방 메세지 테이블

```sql
CREATE TABLE ChatMessages (
	MessageID INT AUTO_INCREMENT PRIMARY KEY,
	RoomID INT, -- 룸 id (외래키)
	SenderID INT, -- 전송자 id
	MessageText TEXT, -- 텍스트 메세지 내용
	Timestamp DATETIME, -- 전송시간, 전송 시간 기준으로 sort하여 채팅창 구성
	FOREIGN KEY (RoomID) REFERENCES Rooms(RoomID)
);
```

1. 채팅방 멤버 테이블

```sql
CREATE TABLE RoomMembers (
  RoomMemberID INT AUTO_INCREMENT PRIMARY KEY,
  RoomID INT, -- 외래키
  UserID INT, -- 참여 유저 정보
  JoinDate DATETIME, -- 참여 날짜
  IsRoomOwner BOOLEAN NOT NULL DEFAULT FALSE, -- 방장 여부
  FOREIGN KEY (RoomID) REFERENCES Rooms(RoomID),
  FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
```

1. 택시 채팅방 전용 기능
