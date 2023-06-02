package net.logicim.ui.panels;

import net.logicim.ui.common.Colours;

import javax.swing.*;
import java.awt.*;

public class CircuitInfoPanel
    extends InformationPanel
{
  public CircuitInfoPanel(JFrame frame)
  {
    super(frame);
    setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Colours.getInstance().getPanelInfoBorder()));
    setBackground(getPanelBackground());
    setPreferredSize(new Dimension(16, 16));
  }
}

