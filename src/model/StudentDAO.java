package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

class StudentDAO 
{
	
	public static class dbFactory
	{
		private static final String DB_URL = "jdbc:derby://localhost:64413/EECS;user=student;password=secret";
		private static dbFactory factory = null;
		
		private dbFactory() throws Exception
		{
			 Class.forName("org.apache.derby.jdbc.ClientDriver").getClass();
		}
		
		static synchronized dbFactory getFactory() throws Exception
		{
			if(factory == null)
			{
				factory = new dbFactory();
			}
			return factory;
		}
		
		Connection getConnection() throws Exception
		{
			 Connection con = DriverManager.getConnection(DB_URL);
			 return con;
			
		}
		
	}
	 
	static private dbFactory factory = null;
	
	private static String toTitleCase(String s)
	{
		if (s.isEmpty())
			return s;
		String result = s.toLowerCase();
		String firstLetter =result.substring(0, 1); 
		return result.replaceFirst(firstLetter, firstLetter.toUpperCase());
	}
	
	static List<StudentBean> query(String prefix, String minGpa, String sortBy) throws Exception
	{
		Connection conn = null;
		PreparedStatement s = null;
		ResultSet data = null;
		
		try
		{
			if(factory == null)
			{
				factory = new dbFactory();
			}
			conn = factory.getConnection();
			s = conn.prepareStatement("set schema roumani");
			s.executeUpdate();
			s.close();
			
			String sql = "SELECT * FROM SIS WHERE SURNAME LIKE ? AND GPA >= ?";  
			
			String name_prefix = prefix == null ? "" : toTitleCase(prefix);
			
			float minGpa_f = minGpa == null || minGpa.isEmpty() ? 0 : Float.parseFloat(minGpa);
			
			if(!sortBy.toUpperCase().equals("NONE"))
			{
				sql += " ORDER BY " + sortBy;
			}
			
			s = conn.prepareStatement(sql);
			s.setString(1, name_prefix+"%");
			s.setFloat(2, minGpa_f);
			
			data = s.executeQuery();
			
			List<StudentBean> result = new ArrayList<StudentBean>();
			
			while(data.next())
			{
				String name = data.getString("SURNAME") + "," + data.getString("GIVENNAME");
				String major = data.getString("MAJOR");
				double  gpa = data.getDouble("GPA");
				int courses = data.getInt("COURSES");
				StudentBean bean = new StudentBean(name, major, gpa, courses);
				result.add(bean);
			}
			return result;
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			if (data!=null && !data.isClosed()) 
				data.close();
			
			if(s!=null && !s.isClosed()) 
				s.close();
			
			if(conn !=null && !conn.isClosed()) 
				conn.close();
		}
	}
		 
}
