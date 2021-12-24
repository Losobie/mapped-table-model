package com.losobie.mapped.table.model;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;

public class MappedTableFrame extends javax.swing.JFrame {

    private static final Logger LOG = Logger.getLogger(MappedTableFrame.class.getName());

    private final MappedTableModel model;

    public MappedTableFrame() {
        // Using a builder to create a new table model
        model = MappedTableModel.builder()

                // Define names for all columns
                .columnNames("Description", "Double Value")

                // Define the class used to find a renderer for each column
                .columnClasses(String.class, String.class)

                // Define what to supply the renderer for each column when the row object is a string
                // - For the String class, first row returns the string to the renderer, second row is undefined
                .rowDisplay(String.class, (String t) -> t)

                // Define how to handle the editors result for each column when the row object is a string
                // - For the String class, first row will check if result is a string then save or revert back to original string, second row is undefined
                .rowEdit(String.class, (String t, Object u) -> u instanceof String ? (String) u : t)

                // Define what to supply the renderer for each column when the row object is an Integer
                // - For Integer class, first row is a toString of the integer, second row multiplies the integer by 2 and will be cast to a string
                .rowDisplay(Integer.class, (Integer i) -> i.toString(), (Integer i) -> i * 2)

                // Define how to handle the editors result for each column when the row object is an integer
                // - For the Integer class
                // - First row does not accept edits
                // -
                .rowEdit(Integer.class, null, (Integer t, Object u) -> u instanceof String ? Integer.parseInt((String) u) / 2 : t)
                .build();
        initComponents();
        model.addRows(1, 2, 3, "Test", "Rows", 4, 5);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(model);
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> {
            MappedTableFrame frame = new MappedTableFrame();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
