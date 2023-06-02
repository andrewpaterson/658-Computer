package net.logicim.ui.panels;

import javax.swing.*;
import java.awt.*;

public class DisplayPanel
    extends ButtonBarPanel
{
  public DisplayPanel(JFrame frame)
  {
    super(frame);
    setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, getPanelBorder()));
    setBackground(getPanelBackground());
    setPreferredSize(defaultButtonBarSize());
  }
}

