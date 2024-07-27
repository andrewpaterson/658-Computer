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
    add(left, gridBagConstraints(0, verticalCount, 0.5, 0, GridBagConstraints.HORIZONTAL));
    add(createSpacer(5), gridBagConstraints(1, verticalCount, 0, 0, GridBagConstraints.NONE));
    add(right, gridBagConstraints(2, verticalCount, 1, 0, GridBagConstraints.HORIZONTAL));
    verticalCount++;

    addVerticalSpacer(verticalSpacing);
  }

  protected JPanel createSpacer(int i)
  {
    JPanel horizontalSpacer = new JPanel();
    horizontalSpacer.setPreferredSize(new Dimension(i, i));
    return horizontalSpacer;
  }

  public void addComponent(Component component)
  {
    addComponent(component, 2);
  }

  public void addComponent(Component component, int verticalSpacing)
  {
    add(component, gridBagConstraints(0, verticalCount, 1, 0, GridBagConstraints.HORIZONTAL, 3, 1));
    verticalCount++;

    addVerticalSpacer(verticalSpacing);
  }

  public void addVerticalSpacer(int verticalSpacing)
  {
    if (verticalSpacing > 0)
    {
      JPanel verticalSpacer = createSpacer(verticalSpacing);
      add(verticalSpacer, gridBagConstraints(0, verticalCount, 0, 0, GridBagConstraints.NONE));
      verticalCount++;
    }
  }

  public boolean isFirst()
  {
    return verticalCount == 0;
  }
}

