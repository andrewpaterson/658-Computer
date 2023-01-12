package net.logicim.ui.components.typeeditor;

import javax.swing.*;

public interface PropertyEditor
{
  Object getValue();

  JComponent getComponent();
}

