package name.bizna.emu65816;

public class Address
{
  public static final int BANK_SIZE_BYTES = 0x10000;
  public static final int HALF_BANK_SIZE_BYTES = 0x8000;
  public static final int PAGE_SIZE_BYTES = 0x100;

  protected byte mBank;
  protected short mOffset;

  public Address()
  {
    mBank = 0x00;
    mOffset = 0x0000;
  }

  public Address(short mOffset)
  {
    mBank = 0x00;
    this.mOffset = mOffset;
  }

  public Address(byte mBank, short mOffset)
  {
    this.mBank = mBank;
    this.mOffset = mOffset;
  }

  public byte getBank()
  {
    return mBank;
  }

  public short getOffset()
  {
    return mOffset;
  }

  public static Address sumOffsetToAddressNoWrapAround(Address address, short offset)
  {
    byte newBank = address.getBank();
    short newOffset;
    int offsetSum = address.getOffset() + offset;
    if (offsetSum >= BANK_SIZE_BYTES)
    {
      ++newBank;
      newOffset = (short) (offsetSum - BANK_SIZE_BYTES);
    }
    else
    {
      newOffset = (short) (address.getOffset() + offset);
    }
    return new Address(newBank, newOffset);
  }

  public static Address sumOffsetToAddressWrapAround(Address address, short offset)
  {
    return new Address(address.getBank(), (short) (address.getOffset() + offset));
  }

  public static Address sumOffsetToAddress(Address address, short offset)
  {
    // This wraps around by default
    // TODO figure out when to wrap around and when not to
    return sumOffsetToAddressWrapAround(address, offset);
  }

  public static boolean offsetsAreOnDifferentPages(short offsetFirst, short offsetSecond)
  {
    int pageOfFirst = offsetFirst / PAGE_SIZE_BYTES;
    int pageOfSecond = offsetSecond / PAGE_SIZE_BYTES;
    return pageOfFirst != pageOfSecond;
  }

  public Address newWithOffset(short offset)
  {
    return sumOffsetToAddress(this, offset);
  }

  public Address newWithOffset1()
  {
    return sumOffsetToAddress(this, (short) 1);
  }

  public Address newWithOffsetNoWrapAround(short offset)
  {
    return sumOffsetToAddressNoWrapAround(this, offset);
  }

  public Address newWithOffsetWrapAround(short offset)
  {
    return sumOffsetToAddressWrapAround(this, offset);
  }

  void decrementOffsetBy(short offset)
  {
    mOffset -= offset;
  }

  void incrementOffsetBy(short offset)
  {
    mOffset += offset;
  }
}

