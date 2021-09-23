package name.bizna.emu65816;

import java.util.ArrayList;
import java.util.List;

import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class SystemBus
{
  private final List<SystemBusDevice> mDevices;

  public SystemBus()
  {
    mDevices = new ArrayList<>();
  }

  public void registerDevice(SystemBusDevice device)
  {
    mDevices.add(device);
  }

  public void storeByte(Address address, int byteValue)
  {
    for (SystemBusDevice device : mDevices)
    {
      Address decodedAddress = device.decodeAddress(address);
      if (decodedAddress != null)
      {
        device.storeByte(decodedAddress, byteValue);
        return;
      }
    }
  }

  public void storeTwoBytes(Address address, int value)
  {
    for (SystemBusDevice device : mDevices)
    {
      Address decodedAddress = device.decodeAddress(address);
      if (decodedAddress != null)
      {
        int leastSignificantByte = toByte(value);
        int mostSignificantByte = toByte((value & 0xFF00) >> 8);
        device.storeByte(decodedAddress, leastSignificantByte);
        decodedAddress.incrementOffsetBy(1);
        device.storeByte(decodedAddress, mostSignificantByte);
        return;
      }
    }
  }

  public int readByte(Address address)
  {
    for (SystemBusDevice device : mDevices)
    {
      Address decodedAddress = device.decodeAddress(address);
      if (decodedAddress != null)
      {
        return toByte(device.readByte(decodedAddress));
      }
    }
    return 0;
  }

  public int readTwoBytes(Address address)
  {
    for (SystemBusDevice device : mDevices)
    {
      Address decodedAddress = device.decodeAddress(address);
      if (decodedAddress != null)
      {
        int leastSignificantByte = toByte(device.readByte(decodedAddress));
        decodedAddress.incrementOffsetBy(Sizeof.sizeofByte);
        int mostSignificantByte = toByte(device.readByte(decodedAddress));
        return toShort((mostSignificantByte << 8) | leastSignificantByte);
      }
    }
    return 0;
  }

  public Address readAddressAt(Address address)
  {
    for (SystemBusDevice device : mDevices)
    {
      Address decodedAddress = device.decodeAddress(address);
      if (decodedAddress != null)
      {
        // Read offset
        int leastSignificantByte = toByte(device.readByte(decodedAddress));
        decodedAddress.incrementOffsetBy(Sizeof.sizeofByte);
        int mostSignificantByte = toByte(device.readByte(decodedAddress));
        int offset = toShort((mostSignificantByte << 8) | leastSignificantByte);
        // Read bank
        decodedAddress.incrementOffsetBy(Sizeof.sizeofByte);
        int bank = device.readByte(decodedAddress);
        return new Address(bank, offset);
      }
    }
    return new Address();
  }
}

