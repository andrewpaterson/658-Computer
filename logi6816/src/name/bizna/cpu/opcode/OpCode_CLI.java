package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_CLI
    extends OpCode
{
  public OpCode_CLI(int mCode, InstructionCycles cycles)
  {
    super("CLI", "Clear Interrupt Disable Bit", mCode, cycles);
  }
}

