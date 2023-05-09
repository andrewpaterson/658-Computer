package net.logicim.file.writer;

import net.logicim.data.common.KeyData;
import net.logicim.data.common.ValueData;
import net.logicim.data.passive.wire.TunnelProperties;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorState;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static net.logicim.assertions.Validator.validate;

public class ReflectiveWriterTest
{
  public static void testWriteInteger()
  {
    StringWriter writer = new StringWriter();
    LogicimFileWriter.writeXML(new ValueData(100), writer, "doc", "root");

    validate("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
             "<doc>\n" +
             "  <root type=\"ValueData\">\n" +
             "    <object type=\"Integer\" x=\"100\"/>\n" +
             "  </root>\n" +
             "</doc>\n", writer.toString().replace("\r", ""));
  }

  public static void testWriteClockState()
  {
    StringWriter writer = new StringWriter();
    LogicimFileWriter.writeXML(new ClockOscillatorState(true), writer, "doc", "root");

    validate("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
             "<doc>\n" +
             "  <root type=\"ClockOscillatorState\">\n" +
             "    <state>true</state>\n" +
             "  </root>\n" +
             "</doc>\n", writer.toString().replace("\r", ""));
  }

  public static void testWriteLongArray()
  {
    StringWriter writer = new StringWriter();
    LogicimFileWriter.writeXML(new ValueData(new long[]{2, 3}), writer, "doc", "root");

    validate("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
             "<doc>\n" +
             "  <root type=\"ValueData\">\n" +
             "    <object length=\"2\" type=\"long[]\">\n" +
             "2,3</object>\n" +
             "  </root>\n" +
             "</doc>\n", writer.toString().replace("\r", ""));
  }

  public static void testWriteList()
  {
    StringWriter writer = new StringWriter();
    ArrayList<Object> list = new ArrayList<>();
    list.add(new KeyData(new KeyData("Hello")));
    list.add(new KeyData(56f));
    LogicimFileWriter.writeXML(new ValueData(list), writer, "doc", "root");

    validate("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
             "<doc>\n" +
             "  <root type=\"ValueData\">\n" +
             "    <object size=\"2\" type=\"ArrayList\">\n" +
             "      <element index=\"0\" type=\"KeyData\">\n" +
             "        <object type=\"KeyData\">\n" +
             "          <object>Hello</object>\n" +
             "        </object>\n" +
             "      </element>\n" +
             "      <element index=\"1\" type=\"KeyData\">\n" +
             "        <object>56.0</object>\n" +
             "      </element>\n" +
             "    </object>\n" +
             "  </root>\n" +
             "</doc>\n", writer.toString().replace("\r", ""));
  }

  public static void testWriteMap()
  {
    StringWriter writer = new StringWriter();
    LinkedHashMap<Object, Object> map = new LinkedHashMap<>();
    map.put(91, "World");
    map.put("Tunnel", new TunnelProperties("Santa", true));
    LogicimFileWriter.writeXML(new ValueData(map), writer, "doc", "root");

    validate("", writer.toString().replace("\r", ""));
  }

  public static void test()
  {
//    testWriteInteger();
//    testWriteClockState();
//    testWriteLongArray();
//    testWriteList();
    testWriteMap();
  }
}

