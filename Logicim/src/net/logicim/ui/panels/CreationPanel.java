package net.logicim.ui.panels;

import javax.swing.*;

public class CreationPanel
    extends ButtonBarPanel
{
  public CreationPanel(JFrame frame)
  {
    super(frame);
    setBackground(getPanelBackground());
    setPreferredSize(defaultButtonBarSize());
  }
}

