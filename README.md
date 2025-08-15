<img width="155" height="143" alt="스크린샷 2025-08-15 181548" src="https://github.com/user-attachments/assets/f90b2c43-4d19-4365-b7ba-eac97685c21e" />


# 나도가이드

진행 기간: 2024년 3월 30일 → 2024년 6월 27일<br>
태그: SpringBoot<br>
한 줄 설명: 졸업 작품<br>
개발 인원(역할): 1인 개발 / 전체 기획 및 프론트·백엔드 구현<br>


<div>&nbsp;</div>
<div>&nbsp;</div>


### **📌 개요 및 사용 기술**

---

본 프로젝트는 여행사의 패키지 투어 한계를 보완하고, 사용자가 직접 맞춤형 투어를 기획·예약할 수 있는 플랫폼 ‘나도가이드’를 개발한 것입니다.

서울시 공공데이터를 활용한 실시간 인구 정보, 문화·이벤트 정보 제공 기능과 함께 투어 등록·예약, 마이페이지, 게시판, 실시간 채팅 등을 구현하였습니다.

- **Backend**: Spring Boot (Java)
- **Frontend**: HTML, CSS, JavaScript, Thymeleaf
- **Security**: Spring Security (세션 기반 인증/인가)
- **Database**: MariaDB
- **Server**: Apache Tomcat
- **Cloud**: AWS EC2
- **API**: 서울시 공공데이터 API (OpenFeign 연동)
- **IDE**: IntelliJ IDEA

<div>&nbsp;</div>
<div>&nbsp;</div>

### 🤔 주제 선정

---

예전에 가족과 함께 패키지여행을 다녀온 적이 있습니다.

일정이 모두 정해져 있어 자유롭게 움직이기 어려운 불편함이 있었지만, 반대로 가이드가 안내하는 일정에 맞춰 움직이는 편리함도 느꼈습니다.

이 두 가지 상반된 경험을 바탕으로, **사용자가 원하는 일정과 코스를 직접 선택하면서도 가이드처럼 편리하게 안내받을 수 있는 서비스**를 만들면 좋겠다는 생각을 하게 되었고, 이를 계기로 ‘나도가이드’를 기획하게 되었습니다.

<div>&nbsp;</div>
<div>&nbsp;</div>

### 📐 개발 환경 및 시스템 구성

---

