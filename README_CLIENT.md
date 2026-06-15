# Client Socket Implementation Documentation

## Tổng quan
File `CaroClient.java` chịu trách nhiệm xử lý kết nối Socket phía người chơi (Client).

## Chức năng chính

### 1. Kết nối đến Server
- **Phương thức**: `startConnection(String name)`
- **Mô tả**: Thiết lập kết nối TCP Socket đến Server tại `127.0.0.1:8080`
- **Quy trình**:
  1. Tạo Socket kết nối đến Server
  2. Khởi tạo BufferedReader và PrintWriter để đọc/ghi dữ liệu
  3. Gửi tin nhắn LOGIN với tên người dùng
  4. Khởi động luồng lắng nghe tin nhắn từ Server

### 2. Bắt tín hiệu từ Server (Nhận tin nhắn)
- **Phương thức**: `listenServer()` (chạy trong luồng riêng)
- **Mô tả**: Lắng nghe liên tục các tin nhắn từ Server
- **Các loại tin nhắn được xử lý**:
  - `START_GAME`: Server thông báo bắt đầu trận đấu, hiển thị bàn cờ
  - `MOVE`: Nhận tọa độ nước đi từ đối thủ, cập nhật lên màn hình
  - `CHAT`: Nhận tin nhắn chat từ đối thủ
  - `QUIT`: Xử lý khi đối thủ thoát game

### 3. Gửi tọa độ nước đi lên Server
- **Phương thức**: `sendMessage(Message msg)`
- **Mô tả**: Gửi tin nhắn đến Server dưới dạng JSON (sử dụng Gson)
- **Sử dụng**: GameFrame gọi phương thức này khi người chơi đánh một nước
- **Ví dụ**: Gửi tin nhắn MOVE với tọa độ (x, y) và symbol (X/O)

### 4. Nhận nước đi đối thủ và cập nhật màn hình
- **Quy trình**:
  1. `listenServer()` nhận tin nhắn MOVE từ Server
  2. Trích xuất tọa độ (x, y) và symbol từ tin nhắn
  3. Gọi `gameFrame.updateOpponentMove(x, y, symbol)` để cập nhật GUI
  4. GameFrame cập nhật bàn cờ và kiểm tra thắng/thua

## Các phương thức quan trọng

### `sendMessage(Message msg)`
```java
public void sendMessage(Message msg) {
    if (out != null && isConnected) {
        out.println(gson.toJson(msg));
    }
}
```
- Gửi tin nhắn đến Server
- Chuyển đổi Message thành JSON bằng Gson
- Kiểm tra trạng thái kết nối trước khi gửi

### `listenServer()`
```java
private void listenServer() {
    // Lắng nghe liên tục tin nhắn từ Server
    // Phân loại và xử lý từng loại tin nhắn
}
```
- Chạy trong luồng riêng (Thread)
- Đọc tin nhắn từ BufferedReader
- Phân tích JSON và chuyển đổi thành Message object
- Cập nhật GUI thông qua SwingUtilities.invokeLater()

### `reconnect()`
```java
public boolean reconnect() {
    // Đóng kết nối cũ
    // Kết nối lại đến Server
    // Gửi lại tin nhắn đăng nhập
    // Khởi động lại luồng lắng nghe
}
```
- Thử kết nối lại khi mất kết nối
- Đợi 1 giây trước khi kết nối lại
- Gửi lại tin nhắn LOGIN sau khi kết nối lại

## Luồng dữ liệu

### Gửi nước đi
1. Người chơi click vào ô trên bàn cờ (GameFrame)
2. GameFrame tạo Message MOVE với tọa độ (x, y)
3. GameFrame gọi `client.sendMessage(msg)`
4. CaroClient chuyển đổi Message thành JSON
5. Gửi JSON qua PrintWriter đến Server

### Nhận nước đi đối thủ
1. Server gửi tin nhắn MOVE đến Client
2. `listenServer()` nhận tin nhắn qua BufferedReader
3. Phân tích JSON thành Message object
4. Gọi `gameFrame.updateOpponentMove(x, y, symbol)`
5. GameFrame cập nhật GUI với nước đi của đối thủ

## Xử lý lỗi
- **Mất kết nối**: Đặt `isConnected = false`, hiển thị thông báo lỗi
- **Không kết nối được**: Hiển thị thông báo lỗi trên LoginFrame
- **Lỗi đóng kết nối**: Ghi log lỗi, không crash ứng dụng

## Cấu hình
- **Server IP**: `127.0.0.1` (localhost)
- **Port**: `8080`
- **Encoding**: UTF-8
- **Protocol**: TCP Socket
- **Data format**: JSON (Gson library)

## Tích hợp với UI
- **LoginFrame**: Hiển thị thông báo kết nối
- **LobbyFrame**: Hiển thị khi đang tìm trận
- **GameFrame**: Hiển thị bàn cờ, nhận/cập nhật nước đi
