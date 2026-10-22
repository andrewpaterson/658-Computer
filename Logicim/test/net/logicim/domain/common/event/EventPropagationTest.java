package net.logicim.domain.common.event;

import net.logicim.assertions.PortSmoothVoltage;
import net.logicim.assertions.SmoothVoltage;
import net.logicim.assertions.Validator;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.helper.TestPower;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.propagation.TestVoltageConfiguration;
import net.logicim.domain.common.voltage.Voltage;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillator;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorPins;
import net.logicim.domain.integratedcircuit.standard.logic.and.AndGate;
import net.logicim.domain.integratedcircuit.standard.logic.and.AndGatePins;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.Buffer;
import net.logicim.domain.integratedcircuit.standard.logic.buffer.BufferPins;
import net.logicim.domain.integratedcircuit.standard.logic.xor.XnorGate;
import net.logicim.domain.integratedcircuit.standard.logic.xor.XorGate;
import net.logicim.domain.integratedcircuit.standard.logic.xor.XorGatePins;
import net.logicim.domain.passive.subcircuit.SubcircuitTopSimulation;
import net.logicim.ui.simulation.DebugGlobalEnvironment;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static net.logicim.domain.common.LongTime.nanosecondsToTime;
import static net.logicim.domain.common.Units.MHz;

public class EventPropagationTest
{
  @BeforeAll
  public static void setUp()
  {
    new DebugGlobalEnvironment();
  }

