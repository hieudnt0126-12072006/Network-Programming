1. Mục tiêu của Stress test / Performance test là đánh giá mức độ sử dụng tài nguyên hệ thống của ứng dụng Caro Online được xây dựng bằng Java Swing và Socket TCP, bao gồm:
- Mức sử dụng CPU.
- Mức sử dụng RAM.
- Khả năng hoạt động ổn định khi nhiều người chơi kết nối.
- Đánh giá khả năng đáp ứng của hệ thống trong điều kiện tải thông thường và tải cao.
2. Performance Test
- Môi trường kiểm thử:
Thành phần                    Thông tin
Hệ điều hành                  Windows 10/11
Ngôn ngữ lập trình            Java
IDE                           IntelliJ IDEA
Giao thức                     TCP Socket
Công cụ đo                    Windows Task Manager
Mô hình triển khai            01 Server + 02 Client
- Quá trình:
Khởi động Server.
Khởi động 02 Client.
Đăng nhập hai người chơi.
Ghép trận thành công.
Thực hiện trao đổi nước đi liên tục.
Sử dụng chức năng chat trong trận đấu.
Theo dõi tài nguyên hệ thống bằng Task Manager.
- Kết quả CPU:
 CPU-test.png**
Kết quả ghi nhận:
| Thông số        | Giá trị |
|-----------------|---------|
| CPU Utilization | 15%     |
| CPU Speed       | 2.60 GHz|
| Processes       | 156     |
| Threads         | 1983    |
| Handles         | 67245   |
- Nhận xét:
Trong quá trình vận hành với 02 Client và 01 Server, mức sử dụng CPU duy trì khoảng 15%.
Điều này cho thấy ứng dụng Caro Online tiêu thụ rất ít tài nguyên xử lý do:
Chỉ trao đổi dữ liệu tọa độ.
Không xử lý đồ họa phức tạp.
Không sử dụng AI.
Không truy vấn cơ sở dữ liệu lớn.
Hệ thống hoạt động ổn định và không xuất hiện hiện tượng nghẽn CPU.
- Kết quả RAM:
 RAM-test.png**
Kết quả ghi nhận:
| Thông số        | Giá trị |
|-----------------|---------|
| RAM sử dụng     | 2.6 GB  |
| Tổng RAM        | 8 GB    |
| Tỷ lệ sử dụng   | 33%     |
| Available       | 5.2 GB  |
| Cached          | 2.3 GB  |
- Nhận xét:
Mức sử dụng bộ nhớ chỉ chiếm khoảng 33% tổng dung lượng RAM của hệ thống.
Ứng dụng Caro Online không yêu cầu bộ nhớ lớn vì:
Dữ liệu bàn cờ nhỏ.
Tin nhắn chat có dung lượng thấp.
Không lưu trữ dữ liệu đa phương tiện.
Qua kiểm thử cho thấy ứng dụng có thể hoạt động tốt trên các máy tính cấu hình phổ thông.
3. Stress Test
- Mục đích:
Kiểm tra khả năng hoạt động ổn định của hệ thống Caro Online khi số lượng người chơi kết nối đồng thời tăng cao, từ đó đánh giá giới hạn chịu tải của Server và mức sử dụng tài nguyên hệ thống.
- Môi trường kiểm thử:
Thành phần	          Thông tin
Hệ điều hành	        Windows 10
Ngôn ngữ lập trình	  Java
Công nghệ	            Java Swing, TCP Socket
IDE	                  IntelliJ IDEA
Công cụ đo	          Windows Task Manager
Mô hình kiểm thử	    01 Server + nhiều Client
- Quá trình:
Để tạo tải cho hệ thống, nhiều tiến trình Java được khởi chạy đồng thời, bao gồm:
01 tiến trình CaroServer.
Nhiều tiến trình CaroClient.
Các Client thực hiện:
Đăng nhập hệ thống.
Ghép trận.
Gửi nhận nước đi liên tục.
Trao đổi tin nhắn chat.
Trong quá trình kiểm thử, Task Manager được sử dụng để theo dõi mức sử dụng CPU và RAM của hệ thống.
- Kết quả:
 Stress test.png**
- Thông số thu được:
| Thông số        | Giá trị |
|-----------------|---------|
| CPU Utilization | 58%     |
| CPU Speed       | 2.60 GHz|
| RAM sử dụng     | 4.6 GB  |
| RAM tổng        | 8.0 GB  |
| Memory Usage    | 58%     |
| Processes       | 218     |
| Threads         | 2765    |
| Handles         | 102345  |
- Các tiến trình Java đang hoạt động:
Tiến trình	    RAM
CaroServer	    642 MB
CaroClient 1    402 MB
CaroClient 2    395 MB
CaroClient 3    387 MB
CaroClient 4    358 MB
CaroClient 5    330 MB
- Nhận xét:
Kết quả kiểm thử cho thấy khi số lượng Client tăng lên đáng kể, hệ thống vẫn duy trì khả năng hoạt động ổn định.
Mức sử dụng CPU đạt khoảng 58%, chứng tỏ Server vẫn còn khả năng xử lý thêm các yêu cầu từ người dùng mà chưa rơi vào trạng thái quá tải.
Dung lượng RAM sử dụng đạt 4.6 GB trên tổng số 8 GB bộ nhớ hệ thống, tương đương 58%. Điều này cho thấy ứng dụng có khả năng quản lý bộ nhớ hiệu quả mặc dù có nhiều kết nối đồng thời.
Bên cạnh đó:
Không xuất hiện hiện tượng treo ứng dụng.
Không xảy ra mất kết nối giữa Client và Server.
Các thao tác đánh cờ và chat vẫn được thực hiện bình thường.
Thời gian phản hồi của hệ thống vẫn đảm bảo yêu cầu.