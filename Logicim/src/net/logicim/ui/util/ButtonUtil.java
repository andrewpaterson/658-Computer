package net.logicim.ui.util;

import net.logicim.ui.components.button.Button;

import javax.swing.*;
import java.awt.*;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.NONE;

public class ButtonUtil
{
  public static final int DEFAULT_WIDTH = 120;

  public static JPanel buildButtons(int width, Button... buttons)
  {
    return buildButtons(0, width, buttons);
  }

  public static JPanel buildButtons(int spacerIndex, int width, Button... buttons)
  {
    JPanel panel = new JPanel();

    panel.setLayout(new GridBagLayout());
    int x = 0;
    boolean first = true;
    for (Button button : buttons)
    {
      if (x == spacerIndex)
      {
        panel.add(new JPanel(), GridBagUtil.gridBagConstraints(x, 0, 1, 0, BOTH));
        x++;
        first = true;
      }

      if (first)
      {
        first = false;
      }
      else
      {
        JPanel spacerPanel = new JPanel();
        spacerPanel.setPreferredSize(new Dimension(5, 5));
        panel.add(spacerPanel, GridBagUtil.gridBagConstraints(x, 0, 0, 0, NONE));
        x++;
      }

      JButton jButton = new JButton(button.getText());

      jButton.setPreferredSize(new Dimension(width, 24));
      jButton.addActionListener(button);
      panel.add(jButton, GridBagUtil.gridBagConstraints(x, 0, 0, 0, NONE));
      x++;
    }
    return panel;
  }
}

