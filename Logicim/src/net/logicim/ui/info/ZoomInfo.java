package net.logicim.ui.info;

import net.logicim.ui.Logicim;

public class ZoomInfo
    extends InfoLabel
{
  public ZoomInfo(Logicim editor)
  {
    super(editor);
  }

  @Override
  public String getInfo()
  {
    return String.format(" Z %.1f ", editor.getZoom()).replace(',', '.');
  }
}

