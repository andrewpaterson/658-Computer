package net.logisim;

import com.cburch.logisim.tools.AddTool;
import com.cburch.logisim.tools.Library;
import com.cburch.logisim.tools.Tool;
import net.integratedcircuits.toshiba.vhc393.VHC393;
import net.logisim.integratedcircuits.diodesinc.b3251.B3251Factory;
import net.logisim.integratedcircuits.maxim.ds1813.DS1813Factory;
import net.logisim.integratedcircuits.nexperia.hc4040.HC4040Factory;
import net.logisim.integratedcircuits.nexperia.hc590.HC590Factory;
import net.logisim.integratedcircuits.nexperia.hct193.HCT193Factory;
import net.logisim.integratedcircuits.nexperia.lsf0102.LSF0102Factory;
import net.logisim.integratedcircuits.nexperia.lsf0204.LSF0204Factory;
import net.logisim.integratedcircuits.nexperia.lv165.LV165Factory;
import net.logisim.integratedcircuits.nexperia.lvc125.LVC125Factory;
import net.logisim.integratedcircuits.nexperia.lvc126.LVC126Factory;
import net.logisim.integratedcircuits.nexperia.lvc138.LVC138Factory;
import net.logisim.integratedcircuits.nexperia.lvc139.LVC139Factory;
import net.logisim.integratedcircuits.nexperia.lvc157.LVC157Factory;
import net.logisim.integratedcircuits.nexperia.lvc161.LVC161Factory;
import net.logisim.integratedcircuits.nexperia.lvc162373.LVC162373Factory;
import net.logisim.integratedcircuits.nexperia.lvc16244.LVC16244Factory;
import net.logisim.integratedcircuits.nexperia.lvc163.LVC163Factory;
import net.logisim.integratedcircuits.nexperia.lvc16373.LVC16373Factory;
import net.logisim.integratedcircuits.nexperia.lvc164245.LVC164245Factory;
import net.logisim.integratedcircuits.nexperia.lvc257.LVC257Factory;
import net.logisim.integratedcircuits.nexperia.lvc273.LVC273Factory;
import net.logisim.integratedcircuits.nexperia.lvc373.LVC373Factory;
import net.logisim.integratedcircuits.nexperia.lvc4245.LVC4245Factory;
import net.logisim.integratedcircuits.nexperia.lvc541.LVC541Factory;
import net.logisim.integratedcircuits.nexperia.lvc573.LVC573Factory;
import net.logisim.integratedcircuits.nexperia.lvc574.LVC574Factory;
import net.logisim.integratedcircuits.nexperia.lvc595.LVC595Factory;
import net.logisim.integratedcircuits.nexperia.lvc74.LVC74Factory;
import net.logisim.integratedcircuits.renesas.idt7201.IDT7201Factory;
import net.logisim.integratedcircuits.renesas.qs3253.QS3253Factory;
import net.logisim.integratedcircuits.ti.f251.F251Factory;
import net.logisim.integratedcircuits.ti.f283.F283Factory;
import net.logisim.integratedcircuits.ti.f521.F521Factory;
import net.logisim.integratedcircuits.ti.hc147.HC147Factory;
import net.logisim.integratedcircuits.ti.hct40103.HCT40103Factory;
import net.logisim.integratedcircuits.ti.ls148.LS148Factory;
import net.logisim.integratedcircuits.ti.lvc543.LVC543Factory;
import net.logisim.integratedcircuits.toshiba.vhc161.VHC161Factory;
import net.logisim.integratedcircuits.toshiba.vhc238.VHC238Factory;
import net.logisim.integratedcircuits.toshiba.vhc393.VHC393Factory;
import net.logisim.integratedcircuits.wdc.w65c02.W65C02Factory;
import net.logisim.integratedcircuits.wdc.w65c816.W65C816Factory;
import net.logisim.integratedcircuits.wdc.w65c816.W65C816TimingFactory;

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
      tools.add(new AddTool(W65C816TimingFactory.create()));
      tools.add(new AddTool(new W65C02Factory()));
      tools.add(new AddTool(LVC74Factory.create()));
      tools.add(new AddTool(LVC273Factory.create()));
      tools.add(new AddTool(LVC373Factory.create()));
      tools.add(new AddTool(LVC573Factory.create()));
      tools.add(new AddTool(LVC574Factory.create()));
      tools.add(new AddTool(LVC543Factory.create()));
      tools.add(new AddTool(LVC16373Factory.create()));
      tools.add(new AddTool(LVC162373Factory.create()));
      tools.add(new AddTool(LSF0102Factory.create()));
      tools.add(new AddTool(LSF0204Factory.create()));
      tools.add(new AddTool(LVC4245Factory.create()));
      tools.add(new AddTool(LVC164245Factory.create()));
      tools.add(new AddTool(LVC125Factory.create()));
      tools.add(new AddTool(LVC126Factory.create()));
      tools.add(new AddTool(LVC541Factory.create()));
      tools.add(new AddTool(LVC16244Factory.create()));
      tools.add(new AddTool(QS3253Factory.create()));
      tools.add(new AddTool(B3251Factory.create()));
      tools.add(new AddTool(F251Factory.create()));
      tools.add(new AddTool(LVC139Factory.create()));
      tools.add(new AddTool(LVC138Factory.create()));
      tools.add(new AddTool(VHC238Factory.create()));
      tools.add(new AddTool(LS148Factory.create()));
      tools.add(new AddTool(HC147Factory.create()));
      tools.add(new AddTool(LVC157Factory.create()));
      tools.add(new AddTool(LVC257Factory.create()));
      tools.add(new AddTool(LVC161Factory.create()));
      tools.add(new AddTool(VHC161Factory.create()));
      tools.add(new AddTool(LVC163Factory.create()));
      tools.add(new AddTool(VHC393Factory.create()));
      tools.add(new AddTool(HC590Factory.create()));
      tools.add(new AddTool(HC4040Factory.create()));
      tools.add(new AddTool(HCT193Factory.create()));
      tools.add(new AddTool(HCT40103Factory.create()));
      tools.add(new AddTool(F283Factory.create()));
      tools.add(new AddTool(F521Factory.create()));
      tools.add(new AddTool(DS1813Factory.create()));
      tools.add(new AddTool(LV165Factory.create()));
      tools.add(new AddTool(LVC595Factory.create()));
      tools.add(new AddTool(IDT7201Factory.create()));
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

