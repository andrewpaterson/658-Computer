package net.logicim.ui.editor;

import net.logicim.ui.Logicim;

public abstract class EditorAction
{
  protected Logicim editor;
  protected String name;

  public EditorAction(Logicim editor)
  {
    this.editor = editor;
  }

  public abstract void executeEditorAction();

  public void setName(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  public abstract boolean isAvailable();
}

