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
	Button loginButton, passwordCheckButton, logoutButton, backToMainButton, changeProfessorPasswordButton;;
	Button insertStudentInfoButton, deleteStudentInfoButton, updateStudentPhoneButton, viewStudentInfoButton;
	Button searchStudentIdToDeleteButton, searchStudentIdToUpdateButton;
	JPasswordField insertProfessorPassword;
	TextField insertId, insertName, insertDepartment, insertPhone; 
	TextField idToDelete, idToUpdate, updatedPhone, idToView;
	TextField informToDelete, informToUpdate;
	TextField insertedLoginID, newPassword;

	String index[] = {"ID", "NAME", "DEPARTMENT", "PHONE"}; 
	DefaultTableModel listmodel = new DefaultTableModel(index, 0);
	JTable studentList = new JTable(listmodel); 

	JPanel loginId = new JPanel();
	JPanel loginPassword = new JPanel();
	JPanel logout = new JPanel();
	JPanel changePassword = new JPanel();
	JPanel addStudent = new JPanel();
	JPanel deleteStudent = new JPanel();
	JPanel updateStudent = new JPanel();
	JPanel viewStudent = new JPanel();

	public studentManagement() throws IndexOutOfBoundsException {
		super("Student Management Program");
		tabpane = new JTabbedPane();

		createLoginIdTab(loginId);
		createLoginPasswordTab(loginPassword);
		createLogoutTab(logout);
		createAddTab(addStudent);
		createDeleteTab(deleteStudent);
		createUpdateTab(updateStudent);
		createViewTab(viewStudent);
		createChangePasswordTab(changePassword);

		tabpane.addTab("Login", loginId);
		getContentPane().add(tabpane, BorderLayout.CENTER);
		attachActionListenerToButtons();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(700,400);
		setVisible(true);

		dbconnectionCheck();  
	}

	void createLoginIdTab(JPanel loginId) {
		loginId.setBackground(Color.white);
		Panel loginPanel = new Panel();
		Label loginID = new Label("ID  :  ");
		insertedLoginID = new TextField(20);
		loginButton = new Button("LOGIN");

		loginPanel.add(loginID);
		loginPanel.add(insertedLoginID);
		loginId.add(loginPanel);
		loginId.add(loginButton);
	}

	void createLoginPasswordTab(JPanel loginPassword) {
		loginPassword.setBackground(Color.white);
		Panel loginPanel = new Panel();
		Label loginPW = new Label("PW  :  ");
		insertProfessorPassword = new JPasswordField(20);
		passwordCheckButton = new Button("LOGIN");
		backToMainButton = new Button("back");

		loginPanel.add(loginPW);
		loginPanel.add(insertProfessorPassword);
		loginPassword.add(loginPanel);
		loginPassword.add(passwordCheckButton);
		loginPassword.add(backToMainButton);
	}

	void createLogoutTab(JPanel logout) {
		logoutButton = new Button("Logout");

		logout.add(logoutButton);
	}

	void createAddTab(JPanel addStudent) {
		addStudent.setLayout(new GridLayout(9,1));
		addStudent.setBackground(Color.white);
		Label studentID_add = new Label("Student ID");
		Label studentName = new Label("Name");
		Label studentDept = new Label("Department");
		Label studentPhone = new Label("Phone number");
		insertId = new TextField(20);
		insertName = new TextField(20);
		insertDepartment = new TextField(20);
		insertPhone = new TextField(20);
		insertStudentInfoButton = new Button("ADD");

		addStudent.add(studentID_add);
		addStudent.add(insertId);
		addStudent.add(studentName);
		addStudent.add(insertName);
		addStudent.add(studentDept);
		addStudent.add(insertDepartment);
		addStudent.add(studentPhone);
		addStudent.add(insertPhone);
		addStudent.add(insertStudentInfoButton);   
	}

	void createDeleteTab(JPanel deleteStudent) {
		deleteStudent.setLayout(new BorderLayout(5, 5));
		deleteStudent.setBackground(Color.white);
		Panel inputPanel = new Panel();
		Label studentID_delete = new Label("Student ID");
		idToDelete = new TextField(20); 
		searchStudentIdToDeleteButton = new Button("SEARCH"); 
		informToDelete = new TextField("Please, Search Id to delete");
		deleteStudentInfoButton = new Button("DELETE");
		deleteStudentInfoButton.setSize(300,10);

		inputPanel.add(studentID_delete);
		inputPanel.add(idToDelete);
		inputPanel.add(searchStudentIdToDeleteButton);
		deleteStudent.add(inputPanel,BorderLayout.NORTH);
		deleteStudent.add(informToDelete, BorderLayout.CENTER);
		deleteStudent.add(deleteStudentInfoButton, BorderLayout.SOUTH);
	}

	void createUpdateTab(JPanel updateStudent) {
		updateStudent.setBackground(Color.white);
		Panel inputPanel = new Panel();
		Label studentID_update = new Label("Student ID");
		idToUpdate = new TextField(20); 
		searchStudentIdToUpdateButton = new Button("SEARCH");
		Panel phonePanel = new Panel();
		Label newPhone = new Label("New Phone Number");
		updatedPhone = new TextField(20);
		updateStudentPhoneButton = new Button("UPDATE");
		informToUpdate = new TextField("Please, Search Id to Update Phone Number");   

		inputPanel.add(studentID_update);
		inputPanel.add(idToUpdate);
		inputPanel.add(searchStudentIdToUpdateButton);
		phonePanel.add(newPhone);
		phonePanel.add(updatedPhone);
		phonePanel.add(updateStudentPhoneButton);
		updateStudent.add(inputPanel,BorderLayout.NORTH);
		updateStudent.add(informToUpdate,BorderLayout.CENTER);
		updateStudent.add(phonePanel,BorderLayout.SOUTH);
	}

	void createViewTab(JPanel viewStudent) {
		viewStudent.setBackground(Color.white);
		JPanel inputPanel = new JPanel();
		Label studentID_view = new Label("Student ID");
		idToView = new TextField(20); 
		viewStudentInfoButton = new Button("SEARCH");
		JScrollPane scroll = new JScrollPane(studentList);
		scroll.setSize(17,20);

		inputPanel.add(studentID_view);
		inputPanel.add(idToView);
		inputPanel.add(viewStudentInfoButton);
		viewStudent.add(inputPanel,BorderLayout.NORTH);
		viewStudent.add(scroll,BorderLayout.SOUTH);
	}

	void createChangePasswordTab(JPanel changePassword) {
		changePassword.setBackground(Color.white);
		Panel inputPanel = new Panel();
		Label NOTICE_PW = new Label("Enter New Password");
		newPassword = new TextField(20);
		changeProfessorPasswordButton = new Button("CHANGE");

		inputPanel.add(NOTICE_PW);
		inputPanel.add(newPassword);
		inputPanel.add(changeProfessorPasswordButton);
		changePassword.add(inputPanel);
	}

	void attachActionListenerToButtons() {
		loginButton.addActionListener(this);
		passwordCheckButton.addActionListener(this);
		logoutButton.addActionListener(this);
		insertStudentInfoButton.addActionListener(this);
		searchStudentIdToDeleteButton.addActionListener(this);
		deleteStudentInfoButton.addActionListener(this);
		searchStudentIdToUpdateButton.addActionListener(this);
		updateStudentPhoneButton.addActionListener(this);
		viewStudentInfoButton.addActionListener(this);
		changeProfessorPasswordButton.addActionListener(this);
		backToMainButton.addActionListener(this);
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
		if (e.getSource() == loginButton)
			checkAuthority();
		if (e.getSource() == passwordCheckButton)
			checkPassword(insertedLoginID.getText().trim());
		if (e.getSource() == logoutButton || e.getSource() == backToMainButton)
			goToMainPage();
		if (e.getSource() == insertStudentInfoButton)
			addStudentInfo();
		if (e.getSource() == searchStudentIdToDeleteButton) {
			String id = idToDelete.getText().trim();
			String NOTICE_MESSAGE = " exists, you can delete!";
			informToDelete.setText(searchId(id, NOTICE_MESSAGE));
		}
		if (e.getSource() == deleteStudentInfoButton)
			deleteStudentById();
		if (e.getSource() == searchStudentIdToUpdateButton) {
			String id = idToUpdate.getText().trim();
			String NOTICE_MESSAGE = " exists, please update phone number!";
			informToUpdate.setText(searchId(id, NOTICE_MESSAGE));
		}
		if (e.getSource() == updateStudentPhoneButton)
			updateStudentById(idToUpdate.getText().trim(),updatedPhone.getText().trim());
		if (e.getSource() == viewStudentInfoButton)
			viewStudentById(idToView.getText().trim());
		if (e.getSource() == changeProfessorPasswordButton)
			updateProfessorPassword(newPassword.getText().trim());
	}

	void checkAuthority() {
		tabpane.removeTabAt(0);
		if (insertedLoginID.getText().equals("professor"))
			tabpane.addTab("Insert Password", loginPassword);
		else if (insertedLoginID.getText().equals("guest")) {
			tabpane.addTab("View Student's Information", viewStudent);
			tabpane.addTab("Logout", logout);
		}
		else {
			tabpane.addTab("Login", loginId);
			insertedLoginID.setText("Wrong Id");
		}
	}

	void checkPassword(String insertedId) {
		try {
			String sql = "select password from login where id = '" + insertedId + "' ";
			PreparedStatement p = dbConnection.prepareStatement(sql);
			ResultSet resultset = p.executeQuery();
			while(resultset.next()) 
				if (resultset.getString("password").equals(insertProfessorPassword.getText()))
					showTabsProfessorAuthority();
		}
		catch(Exception e) {
		}
	}

	void showTabsProfessorAuthority() {
		tabpane.removeTabAt(0);
		tabpane.addTab("Add New Student", addStudent);
		tabpane.addTab("Delete Student", deleteStudent);
		tabpane.addTab("Update Student's Phone Number", updateStudent);
		tabpane.addTab("View Student's Information", viewStudent);
		tabpane.addTab("Change Password", changePassword);
		tabpane.addTab("Logout", logout);
	}

	void goToMainPage() {
		tabpane.removeAll();
		tabpane.addTab("Login", loginId);
		System.out.println(listmodel.getRowCount());
		for (int i = listmodel.getRowCount() - 1; i >= 0; i--)
			listmodel.removeRow(i);
		insertId.setText("");
		insertName.setText("");
		insertDepartment.setText("");
		insertPhone.setText("");
		idToDelete.setText("");
		informToDelete.setText("Please, Search Id to delete");
		idToUpdate.setText("");
		informToUpdate.setText("Please, Search Id to Update Phone Number");
		updatedPhone.setText("");
		idToView.setText("");
		newPassword.setText("");
	}

	void addStudentInfo() {
		try {
			String id = insertId.getText().trim();
			String name = insertName.getText().trim();
			String department = insertDepartment.getText().trim();
			String phone = insertPhone.getText().trim();
			if (checkValueExists(id, name, department, phone)) {
				String sql = "insert into student values(?, ?, ?, ?)";
				PreparedStatement sqlStatement=dbConnection.prepareStatement(sql);
				sqlStatement.setString(1, id);
				sqlStatement.setString(2, name);
				sqlStatement.setString(3, department );
				sqlStatement.setString(4, phone);
				sqlStatement.executeUpdate();
				System.out.println("student data insert success");
			}
		}
		catch(Exception e) {
		}
	}

	boolean checkValueExists(String id, String name, String department, String phone) {
		boolean VALID = true;
		if ( id == null || name == null || department == null || insertPhone == null)
			VALID = false;
		
		return VALID;
	}
	
	String searchId(String id, String message) {
		String update = "";
		try {
			String sql = "select name from student where id='" + id + "' ";
			PreparedStatement p = dbConnection.prepareStatement(sql);
			ResultSet resultset = p.executeQuery(); 
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
			String id = idToDelete.getText().trim();
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
			PreparedStatement sqlStatement=dbConnection.prepareStatement(sql);
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
			PreparedStatement p = dbConnection.prepareStatement(sql);
			ResultSet resultset = p.executeQuery(); 
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
			PreparedStatement p = dbConnection.prepareStatement(sql);
			p.setString(1, newpassword);
			p.executeUpdate();
			System.out.println("professor password updated");
		}
		catch(Exception e) {
		}
	}   

	public static void main(String args[]) {
		studentManagement test = new studentManagement();
	}
}