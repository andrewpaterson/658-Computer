package net.logicim.ui.panels;

import javax.swing.*;
import java.awt.*;

public class SelectedInfoPanel
    extends InformationPanel
{
  public SelectedInfoPanel(JFrame frame)
  {
    super(frame);
    setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, getPanelInfoBorder()));
    setBackground(getPanelBackground());
    setPreferredSize(new Dimension(32, 32));
  }
}

