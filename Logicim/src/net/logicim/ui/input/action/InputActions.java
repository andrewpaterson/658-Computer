package net.logicim.ui.input.action;

import net.logicim.common.SimulatorException;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.editor.EditorAction;
import net.logicim.ui.input.button.ButtonInput;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InputActions
{
  protected List<KeyInput> keyInputs;
  protected List<ButtonInput> mouseButtonInputs;
  protected Map<Class<? extends ComponentView<?>>, EditorAction> componentDoubleClickInputs;

  public InputActions()
  {
    keyInputs = new ArrayList<>();
    mouseButtonInputs = new ArrayList<>();
    componentDoubleClickInputs = new LinkedHashMap<>();
  }

  public void addKeyInputs(KeyInput keyInput)
  {
    keyInputs.add(keyInput);
  }

  public void keyPressed(int keyCode, boolean controlDown, boolean altDown, boolean shiftDown)
  {
    for (KeyInput action : keyInputs)
    {
      if (action.matched(keyCode, altDown, shiftDown, controlDown))
      {
        action.execute();
      }
    }
  }

  private Map<Integer, List<KeyInput>> getActionsByKeyCode()
  {
    Map<Integer, List<KeyInput>> keyActionMap = new LinkedHashMap<>();
    for (KeyInput keyInput : keyInputs)
    {
      int keyPressedCode = keyInput.getKeyPressedCode();
      List<KeyInput> keyInputList = keyActionMap.get(keyPressedCode);
      if (keyInputList == null)
      {
        keyInputList = new ArrayList<>();
        keyActionMap.put(keyPressedCode, keyInputList);
      }
      keyInputList.add(keyInput);
    }
    return keyActionMap;
  }

  public void validate()
  {
    validateActionKeyBindings();
  }

  private void validateActionKeyBindings()
  {
    Map<Integer, List<KeyInput>> keyActionMap = getActionsByKeyCode();
    for (List<KeyInput> keyInputs : keyActionMap.values())
    {
      for (KeyInput keyInputOuter : keyInputs)
      {
        for (KeyInput keyInputInner : keyInputs)
        {
          if (keyInputInner != keyInputOuter)
          {
            if (keyInputInner.isSame(keyInputOuter))
            {
              String innerKeyString = keyInputInner.toKeyString();
              String outerKeyString = keyInputOuter.toKeyString();
              String innerActionString = keyInputInner.getActionName();
              String outerActionString = keyInputOuter.getActionName();
              throw new SimulatorException("%s bound to action [%s] and also %s bound to action [%s].", innerKeyString, innerActionString, outerKeyString, outerActionString);
            }
          }
        }
      }
    }
  }

  public void addButtonInput(ButtonInput buttonInput)
  {
    mouseButtonInputs.add(buttonInput);
  }

  public void updateButtonsEnabled()
  {
    for (ButtonInput buttonInput : mouseButtonInputs)
    {
      buttonInput.enable();
    }
  }

  public void addComponentDoubleClickInput(Class<? extends ComponentView<?>> componentViewClass, EditorAction action)
  {
    componentDoubleClickInputs.put(componentViewClass, action);
  }

  public void componentViewDoubleClicked(StaticView<?> staticView)
  {
    if (staticView != null)
    {
      EditorAction action = componentDoubleClickInputs.get(staticView.getClass());
      if (action != null)
      {
        action.executeEditorAction();
      }
    }
  }
}

