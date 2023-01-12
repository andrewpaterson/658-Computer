package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.property.EditPropertiesDialogHelper;

import javax.swing.*;

public class EditPropertiesAction
    extends SimulatorEditorAction
{
  protected JFrame parentFrame;

  public EditPropertiesAction(SimulatorEditor editor, JFrame parentFrame)
  {
    super(editor);
    this.parentFrame = parentFrame;
  }

  @Override
  public void executeEditorAction()
  {
    ComponentView<?> componentView = editor.getHoverComponentView();
    if (componentView == null)
    {
      componentView = editor.getCircuitEditor().getSingleSelectionDiscreteView();
    }

    if (componentView != null)
    {
      new EditPropertiesDialogHelper(parentFrame, editor, componentView).showPropertyEditorDialog();
    }
  }
}

