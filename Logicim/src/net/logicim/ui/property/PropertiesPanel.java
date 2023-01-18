package net.logicim.ui.property;

import net.logicim.ui.components.form.Form;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.VERTICAL;
import static net.logicim.ui.util.GridBagUtil.gridBagConstraints;

public abstract class PropertiesPanel
    extends JPanel
    implements FocusListener,
               ActionListener
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

  @Override
  public void focusGained(FocusEvent e)
  {
    System.out.println(e.getComponent().getName());
  }

  @Override
  public void focusLost(FocusEvent e)
  {
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getModifiers() == 0)
    {
      KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
      manager.focusNextComponent();
    }
  }
}

