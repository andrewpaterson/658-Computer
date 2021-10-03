package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_TSC
    extends OpCode
{
  public OpCode_TSC(int mCode, InstructionCycles cycles)
  {
    super("TSC", "Transfer Stack Pointer to C Accumulator", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.setA(cpu.getStackPointer());
    cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getA());
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}

