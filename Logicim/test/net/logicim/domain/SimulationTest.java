package net.logicim.domain;

import net.common.collection.linkedlist.LinkedList;
import net.logicim.assertions.SmoothVoltage;
import net.logicim.assertions.TraceSmoothVoltage;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.SimultaneousEvents;
import net.logicim.domain.common.event.Event;
import net.logicim.domain.common.event.TickEvent;
import net.logicim.domain.common.helper.TestPower;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.port.event.DriveEvent;
import net.logicim.domain.common.port.event.PortEvent;
import net.logicim.domain.common.port.event.SlewEvent;
import net.logicim.domain.common.port.event.TransitionEvent;
import net.logicim.domain.common.propagation.TestVoltageConfiguration;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillator;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorPins;
import net.logicim.domain.integratedcircuit.standard.constant.Constant;
import net.logicim.domain.integratedcircuit.standard.constant.ConstantPins;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.BufferPins;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.Inverter;
import net.logicim.domain.integratedcircuit.standard.logic.or.OrGate;
import net.logicim.domain.integratedcircuit.standard.logic.or.OrGatePins;
import net.logicim.domain.passive.subcircuit.SubcircuitTopSimulation;
import net.logicim.ui.simulation.DebugGlobalEnvironment;

import java.util.Map;

import static net.logicim.assertions.Validator.*;
import static net.logicim.domain.common.LongTime.frequencyToTime;
import static net.logicim.domain.common.LongTime.nanosecondsToTime;
import static net.logicim.domain.common.Units.MHz;

