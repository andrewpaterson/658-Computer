package net.logicim.ui.editor;

import net.logicim.ui.Logicim;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.panels.SimulatorPanel;
import net.logicim.ui.property.EditPropertiesDialogHelper;

public class EditPropertiesAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Edit Properties";

  protected SimulatorPanel simulatorPanel;

  public EditPropertiesAction(Logicim editor, SimulatorPanel simulatorPanel)
  {
    super(editor);
    this.simulatorPanel = simulatorPanel;
  }

  protected StaticView<?> getComponent()
  {
    StaticView<?> componentView = editor.getHoverComponentView();
    if (componentView == null)
    {
      componentView = editor.getCircuitEditor().getCurrentSubcircuitEditor().getSingleSelectionStaticView();
    }
    return componentView;
  }

  @Override
  public void executeEditorAction()
  {
    StaticView<?> componentView = getComponent();
    if (componentView != null)
    {
      new EditPropertiesDialogHelper().showPropertyEditorDialog(simulatorPanel.getFrame(),
                                                                editor,
                                                                componentView);
    }
  }

  @Override
  public boolean isAvailable()
  {
    StaticView<?> componentView = getComponent();
    return componentView != null;
  }
}

