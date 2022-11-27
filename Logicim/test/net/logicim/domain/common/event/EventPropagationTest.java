package net.logicim.domain.common.event;

import net.logicim.assertions.SmoothVoltage;
import net.logicim.assertions.Validator;
import net.logicim.data.circuit.CircuitData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.Voltage;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillator;
import net.logicim.domain.integratedcircuit.standard.logic.and.AndGate;
import net.logicim.file.LogicimFileReader;
import net.logicim.ui.CircuitEditor;

import java.util.List;

public class EventPropagationTest
{
  private static final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                                    "<Logicim>\n" +
                                    "  <circuitData Type=\"Circuit\">\n" +
                                    "    <timeline Type=\"Timeline\">\n" +
                                    "      <time>0</time>\n" +
                                    "      <previousEventTime>0</previousEventTime>\n" +
                                    "      <eventTime>0</eventTime>\n" +
                                    "    </timeline>\n" +
                                    "    <integratedCircuits Type=\"ArrayList\" size=\"6\">\n" +
                                    "      <element Type=\"Clock\" index=\"0\">\n" +
                                    "        <frequency>5.0E7</frequency>\n" +
                                    "        <state>false</state>\n" +
                                    "        <position Type=\"Int2D\" x=\"-11\" y=\"2\"/>\n" +
                                    "        <rotation Type=\"Rotation\" rotation=\"North\"/>\n" +
                                    "        <name/>\n" +
                                    "        <events Type=\"ArrayList\" size=\"1\">\n" +
                                    "          <element Type=\"TickEvent\" index=\"0\">\n" +
                                    "            <time>9728</time>\n" +
                                    "            <id>1884</id>\n" +
                                    "          </element>\n" +
                                    "        </events>\n" +
                                    "        <ports Type=\"ArrayList\" size=\"1\">\n" +
                                    "          <element Type=\"Port\" index=\"0\">\n" +
                                    "            <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                    "            <traceId>35</traceId>\n" +
                                    "          </element>\n" +
                                    "        </ports>\n" +
                                    "      </element>\n" +
                                    "      <element Type=\"XorGate\" index=\"1\">\n" +
                                    "        <inputCount>2</inputCount>\n" +
                                    "        <position Type=\"Int2D\" x=\"-13\" y=\"-6\"/>\n" +
                                    "        <rotation Type=\"Rotation\" rotation=\"North\"/>\n" +
                                    "        <name/>\n" +
                                    "        <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                    "        <ports Type=\"ArrayList\" size=\"3\">\n" +
                                    "          <element Type=\"Port\" index=\"0\">\n" +
                                    "            <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                    "            <traceId>33</traceId>\n" +
                                    "          </element>\n" +
                                    "          <element Type=\"Port\" index=\"1\">\n" +
                                    "            <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                    "            <traceId>35</traceId>\n" +
                                    "          </element>\n" +
                                    "          <element Type=\"Port\" index=\"2\">\n" +
                                    "            <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                    "            <traceId>24</traceId>\n" +
                                    "          </element>\n" +
                                    "        </ports>\n" +
                                    "      </element>\n" +
                                    "      <element Type=\"XnorGate\" index=\"2\">\n" +
                                    "        <inputCount>2</inputCount>\n" +
                                    "        <position Type=\"Int2D\" x=\"-9\" y=\"-6\"/>\n" +
                                    "        <rotation Type=\"Rotation\" rotation=\"North\"/>\n" +
                                    "        <name/>\n" +
                                    "        <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                    "        <ports Type=\"ArrayList\" size=\"3\">\n" +
                                    "          <element Type=\"Port\" index=\"0\">\n" +
                                    "            <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                    "            <traceId>35</traceId>\n" +
                                    "          </element>\n" +
                                    "          <element Type=\"Port\" index=\"1\">\n" +
                                    "            <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                    "            <traceId>29</traceId>\n" +
                                    "          </element>\n" +
                                    "          <element Type=\"Port\" index=\"2\">\n" +
                                    "            <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                    "            <traceId>27</traceId>\n" +
                                    "          </element>\n" +
                                    "        </ports>\n" +
                                    "      </element>\n" +
                                    "      <element Type=\"AndGate\" index=\"3\">\n" +
                                    "        <inputCount>2</inputCount>\n" +
                                    "        <position Type=\"Int2D\" x=\"-11\" y=\"-14\"/>\n" +
                                    "        <rotation Type=\"Rotation\" rotation=\"North\"/>\n" +
                                    "        <name/>\n" +
                                    "        <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                    "        <ports Type=\"ArrayList\" size=\"3\">\n" +
                                    "          <element Type=\"Port\" index=\"0\">\n" +
                                    "            <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                    "            <traceId>24</traceId>\n" +
                                    "          </element>\n" +
                                    "          <element Type=\"Port\" index=\"1\">\n" +
                                    "            <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                    "            <traceId>27</traceId>\n" +
                                    "          </element>\n" +
                                    "          <element Type=\"Port\" index=\"2\">\n" +
                                    "            <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                    "            <traceId>36</traceId>\n" +
                                    "          </element>\n" +
                                    "        </ports>\n" +
                                    "      </element>\n" +
                                    "      <element Type=\"Buffer\" index=\"4\">\n" +
                                    "        <position Type=\"Int2D\" x=\"-8\" y=\"-3\"/>\n" +
                                    "        <rotation Type=\"Rotation\" rotation=\"North\"/>\n" +
                                    "        <name/>\n" +
                                    "        <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                    "        <ports Type=\"ArrayList\" size=\"2\">\n" +
                                    "          <element Type=\"Port\" index=\"0\">\n" +
                                    "            <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                    "            <traceId>35</traceId>\n" +
                                    "          </element>\n" +
                                    "          <element Type=\"Port\" index=\"1\">\n" +
                                    "            <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                    "            <traceId>29</traceId>\n" +
                                    "          </element>\n" +
                                    "        </ports>\n" +
                                    "      </element>\n" +
                                    "      <element Type=\"Buffer\" index=\"5\">\n" +
                                    "        <position Type=\"Int2D\" x=\"-14\" y=\"-3\"/>\n" +
                                    "        <rotation Type=\"Rotation\" rotation=\"North\"/>\n" +
                                    "        <name/>\n" +
                                    "        <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                    "        <ports Type=\"ArrayList\" size=\"2\">\n" +
                                    "          <element Type=\"Port\" index=\"0\">\n" +
                                    "            <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                    "            <traceId>35</traceId>\n" +
                                    "          </element>\n" +
                                    "          <element Type=\"Port\" index=\"1\">\n" +
                                    "            <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                    "            <traceId>33</traceId>\n" +
                                    "          </element>\n" +
                                    "        </ports>\n" +
                                    "      </element>\n" +
                                    "    </integratedCircuits>\n" +
                                    "    <traces Type=\"ArrayList\" size=\"17\">\n" +
                                    "      <element Type=\"Trace\" index=\"0\">\n" +
                                    "        <id>35</id>\n" +
                                    "        <start Type=\"Int2D\" x=\"-10\" y=\"-4\"/>\n" +
                                    "        <end Type=\"Int2D\" x=\"-10\" y=\"-2\"/>\n" +
                                    "      </element>\n" +
                                    "      <element Type=\"Trace\" index=\"1\">\n" +
                                    "        <id>35</id>\n" +
                                    "        <start Type=\"Int2D\" x=\"-12\" y=\"-4\"/>\n" +
                                    "        <end Type=\"Int2D\" x=\"-12\" y=\"-2\"/>\n" +
                                    "      </element>\n" +
                                    "      <element Type=\"Trace\" index=\"2\">\n" +
                                    "        <id>35</id>\n" +
                                    "        <start Type=\"Int2D\" x=\"-12\" y=\"-2\"/>\n" +
                                    "        <end Type=\"Int2D\" x=\"-11\" y=\"-2\"/>\n" +
                                    "      </element>\n" +
                                    "      <element Type=\"Trace\" index=\"3\">\n" +
                                    "        <id>35</id>\n" +
                                    "        <start Type=\"Int2D\" x=\"-11\" y=\"-2\"/>\n" +
                                    "        <end Type=\"Int2D\" x=\"-10\" y=\"-2\"/>\n" +
                                    "      </element>\n" +
                                    "      <element Type=\"Trace\" index=\"4\">\n" +
                                    "        <id>24</id>\n" +
                                    "        <start Type=\"Int2D\" x=\"-12\" y=\"-13\"/>\n" +
                                    "        <end Type=\"Int2D\" x=\"-12\" y=\"-11\"/>\n" +
                                    "      </element>\n" +
                                    "      <element Type=\"Trace\" index=\"5\">\n" +
                                    "        <id>24</id>\n" +
                                    "        <start Type=\"Int2D\" x=\"-13\" y=\"-11\"/>\n" +
                                    "        <end Type=\"Int2D\" x=\"-12\" y=\"-11\"/>\n" +
                                    "      </element>\n" +
                                    "      <element Type=\"Trace\" index=\"6\">\n" +
                                    "        <id>24</id>\n" +
                                    "        <start Type=\"Int2D\" x=\"-13\" y=\"-11\"/>\n" +
                                    "        <end Type=\"Int2D\" x=\"-13\" y=\"-8\"/>\n" +
                                    "      </element>\n" +
                                    "      <element Type=\"Trace\" index=\"7\">\n" +
                                    "        <id>27</id>\n" +
                                    "        <start Type=\"Int2D\" x=\"-10\" y=\"-13\"/>\n" +
                                    "        <end Type=\"Int2D\" x=\"-10\" y=\"-11\"/>\n" +
                                    "      </element>\n" +
                                    "      <element Type=\"Trace\" index=\"8\">\n" +
                                    "        <id>27</id>\n" +
                                    "        <start Type=\"Int2D\" x=\"-10\" y=\"-11\"/>\n" +
                                    "        <end Type=\"Int2D\" x=\"-9\" y=\"-11\"/>\n" +
                                    "      </element>\n" +
                                    "      <element Type=\"Trace\" index=\"9\">\n" +
                                    "        <id>27</id>\n" +
                                    "        <start Type=\"Int2D\" x=\"-9\" y=\"-11\"/>\n" +
                                    "        <end Type=\"Int2D\" x=\"-9\" y=\"-9\"/>\n" +
                                    "      </element>\n" +
                                    "      <element Type=\"Trace\" index=\"10\">\n" +
                                    "        <id>35</id>\n" +
                                    "        <start Type=\"Int2D\" x=\"-8\" y=\"-2\"/>\n" +
                                    "        <end Type=\"Int2D\" x=\"-8\" y=\"-1\"/>\n" +
                                    "      </element>\n" +
                                    "      <element Type=\"Trace\" index=\"11\">\n" +
                                    "        <id>35</id>\n" +
                                    "        <start Type=\"Int2D\" x=\"-11\" y=\"-2\"/>\n" +
                                    "        <end Type=\"Int2D\" x=\"-11\" y=\"-1\"/>\n" +
                                    "      </element>\n" +
                                    "      <element Type=\"Trace\" index=\"12\">\n" +
                                    "        <id>35</id>\n" +
                                    "        <start Type=\"Int2D\" x=\"-11\" y=\"-1\"/>\n" +
                                    "        <end Type=\"Int2D\" x=\"-11\" y=\"1\"/>\n" +
                                    "      </element>\n" +
                                    "      <element Type=\"Trace\" index=\"13\">\n" +
                                    "        <id>35</id>\n" +
                                    "        <start Type=\"Int2D\" x=\"-11\" y=\"-1\"/>\n" +
                                    "        <end Type=\"Int2D\" x=\"-8\" y=\"-1\"/>\n" +
                                    "      </element>\n" +
                                    "      <element Type=\"Trace\" index=\"14\">\n" +
                                    "        <id>35</id>\n" +
                                    "        <start Type=\"Int2D\" x=\"-14\" y=\"-2\"/>\n" +
                                    "        <end Type=\"Int2D\" x=\"-14\" y=\"-1\"/>\n" +
                                    "      </element>\n" +
                                    "      <element Type=\"Trace\" index=\"15\">\n" +
                                    "        <id>35</id>\n" +
                                    "        <start Type=\"Int2D\" x=\"-14\" y=\"-1\"/>\n" +
                                    "        <end Type=\"Int2D\" x=\"-11\" y=\"-1\"/>\n" +
                                    "      </element>\n" +
                                    "      <element Type=\"Trace\" index=\"16\">\n" +
                                    "        <id>36</id>\n" +
                                    "        <start Type=\"Int2D\" x=\"-11\" y=\"-19\"/>\n" +
                                    "        <end Type=\"Int2D\" x=\"-11\" y=\"-16\"/>\n" +
                                    "      </element>\n" +
                                    "    </traces>\n" +
                                    "  </circuitData>\n" +
                                    "</Logicim>\n";