public class SimulationTest
{
  private static void testInverterEvents()
  {
    CircuitSimulation circuitSimulation = new CircuitSimulation("");
    SubcircuitTopSimulation subcircuitSimulation = circuitSimulation.getSubcircuitTopSimulation();
    Simulation simulation = circuitSimulation.getSimulation();

    TestPower testPower = new TestPower(subcircuitSimulation, 3.3f);

    Trace constantTrace = new Trace();
    Trace outputTrace = new Trace();
    Constant constant = new Constant(subcircuitSimulation, "Constant", new ConstantPins(new TestVoltageConfiguration(testPower.getVCC(),
                                                                                                                     0.8f,
                                                                                                                     2.0f,
                                                                                                                     testPower.getGND(),
                                                                                                                     testPower.getVCC(),
                                                                                                                     (int) nanosecondsToTime(2.0f),
                                                                                                                     (int) nanosecondsToTime(2.0f))),
                                     (int) nanosecondsToTime(1), 1);
    constant.getPins().getOutput().connect(constantTrace);
    testPower.connect(constant);

    Inverter inverter = new Inverter(subcircuitSimulation, "Not", new BufferPins(1, new TestVoltageConfiguration(testPower.getVCC(),
                                                                                                                 0.8f,
                                                                                                                 2.0f,
                                                                                                                 testPower.getGND(),
                                                                                                                 testPower.getVCC(),
                                                                                                                 (int) nanosecondsToTime(2.5f),
                                                                                                                 (int) nanosecondsToTime(2.5f))));
    inverter.getPins().getInputs().get(0).connect(constantTrace);
    inverter.getPins().getOutputs().get(0).connect(outputTrace);
    testPower.connect(inverter);

    LogicPort constantOutput = (LogicPort) constant.getPort("Output");
    LogicPort inverterInput = (LogicPort) inverter.getPort("Input");
    LogicPort inverterOutput = (LogicPort) inverter.getPort("Output");

    constant.reset(simulation);
    inverter.reset(simulation);

    Map<Long, SimultaneousEvents> events = simulation.getTimeline().getAllEvents();
    validate(1, events.size());
    validateTreeEvent(events, 0, nanosecondsToTime(1), TickEvent.class, constant);
    LinkedList<PortEvent> portEventsForConstant = constantOutput.getEvents();
    validate(0, portEventsForConstant.size());

    portEventsForConstant = inverterOutput.getEvents();
    validate(0, portEventsForConstant.size());

    boolean eventsProcessed = simulation.runSimultaneous();
    validateTrue(eventsProcessed);
    events = simulation.getTimeline().getAllEvents();
    validate(1, events.size());
    Event treeSlewEvent = validateTreeEvent(events, 0, nanosecondsToTime(2), SlewEvent.class, constant);

    portEventsForConstant = constantOutput.getEvents();
    validate(1, portEventsForConstant.size());
    PortEvent portSlewEvent = portEventsForConstant.get(0);
    validateClass(SlewEvent.class, portSlewEvent);
    validate(nanosecondsToTime(2), portSlewEvent.getTime());
    validateIdentity(treeSlewEvent, portSlewEvent);

    float voltage = constantOutput.getVoltageOut(simulation.getTime());
    validateTrue(Float.isNaN(voltage));

    eventsProcessed = simulation.runSimultaneous();
    validateTrue(eventsProcessed);
    events = simulation.getTimeline().getAllEvents();
    validate(2, events.size());
    validateTreeEvent(events, 0, 3290, TransitionEvent.class, inverter);
    validateTreeEvent(events, 1, nanosecondsToTime(4), DriveEvent.class, constant);

    portEventsForConstant = constantOutput.getEvents();
    validate(1, portEventsForConstant.size());
    PortEvent portDriveEvent = portEventsForConstant.get(0);
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

    validate(2, events.size());
    validateTreeEvent(events, 0, nanosecondsToTime(4), DriveEvent.class, constant);
    validateTreeEvent(events, 1, 4570, SlewEvent.class, inverter);

    LinkedList<PortEvent> portEventsForInverter = inverterOutput.getEvents();
    validate(1, portEventsForInverter.size());
    portSlewEvent = portEventsForInverter.get(0);
    validateClass(SlewEvent.class, portSlewEvent);

    eventsProcessed = simulation.runSimultaneous();
    validateTrue(eventsProcessed);
    events = simulation.getTimeline().getAllEvents();
    validate(1, events.size());
    validateTreeEvent(events, 0, 4570, SlewEvent.class, inverter);

    voltage = constantOutput.getVoltageOut(simulation.getTime());
    validateFalse(Float.isNaN(voltage));
    validate(3.3f, voltage);
    validate(3.3f, constantTrace.getVoltage(simulation.getTime()));
    validateTrue(Float.isNaN(inverterOutput.getVoltageOut(simulation.getTime())));
    validateTrue(Float.isNaN(outputTrace.getVoltage(simulation.getTime())));

    eventsProcessed = simulation.runSimultaneous();
    validateTrue(eventsProcessed);
    events = simulation.getTimeline().getAllEvents();
    validate(1, events.size());
    validateTreeEvent(events, 0, 4571, DriveEvent.class, inverter);
    voltage = constantOutput.getVoltageOut(simulation.getTime());
    validate(4570, simulation.getTime());
    validateFalse(Float.isNaN(voltage));
    validate(3.3f, voltage);
    validate(3.3f, constantTrace.getVoltage(simulation.getTime()));

    eventsProcessed = simulation.runSimultaneous();
    validateTrue(eventsProcessed);
    events = simulation.getTimeline().getAllEvents();
    validate(0, events.size());
    validate(4571, simulation.getTime());
    validateFalse(Float.isNaN(voltage));
    validate(3.3f, voltage);
    validate(3.3f, constantTrace.getVoltage(simulation.getTime()));

    eventsProcessed = simulation.runSimultaneous();
    validateFalse(eventsProcessed);
  }

