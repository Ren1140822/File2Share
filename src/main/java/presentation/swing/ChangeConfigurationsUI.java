/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation.swing;

import application.ChangeConfigurationController;
import domain.Configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.EmptyBorder;
import util.Strings;

/**
 * @author Pedro Fernandes
 */
public class ChangeConfigurationsUI extends JDialog {

    private ChangeConfigurationController controller;

    private JTextField txtUDPPort;
    private JTextField txtUDPTime;
    private JTextField txtRefreshFile;
    private JTextField txtShared;
    private JTextField txtDownload;
    
    private JList lstIgnoreFiles;
    private JList lstIgnoreFilesStartingWith;
    private JList lstIgnoreFilesWithExtension;
    
    private ArrayList<String> ignoreFiles;
    private ArrayList<String> ignoreFilesStartingWith;
    private ArrayList<String> ignoreFilesWithExtension;
    
    private JButton addIgnoreFiles;
    private JButton rmIgnoreFiles;
    private JButton addIgnoreFilesStartingWith;
    private JButton rmIgnoreFilesStartingWith;
    private JButton addIgnoreFilesWithExtension;
    private JButton rmIgnoreFilesWithExtension;
    
    private JButton saveBtn;
    private JButton editBtn;
    private JButton sharedChooser;
    private JButton downloadChooser;
    
    private JRadioButton radioBtnIgnoreFoldersTRUE, radioBtnIgnoreFoldersFALSE;
    private JRadioButton radioBtnIgnoreFilesWithoutExtensionTRUE, radioBtnIgnoreFilesWithoutExtensionFALSE;

    private JFileChooser fileChooser;
    
    private static final int WIDTHW = 500, LENGTH = 500;

    /**
     * The resource bundle.
     */
    private final ResourceBundle resourceBundle;
    
    private static final String UDP_PORT = ResourceBundle.getBundle("language.MessagesBundle").getString("udp_port_number_conf");
    private static final String UDP_TIME = ResourceBundle.getBundle("language.MessagesBundle").getString("udp_announcement_time_conf");
    private static final String REFRESH_TIME = ResourceBundle.getBundle("language.MessagesBundle").getString("remote_files_refresh_time_conf");
    private static final String SHARED_FOLDER = ResourceBundle.getBundle("language.MessagesBundle").getString("local_files_folder_conf");
    private static final String DOWNLOAD_FOLDER = ResourceBundle.getBundle("language.MessagesBundle").getString("remote_files_folder_conf");
    private static final String IGNORE_FOLDERS = ResourceBundle.getBundle("language.MessagesBundle").getString("ignore_folders_conf");
    private static final String IGNORE_FILES_WITHOUT_EXT = ResourceBundle.getBundle("language.MessagesBundle").getString("ignore_files_without_extension_conf");
    private static final String IGNORE_FILES = ResourceBundle.getBundle("language.MessagesBundle").getString("ignore_files_conf");
    private static final String IGNORE_FILES_STARTING_WITH = ResourceBundle.getBundle("language.MessagesBundle").getString("ignore_files_starting_with_conf");
    private static final String IGNORE_FILES_WITH_EXTENSION = ResourceBundle.getBundle("language.MessagesBundle").getString("ignore_files_with_extension_conf");
    private static final Dimension LABEL_SIZE = new JLabel(IGNORE_FILES_WITHOUT_EXT).
                                                        getPreferredSize(); 
    