  //  Clock --+------------------+-------------+
  //          |                  |             |
  //          +-- Buffer --+     +-- Buffer ---+
  //                       |                   |
  //                 XOR(clock, clock')   XNOR(clock, clock')
  //                       +------ AND --------+
  //
  //  Ideally the AND output is always low.  Propagation delays let glitches through on each clock edge.
  @Test
  public void testEventPropagation()
  {
    CircuitSimulation circuitSimulation = new CircuitSimulation("");
    circuitSimulation.setTopSimulation(new SubcircuitTopSimulation(circuitSimulation));
    SubcircuitTopSimulation subcircuitSimulation = circuitSimulation.getSubcircuitTopSimulation();
    Simulation simulation = circuitSimulation.getSimulation();

    TestPower testPower = new TestPower(subcircuitSimulation, 3.3f);

    ClockOscillator clock = new ClockOscillator(subcircuitSimulation,
                                                "Clock",
                                                new ClockOscillatorPins(createVoltageConfiguration(testPower, 2.0f), false),
                                                50 * MHz);
    Buffer xorBuffer = new Buffer(subcircuitSimulation, "XorBuffer", new BufferPins(1, createVoltageConfiguration(testPower, 2.5f)));
    Buffer xnorBuffer = new Buffer(subcircuitSimulation, "XnorBuffer", new BufferPins(1, createVoltageConfiguration(testPower, 2.5f)));
    XorGate xorGate = new XorGate(subcircuitSimulation, "Xor", new XorGatePins(2, createVoltageConfiguration(testPower, 2.5f)));
    XnorGate xnorGate = new XnorGate(subcircuitSimulation, "Xnor", new XorGatePins(2, createVoltageConfiguration(testPower, 2.5f)));
    AndGate andGate = new AndGate(subcircuitSimulation, "And", new AndGatePins(2, createVoltageConfiguration(testPower, 2.5f)));

    testPower.connect(clock);
    testPower.connect(xorBuffer);
    testPower.connect(xnorBuffer);
    testPower.connect(xorGate);
    testPower.connect(xnorGate);
    testPower.connect(andGate);

    Trace clockTrace = new Trace();
    Trace xorBufferTrace = new Trace();
    Trace xnorBufferTrace = new Trace();
    Trace xorTrace = new Trace();
    Trace xnorTrace = new Trace();
    Trace andTrace = new Trace();

    clock.getPins().getOutput().connect(clockTrace);

    xorBuffer.getPins().getInputs().get(0).connect(clockTrace);
    xorBuffer.getPins().getOutputs().get(0).connect(xorBufferTrace);
    xnorBuffer.getPins().getInputs().get(0).connect(clockTrace);
    xnorBuffer.getPins().getOutputs().get(0).connect(xnorBufferTrace);

    xorGate.getPins().getInputs().get(0).connect(xorBufferTrace);
    xorGate.getPins().getInputs().get(1).connect(clockTrace);
    xorGate.getPins().getOutput().connect(xorTrace);

    xnorGate.getPins().getInputs().get(0).connect(clockTrace);
    xnorGate.getPins().getInputs().get(1).connect(xnorBufferTrace);
    xnorGate.getPins().getOutput().connect(xnorTrace);

    andGate.getPins().getInputs().get(0).connect(xorTrace);
    andGate.getPins().getInputs().get(1).connect(xnorTrace);
    andGate.getPins().getOutput().connect(andTrace);

    clock.reset(simulation);
    xorBuffer.reset(simulation);
    xnorBuffer.reset(simulation);
    xorGate.reset(simulation);
    xnorGate.reset(simulation);
    andGate.reset(simulation);

    LogicPort andOutput = andGate.getPins().getOutput();
    while (true)
    {
      simulation.runToTime(100);
      float voltage = andOutput.getVoltageOut(simulation.getTime());
      if (!Float.isNaN(voltage))
      {
        break;
      }
    }

    SmoothVoltage andGateSmoothVoltage = new PortSmoothVoltage(andOutput, 0.3f, simulation);

    String simulationResult = runSimulation(andOutput, clock, simulation, andGateSmoothVoltage);
    Validator.validate("Time [16700]  0.0V\n" +
                       "              ...\n" +
                       "Time [19550]  0.1V\n" +
                       "Time [19700]  0.3V\n" +
                       "Time [19850]  0.5V\n" +
                       "Time [20000]  0.3V\n" +
                       "Time [20150]  0.1V\n" +
                       "Time [20300]  0.0V\n" +
                       "              ...\n" +
                       "Time [27200]  0.0V\n" +
                       "Time [27350]  0.2V\n" +
                       "Time [27500]  0.4V\n" +
                       "Time [27650]  0.4V\n" +
                       "Time [27800]  0.2V\n" +
                       "Time [27950]  0.0V\n" +
                       "              ...\n" +
                       "Time [30500]  0.1V\n" +
                       "Time [30650]  0.3V\n" +
                       "Time [30800]  0.5V\n" +
                       "Time [30950]  0.3V\n" +
                       "Time [31100]  0.1V\n" +
                       "Time [31250]  0.0V\n" +
                       "              ...\n" +
                       "Time [37100]  0.0V\n" +
                       "Time [37250]  0.2V\n" +
                       "Time [37400]  0.4V\n" +
                       "Time [37550]  0.4V\n" +
                       "Time [37700]  0.2V\n" +
                       "Time [37850]  0.0V\n" +
                       "Time [38000]  0.0V\n" +
                       "              ...\n" +
                       "Time [39950]  0.0V\n" +
                       "Time [40100]  0.2V\n" +
                       "Time [40250]  0.4V\n" +
                       "Time [40400]  0.4V\n" +
                       "Time [40550]  0.2V\n" +
                       "Time [40700]  0.0V\n" +
                       "Time [40850]  0.0V\n" +
                       "              ...\n" +
                       "Time [47750]  0.1V\n" +
                       "Time [47900]  0.3V\n" +
                       "Time [48050]  0.5V\n" +
                       "Time [48200]  0.3V\n" +
                       "Time [48350]  0.1V\n" +
                       "Time [48500]  0.0V\n" +
                       "              ...\n" +
                       "Time [50900]  0.0V\n" +
                       "Time [51050]  0.2V\n" +
                       "Time [51200]  0.4V\n" +
                       "Time [51350]  0.4V\n" +
                       "Time [51500]  0.2V\n" +
                       "Time [51650]  0.0V\n" +
                       "              ...\n" +
                       "Time [57650]  0.1V\n" +
                       "Time [57800]  0.3V\n" +
                       "Time [57950]  0.5V\n" +
                       "Time [58100]  0.3V\n" +
                       "Time [58250]  0.1V\n" +
                       "Time [58400]  0.0V\n" +
                       "              ...\n" +
                       "Time [60500]  0.1V\n" +
                       "Time [60650]  0.3V\n" +
                       "Time [60800]  0.5V\n" +
                       "Time [60950]  0.3V\n" +
                       "Time [61100]  0.1V\n" +
                       "Time [61250]  0.0V\n" +
                       "              ...\n" +
                       "Time [68150]  0.0V\n" +
                       "Time [68300]  0.2V\n" +
                       "Time [68450]  0.4V\n" +
                       "Time [68600]  0.4V\n" +
                       "Time [68750]  0.2V\n" +
                       "Time [68900]  0.0V\n" +
                       "Time [69050]  0.0V\n" +
                       "              ...\n" +
                       "Time [71450]  0.1V\n" +
                       "Time [71600]  0.3V\n",
                       simulationResult);
  }

  private static TestVoltageConfiguration createVoltageConfiguration(TestPower testPower, float propagationNanoseconds)
  {
    return new TestVoltageConfiguration(testPower.getVCC(),
                                        0.8f,
                                        2.0f,
                                        testPower.getGND(),
                                        testPower.getVCC(),
                                        (int) nanosecondsToTime(propagationNanoseconds),
                                        (int) nanosecondsToTime(propagationNanoseconds));
  }

  private static String runSimulation(LogicPort testPort, ClockOscillator clock, Simulation simulation, SmoothVoltage smoothVoltage)
  {
    StringBuilder builder = new StringBuilder();
    float previousVoltage = Float.NaN;
    do
    {
      smoothVoltage.validate();

      float voltage = testPort.getVoltageOut(simulation.getTime());
      if ((voltage != 0.0f) || (previousVoltage != 0.0f))
      {
        builder.append("Time [" + simulation.getTime() + "]  " + Voltage.toVoltageString(voltage) + "\n");
      }
      if ((voltage == 0.0f) && (previousVoltage != 0.0f))
      {
        builder.append("              ...\n");
      }
      previousVoltage = voltage;
      simulation.runToTime(150);
    }
    while (clock.getFullTicks() != 4);
    return builder.toString();
  }
}
