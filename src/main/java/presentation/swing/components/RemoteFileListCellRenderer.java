package presentation.swing.components;

import domain.RemoteFile;

import javax.swing.*;
import java.awt.*;

/**
 * Represents a list cell renderer for remote files.
 *
 * @author Ivo Ferro
 */
public class RemoteFileListCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
            RemoteFile remoteFile = (RemoteFile) value;
            value = String.format("%s - %s", remoteFile.getName(), remoteFile.getAddress().getHostAddress());
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }

}
