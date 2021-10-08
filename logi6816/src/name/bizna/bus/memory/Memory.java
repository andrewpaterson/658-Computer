package name.bizna.bus.memory;

import name.bizna.bus.common.Omniport;
import name.bizna.bus.common.Port;
import name.bizna.bus.logic.Tickable;
import name.bizna.util.EmulatorException;

import static name.bizna.util.IntUtil.toByte;

public class Memory
    implements Tickable
{
  protected int size;
  protected byte[] pvMemory;

  private final Omniport addressBus;
  private final Omniport dataBus;
  private final Port rwbTrace;

  private int outValue;

  public Memory(Omniport addressBus,
                Omniport dataBus,
                Port rwbTrace,
                byte[] bytes)
  {
    this.addressBus = addressBus;
    this.dataBus = dataBus;
    this.rwbTrace = rwbTrace;

    pvMemory = new byte[bytes.length];
    System.arraycopy(bytes, 0, pvMemory, 0, bytes.length);
    this.size = bytes.length;
  }

  public boolean propagate()
  {
    if (rwbTrace.get())
    {
      int newOutValue = read(addressBus.get());
      dataBus.set(newOutValue);

      boolean settled = newOutValue == outValue;
      this.outValue = newOutValue;
      return settled;
    }
    else
    {
      write(addressBus.get(), dataBus.get());
    }
    return true;
  }

  public void write(long address, long value)
  {
    if (address >= 0 && address < size)
    {
      if (value >= 0 && value <= 0xFF)
      {
        pvMemory[(int) address] = (byte) value;
      }
      else
      {
        throw new EmulatorException("Cannot write value > 0xFF.");
      }
    }
    else
    {
      throw new EmulatorException("Cannot write outside of memory.");
    }
  }

  public int read(long address)
  {
    if (address >= 0 && address < size)
    {
      return toByte(pvMemory[(int) address]);
    }
    else
    {
      throw new EmulatorException("Cannot read outside of memory.");
    }
  }

  byte charToHex(char c)
  {
    if (c >= '0' && c <= '9')
    {
      return (byte) (c - '0');
    }
    if (c >= 'A' && c <= 'F')
    {
      return (byte) (c - 'A' + 0x0A);
    }
    return (byte) 0xff;
  }

  public void set(int address, String szBytes)
  {
    int length = szBytes.length();
    if (length % 2 == 1)
    {
      return;
    }

    for (int i = 0; i < length; i += 2, address++)
    {
      char hi = szBytes.charAt(i);
      char lo = szBytes.charAt(i + 1);

      byte value = (byte) (charToHex(hi) * 0x10 + charToHex(lo));
      pvMemory[address] = value;
    }
  }

  public byte[] get(int start, int length)
  {
    byte[] bytes = new byte[length];
    System.arraycopy(pvMemory, start, bytes, 0, length);
    return bytes;
  }
}

