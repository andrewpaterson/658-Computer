package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_STA
    extends OpCode
{
  public OpCode_STA(int mCode, InstructionCycles cycles)
  {
    super("STA", "Store Accumulator in Memory", mCode, cycles);
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    cpu.setData(cpu.getA());
  }
}

