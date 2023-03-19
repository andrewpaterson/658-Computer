package net.logicim.ui.editor;

import net.logicim.ui.Logicim;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.simulation.component.factory.ViewFactoryStore;

public class PlaceComponentAction
    extends SimulatorEditorAction
{
  private Class<? extends StaticView<?>> discreteViewClass;

  public PlaceComponentAction(Logicim editor, Class<? extends StaticView<?>> discreteViewClass)
  {
    super(editor);
    this.discreteViewClass = discreteViewClass;
  }

  @Override
  public void executeEditorAction()
  {
    editor.startPlaceComponent(ViewFactoryStore.getInstance().get(discreteViewClass));
  }

  @Override
  public String getDescription()
  {
    return "Place Component";
  }
}

