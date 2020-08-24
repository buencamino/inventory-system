import java.util.Date;
import java.text.*;
import javax.swing.*;
import java.awt.*;
import java.util.Vector;
import java.sql.*;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.*;
import java.awt.event.*;
import javax.swing.text.*;
import javax.swing.table.TableColumn;

public class mobile
{
	Vector<Vector<Object>> data, data_trail;
	Vector<Object> vec, vec1;
	Vector<String> headers = new Vector<String>();
	Vector<String> headers_trail = new Vector<String>();
	ResultSet rs = null;
	DefaultTableModel model, model1;
	JTextField text_search = new JTextField();			
	JTextField text_quantity = new JTextField();
	JTextField text_reorder = new JTextField();
	JButton btn_search, btn_update, btn_refresh, btn_reorder, btn_delete, btn_pAdd, btn_pDelete, btn_pEdit, btn_reports;	
	ListSelectionModel listselectionmodel, listselectionmodel_history;	
	JTable tbl_display, tbl_trail;
	JLabel lbl_current, lbl_date, lbl_ticker;
	JEditorPane editNews;
	Document doc;
	JTextField text_pBrand, text_pDescription, text_pDetails, text_pCode;
		
	String buff_quantity, buff_ID, buff_trailquantity, buff_traildate, buff_lastID, buff_selectedID, buff_brand, buff_description, buff_code;
	
