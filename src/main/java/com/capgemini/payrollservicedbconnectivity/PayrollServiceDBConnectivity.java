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
			preparedStmt = con.prepareStatement(
					"select gender,count(gender),sum(basic_pay),avg(basic_pay),min(basic_pay),max(basic_pay) from employee_payroll group by GENDER;");
			employeePayroll = preparedStmt.executeQuery();
			while (employeePayroll.next()) {
				log.info(" " + employeePayroll.getString(1) + " " + employeePayroll.getInt(2) + " "
						+ employeePayroll.getDouble(3) + " " + employeePayroll.getDouble(4) + " "
						+ employeePayroll.getDouble(5) + " " + employeePayroll.getDouble(6));
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
