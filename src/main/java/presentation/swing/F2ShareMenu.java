/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation.swing;

import application.StartConfiguration;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Pedro Fernandes
 */
public class F2ShareMenu extends JFrame{
    
    private StartConfiguration configuration;
    private JPanel panelShared;
    private JPanel panelDownload;
    private JList listShared;
    private JList listDownload;    
    
    private static final int WIDTH = 500, LENGTH = 500;
    private static final String DOWNLOAD = "Download";
    private static final String SHARE = "Share";
    
    public F2ShareMenu() throws IOException{
        super("F2Share");
        
        configuration = new StartConfiguration();

        createComponents();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(WIDTH, LENGTH));
        pack();
        setLocationRelativeTo(null);      
        setVisible(true);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitAPP();
            }
        });
    }
    
    private void createComponents() {
        
        createMenuBar();

        add(createTabPanels(), BorderLayout.CENTER);

    }
    
    private JTabbedPane createTabPanels(){
        JTabbedPane tabbedPane = new JTabbedPane();
        
        tabbedPane.add(DOWNLOAD, createDownloadPanel());
        tabbedPane.add(SHARE, createSharedPanel());
        
        return tabbedPane;
    }
    
    private JPanel createDownloadPanel(){
        panelDownload = new JPanel(new BorderLayout());
        
        listDownload = new JList(new ArrayList().toArray());
        
        panelDownload.add(createPanelList(listDownload));
        
        return panelDownload;
    }
    
    private JPanel createSharedPanel(){
        panelShared = new JPanel(new BorderLayout());
        
        
        listShared = new JList(new ArrayList().toArray());
        
        panelShared.add(createPanelList(listShared), BorderLayout.CENTER);
        
        return panelShared;
    }
    
    private JScrollPane createPanelList(JList jlist) {

        jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrPane = new JScrollPane(jlist);
        
        int aux = 20;
        scrPane.setBorder(BorderFactory.createEmptyBorder( aux, aux, aux, aux));

        return scrPane;
    }
    
    private JMenuBar createMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        menuBar.add(createFileMenu());
        menuBar.add(createOptionsMenu());
        
        return menuBar;
    }
    
    private JMenu createFileMenu(){
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        
        menu.add(createItemConfigurations());
        menu.addSeparator();
        menu.add(createItemExit());
        
        return menu;
        
    }
    
    private JMenu createOptionsMenu(){
        JMenu menu = new JMenu("Options");
        menu.setMnemonic(KeyEvent.VK_O);
        
        menu.add(createSubMenuStyle());
        menu.addSeparator();
        menu.add(createItemAbout());
        
        return menu;
        
    }
    
    private JMenu createSubMenuStyle() {
        JMenu menu = new JMenu("Style");
        menu.setMnemonic(KeyEvent.VK_S);

        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            menu.add(createItemStyle(info));
        }

        return menu;
    }
    
    private JMenuItem createItemStyle(UIManager.LookAndFeelInfo info) {
        JMenuItem item = new JMenuItem(info.getName());

        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem menuItem = (JMenuItem) e.getSource();
                try {
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if (menuItem.getActionCommand().equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                    SwingUtilities.updateComponentTreeUI(F2ShareMenu.this);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(F2ShareMenu.this,
                            ex.getMessage(),
                            "Estilo " + menuItem.getActionCommand(),
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return item;
    }
    
    private JMenuItem createItemConfigurations() {
        JMenuItem item = new JMenuItem("Configurations", KeyEvent.VK_C);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_MASK));
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                configurations();
            }
        });
        return item;
    }
    
    /**
     * cria MenuItem Sair
     * @return MenuItem Sair
     */
    private JMenuItem createItemExit() {
        JMenuItem item = new JMenuItem("Exit", KeyEvent.VK_E);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.ALT_MASK));
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitAPP();
            }
        });
        return item;
    }
    
    private JMenuItem createItemAbout() {
        JMenuItem item = new JMenuItem("About", KeyEvent.VK_A);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(F2ShareMenu.this,                   
                          "F2Share\n\n"
                        + "1060503 - Pedro Fernandes\n"
                        + "1140822 - Renato Oliveira\n" 
                        + "1151159 - Ivo Ferro\n"                        
                        +  "\nRCOMP - 2DD - 2016/2017\n",                            
                    "About",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        return item;
    }
    
    private void configurations(){
        
    }
    
    private void exitAPP(){
        dispose();
    }
    
}
