package net.logicim.ui.input;

import net.logicim.ui.Logicim;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.wire.TunnelView;
import net.logicim.ui.editor.*;
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

public class EditorActionsFactory
{
  public static void create(Logicim editor, SimulatorPanel panel)
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
    editor.addAction(PlacementFlipHorizontallyAction.NAME, new PlacementFlipHorizontallyAction(editor));
    editor.addAction(PlacementFlipVerticallyAction.NAME, new PlacementFlipVerticallyAction(editor));
    editor.addAction(ToggleRunSimulationAction.NAME, new ToggleRunSimulationAction(editor));
    editor.addAction(DeleteAction.NAME, new DeleteAction(editor));
    editor.addAction(IncreaseSimulationSpeedAction.NAME, new IncreaseSimulationSpeedAction(editor));
    editor.addAction(DecreaseSimulationSpeedAction.NAME, new DecreaseSimulationSpeedAction(editor));
    editor.addAction(NormalSpeedSimulationAction.NAME, new NormalSpeedSimulationAction(editor));
    editor.addAction(PauseSimulationAction.NAME, new PauseSimulationAction(editor));
    editor.addAction(RunSimulationAction.NAME, new RunSimulationAction(editor));
    editor.addAction(ResetSimulationAction.NAME, new ResetSimulationAction(editor));
    editor.addAction(RecreateSimulationAction.NAME, new RecreateSimulationAction(editor));
    editor.addAction(SaveSimulationAction.NAME, new SaveSimulationAction(editor, panel));
    editor.addAction(LoadSimulationAction.NAME, new LoadSimulationAction(editor, panel));
    editor.addAction(UndoAction.NAME, new UndoAction(editor));
    editor.addAction(RedoAction.NAME, new RedoAction(editor));
    editor.addAction(ZoomResetAction.NAME, new ZoomResetAction(editor));
    editor.addAction(ZoomFitSelectionAction.NAME, new ZoomFitSelectionAction(editor));
    editor.addAction(ZoomFitAllAction.NAME, new ZoomFitAllAction(editor));
    editor.addAction(ZoomInAction.NAME, new ZoomInAction(editor));
    editor.addAction(ZoomOutAction.NAME, new ZoomOutAction(editor));

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

    editor.addAction(TogglePointViewAction.NAME, new TogglePointViewAction(editor));
    editor.addAction(ViewSimulationTreeAction.NAME, new ViewSimulationTreeAction(editor, panel));
    editor.addAction(SelectSimulationAction.NAME, new SelectSimulationAction(editor, panel));

    editor.addAction(AddComponentAction.NAME, new AddComponentAction(editor, panel));
    editor.addAction(AddConnectionAction.NAME, new AddConnectionAction(editor));
    editor.addAction(AddLabelAction.NAME, new AddLabelAction(editor, panel));
    editor.addAction(AddPowerAction.NAME, new AddPowerAction(editor, panel));
    editor.addAction(AddSplitterAction.NAME, new AddSplitterAction(editor, panel));
    editor.addAction(AddSubcircuitAction.NAME, new AddSubcircuitAction(editor, panel));
    editor.addAction(AddTraceAction.NAME, new AddTraceAction(editor));
    editor.addAction(AddTunnelAction.NAME, new AddTunnelAction(editor, panel));
  }

  private static void placeComponentAction(Logicim editor, Class<? extends StaticView<?>> staticViewClass)
  {
    editor.addAction(PlaceComponentAction.name(staticViewClass), new PlaceComponentAction(editor, staticViewClass));
  }
}

