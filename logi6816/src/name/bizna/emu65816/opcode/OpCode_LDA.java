package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_LDA
    extends OpCode
{
  public OpCode_LDA(int mCode, InstructionCycles busCycles)
  {
    super("LDA", "Load Accumulator with Memory", mCode, busCycles);
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

