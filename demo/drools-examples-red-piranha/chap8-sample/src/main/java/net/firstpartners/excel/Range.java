package net.firstpartners.excel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A range is just a holder for a group of named cells
 * 
 * @author paul
 */
public class Range implements Map<String, Cell>, PropertyChangeListener {

	private final PropertyChangeSupport changes = new PropertyChangeSupport(
			this);

	public static final String CELLNAME_NUM_SEPARATOR = "_";

	private static final long serialVersionUID = 1L;

	private Map<String, Cell> cells = new HashMap<String, Cell>();

	private String rangeName = null;

	private Log log = LogFactory.getLog(Range.class);

	public Range() {
		super();
	}

	public Range(String rangeName) {
		this.rangeName = rangeName;
	}

	public void clear() {
		cells.clear();
	}

	public String getRangeName() {
		return rangeName;
	}

	public void setRangeName(String rangeName) {
		this.rangeName = rangeName;
	}

	public boolean containsKey(Object arg0) {
		return cells.containsKey(arg0);
	}

	public boolean containsValue(Object arg0) {

		return cells.containsValue(arg0);
	}

	public Set<java.util.Map.Entry<String, Cell>> entrySet() {
		return cells.entrySet();
	}

	public Cell get(Object arg0) {
		return cells.get(arg0);
	}

	/**
	 * Get the name of a position cell in the range e.g. if the cell name is
	 * range_8 then , we can ask for the first cell range_0
	 * 
	 * @param currentCellName
	 * @param newCellPosition
	 * @return
	 */
	public String getCellName(String currentFullCellName, int newCellPosition) {

		if (currentFullCellName == null) {
			// log.debug("currentFullCellName is null, returning null");
			return null;
		}

		// Break at the *last* separator
		int breakpoint = currentFullCellName
				.lastIndexOf(CELLNAME_NUM_SEPARATOR);
		String mainPart = currentFullCellName.substring(0, breakpoint);

		// make up desired name and return , leaving off the last
		return mainPart + CELLNAME_NUM_SEPARATOR + newCellPosition;

	}

	/**
	 * Get the Actual Cell of a position cell in the range e.g. if the cell name
	 * is range_8 then , we can ask for the first cell range_0
	 * 
	 * @param currentCellName
	 * @param newCellPosition
	 * @return
	 */
	public Cell getCellInRange(String currentFullCellName, int newCellPosition) {

		String requestedCellName = getCellName(currentFullCellName,
				newCellPosition);
		return cells.get(requestedCellName);

	}

	/**
	 * Get the Actual Cell of a position cell in the range e.g. if the cell name
	 * is range_8 then , we can ask for the first cell range_0
	 * 
	 * @param currentCellName
	 * @param newCellPosition
	 * @return
	 */
	public Cell getCell(int newCellPosition) {

		// log.debug("base range name:"+rangeName);

		// The last "_" is to fool the cell based algorithm into working when
		// we supply the range name only
		// e.g. cell name range_0 , range is called 'range'

		String requestedCellName = getCellName(rangeName + "_", newCellPosition);

		// log.debug("Looking up Cell value"+requestedCellName);

		return cells.get(requestedCellName);

	}

	/**
	 * Get the Actual Cell of a position cell in the range e.g. if the cell name
	 * is range_8 then , we can ask for the first cell range_0
	 * 
	 * @param currentCellName
	 * @param newCellPosition
	 * @return
	 */
	public Object getCellValue(int newCellPosition) {

		Cell thisCell = getCell(newCellPosition);

		if (thisCell != null) {
			// log.debug("cell value is null , returning null");
			return thisCell.getValue();
		}

		// Default
		return null;

	}

	public List<Object> getCellValueList() {

		ArrayList<Object> returnList = new ArrayList<Object>();

		for (Cell c : cells.values()) {
			if (c != null) {
				// log.debug("Cell Value list:"+c.getValue());
				returnList.add(c.getValue());
			}
		}

		return returnList;

	}

	public Object[] getCellValueArray() {

		return getCellValueList().toArray();

	}

	/**
	 * Matches on contents of cells, unlike conventional containsValue method
	 * that matches exact objects
	 * 
	 * @param value
	 * @return
	 */
	public boolean getRangeContainsValue(Object value) {

		return getCellValueList().contains(value);

	}

	/**
	 * Generate cell name based on internal string
	 * 
	 * @param cellInRange
	 * @return
	 */
	public String getUniqueCellName(int cellInRange) {

		return getUniqueCellName(rangeName, cellInRange);

	}

	/**
	 * Generate cell name based on supplied string
	 * 
	 * @param cellInRange
	 * @return
	 */
	public static String getUniqueCellName(String rangeNameExt, int cellInRange) {

		return rangeNameExt + CELLNAME_NUM_SEPARATOR + cellInRange;

	}

	public boolean isEmpty() {
		return cells.isEmpty();
	}

	public Set<String> keySet() {
		return cells.keySet();
	}

	public Cell put(String arg0, Cell arg1) {
		return cells.put(arg0, arg1);
	}

	public void putAll(Map<? extends String, ? extends Cell> arg0) {
		cells.putAll(arg0);
	}

	public Cell remove(Object arg0) {
		return cells.remove(arg0);
	}

	public int size() {
		return cells.size();
	}

	public Collection<Cell> values() {
		return cells.values();
	}

	public String toString() {

		StringBuffer returnString = new StringBuffer("Range:" + rangeName
				+ "\n");

		for (String cellName : cells.keySet()) {
			returnString.append(cellName + ": " + cells.get(cellName) + "\n");
		}

		return returnString.toString();
	}

	public String toShortString() {

		return "Range:" + rangeName;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cells == null) ? 0 : cells.hashCode());
		result = prime * result
				+ ((rangeName == null) ? 0 : rangeName.hashCode());
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
		Range other = (Range) obj;
		if (cells == null) {
			if (other.cells != null)
				return false;
		} else if (!cells.equals(other.cells))
			return false;
		if (rangeName == null) {
			if (other.rangeName != null)
				return false;
		} else if (!rangeName.equals(other.rangeName))
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
