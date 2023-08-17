package net.logicim.ui.circuit;

import java.util.ArrayList;
import java.util.List;

public class CircuitInstanceViewPath
{
  protected List<CircuitInstanceView> path;

  public CircuitInstanceViewPath(List<CircuitInstanceView> path)
  {
    this.path = new ArrayList<>(path);
  }

  public boolean equalsPath(List<CircuitInstanceView> path)
  {
    if (this.path.size() != path.size())
    {
      return false;
    }
    else
    {
      for (int i = 0; i < path.size(); i++)
      {
        if (this.path.get(i) != path.get(i))
        {
          return false;
        }
      }
      return true;
    }
  }

  public List<CircuitInstanceView> getPath()
  {
    return path;
  }
}

