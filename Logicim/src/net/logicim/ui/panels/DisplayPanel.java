package net.logicim.ui.panels;

import javax.swing.*;

public class DisplayPanel
    extends ButtonBarPanel
{
  public DisplayPanel(JFrame frame)
  {
    super(frame);
    setBackground(getPanelBackground());
    setPreferredSize(defaultButtonBarSize());
  }
}

