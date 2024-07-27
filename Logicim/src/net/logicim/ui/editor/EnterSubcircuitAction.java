package net.logicim.ui.editor;

import net.logicim.ui.Logicim;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

public class EnterSubcircuitAction
    extends SimulatorEditorAction
{
  public static final String NAME = "Enter Subcircuit";

  public EnterSubcircuitAction(Logicim editor)
  {
    super(editor);
  }

  @Override
  public void executeEditorAction()
  {
    editor.enterSubcircuit();
  }

  @Override
  public boolean isAvailable()
  {
    StaticView<?> component = editor.getComponent();
    if (component != null)
    {
      if (component instanceof SubcircuitInstanceView)
      {
        return true;
      }
    }
    return false;
  }
}

