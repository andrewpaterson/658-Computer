package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;

public class WriteProcessorStatus
    extends DataOperation
{
  public WriteProcessorStatus()
  {
    super(false, true, true, false, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.getPins().setData(cpu.getProcessorRegisterValue());
  }

  @Override
  public String toString()
  {
    return "Write(P)";
  }
}

