package presentation.swing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Locale;

/**
 * JDialog to choose a language.
 *
 * @author Ivo Ferro
 */
public class LanguageChooserDialog extends JDialog {

    /**
     * The minimum window size.
     */
    private static final Dimension MINIMUM_WINDOW_SIZE = new Dimension(320, 200);
    /**
     * The empty border.
     */
    private static final EmptyBorder EMPTY_BORDER = new EmptyBorder(20, 20, 20, 20);
    /**
     * The local to be chose.
     */
    private Locale locale;

    /**
     * Creates an instance of main frame.
     */
    private LanguageChooserDialog() {
        setModal(true);
        setTitle("F2Share");

        createComponents();

        setMinimumSize(MINIMUM_WINDOW_SIZE);
        setLocationRelativeTo(null);
        pack();
    }

    /**
     * Launches a language chooser to choose the language.
     *
     * @return chose language
     */
    public static Locale chooseLanguage() {
        LanguageChooserDialog l = new LanguageChooserDialog();
        l.setVisible(true);
        return l.getLocale();
    }

    /**
     * Creates the UI Components.
     */
    private void createComponents() {
        JPanel componentsPanel = new JPanel(new BorderLayout());

        JLabel textLabel = new JLabel("Choose a language / Escolha uma Linguagem", SwingConstants.CENTER);

        componentsPanel.add(textLabel, BorderLayout.NORTH);
        componentsPanel.add(createFlags(), BorderLayout.SOUTH);

        componentsPanel.setBorder(EMPTY_BORDER);
        add(componentsPanel);
    }

    /**
     * Creates the flags panel.
     *
     * @return flags panel
     */
    private JPanel createFlags() {
        JPanel flagsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));

        JLabel enLabel, ptLabel;

        try {
            URL url = getClass().getClassLoader().getResource("img/uk_flag.png");
            enLabel = new JLabel(new ImageIcon(url));

        } catch (Exception ex) {
            enLabel = new JLabel("English / Inglês");
        }

        try {
            URL url = getClass().getClassLoader().getResource("img/pt_flag.png");
            ptLabel = new JLabel(new ImageIcon(url));

        } catch (Exception ex) {
            ptLabel = new JLabel("Portuguese / Português");
        }

        enLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                locale = new Locale("en", "GB");
                dispose();
            }
        });

        ptLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                locale = new Locale("pt", "PT");
                dispose();
            }
        });

        flagsPanel.add(enLabel);
        flagsPanel.add(ptLabel);

        return flagsPanel;
    }

    /**
     * Gets the selected locale.
     *
     * @return selected locale
     */
    public Locale getLocale() {
        return this.locale;
    }
}
