package net.logicim.ui.panels;

import net.logicim.ui.common.Colours;

import javax.swing.*;
import java.awt.*;

public class InformationPanel
    extends JPanel
{
  protected JFrame frame;

  public InformationPanel(JFrame frame)
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
}

