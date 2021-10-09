package name.bizna.bus.memory;

import name.bizna.bus.common.*;
import name.bizna.bus.logic.Tickable;
import name.bizna.util.EmulatorException;

import static name.bizna.util.IntUtil.toByte;

public class Memory
    extends Tickable
{
  protected final int size;
  protected final byte[] pvMemory;

  private final Omniport addressBus;
  private final Omniport dataBus;
  private final Uniport rwb;

  private boolean propagateWroteMemory;
  private long oldAddress;
  private int oldValue;

  public Memory(Tickables tickables,
                Bus addressBus,
                Bus dataBus,
                Trace rwb,
                byte[] bytes)
  {
    super(tickables);
    this.addressBus = new Omniport(this, 16);
    this.dataBus = new Omniport(this, 8);
    this.rwb = new Uniport(this);

    this.addressBus.connect(addressBus);
    this.dataBus.connect(dataBus);
    this.rwb.connect(rwb);

    pvMemory = new byte[bytes.length];
    System.arraycopy(bytes, 0, pvMemory, 0, bytes.length);
    this.size = bytes.length;
    this.propagateWroteMemory = false;
  }

  public void propagate()
  {
    propagateWroteMemory = false;

    TraceValue readState = rwb.readState();
    TraceValue addressState = addressBus.readStates();

    if (readState.isInvalid() || addressState.isError())
    {
      dataBus.writeAllPinsError();
    }
    else if (readState.isUndefined() || addressState.isUndefined())
    {
      dataBus.writeAllPinsUndefined();
    }
    else if (readState.isHigh())
    {
      long address = addressBus.getPinsAsBoolAfterRead();
      int data = getMemory(address);
      dataBus.writeAllPinsBool(data);
    }
    else if (readState.isLow())
    {
      TraceValue dataBusState = dataBus.readStates();
      if (dataBusState.isValid())
      {
        oldAddress = addressBus.getPinsAsBoolAfterRead();
        oldValue = getMemory(oldAddress);

        long data = dataBus.getPinsAsBoolAfterRead();
        setMemory(oldAddress, data);

        propagateWroteMemory = true;
      }
    }
  }

  @Override
  public void undoPropagation()
  {
    if (propagateWroteMemory)
    {
      setMemory(oldAddress, oldValue);
    }
  }

  public void setMemory(long address, long value)
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

  public int getMemory(long address)
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

