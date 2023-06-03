package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public class PlaceSubcircuitAction
    extends SimulatorEditorAction
{
  protected int index;

  public PlaceSubcircuitAction(Logicim editor, int index)
  {
    super(editor);
    this.index = index;
  }

  public static String name(int index)
  {
    return "Place Subcircuit " + index;
  }

  @Override
  public void executeEditorAction()
  {
    editor.startPlaceSubcircuit(index);
  }
}

