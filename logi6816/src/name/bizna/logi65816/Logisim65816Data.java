package name.bizna.logi65816;

import com.cburch.logisim.instance.InstanceData;
import com.cburch.logisim.instance.InstanceState;
import name.bizna.emu65816.Cpu65816;

public class Logisim65816Data
    implements InstanceData,
               Cloneable
{
  protected Cpu65816 cpu;
  protected Logisim65816Factory parent;

  public Logisim65816Data(Logisim65816Factory parent, InstanceState state)
  {
    this.parent = parent;
    this.cpu = new Cpu65816(new Logisim65816Pins(state));
  }

  public static Logisim65816Data getOrCreateLogisim65816Data(InstanceState state, Logisim65816Factory factory)
  {
    Logisim65816Data ret = (Logisim65816Data) state.getData();
    if (ret == null)
    {
      ret = new Logisim65816Data(factory, state);
      state.setData(ret);
    }
    return ret;
  }

  @Override
  public Object clone()
  {
    try
    {
      return super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      return null;
    }
  }

  protected String getOpcodeValueHex()
  {
    if (cpu.getCycle() != 0)
    {
      int code = cpu.getOpCode().getCode();
      if (code >= 0 && code <= 255)
      {
        return getByteStringHex(code);
      }
      else
      {
        return "---";
      }
    }
    else
    {
      return "###";
    }
  }

  private String getByteStringHex(int value)
  {
    String s;
    if (value >= 0)
    {
      s = Integer.toHexString(value);
    }
    else
    {
      s = Integer.toHexString(0x100 + value);
    }
    if (s.length() < 2)
    {
      s = "0" + s;
    }
    s = "0x" + s;
    return s;
  }

  private String getWordStringHex(int value)
  {
    StringBuilder s;
    if (value >= 0)
    {
      s = new StringBuilder(Integer.toHexString(value));
    }
    else
    {
      s = new StringBuilder(Integer.toHexString(0x1000 + value));
    }
    while (s.length() < 4)
    {
      s.insert(0, "0");
    }
    s.insert(0, "0x");
    return s.toString();
  }

  protected String getAddressValueHex()
  {
    return getByteStringHex(cpu.getAddress().getBank()) + ":" + getWordStringHex(cpu.getAddress().getOffset());
  }

  public String getAccumulatorValueHex()
  {
    return getWordStringHex(cpu.getA());
  }

  public String getXValueHex()
  {
    return getWordStringHex(cpu.getX());
  }

  public String getYValueHex()
  {
    return getWordStringHex(cpu.getY());
  }

  public String getStackValueHex()
  {
    return getWordStringHex(cpu.getStackPointer());
  }

  public String getProgramCounterValueHex()
  {
    return getByteStringHex(cpu.getProgramCounter().getBank()) + ":" + getWordStringHex(cpu.getProgramCounter().getOffset());
  }

  public String getDataValueHex()
  {
    return getWordStringHex(cpu.getData16Bit());
  }

  public String getCycle()
  {
    return Integer.toString(cpu.getCycle());
  }

  protected String getOpcodeMnemonicString()
  {
    if (cpu.getCycle() == 0)
    {
      return "###";
    }
    else
    {
      return cpu.getOpCode().getName();
    }
  }

  protected boolean isOpcodeValid()
  {
    return cpu.getCycle() != 0;
  }

  public void propagate()
  {
    cpu.tick();
  }

  public Cpu65816 getCpu()
  {
    return cpu;
  }
}

