package net.logicim.domain.common.event;

public class MultipleXORIntoAndSourceXML
{
  public static final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                                   "<Logicim>\n" +
                                   "  <editorData Type=\"Editor\">\n" +
                                   "    <circuitData Type=\"Circuit\">\n" +
                                   "      <timeline Type=\"Timeline\">\n" +
                                   "        <time>0</time>\n" +
                                   "        <previousEventTime>0</previousEventTime>\n" +
                                   "        <eventTime>0</eventTime>\n" +
                                   "      </timeline>\n" +
                                   "      <integratedCircuits Type=\"ArrayList\" size=\"6\">\n" +
                                   "        <element Type=\"Clock\" index=\"0\">\n" +
                                   "          <frequency>5.0E7</frequency>\n" +
                                   "          <state>false</state>\n" +
                                   "          <position Type=\"Int2D\" x=\"-11\" y=\"2\"/>\n" +
                                   "          <rotation Type=\"Rotation\" rotation=\"North\"/>\n" +
                                   "          <name/>\n" +
                                   "          <events Type=\"ArrayList\" size=\"1\">\n" +
                                   "            <element Type=\"TickEvent\" index=\"0\">\n" +
                                   "              <time>9728</time>\n" +
                                   "              <id>1884</id>\n" +
                                   "            </element>\n" +
                                   "          </events>\n" +
                                   "          <ports Type=\"ArrayList\" size=\"1\">\n" +
                                   "            <element Type=\"Port\" index=\"0\">\n" +
                                   "              <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                   "              <traceId>35</traceId>\n" +
                                   "            </element>\n" +
                                   "          </ports>\n" +
                                   "        </element>\n" +
                                   "        <element Type=\"XorGate\" index=\"1\">\n" +
                                   "          <inputCount>2</inputCount>\n" +
                                   "          <position Type=\"Int2D\" x=\"-13\" y=\"-6\"/>\n" +
                                   "          <rotation Type=\"Rotation\" rotation=\"North\"/>\n" +
                                   "          <name/>\n" +
                                   "          <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                   "          <ports Type=\"ArrayList\" size=\"3\">\n" +
                                   "            <element Type=\"Port\" index=\"0\">\n" +
                                   "              <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                   "              <traceId>33</traceId>\n" +
                                   "            </element>\n" +
                                   "            <element Type=\"Port\" index=\"1\">\n" +
                                   "              <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                   "              <traceId>35</traceId>\n" +
                                   "            </element>\n" +
                                   "            <element Type=\"Port\" index=\"2\">\n" +
                                   "              <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                   "              <traceId>24</traceId>\n" +
                                   "            </element>\n" +
                                   "          </ports>\n" +
                                   "        </element>\n" +
                                   "        <element Type=\"XnorGate\" index=\"2\">\n" +
                                   "          <inputCount>2</inputCount>\n" +
                                   "          <position Type=\"Int2D\" x=\"-9\" y=\"-6\"/>\n" +
                                   "          <rotation Type=\"Rotation\" rotation=\"North\"/>\n" +
                                   "          <name/>\n" +
                                   "          <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                   "          <ports Type=\"ArrayList\" size=\"3\">\n" +
                                   "            <element Type=\"Port\" index=\"0\">\n" +
                                   "              <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                   "              <traceId>35</traceId>\n" +
                                   "            </element>\n" +
                                   "            <element Type=\"Port\" index=\"1\">\n" +
                                   "              <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                   "              <traceId>29</traceId>\n" +
                                   "            </element>\n" +
                                   "            <element Type=\"Port\" index=\"2\">\n" +
                                   "              <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                   "              <traceId>27</traceId>\n" +
                                   "            </element>\n" +
                                   "          </ports>\n" +
                                   "        </element>\n" +
                                   "        <element Type=\"AndGate\" index=\"3\">\n" +
                                   "          <inputCount>2</inputCount>\n" +
                                   "          <position Type=\"Int2D\" x=\"-11\" y=\"-14\"/>\n" +
                                   "          <rotation Type=\"Rotation\" rotation=\"North\"/>\n" +
                                   "          <name/>\n" +
                                   "          <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                   "          <ports Type=\"ArrayList\" size=\"3\">\n" +
                                   "            <element Type=\"Port\" index=\"0\">\n" +
                                   "              <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                   "              <traceId>24</traceId>\n" +
                                   "            </element>\n" +
                                   "            <element Type=\"Port\" index=\"1\">\n" +
                                   "              <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                   "              <traceId>27</traceId>\n" +
                                   "            </element>\n" +
                                   "            <element Type=\"Port\" index=\"2\">\n" +
                                   "              <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                   "              <traceId>36</traceId>\n" +
                                   "            </element>\n" +
                                   "          </ports>\n" +
                                   "        </element>\n" +
                                   "        <element Type=\"Buffer\" index=\"4\">\n" +
                                   "          <position Type=\"Int2D\" x=\"-8\" y=\"-3\"/>\n" +
                                   "          <rotation Type=\"Rotation\" rotation=\"North\"/>\n" +
                                   "          <name/>\n" +
                                   "          <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                   "          <ports Type=\"ArrayList\" size=\"2\">\n" +
                                   "            <element Type=\"Port\" index=\"0\">\n" +
                                   "              <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                   "              <traceId>35</traceId>\n" +
                                   "            </element>\n" +
                                   "            <element Type=\"Port\" index=\"1\">\n" +
                                   "              <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                   "              <traceId>29</traceId>\n" +
                                   "            </element>\n" +
                                   "          </ports>\n" +
                                   "        </element>\n" +
                                   "        <element Type=\"Buffer\" index=\"5\">\n" +
                                   "          <position Type=\"Int2D\" x=\"-14\" y=\"-3\"/>\n" +
                                   "          <rotation Type=\"Rotation\" rotation=\"North\"/>\n" +
                                   "          <name/>\n" +
                                   "          <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                   "          <ports Type=\"ArrayList\" size=\"2\">\n" +
                                   "            <element Type=\"Port\" index=\"0\">\n" +
                                   "              <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                   "              <traceId>35</traceId>\n" +
                                   "            </element>\n" +
                                   "            <element Type=\"Port\" index=\"1\">\n" +
                                   "              <events Type=\"ArrayList\" size=\"0\"/>\n" +
                                   "              <traceId>33</traceId>\n" +
                                   "            </element>\n" +
                                   "          </ports>\n" +
                                   "        </element>\n" +
                                   "      </integratedCircuits>\n" +
                                   "      <traces Type=\"ArrayList\" size=\"17\">\n" +
                                   "        <element Type=\"Trace\" index=\"0\">\n" +
                                   "          <id>35</id>\n" +
                                   "          <start Type=\"Int2D\" x=\"-10\" y=\"-4\"/>\n" +
                                   "          <end Type=\"Int2D\" x=\"-10\" y=\"-2\"/>\n" +
                                   "        </element>\n" +
                                   "        <element Type=\"Trace\" index=\"1\">\n" +
                                   "          <id>35</id>\n" +
                                   "          <start Type=\"Int2D\" x=\"-12\" y=\"-4\"/>\n" +
                                   "          <end Type=\"Int2D\" x=\"-12\" y=\"-2\"/>\n" +
                                   "        </element>\n" +
                                   "        <element Type=\"Trace\" index=\"2\">\n" +
                                   "          <id>35</id>\n" +
                                   "          <start Type=\"Int2D\" x=\"-12\" y=\"-2\"/>\n" +
                                   "          <end Type=\"Int2D\" x=\"-11\" y=\"-2\"/>\n" +
                                   "        </element>\n" +
                                   "        <element Type=\"Trace\" index=\"3\">\n" +
                                   "          <id>35</id>\n" +
                                   "          <start Type=\"Int2D\" x=\"-11\" y=\"-2\"/>\n" +
                                   "          <end Type=\"Int2D\" x=\"-10\" y=\"-2\"/>\n" +
                                   "        </element>\n" +
                                   "        <element Type=\"Trace\" index=\"4\">\n" +
                                   "          <id>24</id>\n" +
                                   "          <start Type=\"Int2D\" x=\"-12\" y=\"-13\"/>\n" +
                                   "          <end Type=\"Int2D\" x=\"-12\" y=\"-11\"/>\n" +
                                   "        </element>\n" +
                                   "        <element Type=\"Trace\" index=\"5\">\n" +
                                   "          <id>24</id>\n" +
                                   "          <start Type=\"Int2D\" x=\"-13\" y=\"-11\"/>\n" +
                                   "          <end Type=\"Int2D\" x=\"-12\" y=\"-11\"/>\n" +
                                   "        </element>\n" +
                                   "        <element Type=\"Trace\" index=\"6\">\n" +
                                   "          <id>24</id>\n" +
                                   "          <start Type=\"Int2D\" x=\"-13\" y=\"-11\"/>\n" +
                                   "          <end Type=\"Int2D\" x=\"-13\" y=\"-8\"/>\n" +
                                   "        </element>\n" +
                                   "        <element Type=\"Trace\" index=\"7\">\n" +
                                   "          <id>27</id>\n" +
                                   "          <start Type=\"Int2D\" x=\"-10\" y=\"-13\"/>\n" +
                                   "          <end Type=\"Int2D\" x=\"-10\" y=\"-11\"/>\n" +
                                   "        </element>\n" +
                                   "        <element Type=\"Trace\" index=\"8\">\n" +
                                   "          <id>27</id>\n" +
                                   "          <start Type=\"Int2D\" x=\"-10\" y=\"-11\"/>\n" +
                                   "          <end Type=\"Int2D\" x=\"-9\" y=\"-11\"/>\n" +
                                   "        </element>\n" +
                                   "        <element Type=\"Trace\" index=\"9\">\n" +
                                   "          <id>27</id>\n" +
                                   "          <start Type=\"Int2D\" x=\"-9\" y=\"-11\"/>\n" +
                                   "          <end Type=\"Int2D\" x=\"-9\" y=\"-9\"/>\n" +
                                   "        </element>\n" +
                                   "        <element Type=\"Trace\" index=\"10\">\n" +
                                   "          <id>35</id>\n" +
                                   "          <start Type=\"Int2D\" x=\"-8\" y=\"-2\"/>\n" +
                                   "          <end Type=\"Int2D\" x=\"-8\" y=\"-1\"/>\n" +
                                   "        </element>\n" +
                                   "        <element Type=\"Trace\" index=\"11\">\n" +
                                   "          <id>35</id>\n" +
                                   "          <start Type=\"Int2D\" x=\"-11\" y=\"-2\"/>\n" +
                                   "          <end Type=\"Int2D\" x=\"-11\" y=\"-1\"/>\n" +
                                   "        </element>\n" +
                                   "        <element Type=\"Trace\" index=\"12\">\n" +
                                   "          <id>35</id>\n" +
                                   "          <start Type=\"Int2D\" x=\"-11\" y=\"-1\"/>\n" +
                                   "          <end Type=\"Int2D\" x=\"-11\" y=\"1\"/>\n" +
                                   "        </element>\n" +
                                   "        <element Type=\"Trace\" index=\"13\">\n" +
                                   "          <id>35</id>\n" +
                                   "          <start Type=\"Int2D\" x=\"-11\" y=\"-1\"/>\n" +
                                   "          <end Type=\"Int2D\" x=\"-8\" y=\"-1\"/>\n" +
                                   "        </element>\n" +
                                   "        <element Type=\"Trace\" index=\"14\">\n" +
                                   "          <id>35</id>\n" +
                                   "          <start Type=\"Int2D\" x=\"-14\" y=\"-2\"/>\n" +
                                   "          <end Type=\"Int2D\" x=\"-14\" y=\"-1\"/>\n" +
                                   "        </element>\n" +
                                   "        <element Type=\"Trace\" index=\"15\">\n" +
                                   "          <id>35</id>\n" +
                                   "          <start Type=\"Int2D\" x=\"-14\" y=\"-1\"/>\n" +
                                   "          <end Type=\"Int2D\" x=\"-11\" y=\"-1\"/>\n" +
                                   "        </element>\n" +
                                   "        <element Type=\"Trace\" index=\"16\">\n" +
                                   "          <id>36</id>\n" +
                                   "          <start Type=\"Int2D\" x=\"-11\" y=\"-19\"/>\n" +
                                   "          <end Type=\"Int2D\" x=\"-11\" y=\"-16\"/>\n" +
                                   "        </element>\n" +
                                   "      </traces>\n" +
                                   "    </circuitData>\n" +
                                   "  </editorData>\n" +
                                   "</Logicim>\n";
}

