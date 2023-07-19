package net.logicim.ui.simulation.component.passive.splitter;

import net.logicim.data.passive.wire.SplitterAppearance;
import net.logicim.ui.components.typeeditor.EnumPropertyEditor;
import net.logicim.ui.property.PropertiesPanel;

public class SplitterAppearanceEditor
    extends EnumPropertyEditor<SplitterAppearance>
{

  public SplitterAppearanceEditor(String name, SplitterAppearance current)
  {
    super(name, SplitterAppearance.class, current);
  }
}

