package net.logicim.ui.util;

import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.SimulatorPanel;
import net.logicim.ui.common.wire.TunnelView;
import net.logicim.ui.editor.*;
import net.logicim.ui.input.action.InputAction;
import net.logicim.ui.simulation.component.decorative.label.LabelView;
import net.logicim.ui.simulation.component.integratedcircuit.extra.OscilloscopeView;
import net.logicim.ui.simulation.component.passive.splitter.SplitterView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.clock.ClockView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.and.AndGateView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.and.NandGateView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.buffer.BufferView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.buffer.InverterView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.or.NorGateView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.or.OrGateView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.xor.XnorGateView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.logic.xor.XorGateView;
import net.logicim.ui.simulation.component.passive.power.GroundView;
import net.logicim.ui.simulation.component.passive.power.PositivePowerView;

import java.awt.event.KeyEvent;

import static net.logicim.ui.input.action.ButtonState.*;

public class SimulatorActions
{
  public static void create(SimulatorEditor editor, SimulatorPanel panel)
  {
    editor.addAction(new InputAction(new StopCurrent(editor), KeyEvent.VK_ESCAPE, DontCare, DontCare, DontCare));
    editor.addAction(new InputAction(new RunOneEvent(editor), KeyEvent.VK_T, Up, Up, Up));

    editor.addAction(new InputAction(new CreateComponentAction(editor, ClockView.class), KeyEvent.VK_C, Up, Down, Up));
    editor.addAction(new InputAction(new CreateComponentAction(editor, InverterView.class), KeyEvent.VK_N, Up, Down, Up));
    editor.addAction(new InputAction(new CreateComponentAction(editor, OrGateView.class), KeyEvent.VK_O, Up, Down, Up));
    editor.addAction(new InputAction(new CreateComponentAction(editor, NorGateView.class), KeyEvent.VK_O, Up, Down, Down));
    editor.addAction(new InputAction(new CreateComponentAction(editor, AndGateView.class), KeyEvent.VK_A, Up, Down, Up));
    editor.addAction(new InputAction(new CreateComponentAction(editor, NandGateView.class), KeyEvent.VK_A, Up, Down, Down));
    editor.addAction(new InputAction(new CreateComponentAction(editor, XorGateView.class), KeyEvent.VK_X, Up, Down, Up));
    editor.addAction(new InputAction(new CreateComponentAction(editor, XnorGateView.class), KeyEvent.VK_X, Up, Down, Down));
    editor.addAction(new InputAction(new CreateComponentAction(editor, BufferView.class), KeyEvent.VK_N, Up, Down, Down));
    editor.addAction(new InputAction(new CreateComponentAction(editor, OscilloscopeView.class), KeyEvent.VK_P, Up, Down, Up));
    editor.addAction(new InputAction(new CreateComponentAction(editor, GroundView.class), KeyEvent.VK_G, Up, Up, Up));
    editor.addAction(new InputAction(new CreateComponentAction(editor, PositivePowerView.class), KeyEvent.VK_V, Up, Up, Up));
    editor.addAction(new InputAction(new CreateComponentAction(editor, SplitterView.class), KeyEvent.VK_S, Up, Down, Up));
    editor.addAction(new InputAction(new CreateComponentAction(editor, TunnelView.class), KeyEvent.VK_T, Up, Down, Up));
    editor.addAction(new InputAction(new CreateComponentAction(editor, LabelView.class), KeyEvent.VK_L, Up, Down, Up));

    editor.addAction(new InputAction(new EditPropertiesAction(editor, panel), KeyEvent.VK_E, Up, Up, Up));
    editor.addAction(new InputAction(new PlacementRotateLeft(editor), KeyEvent.VK_R, Up, Down, Up));
    editor.addAction(new InputAction(new PlacementRotateRight(editor), KeyEvent.VK_R, Up, Up, Up));
    editor.addAction(new InputAction(new ToggleRunSimulation(editor), KeyEvent.VK_K, Up, Up, Down));
    editor.addAction(new InputAction(new DeleteComponent(editor), KeyEvent.VK_DELETE, Up, Up, Up));
    editor.addAction(new InputAction(new IncreaseSimulationSpeed(editor), KeyEvent.VK_EQUALS, Up, Up, Up));
    editor.addAction(new InputAction(new DecreaseSimulationSpeed(editor), KeyEvent.VK_MINUS, Up, Up, Up));
    editor.addAction(new InputAction(new ResetSimulation(editor), KeyEvent.VK_R, Up, Up, Down));
    editor.addAction(new InputAction(new SaveSimulation(panel), KeyEvent.VK_S, Up, Up, Down));
    editor.addAction(new InputAction(new LoadSimulation(panel), KeyEvent.VK_L, Up, Up, Down));
    editor.addAction(new InputAction(new UndoAction(editor), KeyEvent.VK_Z, Up, Up, Down));
    editor.addAction(new InputAction(new RedoAction(editor), KeyEvent.VK_Y, Up, Up, Down));
  }
}

