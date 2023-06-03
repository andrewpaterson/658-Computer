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

  @Override
  public void executeEditorAction()
  {
    editor.startPlaceSubcircuit(index);
  }
}

