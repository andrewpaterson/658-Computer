package net.logicim.ui.util;

import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.SimulatorPanel;
import net.logicim.ui.common.wire.TunnelView;
import net.logicim.ui.editor.*;
import net.logicim.ui.input.action.InputAction;
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

public class SimulatorActions
{
  public static void create(SimulatorEditor editor, SimulatorPanel panel)
  {
    editor.addAction(new InputAction(new StopEditAction(editor), KeyEvent.VK_ESCAPE, DontCare, DontCare, DontCare));
    editor.addAction(new InputAction(new RunOneEvent(editor), KeyEvent.VK_T, Up, Up, Up));

    editor.addAction(new InputAction(new PlaceComponentAction(editor, ClockView.class), KeyEvent.VK_C, Up, Down, Up));
    editor.addAction(new InputAction(new PlaceComponentAction(editor, InverterView.class), KeyEvent.VK_N, Up, Down, Up));
    editor.addAction(new InputAction(new PlaceComponentAction(editor, OrGateView.class), KeyEvent.VK_O, Up, Down, Up));
    editor.addAction(new InputAction(new PlaceComponentAction(editor, NorGateView.class), KeyEvent.VK_O, Up, Down, Down));
    editor.addAction(new InputAction(new PlaceComponentAction(editor, AndGateView.class), KeyEvent.VK_A, Up, Down, Up));
    editor.addAction(new InputAction(new PlaceComponentAction(editor, NandGateView.class), KeyEvent.VK_A, Up, Down, Down));
    editor.addAction(new InputAction(new PlaceComponentAction(editor, XorGateView.class), KeyEvent.VK_X, Up, Down, Up));
    editor.addAction(new InputAction(new PlaceComponentAction(editor, XnorGateView.class), KeyEvent.VK_X, Up, Down, Down));
    editor.addAction(new InputAction(new PlaceComponentAction(editor, BufferView.class), KeyEvent.VK_N, Up, Down, Down));
    editor.addAction(new InputAction(new PlaceComponentAction(editor, OscilloscopeView.class), KeyEvent.VK_P, Up, Down, Up));
    editor.addAction(new InputAction(new PlaceComponentAction(editor, GroundView.class), KeyEvent.VK_G, Up, Up, Up));
    editor.addAction(new InputAction(new PlaceComponentAction(editor, PositivePowerView.class), KeyEvent.VK_V, Up, Up, Up));
    editor.addAction(new InputAction(new PlaceComponentAction(editor, SplitterView.class), KeyEvent.VK_S, Up, Down, Up));
    editor.addAction(new InputAction(new PlaceComponentAction(editor, TunnelView.class), KeyEvent.VK_T, Up, Down, Up));
    editor.addAction(new InputAction(new PlaceComponentAction(editor, LabelView.class), KeyEvent.VK_L, Up, Down, Up));

    editor.addAction(new InputAction(new EditPropertiesAction(editor, panel), KeyEvent.VK_E, Up, Up, Up));
    editor.addAction(new InputAction(new PlacementRotateLeft(editor), KeyEvent.VK_R, Up, Down, Up));
    editor.addAction(new InputAction(new PlacementRotateRight(editor), KeyEvent.VK_R, Up, Up, Up));
    editor.addAction(new InputAction(new ToggleRunSimulation(editor), KeyEvent.VK_K, Up, Up, Down));
    editor.addAction(new InputAction(new DeleteAction(editor), KeyEvent.VK_DELETE, Up, Up, Up));
    editor.addAction(new InputAction(new IncreaseSimulationSpeed(editor), KeyEvent.VK_EQUALS, Up, Up, Up));
    editor.addAction(new InputAction(new DecreaseSimulationSpeed(editor), KeyEvent.VK_MINUS, Up, Up, Up));
    editor.addAction(new InputAction(new ResetSimulation(editor), KeyEvent.VK_R, Up, Up, Down));
    editor.addAction(new InputAction(new SaveSimulation(panel), KeyEvent.VK_S, Up, Up, Down));
    editor.addAction(new InputAction(new LoadSimulation(panel), KeyEvent.VK_L, Up, Up, Down));
    editor.addAction(new InputAction(new UndoAction(editor), KeyEvent.VK_Z, Up, Up, Down));
    editor.addAction(new InputAction(new RedoAction(editor), KeyEvent.VK_Y, Up, Up, Down));

    editor.addAction(new InputAction(new MoveAction(editor), KeyEvent.VK_M, Up, Up, Up));
    editor.addAction(new InputAction(new CopyAction(editor), KeyEvent.VK_C, Up, Up, Down));
    editor.addAction(new InputAction(new PasteAction(editor), KeyEvent.VK_V, Up, Up, Down));
    editor.addAction(new InputAction(new CutAction(editor), KeyEvent.VK_X, Up, Up, Down));
    editor.addAction(new InputAction(new DuplicateAction(editor), KeyEvent.VK_D, Up, Up, Down));

    editor.addAction(new InputAction(new PlaceComponentAction(editor, PinView.class), KeyEvent.VK_P, Up, Up, Up));
    editor.addAction(new InputAction(new NewSubcircuitAction(editor, panel), KeyEvent.VK_ENTER, Up, Down, Up));
    editor.addAction(new InputAction(new PreviousSubcircuitAction(editor), KeyEvent.VK_LEFT, Down, Up, Up));
    editor.addAction(new InputAction(new NextSubcircuitAction(editor), KeyEvent.VK_RIGHT, Down, Up, Up));
    editor.addAction(new InputAction(new LeaveSubcircuitAction(editor), KeyEvent.VK_BACK_SPACE, Down, Up, Up));
    editor.addAction(new InputAction(new ReenterSubcircuitAction(editor), KeyEvent.VK_ENTER, Down, Up, Up));

    for (int i = 0; i < 10; i++)
    {
      editor.addAction(new InputAction(new BookmarkSubcircuitAction(editor, i), KeyEvent.VK_0 + i, Up, Up, Down));
      editor.addAction(new InputAction(new GotoSubcircuitAction(editor, i), KeyEvent.VK_0 + i, Up, Up, Up));
      editor.addAction(new InputAction(new PlaceSubcircuitAction(editor, i), KeyEvent.VK_0 + i, Up, Down, Down));
    }
  }
}

