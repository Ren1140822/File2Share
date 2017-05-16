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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Represents the main menu of the application.
 *
 * @author Ivo Ferro
 * @author Pedro Fernandes
 * @author Renato Olveira
 */
public class F2ShareMenu extends JFrame implements Observer {

    /**
     * The resource bundle.
     */
    private final ResourceBundle resourceBundle;

    private static final int WIDTH = 500, LENGTH = 500;
    private static final String REMOTE_FILES = ResourceBundle.getBundle("language.MessagesBundle").getString("remote_files");
    private static final String LOCAL_FILES = ResourceBundle.getBundle("language.MessagesBundle").getString("local_files");
    private static final String DOWNLOADED_FILES = ResourceBundle.getBundle("language.MessagesBundle").getString("downloaded_files");
    private StartConfiguration configuration;
    private JPanel panelShared;
    private JPanel panelDownload;
    private JPanel downloadedPanel;
    private JList listShared;
    private JList listDownload;
    private JList downloadedList;
    private JButton buttonDownload;

    private Set<RemoteFile> remoteFiles;
    private Set<DataFile> dataFiles;
    private Set<DataFile> downloadedFiles;
    private TcpServer server;

    public F2ShareMenu() {
        resourceBundle = ResourceBundle.getBundle("language.MessagesBundle");
        setTitle(resourceBundle.getString("window_title"));

        try {
            configuration = new StartConfiguration();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, resourceBundle.getString("error_reading_configuration_file"));
            e.printStackTrace();
        }

        // default instantiation
        remoteFiles = new TreeSet<>();
        dataFiles = new TreeSet<>();
        downloadedFiles = new TreeSet<>();

        try {
            dataFiles = new TreeSet(DataFileRepository.getSharedFiles());
        } catch (IOException e) {
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

        tabbedPane.add(REMOTE_FILES, createDownloadPanel());
        tabbedPane.add(LOCAL_FILES, createSharedPanel());
        tabbedPane.add(DOWNLOADED_FILES, createDownloadedPanel());

        return tabbedPane;
    }

