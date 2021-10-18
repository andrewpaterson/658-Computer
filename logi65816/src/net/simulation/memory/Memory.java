package net.simulation.memory;

import net.simulation.common.*;
import net.simulation.gate.Tickable;
import net.util.EmulatorException;

import static net.util.IntUtil.toByte;

public class Memory
    extends Tickable
{
  protected final int size;
  protected final byte[] pvMemory;

  protected final Omniport addressBus;
  protected final Omniport dataBus;
  protected final Uniport writeEnableB;
  protected final Uniport outputEnableB;
  protected final Uniport chipEnableB;

  protected boolean propagateWroteMemory;
  protected long oldAddress;
  protected int oldValue;

  public Memory(Tickables tickables,
                String name,
                Bus addressBus,
                Bus dataBus,
                Trace writeEnableBTrace,
                Trace outputEnabledBTrace,
                Trace chipEnabledBTrace,
                byte[] bytes)
  {
    super(tickables, name);
    this.addressBus = new Omniport(this, "Address Bus", 16);
    this.dataBus = new Omniport(this, "Data Bus", 8);
    this.writeEnableB = new Uniport(this, "WEB");
    this.outputEnableB = new Uniport(this, "OEB");
    this.chipEnableB = new Uniport(this, "CEB");

    this.addressBus.connect(addressBus);
    this.dataBus.connect(dataBus);
    this.writeEnableB.connect(writeEnableBTrace);
    this.outputEnableB.connect(outputEnabledBTrace);
    this.chipEnableB.connect(chipEnabledBTrace);

    pvMemory = new byte[bytes.length];
    System.arraycopy(bytes, 0, pvMemory, 0, bytes.length);
    this.size = bytes.length;
    this.propagateWroteMemory = false;
  }

  @Override
  public void startPropagation()
  {
  }

  public void propagate()
  {
    propagateWroteMemory = false;

    TraceValue chipEnabledBValue = chipEnableB.read();
    if (chipEnabledBValue.isError())
    {
      dataBus.error();
    }
    else if (chipEnabledBValue.isUnsettled())
    {
      dataBus.unset();
    }
    else if (chipEnabledBValue.isHigh())
    {
      dataBus.highImpedance();
    }
    else if (chipEnabledBValue.isLow() || chipEnabledBValue.isNotConnected())
    {
      propagateWhenChipEnabled();
    }
  }

  private void propagateWhenChipEnabled()
  {
    TraceValue readState = writeEnableB.read();
    TraceValue addressState = addressBus.read();
    TraceValue outputEnabledBValue = outputEnableB.read();

    if (readState.isError() || addressState.isError() || readState.isNotConnected() || addressState.isNotConnected() || outputEnabledBValue.isError())
    {
      dataBus.error();
    }
    else if (readState.isUnsettled() || addressState.isUnsettled() || outputEnabledBValue.isUnsettled())
    {
      dataBus.unset();
    }
    else if (readState.isHigh())
    {
      if (outputEnabledBValue.isNotConnected() || outputEnabledBValue.isLow())
      {
        long address = addressBus.getPinsAsBoolAfterRead();
        int data = getMemory(address);
        dataBus.writeAllPinsBool(data);
      }
      else
      {
        dataBus.highImpedance();
      }
    }
    else if (readState.isLow())
    {
      TraceValue dataBusState = dataBus.read();
      if (dataBusState.isValid())
      {
        oldAddress = addressBus.getPinsAsBoolAfterRead();
        oldValue = getMemory(oldAddress);

        long data = dataBus.getPinsAsBoolAfterRead();
        setMemory(oldAddress, data);

        propagateWroteMemory = true;
      }
    }
    else
    {
      throw new EmulatorException(getDescription() + " connections are in an impossible state.");
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

  @Override
  public void donePropagation()
  {
  }

  @Override
  public String getType()
  {
    return "Async SRAM";
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

