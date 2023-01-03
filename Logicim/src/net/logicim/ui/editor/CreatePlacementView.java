package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.common.integratedcircuit.DiscreteView;
import net.logicim.ui.integratedcircuit.factory.ViewFactoryStore;

public class CreatePlacementView
    extends SimulatorEditorAction
{
  private Class<? extends DiscreteView<?>> discreteViewClass;

  public CreatePlacementView(SimulatorEditor editor, Class<? extends DiscreteView<?>> discreteViewClass)
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

