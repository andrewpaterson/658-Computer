package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class DeleteComponent
    extends EditorAction
{
  public DeleteComponent(SimulatorEditor editor)
  {
    super(editor);
  }

  @Override
  public void execute()
  {
    editor.deleteComponent();
  }
}

