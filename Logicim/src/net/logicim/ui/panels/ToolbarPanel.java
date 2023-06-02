package net.logicim.ui.panels;

import javax.swing.*;

public class ToolbarPanel
    extends ButtonBarPanel
{
  public ToolbarPanel(JFrame frame)
  {
    super(frame);
    setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, getPanelBorder()));
    setBackground(getPanelBackground());
    setPreferredSize(defaultButtonBarSize());
  }
}

