package net.logicim.ui.util;

import net.logicim.ui.Logicim;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.wire.TunnelView;
import net.logicim.ui.editor.*;
import net.logicim.ui.input.action.KeyInput;
import net.logicim.ui.panels.SimulatorPanel;
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
  public static void createEditActions(Logicim editor, SimulatorPanel panel)
  {
    editor.addAction(StopEditAction.NAME, new StopEditAction(editor));
    editor.addAction(RunOneEventAction.NAME, new RunOneEventAction(editor));

    placeComponentAction(editor, ClockView.class);
    placeComponentAction(editor, InverterView.class);
    placeComponentAction(editor, OrGateView.class);
    placeComponentAction(editor, NorGateView.class);
    placeComponentAction(editor, AndGateView.class);
    placeComponentAction(editor, NandGateView.class);
    placeComponentAction(editor, XorGateView.class);
    placeComponentAction(editor, XnorGateView.class);
    placeComponentAction(editor, BufferView.class);
    placeComponentAction(editor, OscilloscopeView.class);
    placeComponentAction(editor, GroundView.class);
    placeComponentAction(editor, PositivePowerView.class);
    placeComponentAction(editor, SplitterView.class);
    placeComponentAction(editor, TunnelView.class);
    placeComponentAction(editor, LabelView.class);
    placeComponentAction(editor, PinView.class);

    editor.addAction(EditPropertiesAction.NAME, new EditPropertiesAction(editor, panel));
    editor.addAction(PlacementRotateLeftAction.NAME, new PlacementRotateLeftAction(editor));
    editor.addAction(PlacementRotateRightAction.NAME, new PlacementRotateRightAction(editor));
    editor.addAction(ToggleRunSimulationAction.NAME, new ToggleRunSimulationAction(editor));
    editor.addAction(DeleteAction.NAME, new DeleteAction(editor));
    editor.addAction(IncreaseSimulationSpeedAction.NAME, new IncreaseSimulationSpeedAction(editor));
    editor.addAction(DecreaseSimulationSpeedAction.NAME, new DecreaseSimulationSpeedAction(editor));
    editor.addAction(ResetSimulationAction.NAME, new ResetSimulationAction(editor));
    editor.addAction(SaveSimulationAction.NAME, new SaveSimulationAction(panel));
    editor.addAction(LoadSimulationAction.NAME, new LoadSimulationAction(panel));
    editor.addAction(UndoAction.NAME, new UndoAction(editor));
    editor.addAction(RedoAction.NAME, new RedoAction(editor));
    editor.addAction(ResetZoomAction.NAME, new ResetZoomAction(editor));

    editor.addAction(MoveAction.NAME, new MoveAction(editor));
    editor.addAction(CopyAction.NAME, new CopyAction(editor));
    editor.addAction(PasteAction.NAME, new PasteAction(editor));
    editor.addAction(CutAction.NAME, new CutAction(editor));
    editor.addAction(DuplicateAction.NAME, new DuplicateAction(editor));

    editor.addAction(NewSubcircuitAction.NAME, new NewSubcircuitAction(editor, panel));
    editor.addAction(PreviousSubcircuitAction.NAME, new PreviousSubcircuitAction(editor));
    editor.addAction(NextSubcircuitAction.NAME, new NextSubcircuitAction(editor));
    editor.addAction(LeaveSubcircuitAction.NAME, new LeaveSubcircuitAction(editor));
    editor.addAction(ReenterSubcircuitAction.NAME, new ReenterSubcircuitAction(editor));
    editor.addAction(EditSubcircuitAction.NAME, new EditSubcircuitAction(editor, panel));

    for (int i = 1; i < 10; i++)
    {
      editor.addAction(BookmarkSubcircuitAction.name(i), new BookmarkSubcircuitAction(editor, i));
      editor.addAction(GotoSubcircuitAction.name(i), new GotoSubcircuitAction(editor, i));
      editor.addAction(PlaceSubcircuitAction.name(i), new PlaceSubcircuitAction(editor, i));
    }
  }

  private static void placeComponentAction(Logicim editor, Class<? extends StaticView<?>> staticViewClass)
  {
    editor.addAction(PlaceComponentAction.name(staticViewClass), new PlaceComponentAction(editor, staticViewClass));
  }

  public static void create(Logicim editor, SimulatorPanel panel)
  {
    createEditActions(editor, panel);

    editor.addKeyInput(new KeyInput(editor.getAction("Discard Current Edit"), KeyEvent.VK_ESCAPE, DontCare, DontCare, DontCare));
    editor.addKeyInput(new KeyInput(editor.getAction("Step Once"), KeyEvent.VK_T, Up, Up, Up));

    editor.addKeyInput(new KeyInput(new PlaceComponentAction(editor, ClockView.class), KeyEvent.VK_C, Up, Up, Down));
    editor.addKeyInput(new KeyInput(new PlaceComponentAction(editor, InverterView.class), KeyEvent.VK_N, Up, Up, Down));
    editor.addKeyInput(new KeyInput(new PlaceComponentAction(editor, OrGateView.class), KeyEvent.VK_O, Up, Up, Down));
    editor.addKeyInput(new KeyInput(new PlaceComponentAction(editor, NorGateView.class), KeyEvent.VK_O, Down, Up, Down));
    editor.addKeyInput(new KeyInput(new PlaceComponentAction(editor, AndGateView.class), KeyEvent.VK_A, Up, Up, Down));
    editor.addKeyInput(new KeyInput(new PlaceComponentAction(editor, NandGateView.class), KeyEvent.VK_A, Down, Up, Down));
    editor.addKeyInput(new KeyInput(new PlaceComponentAction(editor, XorGateView.class), KeyEvent.VK_X, Up, Up, Down));
    editor.addKeyInput(new KeyInput(new PlaceComponentAction(editor, XnorGateView.class), KeyEvent.VK_X, Down, Up, Down));
    editor.addKeyInput(new KeyInput(new PlaceComponentAction(editor, BufferView.class), KeyEvent.VK_N, Down, Up, Down));
    editor.addKeyInput(new KeyInput(new PlaceComponentAction(editor, OscilloscopeView.class), KeyEvent.VK_P, Up, Up, Down));
    editor.addKeyInput(new KeyInput(new PlaceComponentAction(editor, GroundView.class), KeyEvent.VK_G, Up, Up, Up));
    editor.addKeyInput(new KeyInput(new PlaceComponentAction(editor, PositivePowerView.class), KeyEvent.VK_V, Up, Up, Up));
    editor.addKeyInput(new KeyInput(new PlaceComponentAction(editor, SplitterView.class), KeyEvent.VK_S, Up, Up, Down));
    editor.addKeyInput(new KeyInput(new PlaceComponentAction(editor, TunnelView.class), KeyEvent.VK_T, Up, Up, Down));
    editor.addKeyInput(new KeyInput(new PlaceComponentAction(editor, LabelView.class), KeyEvent.VK_L, Up, Up, Down));

    editor.addKeyInput(new KeyInput(new EditPropertiesAction(editor, panel), KeyEvent.VK_E, Up, Up, Up));
    editor.addKeyInput(new KeyInput(new PlacementRotateLeftAction(editor), KeyEvent.VK_R, Up, Up, Down));
    editor.addKeyInput(new KeyInput(new PlacementRotateRightAction(editor), KeyEvent.VK_R, Up, Up, Up));
    editor.addKeyInput(new KeyInput(new ToggleRunSimulationAction(editor), KeyEvent.VK_K, Up, Up, Up));
    editor.addKeyInput(new KeyInput(new DeleteAction(editor), KeyEvent.VK_DELETE, Up, Up, Up));
    editor.addKeyInput(new KeyInput(new IncreaseSimulationSpeedAction(editor), KeyEvent.VK_EQUALS, Up, Up, Up));
    editor.addKeyInput(new KeyInput(new DecreaseSimulationSpeedAction(editor), KeyEvent.VK_MINUS, Up, Up, Up));
    editor.addKeyInput(new KeyInput(new ResetSimulationAction(editor), KeyEvent.VK_R, Down, Up, Up));
    editor.addKeyInput(new KeyInput(new SaveSimulationAction(panel), KeyEvent.VK_S, Down, Up, Up));
    editor.addKeyInput(new KeyInput(new LoadSimulationAction(panel), KeyEvent.VK_L, Down, Up, Up));
    editor.addKeyInput(new KeyInput(new UndoAction(editor), KeyEvent.VK_Z, Down, Up, Up));
    editor.addKeyInput(new KeyInput(new RedoAction(editor), KeyEvent.VK_Y, Down, Up, Up));
    editor.addKeyInput(new KeyInput(new ResetZoomAction(editor), KeyEvent.VK_0, Down, Up, Up));

    editor.addKeyInput(new KeyInput(new MoveAction(editor), KeyEvent.VK_M, Up, Up, Up));
    editor.addKeyInput(new KeyInput(new CopyAction(editor), KeyEvent.VK_C, Down, Up, Up));
    editor.addKeyInput(new KeyInput(new PasteAction(editor), KeyEvent.VK_V, Down, Up, Up));
    editor.addKeyInput(new KeyInput(new CutAction(editor), KeyEvent.VK_X, Down, Up, Up));
    editor.addKeyInput(new KeyInput(new DuplicateAction(editor), KeyEvent.VK_D, Down, Up, Up));

    editor.addKeyInput(new KeyInput(new PlaceComponentAction(editor, PinView.class), KeyEvent.VK_P, Up, Up, Up));
    editor.addKeyInput(new KeyInput(new NewSubcircuitAction(editor, panel), KeyEvent.VK_ENTER, Up, Up, Down));
    editor.addKeyInput(new KeyInput(new PreviousSubcircuitAction(editor), KeyEvent.VK_LEFT, Up, Down, Up));
    editor.addKeyInput(new KeyInput(new NextSubcircuitAction(editor), KeyEvent.VK_RIGHT, Up, Down, Up));
    editor.addKeyInput(new KeyInput(new LeaveSubcircuitAction(editor), KeyEvent.VK_BACK_SPACE, Up, Down, Up));
    editor.addKeyInput(new KeyInput(new ReenterSubcircuitAction(editor), KeyEvent.VK_ENTER, Up, Down, Up));
    editor.addKeyInput(new KeyInput(new EditSubcircuitAction(editor, panel), KeyEvent.VK_E, Down, Up, Down));

    for (int i = 1; i < 10; i++)
    {
      editor.addKeyInput(new KeyInput(new BookmarkSubcircuitAction(editor, i), KeyEvent.VK_0 + i, Down, Up, Up));
      editor.addKeyInput(new KeyInput(new GotoSubcircuitAction(editor, i), KeyEvent.VK_0 + i, Up, Up, Up));
      editor.addKeyInput(new KeyInput(new PlaceSubcircuitAction(editor, i), KeyEvent.VK_0 + i, Up, Up, Down));
    }
  }
}

