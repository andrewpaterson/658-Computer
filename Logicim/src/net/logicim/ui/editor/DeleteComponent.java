package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;

public class DeleteComponent
    extends SimulatorEditorAction
{
  public DeleteComponent(SimulatorEditor editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.editActionDeleteComponent();
  }
}

