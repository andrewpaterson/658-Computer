package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Unsigned.toByte;

public class OpCode_PLA
    extends OpCode
{
  public OpCode_PLA(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    if (cpu.accumulatorIs8BitWide())
    {
      cpu.setA(Binary.setLower8BitsOf16BitsValue(cpu.getA(), cpu.pull8Bit()));
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(toByte(cpu.getA()));
      cpu.addToProgramAddressAndCycles(1, 4);
    }
    else
    {
      cpu.setA(cpu.pull16Bit());
      cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getA());
      cpu.addToProgramAddressAndCycles(1, 5);
    }
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

