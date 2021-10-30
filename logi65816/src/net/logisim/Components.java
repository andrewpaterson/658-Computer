package net.logisim;

import com.cburch.logisim.tools.AddTool;
import com.cburch.logisim.tools.Library;
import com.cburch.logisim.tools.Tool;
import net.logisim.integratedcircuits.maxim.ds1813.DS1813Factory;
import net.logisim.integratedcircuits.nexperia.lvc16373.LVC16373Factory;
import net.logisim.integratedcircuits.nexperia.lvc4245.LVC4245Factory;
import net.logisim.integratedcircuits.nexperia.lvc573.LVC573Factory;
import net.logisim.integratedcircuits.wdc.w65c02.W65C02Factory;
import net.logisim.integratedcircuits.wdc.w65c816.W65C816Factory;

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

