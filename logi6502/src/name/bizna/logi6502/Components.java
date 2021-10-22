package name.bizna.logi6502;

import com.cburch.logisim.tools.AddTool;
import com.cburch.logisim.tools.Library;
import com.cburch.logisim.tools.Tool;

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

