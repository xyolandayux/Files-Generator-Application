package remake;

import java.awt.Color;
import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class jtablewithlistener extends JPanel{

	DefaultTableModel model;
	JTable table;

	public static <T> T concatenate(T a, T b) {
		if (!a.getClass().isArray() || !b.getClass().isArray()) {
			throw new IllegalArgumentException();
		}

		Class<?> resCompType;
		Class<?> aCompType = a.getClass().getComponentType();
		Class<?> bCompType = b.getClass().getComponentType();

		if (aCompType.isAssignableFrom(bCompType)) {
			resCompType = aCompType;
		} else if (bCompType.isAssignableFrom(aCompType)) {
			resCompType = bCompType;
		} else {
			throw new IllegalArgumentException();
		}

		int aLen = Array.getLength(a);
		int bLen = Array.getLength(b);

		@SuppressWarnings("unchecked")
		T result = (T) Array.newInstance(resCompType, aLen + bLen);
		System.arraycopy(a, 0, result, 0, aLen);
		System.arraycopy(b, 0, result, aLen, bLen);        

		return result;
	}
	
	static Object[][] insertData(InputStream is) {
		String thisLine;
		DataInputStream myInput = new DataInputStream(is);
		ArrayList<Object[]> lines = new ArrayList<Object[]>();
		try {
			while ((thisLine = myInput.readLine()) != null) {
				Object[] one = thisLine.split(",");
				Object[] two = {false};
				Object[] both = concatenate(one, two);
				lines.add(both);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		Object[][] array = new Object[lines.size()][0];
		lines.toArray(array);
		return array;
	}

	public jtablewithlistener(Main container){
		
		String data[][]={ 
				{"--","--","--"}, 
				{"--","--","--"},
				{"--","--","--"}, 
				{"--","--","--"}, 
				{"--","--","--"}, 
				{"--","--","--"}, 
				{"--","--","--"}, 
				{"--","--","--"}, 
				{"--","--","--"}, 
				{"--","--","--"}, 
				{"--","--","--"}};

		String header[]= {"File Name", "WF JobID", "Print Date"};
		
		model = new DefaultTableModel(data,header);
		table = new JTable(model);
		
		table.setRowHeight(17);
		table.getTableHeader().setBackground(new Color(18,55,104));
		table.getTableHeader().setForeground(new Color(235,237,239));
		table.setBackground(new Color(237,254,254));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setPreferredScrollableViewportSize(new Dimension(632, 150));
		table.setFillsViewportHeight(true);

		JScrollPane js = new JScrollPane(table);
		js.setBorder(new LineBorder(Color.cyan, 1));
		js.setVisible(true);
		add(js);

		table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			Boolean p = true;
			Boolean err1 = true;
			Boolean err2 = true;
			public void valueChanged(ListSelectionEvent event) {
				if (table.getSelectedRow() != -1){
					String combined = "";
					try {
					String f = table.getValueAt(table.getSelectedRow(), 0).toString();
					String wf = table.getValueAt(table.getSelectedRow(), 1).toString();
					String d = table.getValueAt(table.getSelectedRow(), 2).toString();
					combined = f + "_" + wf + "_" + d;
					} catch (Exception e){
						if (err1){
							System.out.println("Invalid format of CSV file name");
							err1 = false;
							return;
						}
						else {
							err1 = true;
							return;
						}
					}
					
					String chosen = combined + ".txt";
					String filename = container.input + "\\" + chosen;
					container.outname = combined +"_remake";
	
					File f1 = new File(filename);
	
					try {
						int linenumber = 0;
						FileReader fr = new FileReader(f1);
						LineNumberReader lnr = new LineNumberReader(fr);
	
						while (lnr.readLine() != null) {
							linenumber++;
						}
						lnr.close();
						container.lico.setText("Total Package: " + linenumber);
	
						InputStream is;
						is = new FileInputStream(f1);
						Object[][] data = insertData(is);
						container.mytable2.setdata(data);
	
					} catch (IOException e) {
						if (err2){
							System.out.println("File " + chosen + " can't be read.");
							err2 = false;
						}
						else {
							err2 = true;
						}
					}
					TestPane.selected = 0;
					container.rese.setText("Remakes Selected : 0");
					TestPane.remakes.clear();
					TestPane.outputfile.clear();
					
					container.mytable2.emptyModel.setRowCount(0);
					container.mytable2.emptyModel.fireTableDataChanged();
				}
			}
		});
	}

	public void setdata(String[][] data){
		model.setRowCount(0);
		for (int i = 0; i < data.length; i++){
			model.addRow(data[i]);
		}
		model.fireTableDataChanged();
	}
}
