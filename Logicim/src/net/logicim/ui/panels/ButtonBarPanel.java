package net.logicim.ui.panels;

import net.logicim.ui.common.Colours;

import javax.swing.*;
import java.awt.*;

public class ButtonBarPanel
    extends JPanel
{
  protected JFrame frame;

  public ButtonBarPanel(JFrame frame)
  {
    this.frame = frame;
  }

  protected Color getPanelBackground()
  {
    return Colours.getInstance().getPanelBackground();
  }

  protected Color getPanelBorder()
  {
    return Colours.getInstance().getPanelBorder();
  }

  protected Dimension defaultButtonBarSize()
  {
    return new Dimension(32 + 4, 32 + 4);
  }
}

