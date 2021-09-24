package name.bizna.emu65816;

import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class Address
{
  public static final int BANK_SIZE_BYTES = 0x10000;
  public static final int PAGE_SIZE_BYTES = 0x100;

  protected int mBank;
  protected int mOffset;

  public Address()
  {
    mBank = 0x00;
    mOffset = 0x0000;
  }

  public Address(int mOffset)
  {
    mBank = 0x00;
    this.mOffset = toShort(mOffset);
  }

  public Address(int mBank, int mOffset)
  {
    this.mBank = toByte(mBank);
    this.mOffset = toShort(mOffset);
  }

  public int getBank()
  {
    return toByte(mBank);
  }

  public int getOffset()
  {
    return toShort(mOffset);
  }

  public static Address sumOffsetToAddressNoWrapAround(Address address, int offset)
  {
    int newBank = address.getBank();
    int newOffset;
    int offsetSum = address.getOffset() + offset;
    if (offsetSum >= BANK_SIZE_BYTES)
    {
      newBank++;
      newBank = toByte(newBank);
      newOffset = toShort((offsetSum - BANK_SIZE_BYTES));
    }
    else
    {
      newOffset = toShort(address.getOffset() + offset);
    }
    return new Address(newBank, newOffset);
  }

  public static Address sumOffsetToAddressWrapAround(Address address, int offset)
  {
    return new Address(address.getBank(), toShort(address.getOffset() + offset));
  }

  public static Address sumOffsetToAddress(Address address, int offset)
  {
    // This wraps around by default
    // TODO figure out when to wrap around and when not to
    return sumOffsetToAddressWrapAround(address, offset);
  }

  public static boolean offsetsAreOnDifferentPages(int offsetFirst, int offsetSecond)
  {
    int pageOfFirst = offsetFirst / PAGE_SIZE_BYTES;
    int pageOfSecond = offsetSecond / PAGE_SIZE_BYTES;
    return pageOfFirst != pageOfSecond;
  }

  public Address newWithOffset1()
  {
    return sumOffsetToAddress(this, 1);
  }

  public Address newWithOffsetNoWrapAround(int offset)
  {
    return sumOffsetToAddressNoWrapAround(this, offset);
  }

  public void decrementOffsetBy(int offset)
  {
    mOffset -= offset;
    mOffset = toShort(mOffset);
  }

  public void incrementOffsetBy(int offset)
  {
    mOffset += offset;
    mOffset = toShort(mOffset);
  }
}

