package name.bizna.logi6502;

import com.cburch.logisim.instance.InstanceData;
import com.cburch.logisim.instance.InstanceState;

public class W65C02CoreState
    extends W65C02
    implements InstanceData,
               Cloneable
{
  public W65C02CoreState(Logi6502 parent)
  {
    super(parent);
  }

  public static W65C02CoreState get(InstanceState state, Logi6502 parent)
  {
    W65C02CoreState ret = (W65C02CoreState) state.getData();
    if (ret == null)
    {
      ret = new W65C02CoreState(parent);
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
    if (cycle != 0)
    {
      return getByteStringHex(fetchedOpcode);
    }
    else
    {
      return "###";
    }
  }

  private String getByteStringHex(byte value)
  {
    String s;
    if (value >= 0)
    {
      s = Integer.toHexString(value);
    }
    else
    {
      s = Integer.toHexString(0x100 + ((int) value));
    }
    if (s.length() < 2)
    {
      s = "0" + s;
    }
    s = "0x" + s;
    return s;
  }

  private String getWordStringHex(short value)
  {
    StringBuilder s;
    if (value >= 0)
    {
      s = new StringBuilder(Integer.toHexString(value));
    }
    else
    {
      s = new StringBuilder(Integer.toHexString(0x1000 + ((int) value)));
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
    return getWordStringHex(address);
  }

  public String getAccumulatorValueHex()
  {
    return getByteStringHex(accumulator);
  }

  public String getXValueHex()
  {
    return getByteStringHex(xIndex);
  }

  public String getYValueHex()
  {
    return getByteStringHex(yIndex);
  }

  public String getStackValueHex()
  {
    return getByteStringHex(stack);
  }

  public String getProgramCounterValueHex()
  {
    return getWordStringHex(programCounter);
  }

  public String getDataValueHex()
  {
    return getByteStringHex(data);
  }

  public boolean isProcessorStatus(byte statusBit)
  {
    return (processorStatus & statusBit) != 0;
  }

  public boolean isAddressValid()
  {
    return intendedAddress == address;
  }

  public boolean isDataValid()
  {
    return intendedData == data;
  }

  public String getCycle()
  {
    return Byte.toString(cycle);
  }

  protected String getOpcodeMnemonicString()
  {
    if (cycle == 0)
    {
      return "###";
    }
    else
    {
      return getOpcodeMnemonic();
    }
  }

  protected boolean isOpcodeValid()
  {
    return cycle != 0;
  }
}

