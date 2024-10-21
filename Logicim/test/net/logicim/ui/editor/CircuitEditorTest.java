package net.logicim.ui.editor;

import net.common.type.Int2D;
import net.logicim.data.family.Family;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.SimultaneousEvents;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.Units;
import net.logicim.domain.common.event.Event;
import net.logicim.domain.common.event.TickEvent;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.common.propagation.FamilyVoltageConfigurationStore;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.DebugGlobalEnvironment;
import net.logicim.ui.simulation.component.integratedcircuit.standard.clock.ClockProperties;
import net.logicim.ui.simulation.component.integratedcircuit.standard.clock.ClockView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.clock.ClockViewFactory;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.Map;

import static net.logicim.assertions.Validator.validateEquals;

public class CircuitEditorTest
{
  public static void test()
  {
    FamilyVoltageConfigurationStore.getInstance();

    CircuitEditor circuitEditor = new CircuitEditor("Main");
    SubcircuitEditor subcircuitEditor = circuitEditor.getCurrentSubcircuitEditor();
    SubcircuitView subcircuitView = subcircuitEditor.getInstanceSubcircuitView();
    FamilyVoltageConfiguration voltageConfiguration = FamilyVoltageConfigurationStore.get("LVC");
    Family family = voltageConfiguration.getFamily();
    ClockView clockView = new ClockViewFactory().create(circuitEditor,
                                                        subcircuitView,
                                                        new Int2D(0, 0),
                                                        Rotation.North,
                                                        new ClockProperties("",
                                                                            family,
                                                                            false,
                                                                            100 * Units.MHz,
                                                                            false));

    SubcircuitSimulation subcircuitSimulation = circuitEditor.getSubcircuitSimulation();
    clockView.createComponent(subcircuitSimulation);
    clockView.simulationStarted();

    CircuitSimulation circuitSimulation = subcircuitSimulation.getCircuitSimulation();
    Timeline timeline = circuitSimulation.getSimulation().getTimeline();
    validateEquals(0L, timeline.getTime());
    Map<Long, SimultaneousEvents> eventsByTime = timeline.getAllEvents();
    validateEquals(1, eventsByTime.size());
    Map.Entry<Long, SimultaneousEvents> entry = eventsByTime.entrySet().iterator().next();
    long time = entry.getKey();
    validateEquals(5120L, time);
    SimultaneousEvents events = entry.getValue();
    validateEquals(1, events.size());
    Event event = events.getFirst();
    validateEquals(TickEvent.class, event.getClass());
  }

  public static void main(String[] args)
  {
    new DebugGlobalEnvironment();

    CircuitEditorTest.test();
  }
}

