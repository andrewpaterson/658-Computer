package net.logicim.ui.property;

import net.logicim.ui.components.form.Form;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.VERTICAL;
import static net.logicim.ui.util.GridBagUtil.gridBagConstraints;

public abstract class PropertiesPanel
    extends JPanel
{
  public PropertiesPanel(LayoutManager layout)
  {
    super(layout);
  }

  protected void addPropertyFormView(Form form)
  {
    add(form, gridBagConstraints(0, 0, 1, 0, HORIZONTAL));
    add(new JPanel(), gridBagConstraints(0, 1, 0, 1, VERTICAL));
    setBorder(new EmptyBorder(5, 5, 0, 5));
  }
}
