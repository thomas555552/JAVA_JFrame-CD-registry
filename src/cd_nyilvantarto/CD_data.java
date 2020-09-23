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
	List<CD> cds=new ArrayList<CD>(); //Lista l�trehoz�sa CD-kb�l
	
	// Melyik oszlop szerkeszthet�
	public boolean[] columnisEditable = {false, false, false, false, false, false, false};
	
	//Oszlopok nevei
	private String[] columnNames = {"K�d", "N�v", "T�pus", "Darabsz�m", "�r (Ft)", "Elad�s", "Rendel�s (db)"};
	
//megadja h�ny oszlop van
@Override
public int getColumnCount() {
// TODO Auto-generated method stub
	return 7;
}

// lek�ri a lista m�ret�t, ez adja meg h�ny sor van
@Override
public int getRowCount() {
// TODO Auto-generated method stub
	return cds.size();
}

// melyik oszlopba melyik adat ker�l
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

// oszlopok neveinek megad�sa
@Override
public String getColumnName(int col) {
	return columnNames[col];
}

//milyen t�pus� az oszlop
@Override
public Class getColumnClass(int c) {
	return getValueAt(0,c).getClass();
}

// melyik oszlop szerkeszthet�
@Override
public boolean isCellEditable(int row, int col){
	return columnisEditable[col];
}

//melyik oszlopban melyik adatot k�ri
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

// CD hozz�ad�sa a list�hoz(n�v,t�pus,db)
public void addCD(String name, String type,int amount){
	//k�d gener�l�sa, t�pus els� k�t karaktere nagy bet�kkel,�s a lista m�rete
	String code="#"+type.toUpperCase().charAt(0)+type.toUpperCase().charAt(1)+cds.size()+"/#";
	cds.add(new CD(code, name, type, amount, 0, false, 0));
	fireTableDataChanged();
}

// CD hozz�ad�sa a list�hoz(n�v,t�pus,db,�r)
public void addCD(String name, String type,int amount,double price){
	//k�d gener�l�sa, t�pus els� k�t karaktere nagy bet�kkel,�s a lista m�rete
	String code="#"+type.toUpperCase().charAt(0)+type.toUpperCase().charAt(1)+cds.size()+"/#";
	cds.add(new CD(code, name, type, amount, price, false, 0));
	fireTableDataChanged();
}

// Oszlopok sz�less�g�nek megad�sa
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

// be�ll�tja melyik oszlop szerkeszthet�
public void setColisEditable(boolean a[])
{
	columnisEditable=a;
}

//�sszes oszlopra be�ll�tja hogy ne legyen szerkeszthet�
public void setColEditablefalse()
{
	boolean a[]={false, false, false, false, false, false, false};
	columnisEditable=a;
}

//CD t�rl�se a list�b�l
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

//kit�rli az elad�s oszlopban a bejel�l�seket
public void clearSoldboxes()
{
	for(int i=0;i<cds.size();i++)
	{
		cds.get(i).setSale(false); // be�ll�tja az �sszeset hamisra
	}
	fireTableDataChanged();
}

