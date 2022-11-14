package net.logicim.domain;

import net.logicim.common.collection.linkedlist.LinkedList;
import net.logicim.domain.common.*;
import net.logicim.domain.common.port.Drive;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.event.PortEvent;
import net.logicim.domain.common.port.event.UniportDriveEvent;
import net.logicim.domain.common.port.event.UniportSlewEvent;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillator;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorPins;
import net.logicim.domain.integratedcircuit.standard.constant.Constant;
import net.logicim.domain.integratedcircuit.standard.constant.ConstantPins;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.BufferPins;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.Inverter;

import java.util.Map;

import static net.logicim.assertions.Validator.*;
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
    Constant constant = new Constant(circuit, "Constant", new ConstantPins((byte) 1), LongTime.nanosecondsToTime(1), 0);
    constant.getPins().getOutput().connect(connectingTraceNet);
    Inverter inverter = new Inverter(circuit, "Not", new BufferPins());
    inverter.getPins().getInput().connect(connectingTraceNet);
    inverter.getPins().getOutput().connect(outputTraceNet);

    Simulation simulation = circuit.resetSimulation();

    Map<Long, SimultaneousEvents> events = simulation.getTimeline().getAllEvents();
    validate(1, events.size());
    long eventTime = getMapKey(0, events);
    validate(LongTime.nanosecondsToTime(1), eventTime);
    SimultaneousEvents simultaneousEvents = events.get(eventTime);
    validateNotNull(simultaneousEvents);
    validate(1, simultaneousEvents.size());
    Event treeTickEvent = simultaneousEvents.getEvents().get(0);
    validateClass(TickEvent.class, treeTickEvent);
    Port constantOutput = constant.getPort("Output");
    LinkedList<PortEvent> portEvents = constantOutput.getEvents();
    validate(0, portEvents.size());

    Port inverterOutput = inverter.getPort("Output");
    portEvents = inverterOutput.getEvents();
    validate(0, portEvents.size());

    simulation.runSimultaneous();

    events = simulation.getTimeline().getAllEvents();
    validate(2, events.size());
    eventTime = getMapKey(0, events);
    validate(LongTime.nanosecondsToTime(2), eventTime);
    simultaneousEvents = events.get(eventTime);
    validateNotNull(simultaneousEvents);
    validate(1, simultaneousEvents.size());
    Event treeSlewEvent = simultaneousEvents.getEvents().get(0);
    validateClass(UniportSlewEvent.class, treeSlewEvent);

    eventTime = getMapKey(1, events);
    validate(LongTime.nanosecondsToTime(4), eventTime);
    simultaneousEvents = events.get(eventTime);
    validateNotNull(simultaneousEvents);
    validate(1, simultaneousEvents.size());
    Event treeDriveEvent = simultaneousEvents.getEvents().get(0);
    validateClass(UniportDriveEvent.class, treeDriveEvent);

    portEvents = constantOutput.getEvents();
    validate(2, portEvents.size());
    PortEvent portSlewEvent = portEvents.get(0);
    PortEvent portDriveEvent = portEvents.get(1);
    validateClass(UniportSlewEvent.class, portSlewEvent);
    validateClass(UniportDriveEvent.class, portDriveEvent);
    validate(LongTime.nanosecondsToTime(2), portSlewEvent.getTime());
    validate(LongTime.nanosecondsToTime(4), portDriveEvent.getTime());
    validate(treeSlewEvent, portSlewEvent);
    validate(treeDriveEvent, portDriveEvent);

    Drive drive = constantOutput.getDrive(connectingTraceNet);
    validateFalse(drive.isDriven());

    simulation.runSimultaneous();

    events = simulation.getTimeline().getAllEvents();
    validate(1, events.size());

    portEvents = constantOutput.getEvents();
    validate(0, portEvents.size());
    //Drive event is still in the port list because the transition event happens before it.

    drive = constantOutput.getDrive(connectingTraceNet);
    validateTrue(drive.isDriven());
    validate(drive.getVoltage(), 0.0f);
    validate(connectingTraceNet.getDrivenVoltage(), 0.0f);

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

