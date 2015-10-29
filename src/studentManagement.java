import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.*;

import javax.swing.JFrame;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;



public class studentManagement extends JFrame implements ActionListener{

	//db Connect
	Connection conn;
	Statement  stat;

	ResultSet rs = null;
	JTabbedPane tabpane; //Create the tab
	Button insertInfo,search,deleteBtn, search2,updateBtn, viewBtn;
	TextField valID, valID2, valID3, valID4, valName, valDept, valPhone, valNewPhone;


	JPanel addStudent;

	String index[] = {"ID","NAME","DEPARTMENT","PHONE"}; //table index create
	DefaultTableModel listmodel = new DefaultTableModel(index,0); //create table model
	JTable studentList = new JTable(listmodel); //view table create

	TextField inform, inform2;

	public studentManagement(){

		super("Student Management Program");
		tabpane = new JTabbedPane();

		addStudent = new JPanel();
		JPanel deleteStudent = new JPanel();
		JPanel updateStudent = new JPanel();
		JPanel viewStudent = new JPanel();

		////////////////////////////////////

		//UI Components Creation
		Panel IDpanel, NamePanel, DeptPanel, PhonePanel, IDpanel2, IDpanel3, phonePanel2;
		Label stID, stName, stDept, stPhone, stID2, stID3,newPhone, stID4;

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

		inform = new TextField("please search ID");	//please search ID

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

		inform2 = new TextField("please search ID");	//please search ID
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

		JPanel searchInfo = new JPanel();
		stID4 = new Label("Student ID");
		valID4 = new TextField(20);
		viewBtn = new Button("SEARCH");
		searchInfo.add(stID4);
		searchInfo.add(valID4);
		searchInfo.add(viewBtn);


		JScrollPane scroll = new JScrollPane(studentList);
		scroll.setSize(17,20);
		viewStudent.add(searchInfo,BorderLayout.NORTH);
		viewStudent.add(scroll,BorderLayout.SOUTH);
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
		search.addActionListener(this);
		search2.addActionListener(this);
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
		}else if(e.getSource()== viewBtn){
			viewStudentById(valID4.getText().trim());
		}else if(e.getSource()== search){
			searchId(valID2.getText().trim(),inform," exists, you can delete!");
		}else if(e.getSource()== search2){
			searchId(valID3.getText().trim(),inform2," exists, please update phone number!");
		}else if(e.getSource()== updateBtn){
			updateStudentById(valID3.getText().trim(),valNewPhone.getText().trim());
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
				//valID.setText(id);
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

	void viewStudentById(String id){

		String info[] = new String[4];
		DefaultTableModel model = (DefaultTableModel) studentList.getModel();
		try {
			String sql = "select * from student where id=' " + id + " ' ";
			PreparedStatement p = conn.prepareStatement(sql);
			ResultSet resultset = p.executeQuery(); 

			while(resultset.next()){

				info[0] = resultset.getString("id");
				info[1] = resultset.getString("name");
				info[2] = resultset.getString("department");
				info[3] = resultset.getString("phone");

				model.addRow(info);
				
			}
		}catch(Exception e1){

		}

	}

	void searchId(String id,TextField t,String message){

		try {
			String sql = "select name from student where id=' " + id + " ' ";
			PreparedStatement p = conn.prepareStatement(sql);
			ResultSet resultset = p.executeQuery(); 
			String update = "ID "+id+" not exists";
			while(resultset.next()){
				if(resultset.getString("name") != null){
					update= "ID "+ id + message;
				}
			}
			t.setText(update);
		}catch(Exception e1){

		}	

	}


	void updateStudentById(String id,String phone){
		try{

			String sql = "update student set phone = ? where id='"+id+"'";
			PreparedStatement stat=conn.prepareStatement(sql);
			stat.setString(1, phone);
			stat.executeUpdate();

		}catch(Exception ex){

		}

	}

	public static void main(String args[]) {

		studentManagement test = new studentManagement();

	}

}