//elmenti a bejel�lt elad�sra k�v�nt CD ket
public void saveSoldCDs()
{
	List<CD> soldCDs=new ArrayList<CD>();//l�trehoz egy CD list�t,amibe csak az eladni k�v�nt CDk ker�lnek
	for(int i=0;i<cds.size();i++)
	{
		//melyik CD-t szeretn�k eladni,megn�zi melyikn�l lett bejel�lve
		//+ megn�zi hogy van-e bel�le,ha nincs akkor az nem ker�l elad�sra
		if((cds.get(i).hasSale())==true && cds.get(i).getAmount()!=0)
		{
			cds.get(i).setAmount(cds.get(i).getAmount()-1);
			soldCDs.add(cds.get(i));
		}
	}
	
	//a f�jl neve a d�tum+.dat
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
			//elad�sn�l a CD k�dj�t,nev�t,t�pus�t �s �r�t menti ki
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


// elad�s elment�se CSV f�jlba
public void saveSoldCDsCSV() {
	// TODO Auto-generated method stub
	List<CD> soldCDs=new ArrayList<CD>();//l�trehoz egy CD list�t,amibe csak az eladni k�v�nt CDk ker�lnek
	for(int i=0;i<cds.size();i++)
	{
		//melyik CD-t szeretn�k eladni,megn�zi melyikn�l lett bejel�lve
		//+ megn�zi hogy van-e bel�le,ha nincs akkor az nem ker�l elad�sra
		if((cds.get(i).hasSale())==true && cds.get(i).getAmount()!=0)
		{
			cds.get(i).setAmount(cds.get(i).getAmount()-1);
			soldCDs.add(cds.get(i));
		}
	}
	//a f�jl neve a d�tum+.dat
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
			bw.write(" K�d;N�v;Tipus;�r");
			bw.newLine();
			for(int i=0;i<soldCDs.size();i++)
			{
				//elad�sn�l a CD k�dj�t,nev�t,t�pus�t �s �r�t menti ki
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

//beolvassa a kiv�laszott f�jlb�l az elad�sokat
public String[] loadSaveCDs(String s) throws IOException
{
	String[] allf = null;
	int i=0;
	File load = new File(System.getProperty("user.dir"));
	File f=new File(load,"eladas");
	File loadFrom=new File(f,s);
	if(!loadFrom.isFile()) throw new IOException("Nem l�tezik a f�jl!");
	
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

//kit�rli a beirt rendel�seket
public void clearOrderedboxes()
{
	for(int i=0;i<cds.size();i++)
	{
		cds.get(i).setOrdered(0); // az oszlopban l�v� sorokat 0-ra �ll�tja
	}
	fireTableDataChanged();
}

//elmenti a bejel�lt rendel�sre k�v�nt CD ket
public void saveOrderedCDs()
{
	List<CD> orderedCDs=new ArrayList<CD>();
	for(int i=0;i<cds.size();i++)
	{
		//melyik CD-b�l szeretn�nk rendelni,megn�zi melyikn�l lett be�rva 0-t�l k�l�nb�z� adat
		if((cds.get(i).hasOrdered())!=0)
			orderedCDs.add(cds.get(i));
	}
	
	//a f�jl neve d�tum+.dat // rendel�s mapp�n bel�l
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
			//elmenti a CD k�dj�t, nev�t, t�pus�t h�ny darabot rendelt�nk, �s a rendelt mennyis�g szerint az �rat
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

//rendel�s elment�se CSV f�jlba
public void saveOrderedCDscsv() {
	// TODO Auto-generated method stub
	List<CD> orderedCDs=new ArrayList<CD>();
	for(int i=0;i<cds.size();i++)
	{
		//melyik CD-b�l szeretn�nk rendelni,megn�zi melyikn�l lett be�rva 0-t�l k�l�nb�z� adat
		if((cds.get(i).hasOrdered())!=0)
			orderedCDs.add(cds.get(i));
	}
	
	//a f�jl neve d�tum+.dat // rendel�s mapp�n bel�l
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
		bw.write(" K�d;N�v;Tipus;Darab;�r");
		bw.newLine();
		for(int i=0;i<orderedCDs.size();i++)
		{
			//elmenti a CD k�dj�t, nev�t, t�pus�t h�ny darabot rendelt�nk, �s a rendelt mennyis�g szerint az �rat
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


//beolvassa a kiv�laszott f�jlb�l az rendel�seket
public String[] loadOrderedCDs(String s) throws IOException
{
	String[] allf = null;
	int i=0;
	File load = new File(System.getProperty("user.dir"));
	File loadFrom=new File(load,s);
	if(!loadFrom.isFile()) throw new IOException("Nem l�tezik a f�jl!");
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

// beolvassa az CD.dat b�l az adatb�zisban l�v� CD ket
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

// ki�rja az �sszes CD-t CD.dat adatb�zisba
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