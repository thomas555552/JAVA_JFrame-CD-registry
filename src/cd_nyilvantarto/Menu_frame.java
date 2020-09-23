package cd_nyilvantarto;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

public class Menu_frame extends JFrame {

private CD_data data=new CD_data();

String combob[]={"Játék","Film","Zene","Program","Sorozat","Egyéb"};// a típusok a comboboxhoz

// A menu nyomógombjai,azért hogy elérjem mindenhonnan
JButton listazas=new JButton("CD-k listázása");
JButton hozzaad=new JButton("CD hozzáadás");
JButton frissites=new JButton("CD frissítés");
JButton torles=new JButton("CD törlés");
JButton vasarlas=new JButton("CD vásárlás");
JButton rendeles=new JButton("CD rendelés");

JComboBox type=new JComboBox(combob);
JTable table = new JTable(data);

// menu ablakban lévõ komponensek
private void Menu_components()
{
	JButton mentettrendeles=new JButton("CD rendelések megtekintése");
	JButton eladasok=new JButton("CD eladások megtekintése");

	this.setLayout(new BorderLayout());
	JPanel panel=new JPanel();
	this.add(panel);
	panel.setLayout(new GridBagLayout());
	panel.setBackground(new Color(255,141,66));
	//Menu nyomogombok letrehozasa es hozzaadasa a panelhez
	panel.add(listazas);
	//pár nyomógomb addig nem használható még nincsennek kilistázva a CD-k
	panel.add(hozzaad).setEnabled(false);
	panel.add(frissites).setEnabled(false);
	panel.add(torles).setEnabled(false);
	panel.add(vasarlas).setEnabled(false);
	panel.add(rendeles).setEnabled(false);
	panel.add(eladasok);
	panel.add(mentettrendeles);
	//az egyes nyomógombok gombnyomás figyelõi
	listazas.addActionListener(new listazasActionListener());
	hozzaad.addActionListener(new hozzaadActionListener());
	frissites.addActionListener(new frissitesActionListener());
	torles.addActionListener(new torlesActionListener());
	vasarlas.addActionListener(new vasarlasActionListener());
	rendeles.addActionListener(new rendelesActionListener());
	eladasok.addActionListener(new eladasokActionListener());
	mentettrendeles.addActionListener(new mentettrendelesActionListener());
	panel.setVisible(true);
}

JPanel downpanel = new JPanel();

//a CD rendelések megtekintése nyomógomb
public class mentettrendelesActionListener implements ActionListener
{
	JComboBox ordercombo=new JComboBox();
	JPanel rendelespanel=new JPanel();
	JFrame rendelesWindow=new JFrame("CD rendelések");
	
@Override
public void actionPerformed(ActionEvent arg0) {
// TODO Auto-generated method stub
	rendelespanel.removeAll();
	ordercombo.removeAllItems();
	File sold = new File(System.getProperty("user.dir"));
	File f=new File(sold,"rendeles"); // rendelés mappán belül a fájlok kilistázása
	File[] list = f.listFiles();
	for (int i = 0; i < list.length; i++)
	{
		ordercombo.addItem(list[i].getName()); // comboboxhoz hozzáadása a fájlok nevei
	}
	
	JButton show=new JButton("Mutat:"); //a CD rendelés megtekintésén belül egy nyomógomb+figyelése
	show.addActionListener(new showActionListener());
	
	//Cd rendelés megtekintése ablak beállításai+elemek hozzáadása
	rendelespanel.add(ordercombo);
	rendelespanel.add(show);
	rendelesWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	double width = screenSize.getWidth();
	width=(width-800)/2;
	rendelesWindow.setLocation((int)width, 135);
	rendelesWindow.setMinimumSize(new Dimension(800, 500));
	rendelespanel.setBackground(new Color(128,191,191));
	rendelesWindow.add(rendelespanel,BorderLayout.CENTER);
	rendelespanel.repaint();
	rendelespanel.revalidate();
	rendelespanel.setVisible(true);
	rendelesWindow.setVisible(true);
}

JTextArea ta=new JTextArea(20,30);
JScrollPane scrollta = new JScrollPane(ta);

//Az Mutat nyomógomb!
public class showActionListener implements ActionListener
{
@Override
	public void actionPerformed(ActionEvent arg0) {
	// TODO Auto-generated method stub
	ta.setText("");
	ta.setBackground(new Color(155,255,155));
	rendelespanel.add(scrollta);
	ta.setEditable(false);
	File load = new File(System.getProperty("user.dir"));
	File f=new File(load,"rendeles");
	File loadFrom=new File(f,(String)ordercombo.getSelectedItem());
	try{
		if(!loadFrom.isFile()) throw new IOException("Nem létezik a fájl!");
		FileReader fr=new FileReader(loadFrom);
		BufferedReader bf=new BufferedReader(fr);
		String newline= "\n";
		String text=null;
		for(int i=0;i<bf.read();i++){
			text=bf.readLine();
			ta.append(text.replace(';', ' ')+newline); }
	}catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	rendelesWindow.revalidate();
	}

}
}

//a CD eladások megtekintése nyomógomb
//minden ugyanaz mint a rendelésnél,csak az eladások mappából listázza a fájlokat
public class eladasokActionListener implements ActionListener
{
	JComboBox soldcombo=new JComboBox();
	JPanel eladaspanel=new JPanel();
	JFrame eladasWindow=new JFrame("CD eladások");
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		eladaspanel.removeAll();
		soldcombo.removeAllItems();
		File sold = new File(System.getProperty("user.dir"));
		File f=new File(sold,"eladas");
		File[] list = f.listFiles();
		for (int i = 0; i < list.length; i++)
		{
			soldcombo.addItem(list[i].getName());
		}
		
