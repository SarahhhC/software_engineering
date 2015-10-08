import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.JFrame;

public class studentManagement extends JFrame implements ActionListener{
	//db 연결
	Connection conn;
	Statement  stat;
	public studentManagement(){
		//레이아웃 짜기 
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//이벤트 수행 
		
	}
	public void initialize(){
		
	}
	void dbconnectionCheck(){
		//데이터베이스와 연결 끊어졌는지 확인 
		try {
			 //JDBC 드라이버를 DriverManager에 등록
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = "jdbc:mysql://127.0.0.1:3306/sook?";

			//해당 Driver로 부터 Connection 객체 획득
			conn = DriverManager.getConnection(url, "root", "apmsetup");
			//Connection 객체로 부터 Statement 객체 획득
			stat = conn.createStatement();
			initialize();
			System.out.println("디비연결!!");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	void addStudentInfo(){
		
	}
	void deleteStudentById(){
		
	}
	void viewStudentById(){
		
	}
	void updateStudentById(){
		
	}
	
	
	public static void main() {
		new studentManagement();
	}
}
