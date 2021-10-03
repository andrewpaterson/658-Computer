package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_CLD
    extends OpCode
{
  public OpCode_CLD(int mCode, InstructionCycles cycles)
  {
    super("CLD", "Clear Decimal Mode", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.getCpuStatus().setDecimalFlag(false);
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}

