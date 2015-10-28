import java.awt.*;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.sql.*;

import javax.swing.JFrame;

import javax.swing.*;



public class studentManagement extends JFrame implements ActionListener{

	//db Connect

	Connection conn;

	Statement  stat;


	ResultSet rs = null;

	JTabbedPane tabpane; //Create the tab
	Button insertInfo,search,deleteBtn, search2,updateBtn, viewBtn;

	TextField valID, valID2, valID3, valID4, valName, valDept, valPhone, valNewPhone;
	//JPanel addStudent;


	public studentManagement(){

		super("Student Management Program");

		tabpane = new JTabbedPane();

		JPanel addStudent = new JPanel();
		JPanel deleteStudent = new JPanel();
		JPanel updateStudent = new JPanel();
		JPanel viewStudent = new JPanel();

		////////////////////////////////////

		//UI ���� ���� ����

		Panel IDpanel, NamePanel, DeptPanel, PhonePanel, IDpanel2, IDpanel3, phonePanel2;

		Label stID, stName, stDept, stPhone, stID2, stID3,newPhone, stID4, inform, inform2;



		//UI Components Creation

		IDpanel = new Panel();

		NamePanel = new Panel();

		DeptPanel = new Panel();

		PhonePanel = new Panel();



		stID = new Label("Student ID");

		stName = new Label("Name");

		stDept = new Label("Department");

		stPhone = new Label("Phone number");

		//insert value here
		valID = new TextField(20);

		valName = new TextField(20);

		valDept = new TextField(20);

		valPhone = new TextField(20);



		//������ ���۳�Ʈ �߰�

		IDpanel.add(stID);

		IDpanel.add(valID);

		NamePanel.add(stName);

		NamePanel.add(valName);

		DeptPanel.add(stDept);

		DeptPanel.add(valDept);

		PhonePanel.add(stPhone);

		PhonePanel.add(valPhone);



		addStudent.setLayout(new GridLayout(5,1));

		addStudent.add(IDpanel);

		addStudent.add(NamePanel);

		addStudent.add(DeptPanel);

		addStudent.add(PhonePanel);



		insertInfo = new Button("ADD");

		addStudent.add(insertInfo);



		////////////////////////////////////



		deleteStudent.setLayout(new BorderLayout(5, 5));

		IDpanel2 = new Panel();

		stID2 = new Label("Student ID");
		//put data here to delete

		valID2 = new TextField(20);

		search = new Button("SEARCH");

		IDpanel2.add(stID2);

		IDpanel2.add(valID2);

		IDpanel2.add(search);



		inform = new Label("�˻�����Դϴ�");	//�˻������ ������ �κ�



		deleteBtn = new Button("DELETE");

		deleteBtn.setSize(300,10);

		deleteStudent.add(IDpanel2,BorderLayout.NORTH);

		deleteStudent.add(inform, BorderLayout.CENTER);

		deleteStudent.add(deleteBtn, BorderLayout.SOUTH);



		/////////////////////////////////////////

		updateStudent.setLayout(new BorderLayout(5, 5));

		IDpanel3 = new Panel();



		stID3 = new Label("Student ID");

		valID3 = new TextField(20);

		search2 = new Button("SEARCH");

		IDpanel3.add(stID3);

		IDpanel3.add(valID3);

		IDpanel3.add(search2);



		inform2 = new Label("�˻�����Դϴ�");	//�˻������ ������ �κ�



		phonePanel2 = new Panel();



		newPhone = new Label("New Phone Number");

		valNewPhone = new TextField(20);

		updateBtn = new Button("UPDATE");

		phonePanel2.add(newPhone);

		phonePanel2.add(valNewPhone);

		phonePanel2.add(updateBtn);



		updateStudent.add(IDpanel3,BorderLayout.NORTH);

		updateStudent.add(inform2,BorderLayout.CENTER);

		updateStudent.add(phonePanel2,BorderLayout.SOUTH);



		////////////////////////////////////////



		stID4 = new Label("Student ID");

		valID4 = new TextField(20);

		viewBtn = new Button("SEARCH");

		viewStudent.add(stID4);

		viewStudent.add(valID4);

		viewStudent.add(viewBtn);



		////////////////////////////////////////////////


		addStudent.setBackground(Color.white);

		deleteStudent.setBackground(Color.pink);

		updateStudent.setBackground(Color.white);

		viewStudent.setBackground(Color.pink);



		tabpane.addTab("Add New Student", addStudent);

		tabpane.addTab("Delete Student", deleteStudent);

		tabpane.addTab("Update Student's Phone Number", updateStudent);

		tabpane.addTab("View Student's Information", viewStudent);
		
		insertInfo.addActionListener(this);
		deleteBtn.addActionListener(this);
		updateBtn.addActionListener(this);
		viewBtn.addActionListener(this);



		getContentPane().add(tabpane, BorderLayout.CENTER);

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setSize(700,400);

		setVisible(true);
		//check db connected and toast message
		dbconnectionCheck(); 




	}