  private static void testInverterLevels()
  {
    CircuitSimulation circuitSimulation = new CircuitSimulation("");
    SubcircuitTopSimulation subcircuitSimulation = circuitSimulation.getSubcircuitTopSimulation();
    Simulation simulation = circuitSimulation.getSimulation();

    TestPower testPower = new TestPower(subcircuitSimulation, 3.3f);

    Trace connectingTrace = new Trace();
    Trace outputTrace = new Trace();
    Constant constant = new Constant(subcircuitSimulation,
                                     "Constant",
                                     new ConstantPins(new TestVoltageConfiguration(testPower.getVCC(),
                                                                                   0.8f,
                                                                                   2.0f,
                                                                                   testPower.getGND(),
                                                                                   testPower.getVCC(),
                                                                                   (int) nanosecondsToTime(2.0f),
                                                                                   (int) nanosecondsToTime(2.0f))),
                                     (int) nanosecondsToTime(1),
                                     1);
    constant.getPins().getOutput().connect(connectingTrace);
    testPower.connect(constant);

    Inverter inverter = new Inverter(subcircuitSimulation,
                                     "Not",
                                     new BufferPins(1,
                                                    new TestVoltageConfiguration(testPower.getVCC(),
                                                                                 0.8f,
                                                                                 2.0f,
                                                                                 testPower.getGND(),
                                                                                 testPower.getVCC(),
                                                                                 (int) nanosecondsToTime(2.5f),
                                                                                 (int) nanosecondsToTime(2.5f))));
    inverter.getPins().getInputs().get(0).connect(connectingTrace);
    inverter.getPins().getOutputs().get(0).connect(outputTrace);
    testPower.connect(inverter);

    constant.reset(simulation);
    inverter.reset(simulation);

    StringBuilder builder = new StringBuilder();
    int step = 10;
    while (step > 0)
    {
      String connectingVoltage = connectingTrace.getVoltageString(simulation.getTime());
      String outputVoltage = outputTrace.getVoltageString(simulation.getTime());
      builder.append(connectingVoltage).append(" ").append(outputVoltage).append("\n");
      boolean processedEvent = simulation.runToTime(100);
      if (!processedEvent)
      {
        step--;
      }
    }
    validate("---- ----\n" +
             "---- ----\n" +
             "---- ----\n" +
             "---- ----\n" +
             "---- ----\n" +
             "---- ----\n" +
             "---- ----\n" +
             "---- ----\n" +
             "---- ----\n" +
             "---- ----\n" +
             "---- ----\n" +
             "---- ----\n" +
             "---- ----\n" +
             "---- ----\n" +
             "---- ----\n" +
             "---- ----\n" +
             "---- ----\n" +
             "---- ----\n" +
             "---- ----\n" +
             "---- ----\n" +
             "---- ----\n" +
             "0.1V ----\n" +
             "0.2V ----\n" +
             "0.4V ----\n" +
             "0.6V ----\n" +
             "0.7V ----\n" +
             "0.9V ----\n" +
             "1.1V ----\n" +
             "1.2V ----\n" +
             "1.4V ----\n" +
             "1.5V ----\n" +
             "1.7V ----\n" +
             "1.9V ----\n" +
             "2.0V ----\n" +
             "2.2V ----\n" +
             "2.3V ----\n" +
             "2.5V ----\n" +
             "2.7V ----\n" +
             "2.8V ----\n" +
             "3.0V ----\n" +
             "3.1V ----\n" +
             "3.3V ----\n" +
             "3.3V ----\n" +
             "3.3V ----\n" +
             "3.3V ----\n" +
             "3.3V ----\n" +
             "3.3V 0.0V\n" +
             "3.3V 0.0V\n" +
             "3.3V 0.0V\n" +
             "3.3V 0.0V\n" +
             "3.3V 0.0V\n" +
             "3.3V 0.0V\n" +
             "3.3V 0.0V\n" +
             "3.3V 0.0V\n" +
             "3.3V 0.0V\n",
             builder.toString());
  }

