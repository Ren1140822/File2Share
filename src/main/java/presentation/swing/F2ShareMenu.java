/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation.swing;

import application.CommunicationController;
import application.StartConfiguration;
import application.TcpServer;
import connection.AnnounceTimerTask;
import connection.UdpReceiverThread;
import domain.Configuration;
import domain.DataFile;
import domain.RemoteFile;
import persistence.DataFileRepository;
import presentation.swing.components.DataFileListCellRenderer;
import presentation.swing.components.DataFileListModel;
import presentation.swing.components.RemoteFileListCellRenderer;
import presentation.swing.components.RemoteFileListModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents the main menu of the application.
 *
 * @author Ivo Ferro
 * @author Pedro Fernandes
 * @author Renato Olveira
 */
public class F2ShareMenu extends JFrame implements Observer {

    private static final int WIDTH = 500, LENGTH = 500;
    private static final String DOWNLOAD = "Download";
    private static final String SHARE = "Share";
    private StartConfiguration configuration;
    private JPanel panelShared;
    private JPanel panelDownload;
    private JList listShared;
    private JList listDownload;

    private Set<RemoteFile> remoteFiles;
    private Set<DataFile> dataFiles;
    private TcpServer server;

    public F2ShareMenu() {
        super("F2Share");

        try {
            configuration = new StartConfiguration();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "There was an error reading configuration file");
            e.printStackTrace();
        }

        // TODO fill remoteFiles and dataFiles
        remoteFiles = new TreeSet<>();

        // TODO DataFileRepository.getSharedFiles() => ERROR  nullpointer
        
        try { 
            dataFiles = new TreeSet(DataFileRepository.getSharedFiles()); }
        catch (IOException e) { 
            JOptionPane.showMessageDialog(this, 
                    "There was an error reading local files"); 
            e.printStackTrace();
        }
        
        

        createComponents();

        server = new TcpServer();
        server.start();

        UdpReceiverThread udpReceiverThread = new UdpReceiverThread(remoteFiles);
        udpReceiverThread.addObserver(this);
        udpReceiverThread.start();

        int secondsToAnnounce = Configuration.getUDPTimeAnnouncement();
        AnnounceTimerTask announceTimerTask = new AnnounceTimerTask(dataFiles, server.currentServerPort());
        announceTimerTask.addObserver(this);
        java.util.Timer timer = new java.util.Timer();
        timer.schedule(announceTimerTask, 0, secondsToAnnounce * 1000);

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

    private JTabbedPane createTabPanels() {
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.add(DOWNLOAD, createDownloadPanel());
        tabbedPane.add(SHARE, createSharedPanel());

        return tabbedPane;
    }

    private JPanel createDownloadPanel() {
        panelDownload = new JPanel(new BorderLayout());

        RemoteFileListModel remoteFileListModel = new RemoteFileListModel(remoteFiles);
        listDownload = new JList(remoteFileListModel);
        listDownload.setCellRenderer(new RemoteFileListCellRenderer());

        panelDownload.add(createPanelList(listDownload), BorderLayout.CENTER);
        JButton button = new JButton("DOWNLOAD");
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                CommunicationController ctrl = new CommunicationController(((RemoteFile) listDownload.getSelectedValue()).getAddress(), server, ((RemoteFile) listDownload.getSelectedValue()).getTcpPort());
                ctrl.sendFileRequest(((RemoteFile) listDownload.getSelectedValue()).getName());
                try {
                    JFileChooser chooser = new JFileChooser(Configuration.getDownloadFolderName());
                    if (chooser.showSaveDialog(F2ShareMenu.this) == JFileChooser.APPROVE_OPTION) {
                        ctrl.downloadDataFile(chooser.getSelectedFile().getName(),Configuration.getDownloadFolderName());
                    }
                } catch (IOException ex) {
                    Logger.getLogger(F2ShareMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void mousePressed(MouseEvent me) {

            }

            @Override
            public void mouseReleased(MouseEvent me) {

            }

            @Override
            public void mouseEntered(MouseEvent me) {

            }

            @Override
            public void mouseExited(MouseEvent me) {

            }

        });
        panelDownload.add(button, BorderLayout.SOUTH);
        return panelDownload;
    }

    private JPanel createSharedPanel() {
        panelShared = new JPanel(new BorderLayout());

        DataFileListModel dataFileListModel = new DataFileListModel(dataFiles);
        listShared = new JList(dataFileListModel);
        listShared.setCellRenderer(new DataFileListCellRenderer());

        panelShared.add(createPanelList(listShared), BorderLayout.CENTER);

        return panelShared;
    }

    private JScrollPane createPanelList(JList jlist) {

        jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrPane = new JScrollPane(jlist);

        int aux = 20;
        scrPane.setBorder(BorderFactory.createEmptyBorder(aux, aux, aux, aux));

        return scrPane;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        menuBar.add(createFileMenu());
        menuBar.add(createOptionsMenu());

        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);

        menu.add(createItemConfigurations());
        menu.addSeparator();
        menu.add(createItemExit());

        return menu;

    }

    private JMenu createOptionsMenu() {
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
                try {
                    configurations();
                } catch (IOException ex) {
                    Logger.getLogger(F2ShareMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        return item;
    }

    /**
     * cria MenuItem Sair
     *
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
                        + "\nRCOMP - 2DD - 2016/2017\n",
                        "About",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        return item;
    }

    private void configurations() throws IOException {
        final ChangeConfigurationsUI c = new ChangeConfigurationsUI(F2ShareMenu.this);
    }

    private void exitAPP() {
        String[] op = {"Yes", "No"};
        String question = "Close aplication?";
        int opcao = JOptionPane.showOptionDialog(F2ShareMenu.this, question,
                this.getTitle(), JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, op, op[0]);
        if (opcao == JOptionPane.YES_OPTION) {
            dispose();
        } else {
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        }
    }

    @Override
    public void update(Observable observable, Object o) {

        if (observable instanceof RemoteFile) {
            RemoteFile remoteFile = (RemoteFile) observable;
            remoteFiles.remove(remoteFile);
            remoteFile.cancelTimer();
        }

        DataFileListModel dataFileListModel = new DataFileListModel(dataFiles);
        listShared.setModel(dataFileListModel);
        RemoteFileListModel remoteFileListModel = new RemoteFileListModel(remoteFiles);
        listDownload.setModel(remoteFileListModel);
    }
}
