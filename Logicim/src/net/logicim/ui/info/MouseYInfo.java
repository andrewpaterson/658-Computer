package net.logicim.ui.info;

import net.logicim.ui.Logicim;

public class MouseYInfo
    extends InfoLabel
{
  public MouseYInfo(Logicim editor)
  {
    super(editor);
  }

  @Override
  public String getInfo()
  {
    return String.format(" Y %.1f ", editor.getMouseYOnGrid()).replace(',', '.');
  }
}

