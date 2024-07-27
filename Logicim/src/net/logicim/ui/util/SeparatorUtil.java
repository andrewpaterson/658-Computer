package net.logicim.ui.util;

import javax.swing.*;
import java.awt.*;

import static net.logicim.ui.util.GridBagUtil.gridBagConstraints;

public class SeparatorUtil
{
  public static JPanel buildSeparator(JComponent component)
  {
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    panel.add(new JSeparator(), gridBagConstraints(0, 0, 2, 0, GridBagConstraints.HORIZONTAL));
    panel.add(component, gridBagConstraints(1, 0, 1, 0, GridBagConstraints.NONE));
    panel.add(new JSeparator(), gridBagConstraints(2, 0, 2, 0, GridBagConstraints.HORIZONTAL));
    return panel;
  }
}

