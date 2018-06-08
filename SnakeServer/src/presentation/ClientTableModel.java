package presentation;

import java.awt.Color;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class ClientTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = -9198732392880962681L;
	private List<InetAddress> ips = new ArrayList<InetAddress>();
	private List<Color> colors = new ArrayList<Color>();
	private String[] columnNames = {"Client", "Snake color"};
	
	public void recreate(List<InetAddress> ips, List<Color> colors)
	{
		this.ips = new ArrayList<InetAddress>(ips);
		this.colors = new ArrayList<Color>(colors);
		fireTableDataChanged();
	}
	
	@Override
	public int getRowCount()
	{
		return ips.size();
	}

	@Override
	public int getColumnCount()
	{
		return columnNames.length;
	}
	
	@Override
	public String getColumnName(int col) 
	{
        return columnNames[col];
    }

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return columnIndex == 0 ? ips.get(rowIndex) : colors.get(rowIndex);
	}
	
	@Override
	public Class getColumnClass(int c) 
	{
        return getValueAt(0, c).getClass();
    }
	
}
