package net.logisim;

import com.cburch.logisim.tools.AddTool;
import com.cburch.logisim.tools.Library;
import com.cburch.logisim.tools.Tool;
import net.integratedcircuits.maxim.logisim.ds1813.DS1813Factory;
import net.integratedcircuits.nexperia.logisim.lvc16373.LVC16373Factory;
import net.integratedcircuits.nexperia.logisim.lvc4245.LVC4245Factory;
import net.integratedcircuits.nexperia.logisim.lvc573.LVC573Factory;
import net.integratedcircuits.wdc.logisim.wdc6502.W65C02Factory;
import net.integratedcircuits.wdc.logisim.wdc65816.W65C816Factory;

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
      tools.add(new AddTool(new W65C816Factory()));
      tools.add(new AddTool(new W65C02Factory()));
      tools.add(new AddTool(new LVC573Factory()));
      tools.add(new AddTool(new LVC16373Factory()));
      tools.add(new AddTool(new LVC4245Factory()));
      tools.add(new AddTool(new DS1813Factory()));
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