![작품_](https://github.com/user-attachments/assets/eb5415e9-71d4-47fd-a62d-72bdaabf5347)


<나도가이드 시스템 구성도>

백엔드는 Spring Boot 기반으로 회원 관리, 투어 등록·예약, 실시간 채팅, 마이페이지, 게시판 등 핵심 기능을 구현하였으며, Spring Security를 통해 세션 기반 인증·인가를 처리하였습니다.

프론트엔드는 HTML5, CSS/BootStrap, JavaScript를 활용하여 UI를 구현하였고,

서울시 공공데이터 API를 통해 실시간 인구 정보와 문화·이벤트 정보를 제공하였습니다.

데이터는 MariaDB에 저장하여 사용자 정보, 투어 정보, 예약 내역, 채팅 기록, 일정 관리 등을 관리하였으며,

반복적으로 조회되는 데이터(예: 인기 투어 목록, 최근 이벤트 정보)는 Redis 캐를 활용하여 조회 속도를 향상시켰습니다.

전체 서비스는 AWS EC2 환경에서 Apache Tomcat을 이용해 배포하였습니다.

<div>&nbsp;</div>
<div>&nbsp;</div>

### 🚀 주요 기능

---

나도가이드는 다음과 같은 주요 기능들로 구성되어 있습니다.


**🗺️ 투어 등록 및 예약**

---

<img width="833" height="491" alt="5" src="https://github.com/user-attachments/assets/66f2bebc-ea54-4e2a-9688-ebf0534cc88a" />
<img width="1072" height="544" alt="7" src="https://github.com/user-attachments/assets/50322f93-5510-4329-a412-7f95b9e8b035" />


사용자는 직접 투어를 등록하고, 다른 사용자가 해당 투어를 예약할 수 있습니다.

투어 등록 시에는 제목, 장소, 날짜/시간, 최대 참가 인원, 소요 시간, 준비물, 카테고리, 알림사항 등을 입력할 수 있습니다.

등록된 투어 페이지에서는 현재 예약 인원, 남은 자리, 투어 정보, 준비물, 카테고리, 알림사항이 표시되며, 사용자는 예약하기 버튼을 통해 참여를 신청할 수 있습니다.

<div>&nbsp;</div>

🪪 **마이페이지**

---

<img width="825" height="593" alt="3" src="https://github.com/user-attachments/assets/f21b0010-a21f-4307-b60e-6a552fe44e07" />


마이페이지에서는 회원 정보와 투어 관련 활동 내역을 확인하고 관리할 수 있습니다.

회원 정보 영역에서는 아이디, 이름, 이메일, 닉네임, 주소 등의 정보를 확인할 수 있으며, 닉네임 변경과 회원 탈퇴 기능을 제공합니다.

투어 정보 영역에서는 사용자가 예약한 투어와 등록한 투어 목록이 표시되며, 등록한 투어에 대해서는 수정과 삭제가 가능합니다.

또한, 특정 투어에 예약한 사용자 목록을 확인할 수 있고, 찜한 투어 목록도 함께 표시됩니다.

투어 예약, 취소, 수정 시 관련 내용이 사용자 이메일로 자동 전송되어 변경 사항을 즉시 확인할 수 있습니다.

<div>&nbsp;</div>

💬 **게시판**

---

<img width="812" height="593" alt="8" src="https://github.com/user-attachments/assets/f042a412-9c86-42d9-86d8-7d1b1077b421" />
<img width="635" height="593" alt="9" src="https://github.com/user-attachments/assets/057eb26b-6272-43cb-bd66-749dc258c8bd" />


사용자가 자유롭게 글과 댓글을 작성·열람할 수 있는 게시판 기능입니다.

게시글 수정, 삭제와 댓글 작성이 가능하며, 페이지네이션을 지원합니다.

<div>&nbsp;</div>

**🗓️ 일정 관리**

---

<img width="1061" height="592" alt="4" src="https://github.com/user-attachments/assets/17afbc67-0763-4f33-b5df-d8f0c9bf97a2" />


예약한 투어 일정을 달력 형태로 확인할 수 있는 기능입니다.

또한 사용자가 직접 일정을 추가하여 개인적인 일정 관리에도 활용할 수 있습니다.

<div>&nbsp;</div>

**💬 / 🗨️ 실시간 채팅**

---

<img width="685" height="547" alt="10" src="https://github.com/user-attachments/assets/c7ce281b-e767-4017-92a0-31fdfa9de113" />

투어 참가자 간 소통을 위한 실시간 채팅 기능입니다.

WebSocket 기반으로 구현하여 실시간 메시지 송수신이 가능합니다.

<div>&nbsp;</div>

**🎶 서울시 문화·행사 정보**

---

<img width="1280" height="593" alt="11" src="https://github.com/user-attachments/assets/40ace1d0-281c-49c0-8dc3-1f11e18add1c" />
<img width="1280" height="594" alt="12" src="https://github.com/user-attachments/assets/76d7737f-f693-4a86-beb3-b3791d49a0f4" />


서울시 공공데이터 API를 활용하여 문화공간 정보와 행사 일정을 제공합니다.

사용자는 각 항목의 위치, 일정, 연락처, 이용요금 등의 세부 정보를 확인할 수 있습니다.

<div>&nbsp;</div>
<div>&nbsp;</div>

### 📈 향후 발전 방향

---

- **투어 추천 알고리즘 적용**
    
    사용자의 관심사, 이전 투어 예약 내역, 선호 지역 데이터를 기반으로 맞춤형 투어를 추천하는 기능을 추가할 계획입니다.
    
- **모바일 앱 버전 개발**
    
    반응형 웹에서 나아가 전용 모바일 앱을 개발하여 위치 기반 알림, 즉시 채팅, 오프라인 일정 관리 기능을 강화하고자 합니다.
    
- **다국어 지원 확대**
    
    한국어 외에 영어, 일본어, 중국어 등 다국어를 지원하여 외국인 관광객도 서비스를 쉽게 이용할 수 있도록 개선할 예정입니다.

<div>&nbsp;</div>
<div>&nbsp;</div>

### 💭 회고 및 소감

---

이 정도 규모로 끝까지 완성한 프로젝트는 처음이라, 중간중간 구현이 막히는 부분도 있었지만 1학년 때 K-PaaS 공모전 나가면서 고생했던 경험이 이번에 꽤 도움이 된 것 같음.

기능이 많다 보니 일정이 빡빡했는데, 그래도 하나씩 완성해가는 과정이 재미있었음. 특히 채팅, 달력 일정 관리, 이메일 전송 같은 건 처음엔 복잡해 보여서 부담됐지만, 막상 해보니 생각보다 잘 풀려서 뿌듯했음.
