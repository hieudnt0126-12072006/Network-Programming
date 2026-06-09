# Network Programming
Nhóm 7 
MÃ DỰ ÁN: UDM_17
- 1.Đề xuất Dự án: Lập trình game cờ Caro.
- Bạn muốn làm gì: Nhóm chúng em muốn phát triển một ứng dụng game cờ caro nhiều người chơi trên desktop sử dụng kỹ thuật lập trình mạng. Ứng dụng cho phép nhiều người chơi kết nối qua mạng LAN/Internet và chơi caro thời gian thực bằng mô hình Client–Server và giao tiếp TCP Socket.
+ Tính năng nhóm muốn có:
- Chơi nhiều người qua LAN/Internet. 
- Đồng bộ nước đi thời gian thực giữa các người chơi. 
- Hệ thống đăng nhập và đăng ký. 
- Tính năng tạo phòng và tham gia phòng. 
- Ghép trận giữa người chơi. 
- Hệ thống chat trong game. 
- Quản lý lượt chơi. 
- Xác định thắng/thua/hòa. 
- Giao diện desktop GUI đơn giản và thân thiện.
- Lưu lịch sử trận đấu (tính năng nâng cao tùy chọn).
+ Công nghệ sử dụng: 
- Ngôn ngữ lập trình phù hợp cho ứng dụng Desktop (Java)
- Kiến trúc Client-Server 
- Sử dụng giao thức kết nối Socket (TCP/IP)
- Framework giao diện: JavaFX hoặc Swing
- Cơ sở dữ liệu: MySQL/ SQLSever
- Quản lý source code: Github
- Quản lý dự án: Jira
--> Mục tiêu cuối cùng: Nhóm mong muốn xây dựng được một game caro desktop nhiều người chơi hoàn chỉnh với khả năng giao tiếp thời gian thực ổn định, quản lý phòng chơi và gameplay tương tác thông qua kết nối mạng, không chạy trên trình duyệt web, đáp ứng tốt nhu cầu trao đổi dữ liệu nội bộ.
- 2.Kế hoạch thực hiện (Mỗi 2 tuần)
+ Giai đoạn 1 (Tuần 1 - Tuần 2): 
- Nghiên cứu kiến trúc TCP Socket(ServerSocket và Socket trong Java).
-	Thiết kế cấu trúc gói tin (Message Protocol) dạng String hoặc JSON để Client-Server hiểu nhau (ví dụ: MOVE;x;y, CHAT;content).
-	Tạo Repository GitHub, setup quy tắc chia nhánh (Branching strategy như main, develop, feature/...).
-	Phác thảo giao diện các màn hình (Đăng nhập, Phòng chờ, Bàn cờ) bằng công cụ UI kéo thả của Swing.
+ Giai đoạn 2 (Tuần 3 - Tuần 4): 
-	Xây dựng Server đa luồng (Multi-threading): Server mở cổng, chấp nhận 2 Client kết nối vào và tạo thành một cặp đấu.
-	Hiện bàn cờ 20*20 lên Client. Xử lý đồng bộ nước đi thời gian thực.
-	Xây dựng chức năng Chat trong trận đấu bằng Socket.
+ Giai đoạn 3 (Tuần 5 - Tuần 6): 
-	Cài đặt và thiết kế Database MySQL (Bảng Users, Matches).
-	Kết nối Server với MySQL qua JDBC. Hoàn thiện tính năng Đăng ký / Đăng nhập ở màn hình Client.
-	Xây dựng logic Phòng chơi (Lobby): Người chơi có thể xem danh sách phòng, tạo phòng mới hoặc bấm "Ghép trận nhanh".
-	Xử lý quản lý lượt chơi (đổi lượt X/O) trên Server để chống hack (Client không thể đi 2 nước liên tiếp).
+ Giai đoạn 4 (Tuần 7 - Tuần 8): 
-	Cài đặt thuật toán kiểm tra thắng/thua/hòa (quét 5 ô liên tiếp theo hàng ngang, dọc, chéo) chạy trên Server để đảm bảo tính chính xác.
-	Xử lý các trường hợp mất kết nối đột ngột (đứt mạng, tắt app ngang xương). Xử thua người mất kết nối.
-	Lưu lịch sử trận đấu vào SQL (tính năng nâng cao).
-	Kiểm thử chéo giữa các máy qua mạng LAN, sửa lỗi (Bug).
- 4.Thông tin thành viên
-	Họ và tên: Đào Nguyễn Thiện Hiếu
-	(ID): 062206000126
-	GitHub Account ID: hieudnt0126-12072006
-	Họ và tên: Nguyễn Trần Đình Hiệu
-	(ID): 064206000109
-	GitHub Account ID: hieuclet249-bot
-	Họ và tên: Phan Thành Khoa
-	(ID): 054206001619
-	GitHub Account ID: 1PhanzThanhzKhoa1
-	Họ và tên: Nguyễn Trọng Thế
-	(ID): 064206012295
-	GitHub Account ID: thetrongtran2409
-	Họ và tên: Ngô Châu Huy
-	(ID): 052205004719
-	GitHub Account ID: Chau-Huy
-	Họ và tên: Nguyễn Huy Long Hồ
-	(ID): 089206020755
-	GitHub Account ID: Longho2606

