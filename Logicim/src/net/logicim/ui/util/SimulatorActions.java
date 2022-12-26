package net.logicim.ui.util;

import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.SimulatorPanel;
import net.logicim.ui.editor.*;
import net.logicim.ui.input.action.InputAction;
import net.logicim.ui.integratedcircuit.extra.OscilloscopeViewFactory;
import net.logicim.ui.integratedcircuit.standard.clock.ClockViewFactory;
import net.logicim.ui.integratedcircuit.standard.logic.and.AndGateViewFactory;
import net.logicim.ui.integratedcircuit.standard.logic.and.NandGateViewFactory;
import net.logicim.ui.integratedcircuit.standard.logic.buffer.BufferViewFactory;
import net.logicim.ui.integratedcircuit.standard.logic.buffer.InverterViewFactory;
import net.logicim.ui.integratedcircuit.standard.logic.or.NorGateViewFactory;
import net.logicim.ui.integratedcircuit.standard.logic.or.OrGateViewFactory;
import net.logicim.ui.integratedcircuit.standard.logic.xor.XnorGateViewFactory;
import net.logicim.ui.integratedcircuit.standard.logic.xor.XorGateViewFactory;
import net.logicim.ui.integratedcircuit.standard.power.GroundViewFactory;
import net.logicim.ui.integratedcircuit.standard.power.PositivePowerViewFactory;

import java.awt.event.KeyEvent;

import static net.logicim.ui.input.action.ButtonState.*;

public class SimulatorActions
{
  public static void create(SimulatorEditor editor, SimulatorPanel panel)
  {
    editor.addAction(new InputAction(new PlacementRotateLeft(editor), KeyEvent.VK_R, Up, Down, Up));
    editor.addAction(new InputAction(new PlacementRotateRight(editor), KeyEvent.VK_R, Up, Up, Up));
    editor.addAction(new InputAction(new StopCurrent(editor), KeyEvent.VK_ESCAPE, DontCare, DontCare, DontCare));
    editor.addAction(new InputAction(new RunOneEvent(editor), KeyEvent.VK_T, Up, Up, Up));
    editor.addAction(new InputAction(new CreatePlacementView(editor, new ClockViewFactory()), KeyEvent.VK_C, Up, Down, Up));
    editor.addAction(new InputAction(new CreatePlacementView(editor, new InverterViewFactory()), KeyEvent.VK_N, Up, Down, Up));
    editor.addAction(new InputAction(new CreatePlacementView(editor, new OrGateViewFactory()), KeyEvent.VK_O, Up, Down, Up));
    editor.addAction(new InputAction(new CreatePlacementView(editor, new NorGateViewFactory()), KeyEvent.VK_O, Up, Down, Down));
    editor.addAction(new InputAction(new CreatePlacementView(editor, new AndGateViewFactory()), KeyEvent.VK_A, Up, Down, Up));
    editor.addAction(new InputAction(new CreatePlacementView(editor, new NandGateViewFactory()), KeyEvent.VK_A, Up, Down, Down));
    editor.addAction(new InputAction(new CreatePlacementView(editor, new XorGateViewFactory()), KeyEvent.VK_X, Up, Down, Up));
    editor.addAction(new InputAction(new CreatePlacementView(editor, new XnorGateViewFactory()), KeyEvent.VK_X, Up, Down, Down));
    editor.addAction(new InputAction(new CreatePlacementView(editor, new BufferViewFactory()), KeyEvent.VK_N, Up, Down, Down));
    editor.addAction(new InputAction(new CreatePlacementView(editor, new OscilloscopeViewFactory(editor.getColours())), KeyEvent.VK_P, Up, Down, Up));
    editor.addAction(new InputAction(new ToggleRunSimulation(editor), KeyEvent.VK_K, Up, Up, Down));
    editor.addAction(new InputAction(new DeleteComponent(editor), KeyEvent.VK_DELETE, Up, Up, Up));
    editor.addAction(new InputAction(new IncreaseSimulationSpeed(editor), KeyEvent.VK_EQUALS, Up, Up, Up));
    editor.addAction(new InputAction(new DecreaseSimulationSpeed(editor), KeyEvent.VK_MINUS, Up, Up, Up));
    editor.addAction(new InputAction(new ResetSimulation(editor), KeyEvent.VK_R, Up, Up, Down));
    editor.addAction(new InputAction(new SaveSimulation(panel), KeyEvent.VK_S, Up, Up, Down));
    editor.addAction(new InputAction(new LoadSimulation(panel), KeyEvent.VK_L, Up, Up, Down));
    editor.addAction(new InputAction(new CreatePlacementView(editor, new GroundViewFactory()), KeyEvent.VK_G, Up, Up, Up));
    editor.addAction(new InputAction(new CreatePlacementView(editor, new PositivePowerViewFactory()), KeyEvent.VK_V, Up, Up, Up));
    editor.addAction(new InputAction(new EditParameters(editor, panel.getFrame()), KeyEvent.VK_E, Up, Up, Up));
  }
}

