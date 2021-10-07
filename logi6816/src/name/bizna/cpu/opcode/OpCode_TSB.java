package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_TSB
    extends OpCode
{
  public OpCode_TSB(int mCode, InstructionCycles cycles)
  {
    super("TSB", "Test and Set Bit", mCode, cycles);
  }
}

