package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_TDC
    extends OpCode
{
  public OpCode_TDC(int mCode, InstructionCycles cycles)
  {
    super("TDC", "Transfer Direct Register to C Accumulator", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.setA(cpu.getDirectPage());
    cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getA());
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}

