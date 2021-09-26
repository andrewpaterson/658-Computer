package name.bizna.emu65816;

import static java.lang.Integer.toHexString;
import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;
import static name.bizna.emu65816.util.StringUtil.rightJustify;

public class Address
{
  public static final int BANK_SIZE_BYTES = 0x10000;
  public static final int PAGE_SIZE_BYTES = 0x100;

  protected int bank;
  protected int offset;

  public Address()
  {
    bank = 0x00;
    offset = 0x0000;
  }

  public Address(int offset)
  {
    bank = 0x00;
    this.offset = toShort(offset);
  }

  public Address(int bank, int offset)
  {
    this.bank = toByte(bank);
    this.offset = toShort(offset);
  }

  public int getBank()
  {
    return toByte(bank);
  }

  public int getOffset()
  {
    return toShort(offset);
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
    this.offset -= offset;
    this.offset = toShort(this.offset);
  }

  public void incrementOffsetBy(int offset)
  {
    this.offset += offset;
    this.offset = toShort(this.offset);
  }

  @Override
  public String toString()
  {
    return rightJustify(toHexString(bank), 2, "0") + ":" + rightJustify(toHexString(offset), 4, "0");
  }

  public void setOffset(int offset)
  {
    this.offset = offset;
  }

  public void setBank(int bank)
  {
    this.bank = bank;
  }
}

