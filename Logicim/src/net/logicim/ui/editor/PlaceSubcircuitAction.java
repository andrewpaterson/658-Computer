package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class PlaceSubcircuitAction
    extends SimulatorEditorAction
{
  protected int index;

  public PlaceSubcircuitAction(SimulatorEditor editor, int index)
  {
    super(editor);
    this.index = index;
  }

  @Override
  public void executeEditorAction()
  {
    editor.startPlaceSubcircuit(index);
  }

  @Override
  public String getDescription()
  {
    return "Place Subcircuit";
  }
}

