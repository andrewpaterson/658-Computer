package net.logicim.domain;

import net.logicim.common.collection.linkedlist.LinkedList;
import net.logicim.domain.common.*;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.event.DriveEvent;
import net.logicim.domain.common.port.event.PortEvent;
import net.logicim.domain.common.port.event.SlewEvent;
import net.logicim.domain.common.port.event.TransitionEvent;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillator;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorPins;
import net.logicim.domain.integratedcircuit.standard.constant.Constant;
import net.logicim.domain.integratedcircuit.standard.constant.ConstantPins;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.BufferPins;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.Inverter;

import java.util.Map;

import static net.logicim.assertions.Validator.*;
import static net.logicim.domain.common.LongTime.nanosecondsToTime;
import static net.logicim.domain.common.Units.MHz;

public class SimulationTest
{
  private static void testClockOscillator()
  {
    Circuit circuit = new Circuit();

    ClockOscillator clock = new ClockOscillator(circuit, "10Mhz", new ClockOscillatorPins(), 10 * MHz);
    TraceNet clockTrace = new TraceNet();
    clock.getPins().getOutput().connect(clockTrace);

    Inverter inverter = new Inverter(circuit, "Not", new BufferPins());
    inverter.getPins().getInput().connect(clockTrace);
    inverter.getPins().getOutput().connect(new TraceNet());

    Simulation simulation = circuit.resetSimulation();
    simulation.runSimultaneous();
  }

  private static void testInverterEvents()
  {
    Circuit circuit = new Circuit();
    TraceNet connectingTraceNet = new TraceNet();
    TraceNet outputTraceNet = new TraceNet();
    Constant constant = new Constant(circuit, "Constant", new ConstantPins(), nanosecondsToTime(1), 0);
    constant.getPins().getOutput().connect(connectingTraceNet);
    Inverter inverter = new Inverter(circuit, "Not", new BufferPins());
    inverter.getPins().getInput().connect(connectingTraceNet);
    inverter.getPins().getOutput().connect(outputTraceNet);

    Port constantOutput = constant.getPort("Output");
    Port inverterInput = inverter.getPort("Input");
    Port inverterOutput = inverter.getPort("Output");

    Simulation simulation = circuit.resetSimulation();

    Map<Long, SimultaneousEvents> events = simulation.getTimeline().getAllEvents();
    validate(1, events.size());
    TickEvent treeTickEvent = validateTreeEvent(events, 0, nanosecondsToTime(1), TickEvent.class, constant);
    SimultaneousEvents simultaneousEvents;
    LinkedList<PortEvent> portEventsForConstant = constantOutput.getEvents();
    validate(0, portEventsForConstant.size());

    portEventsForConstant = inverterOutput.getEvents();
    validate(0, portEventsForConstant.size());

    boolean eventsProcessed = simulation.runSimultaneous();
    validateTrue(eventsProcessed);
    events = simulation.getTimeline().getAllEvents();
    validate(2, events.size());
    Event treeSlewEvent = validateTreeEvent(events, 0, nanosecondsToTime(2), SlewEvent.class, constant);
    Event treeDriveEvent = validateTreeEvent(events, 1, nanosecondsToTime(3), DriveEvent.class, constant);

    portEventsForConstant = constantOutput.getEvents();
    validate(2, portEventsForConstant.size());
    PortEvent portSlewEvent = portEventsForConstant.get(0);
    PortEvent portDriveEvent = portEventsForConstant.get(1);
    validateClass(SlewEvent.class, portSlewEvent);
    validateClass(DriveEvent.class, portDriveEvent);
    validate(nanosecondsToTime(2), portSlewEvent.getTime());
    validate(nanosecondsToTime(3), portDriveEvent.getTime());
    validate(treeSlewEvent, portSlewEvent);
    validate(treeDriveEvent, portDriveEvent);

    float voltage = constantOutput.getVoltage(simulation.getTime());
    validateTrue(Float.isNaN(voltage));

    eventsProcessed = simulation.runSimultaneous();
    validateTrue(eventsProcessed);
    events = simulation.getTimeline().getAllEvents();
    validate(2, events.size());
    validateTreeEvent(events, 0, 2576, TransitionEvent.class, inverter);
    validateTreeEvent(events, 1, nanosecondsToTime(3), DriveEvent.class, constant);

    portEventsForConstant = constantOutput.getEvents();
    validate(1, portEventsForConstant.size());
    portDriveEvent = portEventsForConstant.get(0);
    validateClass(DriveEvent.class, portDriveEvent);

    LinkedList<PortEvent> portEventsForInverterInput = inverterInput.getEvents();
    validate(1, portEventsForInverterInput.size());
    PortEvent portTransitionEvent = portEventsForInverterInput.get(0);
    validateClass(TransitionEvent.class, portTransitionEvent);

    LinkedList<PortEvent> portEventsForInverterOutput = inverterOutput.getEvents();
    validate(0, portEventsForInverterOutput.size());

    eventsProcessed = simulation.runSimultaneous();
    validateTrue(eventsProcessed);
    events = simulation.getTimeline().getAllEvents();
    validate(3, events.size());
    validateTreeEvent(events, 0, nanosecondsToTime(3), DriveEvent.class, constant);
    validateTreeEvent(events, 1, 3856, SlewEvent.class, inverter);
    validateTreeEvent(events, 2, 5136, DriveEvent.class, inverter);

    LinkedList<PortEvent> portEventsForInverter = inverterOutput.getEvents();
    validate(2, portEventsForInverter.size());
    portSlewEvent = portEventsForInverter.get(0);
    validateClass(SlewEvent.class, portSlewEvent);
    portDriveEvent = portEventsForInverter.get(1);
    validateClass(DriveEvent.class, portDriveEvent);

    eventsProcessed = simulation.runSimultaneous();
    validateTrue(eventsProcessed);
    events = simulation.getTimeline().getAllEvents();
    validate(2, events.size());
    validateTreeEvent(events, 0, 3856, SlewEvent.class, inverter);
    validateTreeEvent(events, 1, 5136, DriveEvent.class, inverter);

    voltage = constantOutput.getVoltage(simulation.getTime());
    validateFalse(Float.isNaN(voltage));
    validate(0.0f, voltage);
    validate(0.0f, connectingTraceNet.getVoltage(simulation.getTime()));
    validateTrue(Float.isNaN(inverterOutput.getVoltage(simulation.getTime())));
    validateTrue(Float.isNaN(outputTraceNet.getVoltage(simulation.getTime())));

    eventsProcessed = simulation.runSimultaneous();
    validateTrue(eventsProcessed);
    events = simulation.getTimeline().getAllEvents();
    validate(1, events.size());
    validateTreeEvent(events, 0, 5136, DriveEvent.class, inverter);
    voltage = constantOutput.getVoltage(simulation.getTime());
    validate(3856, simulation.getTime());
    validateFalse(Float.isNaN(voltage));
    validate(0.0f, voltage);
    validate(0.0f, connectingTraceNet.getVoltage(simulation.getTime()));

    eventsProcessed = simulation.runSimultaneous();
    validateTrue(eventsProcessed);
    events = simulation.getTimeline().getAllEvents();
    validate(0, events.size());
    validate(5136, simulation.getTime());
    validateFalse(Float.isNaN(voltage));
    validate(0.0f, voltage);
    validate(0.0f, connectingTraceNet.getVoltage(simulation.getTime()));

    eventsProcessed = simulation.runSimultaneous();
    validateFalse(eventsProcessed);
  }

