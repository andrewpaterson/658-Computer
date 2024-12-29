package net.logicim.ui.info;

import net.logicim.ui.Logicim;
import net.logicim.ui.circuit.path.ViewPath;

public class ViewPathInfo
    extends InfoLabel
{
  public ViewPathInfo(Logicim editor)
  {
    super(editor);
  }

  @Override
  public String getInfo()
  {
    ViewPath viewPath = editor.getCurrentViewPath();

    String description;
    if (viewPath != null)
    {
      String name = viewPath.getDescription();
      long id = viewPath.getId();
      description = name + " (" + id + ")";
    }
    else
    {
      description = "";
    }
    return String.format(" View Path: %s", description);
  }
}

