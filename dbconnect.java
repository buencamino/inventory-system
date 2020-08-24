import java.sql.*;

public class dbconnect
{
	Connection conn = null;
	Statement statement = null;
	ResultSet rset = null;
	
	public void connect() throws Exception
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:usb1.db");
		}
		catch (Exception z)
		{
			throw z;
		}
	}
	
	public ResultSet getSearch(String search) throws Exception
	{
		rset = null;
		
		try
		{
			connect();
			statement = conn.createStatement();
			rset = statement.executeQuery("select * from tbl_product where brand like '%" + search + "%' or description like '%" + search + "%'");
		}
		catch (Exception y)
		{
			throw y;
		}
		
		return rset;
	}
	
	public void deleteHistory(String prodnum, String quantity, String date) throws Exception
	{
		try
		{
			connect();
			statement = conn.createStatement();
			statement.executeUpdate("delete from tbl_log where ID_product = '" + prodnum + "' and quantity = '" + quantity + "' and date = '" + date + "'");		
		}
		catch (Exception h1)
		{
			throw h1;
		}
	}
	
	public ResultSet refreshHistory(String prodnum) throws Exception
	{
		rset = null;
		
		try
		{
			connect();
			statement = conn.createStatement();
			rset = statement.executeQuery("select * from tbl_log where ID_product = '" + prodnum + "'");
		}
		catch (Exception m1)
		{
			throw m1;
		}
		
		return rset;
	}
	
	public ResultSet getData() throws Exception
	{
		rset = null;
		
		try
		{
			connect();
			statement = conn.createStatement();
			rset = statement.executeQuery("select * from tbl_product");
		}
		catch (Exception m)
		{
			throw m;
		}
		
		return rset;
	}
	
	public ResultSet getLowCount() throws Exception
	{
		rset = null;
		
		try
		{
			connect();
			statement = conn.createStatement();
			rset = statement.executeQuery("select * from tbl_product where quantity < reorder");
		}
		catch (Exception w)
		{
			throw w;
		}
		
		return rset;
	}
	
	public void updateQuantity(String prodnum, String quantity, String today) throws Exception
	{
		try
		{
			connect();
			statement = conn.createStatement();
			statement.executeUpdate("update tbl_product set quantity = '" + quantity + "' where ID_product = '" + prodnum +"'");
			statement = conn.createStatement();
			statement.executeUpdate("insert into tbl_log (ID_product, quantity, date) values ('" + prodnum + "', '" + quantity + "', '" + today + "')");
		}
		catch (Exception n)
		{
			throw n;
		}
	}
	
	public void updateReorder(String prodnum, String reorder) throws Exception
	{
		try
		{
			connect();
			statement = conn.createStatement();
			statement.executeUpdate("update tbl_product set reorder = '" + reorder + "' where ID_product = '" + prodnum + "'");
		}
		catch (Exception q)
		{
			throw q;
		}
	}
	
	public void addProduct(String ID, String brand, String description, String code) throws Exception
	{
		try
		{
			connect();
			statement = conn.createStatement();
			statement.executeUpdate("insert into tbl_product (ID_product, brand, description, product_code, quantity, reorder) values ('" + ID + "', '" + brand + "', '" + description + "', '" + code + "', 0, 0)");
			
		}
		catch (Exception l1)
		{
			throw l1;
		}
	}
	
	public void updateRecord(String ID, String brand, String description, String code) throws Exception
	{
		try
		{
			connect();
			statement = conn.createStatement();
			statement.executeUpdate("update tbl_product set brand = '" + brand + "', description = '" + description + "', product_code = '" + code + "' where ID_product = '" + ID + "'");
		}
		catch (Exception e)
		{
			throw e;
		}
	}
	
	public void deleteProduct(String ID) throws Exception
	{
		try
		{
			connect();
			statement = conn.createStatement();
			statement.executeUpdate("delete from tbl_product where ID_product = '" + ID + "'");
		}
		catch (Exception m)
		{
			throw m;
		}
	}
	
	public void close() throws Exception
	{
		try
		{
			conn.close();
			statement.close();
			rset = null;
		}
		catch (Exception l)
		{
			throw l;
		}
	}
}