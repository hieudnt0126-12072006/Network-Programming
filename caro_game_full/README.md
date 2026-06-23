# 🎮 Cờ Caro Online - Multiplayer Desktop Game

## Công nghệ sử dụng
- **Java 17** + **Swing** (giao diện desktop)
- **TCP Socket** (Client-Server, multi-thread)
- **MySQL** + **JDBC** (đăng ký/đăng nhập, lịch sử trận)
- **Gson** (serialize/deserialize JSON packet)
- **Maven** (quản lý dependency)

## Cấu trúc dự án
```
src/main/java/
├── Server/
│   ├── CaroServer.java        ← Main server (port 8080)
│   ├── ClientHandler.java     ← Xử lý mỗi client kết nối
│   ├── ClientManager.java     ← Quản lý danh sách client online
│   ├── RoomManager.java       ← Quản lý phòng chơi (in-memory)
│   ├── MatchManager.java      ← Logic trận đấu, quản lý lượt
│   ├── LobbyManager.java      ← Broadcast lobby update
│   └── ServerThreadPool.java  ← Thread pool
├── Client/
│   └── CaroClient.java        ← Main client, kết nối & điều hướng
├── Model/
│   ├── Message.java           ← Packet truyền qua Socket (JSON)
│   ├── MessageType.java       ← Enum các loại message
│   ├── User.java, Room.java, Match.java, ...
├── Ui/
│   ├── LoginFrame.java        ← Màn hình đăng nhập
│   ├── RegisterFrame.java     ← Màn hình đăng ký
│   ├── LobbyFrame.java        ← Sảnh + danh sách phòng
│   ├── GameFrame.java         ← Bàn cờ + chat
│   ├── HistoryFrame.java      ← Lịch sử trận đấu
│   └── StatisticFrame.java    ← Bảng xếp hạng
├── Game/
│   ├── Board.java             ← Ma trận bàn cờ 20x20
│   ├── GameLogic.java         ← Thuật toán kiểm tra thắng
│   ├── TurnManager.java       ← Quản lý lượt đi (server-side)
│   └── WinChecker.java        ← Kiểm tra hòa (bàn đầy)
├── Dao/
│   ├── UserDAO.java           ← CRUD bảng users
│   └── MatchDAO.java          ← CRUD bảng matches
├── Database/
│   ├── DatabaseConfig.java    ← Cấu hình MySQL
│   ├── DatabaseConnection.java← Singleton connection
│   └── DatabaseInitializer.java ← Tự tạo bảng khi khởi động
├── service/
│   ├── AuthenticationService.java ← Logic đăng ký/đăng nhập
│   ├── GameService.java           ← Lưu kết quả sau trận
│   └── ...
└── util/
    ├── PasswordUtil.java      ← SHA-256 hash mật khẩu
    ├── Validator.java         ← Validate input
    └── ...
```

## Cách chạy

### 1. Setup Database
```bash
mysql -u root -p < docs/database_setup.sql
```
Hoặc chỉnh `DatabaseConfig.java` theo thông tin MySQL của bạn.

### 2. Build dự án
```bash
mvn clean package -DskipTests
```

### 3. Chạy Server
```bash
# Trong IntelliJ: Run Server.CaroServer
# Hoặc terminal:
mvn exec:java -Dexec.mainClass="Server.CaroServer"
```

### 4. Chạy Client (nhiều cửa sổ)
```bash
# Trong IntelliJ: Run Client.CaroClient
# Hoặc terminal:
mvn exec:java -Dexec.mainClass="Client.CaroClient"
```

## Luồng hoạt động

```
[Client A]  REGISTER/LOGIN ──→ [Server] ──→ AUTH_SUCCESS ──→ [LobbyFrame]
[Client A]  CREATE_ROOM   ──→ [Server] ──→ ROOM_CREATED  ──→ chờ đối thủ
[Client B]  JOIN_ROOM     ──→ [Server] ──→ START_GAME    ──→ [GameFrame × 2]
[Client A]  MOVE(x,y)    ──→ [Server/MatchManager] ──→ relay → [Client B]
                                          └─ kiểm tra win → GAME_OVER
[Game Over] → lưu DB → cập nhật wins/losses
```

## Ghi chú
- Server chống hack lượt đi: `MatchManager.handleMove()` kiểm tra đúng lượt trên server
- Nếu không có MySQL, server vẫn chạy nhưng không có tính năng đăng nhập/lưu lịch sử
- Mật khẩu được hash SHA-256 trước khi lưu DB
