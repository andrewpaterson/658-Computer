package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_PHA
    extends OpCode
{
  public OpCode_PHA(int mCode, InstructionCycles cycles)
  {
    super("PHA", "Push Accumulator onto Stack", mCode, cycles);
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    if (cpu.isMemory8Bit())
    {
      cpu.setData(cpu.getA());
    }
  }

  @Override
  public void execute2(Cpu65816 cpu)
  {
    if (cpu.isMemory16Bit())
    {
      cpu.setData(cpu.getA());
    }
  }
}

