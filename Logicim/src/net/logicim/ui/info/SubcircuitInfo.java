package net.logicim.ui.info;

import net.logicim.ui.Logicim;
import net.logicim.ui.simulation.SubcircuitEditor;

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
    return String.format(" Subcircuit %s ", subcircuitEditor.getTypeName());
  }
}

