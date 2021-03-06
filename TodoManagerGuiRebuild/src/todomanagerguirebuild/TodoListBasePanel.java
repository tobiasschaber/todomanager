/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package todomanagerguirebuild;

/**
 *
 * @author SCHABT
 */
public class TodoListBasePanel extends javax.swing.JPanel {

    /**
     * Creates new form TodoListBasePanel
     */
    public TodoListBasePanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labTodoItem = new javax.swing.JLabel();
        scrollPaneTodoHistoryList = new javax.swing.JScrollPane();
        todoHistoryList = new javax.swing.JList();
        labDescription = new javax.swing.JLabel();
        txtFieldDescription = new javax.swing.JTextField();
        labStatus = new javax.swing.JLabel();
        comboBoxStatus = new javax.swing.JComboBox();
        labText = new javax.swing.JLabel();
        scrollpaneText = new javax.swing.JScrollPane();
        textAreaText = new javax.swing.JTextArea();
        labOpenTextEditor = new javax.swing.JLabel();
        labPriority = new javax.swing.JLabel();
        sliderPriority = new javax.swing.JSlider();
        panelRight = new javax.swing.JPanel();
        chkBoxReminderActive = new javax.swing.JCheckBox();
        labCategory = new javax.swing.JLabel();
        labDatum = new javax.swing.JLabel();
        buttonSave = new javax.swing.JButton();
        txtFieldDateEnd = new javax.swing.JTextField();
        labTimePlan = new javax.swing.JLabel();
        comboBoxCategory = new javax.swing.JComboBox();
        txtFieldTime = new javax.swing.JTextField();
        labReminder = new javax.swing.JLabel();
        labTimePlanEnd = new javax.swing.JLabel();
        labUhrzeit = new javax.swing.JLabel();
        scrollpaneAttachmentList = new javax.swing.JScrollPane();
        listAttachments = new javax.swing.JList();
        labAttachments = new javax.swing.JLabel();
        txtFieldDate = new javax.swing.JTextField();
        txtFieldDateStart = new javax.swing.JTextField();
        labOpenAttachmentDir = new javax.swing.JLabel();
        labTimePlanStart = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        labTodoItem.setText("Todo-Item");

        todoHistoryList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        scrollPaneTodoHistoryList.setViewportView(todoHistoryList);

        labDescription.setText("Beschreibung");

        labStatus.setText("Status");

        comboBoxStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        labText.setText("Text");

        textAreaText.setColumns(20);
        textAreaText.setRows(5);
        scrollpaneText.setViewportView(textAreaText);

        labOpenTextEditor.setText("X");

        labPriority.setText("Priorität");

        chkBoxReminderActive.setText("Erinnere mich");

        labCategory.setText("Kategorie");

        labDatum.setText("Datum");

        buttonSave.setText("Speichern");

        txtFieldDateEnd.setText("jTextField2");

        labTimePlan.setText("Zeitplan");

        comboBoxCategory.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtFieldTime.setText("jTextField1");

        labReminder.setText("Erinnerung");

        labTimePlanEnd.setText("Ende");

        labUhrzeit.setText("Uhrzeit");

        listAttachments.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        scrollpaneAttachmentList.setViewportView(listAttachments);

        labAttachments.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labAttachments.setText("Attachments");

        txtFieldDate.setText("jTextField1");

        txtFieldDateStart.setText("jTextField1");

        labOpenAttachmentDir.setText("x");

        labTimePlanStart.setText("Start");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout panelRightLayout = new javax.swing.GroupLayout(panelRight);
        panelRight.setLayout(panelRightLayout);
        panelRightLayout.setHorizontalGroup(
            panelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRightLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labReminder, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labTimePlan, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelRightLayout.createSequentialGroup()
                        .addComponent(labUhrzeit, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkBoxReminderActive, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFieldTime, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(buttonSave)
                    .addGroup(panelRightLayout.createSequentialGroup()
                        .addComponent(labCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBoxCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelRightLayout.createSequentialGroup()
                            .addComponent(labDatum, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtFieldDate))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelRightLayout.createSequentialGroup()
                            .addGroup(panelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(labTimePlanEnd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(labTimePlanStart, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panelRightLayout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addComponent(txtFieldDateEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(panelRightLayout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addComponent(txtFieldDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(18, 18, 18)
                .addGroup(panelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollpaneAttachmentList, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addGroup(panelRightLayout.createSequentialGroup()
                        .addComponent(labAttachments, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labOpenAttachmentDir, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelRightLayout.setVerticalGroup(
            panelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelRightLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jSeparator1))
                    .addGroup(panelRightLayout.createSequentialGroup()
                        .addGroup(panelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labReminder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labAttachments)
                            .addComponent(labOpenAttachmentDir))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelRightLayout.createSequentialGroup()
                                .addGroup(panelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(labDatum)
                                    .addComponent(txtFieldDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(labUhrzeit)
                                    .addComponent(txtFieldTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chkBoxReminderActive)
                                .addGap(18, 18, 18)
                                .addComponent(labTimePlan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(labTimePlanStart)
                                    .addComponent(txtFieldDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(11, 11, 11)
                                .addGroup(panelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtFieldDateEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labTimePlanEnd))
                                .addGap(22, 22, 22)
                                .addGroup(panelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(labCategory)
                                    .addComponent(comboBoxCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(buttonSave))
                            .addComponent(scrollpaneAttachmentList))))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labText, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labOpenTextEditor, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollpaneText))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFieldDescription))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labPriority)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sliderPriority, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(labTodoItem)
                    .addComponent(scrollPaneTodoHistoryList))
                .addGap(8, 8, 8)
                .addComponent(panelRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labTodoItem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPaneTodoHistoryList, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labDescription)
                    .addComponent(txtFieldDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(labStatus)
                        .addComponent(comboBoxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labPriority))
                    .addComponent(sliderPriority, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollpaneText)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labText)
                        .addGap(68, 68, 68)
                        .addComponent(labOpenTextEditor)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(panelRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonSave;
    private javax.swing.JCheckBox chkBoxReminderActive;
    private javax.swing.JComboBox comboBoxCategory;
    private javax.swing.JComboBox comboBoxStatus;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel labAttachments;
    private javax.swing.JLabel labCategory;
    private javax.swing.JLabel labDatum;
    private javax.swing.JLabel labDescription;
    private javax.swing.JLabel labOpenAttachmentDir;
    private javax.swing.JLabel labOpenTextEditor;
    private javax.swing.JLabel labPriority;
    private javax.swing.JLabel labReminder;
    private javax.swing.JLabel labStatus;
    private javax.swing.JLabel labText;
    private javax.swing.JLabel labTimePlan;
    private javax.swing.JLabel labTimePlanEnd;
    private javax.swing.JLabel labTimePlanStart;
    private javax.swing.JLabel labTodoItem;
    private javax.swing.JLabel labUhrzeit;
    private javax.swing.JList listAttachments;
    private javax.swing.JPanel panelRight;
    private javax.swing.JScrollPane scrollPaneTodoHistoryList;
    private javax.swing.JScrollPane scrollpaneAttachmentList;
    private javax.swing.JScrollPane scrollpaneText;
    private javax.swing.JSlider sliderPriority;
    private javax.swing.JTextArea textAreaText;
    private javax.swing.JList todoHistoryList;
    private javax.swing.JTextField txtFieldDate;
    private javax.swing.JTextField txtFieldDateEnd;
    private javax.swing.JTextField txtFieldDateStart;
    private javax.swing.JTextField txtFieldDescription;
    private javax.swing.JTextField txtFieldTime;
    // End of variables declaration//GEN-END:variables
}
