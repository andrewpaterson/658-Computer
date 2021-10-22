package net.wdc.wdc65816.instruction.address;

import net.wdc.wdc65816.WDC65816;

public class ProgramBank
    extends AddressOffset
{
  @Override
  public int getOffset(WDC65816 cpu)
  {
    return 0;
  }

  @Override
  public int getBank(WDC65816 cpu)
  {
    return cpu.getProgramCounter().getBank();
  }

  @Override
  public String toString()
  {
    return "PBR,";
  }
}