		JButton show=new JButton("Mutat:");
		show.addActionListener(new showActionListener());
		eladaspanel.add(soldcombo);
		eladaspanel.add(show);
		eladasWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		width=(width-800)/2;
		
		eladasWindow.setLocation((int)width, 135);
		eladasWindow.setMinimumSize(new Dimension(800, 500));
		eladaspanel.setBackground(new Color(128,191,191));
		eladasWindow.add(eladaspanel,BorderLayout.CENTER);
		eladaspanel.repaint();
		eladaspanel.revalidate();
		eladaspanel.setVisible(true);
		eladasWindow.setVisible(true);
	}
	
	JTextArea ta=new JTextArea(20,30);
	JScrollPane scrollta = new JScrollPane(ta);
	
	public class showActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub

			ta.setText("");
			ta.setBackground(new Color(155,255,155));
			eladaspanel.add(scrollta);
			ta.setEditable(false);
			File load = new File(System.getProperty("user.dir"));
			File f=new File(load,"eladas");
			File loadFrom=new File(f,(String)soldcombo.getSelectedItem());
			try{
				if(!loadFrom.isFile()) throw new IOException("Nem létezik a fájl!");
				FileReader fr=new FileReader(loadFrom);
				BufferedReader bf=new BufferedReader(fr);
				String newline= "\n";
				String text=null;
				for(int i=0;i<bf.read();i++)
				{
					text=bf.readLine(); 
					ta.append(text.replace(';', ' ')+newline); 
					}
				
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			eladasWindow.revalidate();
		}
	}
}

//CD rendelés nyomgómb
public class rendelesActionListener implements ActionListener
{
	//A CD-k kilistázása után,mi jelenik meg az alsó panelben
	//a panelhez hozzáadott elemek kezelése
	JLabel rendeles0=new JLabel("Beírhatók a rendelni kívánt CD-k!");
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		//melyik gomb aktiv/passziv
		hozzaad.setEnabled(true);
		torles.setEnabled(true);
		vasarlas.setEnabled(true);
		rendeles.setEnabled(false);
		vasarlas.setEnabled(true);
		//gombok engedelyezes vege
		
		JButton ordergomb=new JButton("Rendelés elmentése! (.dat)");
		JButton ordergombcsv= new JButton("Eladás elmentése!(.csv)");
		downpanel.removeAll();
		boolean a[]={false,false,false,false,false,false,true};
		data.setColisEditable(a);
		downpanel.add(rendeles0);
		downpanel.add(ordergomb);
		downpanel.add(ordergombcsv);
		ordergomb.addActionListener(new rendelesplusActionListener());
		ordergombcsv.addActionListener(new rendelespluscsvActionListener());
		downpanel.repaint();
		downpanel.revalidate();
}
	
	public class rendelesplusActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			data.saveOrderedCDs();
			data.CD_data_write();
		}
	}
	
	public class rendelespluscsvActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			data.saveOrderedCDscsv();
			data.CD_data_write();
		}
	}
}

