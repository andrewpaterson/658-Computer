package name.bizna.emu65816;

public class Stack
{
  public static final short STACK_POINTER_DEFAULT = 0x1FF;

  private SystemBus mSystemBus;
  private Address mStackAddress;

  public Stack(SystemBus mSystemBus)
  {
    this.mSystemBus = mSystemBus;
    this.mStackAddress = new Address((byte) 0, STACK_POINTER_DEFAULT);
  }

  public Stack(SystemBus mSystemBus, Address mStackAddress)
  {
    this.mSystemBus = mSystemBus;
    this.mStackAddress = mStackAddress;
  }

  public void push8Bit(byte value)
  {
    mSystemBus.storeByte(mStackAddress, value);
    mStackAddress.decrementOffsetBy((short) Sizeof.sizeofByte);
  }

  public void push16Bit(short value)
  {
    byte leastSignificant = (byte) ((value) & 0xFF);
    byte mostSignificant = (byte) (((value) & 0xFF00) >> 8);
    push8Bit(mostSignificant);
    push8Bit(leastSignificant);
  }

  public byte pull8Bit()
  {
    mStackAddress.incrementOffsetBy((short) Sizeof.sizeofByte);
    return mSystemBus.readByte(mStackAddress);
  }

  public short pull16Bit()
  {
    return (short) (pull8Bit() | (((short) pull8Bit()) << 8));
  }

  public short getStackPointer()
  {
    return mStackAddress.getOffset();
  }
}
