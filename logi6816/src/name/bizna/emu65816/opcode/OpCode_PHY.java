package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_PHY
    extends OpCode
{
  public OpCode_PHY(int mCode, InstructionCycles cycles)
  {
    super("PHY", "Push Index Y on Stack", mCode, cycles);
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    if (cpu.isIndex8Bit())
    {
      cpu.setData(cpu.getY());
    }
  }

  @Override
  public void execute2(Cpu65816 cpu)
  {
    if (cpu.isIndex16Bit())
    {
      cpu.setData(cpu.getY());
    }
  }
}

