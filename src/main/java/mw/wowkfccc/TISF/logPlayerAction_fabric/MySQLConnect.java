//package mw.wowkfccc.TISF.logPlayerAction_fabric;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class MySQLConnect {
//    private Connection conn;
//
//    // 資料庫連線參數
//    private final String host     = "tisfdatabase.japanwest.cloudapp.azure.com";
//    private final int    port     = 3306;
//    private final String database = "bothem";
//    private final String username = "bothem";
//    private final String password = "logaction";
//
//    /** 建立並初始化 JDBC 連線 */
//    public void connect() {
//        // 如果已經有活躍連線，就不用重連
//        try {
//            if (conn != null && !conn.isClosed()) return;
//        } catch (SQLException e) {
//            // 如果檢查過程拋例外，繼續走後面重連流程
//        }
//
//        try {
//            // 確保驅動已載入
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            System.err.println("[MySQLConnect] 無法載入 com.mysql.cj.jdbc.Driver");
//            e.printStackTrace();
//            return;
//        }
//
//        String jdbcUrl = String.format(
//                "jdbc:mysql://%s:%d/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
//                host, port, database
//        );
//
//        try {
//            conn = DriverManager.getConnection(jdbcUrl, username, password);
//            System.out.println("[MySQLConnect] ✅ MySQL 連線已建立");
//        } catch (SQLException e) {
//            System.err.println("[MySQLConnect] 無法建立 MySQL 連線");
//            e.printStackTrace();
//        }
//    }
//
//    /** 取得一個 Connection；若尚未連線或已關閉，先呼叫 connect() */
//    public Connection getConnection() throws SQLException {
//        if (conn == null || conn.isClosed()) {
//            connect();
//        }
//        return conn;
//    }
//
//    /** 檢查目前連線是否仍然有效 */
//    public boolean isConnected() {
//        try {
//            return (conn != null && !conn.isClosed() && conn.isValid(2));
//        } catch (SQLException e) {
//            return false;
//        }
//    }
//
//    /** 關閉目前的 JDBC 連線 */
//    public void disconnect() {
//        if (conn != null) {
//            try {
//                conn.close();
//                System.out.println("[MySQLConnect] 🛑 MySQL 連線已關閉");
//            } catch (SQLException e) {
//                System.err.println("[MySQLConnect] 關閉 MySQL 連線時發生錯誤");
//                e.printStackTrace();
//            }
//        }
//    }
//}
