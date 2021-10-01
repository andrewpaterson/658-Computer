package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_PHX
    extends OpCode
{
  public OpCode_PHX(int mCode, InstructionCycles cycles)
  {
    super("PHX", "Push Index X on Stack", mCode, cycles);
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    if (cpu.isIndex8Bit())
    {
      cpu.setData(cpu.getX());
    }
  }

  @Override
  public void execute2(Cpu65816 cpu)
  {
    if (cpu.isIndex16Bit())
    {
      cpu.setData(cpu.getX());
    }
  }
}

