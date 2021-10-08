package name.bizna.bus.common;

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

  public int getWidth()
  {
    return traces.size();
  }

  public Trace getTrace(int index)
  {
    return traces.get(index);
  }
}

