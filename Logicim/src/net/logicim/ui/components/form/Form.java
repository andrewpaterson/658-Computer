package net.logicim.ui.components.form;

import net.logicim.ui.util.GridBagUtil;

import javax.swing.*;
import java.awt.*;

import static net.logicim.ui.util.GridBagUtil.*;

public class Form
    extends JPanel
{
  protected int verticalCount;

  public Form()
  {
    super(new GridBagLayout());
    verticalCount = 0;
  }

  public void add(JComponent left, JComponent right)
  {
    add(left, gridBagConstraints(0, verticalCount, 1, 0, GridBagConstraints.HORIZONTAL));
    JPanel horizontalSpacer = new JPanel();
    horizontalSpacer.setPreferredSize(new Dimension(5, 5));
    add(horizontalSpacer, gridBagConstraints(1, verticalCount, 0, 0, GridBagConstraints.NONE));
    add(right, gridBagConstraints(2, verticalCount, 1, 0, GridBagConstraints.HORIZONTAL));
    verticalCount++;

    JPanel verticalSpacer = new JPanel();
    verticalSpacer.setPreferredSize(new Dimension(2, 2));
    add(verticalSpacer, gridBagConstraints(0, verticalCount, 0, 0, GridBagConstraints.NONE));
    verticalCount++;
  }
}

