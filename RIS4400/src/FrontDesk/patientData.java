package FrontDesk;

import javafx.beans.property.StringProperty;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.text.MaskFormatter;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class patientData {
	public IntegerProperty Id;
	public StringProperty firstName;
	public StringProperty lastName;
	public SimpleObjectProperty<Date> Bdate;
	public StringProperty num;
	public StringProperty address1;
	public StringProperty address2;
	public StringProperty city;
	public StringProperty state;
	public IntegerProperty zip;
	DateFormat formatter = new SimpleDateFormat("mm/dd/yyyy");
	
	public patientData(int ID,String firstname,String lastname,Date dob,String Num,String address1,String city,
			String state,int zip)
	{
		this.Id = new SimpleIntegerProperty(ID);
		this.firstName = new SimpleStringProperty(firstname);
		this.lastName = new SimpleStringProperty(lastname);
		this.Bdate = new SimpleObjectProperty<Date>(dob);
		this.num = new SimpleStringProperty(Num);
		this.address1 = new SimpleStringProperty(address1);
		this.city = new SimpleStringProperty(city);
		this.state = new SimpleStringProperty(state);
		this.zip = new SimpleIntegerProperty(zip);
	}
	public int getId()
	{
		return Id.get();
	}
	public void setId(int id)
	{
		Id.set(id);
	}
	public String getFirstName()
	{
		return firstName.get();
	}
	public void setFirstName(String firstname)
	{
		firstName.set(firstname);
	}
	public String getLastName()
	{
		return lastName.get();
	}
	public void setLastName(String lastname)
	{
		lastName.set(lastname);
	}
	public Date getBdate()
	{
		return Bdate.get();
	}
	public void setBdate(Date dob) throws ParseException
	{

		Bdate.set(dob);;
	}
	public void setNum(String num)
	{
		String phoneMask= "###-###-####";
		MaskFormatter format = null;
		try {
			format = new MaskFormatter(phoneMask);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		format.setValueContainsLiteralCharacters(false);
		try {
			num = format.valueToString(num);
			this.num.set(num);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setAddress1(String addr)
	{
		address1.set(addr);
	}
	public void setAddress2(String addr)
	{
		address2.set(addr);
	}
	public void setCity(String city)
	{
		this.city.set(city);
	}
	public void setState(String state)
	{
		this.state.set(state);
	}
	public void setZip(int zip)
	{
		this.zip.set(zip);
	}
	public IntegerProperty idProperty()
	{
		return Id;
	}
	public StringProperty firstNameProperty()
	{
		return firstName;
	}
	public StringProperty lastNameProperty()
	{
		return lastName;
	}
	public ObjectProperty<Date> bdateProperty()
	{
		return Bdate;
	}
	public StringProperty numProperty()
	{
		return num;
	}
	public StringProperty address1Property()
	{
		return address1;
	}
	public StringProperty address2Property()
	{
		return address2;
	}
	public StringProperty cityProperty()
	{
		return city;
	}
	public StringProperty stateProperty()
	{
		return state;
	}
	public IntegerProperty zipProperty()
	{
		return zip;
	}

}
