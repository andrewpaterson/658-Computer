package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_PER
    extends OpCode
{
  public OpCode_PER(int mCode, InstructionCycles cycles)
  {
    super("PER", "Push Program Counter Relative Address", mCode, cycles);
  }
}

