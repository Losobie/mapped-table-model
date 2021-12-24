package com.losobie.mapped.table.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.swing.table.AbstractTableModel;

public class MappedTableModel extends AbstractTableModel {

    public static <T> Builder builder() {
        return new Builder();
    }

    public static class Builder {

        // This map determines what will be providing to a renderer based on the row class and a function for each column
        private final Map<Class<?>, Function[]> displayMap = new HashMap<>();

        // This map determines what will be provided to an editor based on the row class and a function for each column
        private final Map<Class<?>, BiFunction[]> editMap = new HashMap<>();

        // Name for each column
        private String[] columnNames;

        // Class used by JTable to select a renderer and editor for each column
        private Class[] columnClasses;

        public <T> Builder rowDisplay(Class<T> clazz, Function<T, ?>... functions) {
            displayMap.put(clazz, functions);
            return this;
        }

        public <T> Builder rowEdit(Class<T> clazz, BiFunction<T, Object, T>... functions) {
            editMap.put(clazz, functions);
            return this;
        }

        public Builder columnNames(String... columnNames) {
            this.columnNames = columnNames;
            return this;
        }

        public Builder columnClasses(Class... columnClasses) {
            this.columnClasses = columnClasses;
            return this;
        }

        public MappedTableModel build() {
            return new MappedTableModel(displayMap, editMap, columnNames, columnClasses);
        }
    }

    private final List<Object> rowObjects = new ArrayList<>();
    private final Map<Class<?>, Function[]> displayMap;
    private final Map<Class<?>, BiFunction[]> editMap;
    private final String[] columnNames;
    private final Class[] columnClasses;
    private final int colCount;

    private MappedTableModel(
            Map<Class<?>, Function[]> displayMap,
            Map<Class<?>, BiFunction[]> editMap,
            String[] columnNames,
            Class[] columnClasses
    ) {
        this.displayMap = displayMap;
        this.editMap = editMap;
        this.columnNames = columnNames;
        this.columnClasses = columnClasses;
        Optional<Function[]> o = displayMap.values().stream().max((o1, o2) -> o1.length - o2.length);
        colCount = o.isPresent() ? o.get().length : 0;
    }

    public void addRow(Object rowObject) {
        insertRows(getRowCount(), Collections.singleton(rowObject));
    }

    public void addRows(Object... rowObjects) {
        insertRows(getRowCount(), Arrays.asList(rowObjects));
    }

    public void addRows(Collection<Object> rowObjects) {
        insertRows(getRowCount(), rowObjects);
    }

    public void insertRow(int index, Object rowObject) {
        insertRows(index, Collections.singleton(rowObject));
    }

    public void insertRows(int index, Object... rowObjects) {
        insertRows(index, Arrays.asList(rowObjects));
    }

    public void insertRows(int index, Collection<Object> rowObjects) {
        this.rowObjects.addAll(index, rowObjects);
        fireTableRowsInserted(index, index + rowObjects.size() - 1);
    }

    public void updateRow(int index, Object rowObject) {
        rowObjects.set(index, rowObject);
        fireTableRowsUpdated(index, index);
    }

    public Object removeRow(int index) {
        Object rowObject = rowObjects.remove(index);
        fireTableRowsDeleted(index, index);
        return rowObject;
    }

    public void setRowCount( int count ) {
        int rowCount = getRowCount();
        if ( count == rowCount ) {
            return;
        }
        if ( count > rowCount ) {
            Collection newRows = Collections.nCopies( count - rowCount, null );
            rowObjects.addAll( newRows );
            fireTableRowsInserted( rowCount, count - 1 );
        } else if ( count < rowCount ) {
            for ( int i = rowCount -1; i >= count; i-- ) {
                rowObjects.remove( i );
            }
            fireTableRowsDeleted( count, rowCount - 1 );
        }
    }

    public void clear() {
        int rowCount = getRowCount();
        rowObjects.clear();
        fireTableRowsDeleted(0, rowCount);
    }

    @Override
    public int getRowCount() {
        return rowObjects.size();
    }

    @Override
    public int getColumnCount() {
        return colCount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object o = rowObjects.get(rowIndex);
        if (o == null) {
            return null;
        }
        Function[] functions = displayMap.get(o.getClass());
        if (functions == null) {
            return null;
        }
        if (functions.length <= columnIndex) {
            return null;
        }
        Function function = functions[columnIndex];
        if (function == null) {
            return null;
        }
        return function.apply(o);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames.length > columnIndex ? columnNames[columnIndex] : null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnClasses.length > columnIndex ? columnClasses[columnIndex] : Object.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        Object rowObject = rowObjects.get(rowIndex);
        if (rowObject == null) {
            return false;
        }
        BiFunction[] editFunctions = editMap.get(rowObject.getClass());
        return editFunctions != null
                && editFunctions.length > columnIndex
                && editFunctions[columnIndex] != null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Object rowObject = rowObjects.get(rowIndex);
        BiFunction editFunction = editMap.get(rowObject.getClass())[columnIndex];
        Object newRowObject = editFunction.apply(rowObject, aValue);
        updateRow(rowIndex, newRowObject);
    }

}
