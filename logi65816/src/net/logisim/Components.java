package net.logisim;

import com.cburch.logisim.tools.AddTool;
import com.cburch.logisim.tools.Library;
import com.cburch.logisim.tools.Tool;
import net.logisim.integratedcircuits.maxim.ds1813.DS1813Factory;
import net.logisim.integratedcircuits.nexperia.lvc138.LVC138Factory;
import net.logisim.integratedcircuits.nexperia.lvc161.LVC161Factory;
import net.logisim.integratedcircuits.nexperia.lvc16244.LVC16244Factory;
import net.logisim.integratedcircuits.nexperia.lvc163.LVC163Factory;
import net.logisim.integratedcircuits.nexperia.lvc16373.LVC16373Factory;
import net.logisim.integratedcircuits.nexperia.lvc164245.LVC164245Factory;
import net.logisim.integratedcircuits.nexperia.lvc4245.LVC4245Factory;
import net.logisim.integratedcircuits.nexperia.lvc573.LVC573Factory;
import net.logisim.integratedcircuits.ti.f521.F521Factory;
import net.logisim.integratedcircuits.ti.ls148.LS148Factory;
import net.logisim.integratedcircuits.ti.lvc543.LVC543Factory;
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
      tools.add(new AddTool(W65C816Factory.create()));
      tools.add(new AddTool(new W65C02Factory()));
      tools.add(new AddTool(LVC573Factory.create()));
      tools.add(new AddTool(LVC16373Factory.create()));
      tools.add(new AddTool(LVC543Factory.create()));
      tools.add(new AddTool(LVC4245Factory.create()));
      tools.add(new AddTool(LVC164245Factory.create()));
      tools.add(new AddTool(LVC16244Factory.create()));
      tools.add(new AddTool(LVC138Factory.create()));
      tools.add(new AddTool(LS148Factory.create()));
      tools.add(new AddTool(LVC161Factory.create()));
      tools.add(new AddTool(LVC163Factory.create()));
      tools.add(new AddTool(F521Factory.create()));
      tools.add(new AddTool(DS1813Factory.create()));
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

