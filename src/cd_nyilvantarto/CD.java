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
//CD l�trehoz�s konstruktorral
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

//K�d lek�rdez�se
public String getCode()
{
	return code;
}

//K�d be�ll�t�sa
public void setCode(String code)
{
	this.code=code;
}

//N�v lek�rdez�se
public String getName()
{
	return name;
}

//N�v be�ll�t�sa
public void setName(String name)
{
	this.name=name;
}

//T�pus lek�rdez�se
public String getType()
{
	return type;
}

//T�pus be�ll�t�sa
public void setType(String type)
{
	this.type=type;
}

//Darab lek�rdez�se
public int getAmount()
{
	return amount;
}

//Darab be�ll�t�sa
public void setAmount(int amount)
{
	this.amount=amount;
}

//Darab lek�rdez�se
public double getPrice()
{
	return price;
}

//Darab be�ll�t�sa
public void setPrice(double price)
{
	this.price=price;
}

//Elad�s lek�rdez�se
public boolean hasSale()
{
	return sale;
}

//Elad�s be�ll�t�sa
public void setSale(boolean sale)
{
	this.sale=sale;
}

//Rendel�s lek�rdez�se
public int hasOrdered()
{
	return ordered;
}

//Rendel�s be�ll�t�sa
public void setOrdered(int ordered)
{
	this.ordered=ordered;
}

}