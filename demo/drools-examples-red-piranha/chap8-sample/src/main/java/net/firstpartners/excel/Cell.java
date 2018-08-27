package net.firstpartners.excel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Cell implements PropertyChangeListener {

	private final PropertyChangeSupport changes = new PropertyChangeSupport(
			this);

	private String cellName = null;

	private String comment;

	private boolean modified = false;

	private Object value;

	private Range holdingRange = null;

	// Default constructor - needed to keep this a Javabean
	public Cell() {
	}

	public Cell(String cellName, Object value) {
		super();
		this.cellName = cellName;
		this.value = value;
	}

	public Range getHoldingRange() {
		return holdingRange;
	}

	public void setHoldingRange(Range holdingRange) {
		this.holdingRange = holdingRange;
	}

	public String getCellName() {
		return cellName;
	}

	public String getComment() {
		return comment;
	}

	public Object getValue() {
		return value;
	}
	
	public Integer getIntValue(){
		if((value!=null)&&(value instanceof Number)){
			return ((Number)value).intValue();
		}
		
		//Default
		return null;
	}
	
	public Boolean getBooleanValue(){
		if((value!=null)&&(value instanceof Boolean)){
			return ((Boolean)value);
		}
		
		//Default
		return null;
	}
	
	/**
	 * Convenience method
	 * @return
	 */
	public String getValueAsText(){
		if(value!=null){
			return value.toString();
		}
		
		//default
		return null;
	}

	public boolean isModified() {
		return modified;
	}

	public void setCellName(String cellName) {

		String oldValue = this.cellName;
		this.cellName = cellName;
		this.modified = true;
		this.changes.firePropertyChange("cellName", oldValue, cellName);

	}

	public void setComment(String comment) {
		String oldValue = this.comment;
		this.comment = comment;
		this.modified = true;
		this.changes.firePropertyChange("comment", oldValue, comment);
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public void setValue(Object value) {
		Object oldValue = this.value;
		this.value = value;
		this.modified = true;
		this.changes.firePropertyChange("value", oldValue, value);
	}

	@Override
	public String toString() {
		return "cellName:" + cellName + " value:" + value + " comment:"
				+ comment + " modified:" + modified;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cellName == null) ? 0 : cellName.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + (modified ? 1231 : 1237);
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cell other = (Cell) obj;
		if (cellName == null) {
			if (other.cellName != null)
				return false;
		} else if (!cellName.equals(other.cellName))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (modified != other.modified)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	public void propertyChange(PropertyChangeEvent arg0) {

		// Pass on the event to the registered listener
		changes.firePropertyChange(arg0);

	}

	public void addPropertyChangeListener(final PropertyChangeListener l) {
		this.changes.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(final PropertyChangeListener l) {
		this.changes.removePropertyChangeListener(l);
	}

}
