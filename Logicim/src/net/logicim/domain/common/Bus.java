package net.logicim.domain.common;

import java.util.ArrayList;
import java.util.List;

public class Bus
{
  protected List<Trace> traces;

  public Bus(int width)
  {
    traces = new ArrayList<>(width);
    for (int i = 0; i < width; i++)
    {
      traces.add(new Trace());
    }
  }

  public Bus(Bus... busses)
  {
    int width = 0;
    for (Bus bus : busses)
    {
      width += bus.getWidth();
    }

    traces = new ArrayList<>(width);
    for (Bus bus : busses)
    {
      traces.addAll(bus.getTraces());
    }
  }

  public int getWidth()
  {
    return traces.size();
  }

  public Trace getTrace(int index)
  {
    return traces.get(index);
  }

  public List<Trace> getTraces()
  {
    return traces;
  }

  public String getStringValue()
  {
    StringBuilder stringBuilder = new StringBuilder();
    for (Trace connection : traces)
    {
      TraceValue traceValue = connection.getValue();

      char c = traceValue.getStringValue();
      stringBuilder.insert(0, c);
    }
    return stringBuilder.toString();
  }
}

