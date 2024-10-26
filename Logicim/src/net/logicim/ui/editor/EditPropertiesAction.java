package net.logicim.ui.editor;

import net.logicim.ui.Logicim;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.panels.LogicimPanel;
import net.logicim.ui.property.EditPropertiesDialogHelper;

public class EditPropertiesAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Edit Properties";

  protected LogicimPanel logicimPanel;

  public EditPropertiesAction(Logicim editor, LogicimPanel logicimPanel)
  {
    super(editor);
    this.logicimPanel = logicimPanel;
  }

  @Override
  public void executeEditorAction()
  {
    StaticView<?> componentView = editor.getComponent();
    if (componentView != null)
    {
      new EditPropertiesDialogHelper().showPropertyEditorDialog(logicimPanel.getFrame(),
                                                                editor,
                                                                componentView);
    }
  }

  @Override
  public boolean isAvailable()
  {
    StaticView<?> componentView = editor.getComponent();
    return componentView != null;
  }
}