    public ChangeConfigurationsUI(JFrame frame) throws IOException{
        
        super(frame, true);

        resourceBundle = ResourceBundle.getBundle("language.MessagesBundle");
        setTitle(resourceBundle.getString("configurations"));

        controller = new ChangeConfigurationController();

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

    private JPanel createComponents() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(createGeneralPanel(), BorderLayout.NORTH);
        panel.add(createPanelCenter(), BorderLayout.CENTER);
        panel.add(createPanelButons(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createGeneralPanel() {
        
        JPanel panel = new JPanel(new BorderLayout());

        JPanel p = new JPanel(new GridLayout(5, 5));

        p.setBorder(BorderFactory.createTitledBorder(resourceBundle.getString("configurations")));

        int aux = 5, sizePath = 20;

        txtUDPPort = new JTextField(aux);
        txtUDPPort.requestFocusInWindow();
        txtUDPPort.setText("" + controller.currentUDPPortNumber());
        txtUDPPort.setEditable(false);

        txtUDPTime = new JTextField(aux);
        txtUDPTime.setText("" + controller.currentUDPTimeAnnoucement());
        txtUDPTime.setEditable(false);

        txtRefreshFile = new JTextField(aux);
        txtRefreshFile.setText("" + controller.currentRefreshFileTime());
        txtRefreshFile.setEditable(false);

        txtShared = new JTextField(sizePath);
        txtShared.setText(controller.currentSharedFolderName());
        txtShared.setEditable(false);

        txtDownload = new JTextField(sizePath);
        txtDownload.setText(controller.currentDownloadFolderName());
        txtDownload.setEditable(false);
        
        sharedChooser = createButonSharedChooser();
        sharedChooser.setEnabled(false);
        
        downloadChooser = createButonDownloadChooser();
        downloadChooser.setEnabled(false);
        
        radioBtnIgnoreFoldersTRUE = new JRadioButton(resourceBundle.getString("true"));
        radioBtnIgnoreFoldersTRUE.setEnabled(false);        
        radioBtnIgnoreFoldersTRUE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (radioBtnIgnoreFoldersTRUE.isSelected()) {
                    radioBtnIgnoreFoldersFALSE.setSelected(false);
                }
            }
        });
        radioBtnIgnoreFoldersFALSE = new JRadioButton(resourceBundle.getString("false"));
        radioBtnIgnoreFoldersFALSE.setEnabled(false);  
        radioBtnIgnoreFoldersFALSE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (radioBtnIgnoreFoldersFALSE.isSelected()) {
                    radioBtnIgnoreFoldersTRUE.setSelected(false);
                }
            }
        });
        radioBtnIgnoreFilesWithoutExtensionTRUE = new JRadioButton(resourceBundle.getString("true"));
        radioBtnIgnoreFilesWithoutExtensionTRUE.setEnabled(false);
        radioBtnIgnoreFilesWithoutExtensionTRUE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (radioBtnIgnoreFilesWithoutExtensionTRUE.isSelected()) {
                    radioBtnIgnoreFilesWithoutExtensionFALSE.setSelected(false);
                }
            }
        });
        radioBtnIgnoreFilesWithoutExtensionFALSE  = new JRadioButton(resourceBundle.getString("false"));
        radioBtnIgnoreFilesWithoutExtensionFALSE.setEnabled(false);        
        radioBtnIgnoreFilesWithoutExtensionFALSE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (radioBtnIgnoreFilesWithoutExtensionFALSE.isSelected()) {
                    radioBtnIgnoreFilesWithoutExtensionTRUE.setSelected(false);
                }
            }
        });

        p.add(createPanelLabelText(UDP_PORT, txtUDPPort, ""));
        p.add(createPanelLabelText(UDP_TIME, txtUDPTime, resourceBundle.getString("seconds")));
        p.add(createPanelLabelText(REFRESH_TIME, txtRefreshFile, resourceBundle.getString("seconds")));
        p.add(createPanelLabelRadioBtn(IGNORE_FOLDERS, radioBtnIgnoreFoldersTRUE, radioBtnIgnoreFoldersFALSE));
        p.add(createPanelLabelRadioBtn(IGNORE_FILES_WITHOUT_EXT, radioBtnIgnoreFilesWithoutExtensionTRUE, radioBtnIgnoreFilesWithoutExtensionFALSE));
        p.add(new JLabel());
        p.add(createPanelLabelLabel(SHARED_FOLDER, txtShared));
        p.add(createPanelUniqueButon(sharedChooser));
        p.add(createPanelLabelLabel(DOWNLOAD_FOLDER, txtDownload));
        p.add(createPanelUniqueButon(downloadChooser));
        
        ativateRadioButtons();
        
        panel.add(p, BorderLayout.CENTER);
        panel.add(createPanelWest(), BorderLayout.WEST);
        
        return panel;
    }
    
    private void ativateRadioButtons() {
        if(Configuration.getIgnoreFolders() == true){
            radioBtnIgnoreFoldersTRUE.setSelected(true);
        }
        if(Configuration.getIgnoreFolders() == false){
            radioBtnIgnoreFoldersFALSE.setSelected(true);
        }
        if(Configuration.getIgnoreFilesWithoutExtension() == true){
            radioBtnIgnoreFilesWithoutExtensionTRUE.setSelected(true);
        }
        if(Configuration.getIgnoreFilesWithoutExtension() == false){
            radioBtnIgnoreFilesWithoutExtensionFALSE.setSelected(true);
        }
    }
    
    private JPanel createPanelLabelRadioBtn(String label1, JRadioButton radiobtn1, JRadioButton radiobtn2) {
        JLabel lb1 = new JLabel(label1, JLabel.RIGHT);
        lb1.setPreferredSize(LABEL_SIZE);

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));

        p.add(lb1);
        p.add(radiobtn1);
        p.add(radiobtn2);

        return p;
    } 
    
    private JButton createButonSharedChooser(){
        sharedChooser = new JButton(resourceBundle.getString("change_local_folder"));
        sharedChooser.setMnemonic(KeyEvent.VK_C);
        sharedChooser.setToolTipText(resourceBundle.getString("change_local_folder"));
        sharedChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser = new JFileChooser();
                fileChooser.setAcceptAllFileFilterUsed(true);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setCurrentDirectory(new File(Configuration.getSharedFolderName()));
                int resposta = fileChooser.showOpenDialog(ChangeConfigurationsUI.this);
                if (resposta == JFileChooser.APPROVE_OPTION) {
                    String folder = fileChooser.getSelectedFile().getAbsolutePath();
                    controller.changeSharedFolder(folder);
                    txtShared.setText(folder);
                }        
            }
        });
        return sharedChooser;
    }
    
    private JButton createButonDownloadChooser(){
        downloadChooser = new JButton(resourceBundle.getString("change_remote_folder"));
        downloadChooser.setMnemonic(KeyEvent.VK_D);
        downloadChooser.setToolTipText(resourceBundle.getString("change_remote_folder"));
        downloadChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser = new JFileChooser();
                fileChooser.setAcceptAllFileFilterUsed(true);
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setCurrentDirectory(new File(Configuration.getDownloadFolderName()));
                int resposta = fileChooser.showOpenDialog(ChangeConfigurationsUI.this);
                if (resposta == JFileChooser.APPROVE_OPTION) {
                    String folder = fileChooser.getSelectedFile().getAbsolutePath();
                    controller.changeDownloadFolder(folder);
                    txtDownload.setText(folder);
                }        
            }
        });
        return downloadChooser;
    }

    private JPanel createPanelLabelText(String label1, JTextField text, String label2) {
        JLabel lb1 = new JLabel(label1, JLabel.RIGHT);
        lb1.setPreferredSize(LABEL_SIZE);

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel lb2 = new JLabel(label2, JLabel.RIGHT);

        p.add(lb1);
        p.add(text);
        p.add(lb2);

        return p;
    }
    
    private JPanel createPanelLabelLabel(String label1, JTextField text) {
        JLabel lb1 = new JLabel(label1, JLabel.RIGHT);
        lb1.setPreferredSize(LABEL_SIZE);

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        p.add(lb1);
        p.add(text);

        return p;
    }

    private JPanel createPanelButons() {

        FlowLayout l = new FlowLayout();

        l.setHgap(20);
        l.setVgap(20);

        JPanel p = new JPanel(l);

        p.setBorder(BorderFactory.createTitledBorder(resourceBundle.getString("options")));

        JButton bt1 = createButonSave();
        JButton bt2 = createButonEdit();

        getRootPane().setDefaultButton(bt1);

        p.add(bt1);
        p.add(bt2);

        return p;
    }
    
    private JPanel createPanelUniqueButon(JButton bt) {

        FlowLayout l = new FlowLayout(FlowLayout.LEFT);
        
        JPanel p = new JPanel(l);
        p.add(bt);

        return p;
    }

    private JButton createButonSave() {
        saveBtn = new JButton(resourceBundle.getString("save"));
        saveBtn.setMnemonic(KeyEvent.VK_S);
        saveBtn.setToolTipText(resourceBundle.getString("save_new_configurations"));
        saveBtn.setEnabled(false);
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    
                    Integer UDPPortNumber = Integer.parseInt(txtUDPPort.getText());
                    Integer UDPTimeAnnouncement = Integer.parseInt(txtUDPTime.getText());
                    Integer refreshFileTime = Integer.parseInt(txtRefreshFile.getText());
                    boolean ignoreFolders = true;
                    boolean ignoreFilesWithoutExtension = true;
                    if (radioBtnIgnoreFoldersFALSE.isSelected()){
                        ignoreFolders = false;
                    }
                    
                    if (radioBtnIgnoreFilesWithoutExtensionFALSE.isSelected()){
                        ignoreFilesWithoutExtension = false;
                    }
                    
                    String[] aux1 =  Arrays.asList(ignoreFiles.toArray()).toArray(new String[ignoreFiles.toArray().length]);
                    String[] aux2 =  Arrays.asList(ignoreFilesStartingWith.toArray()).toArray(new String[ignoreFilesStartingWith.toArray().length]);
                    String[] aux3 =  Arrays.asList(ignoreFilesWithExtension.toArray()).toArray(new String[ignoreFilesWithExtension.toArray().length]);
                    
                    if (controller.saveConfigurations(UDPPortNumber, UDPTimeAnnouncement,
                            refreshFileTime, ignoreFolders, ignoreFilesWithoutExtension,
                            aux1, aux2, aux3)) {
                        finish();
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                resourceBundle.getString("change_configurations_error"),
                                resourceBundle.getString("error"),
                                JOptionPane.ERROR_MESSAGE);
                    }
                }catch (IllegalStateException | NumberFormatException ex ){
                    JOptionPane.showMessageDialog(
                                    null,
                            resourceBundle.getString("invalid_data"),
                            resourceBundle.getString("error"),
                                    JOptionPane.ERROR_MESSAGE); 
                } catch (FileNotFoundException exc) {
                    Logger.getLogger(ChangeConfigurationsUI.class.getName()).log(Level.SEVERE, null, exc);
                    JOptionPane.showMessageDialog(
                            null,
                            resourceBundle.getString("change_configurations_error"),
                            resourceBundle.getString("error"),
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return saveBtn;

    }

    private JButton createButonEdit() {
        editBtn = new JButton(resourceBundle.getString("edit"));
        editBtn.setMnemonic(KeyEvent.VK_S);
        editBtn.setToolTipText(resourceBundle.getString("edit_configurations"));
        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtUDPPort.setEditable(true);
                txtUDPTime.setEditable(true);
                txtRefreshFile.setEditable(true);
                saveBtn.setEnabled(true);
                sharedChooser.setEnabled(true);
                downloadChooser.setEnabled(true);
                addIgnoreFiles.setEnabled(true);
                rmIgnoreFiles.setEnabled(true);
                addIgnoreFilesStartingWith.setEnabled(true);
                rmIgnoreFilesStartingWith.setEnabled(true);
                addIgnoreFilesWithExtension.setEnabled(true);
                rmIgnoreFilesWithExtension.setEnabled(true);
                radioBtnIgnoreFoldersTRUE.setEnabled(true);
                radioBtnIgnoreFoldersFALSE.setEnabled(true);
                radioBtnIgnoreFilesWithoutExtensionTRUE.setEnabled(true);
                radioBtnIgnoreFilesWithoutExtensionFALSE.setEnabled(true);
                editBtn.setEnabled(false);
            }
        });
        return editBtn;

    }

    private JPanel createPanelWest() {
        ImageIcon background = new ImageIcon("src/main/resources/img/config.png");

        JLabel label = new JLabel();
        label.setIcon(background);

        JPanel panel = new JPanel();
        panel.add(label, BorderLayout.CENTER);

        return panel;
    }
    
    private JPanel createPanelCenter() {


        final int ROW = 1, COLUM = 3;
        final int HGAP = 20, VGAP = 0;
        JPanel p = new JPanel(new GridLayout( ROW,COLUM, HGAP, VGAP));
        
        ignoreFiles = new ArrayList<>();
        Collections.addAll(ignoreFiles, Configuration.getIgnoreFiles());
        ignoreFilesStartingWith = new ArrayList<>();
        Collections.addAll(ignoreFilesStartingWith, Configuration.getIgnoreFilesStartingWith());
        ignoreFilesWithExtension = new ArrayList<>();
        Collections.addAll(ignoreFilesWithExtension, Configuration.getIgnoreFilesWithExtension());
        
        lstIgnoreFiles = new JList(ignoreFiles.toArray());
        lstIgnoreFilesStartingWith = new JList(ignoreFilesStartingWith.toArray());
        lstIgnoreFilesWithExtension = new JList(ignoreFilesWithExtension.toArray());
        
        p.add(createPanelLists( IGNORE_FILES,
                                lstIgnoreFiles,
                                createButtonAddIgnoreFiles(),
                                createButtonRemoveIgnoreFiles()));
        
       p.add(createPanelLists( IGNORE_FILES_STARTING_WITH,
                                lstIgnoreFilesStartingWith,
                                createButtonAddIgnoreFilesStartingWith(),
                                createButtonRemoveIgnoreFilesStartingWith()));
       
       p.add(createPanelLists( IGNORE_FILES_WITH_EXTENSION,
                                lstIgnoreFilesWithExtension,
                                createButtonAddIgnoreFilesWithExtension(),
                                createButtonRemoveIgnoreFilesWithExtension()));
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
    
    private JButton createButtonAddIgnoreFiles(){
        addIgnoreFiles = new JButton(resourceBundle.getString("add"));
        addIgnoreFiles.setMnemonic(KeyEvent.VK_I);
        addIgnoreFiles.setToolTipText(resourceBundle.getString("add_to_ignored_files"));
        addIgnoreFiles.setEnabled(false);
        addIgnoreFiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DialogNewString(ChangeConfigurationsUI.this, IGNORE_FILES, ignoreFiles);
                lstIgnoreFiles.setListData(ignoreFiles.toArray());
            }
        });

        return addIgnoreFiles;
    }
    
    private JButton createButtonAddIgnoreFilesStartingWith(){
        addIgnoreFilesStartingWith = new JButton(resourceBundle.getString("add"));
        addIgnoreFilesStartingWith.setMnemonic(KeyEvent.VK_I);
        addIgnoreFilesStartingWith.setToolTipText(resourceBundle.getString("add_to_ignored_files_starting_with"));
        addIgnoreFilesStartingWith.setEnabled(false);
        addIgnoreFilesStartingWith.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DialogNewString(ChangeConfigurationsUI.this, IGNORE_FILES_STARTING_WITH, ignoreFilesStartingWith);
                lstIgnoreFilesStartingWith.setListData(ignoreFilesStartingWith.toArray());
            }
        });

        return addIgnoreFilesStartingWith;
    }
    
    private JButton createButtonAddIgnoreFilesWithExtension(){
        addIgnoreFilesWithExtension = new JButton(resourceBundle.getString("add"));
        addIgnoreFilesWithExtension.setMnemonic(KeyEvent.VK_I);
        addIgnoreFilesWithExtension.setToolTipText(resourceBundle.getString("add_to_ignored_files_with_extension"));
        addIgnoreFilesWithExtension.setEnabled(false);
        addIgnoreFilesWithExtension.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DialogNewString(ChangeConfigurationsUI.this, IGNORE_FILES_WITH_EXTENSION, ignoreFilesWithExtension);
                lstIgnoreFilesWithExtension.setListData(ignoreFilesWithExtension.toArray());
            }
        });

        return addIgnoreFilesWithExtension;
    }
    
    private JButton createButtonRemoveIgnoreFiles(){
        rmIgnoreFiles = new JButton(resourceBundle.getString("remove"));
        rmIgnoreFiles.setMnemonic(KeyEvent.VK_I);
        rmIgnoreFiles.setToolTipText(resourceBundle.getString("remove_ignored_file"));
        rmIgnoreFiles.setEnabled(false);
        rmIgnoreFiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] options = ignoreFiles.toArray();
                String newString = (String) JOptionPane.showInputDialog(
                        ChangeConfigurationsUI.this,
                        resourceBundle.getString("choose_an_item"), resourceBundle.getString("remove_ignored_file"),
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[0]);
                if (!Strings.isNullOrEmptyOrWhiteSpace(newString)) {
                    String[] options2 = {resourceBundle.getString("yes"), resourceBundle.getString("no")};
                    int answer = JOptionPane.showOptionDialog(
                            ChangeConfigurationsUI.this,
                            resourceBundle.getString("remove") + "\n" + newString,
                            resourceBundle.getString("remove_ignored_file"),
                            0,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options2,
                            options2[1]);
                    final int YES = 0;
                    if (answer == YES) {
                        ignoreFiles.remove(newString);
                        lstIgnoreFiles.setListData(ignoreFiles.toArray());
                        if (ignoreFiles.isEmpty()) {
                            rmIgnoreFiles.setEnabled(false);
                        }
                    }
                }
            }
        });

        return rmIgnoreFiles;
    }
    
    private JButton createButtonRemoveIgnoreFilesStartingWith(){
        rmIgnoreFilesStartingWith = new JButton(resourceBundle.getString("remove"));
        rmIgnoreFilesStartingWith.setMnemonic(KeyEvent.VK_I);
        rmIgnoreFilesStartingWith.setToolTipText(resourceBundle.getString("remove_from_ignored_files_starting_with"));
        rmIgnoreFilesStartingWith.setEnabled(false);
        rmIgnoreFilesStartingWith.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] options = ignoreFilesStartingWith.toArray();
                String newString = (String) JOptionPane.showInputDialog(
                        ChangeConfigurationsUI.this,
                        resourceBundle.getString("choose_an_item"), resourceBundle.getString("remove_from_ignored_files_starting_with"),
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[0]);
                if (!Strings.isNullOrEmptyOrWhiteSpace(newString)) {
                    String[] options2 = {resourceBundle.getString("yes"), resourceBundle.getString("no")};
                    int answer = JOptionPane.showOptionDialog(
                            ChangeConfigurationsUI.this,
                            resourceBundle.getString("remove") + "\n" + newString,
                            resourceBundle.getString("remove_ignored_file"),
                            0,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options2,
                            options2[1]);
                    final int YES = 0;
                    if (answer == YES) {
                        ignoreFilesStartingWith.remove(newString);
                        lstIgnoreFilesStartingWith.setListData(ignoreFilesStartingWith.toArray());
                        if (ignoreFilesStartingWith.isEmpty()) {
                            rmIgnoreFilesStartingWith.setEnabled(false);
                        }
                    }
                }
            }
        });

        return rmIgnoreFilesStartingWith;
    }
    private JButton createButtonRemoveIgnoreFilesWithExtension(){
        rmIgnoreFilesWithExtension = new JButton(resourceBundle.getString("remove"));
        rmIgnoreFilesWithExtension.setMnemonic(KeyEvent.VK_I);
        rmIgnoreFilesWithExtension.setToolTipText(resourceBundle.getString("remove_from_ignored_files_with_extension"));
        rmIgnoreFilesWithExtension.setEnabled(false);
        rmIgnoreFilesWithExtension.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] options = ignoreFilesWithExtension.toArray();
                String newString = (String) JOptionPane.showInputDialog(
                        ChangeConfigurationsUI.this,
                        resourceBundle.getString("choose_an_item"), resourceBundle.getString("remove_from_ignored_files_with_extension"),
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[0]);
                if (!Strings.isNullOrEmptyOrWhiteSpace(newString)) {
                    String[] options2 = {resourceBundle.getString("yes"), resourceBundle.getString("no")};
                    int answer = JOptionPane.showOptionDialog(
                            ChangeConfigurationsUI.this,
                            resourceBundle.getString("remove") + "\n" + newString,
                            resourceBundle.getString("remove_ignored_file"),
                            0,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options2,
                            options2[1]);
                    final int YES = 0;
                    if (answer == YES) {
                        ignoreFilesWithExtension.remove(newString);
                        lstIgnoreFilesWithExtension.setListData(ignoreFilesWithExtension.toArray());
                        if (ignoreFilesWithExtension.isEmpty()) {
                            rmIgnoreFilesWithExtension.setEnabled(false);
                        }
                    }
                }
            }
        });

        return rmIgnoreFilesWithExtension;
    }

    private void finish() {
        JOptionPane.showMessageDialog(
                null,
                resourceBundle.getString("configurations_changed_successfully"),
                resourceBundle.getString("configurations"),
                JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

}
