package net.logicim.ui.info;

import net.logicim.ui.Logicim;

public class MouseXInfo
    extends InfoLabel
{
  public MouseXInfo(Logicim editor)
  {
    super(editor);
  }

  @Override
  public String getInfo()
  {
    return String.format(" X %.1f ", editor.getMouseXOnGrid()).replace(',', '.');
  }
}

