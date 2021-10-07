package name.bizna.cpu;

import name.bizna.util.EmulatorException;

import static name.bizna.util.IntUtil.*;
import static name.bizna.util.StringUtil.to16BitHex;
import static name.bizna.util.StringUtil.to8BitHex;

public class Address
{
  public static final int BANK_SIZE_BYTES = 0x10000;
  public static final int PAGE_SIZE_BYTES = 0x100;

  protected int bank;
  protected int offset;

  public Address(Address address)
  {
    this(address.getBank(), address.getOffset());
  }

  public Address()
  {
    this(0x00, 0x0000);
  }

  public Address(int bank, int offset)
  {
    if (bank > 0xFF)
    {
      throw new EmulatorException("Call toByte(bank) before creating an Address.");
    }
    if (offset > 0xFFFF)
    {
      throw new EmulatorException("Call toShort(offset) before creating an Address.");
    }

    this.bank = toByte(bank);
    this.offset = toShort(offset);
  }

  public int getBank()
  {
    return bank;
  }

  public int getOffset()
  {
    return offset;
  }

  public static boolean areOffsetsAreOnDifferentPages(int offsetFirst, int offsetSecond)
  {
    int pageOfFirst = offsetFirst / PAGE_SIZE_BYTES;
    int pageOfSecond = offsetSecond / PAGE_SIZE_BYTES;
    return pageOfFirst != pageOfSecond;
  }

  public Address offset(int offset, boolean wrapOffset)
  {
    if (offset != 0)
    {
      if (wrapOffset)
      {
        this.offset = toShort(this.offset + offset);
      }
      else
      {
        int newOffset = this.offset + offset;
        if (newOffset >= BANK_SIZE_BYTES)
        {
          this.bank = toByte(bank + 1);
          this.offset = toShort((newOffset - BANK_SIZE_BYTES));  //This subtraction is probably unnecessary.
        }
        else
        {
          this.offset = toShort(this.offset + offset);
        }
      }
    }
    return this;
  }

  @Override
  public String toString()
  {
    return to8BitHex(bank) + ":" + to16BitHex(offset);
  }

  public void setOffset(int offset)
  {
    this.offset = offset;
  }

  public void setBank(int bank)
  {
    this.bank = bank;
  }

  public void setOffsetLow(int offsetLow)
  {
    offset = setLowByte(offset, offsetLow);
  }

  public void setOffsetHigh(int offsetHigh)
  {
    offset = setHighByte(offset, offsetHigh);
  }
}

