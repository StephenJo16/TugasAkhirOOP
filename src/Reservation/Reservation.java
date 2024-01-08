package Reservation;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import Main.DatabaseManager;

public class Reservation {
    private int id;
    private String customerName;
    private int tableSize;
    private String tableType;
    private int numberOfPeople;
    private String status;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public int getTableSize() {
		return tableSize;
	}
	public void setTableSize(int tableSize) {
		this.tableSize = tableSize;
	}
	public String getTableType() {
		return tableType;
	}
	public void setTableType(String tableType) {
		this.tableType = tableType;
	}
	public int getNumberOfPeople() {
		return numberOfPeople;
	}
	public void setNumberOfPeople(int numberOfPeople) {
		this.numberOfPeople = numberOfPeople;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Reservation(int id, String customerName, int tableSize, String tableType, int numberOfPeople,
			String status) {
		super();
		this.id = id;
		this.customerName = customerName;
		this.tableSize = tableSize;
		this.tableType = tableType;
		this.numberOfPeople = numberOfPeople;
		this.status = status;
	}

}