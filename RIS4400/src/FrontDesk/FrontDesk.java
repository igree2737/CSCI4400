package FrontDesk;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DateTimeStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;

import javax.swing.text.MaskFormatter;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;

public class FrontDesk {
	
	private static Connection con;
	public static Statement smt;
	
	java.util.Date date;
	java.sql.Date sqlDate;
	DateFormat formatter = new SimpleDateFormat("mm/dd/yyyy");
	DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	String pattern = "dd/MM/yyyy";
	
	Parent root;
	Stage stage;
	ObservableList<patientData>patientRecord;
	ObservableList<patientData>emptyChecker;
	
	@FXML Button PatientButton;
	@FXML Button ConfirmPatientButton;
	@FXML Button ScheduleButton;
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
		
		String fname = FirstNameField.getText();
		if (fname.trim().equals(""))
			fname = "NONE";
		String lname = LastNameField.getText();
		if (lname.trim().equals(""))
			lname = "NONE";
		String DOB = DOBfield.getText();
		if (DOB.trim().equals(""))
			DOB = "NONE";
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
		
			
		String phoneMask= "###-###-####";
		
		//converts string to date
		//date = formatter.parse(DOB);
		//sqlDate = new java.sql.Date(date.getTime());

		MaskFormatter format = new MaskFormatter(phoneMask);
		format.setValueContainsLiteralCharacters(false);
		Num = format.valueToString(Num);
			
			
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
		patientRecord = FXCollections.observableArrayList();
		
		idcol.setCellValueFactory(new PropertyValueFactory<patientData,Integer>("Id"));
		
		firstNameCol.setCellValueFactory(new PropertyValueFactory<patientData,String>("firstName"));
		firstNameCol.setCellFactory(TextFieldTableCell.<patientData>forTableColumn());
        firstNameCol.setOnEditCommit(
                new EventHandler<CellEditEvent<patientData, String>>() {
                    @Override
                    public void handle(CellEditEvent<patientData, String> t) {
                        ((patientData) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setFirstName(t.getNewValue());
                    }
                }
                );
		lastNameCol.setCellValueFactory(new PropertyValueFactory<patientData,String>("lastName"));
		lastNameCol.setEditable(true);
		lastNameCol.setCellFactory(TextFieldTableCell.<patientData>forTableColumn());
        lastNameCol.setOnEditCommit(
                new EventHandler<CellEditEvent<patientData, String>>() {
                    @Override
                    public void handle(CellEditEvent<patientData, String> t) {
                        ((patientData) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setLastName(t.getNewValue());
                    }
                }
                );
		dobcol.setCellValueFactory(new PropertyValueFactory<patientData,Date>("Bdate"));
		dobcol.setCellFactory(TextFieldTableCell.<patientData, Date>forTableColumn(new StringConverter<Date>(){
			@Override
			public String toString(Date value){
				return (value);
			}
			@Override
			public Date fromString(String string){
				return null;
			}
		}));

		phoneCol.setCellValueFactory(new PropertyValueFactory<patientData,String>("num"));
		if(phoneCol.getText()=="" || phoneCol.getText()==null)
		{
			phoneCol.setCellFactory(TextFieldTableCell.<patientData>forTableColumn());
			phoneCol.setOnEditCommit(
					new EventHandler<CellEditEvent<patientData, String>>() {
						@Override
						public void handle(CellEditEvent<patientData, String> t) {
							((patientData) t.getTableView().getItems().get(
									t.getTablePosition().getRow())
									).setNum(t.getNewValue());
						}
					}
					);
		};
		addrCol.setCellValueFactory(new PropertyValueFactory<patientData,String>("address"));
		if(addrCol.getText()==""||addrCol.getText()==null)
		{
			addrCol.setCellFactory(TextFieldTableCell.<patientData>forTableColumn());
			addrCol.setOnEditCommit(
					new EventHandler<CellEditEvent<patientData, String>>() {
						@Override
						public void handle(CellEditEvent<patientData, String> t) {
							((patientData) t.getTableView().getItems().get(
									t.getTablePosition().getRow())
									).setAddress1(t.getNewValue());
						}
					}
					);
		};
		addrCol2.setCellFactory(TextFieldTableCell.<patientData>forTableColumn());
        if(addrCol2.getText()==""||addrCol2.getText()==null)
        {
        	addrCol2.setOnEditCommit(
        			new EventHandler<CellEditEvent<patientData, String>>() {
        				@Override
        				public void handle(CellEditEvent<patientData, String> t) {
        					((patientData) t.getTableView().getItems().get(
        							t.getTablePosition().getRow())
        							).setAddress2(t.getNewValue());
        				}
        			}
        			);
        };
		cityCol.setCellValueFactory(new PropertyValueFactory<patientData,String>("city"));
		TablePosition pos = archiveTable.getSelectionModel().getSelectedCells().get(0);
		int row = pos.getRow();
		patientData item = archiveTable.getItems().get(row);
		TableColumn col = pos.getTableColumn();
		String data = (String) col.getCellObservableValue(item).getValue();
		if(cityCol.getTableView().getItems())==""||cityCol.getText()==null)
		{
			cityCol.setCellFactory(TextFieldTableCell.<patientData>forTableColumn());
			cityCol.setOnEditCommit(
					new EventHandler<CellEditEvent<patientData, String>>() {
						@Override
	                    public void handle(CellEditEvent<patientData, String> t) {
	                        ((patientData) t.getTableView().getItems().get(
	                                t.getTablePosition().getRow())
	                                ).setCity(t.getNewValue());
	                    }
	                }
	                );
		};
		stateCol.setCellValueFactory(new PropertyValueFactory<patientData,String>("state"));
		if(stateCol.getText()==""||stateCol.getText()==null)
		{
			stateCol.setCellFactory(TextFieldTableCell.<patientData>forTableColumn());
	        stateCol.setOnEditCommit(
	                new EventHandler<CellEditEvent<patientData, String>>() {
	                    @Override
	                    public void handle(CellEditEvent<patientData, String> t) {
	                        ((patientData) t.getTableView().getItems().get(
	                                t.getTablePosition().getRow())
	                                ).setState(t.getNewValue());
	                    }
	                }
	                );
		};
		zipCol.setCellValueFactory(new PropertyValueFactory<patientData,Integer>("zip"));
		if(zipCol.)
		zipCol.setCellFactory(TextFieldTableCell.<patientData,Integer>forTableColumn(new IntegerStringConverter()));
        zipCol.setOnEditCommit(
                new EventHandler<CellEditEvent<patientData, Integer>>() {
                    @Override
                    public void handle(CellEditEvent<patientData, Integer> t) {
                        ((patientData) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                                ).setZip(t.getNewValue());
                    }
                }
                );
		
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
						rs.getString("addressOne"),rs.getString("addressCity"),rs.getString("addressState"),rs.getInt("addressZip")));
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		archiveTable.setItems(patientRecord);
	}
	
}
