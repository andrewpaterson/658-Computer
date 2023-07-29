package net.logicim.ui.input;

import net.logicim.ui.Logicim;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.wire.TunnelView;
import net.logicim.ui.editor.*;
import net.logicim.ui.input.action.ButtonState;
import net.logicim.ui.input.action.KeyInput;
import net.logicim.ui.simulation.component.decorative.label.LabelView;
import net.logicim.ui.simulation.component.integratedcircuit.extra.OscilloscopeView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.clock.ClockView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.and.AndGateView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.and.NandGateView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.buffer.BufferView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.buffer.InverterView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.or.NorGateView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.or.OrGateView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.xor.XnorGateView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.xor.XorGateView;
import net.logicim.ui.simulation.component.passive.pin.PinView;
import net.logicim.ui.simulation.component.passive.power.GroundView;
import net.logicim.ui.simulation.component.passive.power.PositivePowerView;
import net.logicim.ui.simulation.component.passive.splitter.SplitterView;

import java.awt.event.KeyEvent;

import static net.logicim.ui.input.action.ButtonState.*;

public class KeyInputsFactory
{
  public static void create(Logicim editor)
  {
    editor.addKeyInput(new KeyInput(editor.getAction(StopEditAction.NAME), KeyEvent.VK_ESCAPE, DontCare, DontCare, DontCare));
    editor.addKeyInput(new KeyInput(editor.getAction(RunOneEventAction.NAME), KeyEvent.VK_T, Up, Up, Up));

    placeComponentKeyInput(editor, ClockView.class, KeyEvent.VK_C, Up, Up, Down);
    placeComponentKeyInput(editor, InverterView.class, KeyEvent.VK_N, Up, Up, Down);
    placeComponentKeyInput(editor, OrGateView.class, KeyEvent.VK_O, Up, Up, Down);
    placeComponentKeyInput(editor, NorGateView.class, KeyEvent.VK_O, Down, Up, Down);
    placeComponentKeyInput(editor, AndGateView.class, KeyEvent.VK_A, Up, Up, Down);
    placeComponentKeyInput(editor, NandGateView.class, KeyEvent.VK_A, Down, Up, Down);
    placeComponentKeyInput(editor, XorGateView.class, KeyEvent.VK_X, Up, Up, Down);
    placeComponentKeyInput(editor, XnorGateView.class, KeyEvent.VK_X, Down, Up, Down);
    placeComponentKeyInput(editor, BufferView.class, KeyEvent.VK_N, Down, Up, Down);
    placeComponentKeyInput(editor, OscilloscopeView.class, KeyEvent.VK_P, Up, Up, Down);
    placeComponentKeyInput(editor, GroundView.class, KeyEvent.VK_G, Up, Up, Up);
    placeComponentKeyInput(editor, PositivePowerView.class, KeyEvent.VK_V, Up, Up, Up);
    placeComponentKeyInput(editor, SplitterView.class, KeyEvent.VK_S, Up, Up, Down);
    placeComponentKeyInput(editor, TunnelView.class, KeyEvent.VK_T, Up, Up, Down);
    placeComponentKeyInput(editor, LabelView.class, KeyEvent.VK_L, Up, Up, Down);
    placeComponentKeyInput(editor, PinView.class, KeyEvent.VK_P, Up, Up, Up);

    editor.addKeyInput(new KeyInput(editor.getAction(EditPropertiesAction.NAME), KeyEvent.VK_E, Up, Up, Up));
    editor.addKeyInput(new KeyInput(editor.getAction(PlacementRotateLeftAction.NAME), KeyEvent.VK_R, Up, Up, Down));
    editor.addKeyInput(new KeyInput(editor.getAction(PlacementRotateRightAction.NAME), KeyEvent.VK_R, Up, Up, Up));
    editor.addKeyInput(new KeyInput(editor.getAction(ToggleRunSimulationAction.NAME), KeyEvent.VK_K, Up, Up, Up));
    editor.addKeyInput(new KeyInput(editor.getAction(ToggleRunSimulationAction.NAME), KeyEvent.VK_K, Down, Up, Up));
    editor.addKeyInput(new KeyInput(editor.getAction(DeleteAction.NAME), KeyEvent.VK_DELETE, Up, Up, Up));
    editor.addKeyInput(new KeyInput(editor.getAction(IncreaseSimulationSpeedAction.NAME), KeyEvent.VK_EQUALS, Up, Up, Up));
    editor.addKeyInput(new KeyInput(editor.getAction(DecreaseSimulationSpeedAction.NAME), KeyEvent.VK_MINUS, Up, Up, Up));
    editor.addKeyInput(new KeyInput(editor.getAction(ResetSimulationAction.NAME), KeyEvent.VK_R, Down, Up, Up));
    editor.addKeyInput(new KeyInput(editor.getAction(RecreateSimulationAction.NAME), KeyEvent.VK_R, Down, Down, Down));
    editor.addKeyInput(new KeyInput(editor.getAction(SaveSimulationAction.NAME), KeyEvent.VK_S, Down, Up, Up));
    editor.addKeyInput(new KeyInput(editor.getAction(LoadSimulationAction.NAME), KeyEvent.VK_L, Down, Up, Up));
    editor.addKeyInput(new KeyInput(editor.getAction(LoadSimulationAction.NAME), KeyEvent.VK_L, Up, Up, Up));
    editor.addKeyInput(new KeyInput(editor.getAction(UndoAction.NAME), KeyEvent.VK_Z, Down, Up, Up));
    editor.addKeyInput(new KeyInput(editor.getAction(RedoAction.NAME), KeyEvent.VK_Y, Down, Up, Up));
    editor.addKeyInput(new KeyInput(editor.getAction(ZoomResetAction.NAME), KeyEvent.VK_0, Down, Up, Up));

    editor.addKeyInput(new KeyInput(editor.getAction(MoveAction.NAME), KeyEvent.VK_M, Up, Up, Up));
    editor.addKeyInput(new KeyInput(editor.getAction(CopyAction.NAME), KeyEvent.VK_C, Down, Up, Up));
    editor.addKeyInput(new KeyInput(editor.getAction(PasteAction.NAME), KeyEvent.VK_V, Down, Up, Up));
    editor.addKeyInput(new KeyInput(editor.getAction(CutAction.NAME), KeyEvent.VK_X, Down, Up, Up));
    editor.addKeyInput(new KeyInput(editor.getAction(DuplicateAction.NAME), KeyEvent.VK_D, Down, Up, Up));

    editor.addKeyInput(new KeyInput(editor.getAction(NewSubcircuitAction.NAME), KeyEvent.VK_ENTER, Up, Up, Down));
    editor.addKeyInput(new KeyInput(editor.getAction(PreviousSubcircuitAction.NAME), KeyEvent.VK_LEFT, Up, Down, Up));
    editor.addKeyInput(new KeyInput(editor.getAction(NextSubcircuitAction.NAME), KeyEvent.VK_RIGHT, Up, Down, Up));
    editor.addKeyInput(new KeyInput(editor.getAction(NavigateBackwardSubcircuitAction.NAME), KeyEvent.VK_LEFT, Down, Down, Up));
    editor.addKeyInput(new KeyInput(editor.getAction(NavigateForwardSubcircuitAction.NAME), KeyEvent.VK_RIGHT, Down, Down, Up));
    editor.addKeyInput(new KeyInput(editor.getAction(EditSubcircuitAction.NAME), KeyEvent.VK_E, Down, Up, Down));

    for (int i = 1; i < 10; i++)
    {
      editor.addKeyInput(new KeyInput(editor.getAction(BookmarkSubcircuitAction.name(i)), KeyEvent.VK_0 + i, Down, Up, Up));
      editor.addKeyInput(new KeyInput(editor.getAction(GotoSubcircuitAction.name(i)), KeyEvent.VK_0 + i, Up, Up, Up));
      editor.addKeyInput(new KeyInput(editor.getAction(PlaceSubcircuitAction.name(i)), KeyEvent.VK_0 + i, Up, Up, Down));
    }

    editor.addKeyInput(new KeyInput(editor.getAction(SelectSimulationAction.NAME), KeyEvent.VK_S, Down, Up, Down));
    editor.addKeyInput(new KeyInput(editor.getAction(NewSimulationAction.NAME), KeyEvent.VK_S, Down, Down, Down));
  }

  protected static void placeComponentKeyInput(Logicim editor, Class<? extends StaticView<?>> staticViewClass, int keyEvent, ButtonState ctrlHeld, ButtonState altHeld, ButtonState shiftHeld)
  {
    editor.addKeyInput(new KeyInput(editor.getAction(PlaceComponentAction.name(staticViewClass)), keyEvent, ctrlHeld, altHeld, shiftHeld));
  }
}

