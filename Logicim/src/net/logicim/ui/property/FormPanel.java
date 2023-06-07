package net.logicim.ui.property;

import net.logicim.ui.components.Label;
import net.logicim.ui.components.form.Form;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import static java.awt.GridBagConstraints.BOTH;
import static net.logicim.ui.util.GridBagUtil.gridBagConstraints;

public class FormPanel
    extends JPanel
    implements FocusListener,
               ActionListener
{
  protected Form form;

  public FormPanel()
  {
    super(new GridBagLayout());
    form = new Form();
    addComponent(form);
  }

  protected void addComponent(Component component)
  {
    GridBagConstraints constraints = gridBagConstraints(0, 0, 1, 1, BOTH);
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.add(component, BorderLayout.NORTH);
    JScrollPane scrollPane = new JScrollPane(panel);
    scrollPane.setBorder(null);
    add(scrollPane, constraints);
    setBorder(new EmptyBorder(5, 5, 0, 5));
  }

  @Override
  public void focusGained(FocusEvent e)
  {
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

  public void addLabeledComponent(String label, JComponent component)
  {
    form.addComponents(new Label(label), component);
  }
}

