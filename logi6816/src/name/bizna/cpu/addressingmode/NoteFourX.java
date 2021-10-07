package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

import static name.bizna.util.IntUtil.getLowByte;

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

