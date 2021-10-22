package net.wdc.wdc65816.instruction.address;

import net.wdc.wdc65816.WDC65C816;

public class DataBank
    extends AddressOffset
{
  @Override
  public int getOffset(WDC65C816 cpu)
  {
    return 0;
  }

  @Override
  public int getBank(WDC65C816 cpu)
  {
    return cpu.getDataBank();
  }

  @Override
  public String toString()
  {
    return "DBR,";
  }
}

