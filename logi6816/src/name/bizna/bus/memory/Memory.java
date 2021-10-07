package name.bizna.bus.memory;

import name.bizna.cpu.Address;

import java.util.Arrays;

import static name.bizna.util.IntUtil.toByte;

public class Memory
{
  public static final int KB = 1024;

  protected int size;
  protected byte[] pvMemory;

  public Memory(int size)
  {
    pvMemory = new byte[size];
    Arrays.fill(pvMemory, (byte) 0xea);
    this.size = size;
  }

  public Memory(byte[] bytes)
  {
    pvMemory = new byte[bytes.length];
    System.arraycopy(bytes, 0, pvMemory, 0, bytes.length);
    this.size = bytes.length;
  }

  public void storeByte(Address address, int byteValue)
  {
    int bank = address.getBank();
    int offset = address.getOffset();

    int i = bank * 64 * KB + offset;
    pvMemory[i] = (byte) toByte(byteValue);
  }

  public int readByte(Address address)
  {
    int bank = address.getBank();
    int offset = address.getOffset();

    int i = bank * 64 * KB + offset;
    return toByte(pvMemory[i]);
  }

  public void writeByte(Address address, int data)
  {
    int bank = address.getBank();
    int offset = address.getOffset();

    int i = bank * 64 * KB + offset;

    pvMemory[i] = (byte) toByte(data);
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

