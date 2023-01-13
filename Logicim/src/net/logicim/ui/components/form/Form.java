package net.logicim.ui.components.form;

import javax.swing.*;
import java.awt.*;

import static net.logicim.ui.util.GridBagUtil.gridBagConstraints;

public class Form
    extends JPanel
{
  protected int verticalCount;

  public Form()
  {
    super(new GridBagLayout());
    verticalCount = 0;
  }

  public void addComponents(JComponent left, JComponent right)
  {
    addComponents(left, right, 2);
  }

  public void addComponents(JComponent left, JComponent right, int verticalSpacing)
  {
    add(left, gridBagConstraints(0, verticalCount, 1, 0, GridBagConstraints.HORIZONTAL));
    JPanel horizontalSpacer = new JPanel();
    horizontalSpacer.setPreferredSize(new Dimension(5, 5));
    add(horizontalSpacer, gridBagConstraints(1, verticalCount, 0, 0, GridBagConstraints.NONE));
    add(right, gridBagConstraints(2, verticalCount, 1, 0, GridBagConstraints.HORIZONTAL));
    verticalCount++;

    if (verticalSpacing > 0)
    {
      JPanel verticalSpacer = new JPanel();
      verticalSpacer.setPreferredSize(new Dimension(verticalSpacing, verticalSpacing));
      add(verticalSpacer, gridBagConstraints(0, verticalCount, 0, 0, GridBagConstraints.NONE));
      verticalCount++;
    }
  }
}

