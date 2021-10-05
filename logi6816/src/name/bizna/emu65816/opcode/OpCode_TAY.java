package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_TAY
    extends OpCode
{
  public OpCode_TAY(int mCode, InstructionCycles cycles)
  {
    super("TAY", "Transfer Accumulator in Index Y", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if ((cpu.isMemory8Bit() && cpu.isIndex8Bit()) ||
        (cpu.isMemory16Bit() && cpu.isIndex8Bit()))
    {
      int lower8BitsOfA = Binary.getLowByte(cpu.getA());
      cpu.setY(Binary.setLowByte(cpu.getY(), lower8BitsOfA));
      cpu.getCpuStatus().setSignAndZeroFlagFrom8BitValue(lower8BitsOfA);
    }
    else
    {
      cpu.setY(cpu.getA());
      cpu.getCpuStatus().setSignAndZeroFlagFrom16BitValue(cpu.getA());
    }
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}