	public mobile() throws Exception
	{
		HandleControlButton control = new HandleControlButton();
		selectionHandler handler = new selectionHandler();
		selectionHandler_history handler_history = new selectionHandler_history();
		
		model = new DefaultTableModel();
		model1 = new DefaultTableModel(null, headers_trail);
		
		headers.add("ID");
		headers.add("Brand");
		headers.add("Description");
		headers.add("Qty");
		headers.add("Product #");
		headers.add("re-order");
		
		headers_trail.add("Qty");
		headers_trail.add("Date");
		
		btn_delete = new JButton("Delete");
		btn_delete.addActionListener(control);
		btn_reorder = new JButton("Change Re-order");
		btn_reorder.addActionListener(control);
		btn_refresh = new JButton("Refresh");
		btn_refresh.addActionListener(control);
		btn_update = new JButton("update");
		btn_update.addActionListener(control);
		btn_search = new JButton("SEARCH");
		btn_search.addActionListener(control);	
		JFrame mainWin = new JFrame("Stock Status");
		tbl_trail = new JTable(model1);
		JLabel lbl_trail = new JLabel("History");
		lbl_ticker = new JLabel("scrolling");
		lbl_ticker.setForeground(Color.RED);
		JPanel pnl_update = new JPanel();
		JPanel pnl_pmanagement = new JPanel();
		editNews = new JEditorPane();
		editNews.setEditable(false);
		JButton btn_archive = new JButton("archive");
		JLabel lbl_quantity = new JLabel("current quantity :");
		lbl_current = new JLabel("-");
		lbl_current.setFont(new Font("Verdana", 12, 35));
		lbl_current.setForeground(Color.BLUE);
		text_search.addActionListener(control);
		text_quantity.addActionListener(control);
		
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		lbl_date = new JLabel(sdf.format(today));
		
		lbl_ticker.setBounds(10,10,500,30);
		btn_search.setBounds(520,50,200,30);
		text_search.setBounds(10,50,500,30);
		lbl_trail.setBounds(10,90,100,20);
		btn_delete.setBounds(60,90,100,20);
		btn_refresh.setBounds(650,90,100,20);
		btn_reorder.setBounds(470,90,150,20);
		text_reorder.setBounds(360,90,100,20);
		
		lbl_quantity.setBounds(10,10,150,30);
		lbl_current.setBounds(10,40,150,30);
		text_quantity.setBounds(10,80,70,30);
		btn_update.setBounds(85,80,100,30);
		pnl_update.setBounds(10,420,200,200);
		pnl_update.setLayout(null);
		pnl_update.add(lbl_current);
		pnl_update.add(text_quantity);
		pnl_update.add(lbl_quantity);
		pnl_update.add(btn_update);
		lbl_date.setBounds(650,10,150,30);
		
		//product modification panel
		pnl_pmanagement.setBounds(790,50,200,550);
		pnl_pmanagement.setLayout(null);
		
		btn_pAdd = new JButton("add product");
		btn_pAdd.addActionListener(control);
		btn_pDelete = new JButton("delete product");
		btn_pDelete.addActionListener(control);
		btn_pEdit = new JButton("modify product");
		btn_pEdit.addActionListener(control);
		btn_reports = new JButton("under const.");
		
		text_pDetails = new JTextField();
		text_pBrand = new JTextField();
		text_pDescription = new JTextField();
		text_pDetails = new JTextField();
		text_pCode = new JTextField();
		JLabel lbl_pBrand = new JLabel("Brand");
		lbl_pBrand.setBounds(5,40,100,20);
		text_pBrand.setBounds(5,65,130,20);
		JLabel lbl_pDescription = new JLabel("Description");
		lbl_pDescription.setBounds(5,90,100,20);
		text_pDescription.setBounds(5,115,130,20);
		JLabel lbl_pDetails = new JLabel("Details");
		lbl_pDetails.setBounds(5,140,100,20);
		text_pDetails.setBounds(5,165,130,20);
		JLabel lbl_pCode = new JLabel("Product Code");
		lbl_pCode.setBounds(5,190,100,20);
		text_pCode.setBounds(5,215,130,20);
		
		btn_pAdd.setBounds(5,240,120,20);
		btn_pDelete.setBounds(5,265,120,20);
		btn_pEdit.setBounds(5,290,120,20);
		btn_reports.setBounds(5,350,120,30);
		
		pnl_pmanagement.add(btn_reports);
		pnl_pmanagement.add(btn_pEdit);
		pnl_pmanagement.add(btn_pDelete);
		pnl_pmanagement.add(btn_pAdd);
		pnl_pmanagement.add(text_pCode);
		pnl_pmanagement.add(text_pDetails);
		pnl_pmanagement.add(text_pDescription);
		pnl_pmanagement.add(text_pBrand);
		pnl_pmanagement.add(lbl_pBrand);
		pnl_pmanagement.add(lbl_pDescription);
		pnl_pmanagement.add(lbl_pDetails);
		pnl_pmanagement.add(lbl_pCode);
		
		dbconnect conn = new dbconnect();
		
		try
		{
			conn.connect();
			rs = conn.getData();
			refreshVector(rs);
		}
		catch (Exception j)
		{
			throw j;
		}
		
		//main big table
		tbl_display = new JTable(model);
		TableColumn a = tbl_display.getColumnModel().getColumn(2);
		a.setPreferredWidth(200);
		
		JScrollPane scrollpane = new JScrollPane(tbl_display);
		JScrollPane scrollpane1 = new JScrollPane(tbl_trail);
		scrollpane.setBounds(170,120,600,300);
		scrollpane1.setBounds(10,120,150,300);
		
		startReorder();
		JScrollPane scrollpane2 = new JScrollPane(editNews);
		editNews.setText("- Information -\n\n");
		scrollpane2.setBounds(220,430,540,100);
		
		listselectionmodel = tbl_display.getSelectionModel();
		listselectionmodel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listselectionmodel.addListSelectionListener(handler);
		tbl_display.setSelectionModel(listselectionmodel);
		
		listselectionmodel_history = tbl_trail.getSelectionModel();
		listselectionmodel_history.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listselectionmodel_history.addListSelectionListener(handler_history);
		tbl_trail.setSelectionModel(listselectionmodel_history);
		
		//JFrame settings mainWin
		mainWin.getContentPane().setLayout(null);
		
		mainWin.getContentPane().add(pnl_pmanagement);
		mainWin.getContentPane().add(btn_delete);
		mainWin.getContentPane().add(scrollpane2);
		mainWin.getContentPane().add(text_reorder);
		mainWin.getContentPane().add(btn_reorder);
		mainWin.getContentPane().add(lbl_date);
		mainWin.getContentPane().add(pnl_update);
		mainWin.getContentPane().add(lbl_ticker);
		mainWin.getContentPane().add(scrollpane1);
		mainWin.getContentPane().add(lbl_trail);
		mainWin.getContentPane().add(scrollpane);
		mainWin.getContentPane().add(text_search);
		mainWin.getContentPane().add(btn_search);
		mainWin.getContentPane().add(btn_refresh);
		mainWin.setBounds(10,10,950,600);
		mainWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWin.setVisible(true);		
	}
	
	class HandleControlButton implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			Object source = e.getSource();
			String str_search = null;
			ResultSet rsetSearch = null;
			ResultSet rsetRefresh = null;
			
			if(source == btn_search || source == text_search)
			{
				dbconnect condb = new dbconnect();
				
				try
				{
					str_search = text_search.getText();
					condb.connect();
					rsetSearch = condb.getSearch(str_search);
					refreshVector(rsetSearch);
					condb.close();
				}
				catch (Exception k)
				{
					System.out.println(k.getMessage());
				}
				
				TableColumn a = tbl_display.getColumnModel().getColumn(2);
				a.setPreferredWidth(200);
			}
		
