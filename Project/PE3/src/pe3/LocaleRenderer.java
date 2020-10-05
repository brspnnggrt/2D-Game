package pe3;
import javax.swing.*;
import java.awt.*;

// Kleine uitbreiding uiterlijk JList (weeral)

public class LocaleRenderer extends DefaultListCellRenderer {

    /** Creates a new instance of LocaleRenderer */
    public LocaleRenderer() {
    }

    @Override
    public Component getListCellRendererComponent(JList list,
            Object value,
            int index, boolean isSelected,
            boolean cellHasFocus) {

        super.getListCellRendererComponent(list,
                value,
                index,
                isSelected,
                cellHasFocus);
        setBorder(null);
        
        if ( isSelected == true ) {
            setBackground(new Color(73,72,9));
        } else {
            setBackground(new Color(98,97,11));
        }
        return this;
    }

}
