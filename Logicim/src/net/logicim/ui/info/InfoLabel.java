package net.logicim.ui.info;

import net.logicim.ui.Logicim;

import javax.swing.*;

public abstract class InfoLabel
{
  protected Logicim editor;
  protected JLabel label;

  public InfoLabel(Logicim editor)
  {
    this.editor = editor;
    this.label = new JLabel();
  }

  public JLabel getLabel()
  {
    return label;
  }

  public void update()
  {
    label.setText(getInfo());
  }

  public abstract String getInfo();
}