			if(source == btn_delete)
			{
				if (buff_ID.length() != 0)
				{
					dbconnect condb = new dbconnect();
					try
					{
						condb.deleteHistory(buff_ID, buff_trailquantity, buff_traildate);
						condb.close();
					}
					catch (Exception s)
					{
						System.out.println(s.getMessage());
					}
				}
				
				String buff_addtext = null;
				
				doc = editNews.getDocument();
		
				buff_addtext = "deleted history record for product " + buff_ID + " with quantity " + buff_trailquantity + " dated " + buff_traildate + "\n";
				try
				{
					doc.insertString(doc.getLength(), buff_addtext, null);
				}
				catch (Exception a1)
				{
					System.out.println(a1.getMessage());
				}
			}
		
			if(source == btn_refresh)
			{
				dbconnect condb = new dbconnect();
				
				try
				{
					rsetRefresh = condb.getData();
					refreshVector(rsetRefresh);
					condb.close();
					startReorder();
				}
				catch (Exception h)
				{
					System.out.println(h.getMessage());
				}
				
				TableColumn a = tbl_display.getColumnModel().getColumn(2);
				a.setPreferredWidth(200);
			}
			
			if(source == btn_reorder)
			{
				dbconnect condb = new dbconnect();
				
				if (buff_ID != null && text_reorder.getText().length() != 0)
				{		
					try
					{
						condb.updateReorder(buff_ID, text_reorder.getText());
						condb.close();
					}
					catch (Exception u)
					{
						System.out.println(u.getMessage());
					}
				}
			}
			
			if(source == btn_update || source == text_quantity)
			{
				String quantity1 = null, date = null;
				dbconnect condb = new dbconnect();	
				
				if (text_quantity.getText().length() != 0)
				{
					quantity1 = text_quantity.getText();
					date = lbl_date.getText();
					
					try
					{	
						condb.connect();
						condb.updateQuantity(buff_ID, quantity1, date);
						condb.close();
					}
					catch (Exception z)
					{
						System.out.println("1");
						System.out.println(z.getMessage());
					}
				}
				
				text_quantity.setText("");
			}
			
			if(source == btn_pAdd)
			{
				String brand, description, code;
				brand = null;
				description = null;
				code = null;
				
				dbconnect conn = new dbconnect();
				
				if (text_pBrand.getText().length() != 0 && text_pDescription.getText().length() != 0 && text_pCode.getText().length() != 0)
				{
					brand = text_pBrand.getText();
					description = text_pDescription.getText();
					code = text_pCode.getText();
					
					try
					{
						conn.addProduct(buff_lastID, brand, description, code);
						conn.close();
					}
					catch (Exception ma)
					{
						System.out.println("error add");
					}
					System.out.println("product add");
				}
				
				String buff_addtext;
				
				doc = editNews.getDocument();
		
				buff_addtext = "added " + brand + " " + description + " to product database.\n";
				try
				{
					doc.insertString(doc.getLength(), buff_addtext, null);
				}
				catch (Exception a1)
				{
					System.out.println(a1.getMessage());
				}
			}
			
			if(source == btn_pDelete)
			{
				dbconnect conn = new dbconnect();
				
				if(buff_selectedID != null)
				{
					try
					{
						conn.deleteProduct(buff_selectedID);
						conn.close();
					}
					catch (Exception a)
					{
						System.out.println("error delete product");
					}
				}
				
				String buff_addtext;
				
				doc = editNews.getDocument();
		
				buff_addtext = "you have deleted " + text_pBrand.getText() + " " + text_pDescription.getText() + ".\n";
				try
				{
					doc.insertString(doc.getLength(), buff_addtext, null);
				}
				catch (Exception a1)
				{
					System.out.println(a1.getMessage());
				}
			}
			
