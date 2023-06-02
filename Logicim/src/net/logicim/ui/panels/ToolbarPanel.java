package net.logicim.ui.panels;

import javax.swing.*;

public class ToolbarPanel
    extends ButtonBarPanel
{
  public ToolbarPanel(JFrame frame)
  {
    super(frame);
    setBackground(getPanelBackground());
    setPreferredSize(defaultButtonBarSize());
  }
}