  private static void testEventPropagation()
  {
    CircuitEditor circuitEditor = new CircuitEditor();

    LogicimFileReader fileReader = new LogicimFileReader();
    CircuitData circuitData = fileReader.load(xml);
    circuitEditor.load(circuitData);
    Circuit circuit = circuitEditor.getCircuit();

    List<AndGate> andGates = (List) circuit.getIntegratedCircuits(AndGate.TYPE);
    Validator.validate(andGates.size(), 1);
    AndGate andGate = andGates.get(0);
    Port andOutput = andGate.getPins().getOutput();

    List<ClockOscillator> clocks = (List) circuit.getIntegratedCircuits(ClockOscillator.TYPE);
    Validator.validate(clocks.size(), 1);
    ClockOscillator clock = clocks.get(0);

    Simulation simulation = circuit.resetSimulation();

    while (true)
    {
      simulation.runToTime(100);
      float voltage1 = andOutput.getVoltage(simulation.getTime());
      if (!Float.isNaN(voltage1))
      {
        break;
      }
    }

    SmoothVoltage andGateVoltage = new SmoothVoltage(andOutput, 0.2f, simulation);

    do
    {
      andGateVoltage.validate();

      System.out.println(Voltage.getVoltageString(andOutput.getVoltage(simulation.getTime())));
      simulation.runToTime(100);
    }
    while (clock.getFullTicks() != 2);
  }

  public static void test()
  {
    testEventPropagation();
  }
}