			if(source == btn_pEdit)
			{
				dbconnect conn = new dbconnect();
				
				if(buff_selectedID != null)
				{
					try
					{
						conn.updateRecord(buff_selectedID, text_pBrand.getText(), text_pDescription.getText(), text_pCode.getText());
						conn.close();
					}
					catch (Exception e1)
					{
						System.out.println(e1.getMessage());
					}
				}
				
				String buff_addtext;
				
				doc = editNews.getDocument();
		
				buff_addtext = "modified record " + buff_brand + " " + buff_description + " to " + text_pBrand.getText() + " " + text_pDescription.getText() + ".\n";
				
				try
				{
					doc.insertString(doc.getLength(), buff_addtext, null);
				}
				catch (Exception a1)
				{
					System.out.println(a1.getMessage());
				}
			}
		}
	}
	
	class selectionHandler_history implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e)
		{
			ListSelectionModel lsm = (ListSelectionModel) e.getSource();
			
			if(lsm.isSelectionEmpty())
			{
				System.out.println("nothing selected on history");
			}
			else
			{
				int minIndex = lsm.getMinSelectionIndex();
				int maxIndex = lsm.getMaxSelectionIndex();
				
				Object x, y;
				
				x = tbl_trail.getValueAt(maxIndex, 0);
				buff_trailquantity = x.toString();
				
				y = tbl_trail.getValueAt(maxIndex, 1);
				buff_traildate = y.toString();
			}
		}
	}
	
    class selectionHandler implements ListSelectionListener
    {
        public void valueChanged(ListSelectionEvent e)
        {
            ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                     
            if(lsm.isSelectionEmpty())
            {
                System.out.println("nothing selected (message for error checking only)");
            }
            else
            {
                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();

				Object x, y, a, b, c, i;
				
				if(tbl_display.getValueAt(maxIndex, 0) != null)
				{
					i = tbl_display.getValueAt(maxIndex, 0);
					buff_selectedID = i.toString();
				}
				
				if(tbl_display.getValueAt(maxIndex, 1) != null)
				{
					a = tbl_display.getValueAt(maxIndex, 1);
					buff_brand = a.toString();
					text_pBrand.setText(buff_brand);
				}

				if(tbl_display.getValueAt(maxIndex, 2) != null)
				{
					b = tbl_display.getValueAt(maxIndex, 2);
					buff_description = b.toString();
					text_pDescription.setText(buff_description);
				}
				
				if(tbl_display.getValueAt(maxIndex, 4) != null)
				{
					c = tbl_display.getValueAt(maxIndex, 4);
					buff_code = c.toString();
					text_pCode.setText(buff_code);
				}
				
				if(tbl_display.getValueAt(maxIndex, 3) != null)
				{						
					x = tbl_display.getValueAt(maxIndex, 3);
					buff_quantity = x.toString();
					lbl_current.setText(buff_quantity);
                }
				
				if(tbl_display.getValueAt(maxIndex, 0) != null)
				{
					y = tbl_display.getValueAt(maxIndex, 0); 
					buff_ID = y.toString();
				}
				
				if (buff_ID.length() != 0)
				{
					dbconnect condb = new dbconnect();
					ResultSet rs1 = null;
					try
					{
						condb.connect();
						rs1 = condb.refreshHistory(buff_ID);
						
						data_trail = new Vector<Vector<Object>>();
						
						while(rs1.next())
						{
							vec1 = new Vector<Object>();
							vec1.add(rs1.getString("quantity"));
							vec1.add(rs1.getString("date"));
							data_trail.addElement(vec1);
						}
		
						model1.setDataVector(data_trail, headers_trail);
						model1.fireTableDataChanged();
						condb.close();
					}
					catch (Exception s)
					{
						System.out.println(s.getMessage());
					}
				}
            }
        }
    }
	
	public void startReorder() throws Exception
	{
		dbconnect condb = new dbconnect();
		int rcount = 0;
		String buff_addtext = null;
		
		ResultSet rsetReorder = condb.getLowCount();
		
		doc = editNews.getDocument();
		
		while(rsetReorder.next())
		{
			rcount++;
			buff_addtext = rsetReorder.getString("brand") + " " + rsetReorder.getString("description") + " is low on quantity.\n";
			doc.insertString(doc.getLength(), buff_addtext, null);
		}
		
		if (rcount == 0)
		{
			lbl_ticker.setText("currently no stocks have low quantity");
			lbl_ticker.setForeground(Color.BLACK);
		}
		else if (rcount > 0)
		{
			lbl_ticker.setText("* currently " + rcount + " item/s have low quantity");	
			lbl_ticker.setForeground(Color.RED);
		}
	}
	
	public void refreshVector(ResultSet rset1) throws Exception
	{
		data = new Vector<Vector<Object>>();
		buff_lastID = null;
		
		while(rset1.next())
		{
			vec = new Vector<Object>();
			vec.add(rset1.getString("ID_product"));
			vec.add(rset1.getString("brand"));
			vec.add(rset1.getString("description"));
			vec.add(rset1.getString("quantity"));
			vec.add(rset1.getString("product_code"));
			vec.add(rset1.getString("reorder"));
			if(rset1.getString("ID_product") != null && !(rset1.getString("ID_product").equals("null")))
			{		
				buff_lastID = rset1.getString("ID_product");
				int i = Integer.parseInt(buff_lastID);
				i = i + 1;
				
				buff_lastID = String.valueOf(i);
			}
			data.addElement(vec);
		}
		
		model.setDataVector(data, headers);
		model.fireTableDataChanged();
	}
}