package net.logicim.ui.editor;

import net.logicim.common.util.StringUtil;
import net.logicim.ui.Logicim;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.simulation.component.factory.ViewFactoryStore;

public class PlaceComponentAction
    extends SimulatorEditorAction
{
  private Class<? extends StaticView<?>> staticViewClass;

  public PlaceComponentAction(Logicim editor, Class<? extends StaticView<?>> staticViewClass)
  {
    super(editor);
    this.staticViewClass = staticViewClass;
  }

  @Override
  public void executeEditorAction()
  {
    editor.startPlaceComponent(ViewFactoryStore.getInstance().get(staticViewClass));
  }
}

