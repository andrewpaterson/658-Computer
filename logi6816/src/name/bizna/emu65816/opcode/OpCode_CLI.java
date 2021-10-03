package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_CLI
    extends OpCode
{
  public OpCode_CLI(int mCode, InstructionCycles cycles)
  {
    super("CLI", "Clear Interrupt Disable Bit", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.getCpuStatus().setInterruptDisableFlag(false);
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}

