package cd_nyilvantarto;

import java.io.Serializable;
//1 DB CD
public class CD implements Serializable{
//1 DB CD adatai
private String code;
private String name;
private String type;
private int amount;
private double price;
private boolean sale;
private int ordered;
//CD létrehozás konstruktorral
public CD(String code,String name,String type,int amount,double price,boolean sale,int ordered)
{
	this.code=code;
	this.name=name;
	this.type=type;
	this.amount=amount;
	this.price=price;
	this.sale=sale;
	this.ordered=ordered;
}

//Kód lekérdezése
public String getCode()
{
	return code;
}

//Kód beállítása
public void setCode(String code)
{
	this.code=code;
}

//Név lekérdezése
public String getName()
{
	return name;
}

//Név beállítása
public void setName(String name)
{
	this.name=name;
}

//Típus lekérdezése
public String getType()
{
	return type;
}

//Típus beállítása
public void setType(String type)
{
	this.type=type;
}

//Darab lekérdezése
public int getAmount()
{
	return amount;
}

//Darab beállítása
public void setAmount(int amount)
{
	this.amount=amount;
}

//Darab lekérdezése
public double getPrice()
{
	return price;
}

//Darab beállítása
public void setPrice(double price)
{
	this.price=price;
}

//Eladás lekérdezése
public boolean hasSale()
{
	return sale;
}

//Eladás beállítása
public void setSale(boolean sale)
{
	this.sale=sale;
}

//Rendelés lekérdezése
public int hasOrdered()
{
	return ordered;
}

//Rendelés beállítása
public void setOrdered(int ordered)
{
	this.ordered=ordered;
}

}