//CD vásárlás nyomógomb
public class vasarlasActionListener implements ActionListener
{
	//A CD-k kilistázása után,mi jelenik meg az alsó panelben
	//a panelhez hozzáadott elemek kezelése
	JLabel vasarlas0=new JLabel("Kiválaszthatók az eladni kívánt CD-k!");
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//melyik gomb aktiv/passziv
		hozzaad.setEnabled(true);
		torles.setEnabled(true);
		vasarlas.setEnabled(true);
		rendeles.setEnabled(true);
		vasarlas.setEnabled(false);
		//gombok engedelyezes vege
		
		JButton sellgomb=new JButton("Eladás elmentése!(.dat)");
		JButton sellgombcsv= new JButton("Eladás elmentése!(.csv)");
		downpanel.removeAll();
		boolean a[]={false,false,false,false,false,true,false};
		data.setColisEditable(a);
		downpanel.add(vasarlas0);
		downpanel.add(sellgomb);
		downpanel.add(sellgombcsv);
		sellgomb.addActionListener(new sellplusActionListener());
		sellgombcsv.addActionListener(new sellpluscsvActionListener());
		downpanel.repaint();
		downpanel.revalidate();
	}
	
	public class sellplusActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			data.saveSoldCDs();
			data.CD_data_write();
		}
	}
	
	public class sellpluscsvActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			data.saveSoldCDsCSV();
			data.CD_data_write();
		}
	}
}

//CD frissítés nyomógomb
public class frissitesActionListener implements ActionListener
{
	//A CD-k kilistázása után,mi jelenik meg az alsó panelben
	//a panelhez hozzáadott elemek kezelése-itt csak annyi történik,hogy
	// a kód oszlopon kívül mindegyik szerkeszthetõ lesz
	JLabel frissites0=new JLabel("A táblázat szabadon szerkeszthetõ! (Automatikus mentés!!!) ");
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//melyik gomb aktiv/passziv
		hozzaad.setEnabled(true);
		torles.setEnabled(true);
		vasarlas.setEnabled(true);
		rendeles.setEnabled(true);
		vasarlas.setEnabled(true);
		//gombok engedelyezes vege
		
		downpanel.removeAll();
		boolean a[]={false,true,true,true,true,false,false};
		data.setColisEditable(a);
		downpanel.add(frissites0);
		downpanel.repaint();
		downpanel.revalidate();
	}
}

//CD törlés nyomógomb
public class torlesActionListener implements ActionListener
{
	//A CD-k kilistázása után,mi jelenik meg az alsó panelben
	//a panelhez hozzáadott elemek kezelése
	JLabel torles0=new JLabel("#");
	JLabel torles1=new JLabel("/#");
	JTextField textf0=new JTextField(6);
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		//melyik gomb aktiv/passziv
		hozzaad.setEnabled(true);
		torles.setEnabled(false);
		vasarlas.setEnabled(true);
		rendeles.setEnabled(true);
		vasarlas.setEnabled(true);
		//gombok engedelyezes vege

		JButton deletegomb=new JButton("Törlés!");
		data.setColEditablefalse();
		deletegomb.addActionListener(new torlesplusActionListener());
		downpanel.removeAll();
		downpanel.add(torles0);
		downpanel.add(textf0);
		downpanel.add(torles1);
		downpanel.add(deletegomb);
		downpanel.repaint();
		downpanel.revalidate();
	}
	
	public class torlesplusActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String code=textf0.getText();
			data.deleteCD(code);
		}
	}
}

