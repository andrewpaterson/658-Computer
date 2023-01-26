package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.simulation.component.factory.ViewFactoryStore;

public class CreateComponentAction
    extends SimulatorEditorAction
{
  private Class<? extends StaticView<?>> discreteViewClass;

  public CreateComponentAction(SimulatorEditor editor, Class<? extends StaticView<?>> discreteViewClass)
  {
    super(editor);
    this.discreteViewClass = discreteViewClass;
  }

  @Override
  public void executeEditorAction()
  {
    editor.startPlaceComponent(ViewFactoryStore.getInstance().get(discreteViewClass));
  }
}