  private static void testInverterLevels()
  {
    Circuit circuit = new Circuit();
    TraceNet connectingTraceNet = new TraceNet();
    TraceNet outputTraceNet = new TraceNet();
    Constant constant = new Constant(circuit, "Constant", new ConstantPins(), nanosecondsToTime(1), 0);
    constant.getPins().getOutput().connect(connectingTraceNet);
    Inverter inverter = new Inverter(circuit, "Not", new BufferPins());
    inverter.getPins().getInput().connect(connectingTraceNet);
    inverter.getPins().getOutput().connect(outputTraceNet);

    Port constantOutput = constant.getPort("Output");
    Port inverterInput = inverter.getPort("Input");
    Port inverterOutput = inverter.getPort("Output");
    Simulation simulation = circuit.resetSimulation();

    boolean processedEvent=true;
    while (processedEvent)
    {
      String connectingVoltage = connectingTraceNet.getVoltageString(simulation.getTime());
      String outputVoltage = outputTraceNet.getVoltageString(simulation.getTime());
      System.out.println(connectingVoltage + " " + outputVoltage);
      processedEvent = simulation.runToTime(100);
    }
  }

  private static <T> T validateTreeEvent(Map<Long, SimultaneousEvents> events, int treeIndex, int expectedEventTime, Class<?> expectedClass, IntegratedCircuit<?, ?> expectedIC)
  {
    long actualEventTime = getMapKey(treeIndex, events);
    validate(expectedEventTime, actualEventTime);
    SimultaneousEvents simultaneousEvents = events.get(actualEventTime);
    validateNotNull(simultaneousEvents);
    validate(1, simultaneousEvents.size());
    Event event = simultaneousEvents.get(0);
    validateClass(expectedClass, event);
    IntegratedCircuit<?, ?> actualIC = event.getIntegratedCircuit();
    validate(expectedIC, actualIC);
    return (T) event;
  }

  public static <K, V> K getMapKey(int index, Map<K, V> map)
  {
    int count = 0;
    for (K value : map.keySet())
    {
      if (count == index)
      {
        return value;
      }
      count++;
    }
    throw new ArrayIndexOutOfBoundsException(index);
  }

  public static void test()
  {
    testInverterEvents();
    testInverterLevels();
//    testClockOscillator();
  }
}

