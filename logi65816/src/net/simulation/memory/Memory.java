package net.simulation.memory;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.util.EmulatorException;

import static net.util.IntUtil.toByte;

public class Memory
    extends IntegratedCircuit<MemorySnapshot, MemoryTickablePins>
{
  protected final int size;
  protected final byte[] pvMemory;

  public Memory(String name, MemoryTickablePins pins, byte[] bytes)
  {
    super(name, pins);

    pvMemory = new byte[bytes.length];
    System.arraycopy(bytes, 0, pvMemory, 0, bytes.length);
    this.size = bytes.length;
  }

  @Override
  public MemorySnapshot createSnapshot()
  {
    return new MemorySnapshot();
  }

  private void propagateWhenChipEnabled()
  {
    PinValue readState = getPins().getWriteEnableB();
    BusValue address = getPins().getAddress();
    PinValue outputEnabledBValue = getPins().getOutputEnableB();

    if (readState.isError() || address.isError() || readState.isNotConnected() || address.isNotConnected() || outputEnabledBValue.isError())
    {
      getPins().setDataError();
    }
    else if (readState.isUnknown() || address.isUnknown() || outputEnabledBValue.isUnknown())
    {
      getPins().setDataUnsettled();
    }
    else if (readState.isHigh())
    {
      if (outputEnabledBValue.isNotConnected() || outputEnabledBValue.isLow())
      {
        long data = getMemory(address.getValue());
        getPins().setDataValue(data);
      }
      else
      {
        getPins().setDataHighImpedance();
      }
    }
    else if (readState.isLow())
    {
      BusValue data = getPins().getData();
      if (data.isValid())
      {
        long oldAddress = address.getValue();
        long oldValue = getMemory(oldAddress);

        setMemory(oldAddress, data.value);

        getPins().updateSnapshot(oldAddress, oldValue);
      }
    }
    else
    {
      throw new EmulatorException(getDescription() + " connections are in an impossible state.");
    }
  }

  @Override
  public void restoreFromSnapshot(MemorySnapshot snapshot)
  {
    if (snapshot != null)
    {
      if (snapshot.propagateWroteMemory)
      {
        setMemory(snapshot.oldAddress, snapshot.oldValue);
      }
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

  public long getMemory(long address)
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

  @Override
  public void tick()
  {
    getPins().updateSnapshot();

    PinValue chipEnabledBValue = getPins().getChipEnableB();
    if (chipEnabledBValue.isError())
    {
      getPins().setDataError();
    }
    else if (chipEnabledBValue.isUnknown())
    {
      getPins().setDataUnsettled();
    }
    else if (chipEnabledBValue.isHigh())
    {
      getPins().setDataHighImpedance();
    }
    else if (chipEnabledBValue.isLow() || chipEnabledBValue.isNotConnected())
    {
      propagateWhenChipEnabled();
    }
  }

  @Override
  public String getType()
  {
    return "Memory";
  }
}

