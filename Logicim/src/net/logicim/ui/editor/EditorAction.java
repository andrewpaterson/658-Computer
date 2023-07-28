package net.logicim.ui.editor;

import net.logicim.common.SimulatorException;
import net.logicim.ui.Logicim;

public abstract class EditorAction
{
  protected Logicim editor;
  protected String name;

  public EditorAction(Logicim editor)
  {
    if (editor == null)
    {
      throw new SimulatorException("Editor may not be null.");
    }
    this.editor = editor;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  public Logicim getEditor()
  {
    return editor;
  }

  public abstract void executeEditorAction();

  public abstract boolean isAvailable();
}

