package name.bizna.emu65816;

import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class Stack
{
  public static final short STACK_POINTER_DEFAULT = 0x1FF;

  private final SystemBus mSystemBus;
  private final Address mStackAddress;

  public Stack(SystemBus mSystemBus)
  {
    this.mSystemBus = mSystemBus;
    this.mStackAddress = new Address(0, STACK_POINTER_DEFAULT);
  }

  public Stack(SystemBus mSystemBus, Address mStackAddress)
  {
    this.mSystemBus = mSystemBus;
    this.mStackAddress = mStackAddress;
  }

  public void push8Bit(int value)
  {
    mSystemBus.storeByte(mStackAddress, value);
    mStackAddress.decrementOffsetBy(Sizeof.sizeofByte);
  }

  public void push16Bit(int value)
  {
    int leastSignificant = toByte((value) & 0xFF);
    int mostSignificant = toByte(((value) & 0xFF00) >> 8);
    push8Bit(mostSignificant);
    push8Bit(leastSignificant);
  }

  public int pull8Bit(Cpu65816 cpu)
  {
    mStackAddress.incrementOffsetBy(Sizeof.sizeofByte);
    return cpu.readByte(mStackAddress);
  }

  public int pull16Bit(Cpu65816 cpu)
  {
    return toShort((pull8Bit(cpu) | ((pull8Bit(cpu)) << 8)));
  }

  public int getStackPointer()
  {
    return mStackAddress.getOffset();
  }
}