	@Override
	public void actionPerformed(ActionEvent e) {
		//Event Perform
		if(e.getSource() == insertInfo){
			addStudentInfo();
		}else if(e.getSource()== deleteBtn){
			deleteStudentById();
		}
	}

	public void initialize(){



	}


	void dbconnectionCheck(){

		//DB Connection check

		try {

			//JDBC Driver -> DriverManager

			Class.forName("com.mysql.jdbc.Driver").newInstance();

			String url = "jdbc:mysql://127.0.0.1:3306/sook?";



			//get Connection from Driver

			conn = DriverManager.getConnection(url, "root", "apmsetup");

			//get Statement from Connection Object

			stat = conn.createStatement();

			initialize();

			System.out.println("DB Connect!!");

		} catch (Exception e) {

			e.printStackTrace(System.out);

		}

	}

	boolean studentIdExist(String id){

		String IdExistSql = "IF EXIST(SELECT * FROM student WHERE id=?) return true;"
				+ " ELSE return false";
		boolean idExist = false;

		try {
			PreparedStatement p = conn.prepareStatement(IdExistSql);
			p.setString(1, id);
			idExist = p.execute();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		if(idExist){
			return true;
		}
		else
			return false;
	}


	void addStudentInfo(){
		try{
			String id = valID.getText().trim();
			String name = valName.getText().trim();
			String department = valDept.getText().trim();
			String phone = valPhone.getText().trim();
			if( id == null || name == null || department == null || valPhone == null ||
					id.length() == 0 || name.length() == 0 ||department.length() == 0 || phone.length() == 0)
				return ;
			//데이터베이스에 동일한 사용자ID가 있는지 확인
			ResultSet rs2=stat.executeQuery("select id from student where id="+id);
			if(rs2.next()) { 	
				//textarea이름.setText("이미 등록되어있는 학생 ID 입니다.");
				JOptionPane.showConfirmDialog(addStudent, "id", "Title", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null);
			}

			String sql = "insert into student values(?,?,?,?)";
			//Statement의 메소드를 이용해서 SQL문의 실행
			PreparedStatement stat=conn.prepareStatement(sql);
			stat.setString(1, id);
			stat.setString(2, name);
			stat.setString(3, department );
			stat.setString(4, phone);
			stat.executeUpdate();
			//clear();
			System.out.println("student data insert success");
		}catch(Exception ex){

		}

	}

	void deleteStudentById(){
		try{
			String id = valID2.getText().trim();
			//삭제할 id 잘 받아왔는지 확인 
			//System.out.println(valID2);
			if(id == null || id.length() == 0)
				return ;
			//데이터베이스에 동일한 사용자ID가 있는지 확인
			ResultSet rs2=stat.executeQuery("select id from student where id="+id);
			if(rs2.next()) { 	
				//이름.setText("이미 등록되어있는 학생 ID 입니다.");
			}
			//data delete query
			stat.executeUpdate("delete from student where id=' " + id + " ' ");

			System.out.println("student data delete success ");	
		}catch(Exception e1){

		}



	}

	void viewStudentById(){

		if(studentIdExist(valID3.toString())){
			//jtable use
		}
		else{
			//id not existing
		}

	}

	void updateStudentById(){

		System.out.println("this the test");

	}



	public static void main(String args[]) {

		studentManagement test = new studentManagement();



	}

}