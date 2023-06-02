package net.logicim.ui.panels;

import javax.swing.*;
import java.awt.*;

public class CircuitInfoPanel
    extends InformationPanel
{
  public CircuitInfoPanel(JFrame frame)
  {
    super(frame);
    setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, getPanelInfoBorder()));
    setBackground(getPanelBackground());
    setPreferredSize(new Dimension(16, 16));
  }
}

