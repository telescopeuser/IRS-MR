/**
 * Chap 6 /7 sample 
 * 
 * (c) Paul Browne, FirstPartners.net 
 * Contains sample code from FIT and Drools
 * Chap 6/7/ available under the GPL
 */
package net.firstpartners.chap6.domain;

import java.util.ArrayList;
import java.util.Iterator;

public class CustomerOrder {

	private long currentBalance;
	private long initialBalance;

	private ArrayList<ChocolateShipment> shipments = new ArrayList<ChocolateShipment>();

	public CustomerOrder() {
	}

	public CustomerOrder(long amount) {
		super();
		setInitialBalance(amount);
		setCurrentBalance(amount);
	}

	public void addShipment(ChocolateShipment shipment) {

		// Update the current balance on ourselves and the repayment
		currentBalance = currentBalance - shipment.getShipmentAmount();
		shipment.setItemsStillToShip(currentBalance);

		shipments.add(shipment);

	}

	public Iterator<ChocolateShipment> iterator() {
		return shipments.iterator();
	}

	public long getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(long loanBalance) {
		this.currentBalance = loanBalance;
	}

	public long getInitialBalance() {
		return initialBalance;
	}

	public void setInitialBalance(long initialBalance) {
		this.initialBalance = initialBalance;
	}

	public ArrayList<ChocolateShipment> getShipments() {
		return shipments;
	}

	public void setShipments(ArrayList<ChocolateShipment> loanPayments) {
		this.shipments = loanPayments;
	}

	public String toString() {
		StringBuffer returnValue = new StringBuffer("Initial Chocolate Order:");
		returnValue.append(initialBalance);
		returnValue.append(" itemsStillToShip:");
		returnValue.append(currentBalance);
		returnValue.append(" shipments:");

		for (ChocolateShipment shipment : getShipments()) {
			returnValue.append("\n" + shipment.toString());
		}

		if ((getShipments() == null)||(getShipments().isEmpty())) {
			returnValue.append("none-listed");
		}

		return returnValue.toString();
	}

}
