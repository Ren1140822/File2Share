/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import util.Strings;

/**
 *
 * @author Pedro Fernandes
 */
public class DialogNewString extends JDialog{

    /**
     * The resource bundle.
     */
    private final ResourceBundle resourceBundle;
    
    private JTextField txt;
    private ArrayList lst;
    private String title;
    private JDialog dialog;
    
    private static final Dimension LABEL_TAMANHO = new JLabel("AAAAAAAAA").
                                                        getPreferredSize();

    public DialogNewString(JDialog dialog, String str, ArrayList lst){
        
        super(dialog, str, true);

        resourceBundle = ResourceBundle.getBundle("language.MessagesBundle");

        this.dialog = dialog;
        this.title = str;
        this.lst = lst;
        
        createComponents();
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pack();
        setResizable(false);
        setLocationRelativeTo(dialog);
        setVisible(true);
        
    }
    
    private void createComponents(){
        JPanel p1 = createPanelName();
        JPanel p2 = criarPainelBotoes();

        add(p1, BorderLayout.NORTH);
        add(p2, BorderLayout.SOUTH);
    }
    
    private JPanel createPanelName() {
        JLabel lbl = new JLabel(resourceBundle.getString("insert"), JLabel.RIGHT);
        lbl.setPreferredSize(LABEL_TAMANHO);

        txt = new JTextField(10);
        txt.requestFocusInWindow();

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final int UP = 10, DOWN = 0;
        final int LEFT = 10, RIGHT = 0;
        p.setBorder(new EmptyBorder(UP, LEFT,
                DOWN, RIGHT));

        p.add(lbl);
        p.add(txt);

        return p;
    }
    
    private JPanel criarPainelBotoes() {
        JButton btnOK = createButtonOK();
        getRootPane().setDefaultButton(btnOK);

        JButton btnCancelar = createButtonCancel();

        JPanel p = new JPanel();
        final int UP = 0, DOWN = 10;
        final int LEFT = 10, RIGHT = 10;
        p.setBorder(new EmptyBorder(UP, LEFT,
                DOWN, RIGHT));
        p.add(btnOK);
        p.add(btnCancelar);

        return p;
    }
    
    private JButton createButtonOK() {
        JButton btn = new JButton(resourceBundle.getString("ok"));
        btn.setMnemonic(KeyEvent.VK_O);
        btn.setToolTipText(resourceBundle.getString("insert_confirmation"));
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = txt.getText();
                if(!Strings.isNullOrEmptyOrWhiteSpace(s)){
                    boolean check = lst.add(s);
                    if (check) {   
                            JOptionPane.showMessageDialog(
                            DialogNewString.this,
                                    resourceBundle.getString("insert_completed"),
                            title,
                            JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(
                                    DialogNewString.this,
                                    resourceBundle.getString("item_already_exists"),
                                    title,
                                    JOptionPane.ERROR_MESSAGE);
                        }
                } else {
                    JOptionPane.showMessageDialog(
                                    DialogNewString.this,
                                    resourceBundle.getString("invalid_data"),
                                    title,
                                    JOptionPane.ERROR_MESSAGE);
                }

                    
                dispose();
            }
        });
        return btn;
    }
    
    private JButton createButtonCancel() {
        JButton btn = new JButton(resourceBundle.getString("cancel"));
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        return btn;
    }  
    
}
