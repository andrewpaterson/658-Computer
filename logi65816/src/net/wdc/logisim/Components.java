package net.wdc.logisim;

import com.cburch.logisim.tools.AddTool;
import com.cburch.logisim.tools.Library;
import com.cburch.logisim.tools.Tool;
import net.wdc.logisim.wdc6502.Logisim6502Factory;
import net.wdc.logisim.wdc65816.Logisim65816Factory;

import java.util.ArrayList;
import java.util.List;

public class Components
    extends Library
{
  private final ArrayList<Tool> tools;

  public Components()
  {
    try
    {
      tools = new ArrayList<>();
      tools.add(new AddTool(new Logisim65816Factory()));
      tools.add(new AddTool(new Logisim6502Factory()));
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  public String getName()
  {
    return "WDC";
  }

  @Override
  public boolean removeLibrary(String name)
  {
    return false;
  }

  @Override
  public List<? extends Tool> getTools()
  {
    return tools;
  }
}

