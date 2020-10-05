package pe3;
import javax.swing.plaf.basic.*;
import javax.swing.*;
import java.awt.*;

// de JList maakt gebruik van een extended JScrollBar die hier gedefinieerd is.
// Er zijn alleen aanpassingen in het uiterlijk gebeurt.

public class BasicScrollBarUIExtended extends BasicScrollBarUI {

    @Override
    protected JButton createDecreaseButton(int orientation)  {
        return new BasicArrowButton(orientation,
				    new Color(98, 97, 11),
				    new Color(73, 72, 9),
				    new Color(73, 72, 9),
				    new Color(73, 72, 9));
    }

    @Override
    protected JButton createIncreaseButton(int orientation)  {
        return new BasicArrowButton(orientation,
				    new Color(98, 97, 11),
				    new Color(73, 72, 9),
				    new Color(73, 72, 9),
				    new Color(73, 72, 9));
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds)
    {
        g.setColor(new Color(73, 72, 9));
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);

		if(trackHighlight == DECREASE_HIGHLIGHT)	{
			paintDecreaseHighlight(g);
		}
		else if(trackHighlight == INCREASE_HIGHLIGHT)		{
			paintIncreaseHighlight(g);
		}
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds)
    {
		if(thumbBounds.isEmpty() || !scrollbar.isEnabled())	{
			return;
		}

        int w = thumbBounds.width;
        int h = thumbBounds.height;

		g.translate(thumbBounds.x, thumbBounds.y);

		g.setColor(new Color(98, 97, 11));
		g.drawRect(0, 0, w-1, h-1);
		g.setColor(new Color(98, 97, 11));
		g.fillRect(0, 0, w-1, h-1);

		g.setColor(new Color(98, 97, 11));
		g.drawLine(1, 1, 1, h-2);
		g.drawLine(2, 1, w-3, 1);

		g.setColor(new Color(73, 72, 9));
		g.drawLine(2, h-2, w-2, h-2);
		g.drawLine(w-2, 1, w-2, h-3);

		g.translate(-thumbBounds.x, -thumbBounds.y);
    }

}
