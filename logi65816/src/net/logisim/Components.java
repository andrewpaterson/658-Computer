package net.logisim;

import com.cburch.logisim.tools.AddTool;
import com.cburch.logisim.tools.Library;
import com.cburch.logisim.tools.Tool;
import net.nexperia.logisim.LVC4245Factory;
import net.wdc.logisim.wdc6502.Logisim6502Factory;
import net.wdc.logisim.wdc65816.WDC65816LogisimFactory;

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
      tools.add(new AddTool(new WDC65816LogisimFactory()));
      tools.add(new AddTool(new Logisim6502Factory()));
      tools.add(new AddTool(new LVC4245Factory()));
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

