package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class PlacementRotateRight
    extends EditorAction
{
  public PlacementRotateRight(SimulatorEditor editor)
  {
    super(editor);
  }

  @Override
  public void execute()
  {
    editor.placementRotateRight();
  }
}

