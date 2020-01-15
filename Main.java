package remake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.*;

public class Main {
	private String output;
	private String[][] dataset;
	String input;
	String outname;
	JLabel lico;
	TestPane mytable2;
	JLabel rese;
	
	public static void main(String[] args) {
		Main main = new Main();
		main.Design();
	}  

	public void Design(){
		JFrame f = new JFrame("Remake"); 
		f.getContentPane().setBackground(new Color(255,255,255));

		JLabel title = new JLabel("Prisma Remake Generator");
		title.setBounds(330,16,270,20);
		title.setFont(new Font("Serif", Font.BOLD, 22));

		jtablewithlistener mytable1 = new jtablewithlistener(this);
		mytable1.setBounds(20,163,650,178);

		JButton sett = new JButton(new ImageIcon("src/remake/settings.png"));
		sett.setBounds(885,25,31,31);
		sett.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e){  
				List<String[]> filenames = new ArrayList<String[]>();
				
				JPanel mypanel = new JPanel();
				mypanel.setOpaque(true);
				//mypanel.setBackground(new Color(240,251,251));
				JTextField control = new JTextField(20);
				JTextField remake = new JTextField(20);

				mypanel.add(new JLabel("Control Folder                          "));
				mypanel.add(control);
				mypanel.add(Box.createHorizontalStrut(-5));
				mypanel.add(new JLabel("Remake Generator Folder    "));
				mypanel.add(remake);

				mypanel.setPreferredSize(new Dimension(420,75));

				int option = JOptionPane.showConfirmDialog(null, mypanel, "Setting", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					input = control.getText();
					try (Stream<Path> paths = Files.walk(Paths.get(input))) {
						List<String> fil = 
								paths.map(x->x.toString())
								.filter(f->f.endsWith(".txt"))
								.map(y-> {; return y.substring(0, y.length()-4);})
								.collect(Collectors.toList());
						for(String fi : fil) {
							String[] n = fi.split("\\\\", 0);
							String[] id = n[n.length-1].split("_",0);
							filenames.add(id);
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}	
					output = remake.getText();
					dataset = filenames.toArray(new String[filenames.size()][]);
					mytable1.setdata(dataset);
					
					TestPane.selected = 0;
					rese.setText("Remakes Selected : 0");
					TestPane.remakes.clear();
					TestPane.outputfile.clear();
					
					mytable2.model.setRowCount(0);
					mytable2.model.fireTableDataChanged();
					
					mytable2.emptyModel.setRowCount(0);
					mytable2.emptyModel.fireTableDataChanged();

					f.revalidate();
					f.repaint();
				}
			}  
		});

		JLabel filename = new JLabel("File Name");
		filename.setBounds(20,53,80,30);

		JTextField fina = new JTextField("");
		fina.setBounds(90,60,380,21);

		JLabel wf = new JLabel("WF Jobid");
		wf.setBounds(20,84,80,30);

		JTextField wfjo = new JTextField("");
		wfjo.setBounds(90,91,380,21);

		JLabel print = new JLabel("Print Date");
		print.setBounds(20,115,80,30);

		JTextField prda = new JTextField("");
		prda.setBounds(90,123,380,21);

		JButton search=new JButton("Search");
		search.setBounds(530,122,80, 21);
		search.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e){  
				String ffiles = fina.getText();
				String fwf = wfjo.getText();
				String fdate = prda.getText();
				String[] row = {ffiles, fwf, fdate};
				List<String[]> savedsearch = new ArrayList<String[]>();
				for(int i = 0; i < dataset.length; i++){
					boolean match = true;
					for (int j = 0; j < dataset[i].length; j++){
						if (!row[j].isEmpty() && !row[j].equals(dataset[i][j])){
							match = false;
							break;
						}
					}
					if (match){
						savedsearch.add(dataset[i]);
					}
				}
				
				mytable1.model.setRowCount(0);
				for (int i = 0; i < savedsearch.size(); i++){
					mytable1.model.addRow(savedsearch.get(i));
				}
				mytable1.model.fireTableDataChanged();
				
				TestPane.selected = 0;
				rese.setText("Remakes Selected : 0");
				TestPane.remakes.clear();
				TestPane.outputfile.clear();
				
				mytable2.model.setRowCount(0);
				mytable2.model.fireTableDataChanged();
				
				mytable2.emptyModel.setRowCount(0);
				mytable2.emptyModel.fireTableDataChanged();

				f.revalidate();
				f.repaint();	
			}  
		});

		lico = new JLabel("Total Package: 0");
		lico.setBounds(24,352,180,21);


		mytable2 = new TestPane(this);
		mytable2.t.setBounds(20,377,650, 294);
		mytable2.b.setBounds(695,162,210,506);

		rese = new JLabel("Remakes Selected: 0");
		rese.setBounds(700,135,180,21);

		JButton gere=new JButton("Generate Remake");
		gere.setBounds(680,686,138,21);
		gere.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent e){  
				try { 
					FileWriter newfile = new FileWriter(output + "\\" + outname + ".txt");    
					
					List<String> outfile = TestPane.outputfile;
					 
					newfile.write(String.join("\n", outfile));   
					newfile.close();
				} catch (Exception e1){
					System.out.println(e1);
				}
			}  
		});

		JButton cancel=new JButton("Cancel");
		cancel.setBounds(827,686,80,21);
		cancel.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}});

		f.add(title);
		f.add(sett);
		f.add(filename);
		f.add(fina);
		f.add(wf);
		f.add(prda);
		f.add(wfjo);
		f.add(print);
		f.add(search);
		f.add(mytable1);
		f.add(lico);
		f.add(mytable2.t);
		f.add(mytable2.b);
		f.add(rese);
		f.add(gere);  
		f.add(cancel);

		f.setSize(950,765);
		f.setLayout(null);
		f.setResizable(false);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}


