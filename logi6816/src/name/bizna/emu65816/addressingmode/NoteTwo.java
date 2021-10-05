package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Unsigned.toByte;

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
}

