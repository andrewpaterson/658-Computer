package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;

import static net.util.IntUtil.getLowByte;

public class NoteFourX
    extends Operation
{
  private final boolean nextWillRead;

  public NoteFourX(boolean nextWillRead)
  {
    this.nextWillRead = nextWillRead;
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
  }

  @Override
  public boolean mustExecute(Cpu65816 cpu)
  {
    return (getLowByte(cpu.getAddress().getOffset()) + getLowByte(cpu.getX())) > 0xFF ||
           !nextWillRead ||
           cpu.isIndex16Bit();
  }

  @Override
  public String toString()
  {
    return "Note(4)";
  }
}

