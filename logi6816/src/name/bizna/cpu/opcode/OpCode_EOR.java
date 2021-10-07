package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_EOR
    extends OpCode
{
  public OpCode_EOR(int mCode, InstructionCycles cycles)
  {
    super("EOR", "'Exclusive OR' Memory with Accumulator", mCode, cycles);
  }
}

