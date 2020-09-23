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

String combob[]={"J�t�k","Film","Zene","Program","Sorozat","Egy�b"};// a t�pusok a comboboxhoz

// A menu nyom�gombjai,az�rt hogy el�rjem mindenhonnan
JButton listazas=new JButton("CD-k list�z�sa");
JButton hozzaad=new JButton("CD hozz�ad�s");
JButton frissites=new JButton("CD friss�t�s");
JButton torles=new JButton("CD t�rl�s");
JButton vasarlas=new JButton("CD v�s�rl�s");
JButton rendeles=new JButton("CD rendel�s");

JComboBox type=new JComboBox(combob);
JTable table = new JTable(data);

// menu ablakban l�v� komponensek
private void Menu_components()
{
	JButton mentettrendeles=new JButton("CD rendel�sek megtekint�se");
	JButton eladasok=new JButton("CD elad�sok megtekint�se");

	this.setLayout(new BorderLayout());
	JPanel panel=new JPanel();
	this.add(panel);
	panel.setLayout(new GridBagLayout());
	panel.setBackground(new Color(255,141,66));
	//Menu nyomogombok letrehozasa es hozzaadasa a panelhez
	panel.add(listazas);
	//p�r nyom�gomb addig nem haszn�lhat� m�g nincsennek kilist�zva a CD-k
	panel.add(hozzaad).setEnabled(false);
	panel.add(frissites).setEnabled(false);
	panel.add(torles).setEnabled(false);
	panel.add(vasarlas).setEnabled(false);
	panel.add(rendeles).setEnabled(false);
	panel.add(eladasok);
	panel.add(mentettrendeles);
	//az egyes nyom�gombok gombnyom�s figyel�i
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

//a CD rendel�sek megtekint�se nyom�gomb
public class mentettrendelesActionListener implements ActionListener
{
	JComboBox ordercombo=new JComboBox();
	JPanel rendelespanel=new JPanel();
	JFrame rendelesWindow=new JFrame("CD rendel�sek");
	
@Override
public void actionPerformed(ActionEvent arg0) {
// TODO Auto-generated method stub
	rendelespanel.removeAll();
	ordercombo.removeAllItems();
	File sold = new File(System.getProperty("user.dir"));
	File f=new File(sold,"rendeles"); // rendel�s mapp�n bel�l a f�jlok kilist�z�sa
	File[] list = f.listFiles();
	for (int i = 0; i < list.length; i++)
	{
		ordercombo.addItem(list[i].getName()); // comboboxhoz hozz�ad�sa a f�jlok nevei
	}
	
	JButton show=new JButton("Mutat:"); //a CD rendel�s megtekint�s�n bel�l egy nyom�gomb+figyel�se
	show.addActionListener(new showActionListener());
	
	//Cd rendel�s megtekint�se ablak be�ll�t�sai+elemek hozz�ad�sa
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

//Az Mutat nyom�gomb!
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
		if(!loadFrom.isFile()) throw new IOException("Nem l�tezik a f�jl!");
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

//a CD elad�sok megtekint�se nyom�gomb
//minden ugyanaz mint a rendel�sn�l,csak az elad�sok mapp�b�l list�zza a f�jlokat
public class eladasokActionListener implements ActionListener
{
	JComboBox soldcombo=new JComboBox();
	JPanel eladaspanel=new JPanel();
	JFrame eladasWindow=new JFrame("CD elad�sok");
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
				if(!loadFrom.isFile()) throw new IOException("Nem l�tezik a f�jl!");
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

//CD rendel�s nyomg�mb
public class rendelesActionListener implements ActionListener
{
	//A CD-k kilist�z�sa ut�n,mi jelenik meg az als� panelben
	//a panelhez hozz�adott elemek kezel�se
	JLabel rendeles0=new JLabel("Be�rhat�k a rendelni k�v�nt CD-k!");
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
		
		JButton ordergomb=new JButton("Rendel�s elment�se! (.dat)");
		JButton ordergombcsv= new JButton("Elad�s elment�se!(.csv)");
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

//CD v�s�rl�s nyom�gomb
public class vasarlasActionListener implements ActionListener
{
	//A CD-k kilist�z�sa ut�n,mi jelenik meg az als� panelben
	//a panelhez hozz�adott elemek kezel�se
	JLabel vasarlas0=new JLabel("Kiv�laszthat�k az eladni k�v�nt CD-k!");
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
		
		JButton sellgomb=new JButton("Elad�s elment�se!(.dat)");
		JButton sellgombcsv= new JButton("Elad�s elment�se!(.csv)");
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

//CD friss�t�s nyom�gomb
public class frissitesActionListener implements ActionListener
{
	//A CD-k kilist�z�sa ut�n,mi jelenik meg az als� panelben
	//a panelhez hozz�adott elemek kezel�se-itt csak annyi t�rt�nik,hogy
	// a k�d oszlopon k�v�l mindegyik szerkeszthet� lesz
	JLabel frissites0=new JLabel("A t�bl�zat szabadon szerkeszthet�! (Automatikus ment�s!!!) ");
	
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

//CD t�rl�s nyom�gomb
public class torlesActionListener implements ActionListener
{
	//A CD-k kilist�z�sa ut�n,mi jelenik meg az als� panelben
	//a panelhez hozz�adott elemek kezel�se
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

		JButton deletegomb=new JButton("T�rl�s!");
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

// CD hozz�ad�sa nyom�gomb
public class hozzaadActionListener implements ActionListener
{
	//A CD-k kilist�z�sa ut�n,mi jelenik meg az als� panelben
	//a panelhez hozz�adott elemek kezel�se
	JLabel hozzaad0=new JLabel("N�v:");
	JLabel hozzaad1=new JLabel("T�pus:");
	JLabel hozzaad2=new JLabel("Darabsz�m:");
	JLabel hozzaad3=new JLabel("�r:");
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
		JButton addgomb=new JButton("Hozz�ad!");
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

//CD-k kilist�z�sa nyom�gomb
//JTable ben jelen�tj�k meg a CD list�t
public class listazasActionListener implements ActionListener
{
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		//ablak be�llit�sa
		listazas.setEnabled(false);
		data.setColEditablefalse();
		JFrame tableWindow=new JFrame("CD nyilv�ntart�s lista");
		tableWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		width=(width-1024)/2;
		tableWindow.setLocation((int)width, 135);
		tableWindow.setMinimumSize(new Dimension(1024, 500));
		
		//t�bla bet�lt�se
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

		//ablakot ha bez�rjuk akkor a men�ben nyom�gombok deaktiv�l�djanak
		//+adatok elment�se
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

//Menu ablak be�ll�t�sai
public Menu_frame()
{
	super("CD-nyilv�ntart� MENU");
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	data.CD_data_read();
	
	// K�perny� felbont�s�nak lek�rdez�se
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	double width = screenSize.getWidth();
	width=(width-1024)/2; // csak a sz�less�g, kell hogy k�z�pre tudjam rakni az ablakom

	this.setLocation((int)width, 5);
	setMinimumSize(new Dimension(1024, 120));
	this.setResizable(false); // ne lehessen v�ltoztatni az ablak nagys�g�t
	Menu_components();

	// ha bez�rjuk az ablakot mentse el az adatokat
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