package net.logicim.ui.panels;

import javax.swing.*;

public class CreationPanel
    extends ButtonBarPanel
{
  public CreationPanel(JFrame frame)
  {
    super(frame);
    setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, getPanelBorder()));
    setBackground(getPanelBackground());
    setPreferredSize(defaultButtonBarSize());
  }
}

