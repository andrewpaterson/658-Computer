package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.integratedcircuit.factory.ViewFactoryStore;

public class CreateComponentView
    extends SimulatorEditorAction
{
  private Class<? extends ComponentView<?>> discreteViewClass;

  public CreateComponentView(SimulatorEditor editor, Class<? extends ComponentView<?>> discreteViewClass)
  {
    super(editor);
    this.discreteViewClass = discreteViewClass;
  }

  @Override
  public void executeEditorAction()
  {
    editor.startPlacementView(ViewFactoryStore.getInstance().get(discreteViewClass));
  }
}

