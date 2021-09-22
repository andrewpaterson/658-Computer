package name.bizna.emu65816;

import java.util.ArrayList;
import java.util.List;

public class SystemBus
{
  private List<SystemBusDevice> mDevices;

  public SystemBus()
  {
    mDevices = new ArrayList<>();
  }

  public void registerDevice(SystemBusDevice device)
  {
    mDevices.add(device);
  }

  public void storeByte(Address address, byte value)
  {
    for (SystemBusDevice device : mDevices)
    {
      Address decodedAddress = device.decodeAddress(address);
      if (decodedAddress != null)
      {
        device.storeByte(decodedAddress, value);
        return;
      }
    }
  }

  public void storeTwoBytes(Address address, short value)
  {
    for (SystemBusDevice device : mDevices)
    {
      Address decodedAddress = device.decodeAddress(address);
      if (decodedAddress != null)
      {
        byte leastSignificantByte = (byte) (value & 0xFF);
        byte mostSignificantByte = (byte) ((value & 0xFF00) >> 8);
        device.storeByte(decodedAddress, leastSignificantByte);
        decodedAddress.incrementOffsetBy((short) 1);
        device.storeByte(decodedAddress, mostSignificantByte);
        return;
      }
    }
  }

  public byte readByte(Address address)
  {
    for (SystemBusDevice device : mDevices)
    {
      Address decodedAddress = device.decodeAddress(address);
      if (decodedAddress != null)
      {
        return device.readByte(decodedAddress);
      }
    }
    return 0;
  }

  public short readTwoBytes(Address address)
  {
    for (SystemBusDevice device : mDevices)
    {
      Address decodedAddress = device.decodeAddress(address);
      if (decodedAddress != null)
      {
        byte leastSignificantByte = device.readByte(decodedAddress);
        decodedAddress.incrementOffsetBy((short) Sizeof.sizeofByte);
        byte mostSignificantByte = device.readByte(decodedAddress);
        short value = (short) (((short) mostSignificantByte << 8) | leastSignificantByte);
        return value;
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
        byte leastSignificantByte = device.readByte(decodedAddress);
        decodedAddress.incrementOffsetBy((short) Sizeof.sizeofByte);
        byte mostSignificantByte = device.readByte(decodedAddress);
        short offset = (short) (((short) mostSignificantByte << 8) | leastSignificantByte);
        // Read bank
        decodedAddress.incrementOffsetBy((short) Sizeof.sizeofByte);
        byte bank = device.readByte(decodedAddress);
        return new Address(bank, offset);
      }
    }
    return new Address();
  }
}

