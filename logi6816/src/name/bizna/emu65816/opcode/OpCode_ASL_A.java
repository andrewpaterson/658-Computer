package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_ASL_A
    extends OpCode
{
  public OpCode_ASL_A(int mCode, InstructionCycles busCycles)
  {
    super("ASL", "Shift accumulator left one bit; update NZC.", mCode, busCycles);
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    cpu.shiftLeftA();
  }
}