// CD hozzáadása nyomógomb
public class hozzaadActionListener implements ActionListener
{
	//A CD-k kilistázása után,mi jelenik meg az alsó panelben
	//a panelhez hozzáadott elemek kezelése
	JLabel hozzaad0=new JLabel("Név:");
	JLabel hozzaad1=new JLabel("Típus:");
	JLabel hozzaad2=new JLabel("Darabszám:");
	JLabel hozzaad3=new JLabel("Ár:");
	JTextField textf0=new JTextField(20);
	JComboBox textf1=new JComboBox(combob);
	JTextField textf2=new JTextField(5);
	JTextField textf3=new JTextField(10);
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		//melyik gomb aktiv/passziv
		hozzaad.setEnabled(false);
		torles.setEnabled(true);
		vasarlas.setEnabled(true);
		rendeles.setEnabled(true);
		vasarlas.setEnabled(true);
		//gombok engedelyezes vege
		
		data.setColEditablefalse();
		JButton addgomb=new JButton("Hozzáad!");
		downpanel.removeAll();
		downpanel.add(hozzaad0);
		downpanel.add(textf0);
		downpanel.add(hozzaad1);
		downpanel.add(textf1);
		downpanel.add(hozzaad2);
		downpanel.add(textf2);
		downpanel.add(hozzaad3);
		downpanel.add(textf3);
		downpanel.add(addgomb);
		downpanel.repaint();
		downpanel.revalidate();
		addgomb.addActionListener(new hozzaadplusActionListener());
	}
	
	public class hozzaadplusActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			String name=textf0.getText();
			String type=(String)textf1.getSelectedItem();
			int amount=Integer.parseInt(textf2.getText());
			double price= Double.parseDouble(textf3.getText());
			data.addCD(name, type, amount, price);
		}
	}
}

//CD-k kilistázása nyomógomb
//JTable ben jelenítjük meg a CD listát
public class listazasActionListener implements ActionListener
{
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		//ablak beállitása
		listazas.setEnabled(false);
		data.setColEditablefalse();
		JFrame tableWindow=new JFrame("CD nyilvántartás lista");
		tableWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		width=(width-1024)/2;
		tableWindow.setLocation((int)width, 135);
		tableWindow.setMinimumSize(new Dimension(1024, 500));
		
		//tábla betöltése
		data.setColumnWidths(table);
		table.setAutoCreateRowSorter(true);
		TableColumn typeColumn=table.getColumnModel().getColumn(2);
		typeColumn.setCellEditor(new DefaultCellEditor(type));
		
		hozzaad.setEnabled(true);
		frissites.setEnabled(true);
		torles.setEnabled(true);
		vasarlas.setEnabled(true);
		rendeles.setEnabled(true);
		table.setBackground(new Color(236,236,255));
		JScrollPane scrollpane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		downpanel.removeAll();
		downpanel.setBackground(new Color(108,190,58));
		tableWindow.add(scrollpane,BorderLayout.CENTER);
		tableWindow.add(downpanel,BorderLayout.SOUTH);
		scrollpane.setVisible(true);
		tableWindow.setVisible(true);

		//ablakot ha bezárjuk akkor a menüben nyomógombok deaktiválódjanak
		//+adatok elmentése
		tableWindow.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				listazas.setEnabled(true);
				hozzaad.setEnabled(false);
				frissites.setEnabled(false);
				torles.setEnabled(false);
				vasarlas.setEnabled(false);
				rendeles.setEnabled(false);
				data.CD_data_write();
			}
		});

	}
}

//Menu ablak beállításai
public Menu_frame()
{
	super("CD-nyilvántartó MENU");
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	data.CD_data_read();
	
	// Képernyõ felbontásának lekérdezése
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	double width = screenSize.getWidth();
	width=(width-1024)/2; // csak a szélesség, kell hogy középre tudjam rakni az ablakom

	this.setLocation((int)width, 5);
	setMinimumSize(new Dimension(1024, 120));
	this.setResizable(false); // ne lehessen változtatni az ablak nagyságát
	Menu_components();

	// ha bezárjuk az ablakot mentse el az adatokat
	this.addWindowListener(new WindowAdapter()
	{
		@Override
		public void windowClosing(WindowEvent e)
		{
			data.CD_data_write();
		}
	});
}

public static void main(String[] args)
{
	Menu_frame mf = new Menu_frame();
	mf.setVisible(true);
}
}