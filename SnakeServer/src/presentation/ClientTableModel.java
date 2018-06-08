package presentation;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.SortedSet;

import javax.swing.table.AbstractTableModel;

public class ClientTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = -9198732392880962681L;
	private SortedSet<InetAddress> clients;
	private String[] columnNames = {"Client"};
	
	public ClientTableModel(SortedSet<InetAddress> clients)
	{
		super();
		this.clients = clients;
	}
	
	@Override
	public int getRowCount()
	{
		return clients.size();
	}

	@Override
	public int getColumnCount()
	{
		return 1;
	}
	
	@Override
	public String getColumnName(int col) 
	{
        return columnNames[col];
    }

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Iterator<InetAddress> it = clients.iterator();
		InetAddress res = null;
		
		for(int i = 0; i <= rowIndex; ++i) res = it.next();
		
		return res;
	}
	
	@Override
	public Class getColumnClass(int c) 
	{
		return c == 0 ? InetAddress.class: Object.class;
    }
	
	
}
