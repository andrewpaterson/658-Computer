package net.wdc65xx.wdc65816.instruction.address;

import net.wdc65xx.wdc65816.Cpu65816;

public class ProgramBank
    extends AddressOffset
{
  @Override
  public int getOffset(Cpu65816 cpu)
  {
    return 0;
  }

  @Override
  public int getBank(Cpu65816 cpu)
  {
    return cpu.getProgramCounter().getBank();
  }

  @Override
  public String toString()
  {
    return "PBR,";
  }
}
