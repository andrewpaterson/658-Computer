package net.wdc65xx.wdc65816.instruction.address;

import net.wdc65xx.wdc65816.WDC65C816;

public class ProgramBank
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
    return cpu.getProgramCounter().getBank();
  }

  @Override
  public String toString()
  {
    return "PBR,";
  }
}

