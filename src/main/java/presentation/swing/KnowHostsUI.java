/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.ResourceBundle;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import domain.KnownHosts;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import util.Strings;

/**
 *
 * @author Pedro Fernandes
 */
public class KnowHostsUI extends JDialog{
    
    private JButton addHost;
    private JButton rmHost;
    private JList lstHosts;
    
    private Set<String> knowsHosts;
    
    private final ResourceBundle resourceBundle;
    
    private static final String HOSTS = ResourceBundle.getBundle("language.MessagesBundle").getString("known_host_conf");
    
    private static final int WIDTHW = 300, LENGTH = 300;
   
    public KnowHostsUI(JFrame frame) throws FileNotFoundException{
        
        super(frame, true);
        
        resourceBundle = ResourceBundle.getBundle("language.MessagesBundle");
        setTitle(resourceBundle.getString("hosts"));
        
        add(createComponents());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        pack();
        setResizable(true);
        setMinimumSize(new Dimension(WIDTHW, LENGTH));
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private JPanel createComponents() throws FileNotFoundException {
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(createPanelCenter(), BorderLayout.CENTER);

        return panel;
    }
    
    private JPanel createPanelCenter() throws FileNotFoundException {


        final int ROW = 1, COLUM = 3;
        final int HGAP = 20, VGAP = 0;
        JPanel p = new JPanel(new GridLayout( ROW,COLUM, HGAP, VGAP));
        
        knowsHosts = KnownHosts.getKnownHosts();
        
        lstHosts = new JList(knowsHosts.toArray());
        
        p.add(createPanelLists( HOSTS,
                                lstHosts,
                                createButtonAddHost(),
                                createButtonRemoveHost()));

        return p;
    }
    
    private JPanel createPanelLists(
            String title,
            JList lst,
            JButton btnUp,
            JButton btnDown)
    {
        lst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrPane = new JScrollPane(lst);

        JPanel p = new JPanel(new BorderLayout());
        
        final int UP = 5, DOWN = 5;
        final int LEFT = 5, RIGHT = 5;
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(title),
                new EmptyBorder(UP, LEFT,
                DOWN, RIGHT)));
        
        p.add(scrPane, BorderLayout.CENTER);

        JPanel pBotoes = createPanelButtonLists(btnUp,btnDown);
        p.add(pBotoes, BorderLayout.SOUTH);
        return p;
    }
    
    private JPanel createPanelButtonLists(JButton btn1, JButton btn2) {

        final int ROW = 2, COLUM = 1;
        final int HGAP = 0, VGAP = 10;        
        JPanel p = new JPanel(new GridLayout( ROW, COLUM,  HGAP, VGAP));
        
        final int UP = 10, DOWN = 0;
        final int LEFT = 0, RIGHT = 0;
        p.setBorder(BorderFactory.createEmptyBorder( UP, LEFT, DOWN, RIGHT));
        
        p.add(btn1);
        p.add(btn2);
        
        return p;
    }
    
    private JButton createButtonAddHost(){
        addHost = new JButton(resourceBundle.getString("add"));
        addHost.setMnemonic(KeyEvent.VK_A);
        addHost.setToolTipText(resourceBundle.getString("add_known_host"));
        addHost.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DialogNewHost(KnowHostsUI.this, HOSTS, knowsHosts);
                lstHosts.setListData(knowsHosts.toArray());
                try {
                    save();
                } catch (IOException ex) {
                    Logger.getLogger(KnowHostsUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (knowsHosts.isEmpty()) {
                    rmHost.setEnabled(false);
                } else {
                    rmHost.setEnabled(true);
                }
            }
        });

        return addHost;
    }
    
    private JButton createButtonRemoveHost(){
        rmHost = new JButton(resourceBundle.getString("remove"));
        rmHost.setMnemonic(KeyEvent.VK_R);
        rmHost.setToolTipText(resourceBundle.getString("remove_known_host"));
        if (knowsHosts.isEmpty()) {
            rmHost.setEnabled(false);
        } else {
            rmHost.setEnabled(true);
        }
        rmHost.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] options = knowsHosts.toArray();
                String newString = (String) JOptionPane.showInputDialog(
                        KnowHostsUI.this,
                        resourceBundle.getString("choose_an_item"), resourceBundle.getString("known_host_conf"),
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[0]);
                if (!Strings.isNullOrEmptyOrWhiteSpace(newString)) {
                    String[] options2 = {resourceBundle.getString("yes"), resourceBundle.getString("no")};
                    int answer = JOptionPane.showOptionDialog(
                            KnowHostsUI.this,
                            resourceBundle.getString("remove") + "\n" + newString,
                            resourceBundle.getString("remove_known_host"),
                            0,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options2,
                            options2[1]);
                    final int YES = 0;
                    if (answer == YES) {
                        knowsHosts.remove(newString);
                        lstHosts.setListData(knowsHosts.toArray());
                        try {
                            save();
                        } catch (IOException ex) {
                            Logger.getLogger(KnowHostsUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (knowsHosts.isEmpty()) {
                            rmHost.setEnabled(false);
                        }
                    }
                }
            }
        });

        return rmHost;
    }
    
    private void save() throws IOException{
        try {
            KnownHosts.saveKnownHostsFile(knowsHosts);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(KnowHostsUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
