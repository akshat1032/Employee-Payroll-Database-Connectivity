package com.capgemini.payrollservicedbconnectivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Driver;
import java.util.Enumeration;
import java.util.logging.*;

public class PayrollServiceDBConnectivity {

	private static Logger log = Logger.getLogger(PayrollServiceDBConnectivity.class.getName());

	public static void main(String[] args) throws DatabaseException {

		String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
		String userName = "root";
		String password = "123qwe";
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			log.info("Driver loaded");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Cannot find driver in classpath", e);
		}

		listOfDrivers();

		try {
			log.info("Connecting to database : " + jdbcURL);
			con = DriverManager.getConnection(jdbcURL, userName, password);
			log.info("Connection is successful : " + con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		PreparedStatement preparedStmt = null;
		ResultSet employeePayroll = null;
		try {
			// selecting employee using prepare statement
			preparedStmt = con.prepareStatement("select * from employee_payroll where start between cast('2018-01-01' as date) and date(now())");
			employeePayroll = preparedStmt.executeQuery();
			while (employeePayroll.next()) {
				log.info(employeePayroll.getInt(1) + " " + employeePayroll.getString(2) + " "
						+ employeePayroll.getString(3) + " " + employeePayroll.getString(4) + " "
						+ employeePayroll.getString(5) + " " + employeePayroll.getString(6) + " "
						+ employeePayroll.getDouble(7) + " " + employeePayroll.getDouble(8) + " "
						+ employeePayroll.getDouble(9) + " " + employeePayroll.getDouble(10) + " "
						+ employeePayroll.getDouble(11) + " " + employeePayroll.getDate(12));
			}
		} catch (SQLException e) {
			throw new DatabaseException("Error in retrieving data from database");
		}
	}

	// Printing the available classes
	private static void listOfDrivers() {
		Enumeration<Driver> driverList = DriverManager.getDrivers();
		while (driverList.hasMoreElements()) {
			Driver driverClass = (Driver) driverList.nextElement();
			log.info(" " + driverClass.getClass().getName());
		}
	}
}
