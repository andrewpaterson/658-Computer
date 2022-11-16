package net.logicim.domain;

import net.logicim.common.collection.linkedlist.LinkedList;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.Event;
import net.logicim.domain.common.SimultaneousEvents;
import net.logicim.domain.common.TickEvent;
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

    Simulation simulation = circuit.resetSimulation();

    Map<Long, SimultaneousEvents> events = simulation.getTimeline().getAllEvents();
    validate(1, events.size());
    long eventTime = getMapKey(0, events);
    validate(nanosecondsToTime(1), eventTime);
    SimultaneousEvents simultaneousEvents = events.get(eventTime);
    validateNotNull(simultaneousEvents);
    validate(1, simultaneousEvents.size());
    Event treeTickEvent = simultaneousEvents.get(0);
    validateClass(TickEvent.class, treeTickEvent);
    Port constantOutput = constant.getPort("Output");
    LinkedList<PortEvent> portEventsForConstant = constantOutput.getEvents();
    validate(0, portEventsForConstant.size());

    Port inverterInput = inverter.getPort("Input");
    Port inverterOutput = inverter.getPort("Output");
    portEventsForConstant = inverterOutput.getEvents();
    validate(0, portEventsForConstant.size());

    boolean eventsProcessed = simulation.runSimultaneous();
    validateTrue(eventsProcessed);

    events = simulation.getTimeline().getAllEvents();
    validate(2, events.size());
    eventTime = getMapKey(0, events);
    validate(nanosecondsToTime(2), eventTime);
    simultaneousEvents = events.get(eventTime);
    validateNotNull(simultaneousEvents);
    validate(1, simultaneousEvents.size());
    Event treeSlewEvent = simultaneousEvents.get(0);
    validateClass(SlewEvent.class, treeSlewEvent);

    eventTime = getMapKey(1, events);
    validate(nanosecondsToTime(4), eventTime);
    simultaneousEvents = events.get(eventTime);
    validateNotNull(simultaneousEvents);
    validate(1, simultaneousEvents.size());
    Event treeDriveEvent = simultaneousEvents.get(0);
    validateClass(DriveEvent.class, treeDriveEvent);

    portEventsForConstant = constantOutput.getEvents();
    validate(2, portEventsForConstant.size());
    PortEvent portSlewEvent = portEventsForConstant.get(0);
    PortEvent portDriveEvent = portEventsForConstant.get(1);
    validateClass(SlewEvent.class, portSlewEvent);
    validateClass(DriveEvent.class, portDriveEvent);
    validate(nanosecondsToTime(2), portSlewEvent.getTime());
    validate(nanosecondsToTime(4), portDriveEvent.getTime());
    validate(treeSlewEvent, portSlewEvent);
    validate(treeDriveEvent, portDriveEvent);

    float voltage = constantOutput.getVoltage(simulation.getTime());
    validateTrue(Float.isNaN(voltage));

    eventsProcessed = simulation.runSimultaneous();
    validateTrue(eventsProcessed);

    events = simulation.getTimeline().getAllEvents();
    validate(2, events.size());
    eventTime = getMapKey(0, events);
    validate(3600, eventTime);
    simultaneousEvents = events.get(eventTime);
    validate(1, simultaneousEvents.size());
    Event treeTransitionEvent = simultaneousEvents.get(0);
    validateClass(TransitionEvent.class, treeTransitionEvent);
    eventTime = getMapKey(1, events);
    validate(nanosecondsToTime(4), eventTime);
    simultaneousEvents = events.get(eventTime);
    treeDriveEvent = simultaneousEvents.get(0);
    validateClass(DriveEvent.class, treeDriveEvent);

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

    LinkedList<PortEvent> portEventsForInverter = inverterOutput.getEvents();
    validate(2, portEventsForInverter.size());
    portSlewEvent = portEventsForInverter.get(0);
    validateClass(SlewEvent.class, portSlewEvent);
    portDriveEvent = portEventsForInverter.get(1);
    validateClass(DriveEvent.class, portDriveEvent);

    eventsProcessed = simulation.runSimultaneous();
    validateTrue(eventsProcessed);

    voltage = constantOutput.getVoltage(simulation.getTime());
    validateFalse(Float.isNaN(voltage));
    validate(0.0f, voltage);
    validate(0.0f, connectingTraceNet.getVoltage(simulation.getTime()));

    eventsProcessed = simulation.runSimultaneous();
    validateTrue(eventsProcessed);
    eventsProcessed = simulation.runSimultaneous();
    validateTrue(eventsProcessed);
    eventsProcessed = simulation.runSimultaneous();
    validateFalse(eventsProcessed);
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
    testClockOscillator();
  }
}

