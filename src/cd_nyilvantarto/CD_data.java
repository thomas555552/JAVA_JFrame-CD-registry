package cd_nyilvantarto;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

public class CD_data extends AbstractTableModel
{
	List<CD> cds=new ArrayList<CD>(); //Lista létrehozása CD-kbõl
	
	// Melyik oszlop szerkeszthetõ
	public boolean[] columnisEditable = {false, false, false, false, false, false, false};
	
	//Oszlopok nevei
	private String[] columnNames = {"Kód", "Név", "Típus", "Darabszám", "Ár (Ft)", "Eladás", "Rendelés (db)"};
	
//megadja hány oszlop van
@Override
public int getColumnCount() {
// TODO Auto-generated method stub
	return 7;
}

// lekéri a lista méretét, ez adja meg hány sor van
@Override
public int getRowCount() {
// TODO Auto-generated method stub
	return cds.size();
}

// melyik oszlopba melyik adat kerül
@Override
public Object getValueAt(int rowIndex, int columnIndex) {
// TODO Auto-generated method stub
	CD cd1 = cds.get(rowIndex);
	switch(columnIndex){
		case 0: return cd1.getCode();
		case 1: return cd1.getName();
		case 2: return cd1.getType();
		case 3: return cd1.getAmount();
		case 4: return cd1.getPrice();
		case 5: return cd1.hasSale();
		default: return cd1.hasOrdered();
}
}

// oszlopok neveinek megadása
@Override
public String getColumnName(int col) {
	return columnNames[col];
}

//milyen típusú az oszlop
@Override
public Class getColumnClass(int c) {
	return getValueAt(0,c).getClass();
}

// melyik oszlop szerkeszthetõ
@Override
public boolean isCellEditable(int row, int col){
	return columnisEditable[col];
}

//melyik oszlopban melyik adatot kéri
@Override
public void setValueAt(Object value, int row, int col){
	if(col == 0)
		cds.get(row).setCode((String)value);
	if(col == 1)
		cds.get(row).setName((String)value);
	if(col == 2)
		cds.get(row).setType((String)value);
	if(col == 3)
		cds.get(row).setAmount((Integer)value);
	if(col==4)
		cds.get(row).setPrice((Double)value);
	if(col == 5)
		cds.get(row).setSale((Boolean)value);
	if(col == 6)
		cds.get(row).setOrdered((Integer)value);
	fireTableCellUpdated(row,col);
}

// CD hozzáadása a listához(név,típus,db)
public void addCD(String name, String type,int amount){
	//kód generálása, típus elsõ két karaktere nagy betûkkel,és a lista mérete
	String code="#"+type.toUpperCase().charAt(0)+type.toUpperCase().charAt(1)+cds.size()+"/#";
	cds.add(new CD(code, name, type, amount, 0, false, 0));
	fireTableDataChanged();
}

// CD hozzáadása a listához(név,típus,db,ár)
public void addCD(String name, String type,int amount,double price){
	//kód generálása, típus elsõ két karaktere nagy betûkkel,és a lista mérete
	String code="#"+type.toUpperCase().charAt(0)+type.toUpperCase().charAt(1)+cds.size()+"/#";
	cds.add(new CD(code, name, type, amount, price, false, 0));
	fireTableDataChanged();
}

// Oszlopok szélességének megadása
public void setColumnWidths(JTable table)
{
	TableColumn column = null;
	for (int i = 0; i < 7; i++) {
		column = table.getColumnModel().getColumn(i);
		if(i==0) column.setPreferredWidth(20);
		if(i==3) column.setPreferredWidth(20);
		if(i==5) column.setPreferredWidth(20);
		if(i==6) column.setPreferredWidth(20);
}
}

// beállítja melyik oszlop szerkeszthetõ
public void setColisEditable(boolean a[])
{
	columnisEditable=a;
}

//összes oszlopra beállítja hogy ne legyen szerkeszthetõ
public void setColEditablefalse()
{
	boolean a[]={false, false, false, false, false, false, false};
	columnisEditable=a;
}

//CD törlése a listából
public void deleteCD(String code)
{
	String fullcode="#"+code+"/#";
	int j = 0;
	for(int i=0;i<cds.size();i++)
	{
		if(cds.get(i).getCode().equals(fullcode)) {j=i; break;};
	}
	
	cds.remove(j);
	fireTableDataChanged();
}

//kitörli az eladás oszlopban a bejelöléseket
public void clearSoldboxes()
{
	for(int i=0;i<cds.size();i++)
	{
		cds.get(i).setSale(false); // beállítja az összeset hamisra
	}
	fireTableDataChanged();
}

//elmenti a bejelölt eladásra kívánt CD ket
public void saveSoldCDs()
{
	List<CD> soldCDs=new ArrayList<CD>();//létrehoz egy CD listát,amibe csak az eladni kívánt CDk kerülnek
	for(int i=0;i<cds.size();i++)
	{
		//melyik CD-t szeretnék eladni,megnézi melyiknél lett bejelölve
		//+ megnézi hogy van-e belõle,ha nincs akkor az nem kerül eladásra
		if((cds.get(i).hasSale())==true && cds.get(i).getAmount()!=0)
		{
			cds.get(i).setAmount(cds.get(i).getAmount()-1);
			soldCDs.add(cds.get(i));
		}
	}
	
	//a fájl neve a dátum+.dat
	String filename=new SimpleDateFormat("yyyy_MM_dd_HHmmss").format(new Date());
	filename=filename+".dat";
	File save = new File(System.getProperty("user.dir"));
	File f=new File(save,"eladas");
	
	if (!f.isDirectory())
	{ }
	if (f.isDirectory())
		save = new File(save,"eladas");
	
	File filetodir=new File(save,filename);
	
	try{
		FileWriter fw=new FileWriter(filetodir);
		BufferedWriter bw=new BufferedWriter(fw);
		for(int i=0;i<soldCDs.size();i++)
		{
			//eladásnál a CD kódját,nevét,típusát és árát menti ki
			bw.write(" "+soldCDs.get(i).getCode()+" "+soldCDs.get(i).getName()+" "+soldCDs.get(i).getType()+
					" 1x "+(int)soldCDs.get(i).getPrice());
			bw.newLine();
		}
		
		bw.close();
	}catch(IOException e)
	{
		e.printStackTrace();
	}
	clearSoldboxes();
}


// eladás elmentése CSV fájlba
public void saveSoldCDsCSV() {
	// TODO Auto-generated method stub
	List<CD> soldCDs=new ArrayList<CD>();//létrehoz egy CD listát,amibe csak az eladni kívánt CDk kerülnek
	for(int i=0;i<cds.size();i++)
	{
		//melyik CD-t szeretnék eladni,megnézi melyiknél lett bejelölve
		//+ megnézi hogy van-e belõle,ha nincs akkor az nem kerül eladásra
		if((cds.get(i).hasSale())==true && cds.get(i).getAmount()!=0)
		{
			cds.get(i).setAmount(cds.get(i).getAmount()-1);
			soldCDs.add(cds.get(i));
		}
	}
	//a fájl neve a dátum+.dat
		String filename=new SimpleDateFormat("yyyy_MM_dd_HHmmss").format(new Date());
		filename=filename+".csv";
		File save = new File(System.getProperty("user.dir"));
		File f=new File(save,"eladas");
		
		if (!f.isDirectory())
		{ }
		if (f.isDirectory())
			save = new File(save,"eladas");
		
		File filetodir=new File(save,filename);
		
		try{
			FileWriter fw=new FileWriter(filetodir);
			BufferedWriter bw=new BufferedWriter(fw);
			bw.write(" Kód;Név;Tipus;Ár");
			bw.newLine();
			for(int i=0;i<soldCDs.size();i++)
			{
				//eladásnál a CD kódját,nevét,típusát és árát menti ki
				bw.write(" "+soldCDs.get(i).getCode()+";"+soldCDs.get(i).getName()+";"+soldCDs.get(i).getType()+
						";"+(int)soldCDs.get(i).getPrice());
				bw.newLine();
			}
			
			bw.close();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		clearSoldboxes();
}

//beolvassa a kiválaszott fájlból az eladásokat
public String[] loadSaveCDs(String s) throws IOException
{
	String[] allf = null;
	int i=0;
	File load = new File(System.getProperty("user.dir"));
	File f=new File(load,"eladas");
	File loadFrom=new File(f,s);
	if(!loadFrom.isFile()) throw new IOException("Nem létezik a fájl!");
	
	try{
		FileReader fr=new FileReader(loadFrom);
		BufferedReader bf=new BufferedReader(fr);
		while(true)
		{
			allf[i]=bf.readLine();
			i++;
		}
	}catch(IOException e)
	{
		e.printStackTrace();
	}
	return allf;
}

//kitörli a beirt rendeléseket
public void clearOrderedboxes()
{
	for(int i=0;i<cds.size();i++)
	{
		cds.get(i).setOrdered(0); // az oszlopban lévõ sorokat 0-ra állítja
	}
	fireTableDataChanged();
}

//elmenti a bejelölt rendelésre kívánt CD ket
public void saveOrderedCDs()
{
	List<CD> orderedCDs=new ArrayList<CD>();
	for(int i=0;i<cds.size();i++)
	{
		//melyik CD-bõl szeretnénk rendelni,megnézi melyiknél lett beírva 0-tól különbözõ adat
		if((cds.get(i).hasOrdered())!=0)
			orderedCDs.add(cds.get(i));
	}
	
	//a fájl neve dátum+.dat // rendelés mappán belül
	String filename=new SimpleDateFormat("yyyy_MM_dd_HHmmss").format(new Date());
	filename=filename+".dat";
	File order = new File(System.getProperty("user.dir"));
	File f=new File(order,"rendeles");
	if (!f.isDirectory())
	{ }
	if (f.isDirectory())
		order = new File(order,"rendeles");
	
	File filetodir=new File(order,filename);
	try{
		FileWriter fw = new FileWriter(filetodir);
		BufferedWriter bw=new BufferedWriter(fw);
		for(int i=0;i<orderedCDs.size();i++)
		{
			//elmenti a CD kódját, nevét, típusát hány darabot rendeltünk, és a rendelt mennyiség szerint az árat
			bw.write(" "+orderedCDs.get(i).getCode()+" "+orderedCDs.get(i).getName()+" "+orderedCDs.get(i).getType()+
					" "+orderedCDs.get(i).hasOrdered()+" "+(int)((orderedCDs.get(i).hasOrdered())*(orderedCDs.get(i).getPrice())));
			bw.newLine();
		}
		bw.close();
	}catch(Exception e)
	{
		e.printStackTrace();
	}
	
	clearOrderedboxes();
}

//rendelés elmentése CSV fájlba
public void saveOrderedCDscsv() {
	// TODO Auto-generated method stub
	List<CD> orderedCDs=new ArrayList<CD>();
	for(int i=0;i<cds.size();i++)
	{
		//melyik CD-bõl szeretnénk rendelni,megnézi melyiknél lett beírva 0-tól különbözõ adat
		if((cds.get(i).hasOrdered())!=0)
			orderedCDs.add(cds.get(i));
	}
	
	//a fájl neve dátum+.dat // rendelés mappán belül
	String filename=new SimpleDateFormat("yyyy_MM_dd_HHmmss").format(new Date());
	filename=filename+".csv";
	File order = new File(System.getProperty("user.dir"));
	File f=new File(order,"rendeles");
	if (!f.isDirectory())
	{ }
	if (f.isDirectory())
		order = new File(order,"rendeles");
	
	File filetodir=new File(order,filename);
	try{
		FileWriter fw = new FileWriter(filetodir);
		BufferedWriter bw=new BufferedWriter(fw);
		bw.write(" Kód;Név;Tipus;Darab;Ár");
		bw.newLine();
		for(int i=0;i<orderedCDs.size();i++)
		{
			//elmenti a CD kódját, nevét, típusát hány darabot rendeltünk, és a rendelt mennyiség szerint az árat
			bw.write(" "+orderedCDs.get(i).getCode()+";"+orderedCDs.get(i).getName()+";"+orderedCDs.get(i).getType()+
					";"+orderedCDs.get(i).hasOrdered()+";"+(int)((orderedCDs.get(i).hasOrdered())*(orderedCDs.get(i).getPrice())));
			bw.newLine();
		}
		bw.close();
	}catch(Exception e)
	{
		e.printStackTrace();
	}
	
	clearOrderedboxes();
}


//beolvassa a kiválaszott fájlból az rendeléseket
public String[] loadOrderedCDs(String s) throws IOException
{
	String[] allf = null;
	int i=0;
	File load = new File(System.getProperty("user.dir"));
	File loadFrom=new File(load,s);
	if(!loadFrom.isFile()) throw new IOException("Nem létezik a fájl!");
	try{
		FileReader fr=new FileReader(loadFrom);
		BufferedReader bf=new BufferedReader(fr);
		while(true)
		{
			allf[i]=bf.readLine();
			i++;
		}
	}catch(IOException e)
	{
		e.printStackTrace();
	}
	
	return allf;
}

// beolvassa az CD.dat ból az adatbázisban lévõ CD ket
public void CD_data_read()
{
	try {
		FileInputStream fs=new FileInputStream("CD.dat");
		ObjectInputStream ois = new ObjectInputStream(fs);
		cds=(List<CD>) (ois.readObject());
		ois.close();
	} catch(Exception ex)
	{
		ex.printStackTrace();
	}
}

// kiírja az összes CD-t CD.dat adatbázisba
public void CD_data_write()
{
	try {
		FileOutputStream fs=new FileOutputStream("CD.dat");
		ObjectOutputStream oos = new ObjectOutputStream(fs);
		oos.writeObject(cds);
		oos.close();
	} catch(Exception ex)
	{
		ex.printStackTrace();
	}
}




}