  private static void testClockOscillator()
  {
    CircuitSimulation circuitSimulation = new CircuitSimulation("");
    SubcircuitTopSimulation subcircuitSimulation = circuitSimulation.getSubcircuitTopSimulation();
    Simulation simulation = circuitSimulation.getSimulation();

    TestPower testPower = new TestPower(subcircuitSimulation, 3.3f);

    float frequency = 180 * MHz;
    ClockOscillator clock1 = new ClockOscillator(subcircuitSimulation,
                                                 frequency + "Mhz",
                                                 new ClockOscillatorPins(new TestVoltageConfiguration(testPower.getVCC(),
                                                                                                      0.8f,
                                                                                                      2.0f,
                                                                                                      testPower.getGND(),
                                                                                                      testPower.getVCC(),
                                                                                                      (int) nanosecondsToTime(2.0f),
                                                                                                      (int) nanosecondsToTime(2.0f)), false),
                                                 frequency,
                                                 (long) (frequencyToTime(frequency) * 0.5f));
    testPower.connect(clock1);

    ClockOscillator clock2 = new ClockOscillator(subcircuitSimulation,
                                                 frequency + "Mhz",
                                                 new ClockOscillatorPins(new TestVoltageConfiguration(testPower.getVCC(),
                                                                                                      0.8f,
                                                                                                      2.0f,
                                                                                                      testPower.getGND(),
                                                                                                      testPower.getVCC(),
                                                                                                      (int) nanosecondsToTime(2.0f),
                                                                                                      (int) nanosecondsToTime(2.0f)), false),
                                                 frequency,
                                                 (long) (frequencyToTime(frequency) * 0.6f));
    testPower.connect(clock2);

    OrGate orGate = new OrGate(subcircuitSimulation,
                               "Or",
                               new OrGatePins(2, new TestVoltageConfiguration(testPower.getVCC(),
                                                                              0.8f,
                                                                              2.0f,
                                                                              testPower.getGND(),
                                                                              testPower.getVCC(),
                                                                              (int) nanosecondsToTime(2.5f),
                                                                              (int) nanosecondsToTime(2.5f))));
    testPower.connect(orGate);

    Trace clock2Trace = new Trace();
    Trace clock1Trace = new Trace();
    Trace outputTrace = new Trace();

    clock2.getPins().getOutput().connect(clock2Trace);
    clock1.getPins().getOutput().connect(clock1Trace);

    orGate.getPins().getInput(0).connect(clock1Trace);
    orGate.getPins().getInput(1).connect(clock2Trace);
    orGate.getPins().getOutput().connect(outputTrace);

    clock1.reset(simulation);
    clock2.reset(simulation);
    orGate.reset(simulation);

    while (true)
    {
      simulation.runToTime(100);
      float voltage1 = clock1Trace.getVoltage(simulation.getTime());
      float voltage2 = clock2Trace.getVoltage(simulation.getTime());
      if (!Float.isNaN(voltage1) || !Float.isNaN(voltage2))
      {
        break;
      }
    }

    SmoothVoltage clock1Voltage = new TraceSmoothVoltage(clock1Trace, 0.2f, simulation);
    SmoothVoltage clock2Voltage = new TraceSmoothVoltage(clock2Trace, 0.2f, simulation);
    SmoothVoltage outputVoltage = new TraceSmoothVoltage(outputTrace, 0.2f, simulation);
    StringBuilder builder = new StringBuilder();
    do
    {
      clock1Voltage.validate();
      clock2Voltage.validate();
      outputVoltage.validate();
      builder.append(clock1Trace.getVoltageString(simulation.getTime()) + " " +
                     clock2Trace.getVoltageString(simulation.getTime()) + " " +
                     outputTrace.getVoltageString(simulation.getTime()) + "\n");
      simulation.runToTime(100);
    }
    while (clock2.getFullTicks() != 3);
    validate("0.1V ---- ----\n" +
             "0.2V ---- ----\n" +
             "0.4V ---- ----\n" +
             "0.5V ---- ----\n" +
             "0.7V ---- ----\n" +
             "0.9V ---- ----\n" +
             "1.0V 0.1V ----\n" +
             "1.2V 0.3V ----\n" +
             "1.3V 0.4V ----\n" +
             "1.5V 0.6V ----\n" +
             "1.7V 0.7V ----\n" +
             "1.8V 0.9V ----\n" +
             "2.0V 1.1V ----\n" +
             "2.1V 1.2V ----\n" +
             "2.3V 1.4V ----\n" +
             "2.5V 1.6V ----\n" +
             "2.6V 1.7V ----\n" +
             "2.8V 1.9V ----\n" +
             "3.0V 2.0V ----\n" +
             "3.1V 2.2V ----\n" +
             "3.3V 2.4V ----\n" +
             "3.3V 2.5V ----\n" +
             "3.3V 2.7V ----\n" +
             "3.3V 2.8V ----\n" +
             "3.3V 3.0V ----\n" +
             "3.3V 3.2V 0.0V\n" +
             "3.3V 3.3V 0.1V\n" +
             "3.3V 3.3V 0.3V\n" +
             "3.3V 3.3V 0.4V\n" +
             "3.2V 3.3V 0.5V\n" +
             "3.0V 3.3V 0.7V\n" +
             "2.8V 3.3V 0.8V\n" +
             "2.7V 3.3V 0.9V\n" +
             "2.5V 3.3V 1.0V\n" +
             "2.4V 3.3V 1.2V\n" +
             "2.2V 3.1V 1.3V\n" +
             "2.0V 2.9V 1.4V\n" +
             "1.9V 2.8V 1.6V\n" +
             "1.7V 2.6V 1.7V\n" +
             "1.5V 2.5V 1.8V\n" +
             "1.4V 2.3V 1.9V\n" +
             "1.2V 2.1V 2.1V\n" +
             "1.1V 2.0V 2.2V\n" +
             "0.9V 1.8V 2.3V\n" +
             "0.7V 1.7V 2.5V\n" +
             "0.6V 1.5V 2.6V\n" +
             "0.4V 1.3V 2.7V\n" +
             "0.3V 1.2V 2.8V\n" +
             "0.1V 1.0V 3.0V\n" +
             "0.0V 0.9V 3.1V\n" +
             "0.0V 0.7V 3.2V\n" +
             "0.0V 0.5V 3.3V\n" +
             "0.0V 0.4V 3.3V\n" +
             "0.0V 0.2V 3.3V\n" +
             "0.0V 0.0V 3.3V\n" +
             "0.0V 0.0V 3.3V\n" +
             "0.0V 0.0V 3.3V\n" +
             "0.1V 0.0V 3.2V\n" +
             "0.2V 0.0V 3.1V\n" +
             "0.4V 0.0V 3.0V\n" +
             "0.6V 0.0V 2.8V\n" +
             "0.7V 0.0V 2.7V\n" +
             "0.9V 0.0V 2.6V\n" +
             "1.0V 0.1V 2.5V\n" +
             "1.2V 0.3V 2.3V\n" +
             "1.4V 0.4V 2.2V\n" +
             "1.5V 0.6V 2.1V\n" +
             "1.7V 0.8V 1.9V\n" +
             "1.8V 0.9V 1.8V\n" +
             "2.0V 1.1V 1.7V\n" +
             "2.2V 1.3V 1.6V\n" +
             "2.3V 1.4V 1.4V\n" +
             "2.5V 1.6V 1.3V\n" +
             "2.6V 1.7V 1.2V\n" +
             "2.8V 1.9V 1.0V\n" +
             "3.0V 2.1V 0.9V\n" +
             "3.1V 2.2V 0.8V\n" +
             "3.3V 2.4V 0.6V\n" +
             "3.3V 2.5V 0.5V\n" +
             "3.3V 2.7V 0.4V\n" +
             "3.3V 2.9V 0.3V\n" +
             "3.3V 3.0V 0.1V\n" +
             "3.3V 3.2V 0.1V\n" +
             "3.3V 3.3V 0.2V\n" +
             "3.3V 3.3V 0.3V\n" +
             "3.3V 3.3V 0.4V\n" +
             "3.1V 3.3V 0.6V\n" +
             "3.0V 3.3V 0.7V\n" +
             "2.8V 3.3V 0.8V\n" +
             "2.7V 3.3V 1.0V\n" +
             "2.5V 3.3V 1.1V\n" +
             "2.3V 3.2V 1.2V\n" +
             "2.2V 3.1V 1.4V\n" +
             "2.0V 2.9V 1.5V\n" +
             "1.8V 2.8V 1.6V\n" +
             "1.7V 2.6V 1.7V\n" +
             "1.5V 2.4V 1.9V\n" +
             "1.4V 2.3V 2.0V\n" +
             "1.2V 2.1V 2.1V\n" +
             "1.0V 2.0V 2.3V\n" +
             "0.9V 1.8V 2.4V\n" +
             "0.7V 1.6V 2.5V\n" +
             "0.6V 1.5V 2.6V\n" +
             "0.4V 1.3V 2.8V\n" +
             "0.2V 1.2V 2.9V\n" +
             "0.1V 1.0V 3.0V\n" +
             "0.0V 0.8V 3.2V\n" +
             "0.0V 0.7V 3.3V\n" +
             "0.0V 0.5V 3.3V\n", builder.toString());
  }

  private static <T> T validateTreeEvent(Map<Long, SimultaneousEvents> events,
                                         int treeIndex,
                                         long expectedEventTime,
                                         Class<?> expectedClass,
                                         IntegratedCircuit<?, ?> expectedIC)
  {
    long actualEventTime = getMapKey(treeIndex, events);
    validate(expectedEventTime, actualEventTime);
    SimultaneousEvents simultaneousEvents = events.get(actualEventTime);
    validateNotNull(simultaneousEvents);
    validate(1, simultaneousEvents.size());
    Event event = simultaneousEvents.getFirst();
    validateClass(expectedClass, event);
    IntegratedCircuit<?, ?> actualIC = event.getIntegratedCircuit();
    validateIdentity(expectedIC, actualIC);
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
    new DebugGlobalEnvironment();

    testInverterEvents();
    testInverterLevels();
    testClockOscillator();
  }
}

