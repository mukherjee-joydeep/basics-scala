package commonLibs.automation;

/**
 * 
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * @author AGARWBI1
 * 
 */
public class WareHouseDBConnect {

	private static Logger Application_logs = Logger
			.getLogger("WareHouseDBConnect");
	public static String diff_fileName = null;
	static String driver_name = null;
	static String connection_url = null;
	Connection con = null;
	String username = "dMOB_IMSOne_User";
	String pass = "mSales1234";
	ResultSet rs = null;
	PreparedStatement preQuery = null;
	String[] names_DB = null;
	double[] values_DB = null;

	public static void main(String[] args) {
		Properties prop = new Properties();
		try {
			prop.load(new FileReader("./data/credentials.properties"));

		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		
		if(prop.getProperty("database").equalsIgnoreCase("Oracle")){
			driver_name = "oracle.jdbc.driver.OracleDriver";
			connection_url = "jdbc:oracle:thin:@//" + prop.getProperty("DB_host")
			+ ":" + prop.getProperty("DB_port") + "/"
			+ prop.getProperty("DB_SERVICE_NAME");
		}else if(prop.getProperty("database").equalsIgnoreCase("MSSQL")){
			driver_name = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			connection_url = "jdbc:sqlserver://" + prop.getProperty("DB_host")
			 + ";databaseName="
			+ prop.getProperty("DB_Name")+";integratedSecurity=true;";
			
			//connection_url = "jdbc:sqlserver://BLRW7PYCSY1\\SQLEXPRESS;databaseName=Testing;user=sa;password=sa@1234;";
		}
//		else{
//			driver_name = "oracle.jdbc.driver.OracleDriver";
//			connection_url = "jdbc:oracle:thin:@" + prop.getProperty("DB_host")
//			+ ":" + prop.getProperty("DB_port") + ":"
//			+ prop.getProperty("DB_SID");
//		}
		
		Application_logs.info(connection_url);
		WareHouseDBConnect obj_schema1 = null;
		WareHouseDBConnect obj_schema2 = null;
		/*
		 * uname : ONE_UCB_CLNT_INTG1 ONE_UCB_CLNT_INTG2
		 * 
		 * pw is 01 after the uname
		 */

		BufferedReader br = null;

		/**
		 * opening CSV file for Level 1 of queries which contains query and
		 * other details related to dynamic query building
		 */
		try {
			br = new BufferedReader(new FileReader("./data/tableName.csv"));
		} catch (FileNotFoundException e) {

			Application_logs.error("Table Name CSV error :", e);
		}
		String line = null;
		try {

			excelFileUpdate excel_new = new excelFileUpdate();

			Map<String, Object[]> data = new HashMap<String, Object[]>();
			data.put(
					String.valueOf(0),
					new Object[] {
							"Table Name",
							"Count from " + prop.getProperty("schema_name_1")
									+ " (IMS One)",
							"Count from " + prop.getProperty("schema_name_2")
									+ " (BIDW)" });

			/**
			 * connecting to DB schema 1
			 */
			obj_schema1 = new WareHouseDBConnect(
					prop.getProperty("username_schema1"),
					prop.getProperty("password_schema1"));
			obj_schema1.connectDB();

			/**
			 * connecting to DB schema 2
			 */
			obj_schema2 = new WareHouseDBConnect(
					prop.getProperty("username_schema2"),
					prop.getProperty("password_schema2"));
			obj_schema2.connectDB();

			int i = 1;
			while ((line = br.readLine()) != null) {

				String count_schema1, count_schema2 = null;
				/**
				 * executing the count query in schema 1
				 */
				Application_logs.info("select count(*) from "
						+ prop.getProperty("schema_name_1") + "." + line);
				count_schema1 = obj_schema1
						.queryConstructDB("select count(*) from "
								+ prop.getProperty("schema_name_1") + "."
								+ line);

				/**
				 * executing the count query in schema 2
				 */
				Application_logs.info("select count(*) from "
						+ prop.getProperty("schema_name_2") + "." + line);
				count_schema2 = obj_schema2
						.queryConstructDB("select count(*) from "
								+ prop.getProperty("schema_name_2") + "."
								+ line);

				Application_logs.info(line + "," + count_schema1 + ","
						+ count_schema2);
				data.put(String.valueOf(i++), new Object[] { line,
						count_schema1, count_schema2 });
				// i++;
			}

			/**
			 * writing to excel
			 */
			excel_new.excelAddData_NewWay("./output/Result_Table_Count.xlsx",
					data);

			br.close();
			br = null;
			/**
			 * Grant permission to both the schemas
			 * 
			 */

			try {
				br = new BufferedReader(new FileReader("./data/tableName.csv"));
			} catch (FileNotFoundException e) {

				Application_logs.error("Table Name CSV error :", e);
			}
			line = null;
			while ((line = br.readLine()) != null) {

				/**
				 * grant access to schema 2 from schema 1 connection
				 */
				obj_schema1.queryConstructDB_Matching("GRANT SELECT ON "
						+ prop.getProperty("schema_name_1") + "." + line
						+ " TO " + prop.getProperty("schema_name_2"));

				/**
				 * grant access to schema 1 from schema 2 connection
				 */
				obj_schema2.queryConstructDB_Matching("GRANT SELECT ON "
						+ prop.getProperty("schema_name_2") + "." + line
						+ " TO " + prop.getProperty("schema_name_1"));

			}
			br.close();

			/**
			 * starting with Test case 2
			 * 
			 * Matching records and saving the difference in files
			 * 
			 */
			br = null;
			try {
				br = new BufferedReader(new FileReader("./data/Query1.csv"));
			} catch (FileNotFoundException e) {
				Application_logs.error("Table Name CSV error :", e);
			}
			String Query_line = null;
			String[] queryString = null;
			int j = 1;
			Map<String, Object[]> data_matching = new HashMap<String, Object[]>();

			while ((Query_line = br.readLine()) != null) {
				queryString = Query_line.split(",", 2);
				// System.out.println(queryString[0] + "\t" + queryString[1]);

				String schema1Query = null;
				schema1Query = queryString[1].replace(
						queryString[0],
						prop.getProperty("schema_name_1") + "."
								+ queryString[0]).replace("\"", "");

				String schema2Query = null;
				schema2Query = queryString[1].replace(
						queryString[0],
						prop.getProperty("schema_name_2") + "."
								+ queryString[0]).replace("\"", "");

				System.out.println(schema1Query + " MINUS " + schema2Query);

				data_matching.put(
						String.valueOf(0),
						new Object[] {
								"Table Name",
								prop.getProperty("schema_name_1") + " MINUS "
										+ prop.getProperty("schema_name_2"),
								prop.getProperty("schema_name_2") + " MINUS "
										+ prop.getProperty("schema_name_1") });

				String result_match_schema1_2, result_match_schema2_1 = null;
				/**
				 * executing the count query in schema 1 - schema 2
				 */
				int count = 0;
				diff_fileName = queryString[0] + "_Schema_1_2";
				count = obj_schema1.queryConstructDB_Matching(schema1Query
						+ " MINUS " + schema2Query);
				if (count == 0)
					result_match_schema1_2 = "Matched";

				else
					result_match_schema1_2 = "Not Matched - Count " + count;

				/**
				 * executing the count query in schema 2 - schema 1
				 */
				diff_fileName = queryString[0] + "_Schema_2_1";
				count = obj_schema2.queryConstructDB_Matching(schema2Query
						+ " MINUS " + schema1Query);
				if (count == 0)
					result_match_schema2_1 = "Matched";
				else
					result_match_schema2_1 = "Not Matched - Count " + count;

				data_matching.put(String.valueOf(j++), new Object[] {
						queryString[0], result_match_schema1_2,
						result_match_schema2_1 });

			}
			/**
			 * writing to excel
			 */
			excel_new.excelAddData_NewWay(
					"./output/Result_Table_Matching.xlsx", data_matching);

			br.close();
			br = null;

			/**
			 * Revoke permission from both the schemas
			 * 
			 */

			try {
				br = new BufferedReader(new FileReader("./data/tableName.csv"));
			} catch (FileNotFoundException e) {

				Application_logs.error("Table Name CSV file not found  :" + e);
			}
			line = null;
			while ((line = br.readLine()) != null) {

				/**
				 * Revoke access from schema 2 to schema 1 connection
				 */
				obj_schema1.queryConstructDB_Matching("REVOKE  SELECT ON "
						+ prop.getProperty("schema_name_1") + "." + line
						+ " FROM " + prop.getProperty("schema_name_2"));

				/**
				 * Revoke access from schema 1 to schema 2 connection
				 */
				obj_schema2.queryConstructDB_Matching("REVOKE  SELECT ON "
						+ prop.getProperty("schema_name_2") + "." + line
						+ " FROM " + prop.getProperty("schema_name_1"));

			}

			/**
			 * Test case 3
			 */

			br.close();
			br = null;

			/**
			 * opening CSV file for Level 1 of queries which contains query and
			 * other details related to dynamic query building
			 */
			try {
				br = new BufferedReader(new FileReader(
						"./data/tableName_BusinessKey.csv"));
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			}
			line = null;

			excel_new = null;

			excel_new = new excelFileUpdate();

			Map<String, Object[]> data_BusinessKey = new HashMap<String, Object[]>();
			data_BusinessKey.put(
					String.valueOf(0),
					new Object[] {
							"Table Name",
							"Key count from "
									+ prop.getProperty("schema_name_1")
									+ " (IMS One)",
							"Key count from "
									+ prop.getProperty("schema_name_2")
									+ " (BIDW)" });

			obj_schema1 = null;
			/**
			 * connecting to DB schema 1
			 */
			obj_schema1 = new WareHouseDBConnect(
					prop.getProperty("username_schema1"),
					prop.getProperty("password_schema1"));
			//obj_schema1.connectDB();

			obj_schema2 = null;
			/**
			 * connecting to DB schema 2
			 */
			obj_schema2 = new WareHouseDBConnect(
					prop.getProperty("username_schema2"),
					prop.getProperty("password_schema2"));
			//obj_schema2.connectDB();

			i = 1;
			while ((line = br.readLine()) != null) {

				queryString = line.split(",", 2);
				String count_schema1, count_schema2 = null;
				/**
				 * executing the count query in schema 1
				 */
				count_schema1 = obj_schema1
						.queryConstructDB("SELECT COUNT(*) FROM (SELECT DISTINCT "
								+ queryString[1]
								+ " FROM "
								+ prop.getProperty("schema_name_1")
								+ "."
								+ queryString[0] + ")");
				// System.out.println(count_schema1);

				/**
				 * executing the count query in schema 2
				 */
				count_schema2 = obj_schema2
						.queryConstructDB("SELECT COUNT(*) FROM (SELECT DISTINCT "
								+ queryString[1]
								+ " FROM "
								+ prop.getProperty("schema_name_2")
								+ "."
								+ queryString[0] + ")");

				data_BusinessKey.put(String.valueOf(i++), new Object[] {
						queryString[0], count_schema1, count_schema2 });

				// System.out.println(count_schema2);

			}

			/**
			 * writing to excel
			 */
			excel_new.excelAddData_NewWay(
					"./output/Result_Table_Business_Count.xlsx",
					data_BusinessKey);

		} catch (IOException e1) {

			e1.printStackTrace();
		} finally {
			try {
				obj_schema1.con.close();
				obj_schema1.preQuery.close();
				obj_schema1.rs.close();
				obj_schema2.con.close();
				obj_schema2.preQuery.close();
				obj_schema2.rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Default constructor of this class to connect DB
	 */
	public WareHouseDBConnect() {
		boolean status = connectDB();
		if (!status) {
			Application_logs.info("DB connection error");
		}
	}

	/**
	 * Constructor of this class when we need to change username and password of
	 * DB connection
	 * 
	 * @param username
	 * @param password
	 */
	public WareHouseDBConnect(String username, String password) {
		this.username = username;
		this.pass = password;
		boolean status = connectDB();
		if (!status) {
			Application_logs.info("DB connection error");
		}
	}

	/**
	 * Make Connection to DB and store it to a Connection object
	 * 
	 * @return true or false based on Connection to DB made or not
	 */
	public boolean connectDB() {

		try {
			Class.forName(driver_name);
		} catch (ClassNotFoundException e) {
			Application_logs.error(e);
			e.printStackTrace();
		}
		try {
			con = DriverManager.getConnection(connection_url, username, pass);

		} catch (SQLException e) {
			Application_logs.error(e);
			try {
				con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		if (con == null) {
			Application_logs.error("Connection problem with DB");
			return false;
		}

		else {
			Application_logs.info("Connection to DB made");
			return true;
		}
	}

	public void executeQueryDB(String moduleName, String metricName,
			String level, String[] param_values) {

		BufferedReader br = null;
		// BufferedReader br1 = null;
		/**
		 * opening CSV file for Level 1 of queries which contains query and
		 * other details related to dynamic query building
		 */
		try {
			br = new BufferedReader(new FileReader("./data/Query1.csv"));
		} catch (FileNotFoundException e) {
			Application_logs.error(e);
			e.printStackTrace();
		}
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				// String[] param_name_temp = null;
				String[] data_temp = null;
				// String[] param_values = null;
				// line = br.readLine();
				data_temp = line.split(",", 6);
				/**
				 * checking whether module Name, metric Name are matching with
				 * the line from the CSV file row
				 */
				if (data_temp[0].contains(moduleName)) {
					// System.out.println(level);
					// System.out.println("ModuleName level:"+line);
					if (data_temp[1].equals(level)) {
						// System.out.println("Level level:"+line);
						if (data_temp[2].equals(metricName)) {

							// System.out.println("Last level:"+line);
							Application_logs.info(line);
							/**
							 * getting loop to run
							 */
							// int loop = Integer.parseInt(data_temp[3]);

							/**
							 * getting the parameters defined in level 1 file
							 */
							if (data_temp[4].equals("null")) {
								/*
								 * if (data_temp[4].contains(";")) {
								 * param_name_temp = data_temp[4].split(";"); }
								 * else { //param_name_temp = new String[1];
								 * //param_name_temp[0] = data_temp[4]; }
								 */

								// param_values = new
								// String[param_name_temp.length];
								Application_logs
										.info("Start Executing query without parameters");
								queryConstructDB(data_temp[5]);

							} else {

								Application_logs
										.info("Start Executing query with parameters");
								// System.out.println("CPO :"+data_temp[5]);
								// System.out.println("Param Name :"+data_temp[4]);
								queryConstructDB(data_temp[5], param_values);
							}
							/*
							 * try { param_values = new
							 * String[param_name_temp.length][]; int i = 0; for
							 * (String param_temp : param_name_temp) {
							 *//**
							 * opening CSV file - level 2 of queries which
							 * contains brand Names and region Names etc.
							 */
							/*
							 * try { br1 = new BufferedReader(new FileReader(
							 * "./data/Queries_Level2.csv")); } catch
							 * (FileNotFoundException e) {
							 * 
							 * e.printStackTrace(); } String line1 = null;
							 * 
							 * while ((line1 = br1.readLine()) != null) {
							 * 
							 * System.out.println(line1);
							 *//**
							 * searching whether the brand Name or region
							 * Name is present in the line or not if we got that
							 * in the line we are splitting them to get all the
							 * brand Names
							 */
							/*
							 * if (line1.contains(param_temp)) { param_values[i]
							 * = line1 .split(","); } } br1.close(); i++; }
							 *//**
							 * end of the above loop, we will have all brands
							 * with their values in param values
							 */
							/*
							 * } catch (IOException e1) { e1.printStackTrace();
							 * }
							 */

						}
					}
				}
				line = null;
			}
		} catch (IOException e1) {

			e1.printStackTrace();
		}

	}

	/**
	 * Use the Connection object and construct SQL queries
	 */
	public void queryConstructDB(String sql, String[] brandName) {
		/**
		 * brand Name is double dimensional array Need to do that in details
		 */
		/*
		 * if (brandName == null) { queryConstructDB(sql); } else {
		 */
		try {
			if (!con.isClosed()) {
				preQuery = con.prepareStatement(sql,
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				for (int i = 0; i < brandName.length; i++) {
					preQuery.setString((i + 1), brandName[i]);
					// System.out.println(brandName[i]);
				}
			} else {
				// System.out.println("Connection lost");
			}
			queryRunDB();

		} catch (SQLException e) {
			Application_logs.error(e);
			try {
				con.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			e.printStackTrace();
		}
		// }
	}

	/**
	 * Use the Connection object and construct SQL queries
	 */
	/*
	 * public void queryConstructDB(String sql, String[][] brandName) {
	 * 
	 * 
	 * // Statement stmt = null;
	 * 
	 * // sql="Select * from DIM_DETAIL_POSITION";
	 * 
	 * sql = "select c.global_name, a.CONGRESS_ACTV_57 " // , a.TGT_CONGRESS_62
	 * // Target +
	 * "from FACT A join dim_brand B on a.brand_sid = b.brand_sid join dim_geography"
	 * +
	 * " C on A.geo_sid=c.geo_sid where a.period_sid in (?) and b.brand_name in (?) "
	 * +
	 * "and c.global_name in ('Worldwide') and c.geo_lvl = 'GLOBAL' and b.brand_lvl = 'BRAND' and a.feed_id in (18,19) and"
	 * +
	 * " b.status = 'C' and c.status = 'C' and b.is_dummy = 'N' and c.is_dummy = 'Y'"
	 * ;
	 *//**
	 * brand Name is double dimensional array Need to do that in details
	 */
	/*
	 * 
	 * if (brandName.length == 1) { for (String brand_temp : brandName[0]) {
	 * if(brand_temp.equals(brandName[0][0])){ continue; } try {
	 * System.out.println(brand_temp); preQuery =
	 * con.prepareStatement(sql,ResultSet
	 * .TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
	 * 
	 * preQuery.setString(1, brand_temp);
	 * System.out.println(preQuery.toString()); queryRunDB();
	 * 
	 * } catch (SQLException e) {
	 * 
	 * e.printStackTrace(); } } } if (brandName.length == 2) {
	 * 
	 * for (String brand_temp : brandName[0]) {
	 * if(brand_temp.equals(brandName[0][0])){ continue; } try { preQuery =
	 * con.prepareStatement
	 * (sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
	 * preQuery.setString(1, brand_temp);
	 * 
	 * 
	 * for (String region_temp : brandName[1]) {
	 * 
	 * if(region_temp.equals(brandName[1][0])){ continue; }
	 * 
	 * preQuery.setString(2, region_temp);
	 * System.out.println(preQuery.toString()); queryRunDB(); }
	 * 
	 * } catch (SQLException e) {
	 * 
	 * e.printStackTrace(); }
	 * 
	 * } } }
	 */

	public String queryConstructDB(String sql) {
		String temp = null;
		try {
			preQuery = con.prepareStatement(sql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			Application_logs.info(sql);
			// queryRunDB();
			temp = queryRunDB_count();

		} catch (SQLException e) {
			Application_logs.error(e);
			// try {
			// con.close();
			// } catch (SQLException e1) {
			// Application_logs.error(e1);
			// e1.printStackTrace();
			// }
			e.printStackTrace();
		}
		return temp;
	}

	public int queryConstructDB_Matching(String sql) {
		int temp = 0;
		try {

			preQuery = con.prepareStatement(sql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			if (sql.contains("GRANT") || sql.contains("REVOKE")) {
				preQuery.execute();
				return 0;
			}
			Application_logs.info(sql);
			// temp = queryRunDB();
			temp = queryRunDB();

		} catch (SQLException e) {
			Application_logs.error(e);
			// try {
			// con.close();
			// } catch (SQLException e1) {
			// Application_logs.error(e1);
			// e1.printStackTrace();
			// }
			e.printStackTrace();
		}
		return temp;
	}

	public String queryRunDB_count() {

		try {
			rs = preQuery.executeQuery();
		} catch (SQLException e) {
			Application_logs.error(e);
			try {
				con.close();
			} catch (SQLException e1) {
				Application_logs.error(e1);

			}
			e.printStackTrace();
		}
		try {
			// int count = 0;
			// rs.last();
			// count = rs.getRow();

			/**
			 * printing the count of rows in ResultSet
			 */
			while (rs.next()) {
				Application_logs.info(rs.getString(1));
				return (rs.getString(1));
			}

			// rs.beforeFirst();
		} catch (SQLException e) {
			Application_logs.error(e);

		}
		// finally {
		// try {
		// preQuery.close();
		// rs.close();
		// // con.close();
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// }
		return null;
	}

	public int queryRunDB() {

		Map<String, Object[]> data_Not_Matching = new HashMap<String, Object[]>();
		int count = 0;
		try {
			rs = preQuery.executeQuery();

		} catch (SQLException e) {
			Application_logs.error(e);
			try {
				con.close();
			} catch (SQLException e1) {
				Application_logs.error(e1);
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		try {

			rs.last();
			count = rs.getRow();

			// System.out.println("ResultSet count : " + count);

			rs.beforeFirst();
			if (count == 0)
				return count;
			else {
				StringBuilder dataRow = new StringBuilder();
				int j = 1;
				while (rs.next()) {
					ResultSetMetaData rsMd = rs.getMetaData();

					int column_no = rsMd.getColumnCount();
					// if (rs.isFirst())
					// System.out.println("Column count : " + column_no);

					if (rs.isFirst()) {
						// for (int i = 1; i <= column_no; i++) {
						// System.out.println("Column typeName : "
						// + rsMd.getColumnTypeName(i)
						// + " Column Name: " + rsMd.getColumnName(i)
						// + " Column Label : "
						// + rsMd.getColumnLabel(i));
						//
						// }

						// System.out.println();
						for (int i = 1; i <= column_no; i++)
							dataRow.append(rsMd.getColumnLabel(i) + ",");

						data_Not_Matching.put(String.valueOf(0),
								new Object[] { new String(dataRow) });

						System.out.println();
					}

					dataRow = null;
					dataRow = new StringBuilder();

					for (int i = 1; i <= column_no; i++) {

						/**
						 * based on the type print it
						 */

						if (rsMd.getColumnTypeName(i).equals("TIMESTAMP"))
							dataRow.append(rs.getTimestamp(i) + ",");

						if (rsMd.getColumnTypeName(i).equals("INTEGER"))
							dataRow.append(rs.getInt(i) + ",");

						if (rsMd.getColumnTypeName(i).equals("NUMBER"))
							dataRow.append(rs.getLong(i) + ",");

						if (rsMd.getColumnTypeName(i).contains("VARCHAR"))
							dataRow.append(rs.getString(i) + ",");

						if (rsMd.getColumnTypeName(i).equals("DATE"))
							dataRow.append(rs.getDate(i) + ",");

						// System.out.println()
					}
					// System.out.println();

					data_Not_Matching.put(String.valueOf(j++),
							new Object[] { new String(dataRow) });

				}

				/**
				 * writing to excel
				 */
				if (count < 10000) {
					excelFileUpdate excel_new = new excelFileUpdate();
					excel_new.excelAddData_NewWay("./output/" + diff_fileName
							+ ".xlsx", data_Not_Matching);
				} else {
					StringBuilder sb = new StringBuilder();
					userAutomationLibrary uAl = new userAutomationLibrary();
					Set<String> newRows = data_Not_Matching.keySet();
					for (int i = 0; i < newRows.size(); i++) {
						Object[] objArr = data_Not_Matching.get(String
								.valueOf(i));
						for (Object obj : objArr) {
							sb.append((String) obj);
						}
						sb.append('\n');
					}
					uAl.csvFileWrite(diff_fileName, sb);
				}

			}
			// count = 0;
			// while (rs.next()) {
			// String name = null;
			// Double value = null;
			// for (int i = 1; i < 3; i++) {
			// Application_logs.info(rs.getString(i));
			// if (i == 1)
			// name = rs.getString(i);
			// if (i == 2) {
			// double temp;
			// if (rs.getString(i) == null)
			// temp = 0;
			// else
			// temp = Double.parseDouble(rs.getString(i));
			// value = (double) Math.round(temp * 100) / 100;
			// }
			//
			// }
			// names_DB[count] = name;
			// values_DB[count] = value;
			// count++;
			// }
			// preQuery.close();
			// rs.close();
			// con.close();

		} catch (SQLException e) {
			Application_logs.error(e);

		}
		// finally {
		// try {
		// preQuery.close();
		// rs.close();
		// con.close();
		// } catch (SQLException e) {
		//
		// }
		return count;
		// }

	}

	public void getQueryFile() {
		BufferedReader br = null;
		// BufferedReader br1 = null;
		/**
		 * opening CSV file for Level 1 of queries which contains query and
		 * other details related to dynamic query building
		 */
		Map<Integer, List<String>> queries = new LinkedHashMap< Integer, List<String>>();
			
		
		
		/**
		 * No of rules to be executed
		 */
		for (int i = 1; i <= 12; i++) {
			try {
				br = new BufferedReader(new FileReader("./data/Query.csv"));
			} catch (FileNotFoundException e) {
				Application_logs.error(e);
			}
			String line=null;
			String[] param = new String[3];
			try {
				
				while ((line = br.readLine()) != null) {
					param = line.split(";");
					if (Integer.parseInt(param[2]) == i) {
						List<String> currentValue = queries.get(i);
					    if (currentValue == null) {
					        currentValue = new ArrayList<String>();
					        queries.put(i, currentValue);
					    }					    
					    currentValue.add(param[1]);
					}
					queryConstructDB(line);
				}

				line = null;

			} catch (IOException e1) {

				e1.printStackTrace();
			}
		}
	}

//	public void add(Integer key, String newValue) {
//	    List<String> currentValue = map.get(key);
//	    if (currentValue == null) {
//	        currentValue = new ArrayList<String>();
//	        map.put(key, currentValue);
//	    }
//	    currentValue.add(newValue);
//	}
	
	public void resultSetWriteDB() {

	}
}
