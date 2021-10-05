package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_TAX
    extends OpCode
{
  public OpCode_TAX(int mCode, InstructionCycles cycles)
  {
    super("TAX", "Transfer Accumulator in Index X", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if ((cpu.isMemory8Bit() && cpu.isIndex8Bit()) ||
        (cpu.isMemory16Bit() && cpu.isIndex8Bit()))
    {
      int lower8BitsOfA = Binary.getLowByte(cpu.getA());
      cpu.setX(Binary.setLowByte(cpu.getX(), lower8BitsOfA));
      cpu.getCpuStatus().setSignAndZeroFlagFrom8BitValue(lower8BitsOfA);
    }
    else
    {
      cpu.setX(cpu.getA());
      cpu.getCpuStatus().setSignAndZeroFlagFrom16BitValue(cpu.getA());
    }
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}

