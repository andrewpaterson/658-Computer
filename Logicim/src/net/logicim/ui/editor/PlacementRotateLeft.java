package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class PlacementRotateLeft extends EditorAction
{

  public PlacementRotateLeft(SimulatorEditor editor)
  {
    super(editor);
  }

  @Override
  public void execute()
  {
    editor.placementRotateLeft();
  }
}
