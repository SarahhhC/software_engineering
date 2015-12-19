import static org.junit.Assert.*;
import java.sql.*;
import org.junit.Before;
import org.junit.Test;

public class studentManagementTest {
	studentManagement studentmanagement = new studentManagement();
	Connection dbConnection; 
	Statement  sqlStatement;

	@Before
	public void setUpDBconnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = "jdbc:mysql://127.0.0.1:3306/sook?";

			dbConnection = DriverManager.getConnection(url, "root", "root");
			sqlStatement = dbConnection.createStatement();
			System.out.println("DB Connect!!");
		}
		catch (Exception e) {
		} 
	}

	@Test
	public void testdbConnectionCheck() {
		assertTrue(studentmanagement.dbconnectionCheck("root"));
	}

	@Test
	public void testCheckAuthority() {
		assertTrue(studentmanagement.checkAuthority("professor"));
	}

	@Test
	public void testSearchId(){
		studentmanagement.addStudentInfo("1310889", "youjinhan", "it", "01099992020");
		assertTrue(studentmanagement.searchId("1310889"));
	}

	@Test
	public void testDefectSearchId(){
		assertFalse(studentmanagement.searchId(""));
	}

	@Test
	public void testAddStudentInfo() {
		studentmanagement.addStudentInfo("11","you","it","gg");
		try {
			String sql = "select * from student where id='" + 11 + "' ";
			PreparedStatement sqlStatement = dbConnection.prepareStatement(sql);
			ResultSet resultset = sqlStatement.executeQuery();

			assertEquals(resultset.getString(1), "11");
			assertEquals(resultset.getString(2), "you");
			assertEquals(resultset.getString(3), "it");
			assertEquals(resultset.getString(4), "gg");
		}
		catch (Exception e) {   
		}
	}

	@Test
	public void testIsValueNotNull() {
		assertTrue(studentmanagement.isValueNotNull("1311722", "park", "IT", "01011112222"));
	}

	@Test
	public void testDeleteStudentById() {
		studentmanagement.addStudentInfo("1310778", "name", "dept", "phone");
		assertTrue(studentmanagement.searchId("1310778"));
		assertTrue(studentmanagement.deleteStudentById("1310778"));
		assertFalse(studentmanagement.searchId("1310778"));
	}

	@Test
	public void testDefectDeleteStudentById() {
		assertFalse(studentmanagement.deleteStudentById(""));
	}

	@Test
	public void testUpdateStudentById() {
		try {
			studentmanagement.addStudentInfo("1311725","you","it","gg");
			studentmanagement.updateStudentById("1311725", "01012345678");
			String sql = "select phone from student where id='1311725'";
			PreparedStatement sqlStatement = dbConnection.prepareStatement(sql);
			ResultSet resultset = sqlStatement.executeQuery();
			while(resultset.next())
				assertEquals(resultset.getString("phone"), "01012345678");
		}                                                                                                                                                                                                                                                                                         
		catch (Exception e) {
		}
	}

	@Test
	public void testDefectUpdateStudentById() {
		assertFalse(studentmanagement.updateStudentById("","01000000000"));
	}

	@Test
	public void testViewStudentById() {
		studentmanagement.createViewTab();
		studentmanagement.addStudentInfo("1", "name", "dept", "phone");
		studentmanagement.addStudentInfo("2", "name", "dept", "phone");
		studentmanagement.addStudentInfo("3", "name", "dept", "phone");
		studentmanagement.addStudentInfo("4", "name", "dept", "phone");
		studentmanagement.viewStudentById("1");
		studentmanagement.viewStudentById("2");
		studentmanagement.viewStudentById("3");
		studentmanagement.viewStudentById("4");
		assertEquals(studentmanagement.studentListTable.getRowCount(), 4);
		assertEquals(studentmanagement.studentListTable.getValueAt(0, 0), "1");
		assertEquals(studentmanagement.studentListTable.getValueAt(1, 0), "2");
		assertEquals(studentmanagement.studentListTable.getValueAt(2, 0), "3");
		assertEquals(studentmanagement.studentListTable.getValueAt(3, 0), "4");
	}

	@Test
	public void testDefectViewStudentById() {
		studentmanagement.createViewTab();
		assertEquals(studentmanagement.studentListTable.getRowCount(), 0);
		assertFalse(studentmanagement.viewStudentById(""));
	}

	@Test
	public void testUpdateProfessorPassword() {
		studentmanagement.updateProfessorPassword("1234");
		try {
			String sql = "select password from login where id = 'professor'";
			PreparedStatement sqlStatement = dbConnection.prepareStatement(sql);
			ResultSet resultset = sqlStatement.executeQuery();
			while(resultset.next())
				assertEquals(resultset.getString("password"), "1234");
		}
		catch (Exception e) {
		}
	}

	@Test
	public void testDefectUpdateProfessorPassword() {
		assertFalse(studentmanagement.updateProfessorPassword(""));
	}

	@Test
	public void testCheckProfessorPassword() {
		try {
			String sql = "select password from login where id = 'professor' ";
			PreparedStatement sqlStatement = dbConnection.prepareStatement(sql);
			ResultSet resultset = sqlStatement.executeQuery();
			assertFalse(resultset.getString("password") == "____");
		}
		catch (Exception e) {
		}
	}

	@Test
	public void testDefectCheckProfessorPassword() {
		try {
			String sql = "select password from login where id = 'professor' ";
			PreparedStatement sqlStatement = dbConnection.prepareStatement(sql);
			ResultSet resultset = sqlStatement.executeQuery();
			assertEquals(resultset.getString("password"),"0000");
		}
		catch (Exception e) {
		}
	}
}
