package net.logicim.ui.info;

import net.logicim.ui.Logicim;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

public class SubcircuitInfo
    extends InfoLabel
{
  public SubcircuitInfo(Logicim editor)
  {
    super(editor);
  }

  @Override
  public String getInfo()
  {
    SubcircuitEditor subcircuitEditor = editor.getCurrentSubcircuitEditor();
    if (subcircuitEditor != null)
    {
      String typeName = subcircuitEditor.getTypeName();
      return String.format(" Subcircuit %s ", typeName);
    }
    else
    {
      return "";
    }
  }
}

