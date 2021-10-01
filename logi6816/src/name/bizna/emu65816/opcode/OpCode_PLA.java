package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_PLA
    extends OpCode
{
  public OpCode_PLA(int mCode, InstructionCycles cycles)
  {
    super("PLA", "Pull Accumulator from Stack", mCode, cycles);
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    if (cpu.isMemory8Bit())
    {
      cpu.setA(cpu.getData());
    }
  }

  @Override
  public void execute2(Cpu65816 cpu)
  {
    if (cpu.isMemory16Bit())
    {
      cpu.setA(cpu.getData());
    }
  }
}

