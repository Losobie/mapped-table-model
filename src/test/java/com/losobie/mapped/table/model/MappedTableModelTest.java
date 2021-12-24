package com.losobie.mapped.table.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MappedTableModelTest {

    private MappedTableModel model;
    private TableModelListener listener;

    @Before
    public void setUp() {
        model = MappedTableModel.builder()
                                .columnNames( "One", "Two", "Three" )
                                .columnClasses( String.class, Object.class, Integer.class )
                                .rowDisplay( String.class, s -> "String", String::toString, Integer::parseInt )
                                .build();
        listener = mock( TableModelListener.class );
        model.addRows( "1", "2", 3, 4, 5l, 6l );
        model.addTableModelListener( listener );
    }

    @Test
    public void testBuilder() {
        MappedTableModel result = MappedTableModel.builder().build();
        Assert.assertNotNull( result );
    }

    @Test
    public void testAddRow() {
        Object rowObject = "Test";
        model.addRow( rowObject );
        TableModelEvent expected = new TableModelEvent( model,
                                                        6,
                                                        6,
                                                        TableModelEvent.ALL_COLUMNS,
                                                        TableModelEvent.INSERT
        );
        verify( listener ).tableChanged( any( TableModelEvent.class ) );
    }

    //    @Test
    //    public void testAddRows_ObjectArr() {
    //        Object[] rowObjects = {"One", "Two"};
    //        model.addRows( rowObjects );
    //    }
    //
    //    @Test
    //    public void testAddRows_Collection() {
    //        Collection<Object> rowObjects = null;
    //        model.addRows( rowObjects );
    //    }
    //
    //    @Test
    //    public void testInsertRow() {
    //        int index = 0;
    //        Object rowObject = null;
    //        model.insertRow( index, rowObject );
    //    }
    //
    //    @Test
    //    public void testInsertRows_int_ObjectArr() {
    //        int index = 0;
    //        Object[] rowObjects = null;
    //        model.insertRows( index, rowObjects );
    //    }
    //
    //    /**
    //     * Test of insertRows method, of class MappedTableModel.
    //     */
    //    @Test
    //    public void testInsertRows_int_Collection() {
    //        int index = 0;
    //        Collection<Object> rowObjects = null;
    //        model.insertRows( index, rowObjects );
    //    }
    //
    //    @Test
    //    public void testUpdateRow() {
    //        int index = 0;
    //        Object rowObject = null;
    //        model.updateRow( index, rowObject );
    //    }
    //
    //    @Test
    //    public void testRemoveRow() {
    //        int index = 0;
    //        Object expResult = null;
    //        Object result = model.removeRow( index );
    //        assertEquals( expResult, result );
    //    }
    //
    //    @Test
    //    public void testSetRowCount() {
    //        int count = 0;
    //        model.setRowCount( count );
    //    }
    //
    //    @Test
    //    public void testClear() {
    //        model.clear();
    //    }
    //
    //    @Test
    //    public void testGetRowCount() {
    //        int expResult = 0;
    //        int result = model.getRowCount();
    //        assertEquals( expResult, result );
    //    }
    //
    //    @Test
    //    public void testGetColumnCount() {
    //        int expResult = 0;
    //        int result = model.getColumnCount();
    //        assertEquals( expResult, result );
    //    }
    //
    //    @Test
    //    public void testGetValueAt() {
    //        int rowIndex = 0;
    //        int columnIndex = 0;
    //        Object expResult = null;
    //        Object result = model.getValueAt( rowIndex, columnIndex );
    //        assertEquals( expResult, result );
    //    }
    //
    //    @Test
    //    public void testGetColumnName() {
    //        int columnIndex = 0;
    //        String expResult = "";
    //        String result = model.getColumnName( columnIndex );
    //        assertEquals( expResult, result );
    //    }
    //
    //    @Test
    //    public void testGetColumnClass() {
    //        int columnIndex = 0;
    //        Class expResult = null;
    //        Class result = model.getColumnClass( columnIndex );
    //        assertEquals( expResult, result );
    //    }
    //
    //    @Test
    //    public void testIsCellEditable() {
    //        int rowIndex = 0;
    //        int columnIndex = 0;
    //        boolean expResult = false;
    //        boolean result = model.isCellEditable( rowIndex, columnIndex );
    //        assertEquals( expResult, result );
    //    }
    //
    //    @Test
    //    public void testSetValueAt() {
    //        Object aValue = null;
    //        int rowIndex = 0;
    //        int columnIndex = 0;
    //        model.setValueAt( aValue, rowIndex, columnIndex );
    //    }
}
