package name.bizna.emu65816;

public abstract class SystemBusDevice
{
  public abstract void storeByte(Address address, byte value);

  /**
   * Reads one byte from the real address represented by the specified virtual address.
   * That is: maps the virtual address to the real one and reads from it.
   */
  public abstract byte readByte(Address address);

  /**
   * Returns true if the address was decoded successfully by this device.
   */
  public abstract Address decodeAddress(Address address);
}

