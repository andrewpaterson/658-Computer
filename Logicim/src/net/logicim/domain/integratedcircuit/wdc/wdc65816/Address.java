package net.logicim.domain.integratedcircuit.wdc.wdc65816;

import net.common.SimulatorException;
import net.logicim.data.common.ReflectiveData;

import static net.common.util.IntUtil.*;
import static net.common.util.StringUtil.to16BitHex;
import static net.common.util.StringUtil.to8BitHex;

public class Address
    extends ReflectiveData
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
      throw new SimulatorException("Call toByte(bank) before creating an Address.");
    }
    if (offset > 0xFFFF)
    {
      throw new SimulatorException("Call toShort(offset) before creating an Address.");
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

  public Address offset(int offset)
  {
    if (offset != 0)
    {
      this.offset = toShort(this.offset + offset);
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

