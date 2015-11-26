import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class studentManagement extends JFrame implements ActionListener {
	JTabbedPane tabpane;
	Connection dbConnection; 
	Statement  sqlStatement;
	Button buttonToLogin;
	Button buttonToCheckPassword;
	Button buttonToLogout;
	Button buttonToBackMainPage;
	Button buttonToAddStudentInfo;
	Button buttonToSearchAndDelete;
	Button buttonToDeleteStudentInfo;
	Button buttonToSearchAndUpdate;
	Button buttonToUpdateStudentPhone;
	Button buttonToViewStudentInfo;
	Button buttonToChangeProfessorPassword;
	TextField textfieldToAddId;
	TextField textfieldToAddName;
	TextField textfieldToAddDepartment;
	TextField textfieldToAddPhone; 
	TextField textfieldToDeleteId;
	TextField textfieldToUpdateId;
	TextField textfieldToUpdatePhone;
	TextField textfieldToViewId;
	TextField textfieldToInformDeletion;
	TextField textfieldToInformUpdate;
	TextField textfieldToLoginById;
	TextField textfieldToAddNewPassword;
	JPasswordField passwordFieldForProfessor;
	String index[] = {"ID", "NAME", "DEPARTMENT", "PHONE"}; 
	DefaultTableModel listmodel = new DefaultTableModel(index, 0);
	JTable studentList = new JTable(listmodel); 

	public studentManagement() throws IndexOutOfBoundsException {
		super("Student Management Program");
		tabpane = new JTabbedPane();
		tabpane.addTab("Login", createLoginIdTab());

		getContentPane().add(tabpane, BorderLayout.CENTER);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(900, 500);
		setVisible(true);

		dbconnectionCheck();  
	}

	JPanel createLoginIdTab() {
		JPanel panelToLoginById = new JPanel();
		panelToLoginById.setBackground(Color.white);
		Panel loginPanel = new Panel();
		Label loginId = new Label("ID  :  ");
		textfieldToLoginById = new TextField(20);
		buttonToLogin = new Button("LOGIN");
		buttonToLogin.addActionListener(this);

		loginPanel.add(loginId);
		loginPanel.add(textfieldToLoginById);
		panelToLoginById.add(loginPanel);
		panelToLoginById.add(buttonToLogin);

		return panelToLoginById;
	}

	void dbconnectionCheck() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = "jdbc:mysql://127.0.0.1:3306/sook?";

			dbConnection = DriverManager.getConnection(url, "root", "root");
			sqlStatement = dbConnection.createStatement();
			System.out.println("DB Connect!!");
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == buttonToLogin)
			checkAuthority();
		if (e.getSource() == buttonToCheckPassword)
			checkPassword(textfieldToLoginById.getText().trim());
		if (e.getSource() == buttonToLogout || e.getSource() == buttonToBackMainPage)
			goToMainPage();
		if (e.getSource() == buttonToAddStudentInfo)
			addStudentInfo();
		if (e.getSource() == buttonToSearchAndDelete)
			searchToDelete();
		if (e.getSource() == buttonToDeleteStudentInfo)
			deleteStudentById();
		if (e.getSource() == buttonToSearchAndUpdate) 
			searchToUpdate();
		if (e.getSource() == buttonToUpdateStudentPhone)
			updateStudentById(textfieldToUpdateId.getText(), textfieldToUpdatePhone.getText().trim());
		if (e.getSource() == buttonToViewStudentInfo)
			viewStudentById(textfieldToViewId.getText().trim());
		if (e.getSource() == buttonToChangeProfessorPassword)
			updateProfessorPassword(textfieldToAddNewPassword.getText().trim());
	}

	void checkAuthority() {
		tabpane.removeTabAt(0);
		if (textfieldToLoginById.getText().equals("professor"))
			tabpane.addTab("Password", createLoginPasswordTab());
		else if (textfieldToLoginById.getText().equals("guest"))
			showTabsForGuest();
		else {
			goToMainPage();
			textfieldToLoginById.setText("Wrong Id");
		}
	}

	void checkPassword(String addedId) {
		try {
			String sql = "select password from login where id = '" + addedId + "' ";
			PreparedStatement sqlStatement = dbConnection.prepareStatement(sql);
			ResultSet resultset = sqlStatement.executeQuery();
			while(resultset.next()) 
				if (resultset.getString("password").equals(passwordFieldForProfessor.getText()))
					showTabsForProfessor();
		}
		catch(Exception e) {
		}
	}

	void showTabsForGuest() {
		tabpane.addTab("View Student's Information", createViewTab());
		tabpane.addTab("Logout", createLogoutTab());
	}

	void showTabsForProfessor() {
		tabpane.removeTabAt(0);
		tabpane.addTab("Add New Student", createAddTab());
		tabpane.addTab("Delete Student", createDeleteTab());
		tabpane.addTab("Update Student's Phone Number", createUpdateTab());
		tabpane.addTab("View Student's Information", createViewTab());
		tabpane.addTab("Change Password", createChangePasswordTab());
		tabpane.addTab("Logout", createLogoutTab());
	}

	JPanel createLoginPasswordTab() {
		JPanel panelToLoginByPassword = new JPanel();
		panelToLoginByPassword.setBackground(Color.white);
		Panel loginPanel = new Panel();
		Label initialPassword = new Label("initial password is 0000 ");
		Label loginPassword = new Label("Password  :  ");
		passwordFieldForProfessor = new JPasswordField(20);
		buttonToCheckPassword = new Button("LOGIN");
		buttonToCheckPassword.addActionListener(this);
		buttonToBackMainPage = new Button("back");
		buttonToBackMainPage.addActionListener(this);

		loginPanel.add(initialPassword);
		loginPanel.add(loginPassword);
		loginPanel.add(passwordFieldForProfessor);
		panelToLoginByPassword.add(loginPanel);
		panelToLoginByPassword.add(buttonToCheckPassword);
		panelToLoginByPassword.add(buttonToBackMainPage);

		return panelToLoginByPassword;
	}

	JPanel createLogoutTab() {
		JPanel panelToLogout = new JPanel();
		buttonToLogout = new Button("Logout");

		buttonToLogout.addActionListener(this);
		panelToLogout.add(buttonToLogout);

		return panelToLogout;
	}

	JPanel createAddTab() {
		JPanel panelToAddStudent = new JPanel();
		panelToAddStudent.setLayout(new GridLayout(9,1));
		panelToAddStudent.setBackground(Color.white);
		Label studentId_add = new Label("Student ID");
		Label studentName = new Label("Name");
		Label studentDept = new Label("Department");
		Label studentPhone = new Label("Phone number");
		textfieldToAddId = new TextField(20);
		textfieldToAddName = new TextField(20);
		textfieldToAddDepartment = new TextField(20);
		textfieldToAddPhone = new TextField(20);
		buttonToAddStudentInfo = new Button("ADD");
		buttonToAddStudentInfo.addActionListener(this);

		panelToAddStudent.add(studentId_add);
		panelToAddStudent.add(textfieldToAddId);
		panelToAddStudent.add(studentName);
		panelToAddStudent.add(textfieldToAddName);
		panelToAddStudent.add(studentDept);
		panelToAddStudent.add(textfieldToAddDepartment);
		panelToAddStudent.add(studentPhone);
		panelToAddStudent.add(textfieldToAddPhone);
		panelToAddStudent.add(buttonToAddStudentInfo);

		return panelToAddStudent;
	}

	JPanel createDeleteTab() {
		JPanel panelToDeleteStudent = new JPanel();
		panelToDeleteStudent.setLayout(new BorderLayout(5, 5));
		panelToDeleteStudent.setBackground(Color.white);
		Panel inputPanel = new Panel();
		Label studentId_delete = new Label("Student ID");
		textfieldToDeleteId = new TextField(20); 
		buttonToSearchAndDelete = new Button("SEARCH");
		buttonToSearchAndDelete.addActionListener(this);
		textfieldToInformDeletion = new TextField("Please, Search Id to delete");
		buttonToDeleteStudentInfo = new Button("DELETE");
		buttonToDeleteStudentInfo.addActionListener(this);
		buttonToDeleteStudentInfo.setSize(300,10);

		inputPanel.add(studentId_delete);
		inputPanel.add(textfieldToDeleteId);
		inputPanel.add(buttonToSearchAndDelete);
		panelToDeleteStudent.add(inputPanel,BorderLayout.NORTH);
		panelToDeleteStudent.add(textfieldToInformDeletion, BorderLayout.CENTER);
		panelToDeleteStudent.add(buttonToDeleteStudentInfo, BorderLayout.SOUTH);

		return panelToDeleteStudent;
	}

	JPanel createUpdateTab() {
		JPanel panelToUpdateStudent = new JPanel();
		panelToUpdateStudent.setBackground(Color.white);
		Panel inputPanel = new Panel();
		Label studentId_update = new Label("Student ID");
		textfieldToUpdateId = new TextField(20); 
		buttonToSearchAndUpdate = new Button("SEARCH");
		buttonToSearchAndUpdate.addActionListener(this);
		Panel phonePanel = new Panel();
		Label newPhone = new Label("New Phone Number");
		textfieldToUpdatePhone = new TextField(20);
		buttonToUpdateStudentPhone = new Button("UPDATE");
		buttonToUpdateStudentPhone.addActionListener(this);
		textfieldToInformUpdate = new TextField("Please, Search Id to Update Phone Number");   

		inputPanel.add(studentId_update);
		inputPanel.add(textfieldToUpdateId);
		inputPanel.add(buttonToSearchAndUpdate);
		phonePanel.add(newPhone);
		phonePanel.add(textfieldToUpdatePhone);
		phonePanel.add(buttonToUpdateStudentPhone);
		panelToUpdateStudent.add(inputPanel,BorderLayout.NORTH);
		panelToUpdateStudent.add(textfieldToInformUpdate,BorderLayout.CENTER);
		panelToUpdateStudent.add(phonePanel,BorderLayout.SOUTH);

		return panelToUpdateStudent;
	}

	JPanel createViewTab() {
		JPanel panelToViewStudent = new JPanel();
		panelToViewStudent.setBackground(Color.white);
		JPanel inputPanel = new JPanel();
		Label studentId_view = new Label("Student ID");
		textfieldToViewId = new TextField(20); 
		buttonToViewStudentInfo = new Button("SEARCH");
		buttonToViewStudentInfo.addActionListener(this);
		JScrollPane scroll = new JScrollPane(studentList);
		scroll.setSize(17,20);

		inputPanel.add(studentId_view);
		inputPanel.add(textfieldToViewId);
		inputPanel.add(buttonToViewStudentInfo);
		panelToViewStudent.add(inputPanel,BorderLayout.NORTH);
		panelToViewStudent.add(scroll,BorderLayout.SOUTH);

		return panelToViewStudent;
	}

	JPanel createChangePasswordTab() {
		JPanel panelToChangePassword = new JPanel();
		panelToChangePassword.setBackground(Color.white);
		Panel inputPanel = new Panel();
		Label noticePassword = new Label("Enter New Password");
		textfieldToAddNewPassword = new TextField(20);
		buttonToChangeProfessorPassword = new Button("CHANGE");
		buttonToChangeProfessorPassword.addActionListener(this);

		inputPanel.add(noticePassword);
		inputPanel.add(textfieldToAddNewPassword);
		inputPanel.add(buttonToChangeProfessorPassword);
		panelToChangePassword.add(inputPanel);

		return panelToChangePassword;
	}

	void goToMainPage() {
		tabpane.removeAll();
		tabpane.addTab("Login", createLoginIdTab());
		for (int i = listmodel.getRowCount() - 1; i >= 0; i--)
			listmodel.removeRow(i);
		if (textfieldToLoginById.getText() == "professor") {
			textfieldToAddId.setText("");
			textfieldToAddName.setText("");
			textfieldToAddDepartment.setText("");
			textfieldToAddPhone.setText("");
			textfieldToDeleteId.setText("");
			textfieldToInformDeletion.setText("Please, Search Id to delete");
			textfieldToUpdateId.setText("");
			textfieldToInformUpdate.setText("Please, Search Id to Update Phone Number");
			textfieldToUpdatePhone.setText("");
			textfieldToViewId.setText("");
			textfieldToAddNewPassword.setText("");
		}
	}

	void addStudentInfo() {
		try {
			String id = textfieldToAddId.getText().trim();
			String name = textfieldToAddName.getText().trim();
			String department = textfieldToAddDepartment.getText().trim();
			String phone = textfieldToAddPhone.getText().trim();
			if (checkValueExists(id, name, department, phone)) {
				String sql = "add into student values(?, ?, ?, ?)";
				PreparedStatement sqlStatement = dbConnection.prepareStatement(sql);
				sqlStatement.setString(1, id);
				sqlStatement.setString(2, name);
				sqlStatement.setString(3, department );
				sqlStatement.setString(4, phone);
				sqlStatement.executeUpdate();
				System.out.println("student data add success");
			}
		}
		catch(Exception e) {
		}
	}

	boolean checkValueExists(String id, String name, String department, String phone) {
		boolean VALID = true;
		if ( id == null || name == null || department == null || phone == null)
			VALID = false;

		return VALID;
	}

	void searchToDelete() {
		String id = textfieldToDeleteId.getText().trim();
		String NOTICE_MESSAGE = " exists, you can delete!";
		String INFORM_MESSAGE = searchId(id, NOTICE_MESSAGE);
		textfieldToInformDeletion.setText(INFORM_MESSAGE);   
	}

	void searchToUpdate() {
		String id = textfieldToUpdateId.getText().trim();
		String NOTICE_MESSAGE = " exists, please update phone number!";
		String INFORM_MESSAGE = searchId(id, NOTICE_MESSAGE);
		textfieldToInformUpdate.setText(INFORM_MESSAGE);
	}

	String searchId(String id, String message) {
		String update = "";
		try {
			String sql = "select name from student where id='" + id + "' ";
			PreparedStatement sqlStatement = dbConnection.prepareStatement(sql);
			ResultSet resultset = sqlStatement.executeQuery(); 
			update = "ID "+id+" not exists";
			while(resultset.next())
				if (resultset.getString("name") != null)
					update= "ID "+ id + message;
		}
		catch(Exception e) {
		}
		return update;
	}

	void deleteStudentById() {
		try {
			String id = textfieldToDeleteId.getText().trim();
			if (id == null || id.length() == 0)
				return ;
			sqlStatement.executeUpdate("delete from student where id='" + id + "'");
			System.out.println("student data delete success");   
		}
		catch(Exception e) {
		}
	}

	void updateStudentById(String id,String phone) {
		try {
			String sql = "update student set phone = ? where id ='" + id + "'";
			PreparedStatement sqlStatement = dbConnection.prepareStatement(sql);
			sqlStatement.setString(1, phone);
			sqlStatement.executeUpdate();
			System.out.println("student data update success");
		}
		catch(Exception e) {
		}
	}

	void viewStudentById(String id) {
		String info[] = new String[4];
		listmodel = (DefaultTableModel) studentList.getModel();
		try {
			String sql = "select * from student where id='" + id + "'";
			PreparedStatement sqlStatement = dbConnection.prepareStatement(sql);
			ResultSet resultset = sqlStatement.executeQuery(); 
			while(resultset.next()) {
				info[0] = resultset.getString("id");
				info[1] = resultset.getString("name");
				info[2] = resultset.getString("department");
				info[3] = resultset.getString("phone");

				listmodel.addRow(info);
			}
			System.out.println("student data view success");
		}
		catch(Exception e) {
		}   
		System.out.println(studentList.getValueAt(0, 0));
	}

	void updateProfessorPassword(String newpassword) {   
		try {
			String sql = "update login set password = ? where id = 'professor'";
			PreparedStatement sqlStatement = dbConnection.prepareStatement(sql);
			sqlStatement.setString(1, newpassword);
			sqlStatement.executeUpdate();
			System.out.println("professor password updated");
		}
		catch(Exception e) {
		}
	}   

	public static void main(String args[]) {
		studentManagement test = new studentManagement();
	}
}