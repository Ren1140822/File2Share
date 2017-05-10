package presentation.swing.components;

import domain.DataFile;

import javax.swing.*;
import java.awt.*;

/**
 * Represents a list cell renderer for data files.
 *
 * @author Ivo Ferro
 */
public class DataFileListCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
            DataFile dataFile = (DataFile) value;
            value = String.format("%s", dataFile.name());
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }

}
