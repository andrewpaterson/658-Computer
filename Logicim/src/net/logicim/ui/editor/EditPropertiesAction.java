package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.SimulatorPanel;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.property.EditPropertiesDialogHelper;

public class EditPropertiesAction
    extends SimulatorEditorAction
{
  protected SimulatorPanel simulatorPanel;

  public EditPropertiesAction(SimulatorEditor editor, SimulatorPanel simulatorPanel)
  {
    super(editor);
    this.simulatorPanel = simulatorPanel;
  }

  @Override
  public void executeEditorAction()
  {
    StaticView<?> componentView = editor.getHoverComponentView();
    if (componentView == null)
    {
      componentView = editor.getCircuitEditor().getCurrentSubcircuitView().getSingleSelectionStaticView();
    }

    if (componentView != null)
    {
      new EditPropertiesDialogHelper().showPropertyEditorDialog(simulatorPanel.getFrame(),
                                                                editor,
                                                                componentView);
    }
  }

  @Override
  public String getDescription()
  {
    return "Edit";
  }
}

