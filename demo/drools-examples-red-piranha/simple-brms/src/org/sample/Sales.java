package org.sample;

import java.util.Date;

public class Sales {

	private String name;
	private long sales;
	private Date dateOfSale;
	private boolean choclateOnlyCustomer;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getSales() {
		return sales;
	}
	public void setSales(long sales) {
		this.sales = sales;
	}
	public Date getDateOfSale() {
		return dateOfSale;
	}
	public void setDateOfSale(Date dateOfSale) {
		this.dateOfSale = dateOfSale;
	}
	public boolean isChoclateOnlyCustomer() {
		return choclateOnlyCustomer;
	}
	public void setChoclateOnlyCustomer(boolean choclateOnlyCustomer) {
		this.choclateOnlyCustomer = choclateOnlyCustomer;
	}
	
	
}
