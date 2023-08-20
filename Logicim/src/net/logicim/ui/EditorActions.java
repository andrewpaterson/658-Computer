package net.logicim.ui;

import net.common.SimulatorException;
import net.logicim.ui.editor.EditorAction;

import java.util.LinkedHashMap;
import java.util.Map;

public class EditorActions
{
  protected Map<String, EditorAction> actions;

  public EditorActions()
  {
    actions = new LinkedHashMap<>();
  }

  public void addAction(String description, EditorAction action)
  {
    EditorAction existingAction = actions.get(description);
    if (existingAction == null)
    {
      actions.put(description, action);
    }
    else
    {
      throw new SimulatorException("Cannot add Editor Action [%s].  It already exists.", description);
    }
  }

  public EditorAction getAction(String description)
  {
    EditorAction action = actions.get(description);
    if (action == null)
    {
      throw new SimulatorException("Cannot find Editor Action [%s].", description);
    }
    return action;
  }
}

