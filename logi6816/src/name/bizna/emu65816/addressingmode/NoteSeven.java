package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public class NoteSeven
    extends Operation
{
  @Override
  public void execute(Cpu65816 cpu)
  {
  }

  @Override
  public boolean mustExecute(Cpu65816 cpu)
  {
    return !cpu.isEmulationMode();
  }

  @Override
  public String toString()
  {
    return "Note(7)";
  }
}