    private JPanel createDownloadPanel() {
        panelDownload = new JPanel(new BorderLayout());

        RemoteFileListModel remoteFileListModel = new RemoteFileListModel(remoteFiles);
        listDownload = new JList(remoteFileListModel);
        listDownload.setCellRenderer(new RemoteFileListCellRenderer());
        listDownload.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panelDownload.add(createPanelList(listDownload), BorderLayout.CENTER);
        buttonDownload = new JButton(resourceBundle.getString("download"));
        buttonDownload.setEnabled(false);

        listDownload.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (listDownload.isSelectionEmpty()) {
                    buttonDownload.setEnabled(false);
                } else {
                    buttonDownload.setEnabled(true);
                }
            }
        });

        buttonDownload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommunicationController ctrl = new CommunicationController(((RemoteFile) listDownload.getSelectedValue()).getAddress(), server, ((RemoteFile) listDownload.getSelectedValue()).getTcpPort());
                ctrl.sendFileRequest(((RemoteFile) listDownload.getSelectedValue()).getName());
                try {
                    String fileName;
                    fileName = JOptionPane.showInputDialog(F2ShareMenu.this, resourceBundle.getString("save_as"), ((RemoteFile) listDownload.getSelectedValue()).getName());
                    if (findFile(fileName)) {
                        if (JOptionPane.showConfirmDialog(F2ShareMenu.this, resourceBundle.getString("file_exists_overwrite_confirmation"), resourceBundle.getString("file_found"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            if (fileName != null) {
                                final JDialog dialog = new JDialog(F2ShareMenu.this);
                                Execute(dialog);
                                SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
                                    @Override
                                    protected Void doInBackground() throws Exception {

                                        ctrl.downloadDataFile(fileName, Configuration.getDownloadFolderName());
                                        return null;
                                    }

                                };
                                mySwingWorker.addPropertyChangeListener(new PropertyChangeListener() {
                                    @Override
                                    public void propertyChange(PropertyChangeEvent evt) {
                                        if (evt.getPropertyName().equals("state")) {
                                            if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
                                                dialog.dispose();
                                                JOptionPane.showMessageDialog(F2ShareMenu.this, resourceBundle.getString("download_successfully"), resourceBundle.getString("download"), JOptionPane.DEFAULT_OPTION);
                                            }
                                        }
                                    }
                                });
                                mySwingWorker.execute();
                                dialog.setVisible(true);
                                refreshDownloadedFiles();
                                try {
                                    mySwingWorker.get();
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    } else if (fileName != null) {
                        final JDialog dialog = new JDialog(F2ShareMenu.this);
                        Execute(dialog);
                        SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>() {
                            @Override
                            protected Void doInBackground() throws Exception {

                                ctrl.downloadDataFile(fileName, Configuration.getDownloadFolderName());

                                return null;
                            }

                        };
                        mySwingWorker.addPropertyChangeListener(new PropertyChangeListener() {
                            @Override
                            public void propertyChange(PropertyChangeEvent evt) {
                                if (evt.getPropertyName().equals("state")) {
                                    if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
                                        dialog.dispose();
                                        JOptionPane.showMessageDialog(F2ShareMenu.this, resourceBundle.getString("download_successfully"), resourceBundle.getString("download"), JOptionPane.DEFAULT_OPTION);
                                    }
                                }
                            }
                        });
                        mySwingWorker.execute();
                        dialog.setVisible(true);
                        try {
                            mySwingWorker.get();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                    refreshDownloadedFiles();
                } catch (Exception ex) {
                    Logger.getLogger(F2ShareMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        panelDownload.add(buttonDownload, BorderLayout.SOUTH);
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

    private JPanel createDownloadedPanel() {
        downloadedPanel = new JPanel(new BorderLayout());

        downloadedList = new JList();
        downloadedList.setCellRenderer(new DataFileListCellRenderer());
        refreshDownloadedFiles();

        downloadedPanel.add(createPanelList(downloadedList), BorderLayout.CENTER);
        JButton button = new JButton("Open file");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {

                if (downloadedList.getSelectedValue() != null) {
                    String fileName = ((DataFile) downloadedList.getSelectedValue()).name();
                    try {
                        File file = new File(Configuration.getDownloadFolderName() + "/" + fileName);
                        Desktop.getDesktop().open(file);
                    } catch (IOException ex) {
                        Logger.getLogger(F2ShareMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        });
        downloadedList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (downloadedList.isSelectionEmpty()) {
                    button.setEnabled(false);
                } else {
                    button.setEnabled(true);
                }
            }
        });
        downloadedPanel.add(button, BorderLayout.SOUTH);
        return downloadedPanel;
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
        JMenu menu = new JMenu(resourceBundle.getString("file"));
        menu.setMnemonic(KeyEvent.VK_F);

        menu.add(createItemConfigurations());
        menu.addSeparator();
        menu.add(createItemExit());

        return menu;

    }

    private JMenu createOptionsMenu() {
        JMenu menu = new JMenu(resourceBundle.getString("options"));
        menu.setMnemonic(KeyEvent.VK_O);

        menu.add(createSubMenuStyle());
        menu.addSeparator();
        menu.add(createItemAbout());

        return menu;

    }

    private JMenu createSubMenuStyle() {
        JMenu menu = new JMenu(resourceBundle.getString("style"));
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
                            resourceBundle.getString("style") + menuItem.getActionCommand(),
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return item;
    }

    private JMenuItem createItemConfigurations() {
        JMenuItem item = new JMenuItem(resourceBundle.getString("configurations"), KeyEvent.VK_C);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_MASK));
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    configurations();

                } catch (IOException ex) {
                    Logger.getLogger(F2ShareMenu.class
                            .getName()).log(Level.SEVERE, null, ex);
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
        JMenuItem item = new JMenuItem(resourceBundle.getString("exit"), KeyEvent.VK_E);
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
        JMenuItem item = new JMenuItem(resourceBundle.getString("about"), KeyEvent.VK_A);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(F2ShareMenu.this,
                        resourceBundle.getString("about_text"),
                        resourceBundle.getString("about"),
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        return item;
    }

    private void configurations() throws IOException {
        final ChangeConfigurationsUI c = new ChangeConfigurationsUI(F2ShareMenu.this);
    }

    private void exitAPP() {
        String[] op = {resourceBundle.getString("yes"), resourceBundle.getString("no")};
        String question = resourceBundle.getString("close_application_question");
        int opcao = JOptionPane.showOptionDialog(F2ShareMenu.this, question,
                this.getTitle(), JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, op, op[0]);
        if (opcao == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
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
        RemoteFile remoteFile = null;
        if (listDownload.getSelectedValue() != null) {

            remoteFile = ((RemoteFile) listDownload.getSelectedValue());
        }
        listDownload.setModel(remoteFileListModel);
        for (int i = 0; i < listDownload.getModel().getSize(); i++) {
            if (((RemoteFile) listDownload.getModel().getElementAt(i)).equals(remoteFile)) {
                listDownload.setSelectedIndex(i);
            }
        }

    }

    public void Execute(JDialog dialog) {

        ImageIcon gif = new ImageIcon("src/main/resources/loading.gif");
        JLabel labelGif = new JLabel(resourceBundle.getString("downloading"));
        labelGif.setIcon(gif);
        dialog.setUndecorated(true);
        dialog.add(labelGif);
        dialog.pack();
        dialog.setLocationRelativeTo(F2ShareMenu.this);
        dialog.setAlwaysOnTop(true);
        dialog.setModal(true);

    }

    public boolean findFile(String fileName) {
        File folder = new File(Configuration.getDownloadFolderName());
        File[] files = folder.listFiles();
        for (File file : files) {
            boolean isEqual = file.getName().equals(fileName);
            if (isEqual) {
                return true;
            }
        }
        return false;
    }

    public void refreshDownloadedFiles() {
        try {
            downloadedFiles = new TreeSet(DataFileRepository.getDownloadedFiles());
        } catch (IOException e) {
            e.printStackTrace();
        }

        DataFileListModel dataFileListModel = new DataFileListModel(downloadedFiles);
        downloadedList.setModel(dataFileListModel);
    }
}
