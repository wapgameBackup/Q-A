package pl.projectdcm.data;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class ResultSetTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = -8026940486654155764L;
	private ResultSet _rs;
	private ResultSetMetaData _rsmd;

	private ArrayList<Object[]> _cache;

	public ResultSetTableModel(ResultSet rs) {
		_cache = new ArrayList<Object[]>();
		try {
			if (rs != null) { 
				setResultSet(rs);
			} 
		} catch (SQLException e) {
			System.out.println("Error " + e);
		}
	}
	
	public String getColumnName(int c) {
		try {
			return _rsmd.getColumnName(c + 1);
		} catch (SQLException e) {
			System.err.println("Error " + e);
			return "";
		}
	}

	public int getColumnCount() {
		if (_rsmd == null) { return 0; }
		try {
			return _rsmd.getColumnCount();
		} catch (SQLException e) {
			System.err.println("Error " + e);
			return 0;
		}
	}

	protected ResultSet getResultSet() {
		return _rs;
	}

	public Object getValueAt(int r, int c) {
		if (r < _cache.size())
			return ((Object[]) _cache.get(r))[c];
		else
			return null;
	}

	public int getRowCount() {
		return _cache.size();
	}
	
	public void setResultSet(ResultSet rs) throws SQLException {
		_rs = rs;
		if (_rs != null) {
			_rsmd = rs.getMetaData();
		} else {
			_rsmd = null;
		}
			
		_cache = new ArrayList<Object[]>();
		int cols = getColumnCount();
		
		/* place all data in an array list of Object[] arrays
		 We don't use an Object[][] because we don't know
		 how many rows are in the result set
		 */
		if (rs != null) {
			while (rs.next()) {
				Object[] row = new Object[cols];
				for (int j = 0; j < row.length; j++)
					row[j] = rs.getObject(j + 1);
				_cache.add(row)
				;
			}
		}
		
		fireTableStructureChanged();
	}
}
