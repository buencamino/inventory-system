import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.sql.*;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

public class mobile
{
	Vector<Vector<Object>> data = new Vector<Vector<Object>>();                                   
	ResultSet rs = null;
	DefaultTableModel model;
	String[] headers = {"ID","Brand", "Item", "Details", "Qty", "Code", "re-order"};
		
	public mobile() throws Exception
	{
		JFrame mainWin = new JFrame("Stock Status");
		JTextField text_search = new JTextField();
		JButton btn_search = new JButton("SEARCH");
		JTable tbl_trail = new JTable();
		JLabel lbl_trail = new JLabel("History");
		JLabel lbl_ticker = new JLabel("scrolling");
		JPanel pnl_update = new JPanel();
		JButton btn_update = new JButton("Update");
		JLabel lbl_news = new JLabel("NEWS AREA");
		JButton btn_delete = new JButton("delete");
		JButton btn_archive = new JButton("archive");
		JLabel lbl_quantity = new JLabel("Quantity :");
		JTextField text_quantity = new JTextField();
		
		lbl_ticker.setBounds(10,10,600,30);
		btn_search.setBounds(520,50,200,30);
		text_search.setBounds(10,50,500,30);
		lbl_trail.setBounds(10,90,100,20);
		tbl_trail.setBounds(10,120,100,300);
		lbl_news.setBounds(220,430,300,300);
		
		text_quantity.setBounds(10,40,150,30);
		lbl_quantity.setBounds(10,10,150,30);
		btn_update.setBounds(10,80,150,30);
		pnl_update.setBounds(10,430,200,200);
		pnl_update.setLayout(null);
		pnl_update.add(text_quantity);
		pnl_update.add(lbl_quantity);
		pnl_update.add(btn_update);
			
		try
		{
			refreshVector();
		}
		catch (Exception j)
		{
			throw j;
		}
		
		JTable tbl_display = new JTable(model);
		JScrollPane scrollpane = new JScrollPane(tbl_display);
		scrollpane.setBounds(120,120,600,300);
		
		mainWin.getContentPane().setLayout(null);
		
		mainWin.getContentPane().add(lbl_news);
		mainWin.getContentPane().add(pnl_update);
		mainWin.getContentPane().add(lbl_ticker);
		mainWin.getContentPane().add(tbl_trail);
		mainWin.getContentPane().add(lbl_trail);
		mainWin.getContentPane().add(scrollpane);
		mainWin.getContentPane().add(text_search);
		mainWin.getContentPane().add(btn_search);
		mainWin.setBounds(10,10,800,700);
		mainWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWin.setVisible(true);		
	}
	
	class HandleControlButton implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			Object source = e.getSource();
			String str_search = null;
			
			if(source == btn_search)
			{
				System.out.println("clicked search");
				str_search = text_search.getText();
				System.out.println(str_search);
			}
		
		}
	}
	
	public void refreshVector() throws Exception
	{
		dbconnect conn = new dbconnect();
		model = new DefaultTableModel(null, headers);
		
		try
		{
			conn.connect();
		}
		catch (Exception p)
		{
			throw p;
		}
		
		rs = conn.getData();
		
		while(rs.next())
		{
			Vector<Object> vec = new Vector<Object>();
			vec.add(rs.getString("ID_product"));
			vec.add(rs.getString("brand"));
			vec.add(rs.getString("description"));
			vec.add(rs.getString("details"));
			vec.add(rs.getString("quantity"));
			vec.add(rs.getString("product_code"));
			vec.add(rs.getString("reorder"));
			data.addElement(vec);
			model.addRow(vec);
		}
	}
}