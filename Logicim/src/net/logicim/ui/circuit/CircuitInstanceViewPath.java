package net.logicim.ui.circuit;

import net.logicim.common.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class CircuitInstanceViewPath
{
  protected List<CircuitInstanceView> path;

  public CircuitInstanceViewPath(List<CircuitInstanceView> path)
  {
    this.path = new ArrayList<>(path);
  }

  @Override
  public String toString()
  {
    List<String> strings = new ArrayList<>(path.size());
    for (CircuitInstanceView circuitInstanceView : path)
    {
      String s = circuitInstanceView.toString();
      strings.add(s);
    }
    return StringUtil.separateList(strings, ".");
  }
}

