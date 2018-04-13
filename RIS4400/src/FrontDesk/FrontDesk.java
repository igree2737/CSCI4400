package FrontDesk;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import javax.swing.text.MaskFormatter;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class FrontDesk {
	
	private static Connection con;
	public static Statement smt;
	
	java.util.Date date;
	java.sql.Date sqlDate;
	
	DateFormat formatter = new SimpleDateFormat("mm/dd/yyyy");
	DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	Parent root;
	Stage stage;
	
	ObservableList<patientData>patientRecord = FXCollections.observableArrayList();
	ObservableList<String>options;
	
	@FXML Button PatientButton;
	@FXML Button ConfirmPatientButton;
	@FXML Button ScheduleButton;
	@FXML Button searchButton;
	@FXML TextField FirstNameField;
	@FXML TextField LastNameField;
	@FXML TextField	IDfield;
	@FXML TextField DOBfield;
	@FXML TextField GenderField;
	@FXML TextField NumberField;
	@FXML TextField CityField;
	@FXML TextField StateField;
	@FXML TextField AddrField1;
	@FXML TextField AddrField2;
	@FXML TextField ZIPfield;
	@FXML TextField fnameSearch;
	@FXML TextField	lnameSearch;
	@FXML TextField bdaySearch;
	@FXML TextField	phoneSearch;
	@FXML TextField citySearch;
	@FXML TextField stateSearch;
	@FXML TextField	zipSearch;
	@FXML TextField searchText;
	@FXML TableView<patientData> archiveTable;
	@FXML TableColumn<patientData,Integer> idcol;
	@FXML TableColumn<patientData,String> firstNameCol;
	@FXML TableColumn<patientData,String> lastNameCol;
	@FXML TableColumn<patientData,Date> dobcol;
	@FXML TableColumn<patientData,String> phoneCol;
	@FXML TableColumn<patientData,String> addrCol;
	@FXML TableColumn<patientData,String> addrCol2;
	@FXML TableColumn<patientData,String> cityCol;
	@FXML TableColumn<patientData,String> stateCol;
	@FXML TableColumn <patientData,Integer>zipCol;
	@FXML Tab archiveTab;
	@FXML ChoiceBox<String> choiceBox;
	
	//opens patient form in a new window
	public void patientwindow(ActionEvent event)throws Exception
	{
		try {
			System.out.println("New Patient");
			root = FXMLLoader.load(getClass().getResource("/FrontDeskView/FrontDeskNewPatient.fxml"));
			stage = new Stage();
			stage.setScene(new Scene(root));
			stage.show();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//connect to DB
	public void FDConnection(ActionEvent event) throws Exception
	{
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");  
			con=(Connection) DriverManager.getConnection(  
			"jdbc:mysql://localhost:3306/RISystem","root","admin123");  
			smt=(Statement) con.createStatement(); 
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		//getting text entered into textfields
		//if nothing was entered, sets a default value
		String fname = FirstNameField.getText();
		if (fname.trim().equals(""))
			fname = "NONE";
		String lname = LastNameField.getText();
		if (lname.trim().equals(""))
			lname = "NONE";
		String DOB = DOBfield.getText();
		if (DOB.trim().equals("")||DOB.trim().equals(null))
		{
			DOB = "00/00/0000";
			date = formatter.parse(DOB);
			sqlDate = new java.sql.Date(date.getTime());
		}
		else
		{
			date = formatter.parse(DOB);
			sqlDate = new java.sql.Date(date.getTime());

		}
		String gend = GenderField.getText().toUpperCase();
		if (gend.trim().equals(""))
			gend = "NA";
		String Num = NumberField.getText();
		if (Num.trim().equals(""))
			Num = "0000000000";
		String address1 = AddrField1.getText();
		if (address1.trim().equals(""))
			address1 = "NONE";
		String address2 = AddrField2.getText();
		if (address2.trim().equals(""))
			address2 = "NONE";
		String addresscity = CityField.getText();
		if (addresscity.trim().equals(""))
			addresscity = "NONE";
		String addressstate = StateField.getText();
		if (addressstate.trim().equals(""))
			addressstate = "NA";
		int Zip = -1;
		if(!ZIPfield.getText().isEmpty())
			Zip = Integer.parseInt(ZIPfield.getText());
		
		//formats phone numbers	
		String phoneMask= "###-###-####";
		MaskFormatter format = new MaskFormatter(phoneMask);
		format.setValueContainsLiteralCharacters(false);
		Num = format.valueToString(Num);
		//	
			
			//inserts data into patient table
			try {
				String sql = "INSERT INTO patient (fName,lName,dateOfBirth,gender,phoneNum,addressOne,addressTwo,"
						+ "addressCity,addressState,addressZip)"
						+ " VALUES (?,?,?,?,?,?,?,?,?,?)";
						
				PreparedStatement smt = con.prepareStatement(sql);
				
				smt.setString(1, fname);
				smt.setString(2, lname);
				smt.setDate(3, sqlDate); 
				smt.setString(4, gend);
				smt.setString(5, Num);
				smt.setString(6, address1);
				smt.setString(7, address2);
				smt.setString(8, addresscity);
				smt.setString(9, addressstate);
				smt.setInt(10, Zip);
				
				if(smt.executeUpdate() > 0)
				{
					System.out.println("Row inserted.");
					((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
				}
				else
				{
					System.out.println("Row not inserted.");
				}
				
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			finally{
				try {
					if (smt != null)
					{
						smt.close();
					}
					if (con != null)
					{
						con.close();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	
	//loads patient record from DB to the archive table
	public void loadFromDB()
	{
		//initializes the choicebox for the archive table
		options = FXCollections.observableArrayList("First Name","Last Name","Birth","Phone","City","State","Zip");
		choiceBox.setItems(options);
		choiceBox.setValue("First Name");
		//
		
		idcol.setCellValueFactory(new PropertyValueFactory<patientData,Integer>("Id"));
		
		firstNameCol.setCellValueFactory(new PropertyValueFactory<patientData,String>("firstName"));
		firstNameCol.setCellFactory(TextFieldTableCell.<patientData>forTableColumn());
        firstNameCol.setOnEditCommit(event -> {
        	patientData patient = event.getRowValue();
        	patient.setFirstName(event.getNewValue());
        	try {
				updateDB("fName",event.getNewValue(),patient.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
		lastNameCol.setCellValueFactory(new PropertyValueFactory<patientData,String>("lastName"));
		lastNameCol.setCellFactory(TextFieldTableCell.<patientData>forTableColumn());
        lastNameCol.setOnEditCommit(event -> {
        	patientData patient = event.getRowValue();
        	patient.setLastName(event.getNewValue());
        	try {
				updateDB("lName",event.getNewValue(),patient.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
		
        dobcol.setCellValueFactory(new PropertyValueFactory<patientData,Date>("Bdate"));
        //TO DO: Find a way to update Date type objects
        
		phoneCol.setCellValueFactory(new PropertyValueFactory<patientData,String>("num"));
		phoneCol.setCellFactory(TextFieldTableCell.<patientData>forTableColumn());
		phoneCol.setOnEditCommit(event -> {
        	patientData patient = event.getRowValue();
        	patient.setNum(event.getNewValue());
        	try {
				updateDB("phoneNum",event.getNewValue(),patient.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
		
		addrCol.setCellValueFactory(new PropertyValueFactory<patientData,String>("address1"));
		addrCol.setCellFactory(TextFieldTableCell.<patientData>forTableColumn());
		addrCol.setOnEditCommit(event -> {
        	patientData patient = event.getRowValue();
        	patient.setAddress1(event.getNewValue());
        	try {
				updateDB("addressOne",event.getNewValue(),patient.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
		addrCol2.setCellValueFactory(new PropertyValueFactory<patientData,String>("address2"));
		addrCol2.setCellFactory(TextFieldTableCell.<patientData>forTableColumn());
        addrCol2.setOnEditCommit(event -> {
        	patientData patient = event.getRowValue();
        	patient.setAddress2(event.getNewValue());
        	try {
				updateDB("addressTwo",event.getNewValue(),patient.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
        
		cityCol.setCellValueFactory(new PropertyValueFactory<patientData,String>("city"));
		cityCol.setCellFactory(TextFieldTableCell.<patientData>forTableColumn());
		cityCol.setOnEditCommit(event -> {
        	patientData patient = event.getRowValue();
        	patient.setCity(event.getNewValue());
        	try {
				updateDB("addressCity",event.getNewValue(),patient.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
		
		stateCol.setCellValueFactory(new PropertyValueFactory<patientData,String>("state"));
		stateCol.setCellFactory(TextFieldTableCell.<patientData>forTableColumn());
	    stateCol.setOnEditCommit(event -> {
        	patientData patient = event.getRowValue();
        	patient.setState(event.getNewValue());
        	try {
				updateDB("addressState",event.getNewValue(),patient.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
		
		zipCol.setCellValueFactory(new PropertyValueFactory<patientData,Integer>("zip"));
		zipCol.setCellFactory(TextFieldTableCell.<patientData,Integer>forTableColumn(new IntegerStringConverter()));
        zipCol.setOnEditCommit(event -> {
        	patientData patient = event.getRowValue();
        	patient.setZip(event.getNewValue());
        	try {
				updateDB("addressZip",event.getNewValue().toString(),patient.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
                
        try 
		{
			Class.forName("com.mysql.jdbc.Driver");  
			con=(Connection) DriverManager.getConnection(  
			"jdbc:mysql://localhost:3306/RISystem","root","admin123"); //change to whatever your username/password is 
			smt=(Statement) con.createStatement(); 
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		//fetches data from patient table
		try
		{
			String query = "SELECT * FROM patient";
			ResultSet rs = con.createStatement().executeQuery(query);
			while(rs.next())
			{
				patientRecord.add(new patientData(rs.getInt("patientID"),rs.getString("fName"),rs.getString("lName"),rs.getDate("dateOfBirth"),rs.getString("phoneNum"),
						rs.getString("addressOne"),rs.getString("addressTwo"),rs.getString("addressCity"),rs.getString("addressState"),rs.getInt("addressZip")));
			}
			
		}
		//
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		archiveSearch();
	}
	
	//Updates the database to reflect edited values
	public void updateDB(String column, String newValue, int id) throws SQLException
	{
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");  
			con=(Connection) DriverManager.getConnection(  
			"jdbc:mysql://localhost:3306/RISystem","root","admin123"); //change to whatever your username/password is 
			smt=(Statement) con.createStatement(); 
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		PreparedStatement stmt = con.prepareStatement("UPDATE patient SET "+column+" = ? WHERE patientID = ? ");
		stmt.setLong(1, Integer.parseInt(newValue));
        stmt.setInt(2, id);
        stmt.execute();
	}
	
	public void archiveSearch()
	{
		FilteredList<patientData> filteredData = new FilteredList<>(patientRecord, p -> true);
		searchText.textProperty().addListener((observable, oldValue, newValue) -> {
	            filteredData.setPredicate(p -> {
	                // If filter text is empty, display all persons.
	                if (newValue == null || newValue.isEmpty()) {
	                    return true;
	                }

	                // Compare first name and last name of every person with filter text.
	                String lowerCaseFilter = newValue.toLowerCase();

	                if (p.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
	                    return true; // Filter matches first name.
	                } else if (p.getLastName().toLowerCase().contains(lowerCaseFilter)) {
	                    return true; // Filter matches last name.
	                }
	                return false; // Does not match.
	            });
	        });
		SortedList<patientData> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(archiveTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        archiveTable.setItems(sortedData);
	}
	
}
