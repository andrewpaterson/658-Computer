package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

import static name.bizna.util.IntUtil.toByte;

public class NoteTwo
    extends Operation
{
  @Override
  public boolean mustExecute(Cpu65816 cpu)
  {
    return toByte(cpu.getDirectPage()) == 0;
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
  }

  @Override
  public String toString()
  {
    return "Note(2)";
  }
}

