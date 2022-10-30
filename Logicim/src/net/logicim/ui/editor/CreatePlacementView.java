package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.common.ViewFactory;

public class CreatePlacementView
    extends EditorAction
{
  private ViewFactory viewFactory;

  public CreatePlacementView(SimulatorEditor editor, ViewFactory viewFactory)
  {
    super(editor);
    this.viewFactory = viewFactory;
  }

  @Override
  public void execute()
  {
    editor.createPlacementView(viewFactory);
  }
}
