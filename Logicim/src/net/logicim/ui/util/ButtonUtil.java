package net.logicim.ui.util;

import net.logicim.ui.components.button.Button;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.NONE;

public class ButtonUtil
{
  public static void buildButtons(JPanel panel, Button... buttons)
  {
    panel.setLayout(new GridBagLayout());
    int x = 0;
    panel.add(new JPanel(), GridBagUtil.gridBagConstraints(x, 0, 1, 0, BOTH));
    x++;
    boolean first = true;
    for (Button button : buttons)
    {
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
      jButton.setPreferredSize(new Dimension(120, 24));
      jButton.addActionListener(button);
      panel.add(jButton, GridBagUtil.gridBagConstraints(x, 0, 0, 0, NONE));
      x++;
    }
    panel.setBorder(new EmptyBorder(5, 5, 5, 5));
  }
}

