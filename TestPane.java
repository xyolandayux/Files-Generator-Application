package remake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class TestPane extends JPanel {

	private JTable top;
	private JTable bottom;
	static int selected;
	static List<String> remakes;
	static List<String> outputfile;
	DefaultTableModel model;
	DefaultTableModel emptyModel;
	JScrollPane t;
	JScrollPane b;
	
	public TestPane(Main container) {
		Object[][] data = {
				{"","","", false},
				{"--","--","--", false},
				{"--","--","--", false}, 
				{"--","--","--", false}, 
				{"--","--","--", false}, 
				{"--","--","--", false}, 
				{"--","--","--", false}, 
				{"--","--","--", false}, 
				{"--","--","--", false}, 
				{"--","--","--", false},
				{"--","--","--", false}, 
				{"--","--","--", false}, 
				{"--","--","--", false}, 
				{"--","--","--", false}, 
				{"--","--","--", false}, 
				{"--","--","--", false}, 
				{"--","--","--", false}};

		String header[]={"Package Number", "Field 1", "Field 2", "Select"};
		
		model = new DefaultTableModel(data, header);
		top = new JTable(model){

			private static final long serialVersionUID = 1L;

			@Override
			public Class getColumnClass(int column) {
				switch (column) {
				case 0:
					return String.class;
				case 1:
					return String.class;
				case 2:
					return Integer.class;
				case 3:
					return Boolean.class;
				default:
					return Boolean.class;
				}
			}
		};  

		top.setRowHeight(17);
		top.getTableHeader().setBackground(new Color(18,55,104));
		top.getTableHeader().setForeground(new Color(235,237,239));
		top.setBackground(new Color(242,236,253));
		top.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		top.setPreferredScrollableViewportSize(new Dimension(632, 265));
		top.setFillsViewportHeight(true);
		t  = new JScrollPane(top);
		t.setBorder(new LineBorder(new Color(127,0,255), 1));
		t.setVisible(true);
		add(t);
		
		String[] names = {"Selected Packages"};

		emptyModel = new DefaultTableModel(new String[0][0], names);
		bottom = new JTable(emptyModel);
		
		bottom.setRowHeight(17);
		bottom.getTableHeader().setBackground(new Color(18,55,104));
		bottom.getTableHeader().setForeground(new Color(235,237,239));
		bottom.setBackground(new Color(255,243,253));
		bottom.setPreferredScrollableViewportSize(new Dimension(400, 265));
		bottom.setFillsViewportHeight(true);
		b = new JScrollPane(bottom);
		b.setBorder(new LineBorder(new Color(255,0,127), 1));
		b.setVisible(true);
		add(b);

		remakes = new ArrayList<String>();
		outputfile = new ArrayList<String>();
		
		top.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					int row = top.rowAtPoint(e.getPoint());
					int col = top.columnAtPoint(e.getPoint());
					if (row > -1 && col ==3) {
						DefaultTableModel topModel = ((DefaultTableModel)top.getModel());
						DefaultTableModel bottomModel = ((DefaultTableModel)bottom.getModel());

						String packno = (String) topModel.getValueAt(row, 0);
						String field1 = (String) topModel.getValueAt(row, 1);
						String field2 = (String) topModel.getValueAt(row, 2);
						String temp = packno + "," + field1 + ","+ field2;
						
						if((boolean) topModel.getValueAt(row, 3)){
							remakes.add(packno);
							selected += 1;
							container.rese.setText("Remakes Selected : " + selected);
							bottomModel.addRow(new String[]{packno});
							
							outputfile.add(temp);
						} else {
							int index = remakes.indexOf(packno);
							if (index>-1){
								remakes.remove(packno);
								selected -= 1;
								container.rese.setText("Remakes Selected : " + selected);
								bottomModel.removeRow(index);

								outputfile.remove(temp);
							}
						}
					}
				}
			}
		});
	}

	public void setdata(Object[][] data){
		model.setRowCount(0);
		for (int i = 0; i < data.length; i++){
			model.addRow(data[i]);
		}
		model.fireTableDataChanged();
	}
}

